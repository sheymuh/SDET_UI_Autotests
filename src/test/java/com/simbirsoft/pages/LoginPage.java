package com.simbirsoft.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * LoginPage.java
 * <p>
 * Page Object для страницы авторизации сайта Way2Automation
 * <p>
 * Copyright (c) 2026 Way2Automation. All Rights Reserved.
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 28.05.2026
 */
public class LoginPage extends BasePage {
    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(css = ".formly-field .form-control")
    private WebElement usernameDescriptionField;

    @FindBy(css = ".form-actions .btn-danger")
    private WebElement loginButton;

    // Блок подсказка с валидным username и password
    @FindBy(className = "alert-info")
    private WebElement validLoginDataTip;

    // Сообщение об ошибке
    @FindBy(className = "alert-danger")
    private WebElement incorrectLoginDataAlert;

    // Сообщение об успешной авторизации
    @FindBy(xpath = "//p[contains(text(),'logged')]")
    private WebElement homeMessage;

    @FindBy(css = "[href='#/login']")
    private WebElement logoutButton;

    public LoginPage(WebDriver driver, WebDriverWait waiter) {
        super(driver, waiter);
    }

    public WebElement getHomeMessage() {
        return homeMessage;
    }

    public WebElement getIncorrectLoginDataAlert() {
        return incorrectLoginDataAlert;
    }

    public boolean isLoginButtonEnabled() {
        return loginButton.isEnabled();
    }

    public boolean allFieldsAreDisplayed() {
        return usernameField.isDisplayed() && passwordField.isDisplayed() && usernameDescriptionField.isDisplayed();
    }

    public boolean isAllFieldsFilled() {
        return (usernameField.getAttribute("value").length() >= 3) && (passwordField.getAttribute("value").length() >= 3)
                && (usernameDescriptionField.getAttribute("value").length() >= 3);
    }

    public LoginPage login(String username, String password, String usernameDescription) {
        usernameField.clear();
        usernameField.sendKeys(username);

        passwordField.clear();
        passwordField.sendKeys(password);

        usernameDescriptionField.clear();
        usernameDescriptionField.sendKeys(usernameDescription);

        loginButton.click();
        return this;
    }

    public LoginPage loginWithValidData() {
        String[] lines = validLoginDataTip.getText().split("\n");
        String username = "";
        String password = "";

        for (String line : lines) {
            if (line.startsWith("Username:")) {
                username = line.substring("Username:".length()).trim();
            } else if (line.startsWith("Password:")) {
                password = line.substring("Password:".length()).trim();
            }
        }

        login(username, password, "some description");
        waiter.until(ExpectedConditions.visibilityOf(homeMessage));

        return this;
    }

    public LoginPage logout() {
        logoutButton.click();
        return this;
    }

}
