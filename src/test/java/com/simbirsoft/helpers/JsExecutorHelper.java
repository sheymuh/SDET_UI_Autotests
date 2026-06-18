package com.simbirsoft.helpers;

import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * JsExecutorHelper.java
 * <p>
 * Вспомогательный класс для работы с JavascriptExecutor
 * <p>
 * Copyright (c) 2026 Way2Automation. All Rights Reserved.
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 11.06.2026
 */
public final class JsExecutorHelper {
    /**
     * Скроллит до элемента
     * @param driver драйвер сессии браузера
     * @param element элемент, до которого скроллит
     */
    @Step("Скролл до элемента: {element}")
    public static void scrollIntoElementView(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Убирает фокус из элемента
     * @param driver драйвер сессии браузера
     * @param element элемент, с которого убирается фокус
     */
    @Step("Убран фокус из элемента: {element}")
    public static void blurElement(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].blur();", element);
    }

    /**
     * Проверяет наличие скролла(горизонтального или вертикального) на странице
     * @param driver драйвер сессии браузера
     * @return true - скролл есть, false - скролла нет
     */
    @Step("Проверка наличия скролла на странице")
    public static boolean havePageScroll(WebDriver driver) {
        Boolean horizontalPresent = (Boolean) ((JavascriptExecutor) driver).executeScript("return document.documentElement.scrollWidth > document.documentElement.clientWidth;");
        Boolean verticalPresent = (Boolean) ((JavascriptExecutor) driver).executeScript("return document.documentElement.scrollHeight > document.documentElement.clientHeight;");

        return Boolean.TRUE.equals(horizontalPresent) || Boolean.TRUE.equals(verticalPresent);
    }
}
