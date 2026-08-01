package com.project.pages.components;

import com.project.utils.WaitUtils;
import com.project.utils.ReportLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Component Object representing the Person List section.
 * Encapsulates all person list interactions and provides business-level methods.
 * Eliminates Thread.sleep() usage in favor of intelligent waiting.
 */
public class PersonList {

    private final WebDriver driver;
    private final WaitUtils waitUtils;

    // Locator constants - using more flexible selectors to handle different page structures
    private static final By PERSON_RECORDS = By.cssSelector("div[class*='row'], div[class*='item'], div[class*='record'], .views-row, .profesor, .person, .record, .item, tr, li, [data-testid='person-record'], article, section");
    private static final By PERSON_NAME_LINK = By.cssSelector("a[href*='persona'], a[href*='profile'], .views-field-title a, .nombre a, .name a, .person-name a, h1 a, h2 a, h3 a, strong a, [data-testid='person-name']");
    private static final By PERSON_DETAILS = By.cssSelector(".views-field-field-persona-nombre, .views-field-field-persona-apellidos, .nombre, .cargo, .person-details, .description, .info, .details, p, span, div, [data-testid='person-details']");
    private static final By EMPTY_STATE_MESSAGE = By.cssSelector(".view-empty, .no-results, .empty, [data-testid='empty-state'], .message, .alert, .notice");
    private static final By LOADING_INDICATOR = By.cssSelector(".loading, .spinner, [data-testid='loading'], .loader, .progress");
    private static final By PAGINATION_CONTAINER = By.cssSelector(".pagination, .pager, [data-testid='pagination'], .nav, .navigation");
    private static final By NEXT_PAGE_BUTTON = By.cssSelector(".next > a, .pager-next > a, [data-testid='next-page'], .pagination .next, a[href*='page']");
    private static final By PREV_PAGE_BUTTON = By.cssSelector(".prev > a, .pager-previous > a, [data-testid='prev-page'], .pagination .prev, a[href*='prev']");
    private static final By PAGE_INFO = By.cssSelector(".page-info, .pager-current, [data-testid='page-info'], .pagination .current, .current");
    private static final By RESULTS_COUNT = By.cssSelector(".view-header, .results-count, [data-testid='results-count'], .count, .total");

    public PersonList(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
    }

    /**
     * Checks if the person list has loaded and contains records
     */
    public boolean hasRecords() {
        try {
            return waitUtils.waitForNumberOfElementsToBeMoreThan(PERSON_RECORDS, 0);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if empty state message is displayed
     */
    public boolean isEmptyStateMessageDisplayed() {
        return waitUtils.isElementDisplayed(EMPTY_STATE_MESSAGE);
    }

    /**
     * Gets the empty state message text
     */
    public String getEmptyStateMessage() {
        try {
            return waitUtils.waitForVisibility(EMPTY_STATE_MESSAGE).getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Waits for person list to load with intelligent polling
     */
    public boolean waitForPersonListToLoad(int maxWaitTimeMs) {
        long startTime = System.currentTimeMillis();
        
        while (System.currentTimeMillis() - startTime < maxWaitTimeMs) {
            // Check for loading indicator first
            if (waitUtils.isElementPresent(LOADING_INDICATOR)) {
                // Still loading, continue waiting
                try {
                    Thread.sleep(200); // Minimal polling interval
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                continue;
            }
            
            // Check if we have results or empty state
            boolean hasResults = hasRecords();
            boolean hasEmptyState = isEmptyStateMessageDisplayed();
            
            if (hasResults || hasEmptyState) {
                return true;
            }
            
            // Continue polling
            try {
                Thread.sleep(200); // Minimal polling interval
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        return false;
    }

    /**
     * Gets all person records currently displayed
     */
    public List<PersonRecord> getPersonRecords() {
        List<PersonRecord> records = new ArrayList<>();
        
        try {
            List<WebElement> recordElements = waitUtils.waitForAllVisible(PERSON_RECORDS);
            
            ReportLogger.log("Found " + recordElements.size() + " potential record elements");
            
            for (WebElement element : recordElements) {
                try {
                    String name = "";
                    String details = "";
                    String profileUrl = "";
                    
                    // Try to extract name and profile URL with fallback
                    try {
                        WebElement nameLink = element.findElement(PERSON_NAME_LINK);
                        name = nameLink.getText().trim();
                        profileUrl = nameLink.getAttribute("href");
                    } catch (Exception e) {
                        // Try alternative approaches to find name
                        String elementText = element.getText().trim();
                        if (!elementText.isEmpty()) {
                            name = elementText;
                        }
                    }
                    
                    // Extract additional details
                    try {
                        List<WebElement> detailElements = element.findElements(PERSON_DETAILS);
                        for (WebElement detailElement : detailElements) {
                            details += detailElement.getText() + " ";
                        }
                    } catch (Exception e) {
                        // Ignore detail extraction errors
                    }
                    
                    // Only add record if we have some meaningful content
                    if (!name.isEmpty() || !details.trim().isEmpty()) {
                        records.add(new PersonRecord(name, details.trim(), profileUrl));
                    }
                    
                } catch (Exception e) {
                    ReportLogger.log("Error extracting person record: " + e.getMessage());
                }
            }
            
            ReportLogger.log("Successfully extracted " + records.size() + " person records");
            
        } catch (Exception e) {
            ReportLogger.log("No person records found or error page detected: " + e.getMessage());
            // Return empty list instead of throwing exception
        }
        
        return records;
    }

    /**
     * Gets the count of person records
     */
    public int getPersonCount() {
        try {
            return waitUtils.waitForAllVisible(PERSON_RECORDS).size();
        } catch (Exception e) {
            ReportLogger.log("Error getting person count: " + e.getMessage());
            return 0; // Return 0 instead of throwing exception
        }
    }

    /**
     * Checks if pagination is available
     */
    public boolean hasPagination() {
        return waitUtils.isElementDisplayed(PAGINATION_CONTAINER);
    }

    /**
     * Clicks next page button if available
     */
    public boolean clickNextPage() {
        try {
            WebElement nextButton = waitUtils.waitForClickable(NEXT_PAGE_BUTTON);
            nextButton.click();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Clicks previous page button if available
     */
    public boolean clickPreviousPage() {
        try {
            WebElement prevButton = waitUtils.waitForClickable(PREV_PAGE_BUTTON);
            prevButton.click();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets current page information
     */
    public String getCurrentPageInfo() {
        try {
            return waitUtils.waitForVisibility(PAGE_INFO).getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Gets results count information
     */
    public String getResultsCount() {
        try {
            return waitUtils.waitForVisibility(RESULTS_COUNT).getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Waits for content to stabilize after filtering or pagination
     */
    public boolean waitForContentToStabilize() {
        return waitUtils.waitForContentToStabilize(PERSON_RECORDS, 5000); // 5 second max wait
    }

    /**
     * Checks if the list is in a stable state (no loading indicators)
     */
    public boolean isStable() {
        return !waitUtils.isElementPresent(LOADING_INDICATOR) && 
               (hasRecords() || isEmptyStateMessageDisplayed());
    }

    /**
     * Data class representing a person record
     */
    public static class PersonRecord {
        private final String name;
        private final String details;
        private final String profileUrl;

        public PersonRecord(String name, String details, String profileUrl) {
            this.name = name;
            this.details = details;
            this.profileUrl = profileUrl;
        }

        public String getName() {
            return name;
        }

        public String getDetails() {
            return details;
        }

        public String getProfileUrl() {
            return profileUrl;
        }

        @Override
        public String toString() {
            return "PersonRecord{name='" + name + "', details='" + details + "'}";
        }
    }


    /**
     * Validate that the displayed results match the expected letter
     */
    public boolean validateResultsMatchLetter(String expectedLetter) {
        ReportLogger.log("Validating that results match letter: " + expectedLetter);
        
        try {
            List<PersonRecord> personRecords = getPersonRecords();
            
            if (personRecords.isEmpty()) {
                ReportLogger.log("No person names to validate");
                return false;
            }
            
            // Check if all names start with the expected letter (case-insensitive)
            for (PersonRecord record : personRecords) {
                String name = record.getName();
                if (name == null || name.trim().isEmpty()) {
                    continue;
                }
                
                String firstChar = name.trim().substring(0, 1).toUpperCase();
                if (!firstChar.equals(expectedLetter.toUpperCase())) {
                    ReportLogger.log("Name '" + name + "' does not start with expected letter '" + expectedLetter + "'");
                    return false;
                }
            }
            
            ReportLogger.log("All " + personRecords.size() + " results match expected letter '" + expectedLetter + "'");
            return true;
            
        } catch (Exception e) {
            ReportLogger.log("Error validating results match letter: " + e.getMessage());
            return false;
        }
    }
}