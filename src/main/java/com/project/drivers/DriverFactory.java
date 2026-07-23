package com.project.drivers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverFactory {

    public static WebDriver createDriver(String browser) {
        // Por simplicidad: solo Chrome está configurado. Extiende para otros navegadores.
        if (browser == null || browser.isEmpty() || browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            return new ChromeDriver(options);
        }
        throw new IllegalArgumentException("Browser not supported: " + browser);
    }
}
