package com.simbirsoft.tests;

import com.simbirsoft.helpers.ParameterProvider;
import com.simbirsoft.pages.HomePage;
import io.qameta.allure.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * HomePageTests.java
 * <p>
 * Тестовый класс для главной страницы сайта Way2Automation
 * <p>
 * Copyright (c) 2026 Way2Automation. All Rights Reserved.
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 28.05.2026
 */
@Epic("Главная страница")
public class HomePageTests extends BaseTest {
    private HomePage homePage;

    @BeforeMethod
    public void initPage() {
        homePage = new HomePage(driver, waiter);
        driver.get(ParameterProvider.get("base.url"));
    }


    @Test(description = "1.1. Проверка открытия страницы и отображения основных элементов")
    @Feature("Отображение UI компонентов")
    @Story("Открытие страницы")
    @Severity(SeverityLevel.BLOCKER)
    public void testHomePageOpenedAndMainElementsDisplayed() {
        Assert.assertTrue(homePage.isPageOpened(), "Страница не открылась");
        Assert.assertTrue(homePage.areAllMainElementsDisplayed(),
                "Не все основные элементы страницы отображаются");
    }

    @Test(description = "1.2. Проверка хедера на наличие контактной информации")
    @Feature("Отображение UI компонентов")
    @Story("Контакты в хедере")
    @Severity(SeverityLevel.NORMAL)
    public void testHeaderContactInfoDisplayed() {
        Assert.assertTrue(homePage.getHeader().isHeaderDisplayed(), "Хедер не отображается");
        Assert.assertFalse(homePage.getHeader().getIndianPhoneNumbers().isEmpty() &&
                homePage.getHeader().getUsaPhoneNumber().isEmpty(),
                "Телефоны не найдены в хедере");
        Assert.assertFalse(homePage.getHeader().getSkypeId().isEmpty(),
                "Skype ID не найден в хедере");
        Assert.assertFalse(homePage.getHeader().getEmail().isEmpty(),
                "Email не найден в хедере");
        Assert.assertFalse(homePage.getHeader().getSocialLinks().isEmpty(),
                "Ссылки на соцсети не найдены в хедере");
    }

    @Test(description = "1.3. Проверка работы кнопок навигации блока с курсами")
    @Feature("Работа UI компонентов")
    @Story("Карусель курсов")
    @Severity(SeverityLevel.NORMAL)
    public void testCoursesCarouselNavigationButtonsAreWorking() {
        String firstSlideTitle = homePage.getCurrentSlideTitle();
        Assert.assertFalse(firstSlideTitle.isEmpty(),
                "Первый слайд не содержит текста");

        homePage.clickNextButton();
        Assert.assertTrue(homePage.hasSlideChanged(firstSlideTitle),
                "Слайд не изменился после нажатия кнопки 'вперед'");

        homePage.clickPrevButton();
        String backToFirstSlide = homePage.getCurrentSlideTitle();
        Assert.assertEquals(backToFirstSlide, firstSlideTitle,
                "После нажатия 'назад' не вернулись к исходному слайду");
    }

    @Test(description = "1.4. Проверка футера на наличие адреса и контактной информации")
    @Feature("Отображение UI компонентов")
    @Story("Контакты в футере")
    @Severity(SeverityLevel.NORMAL)
    public void testFooterContactInfoDisplayed() {
        waiter.until(ExpectedConditions.visibilityOf(homePage.getFooter().getFooterAboutUsTexts().get(0)));

        Assert.assertTrue(homePage.getFooter().isFooterDisplayed(),
                "Футер не отображается");
        Assert.assertFalse(homePage.getFooter().getAddress().isEmpty(),
                "Адрес не найден в футере");
        Assert.assertFalse(homePage.getFooter().getFooterPhones().isEmpty(),
                "Телефоны не найдены в футере");
        Assert.assertFalse(homePage.getFooter().getFooterEmails().isEmpty(),
                "Emails не найдены в футере");
    }

    @Test(description = "2. Проверка видимости меню после прокрутки вниз")
    @Feature("Отображение UI компонентов")
    @Story("Меню при скролле")
    @Severity(SeverityLevel.MINOR)
    public void testNavMenuIsDisplayedAfterScroll() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", homePage.getFooter().getFooterContainer());

        waiter.until(ExpectedConditions.visibilityOf(homePage.getFooter().getFooterContainer()));

        Assert.assertTrue(homePage.isNavMenuDisplayed());
    }

    @Test(description = "3. Проверка перехода по меню навигации на страницу All courses -> Lifetime membership")
    @Feature("Навигация")
    @Story("Навигация на Lifetime Membership")
    @Severity(SeverityLevel.CRITICAL)
    public void testLifetimeMembershipNavigation() {
        homePage.clickOnCourse();

        Assert.assertEquals(driver.getCurrentUrl(), "https://www.way2automation.com/lifetime-membership-club/");
        Assert.assertEquals(driver.findElement(By.cssSelector("h1.elementor-heading-title")).getText(), HomePage.LIFETIME_MEMBERSHIP_CLUB_HEADING);
    }
}
