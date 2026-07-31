package com.project.pages;

import com.project.pages.components.PersonList;
import com.project.pages.components.AlphabetFilterComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

/**
 * Page Object representing the People Directory Page.
 * Hides all locator details and provides clean business interaction methods.
 */
public class DirectoryPage extends BasePage {

    private final PersonList personList;
    private final AlphabetFilterComponent alphabetFilter;

    private final By pageTitle = By.cssSelector(".titulo-page h2");
    private final By activeSection = By.cssSelector(".menu-directorio .menu-item.menu-item--active-trail a");
    private final By headerContainer = By.id("header");

    public DirectoryPage() {
        super();
        this.personList = new PersonList(this.driver);
        this.alphabetFilter = new AlphabetFilterComponent(this.driver);
    }

    public DirectoryPage(WebDriver driver) {
        super(driver);
        this.personList = new PersonList(driver);
        this.alphabetFilter = new AlphabetFilterComponent(driver);
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
        return personList.hasRecords();
    }

    public int getPersonCount() {
        return personList.getPersonCount();
    }

    public List<String> getPersonNames() {
        List<PersonList.PersonRecord> records = personList.getPersonRecords();
        List<String> names = new ArrayList<>();
        for (PersonList.PersonRecord record : records) {
            names.add(record.getName());
        }
        return names;
    }

    /**
     * Get the alphabet filter component
     * 
     * @return AlphabetFilterComponent instance
     */
    public AlphabetFilterComponent getAlphabetFilter() {
        return alphabetFilter;
    }

    /**
     * Get the person list component
     * 
     * @return PersonList instance
     */
    public PersonList getPersonList() {
        return personList;
    }

    /**
     * Wait for empty state to be displayed
     * User Story 2: Navigate Directory with No Results
     * Contract: PeopleSectionPageContract.md
     */
    public boolean waitForEmptyState(int timeout) {
        return personList.waitForPersonListToLoad(timeout * 1000) && personList.isEmptyStateMessageDisplayed();
    }

    /**
     * Check if empty state message is displayed
     * User Story 2: Navigate Directory with No Results
     * Contract: PeopleSectionPageContract.md
     */
    public boolean isEmptyStateMessageDisplayed() {
        return personList.isEmptyStateMessageDisplayed();
    }

    /**
     * Get the empty state message text
     * User Story 2: Navigate Directory with No Results
     * Contract: PeopleSectionPageContract.md
     */
    public String getEmptyStateMessage() {
        return personList.getEmptyStateMessage();
    }

    /**
     * Check if the current state is no results
     * User Story 2: Navigate Directory with No Results
     */
    public boolean isNoResultsState() {
        return personList.isEmptyStateMessageDisplayed() || personList.getPersonCount() == 0;
    }

    /**
     * Get the empty state status
     * User Story 2: Navigate Directory with No Results
     */
    public String getEmptyStateStatus() {
        if (personList.isEmptyStateMessageDisplayed()) {
            return personList.getEmptyStateMessage();
        } else if (personList.getPersonCount() == 0) {
            return "No personnel records found";
        } else {
            return "Results available";
        }
    }
}
