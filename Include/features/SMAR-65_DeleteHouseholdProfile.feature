
@SMAR-65

Feature: SMAR-65 Delete household profile

  As a user
  I want to delete any profile of my household from the settings
  So that I only keep the profiles that are still needed

  	Scenario: Delete a profile that is not active
	    Given I start the application in the "http://localhost:5173/" url
	    When I open the new profile form
	    And I create the profile "Rui" with the "vegetarian" diet
	    And I open the settings
	    And I delete the first profile
	    Then the settings list 1 profiles
	    And I close the browser
    
  	Scenario: Delete the active profile
	    Given I start the application in the "http://localhost:5173/" url
	    When I open the new profile form
	    And I create the profile "Rui" with the "vegetarian" diet
	    And I open the settings
	    And I delete the second profile
	    And I go back to the household dashboard
	    Then the dashboard shows the "Teste" profile with the "Omnívoro" diet
	    And the profile selector offers exactly 1 profile
	    And I close the browser


  	Scenario: The last profile cannot be deleted
	    Given I start the application in the "http://localhost:5173/" url
	    When I open the settings
	    Then the first Eliminar button is disabled
	    And I close the browser

    
    Scenario: Cancel the deletion
	    Given I start the application in the "http://localhost:5173/" url
	    When I open the new profile form
	    And I create the profile "Rui" with the "vegetarian" diet
	    And I open the settings
	    And I tap the first Eliminar button
	    And I cancel the deletion
	    Then the settings list 2 profiles
	    And I close the browser
    
    
	Scenario: Deletion is kept after reloading
	    Given I start the application in the "http://localhost:5173/" url
	    When I open the new profile form
	    And I create the profile "Rui" with the "vegetarian" diet
	    And I open the settings
	    And I delete the first profile
	    And I reload the dashboard
	    And I go back to the household dashboard
	    Then the profile selector offers exactly 1 profile
	    And I close the browser
