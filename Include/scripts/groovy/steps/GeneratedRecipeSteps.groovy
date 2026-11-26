package steps

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import io.cucumber.java.en.Then
import io.cucumber.java.en.When

import org.openqa.selenium.WebElement

/**
 * Steps for SMAR-54: after the Sous-Chef generates a recipe, the app opens the
 * generated-recipe details modal (title, summary, stats, ingredients with
 * pantry badges, steps) and lets the user start the cooking guide from it.
 * The recipe-generator must run with --test so the content is deterministic.
 */
class GeneratedRecipeSteps {

    private static TestObject testObject(String name) {
        return findTestObject('Page_SmartPantry/' + name)
    }

    private static void verifyContains(String actual, String expected, String label) {
        // Rendered text: CSS text-transform uppercases badges such as "EM DESPENSA".
        if (!actual.toLowerCase().contains(expected.toLowerCase())) {
            com.kms.katalon.core.util.KeywordUtil.markFailedAndStop(
                label + " should contain '" + expected + "' but reads '" + actual + "'"
            )
        }
    }

    @Then('the generated recipe summary is shown')
    def summaryShown() {
        WebUI.verifyEqual(
            WebUI.getText(testObject('p_Sous-Chef receita resumo')).trim(),
            'Receita gerada localmente em modo de testes.'
        )
    }

    @Then('the generated recipe stats show {string} doses, {string} preparation, {string} cooking and {string} total')
    def statsShown(String doses, String preparation, String cooking, String total) {
        WebUI.verifyEqual(WebUI.getText(testObject('strong_Sous-Chef receita doses')).trim(), doses)
        WebUI.verifyEqual(WebUI.getText(testObject('strong_Sous-Chef receita preparacao')).trim(), preparation)
        WebUI.verifyEqual(WebUI.getText(testObject('strong_Sous-Chef receita cozedura')).trim(), cooking)
        WebUI.verifyEqual(WebUI.getText(testObject('strong_Sous-Chef receita total')).trim(), total)
    }

    @Then('the generated recipe ingredient {string} shows {string} and {string}')
    def ingredientShows(String name, String quantity, String badge) {
        String row = WebUI.getText(findTestObject('Page_SmartPantry/sous_chef_receita_ingrediente', [('name'): name])).trim()

        verifyContains(row, quantity, 'The ' + name + ' ingredient row')
        verifyContains(row, badge, 'The ' + name + ' ingredient row')
    }

    @Then('the generated recipe ingredient {string} is marked as optional')
    def ingredientOptional(String name) {
        String row = WebUI.getText(findTestObject('Page_SmartPantry/sous_chef_receita_ingrediente', [('name'): name])).trim()

        verifyContains(row, 'opcional', 'The ' + name + ' ingredient row')
    }

    @Then('the generated recipe lists {string} preparation steps')
    def stepCount(String expected) {
        List<WebElement> steps = WebUI.findWebElements(testObject('li_Sous-Chef receita preparacao'), 10)

        WebUI.verifyEqual(steps.size(), Integer.parseInt(expected))
    }

    @When('I tap the Modo de preparação button in the generated recipe')
    def openCookingGuide() {
        WebUI.click(testObject('button_Modo de preparacao receita gerada'))
        WebUI.waitForElementVisible(testObject('div_Modo cozinha'), 10)
    }

    @Then('the generated cooking guide shows step {string} of {string}')
    def guideStep(String step, String total) {
        String counter = WebUI.getText(testObject('p_active_step_count')).trim().toLowerCase()

        // .step-count applies text-transform: uppercase, so compare case-insensitively.
        verifyContains(counter, ('Passo ' + step + ' de ' + total).toLowerCase(), 'The cooking guide counter')
    }

    @Then('the generated cooking guide step mentions {string}')
    def guideStepMentions(String fragment) {
        String text = WebUI.getText(testObject('p_active_step_text')).trim()

        verifyContains(text, fragment, 'The cooking guide step')
    }

    @Then('the generated cooking guide offers the timer {string}')
    def guideTimer(String label) {
        WebUI.waitForElementVisible(testObject('aside_Temporizadores do passo'), 10)

        String timer = WebUI.getText(testObject('button_Temporizador do passo')).trim()

        verifyContains(timer, label, 'The step timer')
    }

    @When('I leave the generated cooking guide')
    def leaveCookingGuide() {
        WebUI.click(testObject('button_Sair do modo cozinha'))
        WebUI.waitForElementNotPresent(testObject('div_Modo cozinha'), 10)
    }

    @Then('the generated recipe is still open')
    def generatedRecipeStillOpen() {
        WebUI.verifyElementVisible(testObject('div_Sous-Chef receita gerada'))
    }

    @When('I close the generated recipe')
    def closeGeneratedRecipe() {
        WebUI.click(testObject('button_Fechar receita gerada'))
    }

    @Then('the generated recipe is closed')
    def generatedRecipeClosed() {
        WebUI.waitForElementNotPresent(testObject('div_Sous-Chef receita gerada'), 10)
    }

    @Then('the Sous-Chef entry button is available')
    def sousChefEntryAvailable() {
        WebUI.verifyEqual(
            WebUI.isElementClickable(testObject('button_Sous-Chef IA'), 10),
            true
        )
    }
}