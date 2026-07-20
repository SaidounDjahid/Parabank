@api
Feature: Account creation through parabank API

    Background:
        Given an active Parabank customer is available through the API
        And an exisiting customer account is selected through the API

    @positive
    Scenario: Create a checking account
        When a "CHECKING" account is created through the API
        Then the API returns the newly created account
        And the new account belongs to the authenticated customer   

    @negative
    Scenario: Reject Creation with an unknown funding account
        When account creation is requested with unknown funding account
        Then the account creation API reject the request     