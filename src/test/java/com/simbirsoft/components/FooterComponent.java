package com.simbirsoft.components;

import com.simbirsoft.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * FooterComponent.java
 * <p>
 * Компонент футера главной страницы сайта Way2Automation
 * <p>
 * Copyright (c) 2026 Way2Automation. All Rights Reserved.
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 28.05.2026
 */
public class FooterComponent extends BasePage {
    @FindBy(className = "elementor-location-footer")
    private WebElement footerContainer;

    // Адрес, номера телефонов и почты из футера
    @FindBy(css = ".elementor-location-footer .elementor-element-695441a0 .elementor-icon-list-text")
    private List<WebElement> footerAboutUsTexts;

    public FooterComponent(WebDriver driver, WebDriverWait waiter) {
        super(driver, waiter);
    }

    public WebElement getFooterContainer() {
        return footerContainer;
    }

    public boolean isFooterDisplayed() {
        return footerContainer.isDisplayed();
    }

    public List<WebElement> getFooterAboutUsTexts() {
        return footerAboutUsTexts;
    }

    public String getAddress() {
        return footerAboutUsTexts.stream()
                .filter(el -> el.getText().contains("CDR Complex") || el.getText().contains("Sector 15"))
                .findFirst()
                .map(WebElement::getText)
                .orElse("");
    }

    public List<String> getFooterPhones() {
        if (footerAboutUsTexts.isEmpty()) {
            return new ArrayList<>();
        }
        waiter.until(ExpectedConditions.visibilityOf(footerAboutUsTexts.get(0)));
        return footerAboutUsTexts.stream()
                .map(WebElement::getText)
                .filter(text -> text.matches(".*\\+91\\s*\\d{5}-?\\d{2}-?\\d{3}.*"))
                .collect(Collectors.toList());
    }

    public List<String> getFooterEmails() {
        if (footerAboutUsTexts.isEmpty()) {
            return new ArrayList<>();
        }
        return footerAboutUsTexts.stream()
                .map(WebElement::getText)
                .filter(text -> text.contains("@"))
                .collect(Collectors.toList());
    }
}
