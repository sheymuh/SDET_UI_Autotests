package com.simbirsoft.pages;

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

    public CustomerLoginPage login(String fName, String lName) {
        Select select = new Select(nameSelect);
        select.selectByVisibleText(fName + " " + lName);

        loginButton.click();
        return this;
    }

    public String getWelcomeHeadingText() {
        return welcomeHeading.getText();
    }

    public String getTransactionMessageText() {
        return transactionMessage.getText();
    }

    public String getBalanceAmount() {
        return balanceAmount.getText();
    }

    public String getTransactionsCount() {
        return String.valueOf(transactionsAmounts.size());
    }

    public CustomerLoginPage clickTransactionsFormButton() {
        transactionsTableButton.click();
        return this;
    }

    public CustomerLoginPage clickResetTransactionsButton() {
        resetTransactionsButton.click();
        return this;
    }

    public CustomerLoginPage clickBackButton() {
        backButton.click();
        return this;
    }

    public CustomerLoginPage deposit(String amount) {
        waiter.until(ExpectedConditions.elementToBeClickable(depositFormButton));
        depositFormButton.click();

        waiter.until(ExpectedConditions.elementToBeClickable(depositAmountField));
        depositAmountField.sendKeys(amount);

        waiter.until(ExpectedConditions.elementToBeClickable(depositConfirmButton));
        depositConfirmButton.click();

        waitingForTransactionUpdate();

        return this;
    }

    /**
     * Проверка есть ли передаваемая сумма в таблице транзакций
     * @return true - сумма есть, false - суммы нет
     */
    public boolean findTransactionAmount(String amount) {
        return transactionsAmounts.stream()
                .anyMatch(am -> amount.equalsIgnoreCase(am.getText()));
    }

    public CustomerLoginPage withdraw(String amount) {
        waiter.until(ExpectedConditions.elementToBeClickable(withdrawFormButton));
        withdrawFormButton.click();

        waiter.until(ExpectedConditions.elementToBeClickable(withdrawAmountField));
        withdrawAmountField.sendKeys(amount);

        waiter.until(ExpectedConditions.elementToBeClickable(withdrawConfirmButton));
        withdrawConfirmButton.click();

        waitingForTransactionUpdate();

        return this;
    }

    /**
     * Вычисляет баланс клиента по таблице транзакций
     * @return баланс клиента
     */
    public int calculateBalanceFromTransactionsTable() {
        int balance = 0;
        for (int i = 0; i < transactionsAmounts.size(); i++) {
            if (transactionsTypes.get(i).getText().equalsIgnoreCase("Credit")) {
                balance += Integer.parseInt(transactionsAmounts.get(i).getText());
            } else {
                balance -= Integer.parseInt(transactionsAmounts.get(i).getText());
            }
        }

        return balance;
    }

    /**
     * Ожидание обновления скриптов транзакций
     */
    public void waitingForTransactionUpdate() {
        String oldTransactions = (String) ((JavascriptExecutor) driver)
                .executeScript("return localStorage.getItem('Transaction');");

        try {
            // Ожидаем, пока все $http запросы завершатся
            waiter.until(driver -> (Boolean) ((JavascriptExecutor) driver).executeScript(
                    "return angular.element(document.body).injector().get('$http').pendingRequests.length === 0"
            ));

            waiter.until(ExpectedConditions.jsReturnsValue("return localStorage.getItem('Transaction');"));
            waiter.until(ExpectedConditions.jsReturnsValue("var injector = angular.element(document.body).injector();" +
                    "if (!injector) return 0;" +
                    "var Transaction = injector.get('Transaction');" +
                    "var CustomerData = injector.get('CustomerData');" +
                    "var user = CustomerData.getUser();" +
                    "var account = CustomerData.getAccount();" +
                    "if (user && account) {" +
                    "    var txs = Transaction.getTransactions(user.id, account.accountNo);" +
                    "    return txs ? txs.length : 0;" +
                    "}" +
                    "return 0;"));

            // Ожидаем изменения в localStorage
            waiter.until(driver -> {
                String newTransactions = (String) ((JavascriptExecutor) driver)
                        .executeScript("return localStorage.getItem('Transaction');");
                return newTransactions != null && !newTransactions.equals(oldTransactions);
            });

            // Ожидание, пока транзакция появится в сервисе Transaction
            waiter.until(driver -> {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                Long transactionCount = (Long) js.executeScript(
                        "var injector = angular.element(document.body).injector();" +
                                "if (!injector) return 0;" +
                                "var Transaction = injector.get('Transaction');" +
                                "var CustomerData = injector.get('CustomerData');" +
                                "var user = CustomerData.getUser();" +
                                "var account = CustomerData.getAccount();" +
                                "if (user && account) {" +
                                "    var txs = Transaction.getTransactions(user.id, account.accountNo);" +
                                "    return txs ? txs.length : 0;" +
                                "}" +
                                "return 0;"
                );
                return transactionCount != null && transactionCount > 0;
            });
//            Thread.sleep(5000);
        } catch (TimeoutException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
