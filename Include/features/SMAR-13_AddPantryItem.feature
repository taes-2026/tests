@tag
Feature: SMAR-13 Add Pantry item
  As a user
  I want to register and manage the food I have at home
  So that I always know what I have avaiable

  @tag1
   Scenario: Add a pantry item manually
    Given I start the application
    When I tap the "Add Item" button
    And I tap "enter manually" button
    And I fill the item details
    And I save the item
    And I see the item in my pantry
    Then I close the browser