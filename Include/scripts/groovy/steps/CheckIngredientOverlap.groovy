package steps

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

import java.time.LocalDate

import org.openqa.selenium.By
import org.openqa.selenium.WebElement

/**
 * SMAR-43 - ingredient overlap in the recipe suggestions.
 *
 * RecipesView enriches every search result with calculateRecipeOverlap() and
 * sorts the cards so that recipes using expiring pantry items come first.
 * RecipeDetailModal shows the same calculation with one badge per ingredient:
 * "A expirar", "Em stock" or "Em falta".
 */
class CheckIngredientOverlap {

    private static final String OVERLAP_PATTERN = '^(\\d+)%$'

    @Given('I add the pantry item {string} expiring in {int} days')
    def addPantryItem(String name, int days) {
        WebUI.waitForElementClickable(testObject('button_Adicionar produto'), 10)
        WebUI.click(testObject('button_Adicionar produto'))

        WebUI.waitForElementClickable(testObject('button_Introduzir manualmente'), 10)
        WebUI.click(testObject('button_Introduzir manualmente'))

        WebUI.waitForElementVisible(testObject('input_Nome do produto'), 10)
        WebUI.setText(testObject('input_Nome do produto'), name)
        WebUI.selectOptionByValue(testObject('select_Categoria FrutasLegumesCarnesPeixeLaticn'), 'other', false)
        WebUI.setText(testObject('input_Quantidade'), '1')
        WebUI.setText(testObject('input_Data de validade'), LocalDate.now().plusDays(days).toString())

        WebUI.click(testObject('button_Guardar'))
        WebUI.waitForElementVisible(testObject('h1_Dashboard title'), 15)
    }

    @Then('the recipe cards show overlap percentages and stock counts')
    def cardsShowOverlap() {
        List<WebElement> cards = recipeCards()
        if (cards.size() < 2) {
            KeywordUtil.markFailedAndStop('Expected several recipe cards to compare, found ' + cards.size())
        }

        boolean foundMissing = false

        for (WebElement card : cards) {
            List<WebElement> badges = card.findElements(By.cssSelector('.card-overlap-badge'))
            if (badges.isEmpty()) {
                KeywordUtil.markFailedAndStop('A recipe card does not show an overlap percentage badge')
            }

            String badge = badges.get(0).getText().trim()
            if (!(badge ==~ OVERLAP_PATTERN)) {
                KeywordUtil.markFailedAndStop('The overlap badge reads ' + badge + ', expected a percentage')
            }

            List<WebElement> stats = card.findElements(By.cssSelector('.card-stats'))
            if (stats.isEmpty()) {
                KeywordUtil.markFailedAndStop('A recipe card does not show its stock counts')
            }

            String statsText = stats.get(0).getText().trim()
            if (!(statsText ==~ /(?s).*(\d+)\s+em stock.*/)) {
                KeywordUtil.markFailedAndStop('The recipe card stats read ' + statsText + ', expected an "em stock" count')
            }
            if (statsText ==~ /(?s).*\d+\s+em falta.*/) {
                foundMissing = true
            }
        }

        if (!foundMissing) {
            KeywordUtil.markFailedAndStop('No recipe card reports missing ingredients for the partial pantry')
        }
    }

    @When('I sort the recipes by {string}')
    def sortRecipes(String label) {
        WebUI.waitForElementVisible(testObject('select_Ordenar por'), 10)
        WebUI.selectOptionByLabel(testObject('select_Ordenar por'), label, false)
        WebUI.delay(1)
    }

    @Then('the recipes are ordered by descending overlap')
    def orderedByDescendingOverlap() {
        List<Integer> values = recipeCards().collect { card ->
            WebElement badge = card.findElements(By.cssSelector('.card-overlap-badge')).get(0)
            percentage(badge.getText().trim())
        }

        for (int i = 1; i < values.size(); i++) {
            if (values.get(i) > values.get(i - 1)) {
                KeywordUtil.markFailedAndStop('The overlap order is not descending: ' + values)
            }
        }
    }

    @Then('the recipes are ordered by ascending missing ingredients')
    def orderedByAscendingMissing() {
        List<Integer> values = recipeCards().collect { card ->
            String stats = card.findElements(By.cssSelector('.card-stats')).get(0).getText().trim()
            missingCount(stats)
        }

        for (int i = 1; i < values.size(); i++) {
            if (values.get(i) < values.get(i - 1)) {
                KeywordUtil.markFailedAndStop('The missing ingredient order is not ascending: ' + values)
            }
        }
    }

    @Then('the first recipe card uses expiring pantry items')
    def firstCardUsesExpiringItems() {
        List<WebElement> cards = recipeCards()
        if (cards.isEmpty()) {
            KeywordUtil.markFailedAndStop('There are no recipe cards to check')
        }

        WebElement first = cards.get(0)
        List<WebElement> badges = first.findElements(By.cssSelector('.card-expiring-badge'))
        if (badges.isEmpty() || badges.get(0).getText().trim() != 'A expirar') {
            KeywordUtil.markFailedAndStop('The first recipe card is not marked as using expiring ingredients')
        }

        List<WebElement> info = first.findElements(By.cssSelector('.card-expiring-info'))
        if (info.isEmpty()) {
            KeywordUtil.markFailedAndStop('The first recipe card does not name the expiring ingredient')
        }

        String text = info.get(0).getText().trim()
        if (!text.contains('Aproveita:') || !text.contains('Carrot')) {
            KeywordUtil.markFailedAndStop('The first recipe card expiring hint reads ' + text)
        }
    }

    @Then('I see the recipe overlap details')
    def seeOverlapDetails() {
        WebUI.waitForElementVisible(testObject('div_Overlap card'), 15)

        String percentageText = WebUI.getText(testObject('span_Overlap pct')).trim()
        if (!(percentageText ==~ OVERLAP_PATTERN)) {
            KeywordUtil.markFailedAndStop('The detail overlap badge reads ' + percentageText)
        }

        String overlapText = WebUI.getText(testObject('div_Overlap text')).trim()
        if (!overlapText.contains('Correspondência da despensa')) {
            KeywordUtil.markFailedAndStop('The detail overlap card reads ' + overlapText)
        }

        def matcher = (overlapText =~ /(\d+) de (\d+) ingredientes em stock/)
        if (!matcher.find()) {
            KeywordUtil.markFailedAndStop('The detail overlap card does not report the stock counts: ' + overlapText)
        }

        int inStock = Integer.parseInt(matcher.group(1))
        int total = Integer.parseInt(matcher.group(2))
        int missing = missingPillCount()

        if (inStock < 1 || total < 2) {
            KeywordUtil.markFailedAndStop("The recipe reports ${inStock} of ${total} ingredients in stock; the seeded pantry should match several ingredients")
        }
        if (inStock + missing != total) {
            KeywordUtil.markFailedAndStop("The recipe reports ${inStock} in stock and ${missing} missing out of ${total}")
        }

        WebUI.verifyElementPresent(testObject('span_Expiring pill'), 10)
        String expiringPill = WebUI.getText(testObject('span_Expiring pill')).trim()
        if (!expiringPill.contains('a expirar')) {
            KeywordUtil.markFailedAndStop('The expiring pill reads ' + expiringPill)
        }

        List<WebElement> rows = ingredientRows()
        if (rows.size() != total) {
            KeywordUtil.markFailedAndStop("The details list ${rows.size()} ingredients, expected ${total}")
        }

        int expiring = 0
        int stocked = 0
        int missingRows = 0

        for (WebElement row : rows) {
            List<WebElement> badges = row.findElements(By.cssSelector('.ing-badge'))
            if (badges.size() != 1) {
                KeywordUtil.markFailedAndStop('An ingredient row does not have exactly one status badge: ' + row.getText())
            }

            String label = badges.get(0).getText().trim()
            switch (label) {
                case 'A expirar':
                    expiring++
                    break
                case 'Em stock':
                    stocked++
                    break
                case 'Em falta':
                    missingRows++
                    break
                default:
                    KeywordUtil.markFailedAndStop('An ingredient row has the unknown status ' + label)
            }
        }

        if (expiring < 1) {
            KeywordUtil.markFailedAndStop('No ingredient is marked as expiring although Carrot expires in 1 day')
        }
        if (stocked < 1) {
            KeywordUtil.markFailedAndStop('No ingredient is marked as "Em stock"; the seeded pantry should match several ingredients')
        }
        if (missingRows != missing) {
            KeywordUtil.markFailedAndStop("The details show ${missingRows} missing ingredients, the summary reports ${missing}")
        }
        if (stocked + expiring != inStock) {
            KeywordUtil.markFailedAndStop("The details show ${stocked + expiring} in-stock ingredients, the summary reports ${inStock}")
        }
    }

    private static TestObject testObject(String name) {
        return findTestObject('Page_SmartPantry/' + name)
    }

    private static List<WebElement> recipeCards() {
        try {
            List<WebElement> cards = WebUI.findWebElements(testObject('div_Recipe cards'), 15)

            return cards == null ? [] : cards
        } catch (Exception ignored) {
            return []
        }
    }

    private static List<WebElement> ingredientRows() {
        try {
            List<WebElement> rows = WebUI.findWebElements(testObject('li_Ingredient row'), 10)

            return rows == null ? [] : rows
        } catch (Exception ignored) {
            return []
        }
    }

    private static int percentage(String text) {
        def matcher = (text =~ OVERLAP_PATTERN)
        if (!matcher.find()) {
            KeywordUtil.markFailedAndStop('The overlap badge reads ' + text + ', expected a percentage')
        }

        return Integer.parseInt(matcher.group(1))
    }

    private static int missingCount(String stats) {
        def matcher = (stats =~ /(\d+)\s+em falta/)

        return matcher.find() ? Integer.parseInt(matcher.group(1)) : 0
    }

    private static int missingPillCount() {
        if (!WebUI.verifyElementPresent(testObject('span_Missing pill'), 2, com.kms.katalon.core.model.FailureHandling.OPTIONAL)) {
            return 0
        }

        String text = WebUI.getText(testObject('span_Missing pill')).trim()
        def matcher = (text =~ /(\d+)/)
        if (!matcher.find()) {
            KeywordUtil.markFailedAndStop('The missing ingredients pill reads ' + text)
        }

        return Integer.parseInt(matcher.group(1))
    }
}
