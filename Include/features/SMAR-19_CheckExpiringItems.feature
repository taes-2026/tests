@SMAR-19
Feature: SMAR-19 Check expiring items
  As a user 
  I want to be warned about food that is close to expiring 
  So that I can avoid food waste

   Scenario: Item expiring tomorrow appears in the expiring alerts
    Given I start the application in the "http://localhost:5173/" url
    When I tap the adicionar produto button
    And I tap introduzir manualmente button
    And I fill the item details with "Laranja" expiring in 1 days
    And I save the item
    Then I see "Laranja" in the expiring alerts
    And I close the browser