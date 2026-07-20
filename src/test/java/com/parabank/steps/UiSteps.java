package com.parabank.steps;

import com.parabank.pages.BillPayPage;

import com.parabank.config.Config;
import com.parabank.context.TestContext;
import com.parabank.pages.AccountOverviewPage;
import com.parabank.pages.LoginPage;
import com.parabank.pages.OpenNewAccountPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

//Ne contient aucun locators (comme le demande la kata d'éviter d'introduire les locators dans les step définitionns)

public class UiSteps { 

    private final TestContext context;

    private LoginPage loginPage;
    private OpenNewAccountPage openNewAccountPage;
    private AccountOverviewPage accountOverviewPage;

    private BillPayPage billPayPage;
private String lastBillPaymentAmount;

    public UiSteps(TestContext context) { //Constructeur de UiSteps a besoin de context (pico container donne le meme context a ApiSteps, Hooks, et UiSteps )
        this.context = context;
    }

    //ApiSteps écrit dans context.sourceAccountId
// UiSteps lit  context.sourceAccountId
// Hooks écrit context.driver
// UiSteps lit context.driver

    @Given("the customer is logged in to the web portal") //Elle crée les Page Objects avec le navigateur ouvert par le Hook context.driver :
    public void customerIsLoggedIn() {



        loginPage = new LoginPage(context.driver);

        openNewAccountPage = new OpenNewAccountPage(context.driver);

        accountOverviewPage = new AccountOverviewPage(context.driver);

        billPayPage = new BillPayPage(context.driver); 

        loginPage.open();

        loginPage.login(
                Config.get("username"),
                Config.get("password"));

        Assert.assertTrue(
                loginPage.isLoggedIn(),
                "The customer should be logged in.");
    }

    @When("the customer opens a {string} account from the web portal") //le {string} recoit 'SAVINGS' depuis le feature file
    public void customerOpensAccount(String accountType) {

        openNewAccountPage.open();

        openNewAccountPage.createAccount( //elle utilise le compte source préparé par l'API 
                accountType,
                context.sourceAccountId);

        context.newAccountId = //on conserve le id du nouvel account récemment créee car on l'on servira plus tard dans la suite dans l'étape the new account is created successfullyy
                openNewAccountPage.getNewAccountId();
    }

    @Then("the new account is created successfully")
    public void accountIsCreatedSuccessfully() {

        Assert.assertTrue(
                openNewAccountPage.isConfirmationDisplayed(),
                "The account creation confirmation should appear.");

        }

    @Then("the new account is displayed in account overview")//assertions 
    public void accountIsDisplayedInOverview() {

        accountOverviewPage.open();

        Assert.assertTrue(
                accountOverviewPage.containsAccount(
                        context.newAccountId),
                "The new account should appear in Account Overview.");
    }



    @When("the customer pays a bill of {string} from the prepared account")
    public void customerPaysBill(String amount) {

    billPayPage.open();

    lastBillPaymentAmount = amount;

    billPayPage.payBill(
            amount,
            context.sourceAccountId);
    }   
    
    
    
    
    @Then("the bill payment is confirmed in the web portal")
    public void billPaymentIsConfirmed() {

    Assert.assertTrue(
            billPayPage.isPaymentConfirmed(),
            "The bill payment confirmation should be displayed.");

    String confirmation =
            billPayPage.getConfirmationText();

    Assert.assertTrue(
            confirmation.contains(lastBillPaymentAmount),
            "The confirmation should display the payment amount.");

    Assert.assertTrue(
            confirmation.contains(
                    String.valueOf(context.sourceAccountId)),
            "The confirmation should display the source account.");
    }

    //formulaire vide
    @When("the customer submits the bill payment form without mandatory information")
    public void customerSubmitsEmptyBillPaymentForm() {

    billPayPage.open();
    billPayPage.submitEmptyForm();
    }

    //fields verification des champs obligatoires
        @Then("required bill payment validation messages are displayed")
        public void requiredValidationMessagesAreDisplayed() {

        Assert.assertTrue(
            billPayPage.hasAnyVisibleValidationError(),
            "At least one mandatory field validation should be displayed.");
        }

    //montant = string par egg
    @When("the customer submits a bill payment with an invalid amount")
    public void customerSubmitsInvalidAmount() {

    billPayPage.open();

    billPayPage.submitInvalidAmount(
            context.sourceAccountId);
    }
    //assertion du message d'errur Please enter a valid amount.
    @Then("the invalid amount is rejected")
    public void invalidAmountIsRejected() {

    Assert.assertTrue(
            billPayPage.hasVisibleError(
                    "Please enter a valid amount."),
            "The invalid amount validation should appear.");
}

}