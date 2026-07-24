package com.project.pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Component Object representing the personnel list section on the directory page.
 */
public class PersonList {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By listContainer = By.cssSelector(".view-content");
    private final By personRows = By.cssSelector(".view-content .profesor");
    private final By personNames = By.cssSelector(".profesor .nombre a");
    private final By personRoles = By.cssSelector(".profesor .cargo");

    public PersonList(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isListDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(listContainer)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public int getPersonCount() {
        wait.until(ExpectedConditions.presenceOfElementLocated(personRows));
        List<WebElement> elements = driver.findElements(personRows);
        return elements.size();
    }

    public List<String> getPersonNames() {
        wait.until(ExpectedConditions.presenceOfElementLocated(personNames));
        List<WebElement> elements = driver.findElements(personNames);
        List<String> names = new ArrayList<>();
        for (WebElement elem : elements) {
            names.add(elem.getText().trim());
        }
        return names;
    }
}
