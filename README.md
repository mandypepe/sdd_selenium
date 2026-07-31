# sdd_selenium
GitHub Spec Kit and Selenium

## sdd-selenium-framework (Generated Structure)

Example structure of a Selenium framework (Java + Maven + TestNG).

### Created Structure:

```
sdd-selenium-framework/
├── .github/workflows/         # CI/CD Pipelines (GitHub Actions)
├── config/                    # Configuration files
├── src/
│   ├── main/                  # Support: drivers, pages, utils
│   └── test/                  # Tests: tests, data, listeners
├── pom.xml                    # Dependency Manager (Maven)
├── testng.xml                 # TestNG Configuration
└── README.md                  # Instructions
```

### How to Tag Tests:

```java
@Test(groups = {"smoke"})
public void smokeTest() {
    // Test implementation
}

@Test(groups = {"regression"})
public void regressionTest() {
    // Test implementation
}
```

```java
import com.project.annotations.TestCategory;

@TestCategory({"smoke", "critical"})
@Test
public void miTest() {
    // tu código
}
```

### How to Run (Local):

1) Ensure you have Java 17 and Maven installed.
2) From the project root, run:

```bash
mvn test
```
```bash
mvn test -DtestCategory=smoke -Dheadless=true
```
```bash
mvn test -DtestCategory=regression -Dheadless=true
```
```bash
mvn test -Dheadless=true
```
```bash
mvn test -DtestCategory=critical -Dheadless=true
```

### Notes:

- The project uses WebDriverManager to automatically download drivers.
- Update `config/env.qa.properties` or `config/env.staging.properties` according to your environment.
- `testng.xml` is configured for test suite execution with parameters (`baseUrl`, `browser`).
- For detailed AI agent guidance, see `AGENTS.md` (English version).
- Current test base URL: `https://www.uci.cu/index.php/directorio/personas`

---

## Framework Component Overview

This diagram illustrates the general relationship between feature specifications, test classes, and page objects within the framework.

```mermaid
flowchart TD
    subgraph Specifications
        A["TC-001: Initial Page Load"]
        B["TC-002: Web Availability Check"]
        C["TC-003: People Navigation"]
        D["TC-006: Any Filter"]
        E["TC-008: Alphabet Index Filter"]
    end

    subgraph Test Classes
        F["PeopleDirectoryLoadTest.java"]
        G["HealthCheckTests.java"]
        H["PeopleSectionStaffListTest.java"]
        I["AnyFilterAndPaginationTest.java"]
        J["AlphabetFilterFullCoverageTest.java"]
    end

    subgraph Page Objects
        K["DirectoryPage.java"]
        L["HealthCheckPage.java"]
        M["PersonList.java"]
        N["BasePage.java"]
        O["AlphabetFilterComponent.java"]
        P["PeopleSectionPage.java"]
    end

    A --> F
    B --> G
    C --> H
    D --> I
    E --> J

    F --> K
    G --> L
    H --> K
    I --> K
    J --> K

    K --> M
    K --> N
    K --> O
    K --> P
    L --> N
    M --> N
    O --> N
    P --> N
```

---

## TC-002: Validate Application Responsiveness and Content Rendering Flow

This flowchart details the execution flow for the web availability health check test, demonstrating initialization, page loading, and content verification steps.

```mermaid
flowchart TD
    A[Start Test Execution] --> B["BaseTest.setUp: Initialize WebDriver & Navigate to Base URL"]
    B --> C{HealthCheckPage Loaded?}
    C -- Yes --> D["HealthCheckTests.verifyNoBlankScreenOrServerError"]
    D --> E{Page Title Not Empty & No Server Errors?}
    E -- Yes --> F["HealthCheckTests.verifyDirectoryContentVisible"]
    F --> G{Directory Content Visible?}
    G -- Yes --> H[Test Passed]
    C -- No --> I[Test Failed: Page Not Loaded]
    E -- No --> J[Test Failed: Blank Screen or Server Error]
    G -- No --> K[Test Failed: Directory Content Not Visible]
    H --> L[End Test Execution]
    I --> L
    J --> L
    K --> L
```

---

## TC-001: Initial Page Load and Directory Display Flow

This flowchart details the execution flow for the people directory initial load verification, demonstrating navigation, title/header verification, and initial list population.

```mermaid
flowchart TD
    A[Start Test: Initial Page Load] --> B["Navigate to Directory URL"]
    B --> C{Page Loaded Successfully?}
    C -- Yes --> D["Verify Main Title and Personas Header"]
    D --> E{Title and Header Visible?}
    E -- Yes --> F["Verify Default List of People is Visible"]
    F --> G{People List Displayed?}
    G -- Yes --> H[Test Passed]
    C -- No --> I[Test Failed: Page Load Error]
    E -- No --> J[Test Failed: Title/Header Missing]
    G -- No --> K[Test Failed: People List Not Visible]
    H --> L[End Test]
    I --> L
    J --> L
    K --> L
```

---

## TC-003: Validate Directory People Navigation and Data Rendering Flow

This flowchart details the execution flow for validating the "People" section navigation, demonstrating visibility, clicking, and content display.

```mermaid
flowchart TD
    A[Start Test: People Navigation] --> B["Navigate to Directory Page"]
    B --> C["Verify People Option is Visible and Accessible"]
    C --> D{Is People Option Visible?}
    D -- Yes --> E["Click People Option"]
    E --> F{People Section Loads?}
    F -- Yes --> G["Verify List of Personnel Records is Displayed"]
    G --> H{Personnel List Visible?}
    H -- Yes --> I["Verify Each Record Displays Full Name"]
    I --> J["Verify Role/Title Displayed for Individuals"]
    J --> K[Test Passed]
    D -- No --> L[Test Failed: People Option Missing]
    F -- No --> M[Test Failed: Section Not Loaded]
    H -- No --> N[Test Failed: Personnel List Not Visible]
    K --> O[End Test]
    L --> O
    M --> O
    N --> O
```

---

## TC-006: View General Directory Listing Flow

This flowchart details the execution flow for validating the "Any" alphabetical filter with pagination, demonstrating filter selection, result display, and pagination functionality.

```mermaid
flowchart TD
    A[Start Test: Any Filter] --> B["Navigate to Directory Page"]
    B --> C["Select Any Filter Option"]
    C --> D{General List Displayed?}
    D -- Yes --> E["Verify Results Are Not Blank/Empty"]
    E --> F{Results Not Empty?}
    F -- Yes --> G["Verify Pagination Controls are Functional"]
    G --> H{Pagination Works?}
    H -- Yes --> I[Test Passed]
    D -- No --> J[Test Failed: List Not Displayed]
    F -- No --> K[Test Failed: Results Empty]
    H -- No --> L[Test Failed: Pagination Broken]
    I --> M[End Test]
    J --> M
    K --> M
    L --> M
```

---

## TC-008: Directory Alphabetical Index Filter Validation

This diagram illustrates the comprehensive architecture and relationships for the Spanish alphabet filter validation feature, supporting all 27 characters (A-Z + Ñ) with performance monitoring and UTF-8 encoding support.

```mermaid
flowchart TD
    subgraph "TC-008 Specification Layer"
        A["spec.md: 27 Spanish Alphabet Support"]
        B["User Stories US1-US3"]
        C["Functional Requirements FR-001 to FR-006"]
        D["Success Criteria SC-001 to SC-004"]
    end

    subgraph "Test Implementation Layer"
        E["AlphabetFilterFullCoverageTest.java"]
        F["AlphabetDataProvider.java"]
        G["Performance & Encoding Utils"]
    end

    subgraph "Page Object Layer"
        H["DirectoryPage.java"]
        I["AlphabetFilterComponent.java"]
        J["PersonList.java"]
        K["PeopleSectionPage.java"]
    end

    subgraph "Utility & Support Layer"
        L["PerformanceUtils.java"]
        M["EncodingUtils.java"]
        N["AlphabetValidationReporter.java"]
        O["WaitUtils.java"]
        P["BasePage.java"]
    end

    A --> E
    B --> E
    C --> E
    D --> E

    E --> F
    E --> G
    E --> H

    H --> I
    H --> J
    H --> K

    I --> L
    I --> M
    J --> N
    J --> O
    K --> P

    F --> I
    G --> L
    G --> M
```

---

## TC-008: Complete Alphabet Validation Flow

This flowchart details the execution flow for validating all 27 Spanish alphabet characters with performance monitoring, empty state handling, and UTF-8 encoding support.

```mermaid
flowchart TD
    A["Start TC-008: Alphabet Filter Validation"] --> B["BaseTest.setUp: Initialize WebDriver"]
    B --> C["Navigate to Directory Page"]
    C --> D{Alphabet Filter Available?}
    
    D -- No --> E["Framework Validation Mode"]
    E --> F["Validate Framework Capabilities"]
    F --> G["Generate Framework Report"]
    G --> H["End Test"]
    
    D -- Yes --> I["Start Performance Monitoring"]
    I --> J["Loop Through 27 Spanish Letters A-Z + Ñ"]
    
    J --> K["Select Letter with UTF-8 Encoding"]
    K --> L{Letter Clickable?}
    
    L -- No --> M["Record Empty State"]
    M --> N{Empty State Message Displayed?}
    N -- Yes --> O["Record Success for Empty State"]
    N -- No --> P["Record Failure: Missing Empty Message"]
    
    L -- Yes --> Q["Click Letter with Retry Logic"]
    Q --> R{Results Loaded?}
    
    R -- No --> S["Record Failure: No Results"]
    R -- Yes --> T["Validate Results Match Letter"]
    T --> U{Results Valid?}
    
    U -- Yes --> V["Record Success"]
    U -- No --> W["Record Failure: Invalid Results"]
    
    O --> X{More Letters to Test?}
    P --> X
    S --> X
    V --> X
    W --> X
    
    X -- Yes --> Y["Check Performance Target <10s per letter"]
    Y --> J
    
    X -- No --> Z["Generate Comprehensive Report"]
    Z --> AA{Total Time <3 minutes?}
    AA -- Yes --> BB["Test Passed: All Criteria Met"]
    AA -- No --> CC["Test Failed: Performance Issue"]
    
    BB --> DD["End Test"]
    CC --> DD
    H --> DD
```