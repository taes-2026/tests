package steps

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import io.cucumber.java.en.And
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

class SaveRecipeAfterMeal {

    private static TestObject testObject(String name) {
        return findTestObject('Page_SmartPantry/' + name)
    }

    @And('I clear the saved recipes')
    def clearSavedRecipes() {
        WebUI.executeJavaScript("localStorage.removeItem('smartpantry:bookmarks')", null)
        WebUI.refresh()
        WebUI.waitForElementClickable(testObject('button_Procurar receitas'), 10)
    }

    @When('I open the {string} recipe in the results')
    def openRecipeByTitle(String title) {
        WebUI.waitForElementClickable(recipeCard(title), 20)
        WebUI.click(recipeCard(title))
        WebUI.waitForElementPresent(testObject('button_Comear a cozinhar'), 20)
    }

    @When('I tap the Guardar receita button')
    def tapSaveRecipeButton() {
        WebUI.waitForElementClickable(testObject('button_Guardar receita'), 10)
        WebUI.click(testObject('button_Guardar receita'))
    }

    @Then('I see the recipe saved confirmation')
    def seeRecipeSavedConfirmation() {
        WebUI.waitForElementVisible(testObject('p_Receita guardada'), 10)
        WebUI.verifyElementVisible(testObject('p_Receita guardada'))
        WebUI.verifyElementNotPresent(testObject('button_Guardar receita'), 5)
        WebUI.verifyElementClickable(testObject('button_Concluir'))
    }

    @When('I tap the Concluir button on the completion screen')
    def tapConcluirButton() {
        WebUI.waitForElementClickable(testObject('button_Concluir'), 10)
        WebUI.click(testObject('button_Concluir'))
        WebUI.waitForElementNotPresent(testObject('div_Modo cozinha'), 10)
    }

    @When('I close the recipe details')
    def closeRecipeDetails() {
        WebUI.waitForElementClickable(testObject('button_Close'), 10)
        WebUI.click(testObject('button_Close'))
        WebUI.waitForElementClickable(testObject('button_Procurar receitas'), 10)
    }

    @When('I go back to the dashboard')
    def goBackToDashboard() {
        WebUI.waitForElementClickable(testObject('a_Voltar'), 10)
        WebUI.click(testObject('a_Voltar'))
        WebUI.waitForElementClickable(testObject('button_Receitas guardadas'), 10)
    }

    @When('I tap the Receitas guardadas button')
    def tapSavedRecipesButton() {
        WebUI.waitForElementClickable(testObject('button_Receitas guardadas'), 10)
        WebUI.click(testObject('button_Receitas guardadas'))
        WebUI.waitForElementPresent(testObject('h2_Receitas guardadas'), 10)
    }

    @Then('I see the {string} recipe in saved recipes')
    def seeRecipeInSavedRecipes(String title) {
        WebUI.verifyElementPresent(recipeCard(title), 10)
        WebUI.verifyElementVisible(recipeCard(title))
    }

    @Then('I do not see the {string} recipe in saved recipes')
    def doNotSeeRecipeInSavedRecipes(String title) {
        WebUI.verifyElementNotPresent(recipeCard(title), 5)
    }

    private static TestObject recipeCard(String title) {
        return findTestObject('Page_SmartPantry/recipe_card_by_title', [('title'): title])
    }
}
