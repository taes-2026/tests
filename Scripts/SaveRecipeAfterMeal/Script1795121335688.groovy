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

// Bookmarks persist in localStorage, so start from a clean saved-recipes list.
WebUI.executeJavaScript("localStorage.removeItem('smartpantry:bookmarks')", null)

WebUI.refresh()

WebUI.waitForElementClickable(findTestObject('Page_SmartPantry/button_Procurar receitas'), 10)

WebUI.click(findTestObject('Page_SmartPantry/button_Procurar receitas'))

WebUI.setText(findTestObject('Page_SmartPantry/input_Pesquisar receitas (ex_ pasta)'), 'Roasted Broccoli')

WebUI.waitForElementClickable(findTestObject('Page_SmartPantry/button_Pesquisar receitas'), 20)

WebUI.click(findTestObject('Page_SmartPantry/button_Pesquisar receitas'))

TestObject recipeCard = findTestObject('Page_SmartPantry/recipe_card_by_title', [('title'): 'Roasted Broccoli with Lemon and Garlic'])

WebUI.waitForElementClickable(recipeCard, 20)

WebUI.click(recipeCard)

WebUI.waitForElementPresent(findTestObject('Page_SmartPantry/button_Comear a cozinhar'), 20)

WebUI.click(findTestObject('Page_SmartPantry/button_Comear a cozinhar'))

WebUI.waitForElementPresent(findTestObject('Page_SmartPantry/div_Modo cozinha'), 10)

TestObject progress = findTestObject('Page_SmartPantry/div_Progresso da receita')

WebUI.waitForElementPresent(progress, 10)

int totalSteps = Integer.parseInt(WebUI.getAttribute(progress, 'aria-valuemax'))

int currentStep = Integer.parseInt(WebUI.getAttribute(progress, 'aria-valuenow'))

while (currentStep < totalSteps) {
    WebUI.click(findTestObject('Page_SmartPantry/button_Seguinte'))

    WebUI.waitForElementAttributeValue(progress, 'aria-valuenow', String.valueOf(currentStep + 1), 10)

    currentStep++
}

WebUI.click(findTestObject('Page_SmartPantry/button_Concluir refeicao'))

WebUI.verifyElementPresent(findTestObject('Page_SmartPantry/h2_Refeicao concluida'), 10)

WebUI.click(findTestObject('Page_SmartPantry/button_Guardar receita'))

WebUI.verifyElementPresent(findTestObject('Page_SmartPantry/p_Receita guardada'), 10)

WebUI.click(findTestObject('Page_SmartPantry/button_Concluir'))

WebUI.waitForElementNotPresent(findTestObject('Page_SmartPantry/div_Modo cozinha'), 10)

WebUI.click(findTestObject('Page_SmartPantry/button_Close'))

WebUI.click(findTestObject('Page_SmartPantry/a_Voltar'))

WebUI.click(findTestObject('Page_SmartPantry/button_Receitas guardadas'))

WebUI.verifyElementPresent(findTestObject('Page_SmartPantry/recipe_card_by_title', [('title'): 'Roasted Broccoli with Lemon and Garlic']), 10)

WebUI.click(findTestObject('Page_SmartPantry/a_Voltar_1'))

WebUI.closeBrowser()
