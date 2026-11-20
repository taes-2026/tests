package steps

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import io.cucumber.java.en.When

/**
 * Wait helpers shared by the recipe tests. RecipesView loads the suggestions as
 * soon as it opens, so the helpers wait for that first request to finish before
 * the test triggers and checks its own search.
 */
class RecipeNavigationSteps {

    @When('I search the recipes for {string}')
    def searchRecipes(String query) {
        WebUI.waitForElementVisible(findTestObject('Page_SmartPantry/input_Pesquisar receitas (ex_ pasta)'), 15)
        WebUI.setText(findTestObject('Page_SmartPantry/input_Pesquisar receitas (ex_ pasta)'), query)
        WebUI.waitForElementClickable(findTestObject('Page_SmartPantry/button_Pesquisar receitas'), 20)
        WebUI.click(findTestObject('Page_SmartPantry/button_Pesquisar receitas'))

        // Give Vue a tick to render the loading state before waiting for the results.
        WebUI.delay(1)
        waitForResults()
    }

    @When('I wait for the recipe results')
    def waitForResults() {
        WebUI.waitForElementNotPresent(findTestObject('Page_SmartPantry/p_Loading recipes'), 30)
        WebUI.waitForElementVisible(findTestObject('Page_SmartPantry/div_Recipe cards'), 30)
    }

    @When('I reload the recipes page')
    def reloadRecipesPage() {
        WebUI.refresh()
        WebUI.waitForElementVisible(findTestObject('Page_SmartPantry/input_Pesquisar receitas (ex_ pasta)'), 15)
    }
}
