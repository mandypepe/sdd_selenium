package com.project.drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * DriverFactory using native Selenium 4.6+ driver management.
 * Supports headless mode via the "headless" system property.
 * Eliminates third-party WebDriverManager dependency.
 */
public class DriverFactory {

    /**
     * Creates a WebDriver instance based on the specified browser.
     * Uses native Selenium 4.6+ driver management.
     * Supports headless mode via the "headless" system property.
     *
     * @param browser the browser name (e.g., "chrome", "firefox", "edge")
     * @return the configured WebDriver instance
     */
    public static WebDriver createDriver(String browser) {
        boolean isHeadless = Boolean.parseBoolean(System.getProperty("headless", "true"));
        
        if (browser == null || browser.trim().isEmpty() || browser.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            if (isHeadless) {
                options.addArguments("--headless=new");
            }
            options.addArguments("--start-maximized");
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--no-sandbox");
            return new ChromeDriver(options);
            
        } else if (browser.equalsIgnoreCase("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            if (isHeadless) {
                options.addArguments("-headless");
            }
            options.addArguments("--width=1920");
            options.addArguments("--height=1080");
            return new FirefoxDriver(options);
            
        } else if (browser.equalsIgnoreCase("edge")) {
            EdgeOptions options = new EdgeOptions();
            if (isHeadless) {
                options.addArguments("--headless=new");
            }
            options.addArguments("--start-maximized");
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--no-sandbox");
            return new EdgeDriver(options);
        }
        
        throw new IllegalArgumentException("Browser not supported: " + browser);
    }
}