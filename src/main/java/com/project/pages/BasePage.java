package com.project.pages;

import com.project.drivers.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
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

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
