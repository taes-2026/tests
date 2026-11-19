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

WebUI.openBrowser('')

WebUI.navigateToUrl('http://localhost:5173/')

WebUI.setText(findTestObject('Page_SmartPantry/input_Nome do perfil'), 'Teste')

WebUI.click(findTestObject('Page_SmartPantry/button_Comear'))

WebUI.click(findTestObject('Page_SmartPantry/button_Procurar receitas'))

WebUI.setText(findTestObject('Page_SmartPantry/input_Pesquisar receitas (ex_ pasta)'), 'Red Kidney Bean Jambalaya')

WebUI.waitForElementClickable(findTestObject('Page_SmartPantry/button_Pesquisar receitas'), 20)

WebUI.click(findTestObject('Page_SmartPantry/button_Pesquisar receitas'))

TestObject recipeCard = findTestObject('Page_SmartPantry/recipe_card_by_title', [('title'): 'Red Kidney Bean Jambalaya'])

WebUI.waitForElementClickable(recipeCard, 20)

WebUI.click(recipeCard)

WebUI.waitForElementClickable(findTestObject('Page_SmartPantry/button_Ver resumo da refeicao'), 20)

WebUI.click(findTestObject('Page_SmartPantry/button_Ver resumo da refeicao'))

WebUI.waitForElementVisible(findTestObject('Page_SmartPantry/div_Resumo da refeicao modal'), 10)

WebUI.verifyElementVisible(findTestObject('Page_SmartPantry/section_Resumo da refeicao'), 10)

WebUI.verifyElementVisible(findTestObject('Page_SmartPantry/p_Recipe name in summary', [('title'): 'Red Kidney Bean Jambalaya']), 10)

WebUI.verifyElementPresent(findTestObject('Page_SmartPantry/li_Ingrediente do resumo'), 10)

WebUI.verifyElementPresent(findTestObject('Page_SmartPantry/table_Informacao nutricional'), 10)

WebUI.click(findTestObject('Page_SmartPantry/button_Fechar resumo'))

WebUI.verifyElementNotPresent(findTestObject('Page_SmartPantry/div_Resumo da refeicao modal'), 10)

WebUI.click(findTestObject('Page_SmartPantry/button_Close'))

WebUI.closeBrowser()
