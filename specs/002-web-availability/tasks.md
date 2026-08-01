# Tasks — Web Application Availability and Health Check Verification

**Feature**: `002-web-availability`  
**Branch**: `feature/qa-tc002-web-availability`  
**Generated**: 2026-07-24  
**Source**: [spec.md](file:///D:/studio/sdd_selenium/specs/002-web-availability/spec.md) · [plan.md](file:///D:/studio/sdd_selenium/specs/002-web-availability/plan.md)

---

## Phase 1 — Setup

- [x] T001 [US1] Create `HealthCheckPage` extending `BasePage` with private locators for page title, directory content container, and error indicators. File: `src/main/java/com/project/pages/HealthCheckPage.java`
- [x] T002 [US1] Add `getPageTitle()`, `isDirectoryContentVisible()`, `isErrorIndicatorPresent()`, and `getBodyText()` public business methods to `HealthCheckPage`. File: `src/main/java/com/project/pages/HealthCheckPage.java`

## Phase 2 — Foundational

- [x] T003 Enhance `WaitUtils` with `untilTitleContains(String)` and `untilPresenceOfElement(By)` fluent-wait wrapper methods using `FluentWait` with `StaleElementReferenceException` tolerance. File: `src/main/java/com/project/utils/WaitUtils.java`
- [x] T004 Add `ReportLogger.attachScreenshot(WebDriver, String)` static method that captures PNG via `TakesScreenshot` and attaches to Allure. File: `src/main/java/com/project/utils/ReportLogger.java`
- [x] T005 Verify `TestListener.onTestFailure` captures and attaches screenshot using `DriverManager.getDriver()` and `ReportLogger.attachScreenshot`. File: `src/test/java/com/project/listeners/TestListener.java`

## Phase 3 — User Stories

### US1 — Validate Application Responsiveness and Content Rendering

- [x] T006 [US1] Create `HealthCheckTests` class extending `BaseTest` with `@Parameters({"baseUrl","browser"})` from `testng.xml`. File: `src/test/java/com/project/tests/HealthCheckTests.java`
- [x] T007 [US1] Implement test `verifyNoBlankScreenOrServerError` (AC-1.1): navigate to `baseUrl`, assert page title is not empty, assert `<body>` text length > 0, assert no error indicators (title does not contain "404", "500", "503", "Error"). File: `src/test/java/com/project/tests/HealthCheckTests.java`
- [x] T008 [US1] Implement test `verifyDirectoryContentVisible` (AC-1.2): assert `HealthCheckPage.isDirectoryContentVisible()` returns true, assert page contains expected directory heading text. File: `src/test/java/com/project/tests/HealthCheckTests.java`
- [x] T009 [US1] Implement test `verifyExplicitFailureOnUnresponsiveServer` (AC-1.3): navigate to intentionally invalid URL variant, assert test fails gracefully with a `TimeoutException` or error-indicator assertion, verify diagnostic log via `ReportLogger`. File: `src/test/java/com/project/tests/HealthCheckTests.java`

### US2 — Automatic Failure Diagnosis and Screenshot Reporting

- [x] T010 [US2] Implement test `verifyScreenshotCapturedOnFailure` (AC-2.1): trigger intentional assertion failure, verify `TestListener.onTestFailure` is invoked and screenshot attachment is logged. File: `src/test/java/com/project/tests/HealthCheckTests.java`

## Phase 4 — Polish

- [x] T011 Add `<test name="HealthCheckTests">` block in `testng.xml` with `baseUrl` and `browser` parameters, and set `parallel="methods" thread-count="10"` on the suite element. File: `testng.xml`
- [x] T012 Audit entire codebase for `Thread.sleep()` calls; replace any found with `WaitUtils` or `BasePage` wait methods. Scope: `src/`
- [x] T013 Remove placeholder test in `ApplicationNoBlankOrServerErrorTest` (now superseded by `HealthCheckTests`). File: `src/test/java/com/project/tests/ApplicationNoBlankOrServerErrorTest.java`
- [x] T014 Verify `mvn test` executes `HealthCheckTests` successfully with Allure report output and no `Thread.sleep()` in codebase.