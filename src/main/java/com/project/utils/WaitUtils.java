package com.project.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtils {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final int timeoutSeconds;

    public WaitUtils(WebDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.timeoutSeconds = timeoutSeconds;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    public WebElement untilVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void untilUrlContains(String fraction) {
        wait.until(ExpectedConditions.urlContains(fraction));
    }

    /**
     * Waits until the page title contains the specified fragment.
     *
     * @param titleFragment text expected in the page title
     * @return {@code true} if the title contains the fragment within the timeout
     */
    public boolean untilTitleContains(String titleFragment) {
        return wait.until(ExpectedConditions.titleContains(titleFragment));
    }

    /**
     * Waits for an element using FluentWait with tolerance for
     * {@link StaleElementReferenceException} and {@link NoSuchElementException}.
     *
     * @param locator the By locator for the target element
     * @return the located WebElement
     */
    public WebElement untilPresenceOfElement(By locator) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutSeconds))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(StaleElementReferenceException.class)
                .ignoring(NoSuchElementException.class);
        return fluentWait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Waits until an element is clickable.
     *
     * @param locator the By locator for the target element
     * @return the clickable WebElement
     */
    public WebElement untilClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
}
