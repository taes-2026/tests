import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.mobile.helper.MobileElementCommonHelper
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.exception.WebElementNotFoundException

import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

class ManualProductEntrySteps {

   
    @Given("I am on the product entry screen")
    def navigateToProductEntry() {
        WebUI.openBrowser('')
        WebUI.navigateToUrl('http://localhost:5173/')
        WebUI.setText(findTestObject('Page_SmartPantry/input_Email'), 'teste2@teste.com')
        WebUI.setEncryptedText(findTestObject('Page_SmartPantry/input_Palavra-passe'), 'crC3rOCPCmDOluLPEw3lcQ==')
        WebUI.click(findTestObject('Page_SmartPantry/button_Entrar'))
        WebUI.click(findTestObject('Page_SmartPantry/button_Adicionar produto'))
    }

    
    @And("the camera is blocked or the product is not listed")
    def productNotListed() {
        WebUI.click(findTestObject('Page_SmartPantry/button_Digitalizar cdigo de barras'))
        // O "Assert Element Present" traduz-se para verifyElementPresent no Katalon
        WebUI.verifyElementPresent(findTestObject('Page_SmartPantry/p_Produto no encontrado no catlogo. Introduz o'), 5)
    }

   
    @When("I select the manual entry option")
    def selectManualEntry() {
        WebUI.click(findTestObject('Page_SmartPantry/button_Introduzir manualmente'))
    }

  
    @Then("I can manually enter the product details")
    def fillProductDetails() {
        WebUI.setText(findTestObject('Page_SmartPantry/input_Nome do produto'), 'teste')
        WebUI.selectOptionByValue(findTestObject('Page_SmartPantry/select_Categoria FrutasLegumesCarnesPeixeLaticn'), 'meat', false)
        WebUI.setText(findTestObject('Page_SmartPantry/input_Data de validade'), '2026-11-19')
        WebUI.selectOptionByValue(findTestObject('Page_SmartPantry/select_Unidade Unidade(s)kggLmlEmbalagem'), 'g', false)
        
      
        WebUI.click(findTestObject('Page_SmartPantry/button_Guardar'))
        
       
        WebUI.closeBrowser()
    }
}
