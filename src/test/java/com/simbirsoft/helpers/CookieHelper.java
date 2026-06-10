package com.simbirsoft.helpers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.simbirsoft.helpers.dto.CookieDto;
import io.qameta.allure.Step;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * CookieHelper.java
 * <p>
 * Вспомогательный класс для работы с Cookies
 * <p>
 * Copyright (c) 2026 Way2Automation. All Rights Reserved.
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 09.06.2026
 */
public class CookieHelper {
    private static final String COOKIES_FILE_PATH = "src/test/resources/cookies.json";
    private static final ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    /**
     * Сохраняет cookies из текущей сессии в JSON-файл
     */
    @Step("Сохранение cookies из текущей сессии в JSON файл")
    public static void saveCookiesInJson(WebDriver driver) {
        Set<Cookie> cookies = driver.manage().getCookies();
        Set<CookieDto> dtos = cookies.stream()
                .map(CookieDto::fromSeleniumCookie)
                .collect(Collectors.toSet());

        try {
            mapper.writeValue(new File(COOKIES_FILE_PATH), dtos);
        } catch (IOException ex) {
            throw new RuntimeException("Не удалось сохранить куки в файл", ex);
        }
    }

    /**
     * Загружает cookies из JSON-файла в текущую сессию браузера
     */
    @Step("Загрузка cookies из JSON-файла в текущую сессию браузера")
    public static void loadCookiesFromJson(WebDriver driver) {
        File file = new File(COOKIES_FILE_PATH);

        try {
            Set<CookieDto> dtos = mapper.readValue(file, new TypeReference<>() {});

            driver.manage().deleteAllCookies();

            for (CookieDto dto : dtos) {
                Cookie cookie = dto.toSeleniumCookie();

                try {
                    driver.manage().addCookie(cookie);
                } catch (Exception e) {
                    System.err.println("Не удалось добавить " + dto.getName() + ": " + e.getMessage());
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("Не удалось загрузить куки из файла", ex);
        }
    }

    /**
     * Проверяет, есть ли сохранённые cookies
     */
    @Step("Проверка, есть ли сохранённые cookies")
    public static boolean hasSavedCookies() {
        return new File(COOKIES_FILE_PATH).exists();
    }

    /**
     * Удаляем сохранённые cookies
     */
    @Step("Удаление сохранённых cookies")
    public static boolean deleteSavedCookies() {
        return new File(COOKIES_FILE_PATH).delete();
    }

    /**
     * Проверяет, что файл кук не старше указанного количества часов
     */
    @Step("Проверка, не старше ли {maxAgeHours} часов файл с cookies")
    public static boolean isCookiesFileFresh(int maxAgeHours) {
        File file = new File(COOKIES_FILE_PATH);
        if (!file.exists()) return false;

        long ageMs = System.currentTimeMillis() - file.lastModified();
        long maxAgeMs = maxAgeHours * 60L * 60L * 1000L;
        return ageMs < maxAgeMs;
    }
}
