package com.project.pages.components;

import com.project.utils.ReportLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Reusable component for Drupal pagination.
 * Uses composition with WebDriver.
 */
public class PaginationComponent {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final By PAGER_CONTAINER = By.cssSelector("nav.pager, ul.pager__items, .item-list .pager");
    private static final By PAGER_PREV = By.cssSelector(".pager__item--previous a, li.pager-previous a");
    private static final By PAGER_NEXT = By.cssSelector(".pager__item--next a, li.pager-next a");
    private static final By PAGER_PAGES = By.cssSelector(".pager__item a, ul.pager li a");
    private static final By PAGER_ACTIVE = By.cssSelector(".pager__item.is-active, .pager-current");

    public PaginationComponent(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Checks if pagination is present on the page.
     * @return true if pager container is displayed, false otherwise.
     */
    public boolean isPaginationPresent() {
        try {
            return driver.findElement(PAGER_CONTAINER).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the current page number (1-indexed).
     * @return current page number or 1 if not found.
     */
    public int getCurrentPageNumber() {
        if (!isPaginationPresent()) return 1;
        try {
            WebElement activePageElement = driver.findElement(PAGER_ACTIVE);
            String text = activePageElement.getText().trim();
            if (!text.isEmpty()) {
                return Integer.parseInt(text);
            }
        } catch (Exception e) {
            // fallback to URL parsing if active element not found
        }
        
        String url = driver.getCurrentUrl();
        if (url != null && url.contains("?page=")) {
            try {
                String pageStr = url.substring(url.indexOf("?page=") + 6);
                int pageIdx = Integer.parseInt(pageStr.split("&")[0]);
                return pageIdx + 1; // 0-indexed in URL -> 1-indexed
            } catch (Exception ignored) {}
        }
        return 1;
    }

    /**
     * Gets the total number of pages.
     * @return total pages count.
     */
    public int getTotalPages() {
        if (!isPaginationPresent()) return 1;
        try {
            List<WebElement> pages = driver.findElements(PAGER_PAGES);
            int maxPage = getCurrentPageNumber();
            for (WebElement page : pages) {
                try {
                    int pNum = Integer.parseInt(page.getText().trim());
                    if (pNum > maxPage) {
                        maxPage = pNum;
                    }
                } catch (NumberFormatException ignored) {}
            }
            return maxPage;
        } catch (Exception e) {
            return getCurrentPageNumber();
        }
    }

    /**
     * Clicks the next page button.
     */
    public void goToNextPage() {
        ReportLogger.log("Navigating to next page");
        WebElement nextLink = wait.until(ExpectedConditions.elementToBeClickable(PAGER_NEXT));
        nextLink.click();
    }

    /**
     * Clicks the previous page button.
     */
    public void goToPreviousPage() {
        ReportLogger.log("Navigating to previous page");
        WebElement prevLink = wait.until(ExpectedConditions.elementToBeClickable(PAGER_PREV));
        prevLink.click();
    }

    /**
     * Clicks a specific page number link.
     * @param pageNumber the target page number to click.
     */
    public void goToPage(int pageNumber) {
        ReportLogger.log("Navigating to page " + pageNumber);
        if (!isPaginationPresent()) return;
        List<WebElement> pages = driver.findElements(PAGER_PAGES);
        for (WebElement page : pages) {
            if (page.getText().trim().equals(String.valueOf(pageNumber))) {
                wait.until(ExpectedConditions.elementToBeClickable(page)).click();
                return;
            }
        }
    }

    /**
     * Checks if the next page button is enabled (present and displayed).
     * @return true if enabled, false otherwise.
     */
    public boolean isNextPageEnabled() {
        try {
            return driver.findElement(PAGER_NEXT).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if the previous page button is enabled (present and displayed).
     * @return true if enabled, false otherwise.
     */
    public boolean isPreviousPageEnabled() {
        try {
            return driver.findElement(PAGER_PREV).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
