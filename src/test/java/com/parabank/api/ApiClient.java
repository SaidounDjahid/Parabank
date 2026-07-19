package com.parabank.api;

import com.parabank.config.Config;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

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
    
}
