package com.simbirsoft.helpers;

import io.qameta.allure.Attachment;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;

import static ru.yandex.qatools.ashot.util.ImageTool.toByteArray;

public final class ScreenshotHelper {
    /**
     * Скриншот страницы через aShot
     */
    @Attachment(value = "aShot screenshot", type = "image/png")
    public static byte[] takeAShotScreenshot(WebDriver driver) {
        try {
            Screenshot screenshot = new AShot()
                    .takeScreenshot(driver);
            return toByteArray(screenshot.getImage());
        } catch (Exception ex) {
            attachScreenshotError("Failed to take aShot screenshot: " + ex.getMessage());
            return new byte[0];
        }
    }

    /**
     * Базовый скриншот через Selenium
     */
    @Attachment(value = "Screenshot", type = "image/png")
    public static byte[] takeScreenshot(WebDriver driver) {
        try {
            return ((org.openqa.selenium.TakesScreenshot) driver)
                    .getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
        } catch (Exception e) {
            attachScreenshotError("Failed to take screenshot: " + e.getMessage());
            return new byte[0];
        }
    }

    /**
     * Фиксация ошибки скриншота в Allure-отчёте
     */
    @Attachment(value = "Screenshot Error", type = "text/plain")
    public static String attachScreenshotError(String errorMessage) {
        return errorMessage;
    }
}
