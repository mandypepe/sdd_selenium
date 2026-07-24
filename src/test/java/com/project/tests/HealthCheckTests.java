package com.project.tests;

import com.project.pages.HealthCheckPage;
import com.project.tests.base.BaseTest;
import com.project.utils.ReportLogger;
import com.project.utils.WaitUtils;
import org.openqa.selenium.TimeoutException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod; // Added this import
import org.testng.annotations.Test;
import org.testng.annotations.Parameters;
import org.testng.annotations.Optional;

public class HealthCheckTests extends BaseTest {

    private HealthCheckPage healthCheckPage;
    private WaitUtils waitUtils;

    @Override
    @BeforeMethod(alwaysRun = true)
    @Parameters({"baseUrl", "browser"})
    public void setUp(@Optional("https://www.uci.cu/index.php/directorio/personas") String baseUrl,
                      @Optional("chrome") String browser) {
        super.setUp(baseUrl, browser);
        healthCheckPage = new HealthCheckPage(driver);
        waitUtils = new WaitUtils(driver, 10); // Default timeout of 10 seconds
    }

    @Test(description = "AC-1.1: Verify no blank screen or server errors on initial page load")
    public void verifyNoBlankScreenOrServerError() {
        ReportLogger.log("Verifying page title is not empty.");
        Assert.assertFalse(healthCheckPage.getPageTitle().isEmpty(), "Page title should not be empty.");

        ReportLogger.log("Verifying body text length is greater than 0.");
        Assert.assertTrue(healthCheckPage.getBodyText().length() > 0, "Page body should not be empty.");

        ReportLogger.log("Verifying no common error indicators are present in the page title.");
        String pageTitle = healthCheckPage.getPageTitle();
        Assert.assertFalse(pageTitle.contains("404") || pageTitle.contains("500") || pageTitle.contains("503") || pageTitle.contains("Error"),
                "Page title contains error indicators: " + pageTitle);

        ReportLogger.log("Verifying no error indicators are present on the page body.");
        Assert.assertFalse(healthCheckPage.isErrorIndicatorPresent(), "Error indicator element is present on the page.");
    }

    @Test(description = "AC-1.2: Verify primary directory content is visible")
    public void verifyDirectoryContentVisible() {
        ReportLogger.log("Verifying directory content container is visible.");
        Assert.assertTrue(healthCheckPage.isDirectoryContentVisible(), "Directory content container should be visible.");

        ReportLogger.log("Verifying page contains expected directory heading text.");
        String bodyText = healthCheckPage.getBodyText();
        Assert.assertTrue(bodyText.contains("Personas"), "Page should contain 'Personas' heading text.");
    }

    @Test(description = "AC-1.3: Verify explicit failure on unresponsive server or invalid URL")
    public void verifyExplicitFailureOnUnresponsiveServer() {
        ReportLogger.log("Navigating to an intentionally invalid URL variant to provoke a failure.");
        String invalidUrl = "http://localhost:9999/invalid-path"; // Assuming this URL will not respond

        // Attempt to navigate and expect a TimeoutException or similar
        try {
            driver.get(invalidUrl);
            // If no exception, check for error indicators
            Assert.assertTrue(healthCheckPage.isErrorIndicatorPresent() || healthCheckPage.getPageTitle().contains("404"),
                    "Expected error indicators or 404 in title for invalid URL, but none found.");
            ReportLogger.log("Error indicators found as expected for invalid URL.");
        } catch (TimeoutException e) {
            ReportLogger.log("Caught expected TimeoutException for unresponsive server: " + e.getMessage());
            Assert.assertTrue(true, "Test failed gracefully with TimeoutException as expected.");
        } catch (Exception e) {
            ReportLogger.log("Caught unexpected exception for invalid URL: " + e.getMessage());
            Assert.fail("Test failed with an unexpected exception for invalid URL: " + e.getMessage());
        }
    }

    @Test(description = "AC-2.1: Verify screenshot is captured on intentional assertion failure")
    public void verifyScreenshotCapturedOnFailure() {
        ReportLogger.log("Intentionally failing this test to verify screenshot capture.");
        Assert.fail("This is an intentional failure to test screenshot capture.");
    }
}