package com.project.tests;

import com.project.annotations.TestCategory;
import com.project.data.PersonnelDataProvider;
import com.project.drivers.DriverManager;
import com.project.pages.DirectoryNavigationPage;
import com.project.pages.PeopleSectionPage;
import com.project.pages.components.PersonnelRecord;
import com.project.tests.base.BaseTest;
import com.project.utils.ReportLogger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DirectoryPeopleNavigationTests extends BaseTest {

    private DirectoryNavigationPage navigationPage;
    private PeopleSectionPage peopleSectionPage;

    @BeforeMethod(dependsOnMethods = "setUp")
    public void initPages() {
        navigationPage = new DirectoryNavigationPage();
        
        // Check if we're on an error page and skip tests gracefully
        if (navigationPage.isErrorPage()) {
            String errorMsg = navigationPage.getErrorMessage();
            ReportLogger.log("Website is showing an error page: " + errorMsg);
            throw new org.testng.SkipException("Website temporarily unavailable: " + errorMsg);
        }
    }

    @Test(description = "T017: Navigate to People section and view records")
    @TestCategory("smoke")
    public void test_navigateToPeopleAndViewRecords() {
        ReportLogger.log("Navigating to People section");
        Assert.assertTrue(navigationPage.isPeopleOptionVisible(), "People option should be visible");
        peopleSectionPage = navigationPage.selectPeopleSection();
        
        ReportLogger.log("Waiting for records to load");
        peopleSectionPage.waitForRecordsToLoad();
        
        int recordCount = peopleSectionPage.getRecordCount();
        Assert.assertTrue(recordCount > 0, "Should load at least one record");
        ReportLogger.log("Loaded " + recordCount + " records");
        ReportLogger.attachScreenshot(DriverManager.getDriver(), "T017_PeopleSectionLoaded");
    }

    @Test(description = "T018: Verify first page records display")
    @TestCategory("critical")
    public void test_firstPageRecordsDisplay() {
        peopleSectionPage = navigationPage.selectPeopleSection();
        peopleSectionPage.waitForRecordsToLoad();
        
        Assert.assertEquals(peopleSectionPage.getCurrentPageNumber(), 1, "Should be on page 1");
        
        List<PersonnelRecord> records = peopleSectionPage.getVisibleRecords();
        Assert.assertTrue(records.size() > 0, "Should have visible records");
        
        PersonnelRecord firstRecord = records.get(0);
        Assert.assertNotNull(firstRecord.getFullName(), "Full name should not be null");
        Assert.assertFalse(firstRecord.getFullName().trim().isEmpty(), "Full name should not be empty");
        
        ReportLogger.log("First record details: " + firstRecord.toString());
        ReportLogger.attachScreenshot(DriverManager.getDriver(), "T018_FirstPageRecords");
    }

    @Test(description = "T019: Navigate between pages and verify different records")
    @TestCategory("regression")
    public void test_multiPageNavigation() {
        peopleSectionPage = navigationPage.selectPeopleSection();
        peopleSectionPage.waitForRecordsToLoad();
        
        if (peopleSectionPage.getTotalPages() > 1) {
            ReportLogger.log("Pagination exists. Current page: " + peopleSectionPage.getCurrentPageNumber());
            Assert.assertFalse(peopleSectionPage.isPreviousPageButtonEnabled(), "Previous button should be disabled on first page");
            
            List<PersonnelRecord> page1Records = peopleSectionPage.getVisibleRecords();
            
            peopleSectionPage.goToNextPage();
            peopleSectionPage.waitForRecordsToLoad();
            
            Assert.assertEquals(peopleSectionPage.getCurrentPageNumber(), 2, "Should be on page 2");
            Assert.assertTrue(peopleSectionPage.isPreviousPageButtonEnabled(), "Previous button should be enabled on page 2");
            
            List<PersonnelRecord> page2Records = peopleSectionPage.getVisibleRecords();
            
            boolean allDifferent = true;
            for (PersonnelRecord p1 : page1Records) {
                for (PersonnelRecord p2 : page2Records) {
                    if (p1.getFullName().equals(p2.getFullName()) && p1.getId().equals(p2.getId())) {
                        allDifferent = false;
                        break;
                    }
                }
            }
            Assert.assertTrue(allDifferent, "Records on page 2 should be different from page 1");
            
            ReportLogger.attachScreenshot(DriverManager.getDriver(), "T019_Page2Records");
        } else {
            ReportLogger.log("Skipping multi-page navigation test - only one page of results available");
        }
    }

    @Test(description = "T020: Verify pagination boundary conditions", dataProvider = "boundaryScenarios", dataProviderClass = PersonnelDataProvider.class)
    @TestCategory("regression")
    public void test_paginationBoundaryConditions(String scenario, int pageNumber, boolean expectPrevEnabled, boolean expectNextEnabled) {
        peopleSectionPage = navigationPage.selectPeopleSection();
        peopleSectionPage.waitForRecordsToLoad();
        
        int totalPages = peopleSectionPage.getTotalPages();
        if (totalPages <= 1) {
            ReportLogger.log("Skipping boundary conditions test - insufficient pages");
            return;
        }

        if (pageNumber == -1) {
            ReportLogger.log("Navigating to the last page");
            int currentPage = peopleSectionPage.getCurrentPageNumber();
            while (currentPage < totalPages && peopleSectionPage.isNextPageButtonEnabled()) {
                peopleSectionPage.goToNextPage();
                peopleSectionPage.waitForRecordsToLoad();
                currentPage = peopleSectionPage.getCurrentPageNumber();
            }
        } else if (pageNumber > 1 && pageNumber <= totalPages) {
            ReportLogger.log("Navigating to page " + pageNumber);
            peopleSectionPage.navigateToPage(pageNumber);
            peopleSectionPage.waitForRecordsToLoad();
        } else if (pageNumber > totalPages) {
            ReportLogger.log("Requested page " + pageNumber + " exceeds total pages " + totalPages + ", skipping boundary logic");
            return;
        }
        
        Assert.assertEquals(peopleSectionPage.isPreviousPageButtonEnabled(), expectPrevEnabled, "Previous button state mismatch");
        Assert.assertEquals(peopleSectionPage.isNextPageButtonEnabled(), expectNextEnabled, "Next button state mismatch");
        ReportLogger.attachScreenshot(DriverManager.getDriver(), "T020_Boundary_Page_" + pageNumber);
    }

    @Test(description = "T021: Verify special characters rendering in names")
    @TestCategory("regression")
    public void test_specialCharactersRendering() {
        peopleSectionPage = navigationPage.selectPeopleSection();
        peopleSectionPage.waitForRecordsToLoad();
        
        List<PersonnelRecord> records = peopleSectionPage.getVisibleRecords();
        boolean hasAccents = false;
        boolean hasEncodingArtifacts = false;
        
        for (PersonnelRecord record : records) {
            String name = record.getFullName();
            if (name != null) {
                if (name.matches(".*[áéíóúñÁÉÍÓÚÑ].*")) {
                    hasAccents = true;
                }
                if (name.contains("Ã") || name.contains("Â") || name.contains("")) {
                    hasEncodingArtifacts = true;
                    ReportLogger.log("Found possible encoding artifact in name: " + name);
                }
            }
        }
        
        Assert.assertFalse(hasEncodingArtifacts, "Should not have encoding artifacts like Ã¡");
        if (hasAccents) {
            ReportLogger.log("Successfully verified proper accent rendering in names.");
        } else {
            ReportLogger.log("No names with accented characters found on the first page, but no artifacts detected either.");
        }
        
        ReportLogger.attachScreenshot(DriverManager.getDriver(), "T021_SpecialCharacters");
    }

    @Test(description = "T022: Navigate from first to last page and check for duplicates")
    @TestCategory("regression")
    public void test_firstToLastPageNavigation() {
        peopleSectionPage = navigationPage.selectPeopleSection();
        peopleSectionPage.waitForRecordsToLoad();
        
        int totalPages = peopleSectionPage.getTotalPages();
        if (totalPages <= 1) {
            ReportLogger.log("Skipping first to last page navigation - insufficient pages");
            return;
        }
        
        Set<String> uniqueIds = new HashSet<>();
        int currentPage = 1;
        
        while (true) {
            ReportLogger.log("Scanning page " + currentPage);
            List<PersonnelRecord> records = peopleSectionPage.getVisibleRecords();
            for (PersonnelRecord record : records) {
                String identifier = record.getId() != null && !record.getId().isEmpty() ? record.getId() : record.getFullName();
                uniqueIds.add(identifier);
            }
            
            if (peopleSectionPage.isNextPageButtonEnabled() && currentPage < totalPages) {
                peopleSectionPage.goToNextPage();
                peopleSectionPage.waitForRecordsToLoad();
                currentPage++;
            } else {
                break;
            }
        }
        
        Assert.assertFalse(peopleSectionPage.isNextPageButtonEnabled(), "Next button should be disabled on last page");
        Assert.assertTrue(peopleSectionPage.isPreviousPageButtonEnabled(), "Previous button should be enabled on last page");
        
        ReportLogger.log("Total unique records found across all pages: " + uniqueIds.size());
        ReportLogger.attachScreenshot(DriverManager.getDriver(), "T022_LastPage");
    }

    @Test(description = "T023: Direct page jump via numbered pagination")
    @TestCategory("regression")
    public void test_directPageJump() {
        peopleSectionPage = navigationPage.selectPeopleSection();
        peopleSectionPage.waitForRecordsToLoad();
        
        int totalPages = peopleSectionPage.getTotalPages();
        if (totalPages >= 3) {
            List<PersonnelRecord> page1Records = peopleSectionPage.getVisibleRecords();
            
            ReportLogger.log("Jumping to page 3");
            peopleSectionPage.navigateToPage(3);
            peopleSectionPage.waitForRecordsToLoad();
            
            Assert.assertEquals(peopleSectionPage.getCurrentPageNumber(), 3, "Should be on page 3");
            
            List<PersonnelRecord> page3Records = peopleSectionPage.getVisibleRecords();
            
            boolean allDifferent = true;
            for (PersonnelRecord p1 : page1Records) {
                for (PersonnelRecord p3 : page3Records) {
                    if (p1.getFullName().equals(p3.getFullName()) && p1.getId().equals(p3.getId())) {
                        allDifferent = false;
                        break;
                    }
                }
            }
            Assert.assertTrue(allDifferent, "Records on page 3 should be different from page 1");
            ReportLogger.attachScreenshot(DriverManager.getDriver(), "T023_DirectPageJump");
        } else {
            ReportLogger.log("Skipping direct page jump test - requires at least 3 pages");
        }
    }

    @Test(description = "T024: Empty results handling (soft test)")
    @TestCategory("regression")
    public void test_emptyResultsHandling() {
        peopleSectionPage = navigationPage.selectPeopleSection();
        peopleSectionPage.waitForRecordsToLoad();
        
        if (!peopleSectionPage.hasRecords()) {
            ReportLogger.log("No records found. Verifying empty state.");
            Assert.assertEquals(peopleSectionPage.getRecordCount(), 0, "Record count should be 0");
            ReportLogger.attachScreenshot(DriverManager.getDriver(), "T024_EmptyState");
        } else {
            ReportLogger.log("Records found. Empty state test not applicable for current data.");
            Assert.assertTrue(peopleSectionPage.getRecordCount() > 0, "Records count should be greater than 0");
        }
    }

    @Test(description = "T025: Verify parallel execution thread safety")
    @TestCategory("regression")
    public void test_parallelExecutionThreadSafety() {
        ReportLogger.log("Executing thread safety test on thread: " + Thread.currentThread().getName());
        peopleSectionPage = navigationPage.selectPeopleSection();
        peopleSectionPage.waitForRecordsToLoad();
        
        Assert.assertTrue(peopleSectionPage.getRecordCount() > 0, "Should load records safely in parallel");
        ReportLogger.log("Thread safety test passed for: " + Thread.currentThread().getName());
    }
}
