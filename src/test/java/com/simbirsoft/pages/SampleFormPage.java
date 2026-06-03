package com.simbirsoft.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * SampleFormPage.java
 * <p>
 * Page Object для страницы регистрации в банковском приложении сайта Way2Automation
 * <p>
 * Copyright (c) 2026 Way2Automation. All Rights Reserved.
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 29.05.2026
 */
public class SampleFormPage extends BasePage {
    @FindBy(id = "firstName")
    private WebElement firstNameField;

    @FindBy(id = "lastName")
    private WebElement lastNameField;

    @FindBy(id = "email")
    private WebElement emailField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(css = ".checkbox-group label")
    private List<WebElement> hobbiesLabels;

    @FindBy(id = "gender")
    private WebElement genderSelect;

    @FindBy(id = "about")
    private WebElement aboutYourselfField;

    @FindBy(tagName = "button")
    private WebElement registerButton;

    @FindBy(id = "successMessage")
    private WebElement registerSuccessMessage;

    public SampleFormPage(WebDriver driver, WebDriverWait waiter) {
        super(driver, waiter);
    }

    public WebElement getRegisterSuccessMessage() {
        return registerSuccessMessage;
    }

    public SampleFormPage selectHobby(String hobbyName) {
        for (WebElement hobbyLabel : hobbiesLabels) {
            if (hobbyLabel.getText().strip().equalsIgnoreCase(hobbyName)) {
                if (!hobbyLabel.isSelected()) {
                    hobbyLabel.click();
                }
                return this;
            }
        }
        throw new IllegalArgumentException("Хобби не найдено: " + hobbyName);
    }

    public SampleFormPage selectGenderByVisibleText(String genderVisibleText) {
        Select select = new Select(genderSelect);
        select.selectByVisibleText(genderVisibleText);
        return this;
    }

    /**
     * Находит самое длинное слово из hobbiesCheckboxes
     */
    public String findLongestHobbyWord() {
        String longestWord = "";

        for (WebElement checkbox : hobbiesLabels) {
            String word = checkbox.getText().strip();
            if (word.length() > longestWord.length()) {
                longestWord = word;
            }
        }

        return longestWord;
    }

    public SampleFormPage register(String firstName, String lastName, String email, String password,
                                   List<String> hobbies, String gender, String aboutYourself) {
        firstNameField.sendKeys(firstName);
        lastNameField.sendKeys(lastName);
        emailField.sendKeys(email);
        passwordField.sendKeys(password);

        hobbies.forEach(this::selectHobby);

        selectGenderByVisibleText(gender);

        aboutYourselfField.sendKeys(aboutYourself);

        registerButton.click();
        return this;
    }
}
