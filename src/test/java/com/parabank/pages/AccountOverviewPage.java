package com.parabank.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AccountOverviewPage extends BasePage { //c la meme elle hérite de la base page et de toutes ces méthodes

    private final By accountsOverviewLink = //lien cliauqble vers la page accountOVerview
            By.linkText("Accounts Overview");

    private final By accountTable = //le tableau des comptes possède le id="accountTable"
            By.id("accountTable");

    public AccountOverviewPage(WebDriver driver) { //Constructeur de la classe AccountOverviewPage qui prend le chrome driver en paramètre de création
        super(driver);
    }

    public void open() {

        click(accountsOverviewLink); //clique sur le lien pour ouvrir la page

        wait.until(
                ExpectedConditions.visibilityOfElementLocated( // et attend l'affichage du tableau des comptes
                        accountTable));
    }

    public boolean containsAccount(int accountId) { // vérifies sur le tableau si le compte accountId existe

        By accountLink =
                By.linkText(String.valueOf(accountId));

        return wait.until(
                driver -> !driver.findElements(accountLink)
                        .isEmpty());
    }
}