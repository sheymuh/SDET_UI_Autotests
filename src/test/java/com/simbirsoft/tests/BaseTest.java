package com.simbirsoft.tests;

import com.simbirsoft.helpers.ParameterProvider;
import com.simbirsoft.helpers.ScreenshotHelper;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * BaseTest.java
 * <p>
 * Базовый тестовый класс
 * <p>
 * Copyright (c) 2026 Way2Automation. All Rights Reserved.
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 28.05.2026
 */
public abstract class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait waiter;

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.addArguments("--disable-blink-features=AutomationControlled");

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        options.setExperimentalOption("prefs", prefs);

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        waiter = new WebDriverWait(driver,
                Duration.ofSeconds(Long.parseLong(ParameterProvider.get("explicit.wait.time"))));
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (driver != null) {
            if (result.getStatus() == ITestResult.FAILURE) {
                takeScreenshot();
            }
            driver.quit();
        }
    }

    @Step("Выполняется скриншот страницы")
    private void takeScreenshot() {
        byte[] screenshot = ScreenshotHelper.takeAShotScreenshot(driver);
        if (screenshot.length == 0) {
            ScreenshotHelper.takeScreenshot(driver);
        }
    }
}
