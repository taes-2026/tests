@SMAR-57

Feature: SMAR-57 Step timers in cooking mode
  As a user
    I want to start a timer for the cooking times mentioned in a recipe step
    So I can follow the recipe without watching the clock

  Scenario: Steps that mention a time offer timer presets
    Given I start the application in the "http://localhost:5173/" url
    When I tap the Procurar receitas button
    And I search for "soup" recipes
    And I open the first recipe in the results
    And I tap the Começar a cozinhar button
    And I open the first step with a step timer
    Then I see the timer presets for the current step
    And I close the browser

  Scenario: Start a timer and keep it running while moving through the steps
    Given I start the application in the "http://localhost:5173/" url
    When I tap the Procurar receitas button
    And I search for "soup" recipes
    And I open the first recipe in the results
    And I tap the Começar a cozinhar button
    And I open the first step with a step timer
    And I start the first step timer
    Then I see the timer running in the timer panel
    When I tap the Seguinte button in the cooking guide
    Then the timer keeps running for the step it was started on
    And I close the browser

  Scenario: Pause and resume a running timer
    Given I start the application in the "http://localhost:5173/" url
    When I tap the Procurar receitas button
    And I search for "soup" recipes
    And I open the first recipe in the results
    And I tap the Começar a cozinhar button
    And I open the first step with a step timer
    And I start the first step timer
    When I tap the timer pause button
    Then the timer is paused
    When I tap the timer resume button
    Then the timer counts down again
    And I close the browser

  Scenario: Reset a running timer
    Given I start the application in the "http://localhost:5173/" url
    When I tap the Procurar receitas button
    And I search for "soup" recipes
    And I open the first recipe in the results
    And I tap the Começar a cozinhar button
    And I open the first step with a step timer
    And I start the first step timer
    When I tap the timer reset button
    Then the timer is reset to its original duration
    And I close the browser

  Scenario: Close a running timer
    Given I start the application in the "http://localhost:5173/" url
    When I tap the Procurar receitas button
    And I search for "soup" recipes
    And I open the first recipe in the results
    And I tap the Começar a cozinhar button
    And I open the first step with a step timer
    And I start the first step timer
    When I tap the timer close button
    Then I see no timers in the timer panel
    And I close the browser

  Scenario: A finished timer alerts the user
    Given I start the application in the "http://localhost:5173/" url
    When I tap the Procurar receitas button
    And I search for "soup" recipes
    And I open the first recipe in the results
    And I tap the Começar a cozinhar button
    And I open the first step with a step timer
    And I start the first step timer
    When the running timer reaches zero
    Then I see the timer finished alert
    When I dismiss the timer finished alert
    Then I see the timer finished alert is gone
    And I close the browser

  Scenario: Steps that do not mention a time offer no timer
    Given I start the application in the "http://localhost:5173/" url
    When I tap the Procurar receitas button
    And I search for "soup" recipes
    And I open the first recipe in the results
    And I tap the Começar a cozinhar button
    And I open the first step with a step timer
    When I open a step without a step timer
    Then I see no timer presets for the current step
    And I close the browser
