package com.simbirsoft.components;

import com.simbirsoft.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

/**
 * HeaderComponent.java
 * <p>
 * Компонент хедера главной страницы сайта Way2Automation
 * <p>
 * Copyright (c) 2026 Way2Automation. All Rights Reserved.
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 28.05.2026
 */
public class HeaderComponent extends BasePage {
    // Блок всех контактов в верхней панели
    @FindBy(css = ".ast-above-header-bar .elementor-icon-list-item a")
    private List<WebElement> contactLinks;

    // Иконки соцсетей
    @FindBy(css = ".ast-header-social-1-wrap .ast-builder-social-element")
    private List<WebElement> socialIcons;

    // Телефоны Индия
    @FindBy(css = "a[href*='wa.me']")
    private List<WebElement> indianPhoneNums;

    // Телефон США
    @FindBy(css = ".elementor-inline-item a[href*='tel:']")
    private WebElement usaPhoneNum;

    @FindBy(css = "a[href*='skype:']")
    private WebElement skypeLink;

    @FindBy(xpath = "//div[@id='ast-desktop-header']//span[contains(text(),'trainer@way2automation.com')]")
    private WebElement emailLink;

    public HeaderComponent(WebDriver driver, WebDriverWait waiter) {
        super(driver, waiter);
    }

    @Step("Проверка отображения хедера")
    public boolean isHeaderDisplayed() {
        return !contactLinks.isEmpty() && !socialIcons.isEmpty() &&
                contactLinks.get(0).isDisplayed() &&
                socialIcons.get(0).isDisplayed();
    }

    @Step("Получение Skype ID из хедера")
    public String getSkypeId() {
        return skypeLink.getText().trim();
    }

    @Step("Получение Email из хедера")
    public String getEmail() {
        return emailLink.getText().trim();
    }

    @Step("Получение телефона США из хедера")
    public String getUsaPhoneNumber() {
        return usaPhoneNum.getText().trim();
    }

    @Step("Получение индийских телефонов из хедера")
    public List<String> getIndianPhoneNumbers() {
        return indianPhoneNums.stream()
                .map(el -> el.getText().trim())
                .filter(text -> !text.isEmpty())
                .collect(Collectors.toList());
    }

    @Step("Получение ссылок на соцсети из хедера")
    public List<String> getSocialLinks() {
        return socialIcons.stream()
                .map(el -> el
                        .getAttribute("href"))
                .collect(Collectors.toList());
    }
}
