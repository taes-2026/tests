import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.mobile.helper.MobileElementCommonHelper
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.exception.WebElementNotFoundException

import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

class ProfileSetup {

  @Given("I start the application at {string}")
  def startApplication(url) {
    WebUI.openBrowser(null)
    WebUI.navigateToUrl(url)
    WebUI.setText(findTestObject('Page_SmartPantry/input_Nome do perfil'), 'Teste')
  }

  @When("the onboarding is shown")
  def isOnboardingShowing() {
    WebUI.assertElementPresent(findTestObject('Page_SmartPantry/h1_Configurar perfil'), 0)   
  }

  @And("I fill in my household preferences and dietary restrictions")
  def fillPreferences() {
    WebUI.click(findTestObject('Page_SmartPantry/label_Lactose'))
    WebUI.click(findTestObject('Page_SmartPantry/label_Glten'))
  }

  @And("I confirm the profile preferences")
  def confirmPreferences() {
    WebUI.click(findTestObject('Page_SmartPantry/button_Comear'))
  }

  @And("I see the dashboard with my profile active")
  def isOnDashboard() {
    WebUI.assertElementPresent(findTestObject('Page_SmartPantry/h1_Dashboard'), 0)
  }

}

