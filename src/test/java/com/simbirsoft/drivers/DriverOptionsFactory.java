package com.simbirsoft.drivers;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * DriverOptionsFactory.java
 * <p>
 * Вспомогательный класс для создания опций для браузеров
 * <p>
 * Copyright (c) 2026 Way2Automation. All Rights Reserved.
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 16.06.2026
 */
public class DriverOptionsFactory {
    public static MutableCapabilities createOptions(BrowserType browserType) {
        return switch (browserType) {
            case CHROME -> createChromeOptions();
            case FIREFOX -> createFirefoxOptions();
            case EDGE -> createEdgeOptions();
        };
    }

    private static ChromeOptions createChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--headless=new");

        // Настройки для отключения сохранения паролей
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        options.setExperimentalOption("prefs", prefs);

        return options;
    }

    private static FirefoxOptions createFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        options.addPreference("dom.webnotifications.enabled", false);
        options.addPreference("dom.push.enabled", false);
        options.addPreference("signon.rememberSignons", false);
        options.addPreference("network.http.use-cache", false);
        options.addArguments("--headless");
        return options;
    }

    private static EdgeOptions createEdgeOptions() {
        EdgeOptions options = new EdgeOptions();
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--headless=new");

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        options.setExperimentalOption("prefs", prefs);

        return options;
    }

    public static MutableCapabilities createOptionsForGrid(BrowserType browserType) {
        MutableCapabilities options = createOptions(browserType);
        options.setCapability("platformName", "Windows");
        return options;
    }
}
