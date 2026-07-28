# Contract: Page Object Model Structure

**Feature**: TC-003 Directory People Navigation Verification  
**Created**: 2026-07-25  
**Type**: Page Object Architecture Contract

---

## Overview

This document defines the Page Object Model (POM) structure and public interfaces for the Directory People Navigation feature. All Selenium WebDriver mechanics are encapsulated within these classes; test classes consume only public business methods.

---

## Page Object Hierarchy

```
BasePage (abstract)
├── DirectoryNavigationPage
│   └── selectPeopleSection() → PeopleSectionPage
└── PeopleSectionPage
    ├── [composition] PaginationComponent
    └── [composition] PersonnelRecord (value object)
```

---

## BasePage (Abstract Base Class)

### Purpose

Encapsulates common WebDriver operations and wait utilities used by all page objects.

### Protected Methods (for subclass use only)

```java
public abstract class BasePage {
    // Element Discovery & Interaction
    protected WebElement findElement(By locator);
    protected List<WebElement> findElements(By locator);
    protected void clickElement(WebElement element);
    protected void sendKeys(WebElement element, String text);
    protected String getText(WebElement element);
    
    // Wait & Synchronization
    protected void waitForElementVisible(WebElement element);
    protected void waitForElementClickable(WebElement element);
    protected void waitForUrlContains(String urlFragment);
    protected void waitForLoadingIndicatorGone();
    protected void waitForRecordsToLoad();
    
    // Navigation
    protected void navigateTo(String url);
    protected String getCurrentUrl();
    
    // Utilities
    protected void takeScreenshot(String name);
    protected void logStep(String message);
}
```

### Constructor

```java
public BasePage(WebDriver driver) {
    this.driver = driver;
}
```

### Notes

- All methods are `protected` (not `private`), allowing subclasses to reuse common logic.
- Subclasses override common methods as needed for specific behavior.
- All By locators are `private` to subclasses; external code never accesses locators directly.

---

## DirectoryNavigationPage

### Purpose

Represents the Directory main navigation page. Provides business methods for navigating to the "People" section.

### Public Methods (consumed by tests)

```java
public class DirectoryNavigationPage extends BasePage {
    
    /**
     * Navigates to the directory homepage.
     * Assumes baseUrl is configured in configuration.
     */
    public void openDirectory() { ... }
    
    /**
     * Verifies the "People" navigation option is visible.
     * @return true if "People" option is visible; false otherwise
     */
    public boolean isPeopleOptionVisible() { ... }
    
    /**
     * Clicks the "People" navigation option and waits for the PeopleSectionPage to load.
     * @return PeopleSectionPage instance for chaining business workflows
     */
    public PeopleSectionPage selectPeopleSection() { ... }
    
    /**
     * Gets all available navigation options (e.g., ["People", "Institutions", "Departments"]).
     * @return List of navigation option labels
     */
    public List<String> getAvailableNavigationOptions() { ... }
    
    /**
     * Gets the currently active section (e.g., "People").
     * @return Active section label
     */
    public String getActiveSection() { ... }
}
```

### Private Fields (Locators)

```java
private static final By PEOPLE_NAV_LINK = By.cssSelector("[data-testid='nav-people']");
private static final By INSTITUTIONS_NAV_LINK = By.cssSelector("[data-testid='nav-institutions']");
private static final By DEPARTMENTS_NAV_LINK = By.cssSelector("[data-testid='nav-departments']");
private static final By NAVIGATION_CONTAINER = By.id("directory-navigation");
```

### Usage Example

```java
@Test
public void testNavigateToPeopleSection() {
    DirectoryNavigationPage navPage = new DirectoryNavigationPage(DriverManager.getDriver());
    navPage.openDirectory();
    
    assertTrue(navPage.isPeopleOptionVisible(), "People option should be visible");
    
    PeopleSectionPage peoplePage = navPage.selectPeopleSection();
    // Continue workflow with PeopleSectionPage
}
```

---

## PeopleSectionPage

### Purpose

Represents the Directory People section. Provides business methods for interacting with the personnel list and pagination.

### Public Methods (consumed by tests)

```java
public class PeopleSectionPage extends BasePage {
    
    /**
     * Waits for personnel records to load (handles both SSR and AJAX).
     * Throws TimeoutException if records do not appear within default timeout.
     */
    public void waitForRecordsToLoad() { ... }
    
    /**
     * Gets all visible personnel records on the current page.
     * @return List of PersonnelRecord objects representing current page records
     */
    public List<PersonnelRecord> getVisibleRecords() { ... }
    
    /**
     * Gets the count of visible personnel records on the current page.
     * @return Number of records visible
     */
    public int getRecordCount() { ... }
    
    /**
     * Finds a personnel record by full name.
     * @param name Full name to search for (case-sensitive)
     * @return PersonnelRecord if found; null if not found
     */
    public PersonnelRecord getRecordByName(String name) { ... }
    
    /**
     * Gets the current page number.
     * @return 1-indexed page number
     */
    public int getCurrentPageNumber() { ... }
    
    /**
     * Gets the total number of pages.
     * @return Total pages
     */
    public int getTotalPages() { ... }
    
    /**
     * Navigates to a specific page by clicking the numbered page button.
     * @param pageNumber 1-indexed page number
     */
    public void navigateToPage(int pageNumber) { ... }
    
    /**
     * Clicks the Next button to navigate to the next page.
     * Waits for page to reload and records to appear.
     * Throws exception if already on last page or Next button is disabled.
     */
    public void goToNextPage() { ... }
    
    /**
     * Clicks the Previous button to navigate to the previous page.
     * Waits for page to reload and records to appear.
     * Throws exception if already on page 1 or Previous button is disabled.
     */
    public void goToPreviousPage() { ... }
    
    /**
     * Checks if the Next page button is enabled.
     * @return true if Next button is enabled; false if disabled
     */
    public boolean isNextPageButtonEnabled() { ... }
    
    /**
     * Checks if the Previous page button is enabled.
     * @return true if Previous button is enabled; false if disabled
     */
    public boolean isPreviousPageButtonEnabled() { ... }
    
    /**
     * Checks if there are any personnel records on the current page.
     * @return true if records are present; false if empty
     */
    public boolean hasRecords() { ... }
    
    /**
     * Gets the data-testid of the personnel list container for advanced debugging.
     * @return CSS selector or XPath for the list (primarily for test logging)
     */
    public String getPersonnelListLocator() { ... }
}
```

### Private Fields (Locators)

```java
private static final By PEOPLE_SECTION = By.cssSelector("[data-section='people']");
private static final By PERSONNEL_LIST = By.cssSelector("[data-testid='personnel-list']");
private static final By PERSONNEL_RECORDS = By.cssSelector("[data-testid='personnel-record']");
private static final By PAGINATION_CONTROLS = By.cssSelector("[data-testid='pagination-controls']");
private static final By LOADING_INDICATOR = By.cssSelector("[aria-label='Loading personnel records']");
```

### Composition

```java
private PaginationComponent paginationComponent;

public PeopleSectionPage(WebDriver driver) {
    super(driver);
    this.paginationComponent = new PaginationComponent(driver);
}
```

### Usage Example

```java
@Test
public void testMultiPageNavigation() {
    PeopleSectionPage peoplePage = new PeopleSectionPage(DriverManager.getDriver());
    peoplePage.waitForRecordsToLoad();
    
    int page1Count = peoplePage.getRecordCount();
    assertTrue(page1Count > 0, "Page 1 should have records");
    
    assertEquals(1, peoplePage.getCurrentPageNumber(), "Should start on page 1");
    
    if (peoplePage.isNextPageButtonEnabled()) {
        peoplePage.goToNextPage();
        int page2Count = peoplePage.getRecordCount();
        assertEquals(2, peoplePage.getCurrentPageNumber(), "Should be on page 2");
    }
}
```

---

## PaginationComponent

### Purpose

Reusable component encapsulating pagination control logic. Used by PeopleSectionPage and potentially other pages requiring pagination.

### Public Methods

```java
public class PaginationComponent {
    
    /**
     * Gets the current page number.
     * @return 1-indexed page number
     */
    public int getCurrentPageNumber() { ... }
    
    /**
     * Gets the total number of pages.
     * @return Total pages
     */
    public int getTotalPages() { ... }
    
    /**
     * Navigates to the next page.
     * Assumes Next button is enabled.
     */
    public void goToNextPage() { ... }
    
    /**
     * Navigates to the previous page.
     * Assumes Previous button is enabled.
     */
    public void goToPreviousPage() { ... }
    
    /**
     * Navigates to a specific page by clicking the numbered page button.
     * @param pageNumber 1-indexed page number
     */
    public void goToPage(int pageNumber) { ... }
    
    /**
     * Checks if the Next button is enabled.
     * @return true if enabled; false if disabled
     */
    public boolean isNextPageEnabled() { ... }
    
    /**
     * Checks if the Previous button is enabled.
     * @return true if enabled; false if disabled
     */
    public boolean isPreviousPageEnabled() { ... }
}
```

### Private Fields (Locators)

```java
private static final By PAGINATION_PREV_BUTTON = By.cssSelector("[data-testid='pagination-prev']");
private static final By PAGINATION_NEXT_BUTTON = By.cssSelector("[data-testid='pagination-next']");
private static final By PAGINATION_PAGE_BUTTON = By.cssSelector("[data-testid='pagination-page-%d']");
private static final By CURRENT_PAGE_SPAN = By.id("current-page");
private static final By TOTAL_PAGES_SPAN = By.id("total-pages");
```

### Constructor

```java
public PaginationComponent(WebDriver driver) {
    this.driver = driver;
}
```

### Notes

- Component is composable and can be reused in other page objects with pagination.
- Component methods are public; clients can call pagination logic directly.
- Synchronization (waits) handled by PeopleSectionPage after navigation; component focuses on DOM interaction.

---

## PersonnelRecord (Value Object)

### Purpose

Represents a single personnel record displayed in the list. Not a Page Object; a lightweight data transfer object.

### Public Properties

```java
public class PersonnelRecord {
    private String id;
    private String fullName;
    private String roleTitle;
    private String department;      // Optional
    private String email;           // Optional
    private String officeLocation;  // Optional
    
    // Getters (no setters; immutable)
    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public String getRoleTitle() { return roleTitle; }
    public String getDepartment() { return department; }
    public String getEmail() { return email; }
    public String getOfficeLocation() { return officeLocation; }
    
    @Override
    public String toString() {
        return String.format("PersonnelRecord{name=%s, role=%s}", fullName, roleTitle);
    }
}
```

### Factory Method (in PeopleSectionPage or utility)

```java
// Factory method to create PersonnelRecord from WebElement (internal to page object)
private PersonnelRecord createPersonnelRecordFromElement(WebElement recordElement) {
    String id = recordElement.getAttribute("data-id");
    String fullName = recordElement.findElement(By.cssSelector("[data-testid='personnel-name']")).getText();
    String roleTitle = recordElement.findElement(By.cssSelector("[data-testid='personnel-role']")).getText();
    String department = recordElement.findElement(By.cssSelector("[data-testid='personnel-department']")).getText();
    
    return new PersonnelRecord(id, fullName, roleTitle, department);
}
```

### Usage Example

```java
@Test
public void testPersonnelRecordDisplay() {
    PeopleSectionPage peoplePage = new PeopleSectionPage(DriverManager.getDriver());
    peoplePage.waitForRecordsToLoad();
    
    List<PersonnelRecord> records = peoplePage.getVisibleRecords();
    
    for (PersonnelRecord record : records) {
        assertNotNull(record.getFullName(), "Name should not be null");
        assertNotNull(record.getRoleTitle(), "Role should not be null");
        assertTrue(record.getFullName().length() > 0, "Name should not be empty");
    }
}
```

---

## Design Principles Applied

### 1. **Encapsulation**
- All By locators are `private` to page objects.
- Only public business methods exposed to tests.
- Tests never interact with WebDriver or locators directly.

### 2. **Single Responsibility**
- `DirectoryNavigationPage`: Navigation only.
- `PeopleSectionPage`: Personnel list display and interaction.
- `PaginationComponent`: Pagination controls.
- `PersonnelRecord`: Data representation.

### 3. **Composition over Inheritance**
- `PeopleSectionPage` *composes* `PaginationComponent` instead of inheriting pagination logic.
- Allows flexible reuse of pagination in other contexts.

### 4. **Thread Safety**
- Page objects are lightweight and stateless; no shared mutable state.
- Each test gets its own WebDriver instance (via DriverManager) and its own page object instances.
- No static fields or class-level state.

### 5. **Readability**
- Method names are business-focused ("selectPeopleSection", "goToNextPage").
- No WebDriver syntax in tests; tests read like plain English scenarios.

---

## Integration with Test Classes

### BaseTest Framework

```java
public abstract class BaseTest {
    protected WebDriver driver;
    
    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.createDriver(getBrowserType());
        DriverManager.setDriver(driver);
    }
    
    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
    }
    
    protected abstract String getBrowserType();
}
```

### Test Class Example

```java
public class DirectoryPeopleNavigationTests extends BaseTest {
    
    @Test
    public void test_navigateToPeopleAndViewRecords() {
        // Setup
        DirectoryNavigationPage navPage = new DirectoryNavigationPage(DriverManager.getDriver());
        
        // Navigate
        navPage.openDirectory();
        assertTrue(navPage.isPeopleOptionVisible(), "People option should be visible");
        
        // Switch to People section
        PeopleSectionPage peoplePage = navPage.selectPeopleSection();
        
        // Validate
        peoplePage.waitForRecordsToLoad();
        assertTrue(peoplePage.hasRecords(), "People section should display records");
        
        List<PersonnelRecord> records = peoplePage.getVisibleRecords();
        for (PersonnelRecord record : records) {
            assertNotNull(record.getFullName(), "Name should not be null");
            assertNotNull(record.getRoleTitle(), "Role should not be null");
        }
    }
    
    @Override
    protected String getBrowserType() {
        return "chrome";
    }
}
```

---

## Anti-Patterns to Avoid (as per Constitution)

| Anti-Pattern | Why Bad | How POM Prevents It |
|--------------|---------|-------------------|
| Tests accessing locators | Couples tests to UI structure | Page objects hide all locators (private) |
| Static driver references | Breaks parallel execution | Each test gets its own driver via DriverManager |
| Hard-coded waits (`Thread.sleep`) | Slow, fragile, masks instability | BasePage provides intelligent wait methods |
| Test data in test code | Difficult to maintain, reuse | Data externalized; PersonnelRecord represents data only |
| Monolithic page objects | Difficult to read, maintain, reuse | Composed from smaller components (PaginationComponent) |

---

## Validation Checklist

- [ ] All By locators are `private`.
- [ ] All public methods in page objects are business-focused.
- [ ] No WebDriver code in test classes.
- [ ] No static fields in page objects; thread-safe initialization only.
- [ ] Tests use `DriverManager.getDriver()`, never cache driver locally.
- [ ] All waits use WebDriverWait + ExpectedConditions (no `Thread.sleep`).
- [ ] Page objects compose reusable components (PaginationComponent).
- [ ] Constructor accepts WebDriver only (no static initialization).
- [ ] Test data represented as value objects (PersonnelRecord), not page objects.

---

**Status**: POM Contract Defined ✅  
**Next Phase**: Implementation (create actual Java classes in src/main/java/com/project/pages/)
