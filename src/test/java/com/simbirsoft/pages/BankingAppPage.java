package com.simbirsoft.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * BankingAppPage.java
 * <p>
 * Page Object для страницы банковского приложения сайта Way2Automation
 * <p>
 * Copyright (c) 2026 Way2Automation. All Rights Reserved.
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 29.05.2026
 */
public class BankingAppPage extends BasePage {
    @FindBy(css = "a[href*='registration']")
    private WebElement sampleFormButton;

    @FindBy(xpath = "//*[@ng-click='customer()']")
    private WebElement customerLoginButton;

    @FindBy(xpath = "//*[@ng-click='manager()']")
    private WebElement bankManagerLoginButton;

    @FindBy(xpath = "//*[@ng-click='home()']")
    private WebElement homeButton;

    public BankingAppPage(WebDriver driver, WebDriverWait waiter) {
        super(driver, waiter);
    }

    public SampleFormPage clickSampleFormButton() {
        sampleFormButton.click();
        return new SampleFormPage(driver, waiter);
    }

    public CustomerLoginPage clickCustomerLoginButton() {
        customerLoginButton.click();
        return new CustomerLoginPage(driver, waiter);
    }

    public BankManagerLoginPage clickBankManagerLoginButton() {
        bankManagerLoginButton.click();
        return new BankManagerLoginPage(driver, waiter);
    }

    public BankingAppPage clickHomeButton() {
        homeButton.click();
        return this;
    }
}
