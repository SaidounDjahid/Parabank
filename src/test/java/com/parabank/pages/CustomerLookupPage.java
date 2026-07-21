package com.parabank.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CustomerLookupPage extends BasePage {

    // Champs du formulaire Customer Lookup:
    private final By firstNameInput =
            By.name("firstName");

    private final By lastNameInput =
            By.name("lastName");

    private final By streetInput =
            By.name("address.street");

    private final By cityInput =
            By.name("address.city");

    private final By stateInput =
            By.name("address.state");

    private final By zipCodeInput =
            By.name("address.zipCode");

    private final By ssnInput =
            By.name("ssn");

    private final By findLoginInfoButton =
            By.cssSelector("input[value='Find My Login Info']");

    // Ce titre apparaît uniquement lorsqu'une session client est ouverte.
    private final By accountServicesTitle =
            By.xpath("//h2[normalize-space()='Account Services']");

    private final By customerLookupErrorMessage = //message affiché si les informations du customer ne sont pas reconnues
            By.cssSelector("#rightPanel p.error");

    public CustomerLookupPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Remplit le formulaire officiel Customer Lookup de ParaBank.
     *
     * Après validation, ParaBank retrouve les identifiants
     * et ouvre automatiquement une session authentifiée.
     */
    public void recoverLogin( //customer lookup form filling
            String firstName,
            String lastName,
            String street,
            String city,
            String state,
            String zipCode,
            String ssn) {

        type(firstNameInput, firstName);
        type(lastNameInput, lastName);
        type(streetInput, street);
        type(cityInput, city);
        type(stateInput, state);
        type(zipCodeInput, zipCode);
        type(ssnInput, ssn);

        click(findLoginInfoButton);

        /*
         * Après l'envoi du formulaire on attend soit Account Services,
         * soit un message d'erreur venant du Customer Lookup.
         */
        waitForAny(
                accountServicesTitle,
                customerLookupErrorMessage);
    }

    /**
     * Vérifie le résultat réel du fallback ou failsafe :
     * le menu Account Services doit être affiché.
     */
    public boolean isLoggedIn() {//vérifies si le header Account Services est visible ou non

        /*
         * recoverLogin a deja attendu le résultat de la page,
         * donc exists permet de la page,
         * donc exists permet de faire une vérification directe.
         */
        return exists(accountServicesTitle);
    }

    public boolean hasCustomerLookupError() { //verifie si le formulaire Customer Lookup a affiché une erreur

        return exists(customerLookupErrorMessage);
    }

    public String getCustomerLookupErrorMessage() { //retourne le contenu de l'erreur pour faciliter le debug

        if (!hasCustomerLookupError()) {

            return "";
        }

        return getText(customerLookupErrorMessage).trim();
    }
}