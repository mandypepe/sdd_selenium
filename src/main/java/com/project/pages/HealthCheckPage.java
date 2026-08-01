package com.project.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

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
        return isElementDisplayed(directoryContentContainer);
    }

    public boolean isErrorIndicatorPresent() {
        return isElementDisplayed(errorIndicator);
    }

    public String getBodyText() {
        return getText(bodyLocator);
    }
}