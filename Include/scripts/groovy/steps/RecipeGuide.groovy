package steps

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import io.cucumber.java.en.Then
import io.cucumber.java.en.When


class RecipeGuide {

    private static TestObject testObject(String name) {
        return findTestObject('Page_SmartPantry/' + name)
    }

    @When("I search for {string} recipes")
    def searchForRecipes(String query) {
        WebUI.setText(testObject('input_Pesquisar receitas (ex_ pasta)'), query)
        WebUI.waitForElementClickable(testObject('button_Pesquisar receitas'), 20)
        WebUI.click(testObject('button_Pesquisar receitas'))
        WebUI.waitForElementClickable(testObject('recipe_card'), 20)
    }

    @When("I open the first recipe in the results")
    def openFirstRecipeInTheResults() {
        WebUI.waitForElementClickable(testObject('recipe_card'), 20)
        WebUI.click(testObject('recipe_card'))
        WebUI.waitForElementPresent(testObject('button_Comear a cozinhar'), 20)
    }

    @Then("I see the cooking guide on step {int}")
    def seeCookingGuideOnStep(int number) {
        WebUI.verifyElementVisible(testObject('div_Modo cozinha'))
        WebUI.verifyElementPresent(testObject('div_Progresso da receita'), 10)
        WebUI.verifyElementAttributeValue(testObject('div_Progresso da receita'), 'aria-valuenow', String.valueOf(number), 10)

        String counter = WebUI.getText(testObject('p_active_step_count')).trim()

        // The .step-count CSS applies text-transform: uppercase, so Selenium returns e.g. "PASSO 1 DE 5".
        if (!counter.toLowerCase().startsWith(("Passo ${number} de ").toLowerCase())) {
            KeywordUtil.markFailedAndStop("Expected the cooking guide on step ${number}, but it shows '${counter}'")
        }

        String stepText = WebUI.getText(testObject('p_active_step_text')).trim()
        if (!stepText) {
            KeywordUtil.markFailedAndStop("The cooking guide does not show any text for step ${number}")
        }

        verifyIngredientsForCurrentStep()

        if (number == 1) {
            WebUI.verifyElementNotClickable(testObject('button_Anterior'))
        } else {
            WebUI.verifyElementClickable(testObject('button_Anterior'))
        }
    }

    private static void verifyIngredientsForCurrentStep() {
        WebUI.verifyElementPresent(testObject('aside_Ingredientes deste passo'), 10)

        boolean hasIngredientItems = WebUI.verifyElementPresent(testObject('li_Ingrediente do passo'), 2, FailureHandling.OPTIONAL)
        boolean hasEmptyMessage = WebUI.verifyElementPresent(testObject('p_Sem ingredientes especificos neste passo'), 2, FailureHandling.OPTIONAL)

        if (!hasIngredientItems && !hasEmptyMessage) {
            KeywordUtil.markFailedAndStop('The ingredients panel of the current step is empty')
        }

        if (hasIngredientItems) {
            WebUI.verifyElementPresent(testObject('mark_Ingrediente destacado'), 5)
        }
    }

    @When("I tap the Seguinte button in the cooking guide")
    def tapNextStepButton() {
        if (!WebUI.waitForElementClickable(testObject('button_Seguinte'), 5, FailureHandling.OPTIONAL)) {
            KeywordUtil.markFailedAndStop('The current recipe only has one step; this scenario requires a recipe with at least two steps')
        }
        WebUI.click(testObject('button_Seguinte'))
        // Let the 0.35s carousel transform finish; getText() can return "" while the slide is moving.
        WebUI.delay(1)
    }

    @When("I tap the Anterior button in the cooking guide")
    def tapPreviousStepButton() {
        WebUI.click(testObject('button_Anterior'))
        WebUI.delay(1)
    }

    @When("I go to the last step of the cooking guide")
    def goToLastStep() {
        TestObject progress = testObject('div_Progresso da receita')
        WebUI.waitForElementPresent(progress, 10)
        int totalSteps = Integer.parseInt(WebUI.getAttribute(progress, 'aria-valuemax'))
        int currentStep = Integer.parseInt(WebUI.getAttribute(progress, 'aria-valuenow'))
        int guard = 0

        // Advance on the progress bar value instead of the button text: on the last step
        // "Seguinte" is replaced by "Concluir refeição", and both share the .nav.primary class.
        while (currentStep < totalSteps && guard < 50) {
            WebUI.click(testObject('button_Seguinte'))
            WebUI.waitForElementAttributeValue(progress, 'aria-valuenow', String.valueOf(currentStep + 1), 10)
            currentStep++
            guard++
        }

        if (currentStep != totalSteps) {
            KeywordUtil.markFailedAndStop("Could not reach the last step: progress is ${currentStep} of ${totalSteps}")
        }

        WebUI.verifyElementPresent(testObject('button_Concluir refeicao'), 10)
    }

    @When("I tap the Concluir refeição button in the cooking guide")
    def tapFinishButton() {
        WebUI.click(testObject('button_Concluir refeicao'))
    }

    @Then("I see the meal summary")
    def seeMealSummary() {
        WebUI.verifyElementPresent(testObject('h2_Refeicao concluida'), 10)
        WebUI.verifyElementPresent(testObject('section_Resumo da refeicao'), 10)
    }

    @When("I close the meal summary")
    def closeMealSummary() {
        WebUI.click(testObject('button_Agora nao'))
    }

    @When("I tap the Sair do modo cozinha button")
    def tapLeaveCookingGuideButton() {
        WebUI.click(testObject('button_Sair do modo cozinha'))
    }

    @Then("I see the recipe details")
    def seeRecipeDetails() {
        WebUI.verifyElementNotPresent(testObject('div_Modo cozinha'), 10)
        WebUI.verifyElementPresent(testObject('button_Comear a cozinhar'), 10)
    }
}
