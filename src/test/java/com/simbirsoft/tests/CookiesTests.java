package com.simbirsoft.tests;

import com.simbirsoft.helpers.CookieHelper;
import com.simbirsoft.helpers.ParameterProvider;
import com.simbirsoft.pages.SqlExPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * CookiesTests.java
 * <p>
 * Тестовый класс для страницы с формой авторизации сайта с упражнениями SQL sql-ex
 * <p>
 * Copyright (c) 2026 Way2Automation. All Rights Reserved.
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 09.06.2026
 */
public class CookiesTests extends BaseTest {
    private static final String LOGIN = ParameterProvider.get("sql.login");
    private static final String PASSWORD = ParameterProvider.get("sql.password");
    private static final int COOKIES_MAX_AGE_HOURS = Integer.parseInt(ParameterProvider.get("sql.cookies.max.age.hours"));

    private SqlExPage sqlExPage;

    @BeforeMethod
    public void initPage() {
        sqlExPage = new SqlExPage(driver, waiter);
        driver.get(ParameterProvider.get("sql.base.url"));
    }

    @Test(description = "Проверка авторизации при помощи cookies")
    @Epic("Cookies")
    @Feature("Авторизация")
    @Story("Авторизация через cookies")
    @Severity(SeverityLevel.MINOR)
    public void testLoginWithCookies() {
        boolean loggedInViaCookies = false;

        // Пробуем загрузить свежие куки
        if (CookieHelper.hasSavedCookies() && CookieHelper.isCookiesFileFresh(COOKIES_MAX_AGE_HOURS)) {
            CookieHelper.loadCookiesFromJson(driver);
            driver.navigate().refresh();
            loggedInViaCookies = sqlExPage.isUserLoggedIn();

            if (!loggedInViaCookies) {
                CookieHelper.deleteSavedCookies();
            }
        }

        // Fallback: логинимся через форму, если кук нет или они не сработали
        if (!loggedInViaCookies) {
            sqlExPage.login(LOGIN, PASSWORD);
            Assert.assertTrue(sqlExPage.isUserLoggedIn(),
                    "Авторизация через форму не сработала");
            CookieHelper.saveCookiesInJson(driver);
        }
    }
}
