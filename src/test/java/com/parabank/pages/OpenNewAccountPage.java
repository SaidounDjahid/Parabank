package com.parabank.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class OpenNewAccountPage extends BasePage { // la classe OpenNewAccount hérite de la ClasseBase page donc hérite également de toutes ces méthodes

    private final By openNewAccountLink =   //ou cliquer pour pour ouvrir la page
            By.linkText("Open New Account");

    private final By accountTypeSelect = //le id pour le menu déroulant choisir CHECKING OU SAVINGS
            By.id("type");

    private final By sourceAccountSelect = //ou choisir le compte source ou funding accocunt
            By.id("fromAccountId");

    private final By openAccountButton =                         //boutton de confirmation d'ouvertue du compte
            By.cssSelector("input[value='Open New Account']");

    private final By confirmationTitle =     //verifie la baliseopenAccountResult qui affiche le header AccountOpened
            By.id("openAccountResult");

    private final By newAccountId = //contient le account id nouvellement créee 
            By.id("newAccountId");

    public OpenNewAccountPage(WebDriver driver) {
        super(driver);
    }

    public void open() {//ouvre la page de création d'un nouveau compte a partir du dashboard

        click(openNewAccountLink); //click
    }

    public void createAccount( // crée un nouvea compte avec acountType="SAVINGS" (vient du ui_open_account.feature) et et compte source = 13344
            String accountType,
            int sourceAccountId) {

        selectByText( //choisi le type de compte (Savings/checking) et le passe a la methode SelextByText 
                accountTypeSelect, //locator details + type de compte 
                accountType);
        selectByText(sourceAccountSelect,String.valueOf(sourceAccountId));


                    click(openAccountButton);}

    public boolean isConfirmationDisplayed() { //méthode qui vérifie si la confirmation de création du nouveau compte est affichée ou pas

        return isVisible(confirmationTitle);
    }

    public int getNewAccountId() {// getText retourne un String puis Integer.parseInt le transforme en entier pour que getNewAccountId puisse le retourner

        return Integer.parseInt(
                getText(newAccountId));
    }
}