package steps

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import io.cucumber.java.en.Given

/**
 * Shared starting points for the tests added for SMAR-7, SMAR-43, SMAR-46 and
 * SMAR-50. The application keeps all of its data in localStorage, so every
 * scenario starts from a known empty state instead of relying on the state
 * left by a previous execution.
 */
class CleanStateSteps {

    @Given('I start the application with no profiles in the {string} url')
    def startWithNoProfiles(String url) {
        openCleanApplication(url)
        WebUI.waitForElementVisible(findTestObject('Page_SmartPantry/h1_Novo perfil'), 15)
    }

    @Given('I start the application with a clean pantry in the {string} url')
    def startWithCleanPantry(String url) {
        openCleanApplication(url)

        WebUI.setText(findTestObject('Page_SmartPantry/input_Nome do perfil'), 'Teste')
        WebUI.click(findTestObject('Page_SmartPantry/button_Comear'))
        WebUI.waitForElementVisible(findTestObject('Page_SmartPantry/h1_Dashboard title'), 15)
    }

    private static void openCleanApplication(String url) {
        WebUI.openBrowser('')
        WebUI.navigateToUrl(url)
        WebUI.executeJavaScript('window.localStorage.clear();', null)
        WebUI.refresh()
        WebUI.waitForElementPresent(findTestObject('Page_SmartPantry/input_Nome do perfil'), 15)
    }
}
