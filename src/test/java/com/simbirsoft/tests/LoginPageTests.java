package com.simbirsoft.tests;

import com.simbirsoft.helpers.ParameterProvider;
import com.simbirsoft.pages.LoginPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * LoginPageTests.java
 * <p>
 * Тестовый класс для страницы авторизации сайта Way2Automation
 * <p>
 * Copyright (c) 2026 Way2Automation. All Rights Reserved.
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 28.05.2026
 */
@Epic("Авторизация")
@Feature("Форма логина")
public class LoginPageTests extends BaseTest {
    private final String LOGIN_PAGE_URL = ParameterProvider.get("base.url") + "angularjs-protractor/registeration/#/login";
    private final String USERNAME_DESCRIPTION = "some description";

    private LoginPage loginPage;

    @BeforeMethod
    public void initPage() {
        loginPage = new LoginPage(driver, waiter);
        driver.get(LOGIN_PAGE_URL);
    }

    @Test(description = "4.1. Проверка на отображение полей и недоступность кнопки при незаполненных полях")
    @Story("Отображение формы")
    @Severity(SeverityLevel.BLOCKER)
    public void testIsFieldsDisplayedAndLoginButtonIsDisabled() {
        Assert.assertTrue(loginPage.allFieldsAreDisplayed(), "Не все поля отображаются");

        Assert.assertFalse(loginPage.isAllFieldsFilled());
        Assert.assertFalse(loginPage.isLoginButtonEnabled(), "Кнопка 'Login' доступна при незаполненных полях");
    }

    @Test(description = "4.2. Проверка появления сообщения при успешной авторизации")
    @Story("Успешная авторизация")
    @Severity(SeverityLevel.MINOR)
    public void testValidLoginGetsSuccessMessage() {
        String username = loginPage.getUsernameFromTip();
        String password = loginPage.getPasswordFromTip();

        loginPage.login(username, password, USERNAME_DESCRIPTION);

        Assert.assertTrue(loginPage.getHomeMessage().isDisplayed(),
                "Сообщение об успешной авторизации не отображается");
        Assert.assertEquals(loginPage.getHomeMessage().getText(), LoginPage.LOGIN_SUCCESS_MESSAGE,
                "Сообщение об успешной авторизации некорректное");
    }

    @Test(description = "4.3. Проверка появления сообщения об ошибке при авторизации с некорректными данными")
    @Story("Неуспешная авторизация")
    @Severity(SeverityLevel.NORMAL)
    public void testIncorrectLoginGetsErrorMessage() {
        loginPage.login("user", "123", "polzovatel'");

        Assert.assertTrue(loginPage.getIncorrectLoginDataAlert().isDisplayed(),
                "Сообщение об ошибке при авторизации с некорректными данными не отображается");
        Assert.assertEquals(loginPage.getIncorrectLoginDataAlert().getText(), LoginPage.LOGIN_FAILURE_MESSAGE,
                "Сообщение об ошибке при авторизации некорректное");
    }

    @Test(description = "4.4. Проверка успешного разлогирования")
    @Story("Разлогирование")
    @Severity(SeverityLevel.CRITICAL)
    public void testLogoutSucceed() {
        String username = loginPage.getUsernameFromTip();
        String password = loginPage.getPasswordFromTip();

        loginPage.login(username, password, USERNAME_DESCRIPTION);

        loginPage.logout();
        Assert.assertTrue(loginPage.allFieldsAreDisplayed(),
                "Поля для входа не отображаются после разлогирования");
    }
}
