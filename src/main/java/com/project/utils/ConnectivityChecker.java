package com.project.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import java.time.Duration;

/**
 * Utility class for checking website connectivity and availability.
 * Provides methods to detect if the target website is accessible.
 */
public class ConnectivityChecker {
    
    private static final int CONNECTIVITY_TIMEOUT = 5; // seconds
    private static final String[] ERROR_INDICATORS = {
        "ERR_CONNECTION_TIMED_OUT",
        "ERR_CONNECTION_REFUSED", 
        "ERR_DNS_NO_MATCHING_SUPPORTED_ALPN",
        "No se puede acceder a este sitio web",
        "temporalmente inactiva",
        "Error 404",
        "Error 403",
        "403 Forbidden",
        "Forbidden",
        "Server Error",
        "Service Unavailable",
        "Request forbidden by administrative rules"
    };
    
    /**
     * Checks if the website is accessible and properly loaded.
     * @param driver WebDriver instance
     * @param url URL to check
     * @return true if website is accessible, false otherwise
     */
    public static boolean isWebsiteAccessible(WebDriver driver, String url) {
        try {
            ReportLogger.log("Checking website accessibility for: " + url);
            
            // Navigate to the URL with a short timeout
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(CONNECTIVITY_TIMEOUT));
            driver.get(url);
            
            // Check if we got an error page
            String pageSource = driver.getPageSource();
            String pageTitle = driver.getTitle();
            String currentUrl = driver.getCurrentUrl();
            
            ReportLogger.log("Page title: " + pageTitle);
            ReportLogger.log("Current URL: " + currentUrl);
            
            // Check for critical errors that make testing impossible
            if (currentUrl.contains("chrome-error://") || 
                pageSource.contains("ERR_CONNECTION_TIMED_OUT") ||
                pageSource.contains("ERR_NAME_NOT_RESOLVED")) {
                ReportLogger.log("Critical error detected - website not accessible");
                return false;
            }
            
            // For 403 Forbidden, we still consider it "accessible" for testing purposes
            // since we can run tests against the error page
            if (pageSource.contains("403") || pageSource.contains("Forbidden") || 
                pageTitle.contains("403") || pageTitle.contains("Forbidden")) {
                ReportLogger.log("403 Forbidden detected but considering accessible for testing");
                return true;
            }
            
            // Check for other error indicators that would make testing impossible
            for (String errorIndicator : ERROR_INDICATORS) {
                if (pageSource.contains(errorIndicator) || pageTitle.contains(errorIndicator)) {
                    if (errorIndicator.contains("403") || errorIndicator.contains("Forbidden")) {
                        continue; // Skip 403 errors - we can test against these
                    }
                    ReportLogger.log("Website error detected: " + errorIndicator);
                    return false;
                }
            }
            
            // Check if page has meaningful content (not just error page)
            boolean hasContent = driver.findElements(By.cssSelector("body *")).size() > 5;
            boolean hasTitle = !pageTitle.isEmpty() && !pageTitle.equals("about:blank");
            boolean hasCriticalErrorPage = pageSource.contains("<h1>404") || 
                                         pageSource.contains("500 Internal Server Error") ||
                                         pageSource.contains("Service Unavailable");
            
            boolean isAccessible = hasContent && hasTitle && !hasCriticalErrorPage;
            ReportLogger.log("Website accessibility result: " + isAccessible + " (hasContent: " + hasContent + ", hasTitle: " + hasTitle + ", hasCriticalErrorPage: " + hasCriticalErrorPage + ")");
            
            return isAccessible;
            
        } catch (Exception e) {
            ReportLogger.log("Website connectivity check failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Checks if the current page shows an error state.
     * @param driver WebDriver instance
     * @return true if page shows error, false otherwise
     */
    public static boolean isPageInErrorState(WebDriver driver) {
        try {
            String pageSource = driver.getPageSource();
            String pageTitle = driver.getTitle();
            
            for (String errorIndicator : ERROR_INDICATORS) {
                if (pageSource.contains(errorIndicator) || pageTitle.contains(errorIndicator)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return true; // Assume error if we can't check
        }
    }
    
    /**
     * Waits for website to become available (with timeout).
     * @param driver WebDriver instance
     * @param url URL to check
     * @param maxWaitTime Maximum time to wait in seconds
     * @return true if website becomes available, false otherwise
     */
    public static boolean waitForWebsiteAvailable(WebDriver driver, String url, int maxWaitTime) {
        ReportLogger.log("Waiting for website to become available: " + url);
        
        int elapsed = 0;
        while (elapsed < maxWaitTime) {
            if (isWebsiteAccessible(driver, url)) {
                ReportLogger.log("Website became available after " + elapsed + " seconds");
                return true;
            }
            
            try {
                Thread.sleep(2000); // Wait 2 seconds between checks
                elapsed += 2;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        
        ReportLogger.log("Website did not become available within " + maxWaitTime + " seconds");
        return false;
    }
}