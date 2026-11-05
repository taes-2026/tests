import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.llm.keyword.LlmKeywords as LLM
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

WebUI.openBrowser(null)

WebUI.navigateToUrl('http://localhost:5173/')

WebUI.setText(findTestObject('Page_SmartPantry/input_Email'), 'teste2@teste.com')

WebUI.setEncryptedText(findTestObject('Page_SmartPantry/input_Palavra-passe'), 'crC3rOCPCmDOlulPEw3lcQ==')

WebUI.click(findTestObject('Page_SmartPantry/button_Entrar'))

WebUI.click(findTestObject('Page_SmartPantry/button_Adicionar produto'))

WebUI.click(findTestObject('Page_SmartPantry/button_Digitalizar cdigo de barras'))

WebUI.assertElementPresent(findTestObject('Page_SmartPantry/p_Produto no encontrado no catlogo. Introduz o'), 0)

WebUI.click(findTestObject('Page_SmartPantry/button_Introduzir manualmente'))

WebUI.setText(findTestObject('Page_SmartPantry/input_Nome do produto'), 'teste')

WebUI.selectOptionByValue(findTestObject('Page_SmartPantry/select_Categoria FrutasLegumesCarnesPeixeLaticn'), 'meat', false)

WebUI.setText(findTestObject('Page_SmartPantry/input_Data de validade'), '2026-11-19')

WebUI.selectOptionByValue(findTestObject('Page_SmartPantry/select_Unidade Unidade(s)kggLmlEmbalagem'), 'g', false)

WebUI.click(findTestObject('Page_SmartPantry/button_Guardar'))

WebUI.closeBrowser()

