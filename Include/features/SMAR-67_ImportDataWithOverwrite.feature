@SMAR-67

Feature: SMAR-67 Import app data with overwrite
  As a SmartPantry user
  I want to import a backup file
  So that I can replace all current data

  Background:
    Given I start the application with a clean pantry in the "http://localhost:5173/" url

  Scenario: Import data replacing the existing data
    When I import the data file "smartpantry-import-overwrite.json"
    And I confirm the overwrite
    Then I should see the imported data
    And I close the browser
