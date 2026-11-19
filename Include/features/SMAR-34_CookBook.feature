@SMAR-34

Feature: SMAR-34 Cook book with saved recipes and collections
  As a user
    I want to save recipes and group them in collections
    So I can find the recipes I want to cook again

  Scenario: The saved recipes page starts empty
    Given I start the application in the "http://localhost:5173/" url
    When I tap the Receitas guardadas button
    Then I see an empty saved recipes list
    And I close the browser

  Scenario: Save a recipe from the results list
    Given I start the application in the "http://localhost:5173/" url
    When I tap the Procurar receitas button
    And I search for "soup" recipes
    And I save the first recipe from the results
    Then the first recipe is marked as saved
    When I open the saved recipes page
    Then I see 1 saved recipe
    And I see the saved recipe in the saved recipes
    And I close the browser

  Scenario: Remove a saved recipe
    Given I start the application in the "http://localhost:5173/" url
    When I tap the Procurar receitas button
    And I search for "soup" recipes
    And I save the first recipe from the results
    And I open the saved recipes page
    When I remove the first saved recipe
    Then I see an empty saved recipes list
    And I close the browser

  Scenario: Save a recipe from its detail
    Given I start the application in the "http://localhost:5173/" url
    When I tap the Procurar receitas button
    And I search for "soup" recipes
    And I open the first recipe in the results
    When I save the recipe from the detail
    Then the recipe detail is marked as saved
    When I close the recipe detail
    And I open the saved recipes page
    Then I see 1 saved recipe
    And I see the saved recipe in the saved recipes
    And I close the browser

  Scenario: Create a collection and filter by it
    Given I start the application in the "http://localhost:5173/" url
    When I tap the Procurar receitas button
    And I search for "soup" recipes
    And I save the first recipe from the results
    And I open the saved recipes page
    When I create the collection "Semana" for the saved recipe
    Then the collection "Semana" is checked for the saved recipe
    When I close the collections modal
    Then I see the collection "Semana" on the saved recipe
    And I see the collection filter "Semana"
    When I filter the saved recipes by the collection "Semana"
    Then only the saved recipe is shown
    And I close the browser

  Scenario: Manage collections and reject duplicate names
    Given I start the application in the "http://localhost:5173/" url
    When I tap the Procurar receitas button
    And I search for "soup" recipes
    And I save the first recipe from the results
    And I open the saved recipes page
    When I create the empty collection "Fim de semana"
    Then the collection "Fim de semana" is empty
    When I try to create the collection "Fim de semana" again
    Then I see the duplicate collection error
    When I close the collections modal
    Then I see the collection filter "Fim de semana"
    When I filter the saved recipes by the collection "Fim de semana"
    Then I see the empty collection message
    And I close the browser

  Scenario: Saved recipes survive a page reload
    Given I start the application in the "http://localhost:5173/" url
    When I tap the Procurar receitas button
    And I search for "soup" recipes
    And I save the first recipe from the results
    And I open the saved recipes page
    When I reload the saved recipes page
    Then the saved recipe is still listed
    And I close the browser
