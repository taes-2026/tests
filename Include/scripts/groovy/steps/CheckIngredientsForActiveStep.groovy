package steps

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint


import io.cucumber.java.en.And
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

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

import internal.GlobalVariable

public class CheckIngredientsForActiveStep {
	
	
	@And("I open the first recipe in the recipe results")
	def openFirstRecipe() {
		WebUI.click(findTestObject('Page_SmartPantry/recipe_card'))
	}

	@And("I see at least one highlighted ingredient in the active step")
	def seeHighlightedIngredient() {
		WebUI.verifyElementPresent(findTestObject('Page_SmartPantry/mark_Ingrediente destacado'), 10, FailureHandling.STOP_ON_FAILURE)
	}

	@When("I tap the next step button")
	def tapNextStep() {
		WebUI.click(findTestObject('Page_SmartPantry/button_Seguinte'))
	}

	@And("I see the ingredients of the active step")
	def seeIngredientsOfActiveStep() {
		WebUI.verifyElementPresent(findTestObject('Page_SmartPantry/li_Ingrediente do passo'), 10, FailureHandling.STOP_ON_FAILURE)
	}

	@When("I go to a step without ingredients")
	def goToStepWithoutIngredients() {
		int maxSteps = 15
		for (int i = 0; i < maxSteps; i++) {
			if (WebUI.verifyElementPresent(findTestObject('Page_SmartPantry/p_Sem ingredientes especficos neste passo'), 1, FailureHandling.OPTIONAL)) {
				return
			}
			if (!WebUI.verifyElementClickable(findTestObject('Page_SmartPantry/button_Seguinte'), FailureHandling.OPTIONAL)) {
				break
			}
			WebUI.click(findTestObject('Page_SmartPantry/button_Seguinte'))
		}
}

@Then("I see the message {string}")
def seeMessage(String message) {
	WebUI.verifyElementPresent(findTestObject('Page_SmartPantry/p_Sem ingredientes especificos neste passo'), 5, FailureHandling.STOP_ON_FAILURE)
	WebUI.verifyElementText(findTestObject('Page_SmartPantry/p_Sem ingredientes especificos neste passo'), message, FailureHandling.STOP_ON_FAILURE)
}

}
