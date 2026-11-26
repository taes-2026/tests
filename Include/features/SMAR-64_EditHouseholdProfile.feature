@SMAR-64

Feature: SMAR-64 Edit a household profile
  As a SmartPantry user
  I want to edit an existing household profile from the settings page
  So that I can keep the name, the diet and the dietary restrictions up to date

  Background:
    Given I start the application with no profiles in the "http://localhost:5173/" url
    When I create the profile "Ana" with the "omnivore" diet

  Scenario: Edit the name and the diet of a profile from the settings page
    When I open the settings page
    Then the settings list shows the profile "Ana" with the "Omnívoro" diet
    And the settings list marks "Ana" as the active profile
    And the settings list shows exactly 1 profile
    When I edit the "Ana" profile from the settings
    Then the edit profile form is filled with "Ana" and the "omnivore" diet
    When I change the profile name to "Ana Silva" and the diet to "vegan" and save the changes
    Then I am back on the settings page
    And I see the profile updated confirmation
    And the settings list shows the profile "Ana Silva" with the "Vegano" diet
    And the settings list marks "Ana Silva" as the active profile
    And the settings list shows exactly 1 profile
    And I close the browser

  Scenario: Keep the edited profile when returning to the dashboard
    When I open the settings page
    And I edit the "Ana" profile from the settings
    When I change the profile name to "Ana Silva" and the diet to "vegetarian" and save the changes
    Then I am back on the settings page
    When I go back to the dashboard from the settings
    Then the dashboard shows the "Ana Silva" profile with the "Vegetariano" diet
    When I reload the dashboard
    Then the dashboard shows the "Ana Silva" profile with the "Vegetariano" diet
    And I close the browser

  Scenario: Save the intolerances and the restrictions of a profile
    When I open the settings page
    And I edit the "Ana" profile from the settings
    When I check the "Lactose" intolerance and the "Carne de Porco" restriction
    And I change the profile name to "Ana Silva" and the diet to "omnivore" and save the changes
    Then I am back on the settings page
    And I see the profile updated confirmation
    When I edit the "Ana Silva" profile from the settings
    Then the profile form has the "Lactose" intolerance and the "Carne de Porco" restriction checked
    And I close the browser

  Scenario: Cancel an edit and keep the stored profile
    When I open the settings page
    And I edit the "Ana" profile from the settings
    When I change the profile name to "Ana Silva" without saving
    And I cancel the edit and go back to the settings
    Then the settings list shows the profile "Ana" with the "Omnívoro" diet
    And the settings list does not show the profile "Ana Silva"
    When I edit the "Ana" profile from the settings
    Then the edit profile form is filled with "Ana" and the "omnivore" diet
    And I close the browser

  Scenario: Show the not found page when the edited profile no longer exists
    When I open the edit form for a profile that no longer exists
    Then I see the profile not found page
    And I close the browser
