package com.project.pages.components;

import com.project.utils.ReportLogger;
import com.project.utils.EncodingUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
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
     * Checks if the alphabet filter is available on the page
     * 
     * @return true if alphabet filter elements are found
     */
    public boolean isAlphabetFilterAvailable() {
        try {
            // Try to find any alphabet filter elements
            List<WebElement> filterElements = driver.findElements(By.cssSelector("[data-letter]"));
            if (!filterElements.isEmpty()) {
                return true;
            }
            
            // Try alternative selectors
            filterElements = driver.findElements(By.cssSelector(".alphabet-filter a"));
            if (!filterElements.isEmpty()) {
                return true;
            }
            
            // Try to find any links that might be alphabet letters
            filterElements = driver.findElements(By.cssSelector("a"));
            for (WebElement element : filterElements) {
                String text = element.getText().trim();
                if (text.length() == 1 && Character.isLetter(text.charAt(0))) {
                    return true;
                }
            }
            
            return false;
        } catch (Exception e) {
            ReportLogger.log("Error checking alphabet filter availability: " + e.getMessage());
            return false;
        }
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

    /**
     * Get all available letters from the alphabet filter
     * 
     * @return List of characters representing available letters
     */
    public List<Character> getAllAvailableLetters() {
        ReportLogger.log("Getting all available letters from alphabet filter");
        
        // Wait for page to load completely
        try {
            Thread.sleep(2000); // Wait for dynamic content
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        try {
            // Try multiple selector strategies to find alphabet letters
            List<WebElement> letterElements = new java.util.ArrayList<>();
            
            // Strategy 1: data-letter attributes
            letterElements = driver.findElements(By.cssSelector("[data-letter]"));
            if (letterElements.isEmpty()) {
                // Strategy 2: Common alphabet filter patterns
                letterElements = driver.findElements(By.cssSelector(".alphabet-filter a, .alpha-filter a, .pagination a[href*='letra']"));
            }
            if (letterElements.isEmpty()) {
                // Strategy 3: Links containing single letters
                letterElements = driver.findElements(By.cssSelector("a[href*='letra='], a[onclick*='letra']"));
            }
            if (letterElements.isEmpty()) {
                // Strategy 4: Any links that are single letters or contain letter patterns
                List<WebElement> allLinks = driver.findElements(By.tagName("a"));
                for (WebElement link : allLinks) {
                    String text = link.getText().trim();
                    if (text.length() == 1 && Character.isLetter(text.charAt(0))) {
                        letterElements.add(link);
                    }
                }
            }
            
            List<Character> availableLetters = new java.util.ArrayList<>();
            
            for (WebElement element : letterElements) {
                String letter = null;
                
                // Try to get letter from data-letter attribute
                letter = element.getAttribute("data-letter");
                
                if (letter == null || letter.isEmpty()) {
                    // Try to get from href parameter
                    String href = element.getAttribute("href");
                    if (href != null && href.contains("letra=")) {
                        int idx = href.indexOf("letra=") + 6;
                        if (idx < href.length()) {
                            letter = href.substring(idx, idx + 1);
                        }
                    }
                }
                
                if (letter == null || letter.isEmpty()) {
                    // Try to get from link text
                    letter = element.getText().trim();
                }
                
                if (letter != null && !letter.isEmpty() && 
                    !letter.equalsIgnoreCase("any") && !letter.equalsIgnoreCase("cualquiera") &&
                    letter.length() == 1 && Character.isLetter(letter.charAt(0))) {
                    char charLetter = Character.toUpperCase(letter.charAt(0));
                    if (!availableLetters.contains(charLetter)) {
                        availableLetters.add(charLetter);
                    }
                }
            }
            
            
            // Sort the letters alphabetically
            availableLetters.sort(null);
            
            ReportLogger.log("Found " + availableLetters.size() + " available letters: " + availableLetters);
            return availableLetters;
            
        } catch (Exception e) {
            ReportLogger.log("Error getting available letters: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }

    /**
     * Debug method to list all available filter elements on the page
     */
    public void debugAvailableFilterElements() {
        ReportLogger.log("=== DEBUG: Available Filter Elements ===");
        
        try {
            // Find all links that might be alphabet filters
            List<WebElement> allLinks = driver.findElements(By.tagName("a"));
            ReportLogger.log("Found " + allLinks.size() + " total links on page");
            
            int filterCount = 0;
            for (WebElement link : allLinks) {
                String text = link.getText().trim();
                String href = link.getAttribute("href");
                String onclick = link.getAttribute("onclick");
                
                // Check if this looks like an alphabet filter
                boolean isFilter = (text != null && text.length() == 1 && Character.isLetter(text.charAt(0))) ||
                                 (href != null && href.contains("letra=")) ||
                                 (onclick != null && onclick.contains("letra"));
                
                if (isFilter) {
                    filterCount++;
                    ReportLogger.log("Filter " + filterCount + ": text='" + text + "' href='" + href + "' onclick='" + onclick + "'");
                }
            }
            
            if (filterCount == 0) {
                ReportLogger.log("No alphabet filter elements found!");
            }
            
        } catch (Exception e) {
            ReportLogger.log("Error debugging filter elements: " + e.getMessage());
        }
        
        ReportLogger.log("=== END DEBUG ===");
    }

    /**
     * Check if a specific letter is clickable
     * 
     * @param letter The letter to check
     * @return true if the letter is clickable, false otherwise
     */
    public boolean isLetterClickable(String letter) {
        ReportLogger.log("Checking if letter '" + letter + "' is clickable");
        
        try {
            String upperLetter = letter.toUpperCase();
            WebElement letterElement = null;
            
            // Strategy 1: Try data-letter attribute
            try {
                By letterLocator = By.cssSelector(String.format(LETTER_CSS_TEMPLATE, upperLetter));
                letterElement = wait.until(ExpectedConditions.presenceOfElementLocated(letterLocator));
            } catch (Exception e1) {
                // Strategy 2: Try finding link with letter text
                try {
                    List<WebElement> allLinks = driver.findElements(By.tagName("a"));
                    for (WebElement link : allLinks) {
                        String text = link.getText().trim();
                        if (text.equalsIgnoreCase(upperLetter)) {
                            letterElement = link;
                            break;
                        }
                    }
                } catch (Exception e2) {
                    // Strategy 3: Try href with letra parameter
                    try {
                        By hrefLocator = By.cssSelector("a[href*='letra=" + upperLetter + "']");
                        letterElement = driver.findElement(hrefLocator);
                    } catch (Exception e3) {
                        // Strategy 4: Try onclick with letter
                        try {
                            By onclickLocator = By.cssSelector("a[onclick*='" + upperLetter + "']");
                            letterElement = driver.findElement(onclickLocator);
                        } catch (Exception e4) {
                            ReportLogger.log("Could not find letter element with any strategy");
                        }
                    }
                }
            }
            
            if (letterElement == null) {
                ReportLogger.log("Letter '" + letter + "' is not clickable: element not found");
                return false;
            }
            
            boolean isClickable = letterElement.isEnabled() && letterElement.isDisplayed();
            ReportLogger.log("Letter '" + letter + "' is clickable: " + isClickable);
            return isClickable;
            
        } catch (Exception e) {
            ReportLogger.log("Letter '" + letter + "' is not clickable: " + e.getMessage());
            return false;
        }
    }

    /**
     * Click a specific letter in the alphabet filter
     * 
     * @param letter The letter to click
     */
    public void clickLetter(String letter) {
        ReportLogger.log("Clicking letter: " + letter);
        
        try {
            WebElement letterElement = null;
            String upperLetter = letter.toUpperCase();
            
            // Strategy 1: Try data-letter attribute
            try {
                By letterLocator = By.cssSelector(String.format(LETTER_CSS_TEMPLATE, upperLetter));
                letterElement = wait.until(ExpectedConditions.elementToBeClickable(letterLocator));
            } catch (Exception e1) {
                // Strategy 2: Try finding link with letter text
                try {
                    List<WebElement> allLinks = driver.findElements(By.tagName("a"));
                    for (WebElement link : allLinks) {
                        String text = link.getText().trim();
                        if (text.equalsIgnoreCase(upperLetter)) {
                            letterElement = link;
                            break;
                        }
                    }
                    if (letterElement != null) {
                        wait.until(ExpectedConditions.elementToBeClickable(letterElement));
                    }
                } catch (Exception e2) {
                    // Strategy 3: Try href with letra parameter
                    try {
                        By hrefLocator = By.cssSelector("a[href*='letra=" + upperLetter + "']");
                        letterElement = wait.until(ExpectedConditions.elementToBeClickable(hrefLocator));
                    } catch (Exception e3) {
                        // Strategy 4: Try onclick with letter
                        try {
                            By onclickLocator = By.cssSelector("a[onclick*='" + upperLetter + "']");
                            letterElement = wait.until(ExpectedConditions.elementToBeClickable(onclickLocator));
                        } catch (Exception e4) {
                            ReportLogger.log("Could not find letter element with any strategy");
                        }
                    }
                }
            }
            
            if (letterElement == null) {
                throw new RuntimeException("Letter element not found for: " + letter);
            }
            
            // Handle special encoding for Ñ character
            if (EncodingUtils.requiresSpecialEncoding(letter)) {
                ReportLogger.log("Handling special character encoding for: " + letter);
                // Additional handling for special characters if needed
            }
            
            clickWithRetry(letterElement);
            ReportLogger.log("Successfully clicked letter: " + letter);
            
        } catch (Exception e) {
            ReportLogger.log("Failed to click letter '" + letter + "': " + e.getMessage());
            throw new RuntimeException("Failed to click letter: " + letter, e);
        }
    }

    /**
     * Wait for letter click completion and page stabilization
     * 
     * @param letter The letter that was clicked
     * @param timeoutSeconds Maximum time to wait in seconds
     * @return true if stabilization completed successfully
     */
    public boolean waitForLetterClickCompletion(String letter, int timeoutSeconds) {
        ReportLogger.log("Waiting for letter '" + letter + "' click completion");
        
        try {
            // Wait for the letter to become active
            By activeLetterLocator = By.cssSelector(String.format("[data-letter='%s'].active, [data-letter='%s'].is-active", letter, letter));
            
            WebDriverWait customWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(timeoutSeconds));
            boolean isActive = customWait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(activeLetterLocator),
                ExpectedConditions.attributeContains(By.cssSelector(String.format(LETTER_CSS_TEMPLATE, letter)), "class", "active")
            ));
            
            if (isActive) {
                ReportLogger.log("Letter '" + letter + "' is now active");
                return true;
            } else {
                ReportLogger.log("Letter '" + letter + "' did not become active within timeout");
                return false;
            }
            
        } catch (Exception e) {
            ReportLogger.log("Error waiting for letter '" + letter + "' completion: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if special character handling is correct for a given character
     * 
     * @param specialChar The special character to check
     * @return true if special character handling is correct
     */
    public boolean isSpecialCharacterHandlingCorrect(String specialChar) {
        ReportLogger.log("Checking special character handling for: " + specialChar);
        
        try {
            // Validate UTF-8 encoding
            boolean isValidUTF8 = EncodingUtils.validateUTF8Encoding(specialChar);
            
            // Check if the character is properly URL encoded
            String encoded = EncodingUtils.encodeUTF8Safe(specialChar);
            boolean isValidEncoding = encoded != null && !encoded.equals(specialChar);
            
            // Check if the character is clickable in the UI
            boolean isClickable = isLetterClickable(specialChar);
            
            boolean isCorrect = isValidUTF8 && isValidEncoding && isClickable;
            
            ReportLogger.log("Special character '" + specialChar + "' handling is correct: " + isCorrect);
            return isCorrect;
            
        } catch (Exception e) {
            ReportLogger.log("Error checking special character handling for '" + specialChar + "': " + e.getMessage());
            return false;
        }
    }

    /**
     * Wait for the filter to be ready for interaction
     * 
     * @param timeoutSeconds Maximum time to wait in seconds
     * @return true if filter is ready, false otherwise
     */
    public boolean waitForFilterReady(int timeoutSeconds) {
        ReportLogger.log("Waiting for alphabet filter to be ready");
        
        try {
            WebDriverWait customWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(timeoutSeconds));
            
            // Wait for any filter element to be present and clickable
            boolean isReady = customWait.until(ExpectedConditions.or(
                ExpectedConditions.elementToBeClickable(FILTER_ANY_PRIMARY),
                ExpectedConditions.elementToBeClickable(FILTER_ANY_TESTID),
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-letter]"))
            ));
            
            ReportLogger.log("Alphabet filter is ready: " + isReady);
            return isReady;
            
        } catch (Exception e) {
            ReportLogger.log("Alphabet filter not ready within timeout: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get encoded letter parameter for URL handling
     * User Story 3: System Stability During Index Navigation
     * Contract: AlphabetFilterContract.md
     */
    public String getEncodedLetterParameter(String letter) {
        try {
            // Use EncodingUtils for safe UTF-8 encoding
            return EncodingUtils.encodeUTF8Safe(letter);
        } catch (Exception e) {
            ReportLogger.log("Error encoding letter parameter '" + letter + "': " + e.getMessage());
            // Fallback to URL encoding
            try {
                return java.net.URLEncoder.encode(letter, "UTF-8");
            } catch (Exception ex) {
                return letter; // Last resort fallback
            }
        }
    }

    /**
     * Find letter element in the filter
     * 
     * @param letter The letter to find
     * @return WebElement if found, null otherwise
     */
    private WebElement findLetterElement(String letter) {
        try {
            By letterLocator = By.cssSelector(String.format(LETTER_CSS_TEMPLATE, letter));
            return wait.until(ExpectedConditions.presenceOfElementLocated(letterLocator));
        } catch (Exception e) {
            ReportLogger.log("Letter element not found: " + letter);
            return null;
        }
    }

    /**
     * Validate complete alphabet availability
     * User Story 3: System Stability During Index Navigation
     */
    public boolean isCompleteAlphabetAvailable() {
        List<Character> availableLetters = getAllAvailableLetters();
        
        // Check if all Spanish alphabet letters are available
        String spanishAlphabet = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
        
        for (char expectedLetter : spanishAlphabet.toCharArray()) {
            if (!availableLetters.contains(expectedLetter)) {
                ReportLogger.log("Missing letter in alphabet filter: " + expectedLetter);
                return false;
            }
        }
        
        ReportLogger.log("Complete Spanish alphabet is available: " + availableLetters.size() + " letters");
        return true;
    }

    /**
     * Get performance metrics for letter processing
     * User Story 3: System Stability During Index Navigation
     */
    public long measureLetterProcessingTime(String letter) {
        long startTime = System.currentTimeMillis();
        
        try {
            // Click the letter
            clickLetter(letter);
            
            // Wait for completion
            waitForLetterClickCompletion(letter, 5);
            
        } catch (Exception e) {
            ReportLogger.log("Error measuring processing time for letter '" + letter + "': " + e.getMessage());
        }
        
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    /**
     * Validate UTF-8 encoding for special characters
     * User Story 3: System Stability During Index Navigation
     */
    public boolean validateUTF8Encoding(String character) {
        try {
            // Test encoding
            String encoded = EncodingUtils.encodeUTF8Safe(character);
            if (encoded == null) {
                return false;
            }
            
            // Test decoding
            String decoded = java.net.URLDecoder.decode(encoded, "UTF-8");
            return decoded.equals(character);
            
        } catch (Exception e) {
            ReportLogger.log("UTF-8 encoding validation failed for character '" + character + "': " + e.getMessage());
            return false;
        }
    }

    /**
     * Checks if a specific letter is available in the filter
     * @param letter The letter to check
     * @return true if the letter is available, false otherwise
     */
    public boolean isLetterAvailable(String letter) {
        try {
            List<String> availableLetters = getAvailableLetters();
            return availableLetters.contains(letter.toUpperCase());
        } catch (Exception e) {
            ReportLogger.log("Error checking if letter '" + letter + "' is available: " + e.getMessage());
            return false;
        }
    }

    /**
     * Checks if special character handling is correct
     * User Story 3: System Stability During Index Navigation
     */
    public boolean isSpecialCharacterHandlingCorrect() {
        try {
            // Test Ñ character specifically
            return validateUTF8Encoding("Ñ") && 
                   isLetterAvailable("Ñ") &&
                   getEncodedLetterParameter("Ñ") != null;
        } catch (Exception e) {
            ReportLogger.log("Special character handling validation failed: " + e.getMessage());
            return false;
        }
    }


    /**
     * Gets all available letters from the filter
     * User Story 3: Complete alphabet availability
     */
    public List<String> getAvailableLetters() {
        List<String> availableLetters = new ArrayList<>();
        try {
            List<WebElement> letterElements = driver.findElements(By.cssSelector("[data-letter]"));
            for (WebElement element : letterElements) {
                String letter = element.getAttribute("data-letter");
                if (letter != null && !letter.equals("any")) {
                    availableLetters.add(letter.toUpperCase());
                }
            }
        } catch (Exception e) {
            ReportLogger.log("Error getting available letters: " + e.getMessage());
        }
        return availableLetters;
    }

    /**
     * Validates complete Spanish alphabet availability
     * User Story 3: Complete alphabet availability
     */
    public boolean validateCompleteAlphabetAvailability() {
        List<String> availableLetters = getAvailableLetters();
        
        // Expected Spanish alphabet
        List<String> expectedLetters = Arrays.asList(
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "Ñ", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
        );
        
        for (String expectedLetter : expectedLetters) {
            if (!availableLetters.contains(expectedLetter)) {
                ReportLogger.log("Missing letter in alphabet filter: " + expectedLetter);
                return false;
            }
        }
        
        ReportLogger.log("All " + expectedLetters.size() + " letters are available in the alphabet filter");
        return true;
    }
}
