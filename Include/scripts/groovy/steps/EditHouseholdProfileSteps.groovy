package steps

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import io.cucumber.java.en.Then
import io.cucumber.java.en.When

/**
 * SMAR-64 - editing an existing household profile from the settings page.
 *
 * SettingsView lists every stored profile with an "Editar" action that opens
 * the setup form with the profile already loaded (SetupView reads the
 * ?id=...&from=settings query). Saving from that flow returns to the settings
 * page with the "Perfil atualizado com sucesso." confirmation, while cancelling
 * keeps the stored profile untouched. Editing an id that no longer exists shows
 * the "Perfil não encontrado" page instead of silently creating a profile.
 */
class EditHouseholdProfileSteps {

    private static final String BASE_URL = 'http://localhost:5173'

    @When('I open the settings page')
    def openSettingsPage() {
        WebUI.waitForElementClickable(testObject('button_Definicoes'), 10)
        WebUI.click(testObject('button_Definicoes'))
        WebUI.waitForElementVisible(testObject('ul_Settings profile list'), 10)
    }

    @Then('the settings list shows the profile {string} with the {string} diet')
    def settingsListShowsProfile(String name, String diet) {
        Map row = profileRow(name)

        if (!row) {
            KeywordUtil.markFailedAndStop('The settings list does not show the profile ' + name)
        }
        if (row.diet != diet) {
            KeywordUtil.markFailedAndStop(
                'The settings list shows the diet ' + row.diet + ' for ' + name + ', expected ' + diet)
        }
    }

    @Then('the settings list marks {string} as the active profile')
    def settingsListMarksProfileAsActive(String name) {
        Map row = profileRow(name)

        if (!row) {
            KeywordUtil.markFailedAndStop('The settings list does not show the profile ' + name)
        }
        if (!row.active) {
            KeywordUtil.markFailedAndStop('The settings list does not mark ' + name + ' as the active profile')
        }
    }

    @Then('the settings list shows exactly {int} profile(s)')
    def settingsListShowsProfiles(int expected) {
        int count = profileRows().size()

        if (count != expected) {
            KeywordUtil.markFailedAndStop('The settings list shows ' + count + ' profile(s), expected ' + expected)
        }
    }

    @Then('the settings list does not show the profile {string}')
    def settingsListDoesNotShowProfile(String name) {
        if (profileRow(name)) {
            KeywordUtil.markFailedAndStop('The settings list still shows the profile ' + name)
        }
    }

    @When('I edit the {string} profile from the settings')
    def editProfileFromSettings(String name) {
        TestObject editButton = byXpath(
            "//li[contains(@class,'profile-item')][.//span[contains(@class,'profile-name')][contains(normalize-space(.), '" +
            name + "')]]//button[contains(@class,'btn-edit')]")

        WebUI.waitForElementClickable(editButton, 10)
        WebUI.click(editButton)
        WebUI.waitForElementVisible(testObject('input_Nome do perfil'), 10)
    }

    @When('I change the profile name to {string} and the diet to {string} and save the changes')
    def changeProfileAndSave(String name, String diet) {
        setProfileName(name)
        WebUI.selectOptionByValue(testObject('select_Dieta OmnvoroVegetarianoVeganoPescetaria'), diet, false)
        WebUI.waitForElementClickable(testObject('button_Guardar alteracoes'), 10)
        WebUI.click(testObject('button_Guardar alteracoes'))
        WebUI.waitForElementVisible(testObject('ul_Settings profile list'), 15)
    }

    @When('I change the profile name to {string} without saving')
    def changeProfileNameOnly(String name) {
        setProfileName(name)
    }

    @When('I check the {string} intolerance and the {string} restriction')
    def checkIntoleranceAndRestriction(String intolerance, String restriction) {
        checkOption(intolerance)
        checkOption(restriction)
    }

    @Then('the profile form has the {string} intolerance and the {string} restriction checked')
    def profileFormHasOptionsChecked(String intolerance, String restriction) {
        verifyOptionChecked(intolerance)
        verifyOptionChecked(restriction)
    }

    @When('I cancel the edit and go back to the settings')
    def cancelEdit() {
        WebUI.waitForElementClickable(testObject('a_Settings cancel'), 10)
        WebUI.click(testObject('a_Settings cancel'))
        WebUI.waitForElementVisible(testObject('ul_Settings profile list'), 10)
    }

    @When('I go back to the dashboard from the settings')
    def goBackToDashboard() {
        WebUI.waitForElementClickable(testObject('button_Settings back'), 10)
        WebUI.click(testObject('button_Settings back'))
        WebUI.waitForElementVisible(testObject('h1_Dashboard title'), 15)
    }

    @Then('I am back on the settings page')
    def backOnSettingsPage() {
        WebUI.waitForElementVisible(testObject('ul_Settings profile list'), 15)
    }

    @Then('I see the profile updated confirmation')
    def seeProfileUpdatedConfirmation() {
        WebUI.waitForElementVisible(testObject('p_Profile updated alert'), 10)

        String message = WebUI.getText(testObject('p_Profile updated alert')).trim()

        if (!message.contains('Perfil atualizado com sucesso')) {
            KeywordUtil.markFailedAndStop('The settings confirmation reads ' + message)
        }
    }

    @When('I open the edit form for a profile that no longer exists')
    def openEditFormForMissingProfile() {
        WebUI.navigateToUrl(BASE_URL + '/setup?id=00000000-0000-0000-0000-000000000000&from=settings')
        WebUI.waitForElementVisible(testObject('h1_Profile not found'), 10)
    }

    @Then('I see the profile not found page')
    def seeProfileNotFoundPage() {
        WebUI.waitForElementVisible(testObject('h1_Profile not found'), 10)

        String heading = WebUI.getText(testObject('h1_Profile not found')).trim()

        if (heading != 'Perfil não encontrado') {
            KeywordUtil.markFailedAndStop('The edit form shows the heading ' + heading + ', expected Perfil não encontrado')
        }
        WebUI.verifyElementVisible(testObject('p_Profile not found message'))
    }

    private static void setProfileName(String name) {
        WebUI.waitForElementVisible(testObject('input_Nome do perfil'), 10)
        WebUI.clearText(testObject('input_Nome do perfil'))
        WebUI.setText(testObject('input_Nome do perfil'), name)
    }

    private static void checkOption(String label) {
        TestObject checkbox = byXpath(
            "//label[contains(@class,'check')][normalize-space()='" + label + "']//input")

        WebUI.waitForElementClickable(checkbox, 10)
        if (!isChecked(label)) {
            WebUI.click(checkbox)
        }
    }

    private static void verifyOptionChecked(String label) {
        if (!isChecked(label)) {
            KeywordUtil.markFailedAndStop('The profile form does not have ' + label + ' checked')
        }
    }

    private static boolean isChecked(String label) {
        String quoted = "'" + label.replace('\\', '\\\\').replace("'", "\\'") + "'"

        return (WebUI.executeJavaScript(
            "var labels = document.querySelectorAll('label.check'); for (var i = 0; i < labels.length; i++) { if (labels[i].textContent.trim() === " +
            quoted + ") { var input = labels[i].querySelector('input'); return input ? input.checked : false; } } return false;",
            null) ?: false) as boolean
    }

    private static Map profileRow(String name) {
        return profileRows().find { it.name == name }
    }

    private static List<Map> profileRows() {
        def rows = WebUI.executeJavaScript(
            "return Array.prototype.map.call(document.querySelectorAll('li.profile-item'), function (item) {" +
            " var name = item.querySelector('.profile-name');" +
            " var diet = item.querySelector('.profile-diet');" +
            " return { name: name && name.childNodes[0] ? name.childNodes[0].textContent.trim() : ''," +
            " diet: diet ? diet.textContent.trim() : ''," +
            " active: !!item.querySelector('.badge') };" +
            " });",
            null)

        return rows == null ? [] : (List<Map>) rows
    }

    private static TestObject byXpath(String xpath) {
        TestObject object = new TestObject()

        object.addProperty('xpath', ConditionType.EQUALS, xpath)

        return object
    }

    private static TestObject testObject(String name) {
        return findTestObject('Page_SmartPantry/' + name)
    }
}
