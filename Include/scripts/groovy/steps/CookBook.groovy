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
 * SMAR-34 - cook book: saved recipes and collections.
 *
 * Recipes are saved in local storage: the BookmarkButton toggles a recipe, the
 * BookmarksView lists the saved recipes, filters by collection, and the
 * CollectionsModal creates the collections. The recipe search steps come from
 * RecipeGuide.groovy.
 */
class CookBook {

    private static String savedTitle = ''

    private static TestObject testObject(String name) {
        return findTestObject('Page_SmartPantry/' + name)
    }

    private static List<WebElement> findElements(String objectName, int timeout) {
        try {
            List<WebElement> found = WebUI.findWebElements(testObject(objectName), timeout)
            return found == null ? [] : found
        } catch (Exception ignored) {
            return []
        }
    }

    private static List<String> savedRecipeTitles(int timeout) {
        List<String> titles = []
        for (WebElement title : findElements('h3_Receitas guardadas', timeout)) {
            titles << title.getText().trim()
        }
        return titles
    }

    private static String savedRecipesSummary() {
        return WebUI.getText(testObject('p_Resumo receitas guardadas')).trim()
    }

    private static WebElement collectionItem(String name) {
        for (WebElement item : findElements('li_Colecao', 5)) {
            List<WebElement> names = item.findElements(By.className('name'))
            if (!names.isEmpty() && names.get(0).getText().trim() == name) {
                return item
            }
        }
        return null
    }

    private static WebElement collectionChip(String name) {
        for (WebElement chip : findElements('button_Chip colecao', 5)) {
            if (chip.getText().trim().startsWith(name)) {
                return chip
            }
        }
        return null
    }

    private static void openCollectionsModal() {
        WebUI.waitForElementVisible(testObject('div_Modal colecoes'), 15)
    }

    @When("I tap the Receitas guardadas button")
    def tapSavedRecipesButton() {
        WebUI.click(testObject('button_Receitas guardadas do dashboard'))
        WebUI.waitForElementPresent(testObject('h2_Receitas guardadas'), 15)
    }

    @Then("I see an empty saved recipes list")
    def seeEmptySavedRecipesList() {
        WebUI.verifyElementVisible(testObject('h2_Receitas guardadas'))
        String summary = savedRecipesSummary()
        if (summary != '0 receitas guardadas') {
            KeywordUtil.markFailedAndStop('The saved recipes summary reads ' + summary)
        }
        if (!savedRecipeTitles(2).isEmpty()) {
            KeywordUtil.markFailedAndStop('The saved recipes page lists recipes although nothing was saved')
        }
        WebUI.verifyTextPresent('Ainda não guardaste receitas', false)
    }

    @When("I save the first recipe from the results")
    def saveFirstRecipeFromResults() {
        List<String> titles = savedRecipeTitles(10)
        if (titles.isEmpty()) {
            KeywordUtil.markFailedAndStop('There is no recipe in the results to save')
        }
        savedTitle = titles.get(0)

        WebUI.click(testObject('button_Bookmark do primeiro cartao'))
        WebUI.waitForElementAttributeValue(testObject('button_Bookmark do primeiro cartao'), 'aria-pressed', 'true', 10)
    }

    @Then("the first recipe is marked as saved")
    def firstRecipeMarkedAsSaved() {
        WebUI.verifyElementAttributeValue(testObject('button_Bookmark do primeiro cartao'), 'aria-pressed', 'true', 10)
        String label = WebUI.getAttribute(testObject('button_Bookmark do primeiro cartao'), 'aria-label') ?: ''
        if (!label.contains('Remover receita dos guardados')) {
            KeywordUtil.markFailedAndStop('The saved bookmark button is labelled ' + label)
        }
        String buttonClass = WebUI.getAttribute(testObject('button_Bookmark do primeiro cartao'), 'class') ?: ''
        if (!buttonClass.contains('saved')) {
            KeywordUtil.markFailedAndStop('The bookmark button has no saved state: ' + buttonClass)
        }
    }

    @When("I open the saved recipes page")
    def openSavedRecipesPage() {
        WebUI.click(testObject('a_Guardadas'))
        WebUI.waitForElementPresent(testObject('h2_Receitas guardadas'), 15)
    }

    @Then("I see 1 saved recipe")
    def seeOneSavedRecipe() {
        WebUI.verifyElementVisible(testObject('h2_Receitas guardadas'))
        String summary = savedRecipesSummary()
        if (summary != '1 receita guardada') {
            KeywordUtil.markFailedAndStop('The saved recipes summary reads ' + summary)
        }
        List<String> titles = savedRecipeTitles(5)
        if (titles.size() != 1) {
            KeywordUtil.markFailedAndStop('Expected one saved recipe card, found ' + titles.size())
        }
    }

    @Then("I see the saved recipe in the saved recipes")
    def seeSavedRecipeInList() {
        List<String> titles = savedRecipeTitles(10)
        if (!titles.contains(savedTitle)) {
            KeywordUtil.markFailedAndStop('The saved recipes ' + titles + ' do not include ' + savedTitle)
        }
        WebUI.verifyElementAttributeValue(testObject('button_Bookmark do primeiro cartao'), 'aria-pressed', 'true', 10)
    }

    @When("I remove the first saved recipe")
    def removeFirstSavedRecipe() {
        WebUI.click(testObject('button_Bookmark do primeiro cartao'))
        WebUI.waitForElementNotPresent(testObject('button_Bookmark do primeiro cartao'), 10)
    }

    @When("I save the recipe from the detail")
    def saveRecipeFromDetail() {
        WebUI.verifyElementVisible(testObject('div_Detalhe da receita'))
        savedTitle = (WebUI.getAttribute(testObject('div_Detalhe da receita'), 'aria-label') ?: '').trim()
        if (!savedTitle) {
            KeywordUtil.markFailedAndStop('The recipe detail has no accessible title')
        }
        WebUI.click(testObject('button_Bookmark do detalhe'))
        WebUI.waitForElementAttributeValue(testObject('button_Bookmark do detalhe'), 'aria-pressed', 'true', 10)
    }

    @Then("the recipe detail is marked as saved")
    def detailMarkedAsSaved() {
        WebUI.verifyElementAttributeValue(testObject('button_Bookmark do detalhe'), 'aria-pressed', 'true', 10)
        String label = WebUI.getAttribute(testObject('button_Bookmark do detalhe'), 'aria-label') ?: ''
        if (!label.contains('Remover receita dos guardados')) {
            KeywordUtil.markFailedAndStop('The detail bookmark button is labelled ' + label)
        }
    }

    @When("I close the recipe detail")
    def closeRecipeDetail() {
        WebUI.click(testObject('button_Fechar detalhe'))
        WebUI.waitForElementNotPresent(testObject('div_Detalhe da receita'), 10)
    }

    @When("I create the collection {string} for the saved recipe")
    def createCollectionForSavedRecipe(String name) {
        WebUI.click(testObject('button_Adicionar a colecao'))
        openCollectionsModal()

        WebUI.setText(testObject('input_Nome da nova colecao'), name)
        WebUI.click(testObject('button_Criar colecao'))
        WebUI.delay(1)

        if (collectionItem(name) == null) {
            KeywordUtil.markFailedAndStop('The collection ' + name + ' was not created')
        }
    }

    @Then("the collection {string} is checked for the saved recipe")
    def collectionCheckedForSavedRecipe(String name) {
        WebElement item = collectionItem(name)
        if (item == null) {
            KeywordUtil.markFailedAndStop('The collection ' + name + ' is not listed for the saved recipe')
        }
        List<WebElement> checkboxes = item.findElements(By.cssSelector('input[type="checkbox"]'))
        if (checkboxes.isEmpty() || !checkboxes.get(0).isSelected()) {
            KeywordUtil.markFailedAndStop('The collection ' + name + ' is not checked for the saved recipe')
        }
        String count = item.findElement(By.className('count')).getText().trim()
        if (count != '1') {
            KeywordUtil.markFailedAndStop('The collection ' + name + ' counts ' + count + ' recipes, expected 1')
        }
    }

    @When("I close the collections modal")
    def closeCollectionsModal() {
        WebUI.click(testObject('button_Concluir colecoes'))
        WebUI.waitForElementNotPresent(testObject('div_Modal colecoes'), 10)
    }

    @Then("I see the collection {string} on the saved recipe")
    def seeCollectionBadge(String name) {
        List<String> badges = []
        for (WebElement badge : findElements('span_Badge colecao', 5)) {
            badges << badge.getText().trim()
        }
        if (!badges.contains(name)) {
            KeywordUtil.markFailedAndStop('The saved recipe badges ' + badges + ' do not include ' + name)
        }
        String collectionsButton = WebUI.getText(testObject('button_Adicionar a colecao')).trim()
        if (!collectionsButton.contains('Editar coleções')) {
            KeywordUtil.markFailedAndStop('The collections button still reads ' + collectionsButton)
        }
    }

    @Then("I see the collection filter {string}")
    def seeCollectionFilter(String name) {
        WebElement chip = collectionChip(name)
        if (chip == null) {
            KeywordUtil.markFailedAndStop('There is no filter chip for the collection ' + name)
        }
        String text = chip.getText().trim()
        if (!text.contains('(') || !text.contains(')')) {
            KeywordUtil.markFailedAndStop('The collection filter reads ' + text)
        }
    }

    @When("I filter the saved recipes by the collection {string}")
    def filterByCollection(String name) {
        WebElement chip = collectionChip(name)
        if (chip == null) {
            KeywordUtil.markFailedAndStop('There is no filter chip for the collection ' + name)
        }
        chip.click()
        WebUI.delay(1)
    }

    @Then("only the saved recipe is shown")
    def onlySavedRecipeShown() {
        List<String> titles = savedRecipeTitles(5)
        if (titles.size() != 1) {
            KeywordUtil.markFailedAndStop('The filtered saved recipes list has ' + titles.size() + ' card(s)')
        }
        if (titles.get(0) != savedTitle) {
            KeywordUtil.markFailedAndStop('The filtered saved recipe is ' + titles.get(0) + ', expected ' + savedTitle)
        }
    }

    @When("I create the empty collection {string}")
    def createEmptyCollection(String name) {
        WebUI.click(testObject('button_Gerir colecoes'))
        openCollectionsModal()

        WebUI.setText(testObject('input_Nome da nova colecao'), name)
        WebUI.click(testObject('button_Criar colecao'))
        WebUI.delay(1)

        if (collectionItem(name) == null) {
            KeywordUtil.markFailedAndStop('The collection ' + name + ' was not created')
        }
    }

    @Then("the collection {string} is empty")
    def collectionIsEmpty(String name) {
        WebElement item = collectionItem(name)
        if (item == null) {
            KeywordUtil.markFailedAndStop('The collection ' + name + ' is not listed')
        }
        String count = item.findElement(By.className('count')).getText().trim()
        if (count != '0') {
            KeywordUtil.markFailedAndStop('The collection ' + name + ' counts ' + count + ' recipes, expected 0')
        }
    }

    @When("I try to create the collection {string} again")
    def createDuplicateCollection(String name) {
        WebUI.setText(testObject('input_Nome da nova colecao'), name)
        WebUI.click(testObject('button_Criar colecao'))
        WebUI.delay(1)
    }

    @Then("I see the duplicate collection error")
    def seeDuplicateCollectionError() {
        WebUI.verifyElementVisible(testObject('p_Erro colecao'))
        String error = WebUI.getText(testObject('p_Erro colecao')).trim()
        if (error != 'Já existe uma coleção com esse nome.') {
            KeywordUtil.markFailedAndStop('The collection error reads ' + error)
        }
    }

    @When("I reload the saved recipes page")
    def reloadSavedRecipesPage() {
        WebUI.refresh()
        WebUI.waitForElementPresent(testObject('h2_Receitas guardadas'), 15)
        WebUI.delay(1)
    }

    @Then("the saved recipe is still listed")
    def savedRecipeStillListed() {
        WebUI.verifyElementVisible(testObject('h2_Receitas guardadas'))
        List<String> titles = savedRecipeTitles(10)
        if (!titles.contains(savedTitle)) {
            KeywordUtil.markFailedAndStop('After the reload the saved recipes are ' + titles)
        }
        String summary = savedRecipesSummary()
        if (!summary.startsWith('1 receita guardada')) {
            KeywordUtil.markFailedAndStop('After the reload the saved recipes summary reads ' + summary)
        }
    }

    @Then("I see the empty collection message")
    def seeEmptyCollectionMessage() {
        WebUI.verifyTextPresent('Nenhuma receita nesta coleção.', false)
        if (!savedRecipeTitles(2).isEmpty()) {
            KeywordUtil.markFailedAndStop('The empty collection still lists saved recipes')
        }
    }
}
