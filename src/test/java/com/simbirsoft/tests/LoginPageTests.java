package com.simbirsoft.tests;

import com.simbirsoft.helpers.ParameterProvider;
import com.simbirsoft.pages.LoginPage;
import io.qameta.allure.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

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
    private final String SHORT_USERNAME = "oi";
    private final String SHORT_PASSWORD = "23";

    private LoginPage loginPage;

    @BeforeMethod
    public void initPage() {
        loginPage = new LoginPage(driver, waiter);
        driver.get(LOGIN_PAGE_URL);
    }

    @DataProvider(name = "incorrectLogin")
    public Object[][] incorrectLoginData() {
        return new Object[][]{
                { "angular", "1234", USERNAME_DESCRIPTION },
                { "abc123", "password", USERNAME_DESCRIPTION },
                { "abc123", "1234", USERNAME_DESCRIPTION },
        };
    }

    @DataProvider(name = "validation")
    public Object[][] fieldsValidationData() {
        // { username, password, username description, usernameMessage, passwordMessage, isLoginButtonEnabled }
        return new Object[][] {
                {SHORT_USERNAME, SHORT_PASSWORD, USERNAME_DESCRIPTION, LoginPage.USERNAME_SHORT_MESSAGE, LoginPage.PASSWORD_SHORT_MESSAGE, false },
                { "", "", "", LoginPage.USERNAME_EMPTY_MESSAGE, LoginPage.PASSWORD_EMPTY_MESSAGE, false },
                { "!-_. `:", "!-_. `:", USERNAME_DESCRIPTION, null, null, true },
                { "a".repeat(51), "1".repeat(101), USERNAME_DESCRIPTION, null, null, true},
        };
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
    public void testCorrectLoginGetsSuccessMessage() {
        String username = loginPage.getUsernameFromTip();
        String password = loginPage.getPasswordFromTip();

        loginPage.login(username, password, USERNAME_DESCRIPTION);

        Assert.assertTrue(loginPage.getHomeMessage().isDisplayed(),
                "Сообщение об успешной авторизации не отображается");
        Assert.assertEquals(loginPage.getHomeMessage().getText(), LoginPage.LOGIN_SUCCESS_MESSAGE,
                "Сообщение об успешной авторизации некорректное");
    }

    @Test(dataProvider = "incorrectLogin", description = "4.3. Проверка появления сообщения об ошибке при авторизации с некорректными данными")
    @Story("Неуспешная авторизация")
    @Severity(SeverityLevel.NORMAL)
    public void testIncorrectLoginGetsErrorMessage(String username, String password, String usernameDescription) {
        loginPage.login(username, password, usernameDescription);

        Assert.assertTrue(loginPage.getIncorrectLoginDataAlert().isDisplayed(),
                "Сообщение об ошибке при авторизации с некорректными данными не отображается");
        Assert.assertEquals(loginPage.getIncorrectLoginDataAlert().getText(), LoginPage.LOGIN_FAILURE_MESSAGE,
                "Сообщение об ошибке при авторизации некорректное");
    }

    @Test(dataProvider = "validation", description = "Проверка валидации с корректными и некорректными данными")
    @Story("Валидация")
    @Severity(SeverityLevel.CRITICAL)
    public void testValidationIsCorrect(String username, String password, String usernameDescription,
                                        String usernameValMessage, String passwordValMessage,
                                        boolean isLoginButtonEnabled) {
        loginPage.login(username, password, usernameDescription);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(loginPage.getUsernameValidationMessageText(), usernameValMessage, "Валидация поля 'username' не сработала или сообщение некорректно");
        softAssert.assertEquals(loginPage.getPasswordValidationMessageText(), passwordValMessage, "Валидация поля 'password' не сработала или сообщение некорректно");
        softAssert.assertEquals(loginPage.isLoginButtonEnabled(), isLoginButtonEnabled);
        softAssert.assertAll();
    }

    @Test(description = "Проверка, остается ли валидационная подсказка после снятия фокуса с полей username и password")
    @Story("Валидация")
    @Severity(SeverityLevel.MINOR)
    public void testValidationMessageStayAfterBlur() {
        SoftAssert softAssert = new SoftAssert();

        loginPage.enterUsername(SHORT_USERNAME);
        loginPage.blurUsernameField();

        softAssert.assertNotEquals(loginPage.getUsernameValidationMessageText(), null, "Валидационная подсказка пропала после снятия фокуса с поля username");

        loginPage.enterPassword(SHORT_PASSWORD);
        loginPage.blurPasswordField();

        softAssert.assertNotEquals(loginPage.getPasswordValidationMessageText(), null, "Валидационная подсказка пропала после снятия фокуса с поля password");

        softAssert.assertAll();
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
                "Поля формы не отображаются после разлогирования");
        Assert.assertTrue(loginPage.allFieldsAreEmpty(), "Не все поля формы пусты");
    }

    @Test(description = "Проверка навигации назад в браузере после успешной авторизации")
    @Story("Навигация")
    @Severity(SeverityLevel.CRITICAL)
    public void testNavigationBackAfterSuccessfulLogin() {
        String username = loginPage.getUsernameFromTip();
        String password = loginPage.getPasswordFromTip();

        loginPage.login(username, password, USERNAME_DESCRIPTION);
        waiter.until(ExpectedConditions.visibilityOf(loginPage.getHomeMessage()));

        driver.navigate().back();

        Assert.assertTrue(loginPage.allFieldsAreDisplayed(),
                "Поля формы не отображаются после навигации назад");
        Assert.assertTrue(loginPage.allFieldsAreEmpty(), "Не все поля формы пусты");
    }
}
