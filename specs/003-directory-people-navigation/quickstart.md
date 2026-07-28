# Quickstart: Directory People Navigation Testing

**Feature**: TC-003 Directory People Navigation Verification  
**Created**: 2026-07-25  
**Scope**: Local environment setup and test execution guide

---

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Local Environment Setup](#local-environment-setup)
3. [Running Tests Locally](#running-tests-locally)
4. [Pagination Testing Patterns](#pagination-testing-patterns)
5. [Troubleshooting](#troubleshooting)
6. [Quick Reference](#quick-reference)

---

## Prerequisites

### System Requirements

- **Operating System**: Windows, macOS, or Linux
- **Java**: JDK 17 or later
- **Maven**: 3.8.0 or later
- **Git**: Latest version (for repository cloning)

### Browser Requirements

- **Chrome**: Version 120+
- **Firefox**: Version 121+ (optional)
- **Safari**: macOS 14+ (optional)

### IDE (Recommended)

- IntelliJ IDEA 2023.3+
- Eclipse 2023-09+
- VS Code with Java Extensions

---

## Local Environment Setup

### Step 1: Clone Repository

```bash
git clone https://github.com/mandypepe/sdd_selenium.git
cd sdd_selenium
```

### Step 2: Verify Java Installation

```bash
java -version
# Expected: openjdk version "17.x.x" or later

javac -version
# Expected: javac 17.x.x or later
```

If Java is not installed, download from [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or use OpenJDK:
- **Windows**: `choco install openjdk17` (via Chocolatey)
- **macOS**: `brew install openjdk@17` (via Homebrew)
- **Linux**: `sudo apt-get install openjdk-17-jdk` (Debian/Ubuntu)

### Step 3: Verify Maven Installation

```bash
mvn -version
# Expected: Apache Maven 3.8.x or later

# If not installed:
# Windows: choco install maven
# macOS: brew install maven
# Linux: sudo apt-get install maven
```

### Step 4: Build the Project

```bash
# Navigate to project root
cd d:\studio\sdd_selenium  # Windows
# or
cd ~/projects/sdd_selenium  # macOS/Linux

# Clean and build
mvn clean install

# Expected Output:
# [INFO] BUILD SUCCESS
```

**Troubleshooting Build Issues**:
- If dependencies fail to resolve, check internet connection and Maven settings.
- Clear local cache: `mvn clean dependency:purge-local-repository install`

### Step 5: Verify Selenium & TestNG Setup

```bash
# Run a quick validation
mvn test -Dtest=com.project.tests.* -DskipTests

# This ensures all dependencies are downloaded and pom.xml is valid
```

---

## Running Tests Locally

### 1. Run All Tests

```bash
mvn clean test

# Expected: All tests pass; Allure results generated in target/allure-results/
```

### 2. Run Specific Test Class

```bash
mvn test -Dtest=com.project.tests.DirectoryPeopleNavigationTests

# Runs only DirectoryPeopleNavigationTests class
```

### 3. Run Specific Test Method

```bash
mvn test -Dtest=com.project.tests.DirectoryPeopleNavigationTests#test_navigateToPeopleAndViewRecords

# Runs only the specified test method
```

### 4. Run Tests in Parallel

```bash
mvn test -DthreadCount=4 -DparallelTest=true

# Runs tests on 4 parallel threads (TestNG parallel execution)
```

### 5. Run Tests with Specific Browser

```bash
# Via testng.xml parameter:
mvn test -Dbrowser=firefox

# Or modify testng.xml to specify browser:
# <parameter name="browser" value="firefox" />
```

### 6. Generate Allure Reports

```bash
# After tests complete:
mvn allure:report

# View report in browser:
mvn allure:serve

# Or manually:
allure serve target/allure-results/
```

---

## Pagination Testing Patterns

### Pattern 1: Single Page Validation

**Scenario**: Verify personnel list fits on a single page.

```java
@Test
public void test_singlePageDisplay() {
    DirectoryNavigationPage navPage = new DirectoryNavigationPage(DriverManager.getDriver());
    navPage.openDirectory();
    
    PeopleSectionPage peoplePage = navPage.selectPeopleSection();
    peoplePage.waitForRecordsToLoad();
    
    // Validate first page
    assertTrue(peoplePage.getRecordCount() > 0, "Should display personnel records");
    assertEquals(1, peoplePage.getCurrentPageNumber(), "Should start on page 1");
    
    // Validate pagination buttons are disabled (only one page)
    assertFalse(peoplePage.isPreviousPageButtonEnabled(), "Previous should be disabled on page 1");
    // Note: Next button depends on total record count
}
```

### Pattern 2: Multi-Page Navigation

**Scenario**: Navigate through multiple pages and validate data consistency.

```java
@Test
public void test_multiPageNavigation() {
    PeopleSectionPage peoplePage = new PeopleSectionPage(DriverManager.getDriver());
    peoplePage.openDirectory();
    peoplePage.selectPeopleSection();
    peoplePage.waitForRecordsToLoad();
    
    int totalPages = peoplePage.getTotalPages();
    
    if (totalPages > 1) {
        // Navigate to next page
        peoplePage.goToNextPage();
        assertEquals(2, peoplePage.getCurrentPageNumber(), "Should be on page 2");
        
        List<PersonnelRecord> page2Records = peoplePage.getVisibleRecords();
        assertTrue(page2Records.size() > 0, "Page 2 should have records");
        
        // Verify records are different from page 1
        // (Depends on test data setup)
    }
}
```

### Pattern 3: Pagination Boundary Validation

**Scenario**: Verify button states at page boundaries.

```java
@Test
public void test_paginationButtonStates() {
    PeopleSectionPage peoplePage = new PeopleSectionPage(DriverManager.getDriver());
    peoplePage.openDirectory();
    peoplePage.selectPeopleSection();
    peoplePage.waitForRecordsToLoad();
    
    // On page 1
    assertEquals(1, peoplePage.getCurrentPageNumber());
    assertFalse(peoplePage.isPreviousPageButtonEnabled(), "Previous disabled on page 1");
    
    int totalPages = peoplePage.getTotalPages();
    
    // Navigate to last page
    for (int i = 1; i < totalPages; i++) {
        peoplePage.goToNextPage();
    }
    
    assertEquals(totalPages, peoplePage.getCurrentPageNumber());
    assertFalse(peoplePage.isNextPageButtonEnabled(), "Next disabled on last page");
    assertTrue(peoplePage.isPreviousPageButtonEnabled(), "Previous enabled on last page");
}
```

### Pattern 4: Data-Driven Pagination

**Scenario**: Test pagination with multiple test datasets.

```java
@Test(dataProvider = "paginationData")
public void test_paginationDataDriven(int pageNumber, String expectedTitle) {
    PeopleSectionPage peoplePage = new PeopleSectionPage(DriverManager.getDriver());
    peoplePage.openDirectory();
    peoplePage.selectPeopleSection();
    peoplePage.waitForRecordsToLoad();
    
    peoplePage.navigateToPage(pageNumber);
    assertEquals(pageNumber, peoplePage.getCurrentPageNumber());
    
    PersonnelRecord record = peoplePage.getRecordByName(expectedTitle);
    assertNotNull(record, "Expected record should be on page " + pageNumber);
}

@DataProvider(name = "paginationData")
public Object[][] getPaginationData() {
    return new Object[][] {
        { 1, "Dr. María García López" },
        { 2, "José Martínez" },
        { 3, "Dr. Elena Rodríguez Pérez" }
    };
}
```

### Pattern 5: Loading State Validation

**Scenario**: Verify loading indicators appear and disappear correctly.

```java
@Test
public void test_loadingStateTransition() {
    PeopleSectionPage peoplePage = new PeopleSectionPage(DriverManager.getDriver());
    peoplePage.openDirectory();
    peoplePage.selectPeopleSection();
    
    // Loading state (may be brief)
    ReportLogger.log("Waiting for records to load...");
    peoplePage.waitForRecordsToLoad();
    
    // Records loaded
    List<PersonnelRecord> records = peoplePage.getVisibleRecords();
    assertTrue(records.size() > 0, "Records should be loaded");
    
    // Navigate to next page
    if (peoplePage.isNextPageButtonEnabled()) {
        peoplePage.goToNextPage();
        peoplePage.waitForRecordsToLoad();  // Wait for reload
        
        assertTrue(peoplePage.getRecordCount() > 0, "Next page should have records");
    }
}
```

---

## Troubleshooting

### Issue: Tests Fail with "Chrome driver not found"

**Solution**:
- WebDriverManager automatically downloads Chrome driver.
- Ensure internet connection is available on first run.
- If offline, pre-download driver from [ChromeDriver](https://chromedriver.chromium.org/).

### Issue: "No Personnel Records" on Page Load

**Possible Causes**:
- Test environment is down; check availability of test URL.
- Test data not loaded; verify test data exists in backend.
- Incorrect page URL in configuration; check `baseUrl` in `testng.xml`.

**Debug Steps**:
```bash
# 1. Check if page loads manually
curl -I https://directory-test.institution.edu/

# 2. Add screenshot to test to inspect state
takeScreenshot("people-section-failed.png");

# 3. Check browser console logs in Allure report
# (captured if TestListener is configured)

# 4. Increase wait timeout temporarily
WaitUtils.setTimeoutSeconds(15);  // Default: 10
```

### Issue: Tests are Flaky (Pass/Fail Randomly)

**Common Causes**:
- Fixed waits are too short; some pages load slower.
- Locators are fragile; UI changed slightly.
- Parallel tests interfere with each other; driver not properly isolated.

**Fixes**:
```java
// 1. Replace fixed waits with intelligent waits
// BAD:
Thread.sleep(5000);  // ❌

// GOOD:
WaitUtils.untilRecordsLoaded();  // ✅

// 2. Use stable locators (data-testid, not XPath indices)
// BAD:
By.xpath("//div[1]/div[2]/button[3]")  // ❌

// GOOD:
By.cssSelector("[data-testid='pagination-next']")  // ✅

// 3. Verify ThreadLocal driver isolation
// BAD:
public static WebDriver driver;  // ❌ Shared across threads

// GOOD:
private ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();  // ✅
```

### Issue: Pagination Buttons Not Responding

**Debug Steps**:
1. Take screenshot: `takeScreenshot("pagination-state.png")`
2. Verify button is enabled: `assertFalse(button.isDisplayed())`
3. Check if page content is ready: `peoplePage.waitForRecordsToLoad()`
4. Inspect HTML contract: confirm data-testid attributes are present

---

## Quick Reference

### Common Commands

| Task | Command |
|------|---------|
| **Build project** | `mvn clean install` |
| **Run all tests** | `mvn clean test` |
| **Run specific test class** | `mvn test -Dtest=DirectoryPeopleNavigationTests` |
| **Run tests in parallel (4 threads)** | `mvn test -DthreadCount=4 -DparallelTest=true` |
| **Generate Allure report** | `mvn allure:report` |
| **View Allure report** | `mvn allure:serve` |
| **Run tests with verbose logging** | `mvn test -X` |
| **Skip tests during build** | `mvn clean install -DskipTests` |

### Configuration Files

| File | Purpose | Location |
|------|---------|----------|
| **testng.xml** | TestNG suite configuration | `./testng.xml` |
| **pom.xml** | Maven dependencies & plugins | `./pom.xml` |
| **constitution.md** | Architectural principles | `./.specify/memory/constitution.md` |
| **spec.md** | Feature specification | `./specs/003-directory-people-navigation/spec.md` |

### Key Directories

| Directory | Contents |
|-----------|----------|
| `src/main/java/com/project/pages/` | Page Object classes |
| `src/main/java/com/project/drivers/` | DriverFactory, DriverManager |
| `src/main/java/com/project/utils/` | WaitUtils, ReportLogger |
| `src/test/java/com/project/tests/` | Test classes |
| `src/test/java/com/project/data/` | Test data providers |
| `target/allure-results/` | Test reports (Allure) |
| `specs/003-directory-people-navigation/` | Feature documentation |

### Key Methods (Page Objects)

| Method | Usage |
|--------|-------|
| `DirectoryNavigationPage.openDirectory()` | Load directory homepage |
| `DirectoryNavigationPage.selectPeopleSection()` | Click "People" navigation |
| `PeopleSectionPage.waitForRecordsToLoad()` | Wait for data to render |
| `PeopleSectionPage.getVisibleRecords()` | Get list of records |
| `PeopleSectionPage.goToNextPage()` | Navigate to next page |
| `PeopleSectionPage.goToPreviousPage()` | Navigate to previous page |
| `PaginationComponent.getCurrentPageNumber()` | Get current page |

### Key Utilities

| Utility | Usage |
|---------|-------|
| `DriverManager.getDriver()` | Get thread-bound driver |
| `DriverManager.setDriver(driver)` | Set thread-bound driver |
| `DriverManager.quitDriver()` | Close driver and cleanup |
| `WaitUtils.untilVisible(element)` | Wait for element visibility |
| `WaitUtils.untilRecordsLoaded()` | Custom condition for personnel data |
| `ReportLogger.log(message)` | Log step for Allure report |
| `takeScreenshot(name)` | Capture screenshot for debugging |

---

## Next Steps

### After First Run

1. **Review Allure Reports**: `mvn allure:serve` to view detailed test reports with screenshots and logs.
2. **Inspect Test Logs**: Check `target/surefire-reports/` for raw TestNG logs.
3. **Verify Page Objects**: Confirm page object methods match your application's structure.
4. **Update Test Data**: If test data differs from example sets, update `PersonnelDataProvider` in `src/test/java/com/project/data/`.

### Debugging Failed Tests

1. **Take Screenshot**: Automatically captured by TestListener on failure (visible in Allure).
2. **Check Logs**: ReportLogger output shows step-by-step execution.
3. **Inspect Locators**: Verify data-testid attributes exist in actual HTML (use browser DevTools).
4. **Run Locally with Breakpoints**: Use IDE debugger to step through page object methods.

### Extend Pagination Testing

1. **Add More Test Scenarios**: Extend test class with additional data providers.
2. **Test Additional Browsers**: Configure Firefox/Safari in testng.xml; extend DriverFactory.
3. **Add Performance Metrics**: Capture page load time and record fetch duration; assert within SLA.
4. **Test Edge Cases**: Empty results, very large datasets, network latency simulation.

---

## References

- **Spec Kit Feature Specification**: `specs/003-directory-people-navigation/spec.md`
- **Data Model**: `specs/003-directory-people-navigation/data-model.md`
- **Page Object Contracts**: `specs/003-directory-people-navigation/contracts/page-objects.md`
- **API/HTML Contracts**: `specs/003-directory-people-navigation/contracts/navigation-api.md`
- **Implementation Plan**: `specs/003-directory-people-navigation/plan.md`
- **Constitution (Principles)**: `.specify/memory/constitution.md`
- **Allure Docs**: https://docs.qameta.io/allure/
- **Selenium 4 Docs**: https://www.selenium.dev/documentation/

---

**Status**: Quickstart Guide Complete ✅  
**Last Updated**: 2026-07-25  
**Audience**: Automation Engineers, QA Analysts
