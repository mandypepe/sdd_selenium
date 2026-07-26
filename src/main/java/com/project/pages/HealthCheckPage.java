package com.project.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HealthCheckPage extends BasePage {

    // Locators
    private final By pageTitleLocator = By.tagName("title"); // Assuming title is within <title> tag
    private final By directoryContentContainer = By.id("directory-content"); // Placeholder, adjust as per actual HTML
    private final By errorIndicator = By.cssSelector(".error-message, .error-page-indicator"); // Placeholder, adjust as per actual HTML
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
        return isElementDisplayed(directoryContentContainer);
    }

    public boolean isErrorIndicatorPresent() {
        return isElementDisplayed(errorIndicator);
    }

    public String getBodyText() {
        return getText(bodyLocator);
    }
}