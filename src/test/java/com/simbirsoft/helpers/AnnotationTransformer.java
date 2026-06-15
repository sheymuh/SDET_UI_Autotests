package com.simbirsoft.helpers;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

/**
 * AnnotationTransformer.java
 * <p>
 * Вспомогательный класс для добавления аннотаций к тестовым методам во время выполнения
 * <p>
 * Copyright (c) 2026 Way2Automation. All Rights Reserved.
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 15.06.2026
 */
public class AnnotationTransformer implements IAnnotationTransformer {
    @Override
    public void transform(ITestAnnotation annotation,
                          Class testClass,
                          Constructor testConstructor,
                          Method testMethod) {
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
}
