package com.project.pages;

import com.project.pages.components.PaginationComponent;
import com.project.pages.components.PersonnelRecord;
import com.project.pages.components.AlphabetFilterComponent;
import com.project.utils.ReportLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Page object for the People Section, containing personnel records.
 */
public class PeopleSectionPage extends BasePage {

    private static final By PERSONNEL_LIST = By.cssSelector(".view-content");
    private static final By PERSONNEL_RECORDS = By.cssSelector(".view-content .profesor");
    private static final By PERSON_NAME = By.cssSelector(".nombre a");
    private static final By PERSON_ROLE = By.cssSelector(".cargo");
    private static final By PAGE_TITLE = By.cssSelector(".titulo-page h2");

    private final PaginationComponent paginationComponent;
    private final AlphabetFilterComponent alphabetFilterComponent;

    public PeopleSectionPage() {
        super();
        this.paginationComponent = new PaginationComponent(driver);
        this.alphabetFilterComponent = new AlphabetFilterComponent(driver);
    }

    /**
     * Waits until the personnel records are loaded and present in the DOM.
     */
    public void waitForRecordsToLoad() {
        ReportLogger.log("Waiting for personnel records to load");
        wait.until(ExpectedConditions.presenceOfElementLocated(PERSONNEL_LIST));
    }

    /**
     * Retrieves a list of visible personnel records.
     * @return List of PersonnelRecord objects
     */
    public List<PersonnelRecord> getVisibleRecords() {
        ReportLogger.log("Getting visible personnel records");
        List<WebElement> recordElements = waitForAllVisible(PERSONNEL_RECORDS);
        List<PersonnelRecord> records = new ArrayList<>();
        for (WebElement el : recordElements) {
            records.add(createRecordFromElement(el));
        }
        return records;
    }

    /**
     * Returns the count of personnel records currently visible on the page.
     * @return the number of records
     */
    public int getRecordCount() {
        return driver.findElements(PERSONNEL_RECORDS).size();
    }

    /**
     * Gets the current page number.
     * @return the current page number
     */
    public int getCurrentPageNumber() {
        return paginationComponent.getCurrentPageNumber();
    }

    /**
     * Gets the total number of pages.
     * @return total pages count
     */
    public int getTotalPages() {
        return paginationComponent.getTotalPages();
    }

    /**
     * Navigates to a specific page number.
     * @param pageNumber the page number to navigate to
     */
    public void navigateToPage(int pageNumber) {
        ReportLogger.log("Navigating to page " + pageNumber);
        paginationComponent.goToPage(pageNumber);
        waitForRecordsToLoad();
    }

    /**
     * Navigates to the next page of records.
     */
    public void goToNextPage() {
        ReportLogger.log("Going to next page");
        paginationComponent.goToNextPage();
        waitForRecordsToLoad();
    }

    /**
     * Navigates to the previous page of records.
     */
    public void goToPreviousPage() {
        ReportLogger.log("Going to previous page");
        paginationComponent.goToPreviousPage();
        waitForRecordsToLoad();
    }

    /**
     * Applies the 'Any' alphabetical filter by delegating to AlphabetFilterComponent,
     * resets pagination to page 1, and waits for records to load.
     */
    public void applyAnyFilter() {
        ReportLogger.log("Applying 'Any' alphabet filter via component");
        alphabetFilterComponent.selectAny();
        paginationComponent.goToPage(1);
        waitForRecordsToLoad();
    }

    /**
     * Returns true if the 'Any' filter is currently active.
     * @return true when Any filter is active
     */
    public boolean isAnyFilterActive() {
        return alphabetFilterComponent.isAnySelected();
    }

    /**
     * Applies the specified letter filter by delegating to AlphabetFilterComponent,
     * resets pagination to page 1, and waits for records to load.
     * @param letter the letter to select (e.g., "A", "B", "Ñ")
     */
    public void applyLetterFilter(String letter) {
        ReportLogger.log("Applying letter filter: " + letter);
        alphabetFilterComponent.selectLetter(letter);
        paginationComponent.goToPage(1);
        waitForRecordsToLoad();
    }

    /**
     * Checks if the next page button is enabled.
     * @return true if enabled, false otherwise
     */
    public boolean isNextPageButtonEnabled() {
        return paginationComponent.isNextPageEnabled();
    }

    /**
     * Checks if the previous page button is enabled.
     * @return true if enabled, false otherwise
     */
    public boolean isPreviousPageButtonEnabled() {
        return paginationComponent.isPreviousPageEnabled();
    }

    /**
     * Checks if there are any personnel records displayed.
     * @return true if records exist, false otherwise
     */
    public boolean hasRecords() {
        return getRecordCount() > 0;
    }

    /**
     * Gets the page title text.
     * @return the title of the page
     */
    public String getPageTitleText() {
        return getText(PAGE_TITLE).trim();
    }

    /**
     * Helper method to map a WebElement to a PersonnelRecord.
     * @param recordElement the WebElement of the record
     * @return PersonnelRecord object
     */
    private PersonnelRecord createRecordFromElement(WebElement recordElement) {
        String name = recordElement.findElement(PERSON_NAME).getText().trim();
        String role = "";
        try {
            role = recordElement.findElement(PERSON_ROLE).getText().trim();
        } catch (Exception e) {
            // Role may be absent for some records
        }
        String id = String.valueOf(System.identityHashCode(recordElement));
        return new PersonnelRecord(id, name, role, null, null, null);
    }
}
