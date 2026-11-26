package steps

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import io.cucumber.java.en.And
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

class ImportDataWithOverwriteSteps {

	@When('I import the data file {string}')
	def importDataFile(String fileName) {
		String filePath = RunConfiguration.getProjectDir() + File.separator +
				'Include' + File.separator + 'data' + File.separator + fileName

		if (!new File(filePath).exists()) {
			throw new IllegalArgumentException("Import fixture not found: ${filePath}")
		}

		// The application hides this input, so make it visible before sending the file path.
		WebUI.executeJavaScript('document.querySelector(".file-input").style.display = "block";', null)
		WebUI.uploadFile(findTestObject('Page_SmartPantry/input_file-input'), filePath)
	}

	@And('I confirm the overwrite')
	def confirmOverwrite() {
		WebUI.waitForAlert(5)
		WebUI.acceptAlert()
	}

	@Then('I should see the imported data')
	def shouldSeeImportedData() {
		// The application reloads the page after a successful import.
		TestObject importedProfile = new TestObject('Perfil Importado')
		importedProfile.addProperty('xpath', ConditionType.EQUALS, "//*[contains(normalize-space(.), 'Perfil Importado')]")
		WebUI.waitForElementVisible(importedProfile, 15)

		TestObject importedItem = new TestObject('Maçã')
		importedItem.addProperty('xpath', ConditionType.EQUALS, "//td[normalize-space(.) = 'Maçã']")
		WebUI.waitForElementVisible(importedItem, 15)

		WebUI.verifyTextPresent('Perfil Importado', false, FailureHandling.STOP_ON_FAILURE)
		WebUI.verifyTextPresent('Maçã', false, FailureHandling.STOP_ON_FAILURE)
		WebUI.verifyTextNotPresent('Teste', false, FailureHandling.STOP_ON_FAILURE)
	}
}
