package com.simbirsoft.tests;

import com.simbirsoft.helpers.ParameterProvider;
import com.simbirsoft.pages.DroppablePage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * UIFunctionalityTests.java
 * <p>
 * Тестовый класс для проверки функциональности UI компонентов
 * <p>
 * Copyright (c) 2026 Way2Automation. All Rights Reserved.
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 18.06.2026
 */
@Epic("Работа UI компонентов")
public class UIFunctionalityTests extends BaseTest {
    private final String DROPPABLE_PAGE_URL = ParameterProvider.get("base.url") + "way2auto_jquery/droppable.php";

    private DroppablePage droppablePage;

    @BeforeMethod
    public void initPage() {
        droppablePage = new DroppablePage(driver, waiter);
        driver.get(DROPPABLE_PAGE_URL);
    }

    @Test(description = "Проверка изменения текста droppable элемента после перетаскивания")
    @Feature("Drag and Drop")
    @Story("Изменение текста")
    @Severity(SeverityLevel.MINOR)
    public void testDragAndDropChangeElementText() {
        droppablePage.dragAndDrop();

        Assert.assertEquals(droppablePage.getDroppableElementText(), DroppablePage.DROPPED_MESSAGE, "Текст droppable элемента не изменился");
    }
}
