package com.parabank.steps;

import com.parabank.context.TestContext;
import com.parabank.api.ApiClient;
import com.parabank.config.Config;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import io.restassured.response.Response; // not needed anymore 

import java.util.List;

import org.apiguardian.api.API;
import org.testng.Assert;

public class ApiSteps{

    private static final String INVALID_USERNAME = "unkwon-customer";
    private static final String INVALID_PASSWORD = "wrong-password";

    private final ApiClient api = new ApiClient();

    private final TestContext context;

    private int newAccountId; // to store new account id created by api call (createAccount)
    //picocontainer will create a new context for each & every scenario during execution

    //constructeur de la classe ApiSteps: to build an ApiSteps, il faut fournir un context (le contexte consiste en int customerId, int sourceAccountId, et un objet Response response qui store la réponse d'api)
    
    //lexo veut eviter le flow suivant: Ouvrir chrome, créer un nouveau client (long formulaire) => se connecter => créer un nouveau compte bancaire => y verser de l'argent => naviguer a l'account overview => vérifier le résutlat

    // ce que l'on va faire : créer via les API un état backend de façon rapide (authentification)+account créeation + versement d'argent (api donc stabilité ). ensuite l'UI validera directement le résultat des séquences précédantes
    //cet état spécifique que l'on voudra vérifier via selenium webdriver a travers les test d'UI

    //example ouvertue de compte bancaire hybride : context.customerId=context.response.jsonPath().getInt("id") => contextId = 12212, étape 2 context.sourceAccountId=accountIds.get(i) (le 1er account i = 0); le context est donc : customerId= 12212 et sourceAccountId = 13344 puis le UI selectionne openNewAccountPage.openAccount("savings",context.sourceAccountId) le compte trouve par API

    //le scenario de paiement de la facture billPaye :
    //API: trouve le client
    //crée ou selectionne un compte bancaire 
    //dépose x amount d'argent
    //conserve SourceAccountId
    //test context : sourceAccountId = 13344
    //UI se connecte au compte  (Sekenium)
    //Ouvre BillPay (Selenium)
    //selectionne le compte  13344 (sauvegarde dans l'une des variables de testContxt)
    //effectue un paiement de 100 euros par example
    //verifie la confirmation

    //séparation des roles:         Responsabilité
    //ApiClient                     Envoi les requetes HTTP aux endpoints REST
    //ApiSteps                      Coordonne les étapes API
    //TestContext                   Mémorise et sauvegarde les données du scénario
    //PicoContainer                 Fournit le meme contexte aux classes 
    //Uisteps                       Coordonne les actions web
    //PageObjects                   Manipulent l'interface web
    //Le flow respect les attentes de la kata : 
    //FeatureFile => StepDefinition => ApiClient ou PageObject => TestContext pour les données partagés
    //
    //
    public ApiSteps(TestContext context) {
        this.context = context;
    }
    
    //-----------------------------------------------------------------------------
    //Security Scenarios
    //-----------------------------------------------------------------------------------

    @When("valid customer credentials are sent to the login API")
    
    public void sendValidCredentials(){
    
        context.response = api.login(Config.get("username"), Config.get("password"));
    }
    
    @Then("the API authenticates the customer")
    
    public void verifySuccessfulAuthentication() {
    
        Assert.assertEquals(context.response.statusCode(), 200, "The login API should return HTTP 200.");
    
        int customerId = context.response.jsonPath().getInt("id"); //parse la réponse, récupère l'id du client a partir du field id et le store dans customerId 
    
        Assert.assertTrue(customerId > 0, "the response should contain a valid customerId"); //assert a vrai que le customerId est positiv et valide
    }
    
    
    @Then("the login response does not expose the password")
    
    public void verifyPasswordIsNotExposed(){
    
        String body = context.response.asString().toLowerCase();

    
        Assert.assertFalse(body.contains(Config.get("password").toLowerCase()),
    
        "The response should not contain the supplied password.");
    }

    @When("invalid customer credentials are sent to the login API")
    
    public void sendInvalidCrendtials(){
    
        context.response = api.login(INVALID_USERNAME, INVALID_PASSWORD);
    }
    @Then("the login API rejects the request")
    
    public void verifyRejectedAuthentication(){
    
        Assert.assertEquals(context.response.statusCode(),400, "The Login API should rejects invalid credentials");
    }
    @Then("the failed login response does not echo the invalid password")
    
    public void verifyInvalidPasswordIsNotEchoed(){
    
        String body = context.response.asPrettyString().toLowerCase();
    
        Assert.assertFalse(body.contains(INVALID_PASSWORD.toLowerCase()),"The res^ponse should not echo the invalid password");
    }

    //------------------------------------------------------------------------
    //Account Creation Scenario
    //---------------------------------------------------------
    @Given("an active Parabank customer is available through the API")
    
    public void activeCustomerIsAvailable(){
        
        context.response= api.login(Config.get("username"), Config.get("password")); 
        
        Assert.assertEquals(context.response.statusCode(), 200, "The customer login should succeed");
        
        context.customerId=context.response.jsonPath().getInt("id"); // on sauvegarde le id du client donné par la réponse api.login dans l'attribut customerId de l'objet contexte
        
        Assert.assertTrue(context.customerId > 0, "A valid customer Id should be returner");
    }

    @Given("an exisiting customer account is selected through the API")
    
    public void existingAccountIsSelected(){
        
        context.response=api.getCustomerAccounts(context.customerId);//on appelle getCustomerAccounts (qui donne la liste des compte du client) sur api et on store la réponse dans context.response
        
        Assert.assertEquals(context.response.statusCode(), 200, "The accounts retriebal operation should succeed");

        List<Integer> accountIds = 
            context.response.jsonPath().getList("id", Integer.class); //to fitler & extract data (ids) from jsom structure body

        Assert.assertNotNull(accountIds, "The account lis should exist"); //on verifie que assertNotNull n'est pas null
        
        Assert.assertFalse(accountIds.isEmpty(), "The customer should have at least one account"); //on verifie que accountIds qui est une list non vide

        context.sourceAccountId = accountIds.get(0); // on sauvegarde dans l'attribut sourceAccountId de notre objet context le id du 01 1er compte de notre qui client qui apparait dans la réponse de getCustomerAccounts
        
    }

    private int accountTypeNumber(String accountType) { // methode (fonction) qui convertit et la chaine de caractère "CHECKING" ou "SAVING" ou "LOAN"vers un entier 

            if ("CHECKING".equalsIgnoreCase(accountType)) {
                return 0;
            }
            if ("SAVINGS".equalsIgnoreCase(accountType)) {
                return 1;
            }
            if ("LOAN".equalsIgnoreCase(accountType)){ // le type loan n'est pas disponible a la creation a travers le UI mais la valeur 2 correspond au type Load, dans le UI il y'a une flow métier dédié a l'ouverture d'un crédit (autre page)
                return 2;
            }
            throw new IllegalArgumentException("Unkwon account Type" + accountType); // throw an exception if account is not recognized 
    }


    @When("a {string} account is created through the API")
    
    public void createAccountThroughApi(String accountType){
        
        context.response=api.createAccount(context.customerId, accountTypeNumber(accountType), context.sourceAccountId); //car createAccount attend un nombre au niveau de accountType int)

        if (context.response.statusCode() == 200) {
            newAccountId = context.response.jsonPath().getInt("id");//sauvegarde dans newAccountId le id du nouveau compte bancaire crée
        }
        
    }

    @Then("the API returns the newly created account")
    
    public void apiReturnNewAccount(){
    
        Assert.assertEquals(context.response.statusCode(), 200, "Account Creatuion should return HTTP 200");
    
    }

    @Then("the new account belongs to the authenticated customer")
    public void newAccountBelongsToCustomer(){ // on va vérifier que le nouveau account créeé appartient maintenant à la liste des comptes de notre customer en appellant getCustomerAccounts et en vérifiant que le newAccountId y est
        context.response=api.getCustomerAccounts(context.customerId);

        Assert.assertEquals(context.response.statusCode(), 200, "The accounts retrieval request should succed");

        List<Integer> accountIds = context.response.jsonPath().getList("id", Integer.class);

        
        Assert.assertNotNull(
                accountIds,
                "The account list should exist."); //la liste des compte existe

        Assert.assertTrue(accountIds.contains(newAccountId),
        "The newly created account should belong to the customer"); // assertion sur l'object accountIds qui est une liste qui contient les comptes de notre customer pour voir si cette liste contient le nouveau compte bancaire créer par l'api?        

    
    }

    ////// negativ
    @When("account creation is requested with unknown funding account")
    public void createAccountWithUnknownFundingAccount(){

        context.response = api.createAccount(context.customerId, 1, Integer.MAX_VALUE); // tente de créer un compte bancaire avec un compte source inconne (random)


    }

    @Then("the account creation API reject the request")
    public void accountCreationIsRejected(){
        Assert.assertEquals(context.response.statusCode(), 400, "The API should reject an uknwon funding account");
    }


    @Given("the selected customer account is funded through the API")
    public void selectedCustomerAccountIsFunded() {

    int existingAccountId = context.sourceAccountId;

    context.response = api.createAccount(
            context.customerId,
            0,
            existingAccountId);

    Assert.assertEquals(
            context.response.statusCode(),
            200);

    context.sourceAccountId =
            context.response.jsonPath().getInt("id");

    context.response = api.deposit(
            context.sourceAccountId,
            Config.get("billpay.deposit"));

    Assert.assertEquals(
            context.response.statusCode(),
            200);
      

            Assert.assertEquals(
            context.response.statusCode(),
            200,
            "The deposit API should return HTTP 200.");
              }
}








