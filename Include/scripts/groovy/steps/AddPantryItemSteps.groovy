package steps

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When



class AddPantryItemSteps {

    @And("I fill the item details")
    def fillItemDetails() {
        WebUI.setText(findTestObject('Page_SmartPantry/input_Nome do produto'), 'leite')
        WebUI.selectOptionByValue(findTestObject('Page_SmartPantry/select_Categoria FrutasLegumesCarnesPeixeLaticn'), 'dairy', false)
        WebUI.setText(findTestObject('Page_SmartPantry/input_Quantidade'), '2')
        WebUI.setText(findTestObject('Page_SmartPantry/input_Data de validade'), '2026-11-19')
    }

    @And("I see the item in my pantry")
    def verifyItemInPantry() {
	    WebUI.verifyElementPresent(findTestObject('Page_SmartPantry/td_Leite'), 5)
	}
}