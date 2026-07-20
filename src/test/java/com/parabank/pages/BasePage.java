package com.parabank.pages;

import com.parabank.config.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {

    protected final WebDriver driver;
    protected final WevDriverWait wait;

    public BasePage(WebDriver driver){//constructeur de la classe BasePage => construits des objets de type BasePage
        this.driver = driver;

        this.wait = new WebDriverWait(driver, Duration.ofSeconds( //le contenu de la clé wait.seconds a partir du fichier config.properties
                        Config.getInt("wait.seconds")));

    }

    protected void click(By Locator){ // procedure to click on element = Locator
        wait.until(ExpectedConditions.elementToBeClickable(Locator)).click();
    }

    protected void type (By Locator, String text){ //procedure to type in text inside element = Locator
            
        WebElement element = wait.until(
        ExpectedConditions.visibilityOfElementLocated(locator));

        element.clear(); //vider l'élément 
        element.sendKeys(text); // envoyer le texte
    }

    protected String getText(By locator) { //to read a test for a Locator

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator))
                .getText()
                .trim(); //nettoie la chaine des espaces au début et des espaces a la   
    }

    protected void selectByText(By locator, String text) { //this method select the option by the visible text to the user par example <select id="type">
    //<option value="0">CHECKING</option>
    //<option value="1">SAVINGS</option>
    //</select> 
    //selectByText(By.id("type"), "SAVINGS"); => selenium cherche l'option visible a l'utilisateur i.e Savings

    //

        WebElement element = wait.until(
                ExpectedConditions.elementToBeClickable(locator));

        new Select(element).selectByVisibleText(text);
    }

    protected void selectByValue(By locator, String value) {//cette méthode selectionne l'option selon la valeur technique contenu dans l'attribut html value
    //<select id="type">
    //<option value="0">CHECKING</option>
    //<option value="1">SAVINGS</option>
    //</select>
    // selectByValue(By.id("type"), "1"); Selenium cherche <option value="1"> puis sélectionne SAVINGS


        WebElement element = wait.until(
                ExpectedConditions.elementToBeClickable(locator));

        new Select(element).selectByValue(value);
    }

    protected boolean isVisible(By locator) { //attend qu'un élement devient visible

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator))
                .isDisplayed();
    }


}
