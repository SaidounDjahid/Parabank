package com.parabank.api;

import com.parabank.config.Config;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

//ijnstrcuiton : 

public class ApiClient { 
    // la méthode login recoit deux textes et retourne une réponse HTTP de type Response
    public Response login(String username, String password) {

        //given() commence la préparation de la requete : 

        return given()
                .baseUri(Config.get("api.base.url"))
                .accept(ContentType.JSON) // indique que nous souhaitons du JSON
                .pathParam("username", username) // prepare le rmeplacement de {username}
                .pathParam("password", password) //// prepare le rmeplacement de {password}
                .when()
                .get("login/{username}/{password}"); //envoie une requete HTTP Get vers notre endpoint https://parabank.parasoft.com/parabank/services/bank en passant le contenu des variables {username} et password dans le path parameter donc https://parabank.parasoft.com/parabank/services/bank/login/john/demo

        }

    
    //getCustomerAccounts récupère tous les comptes appartenant au client de id customerId (par example id du customer john = 12212)    
    public Response getCustomerAccounts(int customerId){
        return given()
                .baseUri(Config.get("api.base.url"))
                .accept(ContentType.JSON)
                .pathParam("customerId", customerId) // passage des param dans l'url
                .when()
                .get("customers/{customerId}/accounts");
                
    }    

    //crée un nouveau compte bancaire newAccountId de type accounType au client de id = customeriD à partir du compte sourceAccountId
    //createAccount(customerId, accountType, sourceAccountId) 
    //crée un compte bancaire nouveau en utilisant un compte bancaire existant (sourceAccountId) comme funding source
    //Parabank attend 0 = Nouveau compte sera un compte Checking
    //Parabank attend 1 = Nouveau compte sera un compte Savings
    public Response createAccount(int customerId, int accountType, int sourceAccountId){
        return given()
                .baseUri(Config.get("api.base.url"))
                .accept(ContentType.JSON)
                .queryParam("customerId", customerId)
                .queryParam("newAccountType", accountType)
                .queryParam("fromAccountId", sourceAccountId)
                .post("/createAccount");
    }

    public Response deposit(int accountId, String amount) {
    return given()
            .baseUri(Config.get("api.base.url"))
            .accept(ContentType.JSON)
            .queryParam("accountId", accountId)
            .queryParam("amount", amount)
            .when()
            .post("/deposit");
        }

    
}
