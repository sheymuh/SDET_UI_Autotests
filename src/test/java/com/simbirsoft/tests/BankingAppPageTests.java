package com.simbirsoft.tests;

import com.simbirsoft.helpers.ParameterProvider;
import com.simbirsoft.helpers.StringHelper;
import com.simbirsoft.helpers.TransactionsHelper;
import com.simbirsoft.pages.*;
import io.qameta.allure.*;
import org.openqa.selenium.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * BankingAppPageTests.java
 * <p>
 * Тестовый класс для страницы банковского приложения сайта Way2Automation
 * <p>
 * Copyright (c) 2026 Way2Automation. All Rights Reserved.
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 29.05.2026
 */
@Epic("Банковское приложение")
public class BankingAppPageTests extends BaseTest {
    private final String BANKING_APP_PAGE_URL = ParameterProvider.get("base.url") + "angularjs-protractor/banking/#/login";
    private final String CUSTOMER_FNAME = "клиент";
    private final String CUSTOMER_LNAME = "важный";
    private final String CUSTOMER_POST_CODE = "12345";
    private final String DEPOSIT_AMOUNT = "100321";
    private final String ACCOUNT_CURRENCY = "Rupee";

    private BankingAppPage bankingAppPage;
    private BankManagerLoginPage bankManagerLoginPage;
    private CustomerLoginPage customerLoginPage;

    @BeforeMethod()
    public void initPage() {
        bankingAppPage = new BankingAppPage(driver, waiter);
        driver.get(BANKING_APP_PAGE_URL);
    }

    @BeforeMethod(onlyForGroups = {"needLoginCustomer"}, dependsOnMethods = "initPage")
    public void loginCustomer() {
        bankManagerLoginPage = bankingAppPage.clickBankManagerLoginButton();

        bankManagerLoginPage.addCustomer(CUSTOMER_FNAME, CUSTOMER_LNAME, CUSTOMER_POST_CODE);
        driver.switchTo().alert().accept();

        bankManagerLoginPage.openAccount(CUSTOMER_FNAME, CUSTOMER_LNAME, ACCOUNT_CURRENCY);
        driver.switchTo().alert().accept();

        bankingAppPage.clickHomeButton();
        customerLoginPage = bankingAppPage.clickCustomerLoginButton();

        customerLoginPage.login(CUSTOMER_FNAME, CUSTOMER_LNAME);
    }

    @Test(description = "5.1. Проверка появления сообщения при успешной регистрации в форме Sample Form")
    @Feature("Регистрация в Sample Form")
    @Story("Успешная регистрация")
    @Severity(SeverityLevel.NORMAL)
    public void testSampleFormSuccessfulRegisterGetsSuccessMessage() {
        SampleFormPage sampleFormPage = bankingAppPage.clickSampleFormButton();

        List<String> hobbiesTexts = sampleFormPage.getHobbiesTexts();
        String longestHobby = StringHelper.findLongestWord(hobbiesTexts);

        String aboutYourself = "Самое длинное слово из предложенных хобби - " + longestHobby;

        ArrayList<String> hobbies = new ArrayList<>();
        hobbies.add("sports");

        sampleFormPage.register("Успех", "Успешный", "uspeh@gmail.com",
                "1234", hobbies, "Male", aboutYourself);

        Assert.assertTrue(sampleFormPage.getRegisterSuccessMessage().isDisplayed(),
                "Сообщение об успешной авторизации не отображается");
        Assert.assertEquals(sampleFormPage.getRegisterSuccessMessage().getText(),
                SampleFormPage.REGISTER_SUCCESS_TEXT, "Сообщение об успешной авторизации не корректно");
        Assert.assertEquals(longestHobby, SampleFormPage.LONGEST_HOBBY_WORD,
                "Неправильно высчитанно самое длинное слово в хобби");
    }

    @Test(description = "5.2.1. Проверка появления сообщения при успешном добавлении нового покупателя на странице Bank Manager Login")
    @Feature("Аккаунт менеджера банка")
    @Story("Добавление покупателя")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddCustomerWithValidDataGetsSuccessMessage() {
        bankManagerLoginPage = bankingAppPage.clickBankManagerLoginButton();

        bankManagerLoginPage.addCustomer(CUSTOMER_FNAME, CUSTOMER_LNAME, CUSTOMER_POST_CODE);

        Alert alert = driver.switchTo().alert();

        Assert.assertTrue(alert.getText().contains(BankManagerLoginPage.ADD_CUSTOMER_SUCCESS_MESSAGE),
                "Сообщение об успешной авторизации не отображается или не корректно");
    }

    @Test(description = "5.2.2. Проверка появления сообщения при успешном открытии аккаунта покупателя на странице Bank Manager Login")
    @Feature("Аккаунт менеджера банка")
    @Story("Открытие аккаунта покупателя")
    @Severity(SeverityLevel.CRITICAL)
    public void testOpenAccountGetsSuccessMessage() {
        bankManagerLoginPage = bankingAppPage.clickBankManagerLoginButton();

        bankManagerLoginPage.addCustomer(CUSTOMER_FNAME, CUSTOMER_LNAME, CUSTOMER_POST_CODE);
        driver.switchTo().alert().accept();

        bankManagerLoginPage.openAccount(CUSTOMER_FNAME, CUSTOMER_LNAME, ACCOUNT_CURRENCY);

        Alert alert = driver.switchTo().alert();
        Assert.assertTrue(alert.getText().contains(BankManagerLoginPage.OPEN_ACCOUNT_SUCCESS_MESSAGE),
                "Сообщение об успешном открытии аккаунта не отображается или не корректно");
    }

    @Test(groups = {"needLoginCustomer"}, description = "5.3. Проверка входа в созданный аккаунт")
    @Feature("Аккаунт клиента")
    @Story("Авторизация клиента")
    @Severity(SeverityLevel.BLOCKER)
    public void testCustomerLoginGetsWelcomeHeading() {
        String welcomeHeading = "Welcome " + CUSTOMER_FNAME + " " + CUSTOMER_LNAME + " !!";

        Assert.assertEquals(customerLoginPage.getWelcomeHeadingText(), welcomeHeading, "Вход в аккаунт не произошел или приветственный заголовок некорректен");
    }

    @Test(groups = {"needLoginCustomer"}, description = "5.3.1. Проверка успешного пополнения счёта и обновления транзакций")
    @Feature("Аккаунт клиента")
    @Story("Успешное пополнение счёта")
    @Severity(SeverityLevel.CRITICAL)
    public void testSuccessfulDepositWithTransactionsUpdate() {
        customerLoginPage.deposit(DEPOSIT_AMOUNT);
        Assert.assertEquals(customerLoginPage.getTransactionMessageText(), CustomerLoginPage.DEPOSIT_SUCCESS_MESSAGE, "Пополнение счёта не удалось или сообщение некорректно");

        Assert.assertTrue(customerLoginPage.waitingForTransactionUpdateWithRetries(3));

        customerLoginPage.clickTransactionsFormButton();
        Assert.assertTrue(customerLoginPage.findTransactionAmount(DEPOSIT_AMOUNT), "Транзакции не обновились после пополнения счёта");
    }

    @Test(groups = {"needLoginCustomer"}, description = "5.3.2. Проверка неуспешного пополнения счёта и отсутствия изменений в транзакциях")
    @Feature("Аккаунт клиента")
    @Story("Неуспешное пополнение счёта")
    @Severity(SeverityLevel.CRITICAL)
    public void testUnsuccessfulDepositWithNoTransactionsUpdate() {
        String amount = "0";
        customerLoginPage.deposit(amount);
        Assert.assertTrue(customerLoginPage.getTransactionMessageText().isEmpty(), "Счёт не должен пополняться суммой равной нулю");

        customerLoginPage.clickTransactionsFormButton();
        Assert.assertFalse(customerLoginPage.findTransactionAmount(amount), "Не должно быть транзакций с суммой равной нулю");
    }

    @Test(groups = {"needLoginCustomer"}, description = "5.3.3. Проверка успешного снятия средств со счёта и обновления транзакций")
    @Feature("Аккаунт клиента")
    @Story("Успешное снятие средств со счёта")
    @Severity(SeverityLevel.CRITICAL)
    public void testSuccessfulWithdrawWithTransactionsUpdate() {
        customerLoginPage.deposit(DEPOSIT_AMOUNT);

        int balance = customerLoginPage.getBalanceAmount();
        Random random = new Random();
        String amount = String.valueOf(random.nextInt(balance));

        customerLoginPage.withdraw(amount);
        Assert.assertEquals(customerLoginPage.getTransactionMessageText(), CustomerLoginPage.WITHDRAW_SUCCESS_MESSAGE, "Снятие средств со счёта не удалось или сообщение некорректно");

        Assert.assertTrue(customerLoginPage.waitingForTransactionUpdateWithRetries(3));

        customerLoginPage.clickTransactionsFormButton();

        Assert.assertTrue(customerLoginPage.findTransactionAmount(amount), "Транзакции не обновились после снятия средств со счёта");
    }

    @Test(groups = {"needLoginCustomer"}, description = "5.3.4. Проверка неуспешного снятия средств со счёта и отсутствия изменений в транзакциях")
    @Feature("Аккаунт клиента")
    @Story("Неуспешное снятие средств со счёта")
    @Severity(SeverityLevel.CRITICAL)
    public void testUnsuccessfulWithdrawWithNoTransactionsUpdate() {
        customerLoginPage.deposit(DEPOSIT_AMOUNT);
        String amount = "1000000";
        customerLoginPage.withdraw(amount);
        Assert.assertEquals(customerLoginPage.getTransactionMessageText(), CustomerLoginPage.WITHDRAW_FAILURE_MESSAGE, "Нельзя снять со счёта больше, чем есть на балансе или сообщение некорректно");

        customerLoginPage.clickTransactionsFormButton();
        Assert.assertFalse(customerLoginPage.findTransactionAmount(amount), "Транзакции не обновились после снятия средств со счёта");
    }

    @Test(groups = {"needLoginCustomer"}, description = "5.3.5. Проверка равенства баланса, вычисленного из таблицы транзакций, и баланса из страницы аккаунта")
    @Feature("Аккаунт клиента")
    @Story("Просмотр транзакций")
    @Severity(SeverityLevel.CRITICAL)
    public void testCalculateBalanceFromTransaction() {
        customerLoginPage.deposit("2000");
        customerLoginPage.deposit("750");

        customerLoginPage.withdraw("250");

        int balance = customerLoginPage.getBalanceAmount();

        Assert.assertTrue(customerLoginPage.waitingForTransactionUpdateWithRetries(3));

        customerLoginPage.clickTransactionsFormButton();

        int calculatedBalance = TransactionsHelper.calculateBalance(
                customerLoginPage.getTransactionsAmounts(),
                customerLoginPage.getTransactionsTypes()
        );

        Assert.assertEquals(balance, calculatedBalance, "Баланс вычисленный из таблицы транзакций не равен балансу из страницы аккаунта");
    }

    @Test(groups = {"needLoginCustomer"}, description = "5.3.6. Проверка корректного списания всей суммы баланса")
    @Feature("Аккаунт клиента")
    @Story("Успешное снятие средств со счёта")
    @Severity(SeverityLevel.CRITICAL)
    public void testWithdrawAllBalanceWithTransactionsUpdate() {
        customerLoginPage.deposit(DEPOSIT_AMOUNT);

        String balance = String.valueOf(customerLoginPage.getBalanceAmount());

        customerLoginPage.withdraw(balance);

        Assert.assertEquals(customerLoginPage.getTransactionMessageText(), CustomerLoginPage.WITHDRAW_SUCCESS_MESSAGE, "Снятие средств со счёта не удалось или сообщение некорректно");
        Assert.assertEquals(customerLoginPage.getBalanceAmount(), 0, "Баланс после списания всей суммы баланса должен быть равен 0");
    }

    @Test(groups = {"needLoginCustomer"}, description = "5.3.7. Проверка корректной очистки истории транзакций")
    @Feature("Аккаунт клиента")
    @Story("Очистка истории транзакций")
    @Severity(SeverityLevel.CRITICAL)
    public void testResetTransactionsWithTransactionsUpdate() {
        customerLoginPage.deposit(DEPOSIT_AMOUNT);
        customerLoginPage.deposit("500");

        customerLoginPage.clickTransactionsFormButton();

        customerLoginPage.clickResetTransactionsButton();
        Assert.assertEquals(customerLoginPage.getTransactionsCount(), "0", "Таблица транзакций не обновилась после очистки");

        customerLoginPage.clickBackButton();
        Assert.assertEquals(customerLoginPage.getBalanceAmount(), 0, "Баланс после списания всей суммы баланса должен быть равен 0");
    }

    @Test(description = "5.4. Проверка поиска и удаления покупателя на странице Bank Manager Login")
    @Feature("Аккаунт менеджера банка")
    @Story("Удаление покупателя")
    @Severity(SeverityLevel.CRITICAL)
    public void testSearchAndDeleteCustomer() {
        bankManagerLoginPage = bankingAppPage.clickBankManagerLoginButton();

        bankManagerLoginPage.addCustomer(CUSTOMER_FNAME, CUSTOMER_LNAME, CUSTOMER_POST_CODE);
        driver.switchTo().alert().accept();

        bankManagerLoginPage.openAccount(CUSTOMER_FNAME, CUSTOMER_LNAME, ACCOUNT_CURRENCY);
        driver.switchTo().alert().accept();

        bankManagerLoginPage.clickCustomersTableButton();
        int customerIndex = bankManagerLoginPage.findCustomerIndexByFirstName(CUSTOMER_FNAME);
        Assert.assertNotEquals(customerIndex, -1,"Покупатель не был найден");

        bankManagerLoginPage = bankManagerLoginPage.deleteCustomer(customerIndex);
        Assert.assertEquals(bankManagerLoginPage.findCustomerIndexByFirstName(CUSTOMER_FNAME), -1, "Покупатель присутствует в таблице после удаления");
    }
}
