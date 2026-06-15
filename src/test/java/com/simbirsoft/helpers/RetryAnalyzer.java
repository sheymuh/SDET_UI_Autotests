package com.simbirsoft.helpers;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * RetryAnalyzer.java
 * <p>
 * Вспомогательный класс для перезапуска упавших тестов
 * <p>
 * Copyright (c) 2026 Way2Automation. All Rights Reserved.
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 15.06.2026
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    int counter = 0;
    int retryLimit = 2;

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (counter < retryLimit)
        {
            counter++;
            return true;
        }

        return false;
    }
}
