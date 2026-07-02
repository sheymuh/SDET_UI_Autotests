package com.simbirsoft.drivers;

import com.simbirsoft.helpers.ParameterProvider;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Step;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * DriverFactory.java
 * <p>
 * Вспомогательный класс для создания WebDriver
 * <p>
 * Copyright (c) 2026 Way2Automation. All Rights Reserved.
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 16.06.2026
 */
public class DriverFactory {
    private static final Logger logger = LoggerFactory.getLogger(DriverFactory.class);

    /**
     * Создание WebDriver на основе браузера, режима выполнения и версии браузера
     */
    @Step("Создание драйвера: браузер={browserType}, режим={mode}")
    public static WebDriver createDriver(BrowserType browserType, ExecutionMode mode) {
        logger.info("Создание драйвера: браузер={}, режим={}", browserType, mode);

        return switch (mode) {
            case LOCAL -> createLocalDriver(browserType);
            case GRID -> createGridDriver(browserType);
            case SELENOID -> createSelenoidDriver(browserType);
        };
    }

    /**
     * Создание WebDriver для локального запуска
     */
    private static WebDriver createLocalDriver(BrowserType browserType) {
        return switch (browserType) {
            case CHROME -> {
                ChromeOptions options = (ChromeOptions) DriverOptionsFactory.createOptions(BrowserType.CHROME);
                yield new ChromeDriver(options);
            }
            case FIREFOX -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions options = (FirefoxOptions) DriverOptionsFactory.createOptions(BrowserType.FIREFOX);
                yield new FirefoxDriver(options);
            }
            case EDGE -> {
                EdgeOptions options = (EdgeOptions) DriverOptionsFactory.createOptions(BrowserType.EDGE);
                yield new EdgeDriver(options);
            }
        };
    }

    /**
     * Создание WebDriver для запуска через Selenium Grid
     */
    private static WebDriver createGridDriver(BrowserType browserType) {
        String gridUrl = ParameterProvider.get("grid.url");
        if (gridUrl == null || gridUrl.isEmpty()) {
            throw new IllegalArgumentException("Grid URL не задан в config.properties");
        }

        try {
            MutableCapabilities options = DriverOptionsFactory.createOptionsForGrid(browserType);
            return new RemoteWebDriver(new URL(gridUrl), options);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Некорректный URL Grid: " + gridUrl, e);
        }
    }

    /**
     * Создание WebDriver для запуска через Selenoid в Docker
     */
    private static WebDriver createSelenoidDriver(BrowserType browserType) {
        String selenoidUrl = ParameterProvider.get("selenoid.url");
        if (selenoidUrl == null || selenoidUrl.isEmpty()) {
            throw new IllegalArgumentException("Selenoid URL не задан в config.properties");
        }

        try {
            DesiredCapabilities options = DriverOptionsFactory.createOptionsForSelenoid(browserType);
            return new RemoteWebDriver(new URL(selenoidUrl), options);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Некорректный URL Selenoid: " + selenoidUrl, e);
        }
    }
}
