package com.simbirsoft.helpers;

import io.qameta.allure.Step;

import java.util.List;

/**
 * TransactionsHelper.java
 * <p>
 * Вспомогательный класс для бизнес-логики работы с транзакциями
 * <p>
 * Copyright (c) 2026 Way2Automation. All Rights Reserved.
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 04.06.2026
 */
public final class TransactionsHelper {
    public static final String CREDIT_TYPE = "Credit";

    /**
     * Вычисляет баланс из списков сумм и типов транзакций
     * @param amounts список сумм (строки)
     * @param types список типов (Credit/Debit)
     * @return вычисленный баланс
     */
    @Step("Вычисление баланса из списков сумм: {amounts} и типов транзакций: {types}")
    public static int calculateBalance(List<String> amounts, List<String> types) {
        if (amounts == null || types == null || amounts.size() != types.size()) {
            throw new IllegalArgumentException("Списки сумм и типов должны быть одинакового размера");
        }

        int balance = 0;
        for (int i = 0; i < amounts.size(); i++) {
            int amount = Integer.parseInt(amounts.get(i).trim());
            if (CREDIT_TYPE.equalsIgnoreCase(types.get(i).trim())) {
                balance += amount;
            } else {
                balance -= amount;
            }
        }
        return balance;
    }
}
