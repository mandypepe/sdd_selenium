package com.project.tests.base;

import com.project.drivers.DriverFactory;
import com.project.drivers.DriverManager;
import com.project.utils.ReportLogger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Optional;

public class BaseTest {

    protected WebDriver driver;
    protected String baseUrl;

    @BeforeMethod(alwaysRun = true)
    @Parameters({"baseUrl", "browser"})
    public void setUp(@Optional("https://www.uci.cu/index.php/directorio/personas") String baseUrl,
                      @Optional("chrome") String browser) {
        this.baseUrl = baseUrl;
        driver = DriverFactory.createDriver(browser);
        DriverManager.setDriver(driver);
        ReportLogger.log("Browser started: " + browser);
        driver.manage().window().maximize();
        
        // Add retry logic for page navigation
        navigateToPageWithRetry(baseUrl);
    }
    
    /**
     * Attempts to navigate to the specified URL with retry logic
     */
    private void navigateToPageWithRetry(String url) {
        int maxRetries = 3;
        int retryDelay = 5000; // 5 seconds
        
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                ReportLogger.log("Navigation attempt " + attempt + " to: " + url);
                driver.get(url);
                
                // Wait a bit for the page to start loading
                Thread.sleep(2000);
                
                // Check if we're on a valid page (not data: or about:blank)
                String currentUrl = driver.getCurrentUrl();
                if (!currentUrl.equals("data:") && !currentUrl.equals("about:blank") && 
                    !currentUrl.contains("chrome-error://")) {
                    ReportLogger.log("Successfully navigated to: " + currentUrl);
                    return;
                }
                
                if (attempt < maxRetries) {
                    ReportLogger.log("Navigation failed, retrying in " + retryDelay + "ms...");
                    Thread.sleep(retryDelay);
                }
            } catch (Exception e) {
                ReportLogger.log("Navigation attempt " + attempt + " failed: " + e.getMessage());
                if (attempt < maxRetries) {
                    try {
                        Thread.sleep(retryDelay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }
        
        // If all retries failed, still try to continue but log the issue
        ReportLogger.log("All navigation attempts failed, proceeding with test anyway");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        try {
            ReportLogger.log("Closing browser");
            DriverManager.quitDriver();
        } catch (Exception e) {
            System.err.println("Error in teardown: " + e.getMessage());
        }
    }
}


