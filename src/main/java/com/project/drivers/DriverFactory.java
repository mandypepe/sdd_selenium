package com.project.drivers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverFactory {

    /**
     * Creates a WebDriver instance based on the specified browser.
     * Supports headless mode via the "headless" system property.
     *
     * @param browser the browser name (e.g., "chrome", "firefox", "edge")
     * @return the configured WebDriver instance
     */
    public static WebDriver createDriver(String browser) {
        boolean isHeadless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        if (browser == null || browser.trim().isEmpty() || browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            if (isHeadless) options.addArguments("--headless=new");
            options.addArguments("--start-maximized");
            return new ChromeDriver(options);
        } else if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            if (isHeadless) options.addArguments("-headless");
            return new FirefoxDriver(options);
        } else if (browser.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            EdgeOptions options = new EdgeOptions();
            if (isHeadless) options.addArguments("--headless=new");
            options.addArguments("--start-maximized");
            return new EdgeDriver(options);
        }
        throw new IllegalArgumentException("Browser not supported: " + browser);
    }
}
