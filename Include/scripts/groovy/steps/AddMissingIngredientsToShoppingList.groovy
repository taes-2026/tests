package steps

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import io.cucumber.java.en.Then
import io.cucumber.java.en.When

import org.openqa.selenium.By
import org.openqa.selenium.WebElement

/**
 * SMAR-46 - adding a recipe's missing ingredients to the shopping list.
 *
 * RecipeDetailModal only renders the "Add missing to the shopping list" button
 * when the overlap calculation finds missing ingredients. The shopping list
 * keeps one entry per missing ingredient and remembers the source recipe.
 */
class AddMissingIngredientsToShoppingList {

    private static int addedCount = 0
    private static int itemsBeforeClear = 0
    private static String recipeTitle = ''

    @Then('I see the add missing ingredients button')
    def seeAddMissingButton() {
        WebUI.waitForElementVisible(testObject('button_Add missing ingredients'), 15)
        WebUI.verifyElementClickable(testObject('button_Add missing ingredients'))

        recipeTitle = (WebUI.getAttribute(testObject('div_Detalhe da receita'), 'aria-label') ?: '').trim()
        if (!recipeTitle) {
            KeywordUtil.markFailedAndStop('The recipe details do not expose the recipe title')
        }
    }

    @When('I add the missing ingredients to the shopping list')
    def addMissingIngredients() {
        WebUI.waitForElementClickable(testObject('button_Add missing ingredients'), 10)
        WebUI.click(testObject('button_Add missing ingredients'))
        WebUI.waitForElementVisible(testObject('div_Shopping feedback'), 10)
        WebUI.delay(1)
    }

    @Then('I see the shopping list confirmation')
    def seeShoppingListConfirmation() {
        String feedback = WebUI.getText(testObject('div_Shopping feedback')).trim()
        def matcher = (feedback =~ /(\d+) ingrediente\(s\) adicionado\(s\) à lista de compras\./)

        if (!matcher.find()) {
            KeywordUtil.markFailedAndStop('The shopping list confirmation reads ' + feedback)
        }

        addedCount = Integer.parseInt(matcher.group(1))
        if (addedCount < 1) {
            KeywordUtil.markFailedAndStop('The recipe reported no added ingredients')
        }

        WebUI.verifyElementPresent(testObject('a_Ver lista de compras'), 5)
    }

    @Then('I see the duplicate shopping list message')
    def seeDuplicateMessage() {
        WebUI.waitForElementVisible(testObject('div_Shopping feedback'), 10)

        String feedback = WebUI.getText(testObject('div_Shopping feedback')).trim()
        if (!feedback.contains('Os ingredientes em falta já estão na lista de compras.')) {
            KeywordUtil.markFailedAndStop('The second add reads ' + feedback)
        }
    }

    @When('I open the shopping list from the confirmation')
    def openShoppingList() {
        WebUI.waitForElementClickable(testObject('a_Ver lista de compras'), 10)
        WebUI.click(testObject('a_Ver lista de compras'))
        WebUI.waitForElementVisible(testObject('h2_Lista de compras'), 15)
        WebUI.delay(1)
    }

    @Then('the shopping list contains the added ingredients')
    def shoppingListContainsAddedIngredients() {
        List<WebElement> items = shoppingItems()
        if (items.size() != addedCount) {
            KeywordUtil.markFailedAndStop("The shopping list has ${items.size()} item(s), expected ${addedCount}")
        }

        WebUI.verifyElementVisible(testObject('span_Items count'))
        String summary = WebUI.getText(testObject('span_Items count')).trim()
        String expected = "${addedCount} por comprar de ${addedCount} no total"

        if (!summary.startsWith(expected)) {
            KeywordUtil.markFailedAndStop('The shopping list summary reads ' + summary + ', expected ' + expected)
        }

        for (WebElement item : items) {
            List<WebElement> names = item.findElements(By.cssSelector('.item-name'))
            if (names.isEmpty() || !names.get(0).getText().trim()) {
                KeywordUtil.markFailedAndStop('A shopping list item has no name')
            }
        }
    }

    @Then('the shopping list items are linked to the recipe')
    def shoppingListItemsLinkedToRecipe() {
        List<WebElement> items = shoppingItems()
        if (items.isEmpty()) {
            KeywordUtil.markFailedAndStop('The shopping list is empty')
        }

        for (WebElement item : items) {
            List<WebElement> badges = item.findElements(By.cssSelector('.item-recipe-badge'))
            if (badges.isEmpty()) {
                KeywordUtil.markFailedAndStop('A shopping list item does not name its recipe')
            }

            String badge = badges.get(0).getText().trim()
            if (!badge.contains(recipeTitle)) {
                KeywordUtil.markFailedAndStop('The shopping list item reads "' + badge + '", expected the recipe ' + recipeTitle)
            }
        }
    }

    @Then('the shopping list does not contain duplicate ingredients')
    def shoppingListHasNoDuplicates() {
        List<WebElement> items = shoppingItems()
        if (items.size() != addedCount) {
            KeywordUtil.markFailedAndStop("The shopping list has ${items.size()} item(s) after adding the same recipe twice, expected ${addedCount}")
        }
    }

    @When('I check the first shopping list item')
    def checkFirstItem() {
        WebUI.waitForElementClickable(testObject('input_Shopping checkbox'), 10)
        WebUI.click(testObject('input_Shopping checkbox'))
        WebUI.waitForElementVisible(testObject('button_Mover para despensa'), 10)
    }

    @Then('I can move the first item to the pantry')
    def canMoveFirstItem() {
        WebUI.verifyElementVisible(testObject('button_Mover para despensa'))
        WebUI.verifyElementClickable(testObject('button_Mover para despensa'))
        itemsBeforeClear = shoppingItems().size()
    }

    @When('I clear the purchased items')
    def clearPurchasedItems() {
        if (WebUI.verifyElementPresent(testObject('button_Limpar comprados'), 2, FailureHandling.OPTIONAL)) {
            WebUI.click(testObject('button_Limpar comprados'))
            WebUI.delay(1)
        }
    }

    @Then('the checked items are removed')
    def checkedItemsAreRemoved() {
        int remaining = shoppingItems().size()

        if (remaining != itemsBeforeClear - 1) {
            KeywordUtil.markFailedAndStop("The shopping list has ${remaining} item(s) after clearing one checked item out of ${itemsBeforeClear}")
        }
    }

    @When('I clear the shopping list')
    def clearShoppingList() {
        if (WebUI.verifyElementPresent(testObject('button_Limpar tudo'), 2, FailureHandling.OPTIONAL)) {
            WebUI.click(testObject('button_Limpar tudo'))
            WebUI.delay(1)
        }
    }

    @Then('the shopping list is empty')
    def shoppingListIsEmpty() {
        WebUI.verifyElementVisible(testObject('p_Shopping empty'))
        if (!shoppingItems().isEmpty()) {
            KeywordUtil.markFailedAndStop('The shopping list still contains items')
        }
        WebUI.verifyElementNotPresent(testObject('span_Items count'), 2)
    }

    private static TestObject testObject(String name) {
        return findTestObject('Page_SmartPantry/' + name)
    }

    private static List<WebElement> shoppingItems() {
        try {
            List<WebElement> items = WebUI.findWebElements(testObject('li_Shopping item'), 10)

            return items == null ? [] : items
        } catch (Exception ignored) {
            return []
        }
    }
}
