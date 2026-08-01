# Implementation Plan: People Directory Initial Load Verification

**Branch**: `feature/dir-001-people-directory-load` | **Date**: 2026-07-23 | **Spec**: [spec.md](file:///D:/studio/sdd_selenium/specs/001-people-directory-load/spec.md)

**Input**: Feature specification from `/specs/001-people-directory-load/spec.md`

## Summary
The goal of this implementation is to verify that the People Directory page of the University Portal (`https://www.uci.cu/index.php/directorio/personas`) loads correctly and renders the main title, active section header, and a default populated list of staff members with correct accents and text encoding. We will follow Page Object Model (POM) principles with an abstract `BasePage` to encapsulate driver operations and dynamic wait synchronization.

## Technical Context

**Language/Version**: Java 17

**Primary Dependencies**: Selenium Java 4.46.0, TestNG 7.8.0, Webdrivermanager 5.4.1, Allure 2.19.0

**Storage**: N/A (UI layer testing only)

**Testing**: TestNG

**Target Platform**: Desktop Chrome (Headless/Headful)

**Project Type**: Selenium E2E Automation Framework

**Performance Goals**: Time to render initial list < 3 seconds

**Constraints**: WebDriverWait-driven synchronization (max 10s default); 100% thread safety for parallel runs via ThreadLocal driver manager.

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- **Principle I (POM Abstraction)**: Page interactions isolated inside `DirectoryPage` and `BasePage`. Locators kept private.
- **Principle II (ThreadLocal)**: `DriverManager` is used to get the WebDriver thread instance.
- **Principle III (Test-First)**: Test scenarios defined in detail first before coding Page Object interactions.
- **Principle IV (Wait Synchronization)**: `WaitUtils` and custom WebDriverWait wrappers in `BasePage` will be used. No `Thread.sleep()`.
- **Principle V (Observability)**: Using `ReportLogger` to log steps and `TestListener` to capture screenshots on failures.

## Project Structure

### Documentation (this feature)

```text
specs/001-people-directory-load/
├── plan.md              # This file
├── research.md          # Live DOM analysis & character encoding checks
├── data-model.md        # Page models, component and record classes
├── quickstart.md        # How to execute the tests
└── contracts/
    └── README.md        # DOM structure selectors and value assertions contract
```

### Source Code (repository root)

```text
src/main/java/com/project/
├── drivers/
│   ├── DriverFactory.java
│   └── DriverManager.java
├── pages/
│   ├── BasePage.java            ← [NEW] Base Page Object for common waits/actions
│   ├── DirectoryPage.java       ← [NEW] Directory Page Object
│   └── components/
│       └── PersonList.java      ← [NEW] Person List element component wrapper
└── utils/
    ├── WaitUtils.java
    └── ReportLogger.java

src/test/java/com/project/
├── tests/
│   └── DirectoryTests.java      ← [NEW] TestNG Directory verification tests
├── tests/base/
│   └── BaseTest.java
├── listeners/
│   └── TestListener.java
└── data/
    └── DirectoryTestData.java   ← [NEW] Data parameters/expected names
```

**Structure Decision**: Single project Maven layout with custom package components matching existing codebase structure.

## Complexity Tracking

No constitutional gate violations detected.
