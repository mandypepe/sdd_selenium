package com.project.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtils {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public WaitUtils(WebDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    public WebElement untilVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void untilUrlContains(String fraction) {
        wait.until(ExpectedConditions.urlContains(fraction));
    }
}
