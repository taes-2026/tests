@SMAR-61

Feature: SMAR-61 Update used pantrys after a meal
  As a user
    I want the pantry stock to be automatically updated after every meal
    So that my inventory always reflects what I actually have at home


  # ---------- Deve passar ----------

  Scenario: Partial consumption decreases the quantity
    Given I start the application in the "http://localhost:5173/" url
    And the pantry has the product "carrot" with quantity "100" and unit "Unidade(s)"
    When I tap the Procurar receitas button
    And I open the first recipe in the results
    And I tap the Começar a cozinhar button
    And I go to the last step of the cooking guide
    And I tap the Concluir refeição button in the cooking guide
    When I go to the dashboard
    Then the pantry shows "carrot" with quantity less than "100"
    And I close the browser

  Scenario: Consuming more than the stock removes the item
    Given I start the application in the "http://localhost:5173/" url
    And the pantry has the product "carrot" with quantity "0.01" and unit "Unidade(s)"
    When I tap the Procurar receitas button
    And I open the first recipe in the results
    And I tap the Começar a cozinhar button
    And I go to the last step of the cooking guide
    And I tap the Concluir refeição button in the cooking guide
    When I go to the dashboard
    Then the pantry does not show "carrot"
    And I close the browser

  Scenario: Items not used in the recipe are not changed
    Given I start the application in the "http://localhost:5173/" url
    And the pantry has the product "carrot" with quantity "100" and unit "Unidade(s)"
    And the pantry has the product "Pork" with quantity "500" and unit "g"
    When I tap the Procurar receitas button
    And I open the first recipe in the results
    And I tap the Começar a cozinhar button
    And I go to the last step of the cooking guide
    And I tap the Concluir refeição button in the cooking guide
    And I go to the dashboard
    Then the pantry shows "Pork" with "500 g"
    And I close the browser

  Scenario: Pantry changes are kept after reloading the app
    Given I start the application in the "http://localhost:5173/" url
    And the pantry has the product "carrot" with quantity "0.01" and unit "Unidade(s)"
    When I tap the Procurar receitas button
    And I open the first recipe in the results
    And I tap the Começar a cozinhar button
    And I go to the last step of the cooking guide
    And I tap the Concluir refeição button in the cooking guide
    And I go to the dashboard
    And I reload the page
    Then the pantry does not show "garlic"
    And I close the browser

  # ---------- NÃO deve alterar ----------

  Scenario: Different unit leaves the item unchanged and shows a warning
    Given I start the application in the "http://localhost:5173/" url
    And the pantry has the product "carrot" with quantity "5" and unit "Embalagem"
    When I tap the Procurar receitas button
    And I open the first recipe in the results
    And I tap the Começar a cozinhar button
    And I go to the last step of the cooking guide
    And I tap the Concluir refeição button in the cooking guide
    Then I see the pantry warning
    When I go to the dashboard
    Then the pantry shows "carrot" with "5 Embalagem"
    And I close the browser

  Scenario: Empty pantry shows the warning and nothing breaks
    Given I start the application in the "http://localhost:5173/" url
    When I tap the Procurar receitas button
    And I open the first recipe in the results
    And I tap the Começar a cozinhar button
    And I go to the last step of the cooking guide
    And I tap the Concluir refeição button in the cooking guide
    Then I see the pantry warning
    And I see the meal summary
    And I close the browser

  Scenario: Leaving the cooking guide does not change the pantry
    Given I start the application in the "http://localhost:5173/" url
    And the pantry has the product "carrot" with quantity "5" and unit "Unidade(s)"
    When I tap the Procurar receitas button
    And I open the first recipe in the results
    And I tap the Começar a cozinhar button
    And I tap the Sair do modo cozinha button
    And I go to the dashboard
    Then the pantry shows "carrot" with "5 Unidade(s)"
    And I close the browser

  Scenario: Reaching the last step without finishing does not change the pantry
    Given I start the application in the "http://localhost:5173/" url
    And the pantry has the product "carrot" with quantity "5" and unit "Unidade(s)"
    When I tap the Procurar receitas button
    And I open the first recipe in the results
    And I tap the Começar a cozinhar button
    And I go to the last step of the cooking guide
    And I go to the dashboard
    Then the pantry shows "carrot" with "5 Unidade(s)"
    And I close the browser