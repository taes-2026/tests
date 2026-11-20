@SMAR-7

Feature: SMAR-7 Switch between profiles
  As a SmartPantry user
  I want to switch the active profile from the dashboard
  So that the app uses the right diet and restrictions for each person

  Background:
    Given I start the application with no profiles in the "http://localhost:5173/" url

  Scenario: Switch the active profile and keep it after a reload
    When I create the profile "Ana" with the "omnivore" diet
    And I open the new profile form
    And I create the profile "Rui" with the "vegetarian" diet
    Then the dashboard shows the "Rui" profile with the "Vegetariano" diet
    And the profile selector offers exactly 2 profiles
    When I select the "Ana" profile
    Then the dashboard shows the "Ana" profile with the "Omnívoro" diet
    And the "Ana" profile is selected
    When I reload the dashboard
    Then the dashboard shows the "Ana" profile with the "Omnívoro" diet
    And I close the browser

  Scenario: Create a new profile from the dashboard selector
    When I create the profile "Ana" with the "omnivore" diet
    And I open the new profile form
    Then the new profile form is empty
    When I create the profile "Rui" with the "vegan" diet
    Then the dashboard shows the "Rui" profile with the "Vegano" diet
    And the profile selector offers exactly 2 profiles
    And I close the browser

  Scenario: Edit the active profile from the dashboard
    When I create the profile "Ana" with the "omnivore" diet
    And I open the edit profile form
    Then the edit profile form is filled with "Ana" and the "omnivore" diet
    When I change the profile name to "Ana Silva" and save
    Then the dashboard shows the "Ana Silva" profile with the "Omnívoro" diet
    And the profile selector offers exactly 1 profile
    And the profile selector lists "Ana Silva"
    And I close the browser
