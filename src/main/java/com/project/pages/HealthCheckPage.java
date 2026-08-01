package com.project.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.project.utils.ReportLogger;

public class HealthCheckPage extends BasePage {

    // Locators
    private final By pageTitleLocator = By.tagName("title"); // Assuming title is within <title> tag
    private final By directoryContentContainer = By.cssSelector("#directory-content, .directory-content, .content, main, [data-testid='directory-content'], .view-content");
    private final By errorIndicator = By.cssSelector(".error-message, .error-page-indicator, .error, [data-testid='error'], .alert-error");
    private final By bodyLocator = By.tagName("body");

    public HealthCheckPage() {
        super();
    }

    public HealthCheckPage(WebDriver driver) {
        super(driver);
    }

    public String getPageTitle() {
        // For page title, we can directly get it from the driver
        return driver.getTitle();
    }

    public boolean isDirectoryContentVisible() {
        debugPageState("HealthCheckPage.isDirectoryContentVisible");
        
        // Try original selector first
        boolean originalVisible = isElementDisplayed(directoryContentContainer);
        if (originalVisible) {
            return true;
        }
        
        // Try alternative content containers
        String[] contentSelectors = {
            "main", "article", ".content", ".container", "div[class*='content']",
            "div[class*='main']", "div[class*='wrapper']"
        };
        
        for (String selector : contentSelectors) {
            try {
                WebElement element = driver.findElement(By.cssSelector(selector));
                if (element.isDisplayed()) {
                    ReportLogger.log("Found content container with alternative selector: " + selector);
                    return true;
                }
            } catch (Exception e) {
                // Continue trying
            }
        }
        
        ReportLogger.log("No content containers found - page may be in error state");
        return false;
    }

    public boolean isErrorIndicatorPresent() {
        return isElementDisplayed(errorIndicator);
    }

    public String getBodyText() {
        return getText(bodyLocator);
    }
}