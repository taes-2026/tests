@SMAR-1
Feature: Profile setup on startup
  As a user I want to configure my profile when I first open the app
  So that the app knows my preferences and dietary restrictions

  Scenario: Setup the profile on startup
    Given I start the application at "http://localhost:5173/"
    When the onboarding is shown
    And I fill in my household preferences and dietary restrictions
    And I confirm the profile preferences
    And I see the dashboard with my profile active
    Then I close the browser
