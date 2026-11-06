#Author: your.email@your.domain.com
#Keywords Summary :
#Feature: List of scenarios.
#Scenario: Business rule through list of steps with arguments.
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
#And,But: To enumerate more Given,When,Then steps
#Scenario Outline: List of steps for data-driven as an Examples and <placeholder>
#Examples: Container for s table
#Background: List of steps run before each of the scenarios
#""" (Doc Strings)
#| (Data Tables)
#@ (Tags/Labels):To group Scenarios
#<> (placeholder)
#""
## (Comments)
#Sample Feature Definition Template
@tag
Feature: Create new food category (US35)
	As a user
	I want to create a new food category
	So I always have it catalogued 

  Scenario: Add and verify a new category
    Given I start the application "http://localhost:5173" url
    When I tap the adicionar categoria button
    And I name the category
    And I save the category
    And I see the the category in the table below
    Then I close the browser