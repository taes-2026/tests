@SMAR-58

Feature: SMAR-58 Alert when a cooking timer finishes
  As a user
    I want a clear alert when a cooking timer ends
    So I notice it while I am busy cooking

  Scenario: A finished timer opens the alert dialog
    Given I start the application in the "http://localhost:5173/" url
    When I tap the Procurar receitas button
    And I search for "soup" recipes
    And I open the first recipe in the results
    And I tap the Começar a cozinhar button
    And I open the first step with a step timer
    And I start the timer for the current step
    When the running timer reaches zero
    Then I see the timer alert dialog
    And the timer alert describes one finished timer
    And I close the browser

  Scenario: Dismiss the alert and remove the finished timer
    Given I start the application in the "http://localhost:5173/" url
    When I tap the Procurar receitas button
    And I search for "soup" recipes
    And I open the first recipe in the results
    And I tap the Começar a cozinhar button
    And I open the first step with a step timer
    And I start the timer for the current step
    When the running timer reaches zero
    And I dismiss the timer alert
    Then the timer alert is gone and the finished timer is removed
    And I close the browser

  Scenario: Restart a finished timer from the alert
    Given I start the application in the "http://localhost:5173/" url
    When I tap the Procurar receitas button
    And I search for "soup" recipes
    And I open the first recipe in the results
    And I tap the Começar a cozinhar button
    And I open the first step with a step timer
    And I start the timer for the current step
    When the running timer reaches zero
    And I restart the finished timer from the alert
    Then the timer runs again from its full duration
    And I close the browser

  Scenario: The alert lists every finished timer
    Given I start the application in the "http://localhost:5173/" url
    When I tap the Procurar receitas button
    And I search for "soup" recipes
    And I open the first recipe in the results
    And I tap the Começar a cozinhar button
    And I open the first step with a step timer
    And I start the timer for the current step
    And I open the next step with a step timer
    And I start the timer for the current step
    When the running timers reach zero
    Then the timer alert lists both finished timers
    When I restart the first finished timer from the alert
    Then the timer alert still shows the other finished timer
    When I dismiss the timer alert
    Then only the restarted timer is still running
    And I close the browser

  Scenario: No alert while a timer is still running
    Given I start the application in the "http://localhost:5173/" url
    When I tap the Procurar receitas button
    And I search for "soup" recipes
    And I open the first recipe in the results
    And I tap the Começar a cozinhar button
    And I open the first step with a step timer
    And I start the timer for the current step
    Then I see no timer alert while the timer is running
    And I close the browser
