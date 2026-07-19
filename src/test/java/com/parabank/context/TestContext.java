//Test context is the temporary stack (or memory) of a scenario
//Scenario a => TestContext a , scenario b => TestContext b 
//the data does not mix up

//picocontainer is an small object injecter, its goal is to give the same object to the classes that need it 

//TestContext to memorise dynamic data of a scenario, such as the customerId, sourceAccountId & last HTTP response

//Pico container inject la meme instance de ce contexte dans les différentes classes de steps et les hooks pendant un scenario

//cela permet a l'api de préparer un compte bancaire et a selenium de réutiliser son identifiant, ce qui répond directement a l'exigence d'approche hybride de la kata 

//PicoContainer
//      ↓ fournit
//   TestContext
//       ↙       ↘
// ApiSteps   UiSteps
//   ↓ écrit   ↓ lit
//   sourceAccountId
//
//
//
//
//
package com.parabank.context;

import io.restassured.response.Response;



//TestContext a des champs publics
public class TestContext {

    public Response response;
    public int customerId;
    public int sourceAccountId;


    
}