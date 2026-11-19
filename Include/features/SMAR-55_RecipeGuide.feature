@SMAR-55

Feature: SMAR-55 Follow the recipe steps in cooking mode
  As a user
    I want to follow a recipe step by step while I cook
    So I can keep my place and see the ingredients I need at each step

  Scenario: Open the cooking guide from a recipe
    Given I start the application in the "http://localhost:5173/" url
    When I tap the Procurar receitas button
    And I open the first recipe in the results
    And I tap the Começar a cozinhar button
    Then I see the cooking guide on step 1
    And I close the browser

  Scenario: Move forward and backward through the recipe steps
    Given I start the application in the "http://localhost:5173/" url
    When I tap the Procurar receitas button
    And I search for "pasta" recipes
    And I open the first recipe in the results
    And I tap the Começar a cozinhar button
    Then I see the cooking guide on step 1
    When I tap the Seguinte button in the cooking guide
    Then I see the cooking guide on step 2
    When I tap the Anterior button in the cooking guide
    Then I see the cooking guide on step 1
    And I close the browser

  Scenario: Finish the cooking guide on the last step
    Given I start the application in the "http://localhost:5173/" url
    When I tap the Procurar receitas button
    And I open the first recipe in the results
    And I tap the Começar a cozinhar button
    And I go to the last step of the cooking guide
    And I tap the Concluir refeição button in the cooking guide
    Then I see the meal summary
    And I close the meal summary
    And I see the recipe details
    And I close the browser

  Scenario: Leave the cooking guide and return to the recipe
    Given I start the application in the "http://localhost:5173/" url
    When I tap the Procurar receitas button
    And I open the first recipe in the results
    And I tap the Começar a cozinhar button
    Then I see the cooking guide on step 1
    When I tap the Sair do modo cozinha button
    Then I see the recipe details
    And I close the browser
