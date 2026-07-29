package com.project.pages.components;

import com.project.utils.ReportLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Reusable component for the alphabetical filter bar on the directory page.
 * Encapsulates selector strategy and synchronization for the A-Z / "Any" filter.
 * Uses composition with WebDriver (same pattern as PaginationComponent).
 */
public class AlphabetFilterComponent {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Primary selectors — prefer data-* attributes for stability
    private static final By FILTER_ANY_PRIMARY   = By.cssSelector("[data-letter='any']");
    private static final By FILTER_ANY_TESTID    = By.cssSelector("[data-testid='alpha-filter'] [data-letter='any']");
    private static final By FILTER_ANY_LINK_TEXT = By.linkText("Cualquiera");
    private static final By FILTER_ANY_PARTIAL   = By.partialLinkText("Cualquiera");

    // Active-letter detection
    private static final By FILTER_ACTIVE        = By.cssSelector("[data-letter].active, [data-letter].is-active, .alphabet-filter .active");
    private static final By FILTER_ACTIVE_ARIA   = By.cssSelector("[data-letter][aria-current='true'], [data-letter][aria-selected='true']");

    // Letter selector template — resolved at runtime
    private static final String LETTER_CSS_TEMPLATE = "[data-letter='%s']";

    private static final int DEFAULT_TIMEOUT_SECONDS = 10;

    public AlphabetFilterComponent(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
    }

    /**
     * Clicks the "Any" (Cualquiera) filter option and waits for the page to respond.
     * Tries multiple selectors in order of preference.
     */
    public void selectAny() {
        ReportLogger.log("Selecting 'Any' (Cualquiera) alphabetical filter");
        WebElement anyElement = resolveAnyElement();
        clickWithRetry(anyElement);
        ReportLogger.log("'Any' filter selected");
    }

    /**
     * Clicks the filter for the given letter (e.g., "A", "B", "Ñ").
     *
     * @param letter the letter to filter by (case-insensitive)
     */
    public void selectLetter(String letter) {
        if (letter == null || letter.isBlank()) {
            throw new IllegalArgumentException("Letter must not be null or blank");
        }
        String normalizedLetter = letter.trim().toUpperCase();
        ReportLogger.log("Selecting alphabetical filter for letter: " + normalizedLetter);

        By letterLocator = By.cssSelector(String.format(LETTER_CSS_TEMPLATE, normalizedLetter.toLowerCase()));
        try {
            WebElement letterElement = wait.until(
                    ExpectedConditions.elementToBeClickable(letterLocator));
            clickWithRetry(letterElement);
            ReportLogger.log("Letter filter '" + normalizedLetter + "' selected");
        } catch (Exception primary) {
            // Fallback: try link text
            try {
                WebElement letterLink = wait.until(
                        ExpectedConditions.elementToBeClickable(By.linkText(normalizedLetter)));
                clickWithRetry(letterLink);
                ReportLogger.log("Letter filter '" + normalizedLetter + "' selected via link text fallback");
            } catch (Exception fallback) {
                throw new RuntimeException(
                        "Could not select letter filter '" + normalizedLetter + "'. Primary: "
                                + primary.getMessage() + "; Fallback: " + fallback.getMessage(), fallback);
            }
        }
    }

    /**
     * Returns the currently active filter letter, or "any" if the Any option is active,
     * or an empty string if the active state cannot be determined.
     *
     * @return active letter string (lower-case) or "any" or ""
     */
    public String getActiveLetter() {
        // Try data-letter attribute on active element
        for (By activeLocator : new By[]{FILTER_ACTIVE, FILTER_ACTIVE_ARIA}) {
            try {
                List<WebElement> activeElements = driver.findElements(activeLocator);
                if (!activeElements.isEmpty()) {
                    String dataLetter = activeElements.get(0).getAttribute("data-letter");
                    if (dataLetter != null && !dataLetter.isBlank()) {
                        return dataLetter.trim().toLowerCase();
                    }
                    String text = activeElements.get(0).getText().trim().toLowerCase();
                    if (!text.isEmpty()) {
                        return text;
                    }
                }
            } catch (StaleElementReferenceException ignored) {
                // retry with next locator
            }
        }

        // Fallback: check URL for ?letter= parameter
        String currentUrl = driver.getCurrentUrl();
        if (currentUrl != null && currentUrl.contains("letter=")) {
            try {
                String afterLetter = currentUrl.substring(currentUrl.indexOf("letter=") + 7);
                String letterValue = afterLetter.split("[&?#]")[0];
                if (!letterValue.isBlank()) {
                    return letterValue.toLowerCase();
                }
            } catch (Exception ignored) {}
        }

        return "";
    }

    /**
     * Returns true if the "Any" (Cualquiera) filter is currently the active selection.
     *
     * @return true when Any is active
     */
    public boolean isAnySelected() {
        String activeLetter = getActiveLetter();
        if ("any".equalsIgnoreCase(activeLetter) || "cualquiera".equalsIgnoreCase(activeLetter)) {
            return true;
        }

        // Secondary check: look for the Any element with an active CSS class
        try {
            WebElement anyElement = resolveAnyElementQuiet();
            if (anyElement != null) {
                String cssClass = anyElement.getAttribute("class");
                if (cssClass != null && (cssClass.contains("active") || cssClass.contains("is-active"))) {
                    return true;
                }
                String ariaCurrent = anyElement.getAttribute("aria-current");
                if ("true".equalsIgnoreCase(ariaCurrent) || "page".equalsIgnoreCase(ariaCurrent)) {
                    return true;
                }
            }
        } catch (Exception ignored) {}

        // If no letter filter is active at all, treat as "any" (default state)
        return activeLetter.isEmpty();
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    /**
     * Resolves the "Any" element using multiple selector strategies.
     * Throws RuntimeException if none succeed.
     */
    private WebElement resolveAnyElement() {
        By[] candidates = {FILTER_ANY_PRIMARY, FILTER_ANY_TESTID, FILTER_ANY_LINK_TEXT, FILTER_ANY_PARTIAL};
        for (By locator : candidates) {
            try {
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
                if (element != null) {
                    return element;
                }
            } catch (Exception ignored) {}
        }
        throw new RuntimeException(
                "Could not locate the 'Any' (Cualquiera) filter element. "
                        + "Tried: [data-letter='any'], data-testid variant, linkText, partialLinkText.");
    }

    /**
     * Quietly attempts to find the Any element without waiting; returns null on failure.
     */
    private WebElement resolveAnyElementQuiet() {
        By[] candidates = {FILTER_ANY_PRIMARY, FILTER_ANY_TESTID, FILTER_ANY_LINK_TEXT, FILTER_ANY_PARTIAL};
        for (By locator : candidates) {
            try {
                List<WebElement> elements = driver.findElements(locator);
                if (!elements.isEmpty()) {
                    return elements.get(0);
                }
            } catch (Exception ignored) {}
        }
        return null;
    }

    /**
     * Clicks an element with a single StaleElementReferenceException retry.
     */
    private void clickWithRetry(WebElement element) {
        try {
            element.click();
        } catch (StaleElementReferenceException stale) {
            ReportLogger.log("StaleElementReferenceException on click — retrying");
            // Re-resolve and click again
            WebElement fresh = resolveAnyElement();
            fresh.click();
        }
    }
}
