@SMAR-54

Feature: SMAR-54 Show the generated recipe in the preparation menu
  As a home cook
  I want to review the recipe the Sous-Chef generated and start cooking it
  So that I can turn the suggestion into a meal without leaving the app

  The suite expects the recipe-generator service to run with --test (the
  deterministic testing provider), so the generated recipe and its details are
  the same on every run and no Gemini call or API key is involved.


  Background:
    Given the Sous-Chef service is running in test mode
    And I start the application with a clean pantry in the "http://localhost:5173/" url
    And the pantry has the product "carrot" with quantity "2" and unit "Unidade(s)"
    When I tap the Procurar receitas button
    And I open the Sous-Chef
    And I select the Sous-Chef ingredient "carrot"
    And I set the Sous-Chef doses to "4"
    And I ask the Sous-Chef to generate a recipe
    Then the Sous-Chef generated a recipe for "4" doses


  Scenario: The generated recipe details are shown
    Then the generated recipe is the testing recipe "Salteado de carrot"
    And the generated recipe summary is shown
    And the generated recipe stats show "4" doses, "10 min" preparation, "15 min" cooking and "25 min" total
    And the generated recipe ingredient "carrot" shows "2 un" and "em despensa"
    And the generated recipe ingredient "Azeite" shows "20 ml" and "em falta"
    And the generated recipe ingredient "Azeite" is marked as optional
    And the generated recipe lists "3" preparation steps
    When I close the generated recipe
    Then the generated recipe is closed
    And I close the browser

  Scenario: The generated recipe opens in the cooking guide
    When I tap the Modo de preparação button in the generated recipe
    Then the generated cooking guide shows step "1" of "3"
    And the generated cooking guide step mentions "Prepare os ingredientes: carrot"
    When I tap the Seguinte button in the cooking guide
    Then the generated cooking guide shows step "2" of "3"
    And the generated cooking guide step mentions "Aqueça o azeite"
    And the generated cooking guide offers the timer "6 min"
    When I leave the generated cooking guide
    Then the generated recipe is still open
    And I close the generated recipe
    And I close the browser

  Scenario: Closing the generated recipe returns to the recipe search
    When I close the generated recipe
    Then the generated recipe is closed
    And the Sous-Chef entry button is available
    And I close the browser
