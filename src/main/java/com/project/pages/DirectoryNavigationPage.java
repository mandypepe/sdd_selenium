package com.project.pages;

import com.project.utils.ReportLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Page object for Directory Navigation Menu.
 */
public class DirectoryNavigationPage extends BasePage {

    private static final By NAVIGATION_MENU = By.cssSelector(".menu-directorio");
    private static final By MENU_ITEMS = By.cssSelector(".menu-directorio .menu-item a");
    private static final By ACTIVE_SECTION = By.cssSelector(".menu-directorio .menu-item.menu-item--active-trail a");
    private static final By PEOPLE_NAV_LINK = By.xpath("//div[contains(@class,'menu-directorio')]//a[contains(text(),'Persona')]");

    public DirectoryNavigationPage() {
        super();
    }

    /**
     * Navigates to the directory URL.
     * @param baseUrl the base URL to navigate to
     */
    public void openDirectory(String baseUrl) {
        ReportLogger.log("Opening directory URL: " + baseUrl);
        driver.get(baseUrl);
    }

    /**
     * Checks if the 'Personas' option is visible in the navigation menu.
     * @return true if visible, false otherwise
     */
    public boolean isPeopleOptionVisible() {
        return isElementDisplayed(PEOPLE_NAV_LINK);
    }

    /**
     * Selects the 'Personas' section.
     * @return a new PeopleSectionPage
     */
    public PeopleSectionPage selectPeopleSection() {
        ReportLogger.log("Selecting 'Personas' section");
        click(PEOPLE_NAV_LINK);
        wait.until(ExpectedConditions.urlContains("personas"));
        return new PeopleSectionPage();
    }

    /**
     * Retrieves all available navigation options as text.
     * @return a list of available option names
     */
    public List<String> getAvailableNavigationOptions() {
        ReportLogger.log("Getting available navigation options");
        List<WebElement> elements = waitForAllVisible(MENU_ITEMS);
        List<String> options = new ArrayList<>();
        for (WebElement el : elements) {
            options.add(el.getText().trim());
        }
        return options;
    }

    /**
     * Returns the currently active section text.
     * @return active section text
     */
    public String getActiveSection() {
        ReportLogger.log("Getting active navigation section text");
        return getText(ACTIVE_SECTION).trim();
    }
}
