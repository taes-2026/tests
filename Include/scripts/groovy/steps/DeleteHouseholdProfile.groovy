package steps

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.cucumber.java.en.And

import internal.GlobalVariable

public class DeleteHouseholdProfile {
	
	@When('I open the settings')
	def openSettings() {
		WebUI.click(findTestObject('Page_SmartPantry/button_Definicoes'))
	}

	@And('I delete the first profile')
	def deleteFirst() {
		WebUI.click(findTestObject('Page_SmartPantry/button_Eliminar'))
		WebUI.acceptAlert()
	}

	@And('I delete the second profile')
	def deleteSecond() {
		WebUI.click(findTestObject('Page_SmartPantry/button_Eliminar segundo'))
		WebUI.acceptAlert()
	}

	@And('I go back to the household dashboard')
	def backToDashboard() {
		WebUI.click(findTestObject('Page_SmartPantry/a_Voltar definicoes'))
	}
	
	@And('I tap the first Eliminar button')
	def tapFirst() {
		WebUI.click(findTestObject('Page_SmartPantry/button_Eliminar'))
		WebUI.waitForAlert(5)
	}
	
	@And('I cancel the deletion')
	def cancelDeletion() {
		WebUI.dismissAlert()
	}

	@Then('the settings list {int} profiles')
	def settingsList(int expected) {
	    int count = WebUI.findWebElements(
	        findTestObject('Page_SmartPantry/button_Eliminar_todos'), 5
	    ).size()
	
	    WebUI.verifyEqual(count, expected)
	}

	@Then('the first Eliminar button is disabled')
def firstIsDisabled() {
    WebUI.verifyElementAttributeValue(
        findTestObject('Page_SmartPantry/button_Eliminar'),
        'aria-disabled',
        'true',
        5
    )
}

}

