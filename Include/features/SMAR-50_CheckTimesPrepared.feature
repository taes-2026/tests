@SMAR-50

Feature: SMAR-50 Check how many times a recipe was prepared
  As a SmartPantry user
  I want to see how many times I have prepared a recipe
  So that I can repeat the meals I cook most

  Background:
    Given I start the application with a clean pantry in the "http://localhost:5173/" url

  Scenario: A recipe that was never prepared shows an empty preparation count
    When I tap the Procurar receitas button
    And I search the recipes for "Roasted Broccoli"
    And I open the "Roasted Broccoli with Lemon and Garlic" recipe in the results
    Then the recipe has not been prepared yet
    And I close the browser

  Scenario: Completing the cooking mode records the first preparation
    When I tap the Procurar receitas button
    And I search the recipes for "Roasted Broccoli"
    And I open the "Roasted Broccoli with Lemon and Garlic" recipe in the results
    And I remember the recipe title
    And I tap the Começar a cozinhar button
    And I go to the last step of the cooking guide
    And I tap the Concluir refeição button in the cooking guide
    Then I see the first preparation confirmation
    When I close the cooking guide
    Then the recipe details show "Preparada 1 vez"
    And the recipe last preparation date is shown
    When I close the recipe detail
    Then the recipe card shows "1x preparada"
    And I close the browser

  Scenario: Preparation counts increase and persist
    When I tap the Procurar receitas button
    And I search the recipes for "Roasted Broccoli"
    And I open the "Roasted Broccoli with Lemon and Garlic" recipe in the results
    And I remember the recipe title
    And I tap the Começar a cozinhar button
    And I go to the last step of the cooking guide
    And I tap the Concluir refeição button in the cooking guide
    Then I see the first preparation confirmation
    When I close the cooking guide
    Then the recipe details show "Preparada 1 vez"
    When I tap the Começar a cozinhar button
    And I go to the last step of the cooking guide
    And I tap the Concluir refeição button in the cooking guide
    Then I see the second preparation confirmation
    When I close the cooking guide
    Then the recipe details show "Preparada 2 vezes"
    When I close the recipe detail
    And I reload the recipes page
    And I search for the remembered recipe
    Then the recipe card shows "2x preparada"
    When I open the remembered recipe in the results
    And I save the recipe from the detail
    And I close the recipe detail
    And I open the saved recipes page
    Then the saved recipe shows "Preparada 2 vezes"
    And I close the browser
