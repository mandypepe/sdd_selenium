package com.project.tests.base;

import com.project.drivers.DriverManager;
import com.project.drivers.DriverFactory;
import com.project.utils.ConnectivityChecker;
import com.project.utils.TestConfiguration;
import com.project.utils.ReportLogger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.SkipException;

/**
 * Base test class with connectivity awareness.
 * Automatically detects website availability and skips tests gracefully when offline.
 */
public abstract class ConnectivityAwareBaseTest {
    
    protected static final String BASE_URL = "https://www.uci.cu/index.php/directorio/personas";
    protected static final int CONNECTIVITY_WAIT_TIME = 10; // seconds
    
    protected boolean isWebsiteAvailable = false;
    
    @BeforeMethod
    @Parameters({"baseUrl", "browser"})
    public void setUp(@Optional(BASE_URL) String baseUrl,
                      @Optional("chrome") String browser) {
        ReportLogger.log("Setting up connectivity-aware test");
        ReportLogger.log(TestConfiguration.getConfigurationSummary());
        
        // Initialize WebDriver
        DriverManager.setDriver(DriverFactory.createDriver(browser));
        WebDriver driver = DriverManager.getDriver();
        
        // Check if we should skip tests due to offline mode
        if (TestConfiguration.isOfflineMode()) {
            ReportLogger.log("OFFLINE MODE DETECTED - Tests will be skipped with appropriate messages");
            isWebsiteAvailable = false;
            return;
        }
        
        // Check website connectivity
        isWebsiteAvailable = ConnectivityChecker.isWebsiteAccessible(driver, baseUrl);
        
        if (!isWebsiteAvailable) {
            ReportLogger.log("Website is not available - checking configuration");
            if (TestConfiguration.shouldSkipOnConnectivityFailure()) {
                ReportLogger.log("Skip on connectivity failure enabled - enabling offline mode");
                TestConfiguration.enableOfflineMode();
            }
        } else {
            ReportLogger.log("Website is available - proceeding with tests");
            driver.get(baseUrl);
        }
    }
    
    @AfterMethod
    public void tearDown() {
        ReportLogger.log("Cleaning up connectivity-aware test");
        DriverManager.quitDriver();
    }
    
    /**
     * Helper method to skip test if website is not available.
     * Call this at the beginning of test methods that require the website.
     */
    protected void skipTestIfWebsiteUnavailable(String testName) {
        if (!isWebsiteAvailable) {
            String message = "Skipping " + testName + " - website is not available";
            ReportLogger.log(message);
            throw new SkipException(message);
        }
    }
    
    /**
     * Helper method to assert with connectivity awareness.
     * Returns true if website is available, false otherwise.
     */
    protected boolean assertWithConnectivity(String testName, Runnable assertion) {
        if (!isWebsiteAvailable) {
            ReportLogger.log("Skipping assertions for " + testName + " - website unavailable");
            return false;
        }
        
        try {
            assertion.run();
            return true;
        } catch (Exception e) {
            ReportLogger.log("Assertion failed for " + testName + ": " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Enhanced version of isPageLoadedProperly that handles connectivity issues.
     */
    protected boolean isPageLoadedProperlyWithFallback() {
        if (!isWebsiteAvailable) {
            return false; // Can't be properly loaded if website is unavailable
        }
        
        WebDriver driver = DriverManager.getDriver();
        
        try {
            // Check for error state first
            if (ConnectivityChecker.isPageInErrorState(driver)) {
                ReportLogger.log("Page is in error state");
                return false;
            }
            
            // Additional check for 403 Forbidden specifically
            String pageSource = driver.getPageSource();
            String pageTitle = driver.getTitle();
            
            if (pageSource.contains("403") || pageSource.contains("Forbidden") || 
                pageTitle.contains("403") || pageTitle.contains("Forbidden") ||
                pageSource.contains("Request forbidden")) {
                ReportLogger.log("403 Forbidden error detected - page not properly loaded");
                return false;
            }
            
            // Check if we have meaningful content
            boolean hasContent = driver.findElements(org.openqa.selenium.By.cssSelector("body *")).size() > 5;
            boolean hasTitle = !driver.getTitle().isEmpty() && !driver.getTitle().equals("about:blank");
            boolean hasErrorPage = pageSource.contains("<h1>403") || pageSource.contains("Request forbidden");
            
            return hasContent && hasTitle && !hasErrorPage;
            
        } catch (Exception e) {
            ReportLogger.log("Error checking page load: " + e.getMessage());
            return false;
        }
    }
}