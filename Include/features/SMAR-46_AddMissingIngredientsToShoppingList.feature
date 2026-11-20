@SMAR-46

Feature: SMAR-46 Add missing ingredients to the shopping list
  As a SmartPantry user
  I want to add the ingredients a recipe is missing to my shopping list
  So that I can buy everything I need to cook it

  Background:
    Given I start the application with a clean pantry in the "http://localhost:5173/" url

  Scenario: Add the missing ingredients of a recipe to the shopping list
    When I tap the Procurar receitas button
    And I search the recipes for "soup"
    And I open the first recipe in the results
    Then I see the add missing ingredients button
    When I add the missing ingredients to the shopping list
    Then I see the shopping list confirmation
    When I open the shopping list from the confirmation
    Then the shopping list contains the added ingredients
    And the shopping list items are linked to the recipe
    And I close the browser

  Scenario: Adding the same recipe twice does not duplicate the ingredients
    When I tap the Procurar receitas button
    And I search the recipes for "soup"
    And I open the first recipe in the results
    Then I see the add missing ingredients button
    When I add the missing ingredients to the shopping list
    Then I see the shopping list confirmation
    When I add the missing ingredients to the shopping list
    Then I see the duplicate shopping list message
    When I open the shopping list from the confirmation
    Then the shopping list does not contain duplicate ingredients
    And I close the browser

  Scenario: Manage the shopping list items
    When I tap the Procurar receitas button
    And I search the recipes for "soup"
    And I open the first recipe in the results
    Then I see the add missing ingredients button
    When I add the missing ingredients to the shopping list
    Then I see the shopping list confirmation
    When I open the shopping list from the confirmation
    And I check the first shopping list item
    Then I can move the first item to the pantry
    When I clear the purchased items
    Then the checked items are removed
    When I clear the shopping list
    Then the shopping list is empty
    And I close the browser
