@SMAR-51

Feature: SMAR-51 Access saved recipes offline
  As a user
    I want to access the recipes I saved without a connection
    So I can still cook after the application has loaded

  Background:
    Given I start the application in the "http://localhost:5173/" url
    And I clear the saved recipes
    And I prepare the offline cache

  Scenario: See saved recipes offline
    When I tap the Procurar receitas button
    And I search for "soup" recipes
    And I save the first recipe from the results
    And I go offline
    And I open the saved recipes page
    Then I see 1 saved recipe
    And I see the saved recipe in the saved recipes
    And I restore the network
    And I close the browser

  Scenario: Open a saved recipe offline
    When I tap the Procurar receitas button
    And I search for "soup" recipes
    And I save the first recipe from the results
    And I open the saved recipes page
    And I open the first saved recipe
    Then I see the recipe details
    When I close the recipe detail
    And I go offline
    And I open the first saved recipe
    Then I see the recipe details
    And I restore the network
    And I close the browser
