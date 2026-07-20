package com.parabank.hooks;

import com.parabank.config.Config;
import com.parabank.context.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


public class Hooks {

    private final TestContext context;
    
    Public Hooks (TestContext context) { //constructeur de la classe Hooks => crée un objet Hooks et lui assigne l'objet context instance de la classe TestContext
        this.context=context;
    }


    @Before("@ui") //execute les actions qui suit si le tag @ui est rencontré 
    //avant chaque scenario portant le tag "ui" ouvre chrome 
    

    //ouverture de chrome et transmissions des options du chromeDriver
    public void startBrowser(){ //lance un browser + instancie un objet options que l'on passera a l'objet driver (qui lui est un objet de type WebDriver)
        ChromeOptions options = new ChromeOptions();

        if (Config.getBoolean("headless")) { //si headless égale true
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1920,1080"); //rajoute les détails sur la fenetre
        context.driver = new ChromeDriver(options); // instancie un objet nouveau de type ChromeDriver et l'assigne a l'object driver de type Webdriver

        if (!Config.getBoolean("headless")) { // si headless est égal a false
        context.driver.manage().window().maximize(); //lance la fenetre
        }
    }

    @After("ui")
    //Après chaque scénario UI, fermer Chrome.

    public void closeBrowser(){
        if (context.driver != null ) { //si driver contient quelque chose alors quitte le 
            context.driver.quit();
            context.driver = null;
        }
    }



}
