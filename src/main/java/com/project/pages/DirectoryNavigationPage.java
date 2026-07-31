package com.project.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for Directory Navigation.
 * Uses stable CSS selectors instead of fragile XPath.
 */
public class DirectoryNavigationPage extends BasePage {

    // Using stable CSS selectors instead of XPath with contains()
    private static final By PEOPLE_NAV_LINK = By.cssSelector(".menu-directorio a[href*='persona'], [data-testid='people-nav-link']");
    private static final By UNIVERSITIES_NAV_LINK = By.cssSelector(".menu-directorio a[href*='universidad'], [data-testid='universities-nav-link']");
    private static final By PHONES_NAV_LINK = By.cssSelector(".menu-directorio a[href*='telefono'], [data-testid='phones-nav-link']");
    private static final By ACTIVE_MENU_ITEM = By.cssSelector(".menu-item.menu-item--active-trail a");

    public DirectoryNavigationPage() {
        super();
    }

    public DirectoryNavigationPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Clicks on People section in the directory navigation
     */
    public void clickPeopleSection() {
        click(PEOPLE_NAV_LINK);
    }

    /**
     * Clicks on Universities section in the directory navigation
     */
    public void clickUniversitiesSection() {
        click(UNIVERSITIES_NAV_LINK);
    }

    /**
     * Clicks on Phones section in the directory navigation
     */
    public void clickPhonesSection() {
        click(PHONES_NAV_LINK);
    }

    /**
     * Gets the text of the currently active navigation item
     */
    public String getActiveSectionText() {
        return getText(ACTIVE_MENU_ITEM);
    }

    /**
     * Checks if People navigation link is visible
     */
    public boolean isPeopleLinkVisible() {
        return isElementDisplayed(PEOPLE_NAV_LINK);
    }

    /**
     * Checks if Universities navigation link is visible
     */
    public boolean isUniversitiesLinkVisible() {
        return isElementDisplayed(UNIVERSITIES_NAV_LINK);
    }

    /**
     * Checks if Phones navigation link is visible
     */
    public boolean isPhonesLinkVisible() {
        return isElementDisplayed(PHONES_NAV_LINK);
    }

    /**
     * Checks if People option is visible (backward compatibility)
     */
    public boolean isPeopleOptionVisible() {
        return isPeopleLinkVisible();
    }

    /**
     * Selects People section and waits for navigation
     */
    public PeopleSectionPage selectPeopleSection() {
        clickPeopleSection();
        waitForUrlContains("personas");
        return new PeopleSectionPage();
    }
}