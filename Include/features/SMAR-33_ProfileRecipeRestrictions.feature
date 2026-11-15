@SMAR-33

Feature: SMAR-33 Search recipes restricted by my profile recipe restrictions
  As a user 
  	I want the recipes to follow the restrictions for the current active profile
		So I can’t see recipes I can’t eat
		
		
	Scenario: Recipes match the profile diet and intolerance
	Given I start the application in the "http://localhost:5173/" url with vegetarian diet and dairy intolerance
	When I tap the Procurar receitas button
	And I open the first recipe card
	Then I see the vegetarian and dairy free tags
	And I close the browser
	
	Scenario: Recipes exclude the profile restriction
    Given I start the application in the "http://localhost:5173/" url with omnivore diet and pork restriction
    When I tap the Procurar receitas button
    And I search for pork in the search bar
    Then I see there is no recipes
    And I close the browser