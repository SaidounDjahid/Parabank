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

   private final By accountServicesTitle = //le titre Account Services apparait uniquement une fois réellement connecté
            By.xpath("//*[@id='leftPanel']//h2[normalize-space()='Account Services']");

    private final By loginErrorMessage = //message affiché lorsque le login UI n'arrive pas a authentifier le client
            By.cssSelector("#rightPanel p.error");

    private final By forgotLoginInfoLink = //lien permettant d'ouvrir la page Customer Lookup
            By.linkText("Forgot login info?");


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

    public void login(String username, String password) { // décrit les actions de se connecter

        type(usernameField, username); //appelle la méthode type héritée de la classe BasePage : introduit le username john
        type(passwordField, password); // appelle la méthode type hértiée de le classe BasePage: intrduit le mdp
        click(loginButton); //clique sur le login button

        /*
         * Après le click, deux résultats sont possibles:
         * soit Account Services apparait et le login est réussi,
         * soit ParaBank affiche un message d'erreur.
         */
        waitForAny(
                accountServicesTitle,
                loginErrorMessage);
    }

    public boolean isLoggedIn() {//verifie que le titre visible après connexion apparaît.

        /*
         * On utilise exists et non isVisible ici car login() a deja attendu
         * soit le titre Account Services soit le message d'erreur.
         * Cela évite une nouvelle attente de 30 secondes.
         */
        return exists(accountServicesTitle);
    }

    public boolean hasLoginError() { //verifie si ParaBank a affiché une erreur après la tentative de connexion

        return exists(loginErrorMessage);
    }

    public String getLoginErrorMessage() { //récupère le texte d'erreur principalement pour les logs et les assertions

        if (!hasLoginError()) {

            return "";
        }

        return getText(loginErrorMessage).trim();
    }

    public void openCustomerLookup() { //ouvre le parcours Forgot login info qui servira de fallback

        click(forgotLoginInfoLink);
    }
}