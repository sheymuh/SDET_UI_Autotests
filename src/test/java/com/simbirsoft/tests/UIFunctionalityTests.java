package com.simbirsoft.tests;

import com.simbirsoft.helpers.ParameterProvider;
import com.simbirsoft.pages.DroppablePage;
import com.simbirsoft.pages.FramesAndWindowsPage;
import io.qameta.allure.*;
import org.testng.Assert;
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
    private final String FRAMES_AND_WINDOWS_PAGE_URL = ParameterProvider.get("base.url") + "way2auto_jquery/frames-and-windows.php";

    private DroppablePage droppablePage;
    private FramesAndWindowsPage framesAndWindowsPage;

    @Test(description = "Проверка изменения текста droppable элемента после перетаскивания")
    @Feature("Drag and Drop")
    @Story("Изменение текста")
    @Severity(SeverityLevel.MINOR)
    public void testDragAndDropChangeElementText() {
        droppablePage = new DroppablePage(driver, waiter);
        driver.get(DROPPABLE_PAGE_URL);

        droppablePage.dragAndDrop();

        Assert.assertEquals(droppablePage.getDroppableElementText(), DroppablePage.DROPPED_MESSAGE, "Текст droppable элемента не изменился");
    }

    @Test(description = "Проверка открытия новых вкладок и переключения между ними")
    @Feature("Новые вкладки")
    @Story("Переключение между вкладками")
    @Severity(SeverityLevel.MINOR)
    public void testOpenNewTabs() {
        framesAndWindowsPage = new FramesAndWindowsPage(driver, waiter);
        driver.get(FRAMES_AND_WINDOWS_PAGE_URL);

        framesAndWindowsPage.waitForDemoFrame();
        framesAndWindowsPage.clickNewTabLink();
        Object[] windowHandles = driver.getWindowHandles().toArray();
        driver.switchTo().window((String) windowHandles[1]);

        framesAndWindowsPage.clickNewTabLink();
        windowHandles = driver.getWindowHandles().toArray();
        Assert.assertEquals(windowHandles.length, 3, "Третья вкладка не открылась");
    }

    @Test(description = "Проверка открытия новых вкладок и переключения между ними")
    @Feature("Новые вкладки")
    @Story("Переключение между вкладками")
    @Severity(SeverityLevel.MINOR)
    public void testOpenNewTabs() {
        framesAndWindowsPage = new FramesAndWindowsPage(driver, waiter);
        driver.get(FRAMES_AND_WINDOWS_PAGE_URL);

        framesAndWindowsPage.waitForDemoFrame();
        framesAndWindowsPage.clickNewTabLink();
        Object[] windowHandles = driver.getWindowHandles().toArray();
        driver.switchTo().window((String) windowHandles[1]);

        framesAndWindowsPage.clickNewTabLink();
        windowHandles = driver.getWindowHandles().toArray();
        Assert.assertEquals(windowHandles.length, 3, "Третья вкладка не открылась");
    }
}
