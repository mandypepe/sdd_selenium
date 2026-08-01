package com.project.pages;

import com.project.pages.components.PaginationComponent;
import com.project.pages.components.PersonnelRecord;
import com.project.pages.components.AlphabetFilterComponent;
import com.project.utils.ReportLogger;
import com.project.utils.WaitUtils;
import com.project.utils.PerformanceUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Page object for the People Section, containing personnel records.
 */
public class PeopleSectionPage extends BasePage {

    private static final By PERSONNEL_LIST = By.cssSelector(".view-content, .content, main, [data-testid='personnel-list']");
    private static final By PERSONNEL_RECORDS = By.cssSelector(".view-content .profesor, .profesor, .person, .record, [data-testid='personnel-record']");
    private static final By PERSON_NAME = By.cssSelector(".nombre a, .name a, .person-name a, [data-testid='person-name']");
    private static final By PERSON_ROLE = By.cssSelector(".cargo, .role, .person-role, [data-testid='person-role']");
    private static final By PAGE_TITLE = By.cssSelector(".titulo-page h2, h1, h2, .page-title, .title, [data-testid='page-title']");
    
    // Empty state locators for User Story 2
    private static final By EMPTY_STATE_MESSAGE = By.cssSelector(".view-empty .message, .empty .message, .no-results, [data-testid='empty-message']");
    private static final By EMPTY_STATE_CONTAINER = By.cssSelector(".view-empty, .empty, .no-results-container, [data-testid='empty-state']");
    private static final By NO_RESULTS_MESSAGE = By.cssSelector(".view-content .no-results, .view-empty p, .no-results, [data-testid='no-results']");

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

    /**
     * Returns the currently active filter letter (lower-case) or empty string if unknown.
     * Delegates to AlphabetFilterComponent.getActiveLetter()
     */
    public String getActiveFilterLetter() {
        return alphabetFilterComponent.getActiveLetter();
    }

    /**
     * Check if empty state message is displayed
     * User Story 2: Navigate Directory with No Results
     * Contract: PeopleSectionPageContract.md
     */
    public boolean isEmptyStateMessageDisplayed() {
        // Use PersonList component for empty state detection
        try {
            return WaitUtils.waitForElementVisible(driver, EMPTY_STATE_CONTAINER, 2) != null ||
                   WaitUtils.waitForElementVisible(driver, NO_RESULTS_MESSAGE, 2) != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the empty state message text
     * User Story 2: Navigate Directory with No Results
     * Contract: PeopleSectionPageContract.md
     */
    public String getEmptyStateMessage() {
        try {
            WebElement messageElement = WaitUtils.waitForElementVisible(driver, EMPTY_STATE_MESSAGE, 2);
            if (messageElement != null) {
                return messageElement.getText().trim();
            }
        } catch (Exception e) {
            // Try alternative no results message
            try {
                WebElement noResultsElement = WaitUtils.waitForElementVisible(driver, NO_RESULTS_MESSAGE, 2);
                if (noResultsElement != null) {
                    return noResultsElement.getText().trim();
                }
            } catch (Exception ex) {
                ReportLogger.log("No empty state message found: " + ex.getMessage());
            }
        }
        return "";
    }

    /**
     * Wait for empty state to be displayed
     * User Story 2: Navigate Directory with No Results
     * Contract: PeopleSectionPageContract.md
     */
    public boolean waitForEmptyState(int timeout) {
        try {
            return WaitUtils.waitForElementVisible(driver, EMPTY_STATE_CONTAINER, timeout) != null ||
                   WaitUtils.waitForElementVisible(driver, NO_RESULTS_MESSAGE, timeout) != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if the current state is empty (no personnel records)
     * User Story 2: Navigate Directory with No Results
     */
    public boolean isNoResultsState() {
        return isEmptyStateMessageDisplayed() || getPersonnelRecords().isEmpty();
    }

    /**
     * Get the current empty state message or status
     * User Story 2: Navigate Directory with No Results
     */
    public String getEmptyStateStatus() {
        if (isEmptyStateMessageDisplayed()) {
            return getEmptyStateMessage();
        } else if (getPersonnelRecords().isEmpty()) {
            return "No personnel records found";
        } else {
            return "Results available";
        }
    }

    /**
     * Get personnel records list
     * 
     * @return List of personnel records
     */
    public List<PersonnelRecord> getPersonnelRecords() {
        List<PersonnelRecord> records = new ArrayList<>();
        List<WebElement> recordElements = driver.findElements(PERSONNEL_RECORDS);
        
        for (WebElement element : recordElements) {
            try {
                String name = element.findElement(PERSON_NAME).getText();
                String role = element.findElement(PERSON_ROLE).getText();
                // Create PersonnelRecord with all required fields, using empty strings for missing data
                records.add(new PersonnelRecord("", name, role, "", "", ""));
            } catch (Exception e) {
                ReportLogger.log("Error parsing personnel record: " + e.getMessage());
            }
        }
        
        return records;
    }

    /**
     * Validates that the displayed results match the expected letter.
     * User Story 1: Navigate Directory with Existing Results
     * 
     * @param expectedLetter The expected letter to filter results by
     * @return true if results match the expected letter, false otherwise
     */
    public boolean validateResultsMatchLetter(String expectedLetter) {
        ReportLogger.log("Validating results match letter: " + expectedLetter);
        List<String> firstLetters = getFirstLettersOfDisplayedNames();
        
        if (firstLetters.isEmpty()) {
            ReportLogger.log("No results found to validate for letter: " + expectedLetter);
            return false;
        }
        
        // Check if all results start with the expected letter (case-insensitive)
        boolean allMatch = firstLetters.stream()
            .allMatch(letter -> letter.equalsIgnoreCase(expectedLetter));
        
        ReportLogger.log("Results validation for letter '" + expectedLetter + "': " + 
                        (allMatch ? "PASS" : "FAIL") + " (" + firstLetters.size() + " results)");
        return allMatch;
    }

    /**
     * Gets the first letters of all displayed personnel names.
     * Supports validateResultsMatchLetter method.
     * 
     * @return List of first letters of displayed names
     */
    public List<String> getFirstLettersOfDisplayedNames() {
        ReportLogger.log("Getting first letters of displayed names");
        List<PersonnelRecord> records = getPersonnelRecords();
        
        return records.stream()
            .map(record -> {
                String name = record.getFullName();
                if (name != null && !name.isEmpty()) {
                    return name.substring(0, 1).toUpperCase();
                }
                return "";
            })
            .filter(letter -> !letter.isEmpty())
            .collect(Collectors.toList());
    }

    /**
     * Measures the time it takes to load results for a specific letter.
     * User Story 1 & 3: Performance measurement for filtering operations
     * 
     * @param letter The letter to measure performance for
     * @return Time in milliseconds to load results
     */
    public long measureResultLoadTime(String letter) {
        ReportLogger.log("Measuring result load time for letter: " + letter);
        
        // Start performance measurement
        long startTime = System.currentTimeMillis();
        
        try {
            // Wait for results to stabilize
            waitForResultsToStabilize(10);
            
            long endTime = System.currentTimeMillis();
            long loadTime = endTime - startTime;
            
            ReportLogger.log("Result load time for letter '" + letter + "': " + loadTime + "ms");
            PerformanceUtils.startTimer("letter_filter_load_time_" + letter);
            PerformanceUtils.endTimer("letter_filter_load_time_" + letter);
            
            return loadTime;
        } catch (Exception e) {
            ReportLogger.log("Error measuring load time for letter '" + letter + "': " + e.getMessage());
            return -1;
        }
    }

    /**
     * Waits for results to stabilize after a filter operation.
     * User Story 1 & 3: Ensures results are fully loaded before validation
     * 
     * @param timeoutSeconds Maximum time to wait for stabilization
     * @return true if results stabilized within timeout, false otherwise
     */
    public boolean waitForResultsToStabilize(int timeoutSeconds) {
        ReportLogger.log("Waiting for results to stabilize (timeout: " + timeoutSeconds + "s)");
        
        try {
            // Wait for personnel list to be present
            wait.until(ExpectedConditions.presenceOfElementLocated(PERSONNEL_LIST));
            
            // Additional wait for dynamic content to load
            Thread.sleep(1000);
            
            // Verify that either we have results or we have an empty state
            boolean hasResults = !driver.findElements(PERSONNEL_RECORDS).isEmpty();
            boolean hasEmptyState = isEmptyStateMessageDisplayed();
            
            boolean stabilized = hasResults || hasEmptyState;
            
            ReportLogger.log("Results stabilization: " + (stabilized ? "SUCCESS" : "FAILED") + 
                           " (hasResults: " + hasResults + ", hasEmptyState: " + hasEmptyState + ")");
            
            return stabilized;
        } catch (Exception e) {
            ReportLogger.log("Error waiting for results to stabilize: " + e.getMessage());
            return false;
        }
    }
}
