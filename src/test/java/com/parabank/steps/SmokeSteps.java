package com.parabank.steps;


import com.parabank.config.Config;
import io.cucumber.java.en.Given; //cette ligne rend l'annotation @Given disponible
import org.testng.Assert; 



public class SmokeSteps {
    @Given("the test framework is ready")
    public void testFrameworkIsReady(){
        System.out.println("The test framework is ready");
    }
    @Given("the test configuration can be loaded")
    public void testConfigurationCanBeLoaded () {
        String uiBaseUrl = Config.get("ui.base.url");
        Assert.assertFalse(uiBaseUrl.isBlank(), "The UI base URL shoult not be empty."); //vérifie que ui.BaseUrl.isBlank est faux
        //si l'URl CONTIENT du texte : isBlank() = false
        //si l'url est vide : isBlank() = true => l'assertion échoue et le message d'erreur est affiché:
    }

    
}