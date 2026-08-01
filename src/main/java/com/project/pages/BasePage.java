package com.project.pages;

import com.project.drivers.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.project.utils.ReportLogger;

import java.time.Duration;
import java.util.List;

/**
 * Abstract BasePage encapsulating standard WebDriver and WebDriverWait interactions.
 * Hides raw Selenium mechanisms behind clean helper methods.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;
    private static final int DEFAULT_TIMEOUT_SECONDS = 10;

    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
    }

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
    }

    protected WebElement waitForVisibility(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            ReportLogger.log("Element not visible: " + locator + " - " + e.getMessage());
            throw e;
        }
    }

    protected List<WebElement> waitForAllVisible(By locator) {
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        return driver.findElements(locator);
    }

    protected void click(By locator) {
        waitForVisibility(locator).click();
    }

    protected void type(By locator, String text) {
        WebElement element = waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        return waitForVisibility(locator).getText();
    }

    protected boolean isElementDisplayed(By locator) {
        try {
            return waitForVisibility(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * More flexible element checking that tries multiple selectors
     */
    protected boolean isElementDisplayedWithFallback(By... locators) {
        for (By locator : locators) {
            try {
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                if (element != null && element.isDisplayed()) {
                    return true;
                }
            } catch (Exception e) {
                // Continue to next selector
                continue;
            }
        }
        return false;
    }
    
    /**
     * More flexible element finding that tries multiple selectors
     */
    protected WebElement findElementWithFallback(By... locators) {
        for (By locator : locators) {
            try {
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                if (element != null && element.isDisplayed()) {
                    return element;
                }
            } catch (Exception e) {
                // Continue to next selector
                continue;
            }
        }
        throw new RuntimeException("Failed to find element with any of the provided selectors");
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    /**
     * Checks if the page loaded properly (not data:, about:blank, or error pages)
     */
    public boolean isPageLoadedProperly() {
        String currentUrl = driver.getCurrentUrl();
        return !currentUrl.equals("data:") && 
               !currentUrl.equals("about:blank") && 
               !currentUrl.contains("chrome-error://") &&
               !currentUrl.contains("error:") &&
               !isErrorPage();
    }
    
    /**
     * Checks if the current page is showing an error message
     */
    public boolean isErrorPage() {
        try {
            String pageTitle = driver.getTitle();
            String bodyText = driver.findElement(By.tagName("body")).getText().toLowerCase();
            
            // Check for common Spanish error indicators
            return pageTitle.contains("uci.cu") && 
                   (bodyText.contains("no se puede acceder a este sitio web") ||
                    bodyText.contains("página web") && bodyText.contains("temporalmente inactiva") ||
                    bodyText.contains("err_dns_no_matching_supported_alpn"));
        } catch (Exception e) {
            return false; // If we can't check, assume it's not an error page
        }
    }
    
    /**
     * Gets the error message if the page is showing an error
     */
    public String getErrorMessage() {
        try {
            return driver.findElement(By.tagName("body")).getText();
        } catch (Exception e) {
            return "Unable to retrieve error message: " + e.getMessage();
        }
    }
    
    /**
     * Waits for page to load with a more flexible approach
     */
    public boolean waitForPageToLoad() {
        try {
            // Wait for either the URL to be valid or some basic page elements
            int maxWaitTime = 15; // seconds
            for (int i = 0; i < maxWaitTime; i++) {
                if (isPageLoadedProperly()) {
                    return true;
                }
                Thread.sleep(1000);
            }
            return false;
        } catch (Exception e) {
            ReportLogger.log("Error waiting for page to load: " + e.getMessage());
            return false;
        }
    }

    protected void navigateTo(String url) {
        driver.get(url);
    }

    protected void takeScreenshot(String name) {
        ReportLogger.attachScreenshot(driver, name);
    }

    protected void logStep(String message) {
        ReportLogger.log(message);
    }

    protected WebElement waitForElementClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected void waitForUrlContains(String urlFragment) {
        wait.until(ExpectedConditions.urlContains(urlFragment));
    }

    protected List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }
}
