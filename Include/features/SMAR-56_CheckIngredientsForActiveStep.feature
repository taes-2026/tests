@SMAR-56

Feature: SMAR-56 Check ingredients for active step
	As a user
		I want to see only the ingredients and measurements needed for the current step
			so that I can cook without searching the whole ingredient list
			
Scenario: Active step highlights its ingredients
    Given I start the application in the "http://localhost:5173/" url
    And I tap the adicionar produto button
    And I tap introduzir manualmente button
    And I fill the item details with "Carrot" expiring in 1 days
    And I save the item
    And I tap the Procurar receitas button
    And I open the first recipe in the recipe results
    And I tap the Começar a cozinhar button
    Then I see the cooking guide on step 1
    And I see at least one highlighted ingredient in the active step
    And I close the browser

	Scenario: Ingredients change when moving to the next step
    Given I start the application in the "http://localhost:5173/" url
    And I tap the adicionar produto button
    And I tap introduzir manualmente button
    And I fill the item details with "Carrot" expiring in 1 days
    And I save the item
    And I tap the Procurar receitas button
    And I open the first recipe in the recipe results
    And I tap the Começar a cozinhar button
    When I tap the Seguinte button in the cooking guide
    Then I see the cooking guide on step 2
    And I see the ingredients of the active step
    And I close the browser
