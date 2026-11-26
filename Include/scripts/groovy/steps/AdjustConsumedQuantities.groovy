package steps

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import io.cucumber.java.en.Then
import io.cucumber.java.en.When

/**
 * SMAR-60 - the "Quantidades consumidas" dialog shown before a meal is finished.
 *
 * Tapping "Concluir refeição" on the last step of the cooking guide opens the
 * dialog instead of finishing the meal: every recipe ingredient gets an editable
 * amount (defaulting to the recipe amount), ingredients without a pantry match
 * are listed but cannot be edited, "Repor receita" restores the defaults and
 * only "Confirmar refeição" deducts the pantry and shows the meal summary.
 */
class AdjustConsumedQuantities {

    @Then('I see the consumed quantities dialog')
    def seeDialog() {
        WebUI.waitForElementVisible(testObject('div_Consumed quantities dialog'), 15)

        String title = WebUI.getText(testObject('h2_Consumed quantities title')).trim()

        if (title != 'Quantidades consumidas') {
            KeywordUtil.markFailedAndStop("The consumed quantities dialog shows the title '${title}'")
        }
    }

    @Then('the consumed quantities dialog shows {string} with the stock quantity {string}')
    def dialogShowsStockQuantity(String ingredient, String quantity) {
        WebUI.waitForElementPresent(row(ingredient), 10)

        String text = WebUI.getText(
            byXpath(rowXpath(ingredient) + "//span[contains(@class,'stock')]")).trim()

        if (!(text =~ /Na despensa:\s*${quantity}(?!\d)/)) {
            KeywordUtil.markFailedAndStop(
                "The dialog shows the stock '${text}' for ${ingredient}, expected ${quantity}")
        }
    }

    @When('I set the consumed quantity of {string} to {string}')
    def setConsumedQuantity(String ingredient, String amount) {
        TestObject input = quantityInput(ingredient)

        WebUI.waitForElementVisible(input, 10)
        WebUI.clearText(input)
        WebUI.setText(input, amount)
    }

    @Then('the consumed quantity of {string} is {string}')
    def consumedQuantityIs(String ingredient, String amount) {
        String actual = consumedQuantity(ingredient)

        if (actual != amount) {
            KeywordUtil.markFailedAndStop(
                "The dialog shows the consumed quantity '${actual}' for ${ingredient}, expected ${amount}")
        }
    }

    @When('I increase the consumed quantity of {string}')
    def increaseConsumedQuantity(String ingredient) {
        clickStepButton(ingredient, 2)
    }

    @Then('the consumed quantities dialog marks {string} as adjusted')
    def markedAsAdjusted(String ingredient) {
        WebUI.waitForElementVisible(
            byXpath(rowXpath(ingredient) + "//span[contains(@class,'adjusted')]"), 10)
    }

    @Then('the consumed quantities dialog does not mark {string} as adjusted')
    def notMarkedAsAdjusted(String ingredient) {
        WebUI.verifyElementNotPresent(
            byXpath(rowXpath(ingredient) + "//span[contains(@class,'adjusted')]"), 2)
    }

    @When('I restore the recipe quantities')
    def restoreRecipeQuantities() {
        WebUI.waitForElementClickable(testObject('button_Reset consumed quantities'), 10)
        WebUI.click(testObject('button_Reset consumed quantities'))
    }

    @When('I confirm the consumed quantities')
    def confirmConsumedQuantities() {
        WebUI.waitForElementClickable(testObject('button_Confirm consumed quantities'), 10)
        WebUI.click(testObject('button_Confirm consumed quantities'))
    }

    @When('I cancel the consumed quantities')
    def cancelConsumedQuantities() {
        WebUI.waitForElementClickable(testObject('button_Cancel consumed quantities'), 10)
        WebUI.click(testObject('button_Cancel consumed quantities'))
    }

    @Then('I am still in the cooking guide')
    def stillInTheCookingGuide() {
        WebUI.verifyElementNotPresent(testObject('div_Consumed quantities dialog'), 2)
        WebUI.verifyElementVisible(testObject('div_Modo cozinha'))
    }

    @Then('the consumed quantities dialog marks the ingredients missing from the pantry')
    def marksMissingIngredients() {
        def state = WebUI.executeJavaScript(
            "return Array.prototype.map.call(document.querySelectorAll('li.ingredient-row'), function (row) {" +
            " var stock = row.querySelector('.stock');" +
            " return { missing: !!stock && stock.className.indexOf('missing') >= 0," +
            " hasInput: !!row.querySelector('input.quantity-input') };" +
            " });",
            null)

        List<Map> rows = state == null ? [] : (List<Map>) state
        List<Map> missing = rows.findAll { it.missing }

        if (missing.isEmpty()) {
            KeywordUtil.markFailedAndStop('The dialog does not mark any ingredient as missing from the pantry')
        }
        if (missing.any { it.hasInput }) {
            KeywordUtil.markFailedAndStop('An ingredient missing from the pantry still offers a quantity control')
        }
    }

    private static TestObject row(String ingredient) {
        return byXpath(rowXpath(ingredient))
    }

    private static String rowXpath(String ingredient) {
        return "//li[contains(@class,'ingredient-row')]" +
            "[.//span[contains(@class,'ingredient-name')][contains(normalize-space(.), '${ingredient}')]]"
    }

    private static TestObject quantityInput(String ingredient) {
        return byXpath(rowXpath(ingredient) + "//input[contains(@class,'quantity-input')]")
    }

    private static void clickStepButton(String ingredient, int position) {
        TestObject button = byXpath(
            rowXpath(ingredient) + "//button[contains(@class,'step-btn')][${position}]")

        WebUI.waitForElementClickable(button, 10)
        WebUI.click(button)
    }

    private static String consumedQuantity(String ingredient) {
        String name = ingredient.replace('\\', '\\\\').replace("'", "\\'")

        return (WebUI.executeJavaScript(
            "var rows = document.querySelectorAll('li.ingredient-row');" +
            " for (var i = 0; i < rows.length; i++) {" +
            "  var label = rows[i].querySelector('.ingredient-name');" +
            "  if (label && label.textContent.indexOf('" + name + "') >= 0) {" +
            "   var input = rows[i].querySelector('input.quantity-input');" +
            "   return input ? String(input.value) : '';" +
            "  }" +
            " }" +
            " return '';",
            null) ?: '').trim()
    }

    private static TestObject byXpath(String xpath) {
        TestObject object = new TestObject()

        object.addProperty('xpath', ConditionType.EQUALS, xpath)

        return object
    }

    private static TestObject testObject(String name) {
        return findTestObject('Page_SmartPantry/' + name)
    }
}
