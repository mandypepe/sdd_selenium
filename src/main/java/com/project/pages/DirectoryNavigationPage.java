package com.project.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for Directory Navigation.
 * Uses stable CSS selectors instead of fragile XPath.
 */
public class DirectoryNavigationPage extends BasePage {

    // Using stable CSS selectors instead of XPath with contains()
    private static final By PEOPLE_NAV_LINK = By.cssSelector(".menu-directorio a[href*='persona'], [data-testid='people-nav-link'], a[href*='personas'], nav a[href*='persona'], .menu a[href*='persona']");
    private static final By UNIVERSITIES_NAV_LINK = By.cssSelector(".menu-directorio a[href*='universidad'], [data-testid='universities-nav-link'], a[href*='universidades'], nav a[href*='universidad']");
    private static final By PHONES_NAV_LINK = By.cssSelector(".menu-directorio a[href*='telefono'], [data-testid='phones-nav-link'], a[href*='telefonos'], nav a[href*='telefono']");
    private static final By ACTIVE_MENU_ITEM = By.cssSelector(".menu-item.menu-item--active-trail a, .active a, .current-menu-item a");
    
    // Fallback XPath selectors for more complex matching
    private static final By PEOPLE_NAV_LINK_XPATH = By.xpath("//a[contains(@href, 'persona') or contains(text(), 'Personas') or contains(text(), 'People')]");
    private static final By UNIVERSITIES_NAV_LINK_XPATH = By.xpath("//a[contains(@href, 'universidad') or contains(text(), 'Universidades') or contains(text(), 'Universities')]");
    private static final By PHONES_NAV_LINK_XPATH = By.xpath("//a[contains(@href, 'telefono') or contains(text(), 'Teléfonos') or contains(text(), 'Phones')]");

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
        clickWithFallback(PEOPLE_NAV_LINK, PEOPLE_NAV_LINK_XPATH);
    }

    /**
     * Clicks on Universities section in the directory navigation
     */
    public void clickUniversitiesSection() {
        clickWithFallback(UNIVERSITIES_NAV_LINK, UNIVERSITIES_NAV_LINK_XPATH);
    }

    /**
     * Clicks on Phones section in the directory navigation
     */
    public void clickPhonesSection() {
        clickWithFallback(PHONES_NAV_LINK, PHONES_NAV_LINK_XPATH);
    }
    
    /**
     * Helper method to try CSS selector first, then XPath fallback
     */
    private void clickWithFallback(By cssSelector, By xpathSelector) {
        try {
            click(cssSelector);
        } catch (Exception e) {
            try {
                click(xpathSelector);
            } catch (Exception ex) {
                throw new RuntimeException("Failed to locate element with both CSS and XPath selectors", ex);
            }
        }
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
        return isElementDisplayedWithFallback(PEOPLE_NAV_LINK, PEOPLE_NAV_LINK_XPATH);
    }

    /**
     * Checks if Universities navigation link is visible
     */
    public boolean isUniversitiesLinkVisible() {
        return isElementDisplayedWithFallback(UNIVERSITIES_NAV_LINK, UNIVERSITIES_NAV_LINK_XPATH);
    }

    /**
     * Checks if Phones navigation link is visible
     */
    public boolean isPhonesLinkVisible() {
        return isElementDisplayedWithFallback(PHONES_NAV_LINK, PHONES_NAV_LINK_XPATH);
    }
    
    /**
     * Helper method to check visibility with CSS selector first, then XPath fallback
     */
    private boolean isElementDisplayedWithFallback(By cssSelector, By xpathSelector) {
        try {
            return isElementDisplayed(cssSelector);
        } catch (Exception e) {
            try {
                return isElementDisplayed(xpathSelector);
            } catch (Exception ex) {
                return false;
            }
        }
    }

    /**
     * Checks if People option is visible (backward compatibility)
     */
    public boolean isPeopleOptionVisible() {
        // If we're already on the personas page, consider the people option available
        if (isOnPeoplePage()) {
            return true;
        }
        return isPeopleLinkVisible();
    }

    /**
     * Selects People section and waits for navigation
     */
    public PeopleSectionPage selectPeopleSection() {
        // Check if we're already on the personas page
        if (!getCurrentUrl().contains("personas")) {
            clickPeopleSection();
            waitForUrlContains("personas");
        }
        return new PeopleSectionPage();
    }
    
    /**
     * Checks if we're already on the personas page
     */
    public boolean isOnPeoplePage() {
        return getCurrentUrl().contains("personas");
    }
}