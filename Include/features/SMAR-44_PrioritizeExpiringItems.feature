@SMAR-44


Feature: SMAR-44 Prioritze expiring items in recipes
	As a user 
		I want to be able to prioritize expiring ingredients 
			so that I can use them first
			
	
	Scenario: Recipes suggested use the items closest to expiring
    Given I start the application in the "http://localhost:5173/" url
    And I tap the adicionar produto button
	And I tap introduzir manualmente button
	And I fill the item details with "Carrot" expiring in 1 days
	And I save the item
	When I tap the Procurar receitas button
	Then I should see at least one recipe card with "carrot" ingredient
	And I close the browser