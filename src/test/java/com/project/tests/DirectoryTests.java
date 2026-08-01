package com.project.tests;

import com.project.annotations.TestCategory;
import com.project.data.DirectoryTestData;
import com.project.pages.DirectoryPage;
import com.project.tests.base.ConnectivityAwareBaseTest;
import com.project.utils.ReportLogger;
import com.project.drivers.DriverManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

/**
 * End-to-End Test Suite for People Directory Initial Load Verification.
 * Strictly adheres to POM: no raw WebDriver logic in test methods.
 */
public class DirectoryTests extends ConnectivityAwareBaseTest {

    private DirectoryPage directoryPage;

    @BeforeMethod
    public void initPages() {
        directoryPage = new DirectoryPage();
        
        // Wait for page to load properly before proceeding with tests
        if (!directoryPage.waitForPageToLoad()) {
            ReportLogger.log("Page did not load properly, tests may fail");
        }
        
        // Check if we're on an error page and skip tests gracefully
        if (directoryPage.isErrorPage()) {
            String errorMsg = directoryPage.getErrorMessage();
            ReportLogger.log("Website is showing an error page: " + errorMsg);
            throw new org.testng.SkipException("Website temporarily unavailable: " + errorMsg);
        }
    }

    @TestCategory({"smoke", "critical"})
    @Test(description = "AC-1.1 & FR-001: Navigate to directory page without HTTP or visual errors")
    public void test_navigateToDirectoryWithoutErrors() {
        skipTestIfWebsiteUnavailable("test_navigateToDirectoryWithoutErrors");
        
        ReportLogger.log("Verifying navigation to People Directory without errors");
        
        assertWithConnectivity("test_navigateToDirectoryWithoutErrors", () -> {
            // First check if page loaded properly
            if (!isPageLoadedProperlyWithFallback()) {
                String currentUrl = DriverManager.getDriver().getCurrentUrl();
                Assert.fail("Page did not load properly. Current URL: " + currentUrl);
            }
            
            String currentUrl = DriverManager.getDriver().getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("directorio/personas"), 
                    "URL should contain 'directorio/personas'. Actual: " + currentUrl);
        });
    }

    @TestCategory({"smoke", "critical"})
    @Test(description = "AC-1.2 & FR-002: Main page title and section header are visible and correct")
    public void test_mainTitleAndHeaderVisible() {
        skipTestIfWebsiteUnavailable("test_mainTitleAndHeaderVisible");
        
        ReportLogger.log("Verifying page title and section header text");
        
        assertWithConnectivity("test_mainTitleAndHeaderVisible", () -> {
            // Check if page loaded properly first
            if (!isPageLoadedProperlyWithFallback()) {
                Assert.fail("Cannot verify page elements - page did not load properly");
            }
        
        try {
            String pageTitle = directoryPage.getPageTitleText();
            String sectionHeader = directoryPage.getActiveSectionText();

            // Normalize strings to handle encoding differences
            String normalizedPageTitle = java.text.Normalizer.normalize(pageTitle, java.text.Normalizer.Form.NFC)
                    .replaceAll("\\p{M}", "");
            String normalizedExpectedTitle = java.text.Normalizer.normalize(DirectoryTestData.EXPECTED_PAGE_TITLE, java.text.Normalizer.Form.NFC)
                    .replaceAll("\\p{M}", "");
            
            Assert.assertTrue(normalizedPageTitle.equalsIgnoreCase(normalizedExpectedTitle), 
                    "Page title mismatch! Expected (case-insensitive): " + normalizedExpectedTitle + ", Actual: " + normalizedPageTitle);
            Assert.assertEquals(sectionHeader, DirectoryTestData.EXPECTED_SECTION_HEADER, 
                    "Active section header mismatch!");
        } catch (Exception e) {
            Assert.fail("Failed to retrieve page elements: " + e.getMessage());
        }
        });
    }

    @TestCategory({"smoke", "critical"})
    @Test(description = "AC-1.3 & FR-003: Initial personnel list is populated with at least 1 record")
    public void test_initialPersonListPopulated() {
        skipTestIfWebsiteUnavailable("test_initialPersonListPopulated");
        
        ReportLogger.log("Verifying personnel list is visible and populated");
        
        assertWithConnectivity("test_initialPersonListPopulated", () -> {
            // Check if page loaded properly first
            if (!isPageLoadedProperlyWithFallback()) {
                Assert.fail("Cannot verify person list - page did not load properly");
            }
        
        try {
            Assert.assertTrue(directoryPage.isPersonListDisplayed(), 
                    "Person list container should be displayed");

            int personCount = directoryPage.getPersonCount();
            ReportLogger.log("Found " + personCount + " personnel records on initial load.");
            Assert.assertTrue(personCount > 0, 
                    "Personnel list should contain at least 1 record");
        } catch (Exception e) {
            Assert.fail("Failed to verify person list: " + e.getMessage());
        }
        });
    }

    @TestCategory({"regression", "encoding"})
    @Test(description = "FR-005: Special accented characters in personnel names render correctly")
    public void test_specialCharactersEncodedCorrectly() {
        skipTestIfWebsiteUnavailable("test_specialCharactersEncodedCorrectly");
        
        ReportLogger.log("Verifying character encoding for accented names");
        
        assertWithConnectivity("test_specialCharactersEncodedCorrectly", () -> {
            List<String> names = directoryPage.getPersonNames();
            Assert.assertFalse(names.isEmpty(), "Names list should not be empty");

            boolean foundAccentedName = names.stream()
                    .anyMatch(name -> name.contains("Velázquez") || name.contains("Llaneras") || name.contains("á") || name.contains("é"));

            ReportLogger.log("First person name in list: " + names.get(0));
            Assert.assertTrue(foundAccentedName, 
                    "Directory should display names with proper accented character encoding");
        });
    }

    @TestCategory({"smoke", "critical"})
    @Test(description = "FR-004: Global navigation and header layout remain intact")
    public void test_navigationAndLayoutNotBroken() {
        skipTestIfWebsiteUnavailable("test_navigationAndLayoutNotBroken");
        
        ReportLogger.log("Verifying global portal header layout is visible");
        
        assertWithConnectivity("test_navigationAndLayoutNotBroken", () -> {
            Assert.assertTrue(directoryPage.isHeaderVisible(), 
                    "Portal header should remain visible and intact");
        });
    }
}
