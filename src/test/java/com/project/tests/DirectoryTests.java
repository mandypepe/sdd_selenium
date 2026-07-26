package com.project.tests;

import com.project.annotations.TestCategory;
import com.project.data.DirectoryTestData;
import com.project.pages.DirectoryPage;
import com.project.tests.base.BaseTest;
import com.project.utils.ReportLogger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

/**
 * End-to-End Test Suite for People Directory Initial Load Verification.
 * Strictly adheres to POM: no raw WebDriver logic in test methods.
 */
public class DirectoryTests extends BaseTest {

    private DirectoryPage directoryPage;

    @BeforeMethod
    public void initPages() {
        directoryPage = new DirectoryPage();
    }

    @TestCategory({"smoke", "critical"})
    @Test(description = "AC-1.1 & FR-001: Navigate to directory page without HTTP or visual errors")
    public void test_navigateToDirectoryWithoutErrors() {
        ReportLogger.log("Verifying navigation to People Directory without errors");
        String currentUrl = directoryPage.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("directorio/personas"), 
                "URL should contain 'directorio/personas'. Actual: " + currentUrl);
    }

    @TestCategory({"smoke", "critical"})
    @Test(description = "AC-1.2 & FR-002: Main page title and section header are visible and correct")
    public void test_mainTitleAndHeaderVisible() {
        ReportLogger.log("Verifying page title and section header text");
        String pageTitle = directoryPage.getPageTitleText();
        String sectionHeader = directoryPage.getActiveSectionText();

        Assert.assertTrue(pageTitle.equalsIgnoreCase(DirectoryTestData.EXPECTED_PAGE_TITLE), 
                "Page title mismatch! Expected (case-insensitive): " + DirectoryTestData.EXPECTED_PAGE_TITLE + ", Actual: " + pageTitle);
        Assert.assertEquals(sectionHeader, DirectoryTestData.EXPECTED_SECTION_HEADER, 
                "Active section header mismatch!");
    }

    @TestCategory({"smoke", "critical"})
    @Test(description = "AC-1.3 & FR-003: Initial personnel list is populated with at least 1 record")
    public void test_initialPersonListPopulated() {
        ReportLogger.log("Verifying personnel list is visible and populated");
        Assert.assertTrue(directoryPage.isPersonListDisplayed(), 
                "Person list container should be displayed");

        int personCount = directoryPage.getPersonCount();
        ReportLogger.log("Found " + personCount + " personnel records on initial load.");
        Assert.assertTrue(personCount > 0, 
                "Personnel list should contain at least 1 record");
    }

    @TestCategory({"regression", "encoding"})
    @Test(description = "FR-005: Special accented characters in personnel names render correctly")
    public void test_specialCharactersEncodedCorrectly() {
        ReportLogger.log("Verifying character encoding for accented names");
        List<String> names = directoryPage.getPersonNames();
        Assert.assertFalse(names.isEmpty(), "Names list should not be empty");

        boolean foundAccentedName = names.stream()
                .anyMatch(name -> name.contains("Velázquez") || name.contains("Llaneras") || name.contains("á") || name.contains("é"));

        ReportLogger.log("First person name in list: " + names.get(0));
        Assert.assertTrue(foundAccentedName, 
                "Directory should display names with proper accented character encoding");
    }

    @TestCategory({"smoke", "critical"})
    @Test(description = "FR-004: Global navigation and header layout remain intact")
    public void test_navigationAndLayoutNotBroken() {
        ReportLogger.log("Verifying global portal header layout is visible");
        Assert.assertTrue(directoryPage.isHeaderVisible(), 
                "Portal header should remain visible and intact");
    }
}
