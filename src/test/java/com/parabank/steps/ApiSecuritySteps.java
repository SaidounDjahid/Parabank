package com.parabank.steps;

import com.parabank.api.ApiClient;
import com.parabank.config.Config;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import io.restassured.response.Response;
import org.testng.Assert;

public class ApiSecuritySteps{

    private static final String INVALID_USERNAME = "unkwon-customer";
    private static final String INVALID_PASSWORD = "wrong-password";

    private final ApiClient api = new ApiClient();

    private Response response;

    @When("valid customer credentials are sent to the login API")
    public void sendValidCredentials(){
        response = api.login(Config.get("username"), Config.get("password"));
    }
    @Then("the API authenticates the customer")
    public void verifySuccessfulAuthentication() {
        Assert.assertEquals(response.statusCode(), 200, "The login API should return HTTP 200.");
    }
    @Then("the login response does not expose the password")
    public void verifyPasswordIsNotExposed(){
        String body = response.asString().toLowerCase();

        Assert.assertFalse(body.contains(Config.get("password").toLowerCase()),
        "The response should not contain the supplied password.");
    }

    @When("invalid customer credentials are sent to the login API")
    public void sendInvalidCrendtials(){
        response = api.login(INVALID_USERNAME, INVALID_PASSWORD);
    }
    @Then("the login API rejects the request")
    public void verifyRejectedAuthentication(){
        Assert.assertEquals(response.statusCode(),400, "The Login API should rejects invalid credentials");
    }
    @Then("the failed login response does not echo the invalid password")
    public void verifyInvalidPasswordIsNotEchoed(){
        String body = response.asPrettyString().toLowerCase();
        Assert.assertFalse(body.contains(INVALID_PASSWORD.toLowerCase()),"The res^ponse should not echo the invalid password");
    }

}

