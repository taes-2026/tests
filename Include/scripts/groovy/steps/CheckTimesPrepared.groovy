package steps

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import io.cucumber.java.en.Then
import io.cucumber.java.en.When

import org.openqa.selenium.By
import org.openqa.selenium.WebElement

/**
 * SMAR-50 - number of times a recipe was prepared.
 *
 * Finishing the cook mode calls recordPreparation(); the recipe details then
 * show "Preparada N vez(es)" with the last preparation date, the recipe card in
 * the results shows "Nx preparada" and the saved recipes card shows
 * "Preparada N vezes". The count is stored in localStorage, so it survives a
 * page reload.
 */
class CheckTimesPrepared {

    private static String recipeTitle = ''

    @When('I remember the recipe title')
    def rememberRecipeTitle() {
        WebUI.waitForElementVisible(testObject('div_Detalhe da receita'), 15)

        recipeTitle = (WebUI.getAttribute(testObject('div_Detalhe da receita'), 'aria-label') ?: '').trim()
        if (!recipeTitle) {
            KeywordUtil.markFailedAndStop('The recipe details do not expose the recipe title')
        }
    }

    @Then('the recipe has not been prepared yet')
    def recipeHasNotBeenPreparedYet() {
        WebUI.waitForElementVisible(testObject('div_Preparation card'), 15)

        String label = normalized(WebUI.getText(testObject('strong_Preparation label')))
        if (label != 'Ainda não preparada') {
            KeywordUtil.markFailedAndStop('The preparation card reads ' + label)
        }

        String count = WebUI.getText(testObject('div_Preparation count')).trim()
        if (count != '0') {
            KeywordUtil.markFailedAndStop('The preparation count reads ' + count + ', expected 0')
        }

        String hint = WebUI.getText(testObject('span_Preparation last')).trim()
        if (!hint.contains('Conclui o modo cozinha')) {
            KeywordUtil.markFailedAndStop('The empty preparation card reads ' + hint)
        }
    }

    @When('I close the cooking guide')
    def closeCookingGuide() {
        WebUI.waitForElementClickable(testObject('button_Agora nao'), 10)
        WebUI.click(testObject('button_Agora nao'))
        WebUI.waitForElementNotPresent(testObject('div_Modo cozinha'), 10)
        WebUI.waitForElementVisible(testObject('div_Detalhe da receita'), 10)
        WebUI.waitForElementVisible(testObject('div_Preparation card'), 10)
    }

    @Then('I see the first preparation confirmation')
    def seeFirstPreparationConfirmation() {
        WebUI.waitForElementVisible(testObject('p_Preparation note'), 10)

        String note = normalized(WebUI.getText(testObject('p_Preparation note')))
        if (note != 'Esta é a primeira vez que preparas esta receita.') {
            KeywordUtil.markFailedAndStop('The first preparation confirmation reads ' + note)
        }
    }

    @Then('I see the second preparation confirmation')
    def seeSecondPreparationConfirmation() {
        WebUI.waitForElementVisible(testObject('p_Preparation note'), 10)

        String note = normalized(WebUI.getText(testObject('p_Preparation note')))
        if (note != 'Já preparaste esta receita 2 vezes.') {
            KeywordUtil.markFailedAndStop('The second preparation confirmation reads ' + note)
        }
    }

    @Then('the recipe details show {string}')
    def recipeDetailsShow(String expected) {
        WebUI.waitForElementVisible(testObject('div_Preparation card'), 10)

        String label = normalized(WebUI.getText(testObject('strong_Preparation label')))
        if (label != expected) {
            KeywordUtil.markFailedAndStop('The recipe details show "' + label + '", expected "' + expected + '"')
        }
    }

    @Then('the recipe last preparation date is shown')
    def lastPreparationDateIsShown() {
        String text = normalized(WebUI.getText(testObject('span_Preparation last')))
        if (!text.startsWith('Última preparação:')) {
            KeywordUtil.markFailedAndStop('The preparation card does not show the last preparation date: ' + text)
        }
        if (!(text ==~ /Última preparação: \d{1,2} de \p{L}+ de \d{4}/)) {
            KeywordUtil.markFailedAndStop('The last preparation date reads ' + text)
        }
    }

    @Then('the recipe card shows {string}')
    def recipeCardShows(String expected) {
        WebUI.waitForElementVisible(recipeCard(), 15)

        List<WebElement> badges = recipeCard().findElements(By.cssSelector('.stat-badge.prepared'))
        if (badges.isEmpty()) {
            KeywordUtil.markFailedAndStop('The recipe card ' + recipeTitle + ' does not show a preparation badge')
        }

        String badge = normalized(badges.get(0).getText())
        if (badge != expected) {
            KeywordUtil.markFailedAndStop('The recipe card shows "' + badge + '", expected "' + expected + '"')
        }
    }

    @When('I search for the remembered recipe')
    def searchForRememberedRecipe() {
        WebUI.waitForElementVisible(testObject('input_Pesquisar receitas (ex_ pasta)'), 15)
        WebUI.setText(testObject('input_Pesquisar receitas (ex_ pasta)'), recipeTitle)
        WebUI.waitForElementClickable(testObject('button_Pesquisar receitas'), 20)
        WebUI.click(testObject('button_Pesquisar receitas'))

        WebUI.delay(1)
        WebUI.waitForElementNotPresent(testObject('p_Loading recipes'), 30)
        WebUI.waitForElementVisible(testObject('div_Recipe cards'), 30)
    }

    @When('I open the remembered recipe in the results')
    def openRememberedRecipe() {
        WebUI.waitForElementClickable(recipeCard(), 20)
        WebUI.click(recipeCard())
        WebUI.waitForElementVisible(testObject('div_Preparation card'), 15)
    }

    @Then('the saved recipe shows {string}')
    def savedRecipeShows(String expected) {
        WebUI.waitForElementVisible(testObject('h2_Receitas guardadas'), 15)
        WebUI.waitForElementVisible(recipeCard(), 15)

        List<WebElement> badges = recipeCard().findElements(By.cssSelector('.prep-badge'))
        if (badges.isEmpty()) {
            KeywordUtil.markFailedAndStop('The saved recipe ' + recipeTitle + ' does not show its preparation count')
        }

        String badge = normalized(badges.get(0).getText())
        if (badge != expected) {
            KeywordUtil.markFailedAndStop('The saved recipe shows "' + badge + '", expected "' + expected + '"')
        }
    }

    private static TestObject testObject(String name) {
        return findTestObject('Page_SmartPantry/' + name)
    }

    private static TestObject recipeCard() {
        return findTestObject('Page_SmartPantry/recipe_card_by_title', [('title'): recipeTitle])
    }

    private static String normalized(String text) {
        return (text ?: '').replaceAll('\\s+', ' ').trim()
    }
}
