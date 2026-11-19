package steps

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import io.cucumber.java.en.Then
import io.cucumber.java.en.When

import org.openqa.selenium.By
import org.openqa.selenium.WebElement

/**
 * SMAR-58 - alert dialog for finished cooking timers.
 *
 * The cook mode renders TimerAlert.vue for every finished timer: a role=alertdialog
 * overlay with one list entry per finished timer, a Repetir button per entry and a
 * single OK, continuar dismiss button. The cooking guide navigation steps live in
 * StepTimers.groovy (SMAR-57) and are reused here.
 */
class TimerAlert {

    private static final int MAX_TIMER_SECONDS = 6 * 3600

    private static String lastFinishedLabel = ''
    private static int lastFinishedStep = -1

    private static TestObject testObject(String name) {
        return findTestObject('Page_SmartPantry/' + name)
    }

    private static List<WebElement> findElements(String objectName, int timeout) {
        try {
            List<WebElement> found = WebUI.findWebElements(testObject(objectName), timeout)
            return found == null ? [] : found
        } catch (Exception ignored) {
            return []
        }
    }

    private static int activeStepNumber() {
        return Integer.parseInt(WebUI.getAttribute(testObject('div_Progresso da receita'), 'aria-valuenow').trim())
    }

    private static int activeStepCount() {
        return Integer.parseInt(WebUI.getAttribute(testObject('div_Progresso da receita'), 'aria-valuemax').trim())
    }

    private static int secondsFromLabel(String label) {
        int seconds = 0
        def matcher = (label ?: '') =~ /(\d+)\s*(h|min|s)/
        while (matcher.find()) {
            int value = Integer.parseInt(matcher.group(1))
            String unit = matcher.group(2)
            seconds += unit == 'h' ? value * 3600 : (unit == 'min' ? value * 60 : value)
        }
        return seconds
    }

    private static String formatClock(int totalSeconds) {
        int seconds = Math.max(0, totalSeconds)
        int hours = seconds.intdiv(3600)
        int minutes = (seconds % 3600).intdiv(60)
        int remainder = seconds % 60
        if (hours > 0) return String.format('%d:%02d:%02d', hours, minutes, remainder)
        return String.format('%d:%02d', minutes, remainder)
    }

    private static int clockToSeconds(String clock) {
        int seconds = 0
        for (String part : (clock ?: '').trim().split(':')) {
            seconds = seconds * 60 + Integer.parseInt(part.trim())
        }
        return seconds
    }

    private static List<WebElement> timerRows() {
        return findElements('div_Timer rows', 5)
    }

    private static String rowClock(WebElement row) {
        return row.findElement(By.className('timer-clock')).getText().trim()
    }

    private static String rowMeta(WebElement row) {
        return row.findElement(By.className('timer-meta')).getText().trim()
    }

    private static List<String> metaParts(WebElement row) {
        List<String> parts = []
        for (String part : rowMeta(row).split('·')) {
            if (part.trim()) parts << part.trim()
        }
        return parts
    }

    private static void rememberTimerOfRow(WebElement row) {
        List<String> parts = metaParts(row)
        lastFinishedLabel = parts.isEmpty() ? '' : parts[0]
        lastFinishedStep = -1
        if (parts.size() > 1) {
            def matcher = parts[1] =~ /(\d+)/
            if (matcher.find()) lastFinishedStep = Integer.parseInt(matcher.group(1))
        }
        if (!lastFinishedLabel || lastFinishedStep < 0) {
            KeywordUtil.markFailedAndStop('Could not read the timer label and step from ' + rowMeta(row))
        }
    }

    private static String normalized(String text) {
        return (text ?: '').replaceAll('\\s+', ' ').trim()
    }

    private static void assertAlertItemMatches(WebElement item, WebElement row) {
        List<String> parts = metaParts(row)
        String itemText = normalized(item.getText())
        if (!parts.isEmpty() && !itemText.contains(parts[0])) {
            KeywordUtil.markFailedAndStop('The timer alert item ' + itemText + ' does not name the timer ' + parts[0])
        }
        if (parts.size() > 1 && !itemText.contains(parts[1])) {
            KeywordUtil.markFailedAndStop('The timer alert item ' + itemText + ' does not name the step ' + parts[1])
        }
    }

    @When("I start the timer for the current step")
    def startTimerForCurrentStep() {
        int stepNumber = activeStepNumber()
        int rowsBefore = timerRows().size()
        List<WebElement> presets = findElements('button_Temporizador do passo', 10)
        if (presets.isEmpty()) {
            KeywordUtil.markFailedAndStop('Step ' + stepNumber + ' offers no timer preset')
        }

        WebUI.click(testObject('button_Temporizador do passo'))
        WebUI.waitForElementVisible(testObject('div_Temporizadores ativos'), 10)
        WebUI.delay(1)

        List<WebElement> rows = timerRows()
        if (rows.size() != rowsBefore + 1) {
            KeywordUtil.markFailedAndStop('Expected a new timer row for step ' + stepNumber + ', found ' + rows.size() + ' row(s)')
        }
        String meta = rowMeta(rows.get(rows.size() - 1))
        if (!meta.contains('Passo ' + stepNumber)) {
            KeywordUtil.markFailedAndStop('The new timer shows ' + meta + ', expected Passo ' + stepNumber)
        }
    }

    @When("I open the next step with a step timer")
    def openNextStepWithStepTimer() {
        int total = activeStepCount()
        int current = activeStepNumber()
        int guard = 0

        while (current < total && guard < total) {
            WebUI.click(testObject('button_Seguinte'))
            WebUI.waitForElementAttributeValue(testObject('div_Progresso da receita'), 'aria-valuenow', String.valueOf(current + 1), 10)
            WebUI.delay(1)
            current++
            guard++
            if (!findElements('button_Temporizador do passo', 1).isEmpty()) {
                return
            }
        }

        KeywordUtil.markFailedAndStop('No later step of this recipe offers a timer preset')
    }

    @When("the running timers reach zero")
    def runningTimersReachZero() {
        // Simulate the elapsing clock instead of waiting minutes: every running timer
        // compares Date.now() with its stored end time, so one shifted tick finishes them all.
        WebUI.executeJavaScript('window.__katalonTimerRealNow = Date.now; window.__katalonTimerOffset = 0; Date.now = function () { return window.__katalonTimerRealNow() + window.__katalonTimerOffset; };', null)
        WebUI.delay(1)
        WebUI.executeJavaScript('window.__katalonTimerOffset = ' + ((MAX_TIMER_SECONDS + 120) * 1000) + ';', null)
        WebUI.delay(2)
        WebUI.executeJavaScript('if (window.__katalonTimerRealNow) { Date.now = window.__katalonTimerRealNow; }', null)
    }

    @Then("I see the timer alert dialog")
    def seeTimerAlertDialog() {
        WebUI.verifyElementVisible(testObject('div_Timer alert'))
        WebUI.verifyElementVisible(testObject('div_Timer alert card'))

        String role = WebUI.getAttribute(testObject('div_Timer alert card'), 'role') ?: ''
        if (role != 'alertdialog') {
            KeywordUtil.markFailedAndStop('The timer alert role is ' + role + ', expected alertdialog')
        }
        String ariaModal = WebUI.getAttribute(testObject('div_Timer alert card'), 'aria-modal') ?: ''
        if (ariaModal != 'true') {
            KeywordUtil.markFailedAndStop('The timer alert aria-modal is ' + ariaModal + ', expected true')
        }
        String title = WebUI.getText(testObject('h2_Timer alert title')).trim()
        if (title != 'Tempo terminado!') {
            KeywordUtil.markFailedAndStop('The timer alert title is ' + title)
        }
        WebUI.verifyElementVisible(testObject('p_Timer alert description'))
        WebUI.verifyElementPresent(testObject('button_OK continuar'), 5)
        WebUI.verifyElementPresent(testObject('button_Repetir temporizador'), 5)

        String dismissText = WebUI.getText(testObject('button_OK continuar')).trim()
        if (dismissText != 'OK, continuar') {
            KeywordUtil.markFailedAndStop('The dismiss button reads ' + dismissText)
        }
    }

    @Then("the timer alert describes one finished timer")
    def alertDescribesOneFinishedTimer() {
        List<WebElement> rows = timerRows()
        if (rows.size() != 1) {
            KeywordUtil.markFailedAndStop('Expected one finished timer row, found ' + rows.size())
        }
        rememberTimerOfRow(rows.get(0))

        String description = WebUI.getText(testObject('p_Timer alert description')).trim()
        if (description != 'Um temporizador chegou ao fim.') {
            KeywordUtil.markFailedAndStop('The timer alert description is ' + description)
        }

        List<WebElement> items = findElements('li_Timer alert item', 5)
        if (items.size() != 1) {
            KeywordUtil.markFailedAndStop('Expected one timer in the alert, found ' + items.size())
        }
        assertAlertItemMatches(items.get(0), rows.get(0))
    }

    @When("I dismiss the timer alert")
    def dismissTimerAlert() {
        WebUI.click(testObject('button_OK continuar'))
        WebUI.waitForElementNotPresent(testObject('div_Timer alert'), 10)
    }

    @Then("the timer alert is gone and the finished timer is removed")
    def alertGoneAndTimerRemoved() {
        WebUI.verifyElementNotPresent(testObject('div_Timer alert'), 5)
        WebUI.verifyTextNotPresent('Tempo terminado', false)
        int rows = timerRows().size()
        if (rows > 0) {
            KeywordUtil.markFailedAndStop('The finished timer is still in the timer panel (' + rows + ' row(s))')
        }
        if (WebUI.verifyElementPresent(testObject('div_Temporizadores ativos'), 1, FailureHandling.OPTIONAL)) {
            KeywordUtil.markFailedAndStop('The active timers panel is still visible after dismissing the last finished timer')
        }
    }

    @When("I restart the finished timer from the alert")
    def restartFinishedTimer() {
        List<WebElement> rows = timerRows()
        if (rows.isEmpty()) {
            KeywordUtil.markFailedAndStop('There is no finished timer to restart')
        }
        rememberTimerOfRow(rows.get(0))
        WebUI.click(testObject('button_Repetir temporizador'))
        WebUI.waitForElementNotPresent(testObject('div_Timer alert'), 10)
    }

    @Then("the timer runs again from its full duration")
    def timerRunsFromFullDuration() {
        WebUI.verifyElementVisible(testObject('div_Temporizadores ativos'))
        List<WebElement> rows = timerRows()
        if (rows.size() != 1) {
            KeywordUtil.markFailedAndStop('Expected the restarted timer row, found ' + rows.size() + ' row(s)')
        }

        String expectedClock = formatClock(secondsFromLabel(lastFinishedLabel))
        String clock = rowClock(rows.get(0))
        if (clock != expectedClock) {
            KeywordUtil.markFailedAndStop('The restarted timer shows ' + clock + ', expected ' + expectedClock + ' for ' + lastFinishedLabel)
        }

        List<String> parts = metaParts(rows.get(0))
        if (parts.isEmpty() || !parts[0].contains(lastFinishedLabel)) {
            KeywordUtil.markFailedAndStop('The restarted timer lost its label: ' + rowMeta(rows.get(0)))
        }
        if (lastFinishedStep > 0 && !rowMeta(rows.get(0)).contains('Passo ' + lastFinishedStep)) {
            KeywordUtil.markFailedAndStop('The restarted timer lost its step: ' + rowMeta(rows.get(0)))
        }

        String rowClass = rows.get(0).getAttribute('class') ?: ''
        if (rowClass.contains('finished')) {
            KeywordUtil.markFailedAndStop('The restarted timer is still marked as finished')
        }
        WebUI.verifyElementPresent(testObject('button_Pausar temporizador'), 5)

        int before = clockToSeconds(rowClock(rows.get(0)))
        WebUI.delay(2)
        int after = clockToSeconds(rowClock(timerRows().get(0)))
        if (after >= before) {
            KeywordUtil.markFailedAndStop('The restarted timer is not counting down (was ' + before + 's, now ' + after + 's)')
        }
    }

    @Then("the timer alert lists both finished timers")
    def alertListsBothFinishedTimers() {
        List<WebElement> rows = timerRows()
        if (rows.size() != 2) {
            KeywordUtil.markFailedAndStop('Expected two finished timer rows, found ' + rows.size())
        }

        String description = WebUI.getText(testObject('p_Timer alert description')).trim()
        if (description != 'Temporizadores chegaram ao fim.') {
            KeywordUtil.markFailedAndStop('The timer alert description is ' + description)
        }

        List<WebElement> items = findElements('li_Timer alert item', 5)
        if (items.size() != 2) {
            KeywordUtil.markFailedAndStop('Expected two timers in the alert, found ' + items.size())
        }
        for (int i = 0; i < 2; i++) {
            assertAlertItemMatches(items.get(i), rows.get(i))
        }
        String firstClock = rowClock(rows.get(0))
        String secondClock = rowClock(rows.get(1))
        if (firstClock != '0:00' || secondClock != '0:00') {
            KeywordUtil.markFailedAndStop('The finished timers show ' + firstClock + ' and ' + secondClock + ', expected 0:00')
        }
    }

    @When("I restart the first finished timer from the alert")
    def restartFirstFinishedTimer() {
        List<WebElement> rows = timerRows()
        if (rows.size() != 2) {
            KeywordUtil.markFailedAndStop('Expected two finished timer rows, found ' + rows.size())
        }
        rememberTimerOfRow(rows.get(0))
        WebUI.click(testObject('button_Repetir temporizador'))
        WebUI.delay(1)
    }

    @Then("the timer alert still shows the other finished timer")
    def alertShowsOtherFinishedTimer() {
        WebUI.verifyElementVisible(testObject('div_Timer alert'))

        String description = WebUI.getText(testObject('p_Timer alert description')).trim()
        if (description != 'Um temporizador chegou ao fim.') {
            KeywordUtil.markFailedAndStop('The timer alert description is ' + description)
        }

        List<WebElement> items = findElements('li_Timer alert item', 5)
        if (items.size() != 1) {
            KeywordUtil.markFailedAndStop('Expected one timer left in the alert, found ' + items.size())
        }

        List<WebElement> rows = timerRows()
        if (rows.size() != 2) {
            KeywordUtil.markFailedAndStop('Expected two timer rows, found ' + rows.size())
        }
        assertAlertItemMatches(items.get(0), rows.get(1))

        String restartedClass = rows.get(0).getAttribute('class') ?: ''
        if (restartedClass.contains('finished')) {
            KeywordUtil.markFailedAndStop('The restarted timer is still marked as finished')
        }
        if (rowClock(rows.get(1)) != '0:00') {
            KeywordUtil.markFailedAndStop('The remaining finished timer shows ' + rowClock(rows.get(1)))
        }
    }

    @Then("only the restarted timer is still running")
    def onlyRestartedTimerRunning() {
        WebUI.verifyElementNotPresent(testObject('div_Timer alert'), 5)
        List<WebElement> rows = timerRows()
        if (rows.size() != 1) {
            KeywordUtil.markFailedAndStop('Expected only the restarted timer, found ' + rows.size() + ' row(s)')
        }
        String rowClass = rows.get(0).getAttribute('class') ?: ''
        if (rowClass.contains('finished')) {
            KeywordUtil.markFailedAndStop('The remaining timer is marked as finished instead of running')
        }
        WebUI.verifyElementPresent(testObject('button_Pausar temporizador'), 5)

        int before = clockToSeconds(rowClock(rows.get(0)))
        WebUI.delay(2)
        int after = clockToSeconds(rowClock(timerRows().get(0)))
        if (after >= before) {
            KeywordUtil.markFailedAndStop('The restarted timer is not counting down (was ' + before + 's, now ' + after + 's)')
        }
    }

    @Then("I see no timer alert while the timer is running")
    def noAlertWhileTimerRunning() {
        WebUI.verifyElementNotPresent(testObject('div_Timer alert'), 5)
        WebUI.verifyElementVisible(testObject('div_Temporizadores ativos'))
        WebUI.verifyElementPresent(testObject('button_Pausar temporizador'), 5)
        String clock = rowClock(timerRows().get(0))
        if (clockToSeconds(clock) <= 0) {
            KeywordUtil.markFailedAndStop('The running timer shows ' + clock)
        }
    }
}
