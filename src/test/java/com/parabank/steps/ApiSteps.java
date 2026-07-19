package com.parabank.steps;

import com.parabank.context.TestContext;
import com.parabank.api.ApiClient;
import com.parabank.config.Config;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import io.restassured.response.Response;
import org.testng.Assert;

public class ApiSteps{

    private static final String INVALID_USERNAME = "unkwon-customer";
    private static final String INVALID_PASSWORD = "wrong-password";

    private final ApiClient api = new ApiClient();

    private final TestContext context;
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
    
    @When("valid customer credentials are sent to the login API")
    public void sendValidCredentials(){
        context.response = api.login(Config.get("username"), Config.get("password"));
    }
    @Then("the API authenticates the customer")
    public void verifySuccessfulAuthentication() {
        Assert.assertEquals(context.response.statusCode(), 200, "The login API should return HTTP 200.");
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

}

