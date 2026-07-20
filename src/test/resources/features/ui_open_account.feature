@ui
Feature: Open New Account from UI and Verify Account Overview

  Background:
    Given an active Parabank customer is available through the API  
    And an exisiting customer account is selected through the API 
    And the customer is logged in to the web portal 

  @positive
  Scenario: Open a new savings account
    When the customer opens a "SAVINGS" account from the web portal
    Then the new account is created successfully
    And the new account is displayed in account overview