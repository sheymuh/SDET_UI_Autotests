package com.simbirsoft.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * DroppablePage.java
 * <p>
 * Page Object для страницы с Drag n Drop сайта Way2Automation
 * <p>
 * Copyright (c) 2026 Way2Automation. All Rights Reserved.
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 18.06.2026
 */
public class DroppablePage extends BasePage {
    public static final String DROPPED_MESSAGE = "Dropped!";

    @FindBy(css = "div#example-1-tab-1 iframe.demo-frame")
    private WebElement demoFrame;

    @FindBy(xpath = "//*[@id='draggable']")
    private List<WebElement> draggableElements;

    @FindBy(xpath = "//*[@id='droppable']")
    private List<WebElement> droppableElements;

    public DroppablePage(WebDriver driver, WebDriverWait waiter) {
        super(driver, waiter);
    }

    @Step("Получение draggable элемента")
    public WebElement getDraggableElement() {
        if (draggableElements != null && !draggableElements.isEmpty()) {
            return draggableElements.get(0);
        }
        return null;
    }

    @Step("Получение droppable элемента")
    public WebElement getDroppableElement() {
        if (droppableElements != null && !droppableElements.isEmpty()) {
            return droppableElements.get(0);
        }
        return null;
    }

    @Step("Перетаскивание элемента {draggableElement} в {droppableElement}")
    public DroppablePage dragAndDrop() {
        waiter.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(demoFrame));

        WebElement draggable = getDraggableElement();
        WebElement droppable = getDroppableElement();

        Actions actions = new Actions(driver);

        actions.dragAndDrop(draggable, droppable).perform();

        return this;
    }

    @Step("Получение текста элемента {droppableElement}")
    public String getDroppableElementText() {
        return getDroppableElement().getText();
    }
}
