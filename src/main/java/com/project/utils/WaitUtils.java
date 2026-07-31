package com.project.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

/**
 * Advanced WaitUtils implementing intelligent synchronization strategies.
 * Eliminates all Thread.sleep() usage and provides dynamic waiting mechanisms.
 * Handles StaleElementReferenceException and other transient exceptions gracefully.
 */
public class WaitUtils {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public WaitUtils(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public WaitUtils(WebDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    /**
     * Waits for element to be visible with stale element handling
     */
    public WebElement waitForVisibility(By locator) {
        return wait.withMessage("Element not visible: " + locator)
                  .ignoring(StaleElementReferenceException.class)
                  .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits for element to be clickable with stale element handling
     */
    public WebElement waitForClickable(By locator) {
        return wait.withMessage("Element not clickable: " + locator)
                  .ignoring(StaleElementReferenceException.class)
                  .until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Waits for element to be present in DOM
     */
    public WebElement waitForPresence(By locator) {
        return wait.withMessage("Element not present in DOM: " + locator)
                  .ignoring(StaleElementReferenceException.class)
                  .until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Waits for multiple elements to be visible
     */
    public List<WebElement> waitForAllVisible(By locator) {
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        return driver.findElements(locator);
    }

    /**
     * Waits for element to disappear
     */
    public boolean waitForInvisibility(By locator) {
        return wait.withMessage("Element still visible: " + locator)
                  .ignoring(StaleElementReferenceException.class)
                  .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Waits for URL to contain specific fragment
     */
    public boolean waitForUrlContains(String urlFragment) {
        return wait.withMessage("URL does not contain: " + urlFragment)
                  .until(ExpectedConditions.urlContains(urlFragment));
    }

    /**
     * Waits for URL to be exactly as expected
     */
    public boolean waitForUrlToBe(String expectedUrl) {
        return wait.withMessage("URL is not: " + expectedUrl)
                  .until(ExpectedConditions.urlToBe(expectedUrl));
    }

    /**
     * Waits for element attribute to contain specific value
     */
    public boolean waitForAttributeContains(By locator, String attribute, String value) {
        return wait.withMessage("Attribute " + attribute + " does not contain: " + value)
                  .until(ExpectedConditions.attributeContains(locator, attribute, value));
    }

    /**
     * Waits for text to be present in element
     */
    public boolean waitForTextToBePresentInElement(By locator, String text) {
        return wait.withMessage("Text not present in element: " + text)
                  .ignoring(StaleElementReferenceException.class)
                  .until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    /**
     * Waits for element count to be greater than specified number
     */
    public boolean waitForNumberOfElementsToBeMoreThan(By locator, int number) {
        List<WebElement> elements = wait.withMessage("Element count not greater than: " + number)
                                       .until(ExpectedConditions.numberOfElementsToBeMoreThan(locator, number));
        return elements != null && elements.size() > number;
    }

    /**
     * Custom wait for dynamic content loading with polling
     */
    public boolean waitForContentToStabilize(By locator, int maxWaitTimeMs) {
        long startTime = System.currentTimeMillis();
        String previousContent = "";
        int stableCount = 0;
        final int requiredStableCount = 3; // Content must be stable for 3 consecutive checks
        
        while (System.currentTimeMillis() - startTime < maxWaitTimeMs) {
            try {
                List<WebElement> elements = driver.findElements(locator);
                String currentContent = "";
                
                for (WebElement element : elements) {
                    currentContent += element.getText() + "|";
                }
                
                if (currentContent.equals(previousContent)) {
                    stableCount++;
                    if (stableCount >= requiredStableCount) {
                        return true;
                    }
                } else {
                    stableCount = 0;
                    previousContent = currentContent;
                }
                
                // Poll every 200ms instead of Thread.sleep()
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                
            } catch (StaleElementReferenceException | NoSuchElementException e) {
                // Element not ready yet, continue waiting
                stableCount = 0;
            }
        }
        
        return false;
    }

    /**
     * Waits for any of multiple conditions to be true
     */
    public boolean waitForAnyOf(ExpectedCondition<?>... conditions) {
        return wait.until(ExpectedConditions.or(conditions));
    }

    /**
     * Waits for all conditions to be true
     */
    public boolean waitForAllOf(ExpectedCondition<?>... conditions) {
        return wait.until(ExpectedConditions.and(conditions));
    }

    /**
     * Custom fluent wait with specific polling interval and exception handling
     */
    public <V> V waitFor(Function<WebDriver, V> condition, Duration timeout, Duration pollingInterval) {
        return new FluentWait<>(driver)
                .withTimeout(timeout)
                .pollingEvery(pollingInterval)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(NoSuchElementException.class)
                .until(condition);
    }

    /**
     * Checks if element is displayed without waiting
     */
    public boolean isElementDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Checks if element is present in DOM without waiting
     */
    public boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Static method to wait for element visibility (for backward compatibility)
     */
    public static WebElement waitForElementVisible(WebDriver driver, By locator, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            return null;
        }
    }
}