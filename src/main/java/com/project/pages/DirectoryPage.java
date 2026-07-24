package com.project.pages;

import com.project.pages.components.PersonList;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

/**
 * Page Object representing the People Directory Page.
 * Hides all locator details and provides clean business interaction methods.
 */
public class DirectoryPage extends BasePage {

    private final PersonList personList;

    private final By pageTitle = By.cssSelector(".titulo-page h2");
    private final By activeSection = By.cssSelector(".menu-directorio .menu-item.menu-item--active-trail a");
    private final By headerContainer = By.id("header");

    public DirectoryPage() {
        super();
        this.personList = new PersonList(this.driver);
    }

    public DirectoryPage(WebDriver driver) {
        super(driver);
        this.personList = new PersonList(driver);
    }

    public void open(String url) {
        driver.get(url);
    }

    public String getPageTitleText() {
        return getText(pageTitle);
    }

    public String getActiveSectionText() {
        return getText(activeSection);
    }

    public boolean isHeaderVisible() {
        return isElementDisplayed(headerContainer);
    }

    public boolean isPersonListDisplayed() {
        return personList.isListDisplayed();
    }

    public int getPersonCount() {
        return personList.getPersonCount();
    }

    public List<String> getPersonNames() {
        return personList.getPersonNames();
    }
}
