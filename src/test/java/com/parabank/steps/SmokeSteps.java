package com.parabank.steps;

import io.cucumber.java.en.Given; //cette ligne rend l'annotation @Given disponible



public class SmokeSteps {

    @Given("the test framework is ready")
    public void testFrameworkIsReady() {
        System.out.println("The Framework is ready");
    }

}