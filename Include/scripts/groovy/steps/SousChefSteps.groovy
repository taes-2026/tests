package steps

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

/**
 * Steps for SMAR-52: the Sous-Chef modal lists the pantry, lets the user pick
 * the ingredients it should use and generates a recipe through the local
 * recipe-generator API. The service must run with --test (testing provider);
 * the generation scenario checks /healthz and asserts the deterministic
 * "Salteado de <ingredient>" title and the testing-v1 model.
 */
class SousChefSteps {

    private static final String HEALTH_URL = 'http://localhost:8787/healthz'

    @Given('the Sous-Chef service is running in test mode')
    def sousChefServiceInTestMode() {
        HttpClient client = HttpClient.newHttpClient()
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(HEALTH_URL)).GET().build()
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString())

        WebUI.verifyEqual(response.statusCode(), 200)
        WebUI.verifyEqual(response.body().contains('"mode":"testing"'), true)
    }

    @When('I open the Sous-Chef')
    def openSousChef() {
        WebUI.waitForElementClickable(findTestObject('Page_SmartPantry/button_Sous-Chef IA'), 45)
        WebUI.click(findTestObject('Page_SmartPantry/button_Sous-Chef IA'))
        WebUI.waitForElementVisible(findTestObject('Page_SmartPantry/div_Sous-Chef modal'), 15)
    }

    @Then('the Sous-Chef lists {string} and {string}')
    def listsIngredients(String first, String second) {
        [first, second].each { String name ->
            WebUI.verifyElementPresent(
                findTestObject('Page_SmartPantry/sous_chef_listed_ingredient', [('name'): name]),
                10
            )
        }
    }

    @Then('the Sous-Chef counter shows {string}')
    def counterShows(String expected) {
        WebUI.verifyElementPresent(
            findTestObject('Page_SmartPantry/sous_chef_counter', [('counter'): expected]),
            10
        )
    }

    @Then('the Sous-Chef generate button is disabled')
    def generateDisabled() {
        WebUI.verifyElementPresent(findTestObject('Page_SmartPantry/button_Gerar receita desativado'), 10)
    }

    @Then('the Sous-Chef generate button is enabled')
    def generateEnabled() {
        WebUI.verifyEqual(
            WebUI.isElementClickable(findTestObject('Page_SmartPantry/button_Gerar receita'), 10),
            true
        )
    }

    @When('I click Selecionar tudo in the Sous-Chef')
    def selectAll() {
        WebUI.click(findTestObject('Page_SmartPantry/input_Selecionar tudo Sous-Chef'))
    }

    @When('I select the Sous-Chef ingredient {string}')
    def selectIngredient(String name) {
        WebUI.click(findTestObject('Page_SmartPantry/sous_chef_ingredient', [('name'): name]))
    }

    @When('I set the Sous-Chef doses to {string}')
    def setDoses(String value) {
        WebUI.clearText(findTestObject('Page_SmartPantry/input_Dose Sous-Chef'))
        WebUI.setText(findTestObject('Page_SmartPantry/input_Dose Sous-Chef'), value)
    }

    @When('I ask the Sous-Chef to generate a recipe')
    def generateRecipe() {
        WebUI.click(findTestObject('Page_SmartPantry/button_Gerar receita'))
    }

    @Then('the Sous-Chef generated a recipe for {string} doses')
    def generatedRecipeFor(String doses) {
        WebUI.waitForElementVisible(findTestObject('Page_SmartPantry/div_Sous-Chef receita gerada'), 30)

        WebUI.verifyNotEqual(
            WebUI.getText(findTestObject('Page_SmartPantry/h2_Sous-Chef receita titulo')).trim(),
            ''
        )
        WebUI.verifyEqual(
            WebUI.getText(findTestObject('Page_SmartPantry/strong_Sous-Chef receita doses')).trim(),
            doses
        )
        WebUI.verifyElementPresent(findTestObject('Page_SmartPantry/li_Sous-Chef receita ingredientes'), 10)
        WebUI.verifyElementPresent(findTestObject('Page_SmartPantry/li_Sous-Chef receita preparacao'), 10)
    }

    @Then('the generated recipe is the testing recipe {string}')
    def generatedTestingRecipe(String expectedTitle) {
        WebUI.verifyEqual(
            WebUI.getText(findTestObject('Page_SmartPantry/h2_Sous-Chef receita titulo')).trim(),
            expectedTitle
        )

        String source = WebUI.getText(findTestObject('Page_SmartPantry/p_Sous-Chef receita fonte'))
        WebUI.verifyEqual(source.contains('testing-v1'), true)
    }

    @Then('the Sous-Chef says the pantry is empty')
    def emptyPantry() {
        WebUI.verifyElementPresent(findTestObject('Page_SmartPantry/p_Sous-Chef despensa vazia'), 10)
    }

    @Then('the Sous-Chef has no ingredient list')
    def noIngredientList() {
        WebUI.verifyElementNotPresent(findTestObject('Page_SmartPantry/div_Sous-Chef ingredient list'), 3)
    }

    @When('I cancel the Sous-Chef')
    def cancelSousChef() {
        WebUI.click(findTestObject('Page_SmartPantry/button_Cancelar Sous-Chef'))
    }

    @Then('the Sous-Chef is closed')
    def sousChefClosed() {
        WebUI.waitForElementNotPresent(findTestObject('Page_SmartPantry/div_Sous-Chef modal'), 10)
    }
}
