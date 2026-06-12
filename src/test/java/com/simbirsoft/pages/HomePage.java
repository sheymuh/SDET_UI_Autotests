package com.simbirsoft.pages;

import com.simbirsoft.components.FooterComponent;
import com.simbirsoft.components.HeaderComponent;
import com.simbirsoft.helpers.JsExecutorHelper;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Objects;

/**
 * HomePage.java
 * <p>
 * Page Object для главной страницы сайта Way2Automation
 * <p>
 * Copyright (c) 2026 Way2Automation. All Rights Reserved.
 * <p>
 * Author: Sheimukhov Andrei
 * <p>
 * Date: 28.05.2026
 */
public class HomePage extends BasePage {
    public static final String LIFETIME_MEMBERSHIP_CLUB_HEADING = "LIFETIME MEMBERSHIP CLUB";

    private HeaderComponent header;

    private FooterComponent footer;

    // Разделы меню навигации
    @FindBy(css = "ul#ast-hf-menu-1")
    private List<WebElement> navigationMenuItems;

    // Раздел "AllCourses" меню навигации
    @FindBy(xpath = "//li[@id='menu-item-27580']/a[@href='#']")
    private WebElement allCoursesNavMenuItem;

    // Раздел "Lifetime Membership" из "AllCourses"
    @FindBy(id = "menu-item-27581")
    private WebElement lifetimeMembershipCourse;

    // Блок с курсами (Most Popular Software Testing Courses)
    @FindBy(className = "pp-info-box-container")
    private WebElement coursesCarousel;

    @FindBy(className = "swiper-button-prev-c50f9f0")
    private WebElement carouselPrevButton;

    @FindBy(className = "swiper-button-next-c50f9f0")
    private WebElement carouselNextButton;

    @FindBy(css = ".pp-info-box-carousel .swiper-slide")
    private List<WebElement> carouselSlides;

    public HomePage(WebDriver driver, WebDriverWait waiter) {
        super(driver, waiter);
        this.header = new HeaderComponent(driver, waiter);
        this.footer = new FooterComponent(driver, waiter);
    }

    @Step("Проверка, что открыта главная страница")
    public boolean isPageOpened() {
        return Objects.equals(driver.getCurrentUrl(), "https://www.way2automation.com/");
    }

    /**
     * Проверяет отображение всех основных элементов страницы.
     */
    @Step("Проверка отображения всех основных элементов страницы")
    public boolean areAllMainElementsDisplayed() {
        boolean isHeaderDisplayed = header.isHeaderDisplayed();

        boolean isNavDisplayed = navigationMenuItems.get(0).isDisplayed();

        waiter.until(ExpectedConditions.visibilityOf(coursesCarousel));
        boolean isCourseBlockDisplayed = coursesCarousel.isDisplayed();

        boolean isFooterDisplayed = footer.isFooterDisplayed();

        return isHeaderDisplayed && isNavDisplayed && isCourseBlockDisplayed && isFooterDisplayed;
    }

    @Step("Получение компонента хедера")
    public HeaderComponent getHeader() {
        return header;
    }

    @Step("Получение компонента футера")
    public FooterComponent getFooter() {
        return footer;
    }

    @Step("Клик по меню 'All Courses' -> 'Lifetime Membership'")
    public HomePage clickOnCourse() {
        allCoursesNavMenuItem.click();
        waiter.until(ExpectedConditions.elementToBeClickable(lifetimeMembershipCourse));
        lifetimeMembershipCourse.click();
        return this;
    }

    @Step("Проверка отображения меню навигации")
    public boolean isNavMenuDisplayed() {
        return !navigationMenuItems.isEmpty() &&
                navigationMenuItems.get(0).isDisplayed();

    }

    @Step("Клик по кнопке '>' (вперёд) в карусели")
    public HomePage clickNextButton() {
        waiter.until(ExpectedConditions.elementToBeClickable(carouselNextButton));
        carouselNextButton.click();
        return this;
    }

    @Step("Клик по кнопке '<' (назад) в карусели")
    public HomePage clickPrevButton() {
        waiter.until(ExpectedConditions.elementToBeClickable(carouselPrevButton));
        carouselPrevButton.click();
        return this;
    }

    /**
     * Получает видимый (текущий отображаемый) слайд.
     */
    @Step("Получение текущего видимого слайда карусели")
    public WebElement getCurrentlyVisibleSlide() {
        waiter.until(ExpectedConditions.visibilityOf(coursesCarousel));

        // Ищем слайд, который виден в контейнере
        return carouselSlides.stream()
                .filter(slide -> slide.isDisplayed())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Нет видимых слайдов в карусели"));
    }

    /**
     * Возвращает заголовок текущего видимого слайда.
     */
    @Step("Получение заголовка текущего слайда")
    public String getCurrentSlideTitle() {
        WebElement visibleSlide = getCurrentlyVisibleSlide();

        WebElement titleElement = visibleSlide.findElement(By.cssSelector(".pp-info-box-title"));
        return titleElement.getText().trim();
    }

    /**
     * Проверяет, изменился ли видимый слайд после нажатия.
     */
    @Step("Проверка изменения видимого слайда после нажатия")
    public boolean hasSlideChanged(String previousSlideTitle) {
        String newSlideTitle = getCurrentSlideTitle();
        return !newSlideTitle.equals(previousSlideTitle);
    }


    public boolean havePageScroll() {
        return JsExecutorHelper.havePageScroll(driver);
    }
}
