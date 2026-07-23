package com.project.pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class NavbarComponent {
    private final WebDriver driver;
    private final By profileMenu = By.id("nav-profile");

    public NavbarComponent(WebDriver driver) {
        this.driver = driver;
    }

    public void openProfile() {
        driver.findElement(profileMenu).click();
    }
}
