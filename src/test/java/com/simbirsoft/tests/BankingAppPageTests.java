package com.simbirsoft.tests;

import com.simbirsoft.helpers.ParameterProvider;
import com.simbirsoft.pages.*;
import org.openqa.selenium.Alert;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
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
public class BankingAppPageTests extends BaseTest {
    private final String BANKING_APP_PAGE_URL = ParameterProvider.get("base.url") + "angularjs-protractor/banking/#/login";
    private final String CUSTOMER_FNAME = "клиент";
    private final String CUSTOMER_LNAME = "важный";
    private final String CUSTOMER_POST_CODE = "12345";
    private final String DEPOSIT_AMOUNT = "100321";

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

        bankManagerLoginPage.openAccount(CUSTOMER_FNAME, CUSTOMER_LNAME, "Rupee");
        driver.switchTo().alert().accept();

        bankingAppPage.clickHomeButton();
        customerLoginPage = bankingAppPage.clickCustomerLoginButton();

        customerLoginPage.login(CUSTOMER_FNAME, CUSTOMER_LNAME);
    }

    @Test(description = "5.1. Проверка появления сообщения при успешной регистрации в форме Sample Form")
    public void testSampleFormSuccessfulRegisterGetsSuccessMessage() {
        SampleFormPage sampleFormPage = bankingAppPage.clickSampleFormButton();

        ArrayList<String> hobbies = new ArrayList<>();
        hobbies.add("sports");
        String aboutYourself = "Самое длинное слово из предложенных хобби - " + sampleFormPage.findLongestHobbyWord();

        sampleFormPage.register("Успех", "Успешный", "uspeh@gmail.com",
                "1234", hobbies, "Male", aboutYourself);

        Assert.assertTrue(sampleFormPage.getRegisterSuccessMessage().isDisplayed(),
                "Сообщение об успешной авторизации не отображается");
        Assert.assertEquals(sampleFormPage.getRegisterSuccessMessage().getText(),
                "User registered successfully!", "Сообщение об успешной авторизации не корректно");
        Assert.assertEquals(sampleFormPage.findLongestHobbyWord(), "Traveling",
                "Неправильно высчитанно самое длинное слово в хобби");
    }

    @Test(description = "5.2.1. Проверка появления сообщения при успешном добавлении нового покупателя на странице Bank Manager Login")
    public void testAddCustomerWithValidDataGetsSuccessMessage() {
        bankManagerLoginPage = bankingAppPage.clickBankManagerLoginButton();

        bankManagerLoginPage.addCustomer(CUSTOMER_FNAME, CUSTOMER_LNAME, CUSTOMER_POST_CODE);

        Alert alert = driver.switchTo().alert();

        Assert.assertTrue(alert.getText().contains("Customer added successfully with customer id :"),
                "Сообщение об успешной авторизации не отображается или не корректно");
    }

    @Test(description = "5.2.2. Проверка появления сообщения при успешном открытии аккаунта покупателя на странице Bank Manager Login")
    public void testOpenAccountGetsSuccessMessage() {
        bankManagerLoginPage = bankingAppPage.clickBankManagerLoginButton();

        bankManagerLoginPage.addCustomer(CUSTOMER_FNAME, CUSTOMER_LNAME, CUSTOMER_POST_CODE);
        driver.switchTo().alert().accept();

        bankManagerLoginPage.openAccount(CUSTOMER_FNAME, CUSTOMER_LNAME, "Rupee");

        Alert alert = driver.switchTo().alert();
        Assert.assertTrue(alert.getText().contains("Account created successfully with account Number :"),
                "Сообщение об успешном открытии аккаунта не отображается или не корректно");
    }

    @Test(groups = {"needLoginCustomer"}, description = "5.3. Проверка входа в созданный аккаунт")
    public void testCustomerLoginGetsWelcomeHeading() {
        String welcomeHeading = "Welcome " + CUSTOMER_FNAME + " " + CUSTOMER_LNAME + " !!";

        Assert.assertEquals(customerLoginPage.getWelcomeHeadingText(), welcomeHeading, "Вход в аккаунт не произошел или приветственный заголовок некорректен");
    }

    @Test(groups = {"needLoginCustomer"}, description = "5.3.1. Проверка успешного пополнения счёта и обновления транзакций")
    public void testSuccessfulDepositWithTransactionsUpdate() {
        customerLoginPage.deposit(DEPOSIT_AMOUNT);
        Assert.assertEquals(customerLoginPage.getTransactionMessageText(), "Deposit Successful", "Пополнение счёта не удалось или сообщение некорректно");

        customerLoginPage.clickTransactionsFormButton();
        Assert.assertTrue(customerLoginPage.findTransactionAmount(DEPOSIT_AMOUNT), "Транзакции не обновились после пополнения счёта");
    }

    @Test(groups = {"needLoginCustomer"}, description = "5.3.2. Проверка неуспешного пополнения счёта и отсутствия изменений в транзакциях")
    public void testUnsuccessfulDepositWithNoTransactionsUpdate() {
        String amount = "0";
        customerLoginPage.deposit(amount);
        Assert.assertTrue(customerLoginPage.getTransactionMessageText().isEmpty(), "Счёт не должен пополняться суммой равной нулю");

        customerLoginPage.clickTransactionsFormButton();
        Assert.assertFalse(customerLoginPage.findTransactionAmount(amount), "Не должно быть транзакций с суммой равной нулю");
    }

    @Test(groups = {"needLoginCustomer"}, description = "5.3.3. Проверка успешного снятия средств со счёта и обновления транзакций")
    public void testSuccessfulWithdrawWithTransactionsUpdate() {
        customerLoginPage.deposit(DEPOSIT_AMOUNT);

        int balance = Integer.parseInt(customerLoginPage.getBalanceAmount());
        Random random = new Random();
        String amount = String.valueOf(random.nextInt(balance));

        customerLoginPage.withdraw(amount);
        Assert.assertEquals(customerLoginPage.getTransactionMessageText(), "Transaction successful", "Снятие средств со счёта не удалось или сообщение некорректно");

        customerLoginPage.clickTransactionsFormButton();
        Assert.assertTrue(customerLoginPage.findTransactionAmount(amount), "Транзакции не обновились после снятия средств со счёта");
    }

    @Test(groups = {"needLoginCustomer"}, description = "5.3.4. Проверка неуспешного снятия средств со счёта и отсутствия изменений в транзакциях")
    public void testUnsuccessfulWithdrawWithNoTransactionsUpdate() {
        customerLoginPage.deposit(DEPOSIT_AMOUNT);
        String amount = "1000000";
        customerLoginPage.withdraw(amount);
        Assert.assertEquals(customerLoginPage.getTransactionMessageText(), "Transaction Failed. You can not withdraw amount more than the balance.", "Нельзя снять со счёта больше, чем есть на балансе или сообщение некорректно");

        customerLoginPage.clickTransactionsFormButton();
        Assert.assertFalse(customerLoginPage.findTransactionAmount(amount), "Транзакции не обновились после снятия средств со счёта");
    }

    @Test(groups = {"needLoginCustomer"}, description = "5.3.5. Проверка равенства баланса, вычисленного из таблицы транзакций, и баланса из страницы аккаунта")
    public void testCalculateBalanceFromTransaction() {
        customerLoginPage.deposit("2000");
        customerLoginPage.deposit("750");

        customerLoginPage.withdraw("250");

        int balance = Integer.parseInt(customerLoginPage.getBalanceAmount());

        customerLoginPage.clickTransactionsFormButton();
        int calculatedBalance = customerLoginPage.calculateBalanceFromTransactionsTable();
        Assert.assertEquals(balance, calculatedBalance, "Баланс вычисленный из таблицы транзакций не равен балансу из страницы аккаунта");
    }

    @Test(groups = {"needLoginCustomer"}, description = "5.3.6. Проверка корректного списания всей суммы баланса")
    public void testWithdrawAllBalanceWithTransactionsUpdate() {
        customerLoginPage.deposit(DEPOSIT_AMOUNT);

        String balance = customerLoginPage.getBalanceAmount();

        customerLoginPage.withdraw(balance);

        Assert.assertEquals(customerLoginPage.getTransactionMessageText(), "Transaction successful", "Снятие средств со счёта не удалось или сообщение некорректно");
        Assert.assertEquals(customerLoginPage.getBalanceAmount(), "0", "Баланс после списания всей суммы баланса должен быть равен 0");
    }

    @Test(groups = {"needLoginCustomer"}, description = "5.3.7. Проверка корректной очистки истории транзакций")
    public void testResetTransactionsWithTransactionsUpdate() {
        customerLoginPage.deposit(DEPOSIT_AMOUNT);
        customerLoginPage.deposit("500");

        customerLoginPage.clickTransactionsFormButton();

        customerLoginPage.clickResetTransactionsButton();
        Assert.assertEquals(customerLoginPage.getTransactionsCount(), "0", "Таблица транзакций не обновилась после очистки");

        customerLoginPage.clickBackButton();
        Assert.assertEquals(customerLoginPage.getBalanceAmount(), "0", "Баланс после списания всей суммы баланса должен быть равен 0");
    }

    @Test(description = "5.4. Проверка поиска и удаления покупателя на странице Bank Manager Login")
    public void testSearchAndDeleteCustomer() {
        bankManagerLoginPage = bankingAppPage.clickBankManagerLoginButton();

        bankManagerLoginPage.addCustomer(CUSTOMER_FNAME, CUSTOMER_LNAME, CUSTOMER_POST_CODE);
        driver.switchTo().alert().accept();

        bankManagerLoginPage.openAccount(CUSTOMER_FNAME, CUSTOMER_LNAME, "Rupee");
        driver.switchTo().alert().accept();

        bankManagerLoginPage.clickCustomersTableButton();
        int customerIndex = bankManagerLoginPage.findCustomerIndexByFirstName(CUSTOMER_FNAME);
        Assert.assertNotEquals(customerIndex, -1,"Покупатель не был найден");

        bankManagerLoginPage = bankManagerLoginPage.deleteCustomer(customerIndex);
        Assert.assertEquals(bankManagerLoginPage.findCustomerIndexByFirstName(CUSTOMER_FNAME), -1, "Покупатель присутствует в таблице после удаления");
    }
}
