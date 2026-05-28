package com.simbirsoft.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * BasePage.java
 * <p>
 * Базовый класс Page Object
 * <p>
 * Copyright (c) 2024 Way2Automation
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 28.05.2024
 */
public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait waiter;

    public BasePage(WebDriver driver, WebDriverWait waiter) {
        this.driver = driver;
        this.waiter = waiter;
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 10), this);
    }
}
