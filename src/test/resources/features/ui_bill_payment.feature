@ui @billpay
Feature: Bill Payment Validation

  Background:
    Given an active Parabank customer is available through the API
    And an exisiting customer account is selected through the API
    And the selected customer account is funded through the API
    And the customer is logged in to the web portal

  
  @positive
  Scenario: Pay a bill from a funded account
  When the customer pays a bill of "18.00" from the prepared account
  Then the bill payment is confirmed in the web portal

  @negative
  Scenario: Bill payment rejects missing mandatory information
    When the customer submits the bill payment form without mandatory information
    Then required bill payment validation messages are displayed

  @negative
  Scenario: Bill payment rejects an invalid amount
    When the customer submits a bill payment with an invalid amount
    Then the invalid amount is rejected