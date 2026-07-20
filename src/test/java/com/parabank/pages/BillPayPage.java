package com.parabank.pages;

import com.parabank.config.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class BillPayPage extends BasePage {

    private final By billPayLink =
            By.linkText("Bill Pay");

    private final By payeeNameField =
            By.name("payee.name");

    private final By addressField =
            By.name("payee.address.street");

    private final By cityField =
            By.name("payee.address.city");

    private final By stateField =
            By.name("payee.address.state");

    private final By zipCodeField =
            By.name("payee.address.zipCode");

    private final By phoneField =
            By.name("payee.phoneNumber");

    private final By accountNumberField =
            By.name("payee.accountNumber");

    private final By verifyAccountField =
            By.name("verifyAccount");

    private final By amountField =
            By.name("amount");

    private final By sourceAccountSelect =
            By.name("fromAccountId");

    private final By sendPaymentButton =
            By.cssSelector("input[value='Send Payment']");

    private final By billPayResult =
            By.id("billpayResult");

    private final By validationErrors =
            By.cssSelector("#billpayForm .error");

    public BillPayPage(WebDriver driver) {
        super(driver);
    }

    public void open() {

        click(billPayLink);
    }

    public void payBill(
            String amount,
            int sourceAccountId) {

        fillValidPayeeInformation();

        String payeeAccount =
                Config.get("billpay.payee.account");

        type(accountNumberField, payeeAccount);
        type(verifyAccountField, payeeAccount);
        type(amountField, amount);

        chooseSourceAccount(sourceAccountId);

        click(sendPaymentButton);
    }

    public void submitEmptyForm() {

        click(sendPaymentButton);
    }

    public void submitInvalidAmount(
            int sourceAccountId) {

        fillValidPayeeInformation();

        String payeeAccount =
                Config.get("billpay.payee.account");

        type(accountNumberField, payeeAccount);
        type(verifyAccountField, payeeAccount);

        type(amountField, "abc");
        chooseSourceAccount(sourceAccountId);

        click(sendPaymentButton);
    }

    public boolean isPaymentConfirmed() {

        return isVisible(billPayResult);
    }

    public String getConfirmationText() {

        return getText(billPayResult);
    }


    public boolean hasVisibleError(String expectedMessage) {

    String expected =
            expectedMessage.toLowerCase();

    return wait.until(driver ->
            driver.findElements(validationErrors)
                    .stream()
                    .filter(WebElement::isDisplayed)
                    .map(error ->
                            error.getText()
                                    .trim()
                                    .toLowerCase())
                    .anyMatch(text ->
                            text.contains(expected)));
}

    private void fillValidPayeeInformation() {

        type(
                payeeNameField,
                Config.get("billpay.payee.name"));

        type(
                addressField,
                Config.get("billpay.payee.address"));

        type(
                cityField,
                Config.get("billpay.payee.city"));

        type(
                stateField,
                Config.get("billpay.payee.state"));

        type(
                zipCodeField,
                Config.get("billpay.payee.zip"));

        type(
                phoneField,
                Config.get("billpay.payee.phone"));
    }


    private void chooseSourceAccount(int sourceAccountId) {

    By option = By.cssSelector(
            "#billpayForm option[value='"
                    + sourceAccountId
                    + "']");

    wait.until(driver ->
            !driver.findElements(option).isEmpty());

    selectByValue(
            sourceAccountSelect,
            String.valueOf(sourceAccountId));


}

    public boolean hasAnyVisibleValidationError() {

    return wait.until(driver ->
            driver.findElements(validationErrors)
                    .stream()
                    .anyMatch(error ->
                            error.isDisplayed()
                                    && !error.getText()
                                            .trim()
                                            .isEmpty()));
    }
}