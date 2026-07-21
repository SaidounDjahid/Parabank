package com.parabank.pages;

import com.parabank.config.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

//the page that will host all the common actions needed on all the web pages of the parabank software

public class BasePage {

    protected final WebDriver driver; //driver c'est l'objet qui controle chrome : ouvre url, cherche element, cliquer, ecrire, fermer le navigateur
    protected final WebDriverWait wait; // sert a attendre qu'un élement soit prêt

    public BasePage(WebDriver driver){//constructeur de la classe BasePage => construits des objets de type BasePage

        this.driver = driver; //une page recoit le navigateur driver deja ouvert puis construis son attente selenium

        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(
                        Config.getInt("wait.seconds")));
    }

    protected void click(By Locator){ // procedure to click on element = Locator

        //attend jusqu'au ce que Locator soit cliquable puis continue immédiatlement
        wait.until(
                ExpectedConditions.elementToBeClickable(Locator))
                .click(); //attends jusqu'a ce que l'élément soit pret plutot que de faire Thread.Sleep(3000)
    }

    protected void type(By Locator, String text){ //procedure to type in text inside element = Locator

        WebElement element = wait.until(
                ExpectedConditions.visibilityOfElementLocated(Locator));

        element.clear(); //vider l'élément ie efface son ancien contenu
        element.sendKeys(text); // envoyer le texte ie ecrit le nouveau texte text
    }

    protected String getText(By locator) { //to read a text for a Locator, elle lit le texte lisible d'un élément

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator))
                .getText()
                .trim(); //nettoie la chaine des espaces au début et a la fin
    }

    protected void selectByText(By locator, String text) {

        /*
         * On attend que l'option demandé soit disponible dans le select.
         * C'est utile car certaines options ParaBank peuvent arriver après le chargement de la page.
         */
        wait.until(driver -> {

            WebElement selectElement =
                    driver.findElement(locator);

            Select select =
                    new Select(selectElement);

            return select.getOptions()
                    .stream()
                    .anyMatch(option ->
                            option.getText()
                                    .trim()
                                    .equals(text));
        });

        WebElement selectElement =
                driver.findElement(locator);

        new Select(selectElement)
                .selectByVisibleText(text);
    }

    protected void selectByValue(By locator, String value) {//cette méthode selectionne l'option selon la valeur technique contenu dans l'attribut technique html value
        //<select id="type">
        //<option value="0">CHECKING</option>
        //<option value="1">SAVINGS</option>
        //</select>
        // selectByValue(By.id("type"), "1"); Selenium cherche <option value="1"> puis sélectionne SAVINGS

        WebElement element = wait.until(
                ExpectedConditions.elementToBeClickable(locator));

        new Select(element)
                .selectByValue(value);
    }

    protected boolean isVisible(By locator) { //attend qu'un élement devient visible

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator))
                .isDisplayed();
    }

    protected boolean exists(By locator) { //verifie directement si au moins un element visible existe sur la page

        /*
         * findElements ne provoque pas d'erreur quand l'element n'existe pas.
         * Il retourne simplement une liste vide.
         *
         * On vérifie aussi isDisplayed car un element peut etre présent
         * dans le HTML mais caché a l'utilisateur.
         */
        return driver.findElements(locator)
                .stream()
                .anyMatch(WebElement::isDisplayed);
    }

    protected void waitForAny(By... locators) { //attend qu'au moins un des differents résultats possible apparaisse

        /*
         * By... permet de passer plusieurs locators à la méthode.
         *
         * Exemple:
         * waitForAny(accountServicesTitle, loginErrorMessage);
         *
         * L'attente s'arrete dès qu'un des éléments est visible.
         */
        wait.until(currentDriver -> {

            for (By locator : locators) {

                boolean elementIsVisible =
                        currentDriver.findElements(locator)
                                .stream()
                                .anyMatch(WebElement::isDisplayed);

                if (elementIsVisible) {

                    return true;
                }
            }

            return false;
        });
    }

    //les procedures de la page BasePage seront utilisés par toutes les pages du site web
    //(LoginPage, CustomerLookupPage, OpenNewAccountPage, AccountOverviewPage, BillPayPage)
    //elles devront donc utiliser les méthodes de la classe BasePage, il faut alors que les méthodes
    //soient en protected afin que les classes enfants puissent les utiliser
}