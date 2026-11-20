@SMAR-43

Feature: SMAR-43 Check ingredient overlap
  As a SmartPantry user planning meals
  I want the recipe suggestions to calculate how much of each recipe I already have
  So that I can cook with ingredients I have and use up expiring food

  Background:
    Given I start the application with a clean pantry in the "http://localhost:5173/" url
    And I add the pantry item "Carrot" expiring in 1 days
    And I add the pantry item "Onion" expiring in 10 days
    And I add the pantry item "Garlic" expiring in 10 days
    And I add the pantry item "Salt" expiring in 10 days
    And I add the pantry item "Olive Oil" expiring in 10 days
    And I add the pantry item "Vegetable Broth" expiring in 10 days

  Scenario: Recipe cards show overlap percentages and stock counts
    When I tap the Procurar receitas button
    And I search the recipes for "carrot soup"
    Then the recipe cards show overlap percentages and stock counts
    When I sort the recipes by "Maior correspondência (%)"
    Then the recipes are ordered by descending overlap
    When I sort the recipes by "Menos ingredientes em falta"
    Then the recipes are ordered by ascending missing ingredients
    And I close the browser

  Scenario: Expiring ingredients are prioritized and marked in the recipe details
    When I tap the Procurar receitas button
    And I search the recipes for "carrot soup"
    Then the first recipe card uses expiring pantry items
    When I open the first recipe in the results
    Then I see the recipe overlap details
    And I close the browser
