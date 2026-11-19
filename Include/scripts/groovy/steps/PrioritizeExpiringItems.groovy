package steps

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint

import io.cucumber.java.en.Given
import io.cucumber.java.en.And
import io.cucumber.java.en.Then
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

public class PrioritizeExpiringItems {
	
	@Then("I should see at least one recipe card with {string} ingredient")
    def i_should_see_at_least_one_recipe_card_with_ingredient(String item) {
		WebUI.click(findTestObject('Page_SmartPantry/recipe_card'))
		WebUI.verifyElementPresent(findTestObject('Page_SmartPantry/li_ingredient', [('item'): item.toLowerCase()]), 10)
    }

}
