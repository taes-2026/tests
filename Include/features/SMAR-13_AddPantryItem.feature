@SMAR-13

Feature: SMAR-13 Add Pantry item
  As a user
  I want to register and manage the food I have at home
  So that I always know what I have avaiable

   Scenario: Add a pantry item manually
    Given I start the application in the "http://localhost:5173/" url
    When I tap the adicionar produto button
    And I tap introduzir manualmente button
    And I fill the item details with "Milk"
    And I save the item
    And I see the item in my pantry
    Then I close the browser