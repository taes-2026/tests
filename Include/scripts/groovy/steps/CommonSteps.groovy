package steps
import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testcase.TestCaseFactory
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testdata.TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

import org.openqa.selenium.WebElement
import org.openqa.selenium.WebDriver
import org.openqa.selenium.By

import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.webui.driver.DriverFactory

import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObjectProperty

import com.kms.katalon.core.mobile.helper.MobileElementCommonHelper
import com.kms.katalon.core.util.KeywordUtil

import com.kms.katalon.core.webui.exception.WebElementNotFoundException

import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When



class CommonSteps {

    @Given("I start the application in the {string} url")
    def startApplication(String url) {
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

    @And("I save the item")
    def saveItem() {
        WebUI.click(findTestObject('Page_SmartPantry/button_Comear'))
    }

    @And("I close the browser")
    def closeBrowser() {
        WebUI.closeBrowser()
    }
}