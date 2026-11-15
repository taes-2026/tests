@SMAR-31

Feature: SMAR-31 Find recipes with items in my pantry
  As a user 
  	I want to find recipes with what I have in the kitchen.
		So I can know what to cook
		

	Scenario: Find recipes with items in pantry
		Given I start the application in the "http://localhost:5173/" url
		And I tap the adicionar produto button
		And I tap introduzir manualmente button
		And I fill the item details with "Milk"
		And I save the item
		When I tap the Procurar receitas button
		And I tap selecionar items button
		And I select "Milk" in the menu
		And I tap usar ingredientes button
		Then I should see at least one recipe card
		And I close the browser