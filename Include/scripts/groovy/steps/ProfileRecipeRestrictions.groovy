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

import internal.GlobalVariable
import io.cucumber.java.en.Given
import io.cucumber.java.en.And
import io.cucumber.java.en.Then


public class ProfileRecipeRestrictions {
	
	@Given("I start the application in the {string} url with vegetarian diet and dairy intolerance")
	def startApplicationWithDietAndIntolerance(String url) {
		WebUI.openBrowser('')
		WebUI.navigateToUrl(url)
		WebUI.setText(findTestObject('Page_SmartPantry/input_Nome do perfil'), 'Teste')
		WebUI.selectOptionByValue(findTestObject('Page_SmartPantry/select_Dieta OmnvoroVegetarianoVeganoPescetaria'), 'vegetarian',
			false)
		WebUI.click(findTestObject('Page_SmartPantry/input_Lactose'))
		WebUI.click(findTestObject('Page_SmartPantry/button_Comear'))
	}	
	
	@And("I open the first recipe card")
	def openRecipeCard() {
		WebUI.click(findTestObject('Page_SmartPantry/article_Garlicky Kale'))
	}
	
	@Then ("I see the vegetarian and dairy free tags")
	def seeTags() {
		WebUI.verifyElementPresent(findTestObject('Page_SmartPantry/span_Dairy free'), 10)
		WebUI.verifyElementPresent(findTestObject('Page_SmartPantry/span_Vegetarian'), 10)
	}
	
	@Given("I start the application in the {string} url with omnivore diet and pork restriction")
	def startApplicationWithDietAndRestriction(String url) {
		WebUI.openBrowser('')
		WebUI.navigateToUrl(url)
		WebUI.setText(findTestObject('Page_SmartPantry/input_Nome do perfil'), 'Teste')
		WebUI.selectOptionByValue(findTestObject('Page_SmartPantry/select_Dieta OmnvoroVegetarianoVeganoPescetaria'), 'omnivore',
			false)
		WebUI.click(findTestObject('Page_SmartPantry/input_Carne de Porco'))
		WebUI.click(findTestObject('Page_SmartPantry/button_Comear'))
	}
	
	@And ("I search for pork in the search bar")
	def searchPorkInSearchBar () {
		WebUI.setText(findTestObject('Page_SmartPantry/input_Pesquisar receitas (ex_ pasta)'), 'Pork')
		WebUI.click(findTestObject('Page_SmartPantry/button_Comear'))
	}
	
	@Then ("I see there is no recipes")
	def checkIfThereIsNoRecipes() {
		WebUI.verifyElementPresent(findTestObject('Page_SmartPantry/p_Nenhuma receita encontrada'), 10)
	}


}
