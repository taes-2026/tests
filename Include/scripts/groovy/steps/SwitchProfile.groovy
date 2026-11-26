package steps

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import io.cucumber.java.en.Then
import io.cucumber.java.en.When

/**
 * SMAR-7 - switching the active profile from the dashboard.
 *
 * The dashboard header lists every stored profile in a select. Changing the
 * selection calls switchProfile(), which persists activeProfileId, and the
 * welcome text plus the diet badge must follow the newly active profile.
 */
class SwitchProfile {

    @When('I create the profile {string} with the {string} diet')
    def createProfile(String name, String diet) {
        WebUI.waitForElementVisible(testObject('input_Nome do perfil'), 10)
        WebUI.clearText(testObject('input_Nome do perfil'))
        WebUI.setText(testObject('input_Nome do perfil'), name)
        WebUI.selectOptionByValue(testObject('select_Dieta OmnvoroVegetarianoVeganoPescetaria'), diet, false)
        WebUI.click(testObject('button_Comear'))
        WebUI.waitForElementVisible(testObject('h1_Dashboard title'), 15)
    }

    @When('I open the new profile form')
	def openNewProfileForm() {
	    WebUI.waitForElementClickable(testObject('button_Definicoes'), 10)
	    WebUI.click(testObject('button_Definicoes'))
	    WebUI.waitForElementClickable(testObject('button_Novo perfil'), 10)
	    WebUI.click(testObject('button_Novo perfil'))
	    WebUI.waitForElementVisible(testObject('h1_Novo perfil'), 10)
	}

    @Then('the new profile form is empty')
    def newProfileFormIsEmpty() {
        String name = profileNameInput()

        if (name) {
            KeywordUtil.markFailedAndStop('The new profile form is prefilled with ' + name)
        }
    }

    @When('I open the edit profile form')
	def openEditProfileForm() {
	    WebUI.waitForElementClickable(testObject('button_Definicoes'), 10)
	    WebUI.click(testObject('button_Definicoes'))
	    WebUI.waitForElementClickable(testObject('button_Editar perfil'), 10)
	    WebUI.click(testObject('button_Editar perfil'))
	    WebUI.waitForElementVisible(testObject('h1_Editar perfil'), 10)
	}

    @Then('the edit profile form is filled with {string} and the {string} diet')
    def editProfileFormIsFilled(String name, String diet) {
        String actualName = profileNameInput()
        String actualDiet = selectedDiet()

        if (actualName != name) {
            KeywordUtil.markFailedAndStop('The edit profile form shows the name ' + actualName + ', expected ' + name)
        }
        if (actualDiet != diet) {
            KeywordUtil.markFailedAndStop('The edit profile form shows the diet ' + actualDiet + ', expected ' + diet)
        }
    }

    @When('I change the profile name to {string} and save')
    def changeProfileNameAndSave(String name) {
        WebUI.clearText(testObject('input_Nome do perfil'))
        WebUI.setText(testObject('input_Nome do perfil'), name)
        WebUI.waitForElementClickable(testObject('button_Guardar alteracoes'), 10)
        WebUI.click(testObject('button_Guardar alteracoes'))
        WebUI.waitForElementVisible(testObject('h1_Dashboard title'), 15)
    }

    @When('I select the {string} profile')
    def selectProfile(String name) {
        String value = profileValue(name)

        if (!value) {
            KeywordUtil.markFailedAndStop('The profile selector has no option for ' + name)
        }

        WebUI.selectOptionByValue(testObject('select_Perfil'), value, false)
        WebUI.delay(1)
    }

    @Then('the dashboard shows the {string} profile with the {string} diet')
    def dashboardShowsProfile(String name, String diet) {
        WebUI.waitForElementVisible(testObject('p_Bem-vindo'), 10)

        String welcome = WebUI.getText(testObject('p_Bem-vindo')).trim()
        if (!welcome.contains('Bem-vindo, ' + name)) {
            KeywordUtil.markFailedAndStop('The dashboard welcome reads ' + welcome + ', expected the profile ' + name)
        }
        if (!welcome.contains('(' + diet + ')')) {
            KeywordUtil.markFailedAndStop('The dashboard welcome reads ' + welcome + ', expected the diet ' + diet)
        }

        WebUI.verifyElementVisible(testObject('span_Diet badge'))
        String badge = WebUI.getText(testObject('span_Diet badge')).trim()
        if (badge != '(' + diet + ')') {
            KeywordUtil.markFailedAndStop('The diet badge reads ' + badge + ', expected (' + diet + ')')
        }
    }

    @Then('the profile selector offers exactly {int} profile(s)')
    def selectorOffersProfiles(int expected) {
        int count = profileOptions().size()

        if (count != expected) {
            KeywordUtil.markFailedAndStop('The profile selector lists ' + count + ' profile(s), expected ' + expected)
        }
    }

    @Then('the {string} profile is selected')
    def profileIsSelected(String name) {
        String selected = selectedProfile()

        if (!selected.startsWith(name)) {
            KeywordUtil.markFailedAndStop('The selected profile reads ' + selected + ', expected ' + name)
        }
    }

    @Then('the profile selector lists {string}')
    def selectorListsProfile(String name) {
        boolean found = profileOptions().any { it.startsWith(name) }

        if (!found) {
            KeywordUtil.markFailedAndStop('The profile selector options do not include ' + name)
        }
    }

    @When('I reload the dashboard')
    def reloadDashboard() {
        WebUI.refresh()
        WebUI.waitForElementVisible(testObject('h1_Dashboard title'), 15)
        WebUI.delay(1)
    }

    private static TestObject testObject(String name) {
        return findTestObject('Page_SmartPantry/' + name)
    }

    private static String selectedDiet() {
        return (WebUI.executeJavaScript(
            "var select = document.querySelector('select'); return select ? select.value : '';",
            null) ?: '').trim()
    }

    private static String profileNameInput() {
        return (WebUI.executeJavaScript(
            "var input = document.querySelector('input[autocomplete=\"name\"]'); return input ? input.value : '';",
            null) ?: '').trim()
    }

    private static List<String> profileOptions() {
        def options = WebUI.executeJavaScript(
            "return Array.prototype.map.call(document.querySelectorAll('#profile-select option'), function (option) { return option.textContent.trim(); });",
            null)

        return options == null ? [] : (List<String>) options
    }

    private static String selectedProfile() {
        return (WebUI.executeJavaScript(
            "var select = document.querySelector('#profile-select'); return select && select.selectedIndex >= 0 ? select.options[select.selectedIndex].textContent.trim() : '';",
            null) ?: '').trim()
    }

    private static String profileValue(String name) {
        String quoted = "'" + name.replace('\\', '\\\\').replace("'", "\\'") + "'"

        return (WebUI.executeJavaScript(
            "var option = Array.prototype.find.call(document.querySelectorAll('#profile-select option'), function (item) { return item.textContent.trim().indexOf(" + quoted + ") === 0; }); return option ? option.value : '';",
            null) ?: '').trim()
    }
}
