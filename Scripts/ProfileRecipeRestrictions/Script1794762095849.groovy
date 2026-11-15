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

WebUI.click(findTestObject('Page_SmartPantry/input_Lactose'))

WebUI.click(findTestObject('Page_SmartPantry/input_Carne de Porco'))

WebUI.click(findTestObject('Page_SmartPantry/button_Comear'))

WebUI.click(findTestObject('Page_SmartPantry/button_Procurar receitas'))

WebUI.click(findTestObject('Page_SmartPantry/article_Garlicky Kale'))

WebUI.click(findTestObject('Page_SmartPantry/span_Dairy free'))

WebUI.click(findTestObject('Page_SmartPantry/span_Vegetarian'))

WebUI.click(findTestObject('Page_SmartPantry/button_Close'))

WebUI.setText(findTestObject('Page_SmartPantry/input_Pesquisar receitas (ex_ pasta)'), 'Pork')

WebUI.click(findTestObject('Page_SmartPantry/button_Comear'))

WebUI.click(findTestObject('Page_SmartPantry/p_Nenhuma receita encontrada'))

WebUI.click(findTestObject('Page_SmartPantry/a_Voltar'))

WebUI.click(findTestObject('Page_SmartPantry/button_Editar'))

WebUI.selectOptionByValue(findTestObject('Page_SmartPantry/select_Dieta OmnvoroVegetarianoVeganoPescetaria'), 'vegetarian', 
    false)

WebUI.click(findTestObject('Page_SmartPantry/input_Lactose'))

WebUI.click(findTestObject('Page_SmartPantry/input_Carne de Porco'))

WebUI.click(findTestObject('Page_SmartPantry/button_Comear'))

WebUI.click(findTestObject('Page_SmartPantry/button_Procurar receitas'))

WebUI.click(findTestObject('Page_SmartPantry/article_Garlicky Kale'))

WebUI.click(findTestObject('Page_SmartPantry/span_Dairy free'))

WebUI.click(findTestObject('Page_SmartPantry/button_Close'))

WebUI.closeBrowser()

WebUI.openBrowser('')

WebUI.navigateToUrl('http://localhost:5173/')

WebUI.selectOptionByValue(findTestObject('Page_SmartPantry/select_Dieta OmnvoroVegetarianoVeganoPescetaria'), 'vegetarian', 
    false)

WebUI.click(findTestObject('Page_SmartPantry/button_Comear'))

WebUI.setText(findTestObject('Page_SmartPantry/input_Nome do perfil'), 'fdfds')

WebUI.click(findTestObject('Page_SmartPantry/button_Comear'))

WebUI.click(findTestObject('Page_SmartPantry/button_Procurar receitas'))

WebUI.click(findTestObject('Page_SmartPantry/h3_Asparagus and Pea Soup_ Real Convenience Food'))

WebUI.click(findTestObject('Page_SmartPantry/article_Garlicky Kale'))

WebUI.click(findTestObject('Page_SmartPantry/span_Vegetarian'))

WebUI.closeBrowser()

