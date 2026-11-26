package steps

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import io.cucumber.java.en.Then
import io.cucumber.java.en.When

class ExportAppDataSteps {

	@When("I tap the Exportar dados button")
	def tapExportarDadosButton() {
		WebUI.waitForElementClickable(findTestObject('Page_SmartPantry/button_Exportar dados'), 10)
		WebUI.click(findTestObject('Page_SmartPantry/button_Exportar dados'))
	}

	@Then("I should see the export confirmation message")
	def shouldSeeExportConfirmationMessage() {
		WebUI.waitForElementVisible(findTestObject('Page_SmartPantry/p_Dados exportados'), 10)
		WebUI.verifyMatch(
			WebUI.getText(findTestObject('Page_SmartPantry/p_Dados exportados')).trim(),
			'Dados exportados para .*\\.json\\.',
			true
		)
	}
}
