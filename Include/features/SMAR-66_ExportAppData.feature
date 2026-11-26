@SMAR-66

Feature: SMAR-66 Export app data
  As a SmartPantry user
  I want to export my app data
  So that I can back up or transfer my information

  Background:
    Given I start the application in the "http://localhost:5173/" url

  Scenario: Export app data from the dashboard
    When I tap the Exportar dados button
    Then I should see the export confirmation message
    And I close the browser
