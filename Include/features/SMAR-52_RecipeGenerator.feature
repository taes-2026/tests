@SMAR-52

Feature: SMAR-52 Generate a recipe with the Sous-Chef
  As a home cook
  I want the Sous-Chef to build a recipe from the ingredients I choose
  So that I can cook a meal with the food I already have in the pantry

  The suite expects the recipe-generator service to run with --test (the
  deterministic testing provider), so the generated recipe is the same on
  every run and no Gemini call or API key is involved.

  The recipes page may show "Erro ao obter receitas (402)" when the live
  Spoonacular quota is exhausted; the Sous-Chef flow does not use those
  suggestions, so these scenarios still run.


  # ---------- Deve passar ----------

  Scenario: The Sous-Chef lists the pantry ingredients
    Given I start the application with a clean pantry in the "http://localhost:5173/" url
    And the pantry has the product "carrot" with quantity "2" and unit "Unidade(s)"
    And the pantry has the product "rice" with quantity "500" and unit "g"
    When I tap the Procurar receitas button
    And I open the Sous-Chef
    Then the Sous-Chef lists "carrot" and "rice"
    And the Sous-Chef counter shows "0/2"
    And the Sous-Chef generate button is disabled
    And I close the browser

  Scenario: Selecting every ingredient enables the generation
    Given I start the application with a clean pantry in the "http://localhost:5173/" url
    And the pantry has the product "carrot" with quantity "2" and unit "Unidade(s)"
    And the pantry has the product "rice" with quantity "500" and unit "g"
    When I tap the Procurar receitas button
    And I open the Sous-Chef
    And I click Selecionar tudo in the Sous-Chef
    Then the Sous-Chef counter shows "2/2"
    And the Sous-Chef generate button is enabled
    And I close the browser

  Scenario: Generating a recipe for the chosen doses
    Given the Sous-Chef service is running in test mode
    And I start the application with a clean pantry in the "http://localhost:5173/" url
    And the pantry has the product "carrot" with quantity "2" and unit "Unidade(s)"
    And the pantry has the product "rice" with quantity "500" and unit "g"
    When I tap the Procurar receitas button
    And I open the Sous-Chef
    And I select the Sous-Chef ingredient "carrot"
    And I set the Sous-Chef doses to "4"
    And I ask the Sous-Chef to generate a recipe
    Then the Sous-Chef generated a recipe for "4" doses
    And the generated recipe is the testing recipe "Salteado de carrot"
    And I close the browser

  Scenario: The empty pantry is reported
    Given I start the application with a clean pantry in the "http://localhost:5173/" url
    When I tap the Procurar receitas button
    And I open the Sous-Chef
    Then the Sous-Chef says the pantry is empty
    And the Sous-Chef has no ingredient list
    And I close the browser

  Scenario: Cancelling the Sous-Chef leaves the recipes page open
    Given I start the application with a clean pantry in the "http://localhost:5173/" url
    And the pantry has the product "carrot" with quantity "2" and unit "Unidade(s)"
    When I tap the Procurar receitas button
    And I open the Sous-Chef
    And I cancel the Sous-Chef
    Then the Sous-Chef is closed
    And I close the browser