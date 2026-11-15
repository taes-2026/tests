package steps

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import java.time.LocalDate

import io.cucumber.java.en.And
import io.cucumber.java.en.Then



class ExpiringItemsSteps {
	
	@And("I fill the item details with {string} expiring in {int} days")
	def fillItems(String item, int days) {
		WebUI.setText(findTestObject('Page_SmartPantry/input_Nome do produto'), item)
		WebUI.selectOptionByValue(findTestObject('Page_SmartPantry/select_Categoria FrutasLegumesCarnesPeixeLaticn'), 'fruits', false)
		WebUI.setText(findTestObject('Page_SmartPantry/input_Quantidade'), '1')
		WebUI.setText(findTestObject('Page_SmartPantry/input_Data de validade'), LocalDate.now().plusDays(days).toString())
	}

    @Then("I see {string} in the expiring alerts")
    def verifyInExpiringAlerts(String item) {
		//Foi adicionado ao PATH do objeto section_expiring-title o filtro //span[text()='${item}'] para que o teste só passe caso o item "..." esteja dentro da secção
		WebUI.verifyElementPresent(findTestObject('Page_SmartPantry/span_item', [('item') : item]), 5)
    }
}