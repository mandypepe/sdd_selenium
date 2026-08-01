# Quickstart Guide: Directory Alphabetical Index Filter Validation

**Feature**: TC-008 Directory Alphabetical Index Filter Validation  
**Date**: 2026-07-30  
**Purpose**: Quick implementation guide for developers and testers

## Overview

This guide provides step-by-step instructions for implementing the comprehensive alphabet filter validation that covers all 27 characters of the Spanish alphabet (A-Z including Ñ). The implementation follows Test-Driven Development (TDD) principles and maintains compliance with the project constitution.

## Prerequisites

### 1. Environment Setup
```bash
# Verify Java version
java -version  # Should be Java 17+

# Verify Maven
mvn -version   # Should be Maven 3.8+

# Verify project builds
mvn clean compile
```

### 2. Required Dependencies
All dependencies are already defined in `pom.xml`:
- Selenium 4.46.0+
- TestNG 7.8.0+
- WebDriverManager 5.4.1+
- Allure 2.19.0+

### 3. Browser Setup
```bash
# Chrome (primary - already configured)
# Verify ChromeDriver is automatically managed
```

## Implementation Steps

### Step 1: Enhance Page Objects

#### 1.1 Update AlphabetFilterComponent.java
```java
// Add these methods to src/main/java/com/project/pages/components/AlphabetFilterComponent.java

/**
 * Retrieves all available alphabet letters from the filter
 */
public List<Character> getAllAvailableLetters() {
    waitForFilterReady(10);
    List<WebElement> letterElements = driver.findElements(By.cssSelector("[data-letter]"));
    return letterElements.stream()
        .map(element -> element.getAttribute("data-letter").charAt(0))
        .collect(Collectors.toList());
}

/**
 * Checks if a specific letter is present and clickable
 */
public boolean isLetterClickable(String letter) {
    if (letter == null || letter.trim().isEmpty()) return false;
    
    try {
        WebElement letterElement = driver.findElement(By.cssSelector("[data-letter='" + letter.toLowerCase() + "']"));
        return letterElement.isDisplayed() && letterElement.isEnabled();
    } catch (NoSuchElementException e) {
        return false;
    }
}

/**
 * Waits for filter to be fully interactive
 */
public boolean waitForFilterReady(int timeoutSeconds) {
    try {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".menu-directorio"))) != null;
    } catch (TimeoutException e) {
        return false;
    }
}
```

#### 1.2 Update PeopleSectionPage.java
```java
// Add these methods to src/main/java/com/project/pages/PeopleSectionPage.java

/**
 * Checks if empty state message is displayed
 */
public boolean isEmptyStateMessageDisplayed() {
    try {
        WebElement emptyState = driver.findElement(By.cssSelector(".view-empty"));
        return emptyState.isDisplayed();
    } catch (NoSuchElementException e) {
        return false;
    }
}

/**
 * Gets the text content of the empty state message
 */
public String getEmptyStateMessage() {
    try {
        WebElement emptyState = driver.findElement(By.cssSelector(".view-empty"));
        return emptyState.getText().trim();
    } catch (NoSuchElementException e) {
        return null;
    }
}

/**
 * Validates that displayed results match the selected letter filter
 */
public boolean validateResultsMatchLetter(String expectedLetter) {
    if (!hasRecords()) return isEmptyStateMessageDisplayed();
    
    List<String> names = getVisibleRecords().stream()
        .map(PersonnelRecord::getFullName)
        .collect(Collectors.toList());
    
    String expectedUpper = expectedLetter.toUpperCase();
    String expectedLower = expectedLetter.toLowerCase();
    
    for (String name : names) {
        if (name == null || name.trim().isEmpty()) continue;
        
        char firstChar = Character.toUpperCase(name.trim().charAt(0));
        if (firstChar != expectedUpper.charAt(0) && firstChar != expectedLower.charAt(0)) {
            return false;
        }
    }
    return true;
}
```

### Step 2: Create Test Data Provider

#### 2.1 Create AlphabetDataProvider.java
```java
// Create src/test/java/com/project/data/AlphabetDataProvider.java
package com.project.data;

import org.testng.annotations.DataProvider;
import java.util.List;
import java.util.Arrays;

public class AlphabetDataProvider {
    
    // Complete Spanish alphabet (27 characters)
    private static final List<String> SPANISH_ALPHABET = Arrays.asList(
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
        "N", "Ñ", "O", "P", "Q", "R", "S", "T", "U", "V", "X", "Y", "Z"
        // Note: W is not in traditional Spanish alphabet
    );
    
    @DataProvider(name = "spanishAlphabet")
    public Object[][] spanishAlphabetProvider() {
        Object[][] data = new Object[SPANISH_ALPHABET.size()][1];
        for (int i = 0; i < SPANISH_ALPHABET.size(); i++) {
            data[i][0] = SPANISH_ALPHABET.get(i);
        }
        return data;
    }
    
    @DataProvider(name = "specialCharacters")
    public Object[][] specialCharactersProvider() {
        return new Object[][] {{"Ñ"}};
    }
    
    @DataProvider(name = "commonLetters")
    public Object[][] commonLettersProvider() {
        return new Object[][] {
            {"A"}, {"E"}, {"I"}, {"L"}, {"M"}, {"R"}, {"S"}
        };
    }
}
```

### Step 3: Implement Main Test Class

#### 3.1 Update AlphabetFilterFullCoverageTest.java
```java
// Update src/test/java/com/project/tests/AlphabetFilterFullCoverageTest.java
package com.project.tests.uh;

import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import com.project.utils.ReportLogger;
import com.project.tests.base.BaseTest;
import com.project.pages.PeopleSectionPage;
import com.project.pages.components.AlphabetFilterComponent;
import com.project.data.AlphabetDataProvider;
import java.time.Duration;
import java.util.List;

public class AlphabetFilterFullCoverageTest extends BaseTest {
    
    private PeopleSectionPage peopleSectionPage;
    private AlphabetFilterComponent alphabetFilter;
    
    @Override
    public void setUp() {
        super.setUp();
        // Navigate to directory page
        driver.get("https://www.uci.cu/index.php/directorio/personas");
        peopleSectionPage = new PeopleSectionPage(driver);
        alphabetFilter = new AlphabetFilterComponent(driver);
        
        // Wait for initial load
        peopleSectionPage.waitForRecordsToLoad();
    }
    
    @Test(dataProvider = "spanishAlphabet", dataProviderClass = AlphabetDataProvider.class,
          description = "TC-008: Validate each alphabet letter functionality")
    public void testAlphabetLetterFilter(String letter) {
        ReportLogger.log("TC-008: Testing letter '" + letter + "'");
        
        // Step 1: Verify letter is clickable
        Assert.assertTrue(alphabetFilter.isLetterClickable(letter), 
                        "Letter '" + letter + "' should be clickable");
        
        // Step 2: Click the letter
        long startTime = System.currentTimeMillis();
        alphabetFilter.clickLetter(letter);
        
        // Step 3: Wait for results to load
        peopleSectionPage.waitForRecordsToStabilize(10);
        long loadTime = System.currentTimeMillis() - startTime;
        
        // Step 4: Validate results
        if (peopleSectionPage.hasRecords()) {
            // Has results - validate they match the letter
            Assert.assertTrue(peopleSectionPage.validateResultsMatchLetter(letter),
                            "Results should match letter '" + letter + "'");
            
            int recordCount = peopleSectionPage.getRecordCount();
            ReportLogger.log("Letter '" + letter + "' returned " + recordCount + " records in " + loadTime + "ms");
            
        } else {
            // No results - validate empty state
            Assert.assertTrue(peopleSectionPage.isEmptyStateMessageDisplayed(),
                            "Empty state message should be displayed for letter '" + letter + "'");
            
            String emptyMessage = peopleSectionPage.getEmptyStateMessage();
            Assert.assertNotNull(emptyMessage, "Empty state message should not be null");
            Assert.assertFalse(emptyMessage.trim().isEmpty(), "Empty state message should not be empty");
            
            ReportLogger.log("Letter '" + letter + "' returned no results: " + emptyMessage);
        }
        
        // Step 5: Performance check
        Assert.assertTrue(loadTime < 10000, "Load time should be less than 10 seconds");
        
        ReportLogger.attachScreenshot(driver, "TC-008_Letter_" + letter + "_Results");
    }
    
    @Test(description = "TC-008: Validate complete alphabet availability")
    public void testCompleteAlphabetAvailability() {
        ReportLogger.log("TC-008: Validating complete Spanish alphabet availability");
        
        // Get all available letters
        List<Character> availableLetters = alphabetFilter.getAllAvailableLetters();
        
        // Should have exactly 27 characters
        Assert.assertEquals(availableLetters.size(), 27, 
                          "Should have exactly 27 characters in Spanish alphabet");
        
        // Should include Ñ character
        Assert.assertTrue(availableLetters.contains('Ñ'), 
                          "Should include Ñ character");
        
        // Should include A and Z
        Assert.assertTrue(availableLetters.contains('A'), "Should include A character");
        Assert.assertTrue(availableLetters.contains('Z'), "Should include Z character");
        
        ReportLogger.log("Complete alphabet validated: " + availableLetters.size() + " characters found");
    }
    
    @Test(dataProvider = "specialCharacters", dataProviderClass = AlphabetDataProvider.class,
          description = "TC-008: Validate special character handling (Ñ)")
    public void testSpecialCharacterHandling(String specialChar) {
        ReportLogger.log("TC-008: Testing special character '" + specialChar + "'");
        
        // Verify special character is clickable
        Assert.assertTrue(alphabetFilter.isLetterClickable(specialChar), 
                        "Special character '" + specialChar + "' should be clickable");
        
        // Click and verify proper handling
        alphabetFilter.clickLetter(specialChar);
        peopleSectionPage.waitForRecordsToStabilize(10);
        
        // Should not cause encoding errors
        boolean hasError = false;
        try {
            if (peopleSectionPage.hasRecords()) {
                peopleSectionPage.getVisibleRecords();
            } else {
                peopleSectionPage.getEmptyStateMessage();
            }
        } catch (Exception e) {
            hasError = true;
            ReportLogger.log("Error handling special character: " + e.getMessage());
        }
        
        Assert.assertFalse(hasError, "Special character '" + specialChar + "' should not cause errors");
        
        ReportLogger.log("Special character '" + specialChar + "' handled correctly");
    }
    
    @Test(description = "TC-008: Performance validation - full alphabet execution")
    public void testFullAlphabetPerformance() {
        ReportLogger.log("TC-008: Validating full alphabet performance");
        
        long startTime = System.currentTimeMillis();
        int lettersTested = 0;
        int errors = 0;
        
        List<String> alphabet = Arrays.asList(
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "Ñ", "O", "P", "Q", "R", "S", "T", "U", "V", "X", "Y", "Z"
        );
        
        for (String letter : alphabet) {
            try {
                alphabetFilter.clickLetter(letter);
                peopleSectionPage.waitForRecordsToStabilize(5);
                lettersTested++;
            } catch (Exception e) {
                errors++;
                ReportLogger.log("Error testing letter '" + letter + "': " + e.getMessage());
            }
        }
        
        long totalTime = System.currentTimeMillis() - startTime;
        
        // Performance assertions
        Assert.assertTrue(totalTime < 180000, "Total execution time should be less than 3 minutes");
        Assert.assertEquals(lettersTested, 27, "All 27 letters should be tested");
        Assert.assertEquals(errors, 0, "No errors should occur during execution");
        
        ReportLogger.log("Performance validation completed:");
        ReportLogger.log("- Total time: " + totalTime + "ms");
        ReportLogger.log("- Letters tested: " + lettersTested);
        ReportLogger.log("- Errors: " + errors);
        ReportLogger.log("- Average time per letter: " + (totalTime / lettersTested) + "ms");
    }
}
```

### Step 4: Run Tests

#### 4.1 Single Test Execution
```bash
# Run specific test class
mvn -Dtest=AlphabetFilterFullCoverageTest test

# Run specific test method
mvn -Dtest=AlphabetFilterFullCoverageTest#testCompleteAlphabetAvailability test
```

#### 4.2 Full Test Suite
```bash
# Run all tests
mvn test

# Run with specific browser
mvn -Dbrowser=chrome test
```

#### 4.3 Generate Reports
```bash
# Generate Allure report
mvn test
allure serve allure-results/

# Or open in browser
allure open allure-results/
```

## Troubleshooting

### Common Issues

#### 1. Element Not Found
```java
// Problem: Locator doesn't match actual UI
// Solution: Update locators in page objects
// Check actual UI using browser dev tools
```

#### 2. Timeout Issues
```java
// Problem: Elements take too long to load
// Solution: Increase timeout values
peopleSectionPage.waitForRecordsToStabilize(15); // Increase from 10
```

#### 3. Encoding Issues with Ñ
```java
// Problem: Ñ character not handled correctly
// Solution: Verify UTF-8 encoding
System.setProperty("file.encoding", "UTF-8");
```

### Debug Tips

#### 1. Enable Debug Logging
```bash
# Run with debug logging
mvn test -Dorg.slf4j.simpleLogger.defaultLogLevel=debug
```

#### 2. Take Screenshots
```java
// Add screenshots for debugging
ReportLogger.attachScreenshot(driver, "debug_state");
```

#### 3. Check Page Source
```java
// Log page source for debugging
ReportLogger.log("Page source: " + driver.getPageSource());
```

## Best Practices

### 1. Test Organization
- Use descriptive test method names
- Group related tests together
- Use data providers for parameterized tests

### 2. Error Handling
- Provide meaningful error messages
- Use try-catch blocks for expected failures
- Log all important actions and results

### 3. Performance
- Use efficient locators (CSS > XPath)
- Avoid unnecessary waits
- Reuse page objects and components

### 4. Maintenance
- Keep page objects updated with UI changes
- Regular test execution to catch regressions
- Review and refactor test code periodically

## Next Steps

1. **UI Inspection**: Identify actual empty state message and selectors
2. **Enhanced Error Handling**: Add more specific exception types
3. **Parallel Execution**: Configure TestNG for parallel test execution
4. **CI/CD Integration**: Add to GitHub Actions workflow
5. **Reporting**: Enhance Allure reports with custom metrics

## Support

For questions or issues:
1. Check existing test patterns in the codebase
2. Review the project constitution in `.specify/memory/constitution.md`
3. Consult AGENTS.md for development guidelines
4. Check Allure reports for detailed test results

This quickstart guide provides everything needed to successfully implement the TC-008 alphabet filter validation while maintaining code quality and test reliability.