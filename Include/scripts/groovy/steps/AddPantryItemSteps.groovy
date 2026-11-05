package steps

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When



class AddPantryItemSteps {

    @Given("I start the application in the {string} url")
    def startApplication(url) {
        WebUI.openBrowser('')
        WebUI.navigateToUrl(url)
        WebUI.setText(findTestObject('Page_SmartPantry/input_Nome do perfil'), 'Teste')
        WebUI.click(findTestObject('Page_SmartPantry/button_Comear'))
    }

    @When("I tap the adicionar produto button")
    def tapAdicionarProduto() {
        WebUI.click(findTestObject('Page_SmartPantry/button_Adicionar produto'))
    }

    @And("I tap introduzir manualmente button")
    def tapIntroduzirManualmente() {
        WebUI.click(findTestObject('Page_SmartPantry/button_Introduzir manualmente'))
    }

    @And("I fill the item details")
    def fillItemDetails() {
        WebUI.setText(findTestObject('Page_SmartPantry/input_Nome do produto'), 'leite')
        WebUI.selectOptionByValue(findTestObject('Page_SmartPantry/select_Categoria FrutasLegumesCarnesPeixeLaticn'), 'dairy', false)
        WebUI.setText(findTestObject('Page_SmartPantry/input_Quantidade'), '2')
        WebUI.setText(findTestObject('Page_SmartPantry/input_Data de validade'), '2026-11-19')
    }

    @And("I save the item")
    def saveItem() {
        WebUI.click(findTestObject('Page_SmartPantry/button_Guardar'))
    }

    @And("I see the item in my pantry")
    def verifyItemInPantry() {
	    WebUI.verifyTextPresent('(?is).*leite.*', true)
	}

}
