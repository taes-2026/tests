@SMAR-63

Feature: SMAR-63 Save a recipe after finishing a meal
  As a user
    I want to save the recipe I have just cooked from the meal completion screen
    So I can find it again later in my saved recipes

  Background:
    Given I start the application in the "http://localhost:5173/" url
    And I clear the saved recipes

  Scenario: Save the recipe from the meal completion screen
    When I tap the Procurar receitas button
    And I search for "Roasted Broccoli" recipes
    And I open the "Roasted Broccoli with Lemon and Garlic" recipe in the results
    And I tap the Começar a cozinhar button
    And I go to the last step of the cooking guide
    And I tap the Concluir refeição button in the cooking guide
    Then I see the meal summary
    When I tap the Guardar receita button
    Then I see the recipe saved confirmation
    When I tap the Concluir button on the completion screen
    And I close the recipe details
    And I go back to the dashboard
    And I tap the Receitas guardadas button
    Then I see the "Roasted Broccoli with Lemon and Garlic" recipe in saved recipes
    And I close the browser

  Scenario: Skip saving and keep the saved recipes unchanged
    When I tap the Procurar receitas button
    And I search for "Roasted Broccoli" recipes
    And I open the "Roasted Broccoli with Lemon and Garlic" recipe in the results
    And I tap the Começar a cozinhar button
    And I go to the last step of the cooking guide
    And I tap the Concluir refeição button in the cooking guide
    Then I see the meal summary
    When I close the meal summary
    And I close the recipe details
    And I go back to the dashboard
    And I tap the Receitas guardadas button
    Then I do not see the "Roasted Broccoli with Lemon and Garlic" recipe in saved recipes
    And I close the browser
