package steps

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import io.cucumber.java.en.Then
import io.cucumber.java.en.When

import org.openqa.selenium.WebElement

/**
 * SMAR-57 - step timers in cooking mode.
 *
 * The timer presets, the timer panel and the finished alert are read from the
 * rendered page. The finished alert is asserted in a version tolerant way: the
 * SMAR-57 build renders the inline ".timer-banner" with a "Parar" button, while
 * later builds render the ".timer-alert" dialog with an "OK, continuar" button.
 * Both contain the text "Tempo terminado", so the scenario accepts either.
 */
class StepTimers {

    private static final int MIN_TIMER_SECONDS = 10
    private static final int MAX_TIMER_SECONDS = 6 * 3600

    private static String presetLabel = ''
    private static int presetStep = -1
    private static int presetSeconds = -1
    private static int referenceClockSeconds = -1

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

    private static boolean hasTimerPresets() {
        if (!WebUI.verifyElementPresent(testObject('aside_Temporizadores do passo'), 1, FailureHandling.OPTIONAL)) {
            return false
        }
        return !findElements('button_Temporizador do passo', 1).isEmpty()
    }

    private static String cleanLabel(String raw) {
        return (raw ?: '').replaceAll('[^0-9a-zA-Z ]', ' ').replaceAll('\\s+', ' ').trim()
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

    private static String formatDuration(int totalSeconds) {
        int seconds = Math.max(1, totalSeconds)
        int hours = seconds.intdiv(3600)
        int minutes = (seconds % 3600).intdiv(60)
        int remainder = seconds % 60
        List<String> parts = []
        if (hours > 0) parts << "${hours} h"
        if (minutes > 0) parts << "${minutes} min"
        if (remainder > 0 && hours == 0) parts << "${remainder} s"
        return parts.join(' ')
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

    /** Mirrors the durations the app detects in a step text: numeric amount + time unit. */
    private static List<String> durationLabelsInText(String text) {
        List<String> labels = []
        Set<Integer> seen = new LinkedHashSet<>()
        def matcher = (text ?: '') =~ /(?i)(\d+(?:[.,]\d+)?)\s*(seconds?|secs?|minutes?|mins?|hours?|hrs?)\b/
        while (matcher.find()) {
            double amount = Double.parseDouble(matcher.group(1).replace(',', '.'))
            if (!(amount > 0)) continue
            String unit = matcher.group(2).toLowerCase()
            int multiplier = unit.startsWith('h') ? 3600 : (unit.startsWith('m') ? 60 : 1)
            int seconds = (int) Math.round(amount * multiplier)
            if (seconds < MIN_TIMER_SECONDS || seconds > MAX_TIMER_SECONDS) continue
            if (!seen.add(seconds)) continue
            labels << formatDuration(seconds)
        }
        return labels
    }

    private static void advanceToNextStep() {
        TestObject next = testObject('button_Seguinte')
        if (!WebUI.waitForElementClickable(next, 5, FailureHandling.OPTIONAL)) {
            return
        }
        int current = activeStepNumber()
        WebUI.click(next)
        WebUI.waitForElementAttributeValue(testObject('div_Progresso da receita'), 'aria-valuenow', String.valueOf(current + 1), 10)
        // The carousel takes 0.35s; let getText() read the settled slide.
        WebUI.delay(1)
    }

    @When("I open the first step with a step timer")
    def openFirstStepWithStepTimer() {
        WebUI.verifyElementPresent(testObject('div_Progresso da receita'), 10)
        int total = activeStepCount()
        int guard = 0

        while (guard <= total) {
            if (hasTimerPresets()) {
                return
            }
            if (activeStepNumber() >= total) break
            if (!WebUI.waitForElementClickable(testObject('button_Seguinte'), 5, FailureHandling.OPTIONAL)) break
            advanceToNextStep()
            guard++
        }

        KeywordUtil.markFailedAndStop('No step of the opened recipe offers a timer preset. This scenario needs a recipe whose steps mention a numeric cooking time.')
    }

    @Then("I see the timer presets for the current step")
    def seeTimerPresetsForCurrentStep() {
        WebUI.verifyElementVisible(testObject('aside_Temporizadores do passo'))
        String asideText = WebUI.getText(testObject('aside_Temporizadores do passo'))
        if (!asideText.contains('Temporizadores')) {
            KeywordUtil.markFailedAndStop("The timer panel of the step is missing its heading: '${asideText}'")
        }

        List<WebElement> presets = findElements('button_Temporizador do passo', 10)
        if (presets.isEmpty()) {
            KeywordUtil.markFailedAndStop('The current step shows no timer preset')
        }

        String stepText = WebUI.getText(testObject('p_active_step_text')).trim()
        List<String> expectedLabels = durationLabelsInText(stepText)
        if (expectedLabels.isEmpty()) {
            KeywordUtil.markFailedAndStop("The step text does not mention a numeric duration: '${stepText}'")
        }

        List<String> presetLabels = []
        for (WebElement preset : presets) {
            String label = cleanLabel(preset.getText())
            if (!(label ==~ /\d+ (h|min|s)( \d+ (h|min|s))?/)) {
                KeywordUtil.markFailedAndStop("Unexpected timer preset label '${label}'")
            }
            presetLabels << label
        }

        if (!presetLabels.any { expectedLabels.contains(it) }) {
            KeywordUtil.markFailedAndStop("Timer presets ${presetLabels} do not match the times in the step '${stepText}' (expected one of ${expectedLabels})")
        }
    }

    @When("I start the first step timer")
    def startFirstStepTimer() {
        presetStep = activeStepNumber()
        List<WebElement> presets = findElements('button_Temporizador do passo', 10)
        if (presets.isEmpty()) {
            KeywordUtil.markFailedAndStop('There is no timer preset to start on the current step')
        }

        presetLabel = cleanLabel(presets.get(0).getText())
        presetSeconds = secondsFromLabel(presetLabel)
        if (presetSeconds <= 0) {
            KeywordUtil.markFailedAndStop("Could not read the timer duration from '${presetLabel}'")
        }

        WebUI.click(testObject('button_Temporizador do passo'))
        WebUI.waitForElementVisible(testObject('div_Temporizadores ativos'), 10)
        WebUI.waitForElementVisible(testObject('div_Timer'), 10)

        String meta = WebUI.getText(testObject('span_Timer meta')).trim()
        if (!meta.contains(presetLabel) || !meta.contains("Passo ${presetStep}")) {
            KeywordUtil.markFailedAndStop("The timer panel shows '${meta}', expected '${presetLabel} · Passo ${presetStep}'")
        }

        String clock = WebUI.getText(testObject('span_Timer clock')).trim()
        if (!(clock ==~ /\d+:\d{2}(:\d{2})?/)) {
            KeywordUtil.markFailedAndStop("The created timer clock shows '${clock}'")
        }
    }

    @Then("I see the timer running in the timer panel")
    def seeTimerRunning() {
        WebUI.verifyElementVisible(testObject('div_Temporizadores ativos'))
        WebUI.verifyElementVisible(testObject('div_Timer'))
        WebUI.verifyElementPresent(testObject('button_Pausar temporizador'), 5)

        referenceClockSeconds = clockToSeconds(WebUI.getText(testObject('span_Timer clock')).trim())
        if (referenceClockSeconds <= 0 || referenceClockSeconds > presetSeconds) {
            KeywordUtil.markFailedAndStop("The running '${presetLabel}' timer shows ${referenceClockSeconds}s")
        }

        String meta = WebUI.getText(testObject('span_Timer meta')).trim()
        if (!meta.contains(presetLabel) || !meta.contains("Passo ${presetStep}")) {
            KeywordUtil.markFailedAndStop("The timer panel shows '${meta}'")
        }
    }

    @Then("the timer keeps running for the step it was started on")
    def timerKeepsRunning() {
        WebUI.verifyElementVisible(testObject('div_Temporizadores ativos'))
        String meta = WebUI.getText(testObject('span_Timer meta')).trim()
        if (!meta.contains(presetLabel) || !meta.contains("Passo ${presetStep}")) {
            KeywordUtil.markFailedAndStop("The timer no longer belongs to step ${presetStep}: '${meta}'")
        }
        if (activeStepNumber() == presetStep) {
            KeywordUtil.markFailedAndStop('The cooking guide did not move to another step')
        }

        WebUI.delay(2)
        int clockSeconds = clockToSeconds(WebUI.getText(testObject('span_Timer clock')).trim())
        if (clockSeconds >= referenceClockSeconds) {
            KeywordUtil.markFailedAndStop("The timer stopped running after moving to another step (was ${referenceClockSeconds}s, now ${clockSeconds}s)")
        }
    }

    @When("I tap the timer pause button")
    def tapTimerPauseButton() {
        WebUI.click(testObject('button_Pausar temporizador'))
        WebUI.waitForElementPresent(testObject('button_Retomar temporizador'), 5)
    }

    @Then("the timer is paused")
    def timerIsPaused() {
        String clock = WebUI.getText(testObject('span_Timer clock')).trim()
        referenceClockSeconds = clockToSeconds(clock)
        WebUI.delay(2)
        String clockAfterPause = WebUI.getText(testObject('span_Timer clock')).trim()
        if (clockAfterPause != clock) {
            KeywordUtil.markFailedAndStop("The paused timer kept counting down: '${clock}' -> '${clockAfterPause}'")
        }
    }

    @When("I tap the timer resume button")
    def tapTimerResumeButton() {
        WebUI.click(testObject('button_Retomar temporizador'))
        WebUI.waitForElementPresent(testObject('button_Pausar temporizador'), 5)
    }

    @Then("the timer counts down again")
    def timerCountsDownAgain() {
        WebUI.delay(2)
        int clockSeconds = clockToSeconds(WebUI.getText(testObject('span_Timer clock')).trim())
        if (clockSeconds >= referenceClockSeconds) {
            KeywordUtil.markFailedAndStop("The resumed timer is not counting down (was ${referenceClockSeconds}s, now ${clockSeconds}s)")
        }
    }

    @When("I tap the timer reset button")
    def tapTimerResetButton() {
        WebUI.delay(2)
        int clockSeconds = clockToSeconds(WebUI.getText(testObject('span_Timer clock')).trim())
        if (clockSeconds >= presetSeconds) {
            KeywordUtil.markFailedAndStop("The timer did not count down before the reset (still ${clockSeconds}s of ${presetSeconds}s)")
        }
        WebUI.click(testObject('button_Reiniciar temporizador'))
    }

    @Then("the timer is reset to its original duration")
    def timerIsReset() {
        String expected = formatClock(presetSeconds)
        String clock = WebUI.getText(testObject('span_Timer clock')).trim()
        if (clock != expected) {
            KeywordUtil.markFailedAndStop("The reset timer shows '${clock}', expected '${expected}' for ${presetLabel}")
        }

        String rowClass = WebUI.getAttribute(testObject('div_Timer'), 'class') ?: ''
        if (rowClass.contains('finished')) {
            KeywordUtil.markFailedAndStop('The reset timer is still marked as finished')
        }
        if (WebUI.verifyElementPresent(testObject('div_Timer alert'), 1, FailureHandling.OPTIONAL)
                || WebUI.verifyElementPresent(testObject('div_Timer banner'), 1, FailureHandling.OPTIONAL)) {
            KeywordUtil.markFailedAndStop('The finished timer alert is still shown after the reset')
        }
    }

    @When("I tap the timer close button")
    def tapTimerCloseButton() {
        WebUI.click(testObject('button_Fechar temporizador'))
    }

    @Then("I see no timers in the timer panel")
    def seeNoTimersInPanel() {
        WebUI.waitForElementNotPresent(testObject('div_Temporizadores ativos'), 5)
        int rows = findElements('div_Timer', 1).size()
        if (rows > 0) {
            KeywordUtil.markFailedAndStop("The timer panel still shows ${rows} timer(s) after closing the timer")
        }
    }

    @When("the running timer reaches zero")
    def runningTimerReachesZero() {
        // Simulate the elapsing clock instead of waiting several minutes: the timers
        // compare Date.now() with the stored end time, so shifting Date.now() for a
        // moment makes the next 500ms tick finish the timer.
        WebUI.executeJavaScript('window.__katalonTimerRealNow = Date.now; window.__katalonTimerOffset = 0; Date.now = function () { return window.__katalonTimerRealNow() + window.__katalonTimerOffset; };', null)
        WebUI.delay(1)
        WebUI.executeJavaScript('window.__katalonTimerOffset = ' + ((MAX_TIMER_SECONDS + 120) * 1000) + ';', null)
        WebUI.delay(2)
        WebUI.executeJavaScript('if (window.__katalonTimerRealNow) { Date.now = window.__katalonTimerRealNow; }', null)
    }

    @Then("I see the timer finished alert")
    def seeTimerFinishedAlert() {
        WebUI.verifyTextPresent('Tempo terminado', false)

        String clock = WebUI.getText(testObject('span_Timer clock')).trim()
        if (clock != '0:00') {
            KeywordUtil.markFailedAndStop("The finished timer shows '${clock}', expected '0:00'")
        }

        String rowClass = WebUI.getAttribute(testObject('div_Timer'), 'class') ?: ''
        if (!rowClass.contains('finished')) {
            KeywordUtil.markFailedAndStop("The finished timer is not marked as finished: '${rowClass}'")
        }

        boolean dialogVersion = WebUI.verifyElementPresent(testObject('div_Timer alert'), 2, FailureHandling.OPTIONAL)
        if (dialogVersion) {
            String alertText = WebUI.getText(testObject('div_Timer alert'))
            if (!alertText.contains('Tempo terminado')) {
                KeywordUtil.markFailedAndStop("The timer alert shows '${alertText}'")
            }
            WebUI.verifyElementPresent(testObject('button_OK continuar'), 5)
        } else {
            WebUI.verifyElementPresent(testObject('div_Timer banner'), 5)
            WebUI.verifyElementPresent(testObject('button_Parar'), 5)
        }
    }

    @When("I dismiss the timer finished alert")
    def dismissTimerFinishedAlert() {
        if (WebUI.verifyElementPresent(testObject('button_OK continuar'), 2, FailureHandling.OPTIONAL)) {
            WebUI.click(testObject('button_OK continuar'))
        } else if (WebUI.verifyElementPresent(testObject('button_Parar'), 2, FailureHandling.OPTIONAL)) {
            WebUI.click(testObject('button_Parar'))
        } else {
            KeywordUtil.markFailedAndStop('The timer finished alert has no dismiss button')
        }
    }

    @Then("I see the timer finished alert is gone")
    def timerFinishedAlertIsGone() {
        WebUI.verifyTextNotPresent('Tempo terminado', false)
        WebUI.waitForElementNotPresent(testObject('div_Timer alert'), 5)
        WebUI.waitForElementNotPresent(testObject('div_Timer banner'), 5)
        WebUI.waitForElementNotPresent(testObject('div_Temporizadores ativos'), 5)
        int rows = findElements('div_Timer', 1).size()
        if (rows > 0) {
            KeywordUtil.markFailedAndStop("The dismissed timer is still in the timer panel (${rows} row(s))")
        }
    }

    @When("I open a step without a step timer")
    def openStepWithoutStepTimer() {
        int total = activeStepCount()
        int guard = 0

        while (guard < total) {
            if (!hasTimerPresets()) {
                return
            }
            if (activeStepNumber() >= total) break
            if (!WebUI.waitForElementClickable(testObject('button_Seguinte'), 5, FailureHandling.OPTIONAL)) break
            advanceToNextStep()
            guard++
        }

        KeywordUtil.markFailedAndStop('Every step of the opened recipe offers a timer; this scenario needs a step without a numeric time.')
    }

    @Then("I see no timer presets for the current step")
    def seeNoTimerPresetsForCurrentStep() {
        if (WebUI.verifyElementPresent(testObject('aside_Temporizadores do passo'), 2, FailureHandling.OPTIONAL)) {
            KeywordUtil.markFailedAndStop('The current step shows the timer panel although its text mentions no time')
        }
        int presets = findElements('button_Temporizador do passo', 1).size()
        if (presets > 0) {
            KeywordUtil.markFailedAndStop("The current step shows ${presets} timer preset(s) although its text mentions no time")
        }
    }
}