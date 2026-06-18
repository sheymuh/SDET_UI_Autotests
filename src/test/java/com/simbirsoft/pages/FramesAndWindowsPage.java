package com.simbirsoft.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * FramesAndWindowsPage.java
 * <p>
 * Page Object для страницы работы с вкладками и окнами сайта Way2Automation
 * <p>
 * Copyright (c) 2026 Way2Automation. All Rights Reserved.
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 18.06.2026
 */
public class FramesAndWindowsPage extends BasePage {
    @FindBy(css = "div#example-1-tab-1 iframe.demo-frame")
    private WebElement demoFrame;

    @FindBy(xpath = "//a[contains(text(),'New Browser Tab')]")
    private WebElement newTabLink;

    public FramesAndWindowsPage(WebDriver driver, WebDriverWait waiter) {
        super(driver, waiter);
    }

    public WebDriver switchToNewWindow(WebDriver driver) {
        Object[] windowHandles = driver.getWindowHandles().toArray();
        return driver.switchTo().window((String) windowHandles[windowHandles.length - 1]);
    }

    @Step("Ожидание доступности фрейма demoFrame и фокус на нем")
    public void waitForDemoFrame() {
        waiter.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(demoFrame));
    }

    @Step("Переход по ссылке 'New Tab Link'")
    public FramesAndWindowsPage clickNewTabLink() {
        newTabLink.click();

        return this;
    }
}
