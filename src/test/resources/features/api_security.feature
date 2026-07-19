@api
Feature: Security Validation Pure API 
    @positive
    Scenario: Valid credentials (demo) authenticate the customer
        When valid customer credentials are sent to the login API 
        Then the API authenticates the customer
        And the login response does not expose the password 
    @negative
    Scenario: Invalid crendtials are rejected
        When invalid customer credentials are sent to the login API
        Then the login API rejects the request
        And the failed login response does not echo the invalid password