package com.parabank.pages;

import com.parabank.config.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage { //LoginPage herite de BasePage ie hérite des méthodes clic, type, isVisible ...

    private final By usernameField = //les locators sont en private final => les credentials et leur paramètre restent uniquement ocnnues dans le scope de ma classe LoginPage, Ui steps ne doit pas savoir que le champ possède name = "username" 
            By.name("username");

    private final By passwordField =
            By.name("password");

    private final By loginButton =
            By.cssSelector("input[value='Log In']");

    private final By accountServicesTitle = //le titre qui apparait une fois connecté
            By.xpath("//*[@id='leftPanel']/h2");

    public LoginPage(WebDriver driver) { //le constructeur de LoginPage : super(driver) appelle a son tour le constructeur de BasePage

        super(driver); //il transmet donc le navigateur a la classe 
    }

    public void open() { //open permet de construire l'URL de la page login en concaténant le ui.base.url avec "/index.htm"

        driver.get(
                Config.get("ui.base.url") + "/index.htm");

        wait.until(//attend l'affiche du field username pour valider ouverture correcte de la page
                driver -> driver.findElement(usernameField)
                        .isDisplayed());
    }

    public void login(String username, String password) { // décrot les actions de se connecter 

        type(usernameField, username); //appelle la méthode type héritée de la classe BasePage : introduit le username john
        type(passwordField, password); // appelle la méthide type hértiée de le classe BasePage: intrduit le mdp 
        click(loginButton); //clique sur le login button
    }

    public boolean isLoggedIn() {//verifie que le titre visible après connexion apparaît.

        return isVisible(accountServicesTitle); //verifie sir le titre est visible a l'écran et retourne un boolean connecté true or false
    }
}