package com.project.drivers;

import org.openqa.selenium.WebDriver;

/**
 * DriverManager simple usando ThreadLocal para permitir tests paralelos.
 * Ensures thread-safe WebDriver management for parallel test execution.
 * Each thread gets its own WebDriver instance, preventing state pollution.
 */
public class DriverManager {
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    public static void setDriver(WebDriver driver) {
        DRIVER.set(driver);
    }

    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }
}
