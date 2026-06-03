package com.simbirsoft.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Optional;

/**
 * BankManagerLoginPage.java
 * <p>
 * Page Object для страницы работы с клиентами в банковском приложении сайта Way2Automation
 * <p>
 * Copyright (c) 2026 Way2Automation. All Rights Reserved.
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 01.06.2026
 */
public class BankManagerLoginPage extends BasePage {
    @FindBy(xpath = "//*[@ng-click='addCust()']")
    private WebElement addCustomerFormButton;

    @FindBy(xpath = "//*[@ng-click='openAccount()']")
    private WebElement openAccountFormButton;

    @FindBy(xpath = "//*[@ng-click='showCust()']")
    private WebElement customersTableButton;

    @FindBy(xpath = "//*[@ng-model='fName']")
    private WebElement firstNameField;

    @FindBy(xpath = "//*[@ng-model='lName']")
    private WebElement lastNameField;

    @FindBy(xpath = "//*[@ng-model='postCd']")
    private WebElement postCodeField;

    @FindBy(className = "btn-default")
    private WebElement addCustomerButton;

    @FindBy(id = "userSelect")
    private WebElement customerSelect;

    @FindBy(id = "currency")
    private WebElement currencySelect;

    @FindBy(xpath = "//*[text() = 'Process']")
    private WebElement processButton;

    @FindBy(xpath = "//*[@ng-model='searchCustomer']")
    private WebElement searchCustomerField;

    @FindBy(css = "tbody td:first-child")
    private List<WebElement> customersFirstNames;

    @FindBy(css = "tbody td:last-child button")
    private List<WebElement> deleteButtons;

    @FindBy(css = "tbody tr")
    private List<WebElement> customersTableRows;

    public BankManagerLoginPage(WebDriver driver, WebDriverWait waiter) {
        super(driver, waiter);
    }

    public BankManagerLoginPage clickCustomersTableButton() {
        customersTableButton.click();
        waiter.until(ExpectedConditions.visibilityOf(searchCustomerField));
        return this;
    }

    public BankManagerLoginPage fillCustomerFields(String fName, String lName, String postCode) {
        firstNameField.clear();
        firstNameField.sendKeys(fName);

        lastNameField.clear();
        lastNameField.sendKeys(lName);

        postCodeField.clear();
        postCodeField.sendKeys(postCode);

        return this;
    }

    public BankManagerLoginPage addCustomer(String fName, String lName, String postCode) {
        addCustomerFormButton.click();

        fillCustomerFields(fName, lName, postCode);

        addCustomerButton.click();

        return this;
    }

    public BankManagerLoginPage selectCustomerByVisibleText(String customerVisibleText) {
        Select select = new Select(customerSelect);
        select.selectByVisibleText(customerVisibleText);

        return this;
    }

    public BankManagerLoginPage selectCurrencyByVisibleText(String currencyVisibleText) {
        Select select = new Select(currencySelect);
        select.selectByVisibleText(currencyVisibleText);

        return this;
    }

    public BankManagerLoginPage openAccount(String fName, String lName, String currency) {
        openAccountFormButton.click();

        selectCustomerByVisibleText(fName + " " + lName);
        selectCurrencyByVisibleText(currency);

        processButton.click();

        return this;
    }

    public int findCustomerIndexByFirstName(String fName) {
        Optional<WebElement> customer = customersFirstNames.stream()
                .filter(name -> fName.equalsIgnoreCase(name.getText()))
                .findFirst();

        return customer.map(webElement -> customersFirstNames.indexOf(webElement)).orElse(-1);
    }

    public BankManagerLoginPage deleteCustomer(int index) {
        deleteButtons.get(index).click();

        return this;
    }
}
