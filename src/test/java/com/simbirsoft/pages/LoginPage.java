package com.simbirsoft.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
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
    public static final String LOGIN_SUCCESS_MESSAGE = "You're logged in!!";
    public static final String LOGIN_FAILURE_MESSAGE = "Username or password is incorrect";

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

    @Step("Получение элемента сообщения об успешной авторизации")
    public WebElement getHomeMessage() {
        return homeMessage;
    }

    @Step("Получение элемента сообщения об ошибке авторизации")
    public WebElement getIncorrectLoginDataAlert() {
        return incorrectLoginDataAlert;
    }

    @Step("Проверка доступности кнопки 'Login'")
    public boolean isLoginButtonEnabled() {
        return loginButton.isEnabled();
    }

    @Step("Проверка отображения всех полей формы")
    public boolean allFieldsAreDisplayed() {
        return usernameField.isDisplayed() && passwordField.isDisplayed() && usernameDescriptionField.isDisplayed();
    }

    @Step("Проверка все ли поля заполнены")
    public boolean isAllFieldsFilled() {
        return (usernameField.getAttribute("value").length() >= 3) && (passwordField.getAttribute("value").length() >= 3)
                && (usernameDescriptionField.getAttribute("value").length() >= 3);
    }

    /**
     * Возвращает username из подсказки
     * @return username из подсказки или пустую строку если username не найден
     */
    @Step("Получение username из подсказки")
    public String getUsernameFromTip() {
        String[] lines = validLoginDataTip.getText().split("\n");

        for (String line : lines) {
            if (line.startsWith("Username:")) {
                return line.substring("Username:".length()).strip();
            }
        }

        return "";
    }

    /**
     * Возвращает password из подсказки
     * @return password из подсказки или пустую строку если password не найден
     */
    @Step("Получение password из подсказки")
    public String getPasswordFromTip() {
        String[] lines = validLoginDataTip.getText().split("\n");

        for (String line : lines) {
            if (line.startsWith("Password:")) {
                return line.substring("Password:".length()).strip();
            }
        }

        return "";
    }

    @Step("Авторизация с параметрами: username={username}, password={password}, description={usernameDescription}")
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

    @Step("Клик по кнопке 'Logout'")
    public LoginPage logout() {
        logoutButton.click();
        return this;
    }
}
