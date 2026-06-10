package com.simbirsoft.pages;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * SqlExPage.java
 * <p>
 * Page Object для страницы с формой авторизации сайта с упражнениями SQL sql-ex
 * <p>
 * Copyright (c) 2026 Way2Automation. All Rights Reserved.
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 09.06.2026
 */
public class SqlExPage extends BasePage {
    @FindBy(xpath = "//*[@name='frmlogin']//*[@name='login']")
    private WebElement loginField;

    @FindBy(xpath = "//*[@name='frmlogin']//*[@name='psw']")
    private WebElement passwordField;

    @FindBy(xpath = "//*[@name='frmlogin']//*[@value='Вход']")
    private WebElement loginButton;

    @FindBy(css = "b a[href*='/personal.php']")
    private WebElement accountTag;

    public SqlExPage(WebDriver driver, WebDriverWait waiter) {
        super(driver, waiter);
    }

    public boolean isUserLoggedIn() {
        try {
            return accountTag.isDisplayed();
        } catch (NoSuchElementException ex) {
            return false;
        }
    }

    public SqlExPage login(String login, String password) {
        loginField.clear();
        loginField.sendKeys(login);

        passwordField.clear();
        passwordField.sendKeys(password);

        waiter.until(ExpectedConditions.elementToBeClickable(loginButton));
        loginButton.click();

        return this;
    }
}
