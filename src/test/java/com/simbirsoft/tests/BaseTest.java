package com.simbirsoft.tests;

import com.simbirsoft.helpers.ParameterProvider;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

/**
 * BaseTest.java
 * <p>
 * Базовый тестовый класс
 * <p>
 * Copyright (c) 2024 Way2Automation
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 28.05.2024
 */
public abstract class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait waiter;

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        waiter = new WebDriverWait(driver,
                Duration.ofSeconds(Long.parseLong(ParameterProvider.get("explicit.wait.time"))));
        driver.get(ParameterProvider.get("base.url"));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
