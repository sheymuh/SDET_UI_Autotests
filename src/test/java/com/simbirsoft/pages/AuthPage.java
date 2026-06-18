package com.simbirsoft.pages;

import com.simbirsoft.helpers.JsExecutorHelper;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * AuthPage.java
 * <p>
 * Page Object для страницы работы с авторизацией сайта HttpWatch
 * <p>
 * Copyright (c) 2026 Way2Automation. All Rights Reserved.
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 18.06.2026
 */
public class AuthPage extends BasePage {
    @FindBy(id = "displayImage")
    private WebElement displayImageButton;

    @FindBy(id = "downloadImg")
    private WebElement authenticatedImage;

    public AuthPage(WebDriver driver, WebDriverWait waiter) {
        super(driver, waiter);
    }

    @Step("Нажатие на кнопку 'Display Image'")
    public AuthPage clickDisplayImageButton() {
        JsExecutorHelper.scrollIntoElementView(driver, displayImageButton);
        displayImageButton.click();

        return this;
    }

    @Step("Получение элемента 'Authenticated Image'")
    public WebElement getAuthenticatedImage() {
        return authenticatedImage;
    }
}
