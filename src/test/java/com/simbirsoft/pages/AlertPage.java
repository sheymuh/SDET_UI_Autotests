package com.simbirsoft.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * AlertPage.java
 * <p>
 * Page Object для страницы работы с алертами сайта Way2Automation
 * <p>
 * Copyright (c) 2026 Way2Automation. All Rights Reserved.
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 18.06.2026
 */
public class AlertPage extends BasePage {
    @FindBy(css = "div#example-1-tab-2 iframe.demo-frame")
    private WebElement demoFrame;

    @FindBy(css = "a[href='#example-1-tab-2']")
    private WebElement inputAlertTab;

    @FindBy(xpath = "//button[contains(text(),'Input box')]")
    private WebElement inputBoxButton;

    @FindBy(id = "demo")
    private WebElement helloMessage;

    public AlertPage(WebDriver driver, WebDriverWait waiter) {
        super(driver, waiter);
    }

    @Step("Получение текста приветственного сообщения")
    public String getHelloMessageText() {
        return helloMessage.getText();
    }

    @Step("Открытие владки 'Input Alert'")
    public AlertPage clickInputAlertTab() {
        inputAlertTab.click();

        return this;
    }

    @Step("Нажатие на кнопку input box")
    public AlertPage clickInputBoxButton() {
        waiter.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(demoFrame));
        inputBoxButton.click();

        return this;
    }
}
