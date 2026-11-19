package steps

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import io.cucumber.java.en.Then
import io.cucumber.java.en.When

class CheckMealSummary {

    private static TestObject testObject(String name) {
        return findTestObject('Page_SmartPantry/' + name)
    }

    @When('I tap the Ver resumo da refeição button')
    def tapMealSummaryButton() {
        WebUI.waitForElementClickable(testObject('button_Ver resumo da refeicao'), 20)
        WebUI.click(testObject('button_Ver resumo da refeicao'))
        WebUI.waitForElementVisible(testObject('div_Resumo da refeicao modal'), 10)
    }

    @Then('I see the meal summary for {string}')
    def seeMealSummaryFor(String title) {
        WebUI.waitForElementVisible(testObject('section_Resumo da refeicao'), 10)
        WebUI.verifyElementVisible(testObject('section_Resumo da refeicao'))

        WebUI.waitForElementVisible(recipeName(title), 10)
        WebUI.verifyElementVisible(recipeName(title))
        WebUI.verifyElementPresent(testObject('li_Ingrediente do resumo'), 10)
        WebUI.verifyElementPresent(testObject('table_Informacao nutricional'), 10)
    }

    @When('I close the meal summary dialog')
    def closeMealSummaryDialog() {
        WebUI.waitForElementClickable(testObject('button_Fechar resumo'), 10)
        WebUI.click(testObject('button_Fechar resumo'))
        WebUI.waitForElementNotPresent(testObject('div_Resumo da refeicao modal'), 10)
    }

    @Then('I do not see the meal summary dialog')
    def doNotSeeMealSummaryDialog() {
        WebUI.verifyElementNotPresent(testObject('div_Resumo da refeicao modal'), 5)
    }

    private static TestObject recipeName(String title) {
        return findTestObject('Page_SmartPantry/p_Recipe name in summary', [('title'): title])
    }
}
