import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

WebUI.openBrowser('')

WebUI.navigateToUrl('http://localhost:5173/')

WebUI.setText(findTestObject('Page_SmartPantry/input_Nome do perfil'), 'Teste')

WebUI.click(findTestObject('Page_SmartPantry/button_Comear'))

WebUI.click(findTestObject('Page_SmartPantry/button_Procurar receitas'))

WebUI.setText(findTestObject('Page_SmartPantry/input_Pesquisar receitas (ex_ pasta)'), 'pasta')

WebUI.waitForElementClickable(findTestObject('Page_SmartPantry/button_Pesquisar receitas'), 20)

WebUI.click(findTestObject('Page_SmartPantry/button_Pesquisar receitas'))

WebUI.waitForElementClickable(findTestObject('Page_SmartPantry/recipe_card'), 20)

WebUI.click(findTestObject('Page_SmartPantry/recipe_card'))

WebUI.waitForElementPresent(findTestObject('Page_SmartPantry/button_Comear a cozinhar'), 20)

WebUI.click(findTestObject('Page_SmartPantry/button_Comear a cozinhar'))

WebUI.verifyElementVisible(findTestObject('Page_SmartPantry/div_Modo cozinha'))

WebUI.verifyElementAttributeValue(findTestObject('Page_SmartPantry/div_Progresso da receita'), 'aria-valuenow', '1', 
    10)

WebUI.verifyElementNotClickable(findTestObject('Page_SmartPantry/button_Anterior'))

WebUI.verifyTextPresent('Ingredientes deste passo', false)

WebUI.click(findTestObject('Page_SmartPantry/button_Seguinte'))

WebUI.verifyElementAttributeValue(findTestObject('Page_SmartPantry/div_Progresso da receita'), 'aria-valuenow', '2', 
    10)

WebUI.click(findTestObject('Page_SmartPantry/button_Anterior'))

WebUI.verifyElementAttributeValue(findTestObject('Page_SmartPantry/div_Progresso da receita'), 'aria-valuenow', '1', 
    10)

WebUI.click(findTestObject('Page_SmartPantry/button_Sair do modo cozinha'))

WebUI.verifyElementNotPresent(findTestObject('Page_SmartPantry/div_Modo cozinha'), 10)

WebUI.verifyElementPresent(findTestObject('Page_SmartPantry/button_Comear a cozinhar'), 10)

WebUI.closeBrowser()
