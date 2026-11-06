package steps

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

class CategorySteps {

    @Given("I start the application {string} url")
    def startApp(String url) {
        WebUI.openBrowser(url)
        WebUI.maximizeWindow()
    }

    @When("I tap the adicionar categoria button")
    def tapAddCategory() {
        WebUI.click(findTestObject('Page_SmartPantry/btn_Adicionar_Categoria'))
    }

    @And("I name the category")
    def nameCategory() {
        WebUI.setText(findTestObject('Page_SmartPantry/input_Nome_da_categoria'), 'test')
    }

    @And("I save the category")
    def saveCategory() {
        WebUI.click(findTestObject('Page_SmartPantry/btn_Guardar_categoria'))
    }

    @And("I see the the category in the table below")
    def verifyCategoryInTable() {
        WebUI.verifyTextPresent('(?is).*test.*', true)
    }
}