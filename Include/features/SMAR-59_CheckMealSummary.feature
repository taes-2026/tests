@SMAR-59

Feature: SMAR-59 Check the meal summary
  As a user
    I want to see the meal summary of a recipe
    So I can review the servings, ingredients and nutrition of a recipe

  Background:
    Given I start the application in the "http://localhost:5173/" url

  Scenario: Check the meal summary from the recipe details
    When I tap the Procurar receitas button
    And I search for "Red Kidney Bean Jambalaya" recipes
    And I open the "Red Kidney Bean Jambalaya" recipe in the results
    And I tap the Ver resumo da refeição button
    Then I see the meal summary for "Red Kidney Bean Jambalaya"
    When I close the meal summary dialog
    Then I do not see the meal summary dialog
    And I close the browser
