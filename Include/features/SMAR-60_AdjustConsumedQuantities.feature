@SMAR-60

Feature: SMAR-60 Adjust the quantities consumed in a meal
  As a SmartPantry user
  I want to adjust how much of each ingredient I actually used
  So that the pantry is only discounted by what I really consumed

  Background:
    Given I start the application in the "http://localhost:5173/" url
    And the pantry has the product "carrot" with quantity "100" and unit "Unidade(s)"
    When I tap the Procurar receitas button
    And I open the first recipe in the results
    And I tap the Começar a cozinhar button
    And I go to the last step of the cooking guide
    And I tap the Concluir refeição button in the cooking guide

  Scenario: Adjust a quantity and confirm the meal
    Then I see the consumed quantities dialog
    And the consumed quantities dialog shows "carrot" with the stock quantity "100"
    When I set the consumed quantity of "carrot" to "2"
    Then the consumed quantity of "carrot" is "2"
    When I confirm the consumed quantities
    Then I see the meal summary
    When I go to the dashboard
    Then the pantry shows "carrot" with "98 Unidade(s)"
    And I close the browser

  Scenario: Cancel the adjustment and keep the pantry untouched
    Then I see the consumed quantities dialog
    When I set the consumed quantity of "carrot" to "1"
    And I cancel the consumed quantities
    Then I am still in the cooking guide
    When I go to the dashboard
    Then the pantry shows "carrot" with "100 Unidade(s)"
    And I close the browser

  Scenario: Step the quantity and restore the recipe quantities
    Then I see the consumed quantities dialog
    When I increase the consumed quantity of "carrot"
    Then the consumed quantities dialog marks "carrot" as adjusted
    When I restore the recipe quantities
    Then the consumed quantities dialog does not mark "carrot" as adjusted
    And I close the browser

  Scenario: Ingredients that are not in the pantry are not discounted
    Then I see the consumed quantities dialog
    And the consumed quantities dialog marks the ingredients missing from the pantry
    And I close the browser
