package com.simbirsoft.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * CustomerLoginPage.java
 * <p>
 * Page Object для страницы аккаунта клиента в банковском приложении сайта Way2Automation
 * <p>
 * Copyright (c) 2026 Way2Automation. All Rights Reserved.
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 01.06.2026
 */
public class CustomerLoginPage extends BasePage {
    public static final String DEPOSIT_SUCCESS_MESSAGE = "Deposit Successful";
    public static final String WITHDRAW_SUCCESS_MESSAGE = "Transaction successful";
    public static final String WITHDRAW_FAILURE_MESSAGE = "Transaction Failed. You can not withdraw amount more than the balance.";

    @FindBy(id = "userSelect")
    private WebElement nameSelect;

    @FindBy(xpath = "//*[@ng-show=\"custId != ''\"]")
    private WebElement loginButton;

    @FindBy(xpath = "//strong[contains(text(),'Welcome')]")
    private WebElement welcomeHeading;

    // Кнопка открытия формы пополнения счёта
    @FindBy(xpath = "//*[@ng-click='deposit()']")
    private WebElement depositFormButton;

    // Кнопка открытия формы списания со счёта
    @FindBy(xpath = "//*[@ng-click='withdrawl()']")
    private WebElement withdrawFormButton;

    // Кнопка открытия таблицы транзакций
    @FindBy(xpath = "//*[@ng-click='transactions()']")
    private WebElement transactionsTableButton;

    @FindBy(xpath = "//label[contains(text(),'Deposit')]/following::*[@ng-model='amount']")
    private WebElement depositAmountField;

    @FindBy(xpath = "//label[contains(text(),'Withdraw')]/following::*[@ng-model='amount']")
    private WebElement withdrawAmountField;

    // Кнопка подтверждения пополнения счёта
    @FindBy(xpath = "//*[text() = 'Deposit']")
    private WebElement depositConfirmButton;

    // Кнопка подтверждения списания со счёта
    @FindBy(xpath = "//*[text() = 'Withdraw']")
    private WebElement withdrawConfirmButton;

    // Сообщение о статусе транзакции
    @FindBy(xpath = "//*[@ng-show='message']")
    private WebElement transactionMessage;

    // Столбец сумм транзакций
    @FindBy(css = "tbody td:nth-of-type(2)")
    private List<WebElement> transactionsAmounts;

    // Столбец типов транзакций
    @FindBy(css = "tbody td:nth-of-type(3)")
    private List<WebElement> transactionsTypes;

    // Сумма на балансе
    @FindBy(xpath = "//div[@ng-hide='noAccount'][1]/strong[2]")
    private WebElement balanceAmount;

    @FindBy(xpath = "//*[@ng-click='reset()']")
    private WebElement resetTransactionsButton;

    @FindBy(xpath = "//*[@ng-click='back()']")
    private WebElement backButton;

    public CustomerLoginPage(WebDriver driver, WebDriverWait waiter) {
        super(driver, waiter);
    }

    @Step("Авторизация клиента: {fName} {lName}")
    public CustomerLoginPage login(String fName, String lName) {
        Select select = new Select(nameSelect);
        select.selectByVisibleText(fName + " " + lName);

        loginButton.click();
        return this;
    }

    @Step("Получение текста приветственного заголовка")
    public String getWelcomeHeadingText() {
        return welcomeHeading.getText();
    }

    @Step("Получение текста сообщения о транзакции")
    public String getTransactionMessageText() {
        return transactionMessage.getText();
    }

    @Step("Получение текущего баланса")
    public int getBalanceAmount() {
        return Integer.parseInt(balanceAmount.getText());
    }

    @Step("Получение количества транзакций")
    public String getTransactionsCount() {
        return String.valueOf(transactionsAmounts.size());
    }

    /**
     * Возвращает список сумм транзакций из таблицы
     */
    @Step("Получение списка сумм транзакций из таблицы")
    public List<String> getTransactionsAmounts() {
        return transactionsAmounts.stream()
                .map(WebElement::getText)
                .map(String::strip)
                .toList();
    }

    /**
     * Возвращает список типов транзакций из таблицы
     */
    @Step("Получение списка типов транзакций из таблицы")
    public List<String> getTransactionsTypes() {
        return transactionsTypes.stream()
                .map(WebElement::getText)
                .map(String::strip)
                .toList();
    }

    @Step("Клик по кнопке открытия таблицы транзакций 'Transactions'")
    public CustomerLoginPage clickTransactionsFormButton() {
        transactionsTableButton.click();
        return this;
    }

    @Step("Клик по кнопке 'Reset'")
    public CustomerLoginPage clickResetTransactionsButton() {
        resetTransactionsButton.click();
        return this;
    }

    @Step("Клик по кнопке 'Back'")
    public CustomerLoginPage clickBackButton() {
        backButton.click();
        return this;
    }

    @Step("Выполнение пополнения счёта на сумму: {amount}")
    public CustomerLoginPage deposit(String amount) {
        waiter.until(ExpectedConditions.elementToBeClickable(depositFormButton));
        depositFormButton.click();

        waiter.until(ExpectedConditions.elementToBeClickable(depositAmountField));
        depositAmountField.sendKeys(amount);

        waiter.until(ExpectedConditions.elementToBeClickable(depositConfirmButton));
        depositConfirmButton.click();

        return this;
    }

    /**
     * Проверка есть ли передаваемая сумма в таблице транзакций
     * @return true - сумма есть, false - суммы нет
     */
    @Step("Поиск транзакции на сумму: {amount}")
    public boolean findTransactionAmount(String amount) {
        return transactionsAmounts.stream()
                .anyMatch(am -> amount.equalsIgnoreCase(am.getText()));
    }

    @Step("Выполнение снятия со счёта суммы: {amount}")
    public CustomerLoginPage withdraw(String amount) {
        waiter.until(ExpectedConditions.elementToBeClickable(withdrawFormButton));
        withdrawFormButton.click();

        waiter.until(ExpectedConditions.elementToBeClickable(withdrawAmountField));
        withdrawAmountField.sendKeys(amount);

        waiter.until(ExpectedConditions.elementToBeClickable(withdrawConfirmButton));
        withdrawConfirmButton.click();

        return this;
    }

    /**
     * Ожидание обновления скриптов транзакций после операции
     * @return true если транзакция успешно обновлена, false в случае таймаута
     */
    @Step("Ожидание обновления транзакций")
    public boolean waitingForTransactionUpdate() {
        try {
            // Ожидаем завершения всех HTTP запросов
            waiter.until(driver -> (Boolean) ((JavascriptExecutor) driver).executeScript(
                    "try {" +
                            "    var injector = angular.element(document.body).injector();" +
                            "    return !injector || injector.get('$http').pendingRequests.length === 0;" +
                            "} catch(e) { return true; }"
            ));
        } catch (TimeoutException ex) {
            System.err.println("TimeoutException при ожидании завершения всех HTTP запросов: " + ex.getMessage());
            return false;
        }

        try {
            // Ожидаем появления транзакции в сервисе
            waiter.until(driver -> {
                Long transactionCount = (Long) ((JavascriptExecutor) driver).executeScript(
                        "try {" +
                                "    var injector = angular.element(document.body).injector();" +
                                "    if (!injector) return 0;" +
                                "    var Transaction = injector.get('Transaction');" +
                                "    var CustomerData = injector.get('CustomerData');" +
                                "    var user = CustomerData.getUser();" +
                                "    var account = CustomerData.getAccount();" +
                                "    if (user && account) {" +
                                "        var txs = Transaction.getTransactions(user.id, account.accountNo);" +
                                "        return txs ? txs.length : 0;" +
                                "    }" +
                                "    return 0;" +
                                "} catch(e) { return 0; }"
                );
                return transactionCount != null && transactionCount > 0;
            });
        }
        catch (TimeoutException ex) {
            System.err.println("TimeoutException при ожидании появления транзакции в сервисе: " + ex.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Ожидание обновления скриптов транзакций в несколько попыток
     * @param maxRetries максимальное количество попыток
     * @return true если транзакция успешно обновлена
     */
    @Step("Ожидание обновления транзакций в несколько попыток: {maxRetries}")
    public boolean waitingForTransactionUpdateWithRetries(int maxRetries) {
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            System.out.println("Попытка " + attempt + " из " + maxRetries);

            if (waitingForTransactionUpdate()) {
                System.out.println("Транзакция успешно обновлена после " + attempt + " попытки");
                return true;
            }
        }
        return false;
    }
}
