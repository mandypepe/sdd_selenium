# Tasks: Directory People Navigation Verification

**Feature**: TC-003 Directory People Navigation Verification  
**Branch**: `feature/qa-003-directory-people-navigation`  
**Created**: 2026-07-25  
**Specification**: `specs/003-directory-people-navigation/spec.md`  
**Implementation Plan**: `specs/003-directory-people-navigation/plan.md`

---

## Overview

This task list defines the actionable implementation steps for the Directory People Navigation feature, a comprehensive UI test suite validating personnel discovery and pagination in an institutional directory.

**Key Characteristics**:
- **Scope**: Navigation discovery, personnel list rendering, pagination validation with edge cases
- **Technology Stack**: Java 17, Maven 3.8+, Selenium 4.46, TestNG 7.8, Allure 2.19, WebDriverManager 5.4.1
- **Testing Methodology**: TDD (Test-Driven Development), Page Object Model, ThreadLocal driver management, data-driven parameterization
- **Parallel Execution**: 1–4 concurrent threads with ThreadLocal driver isolation
- **Reporting**: Allure 2.x with structured logs and screenshot attachments

**Phases**:
1. **Phase 1: Setup** (T001–T003) — Environment validation & initialization
2. **Phase 2: Foundational** (T004–T010) — Infrastructure & base abstractions (ThreadLocal drivers, BasePage, utilities)
3. **Phase 3: User Story 1** (T011–T025) — Feature implementation & testing (navigation pages, pagination, test scenarios)
4. **Phase 4: Polish** (T026–T028) — Anti-pattern validation & documentation

**Estimated Duration**: 8–10 business days (distributed across phases)

**Success Criteria**:
- All 7 functional requirements (FR-001 to FR-007) mapped to implementation tasks
- All 4 acceptance scenarios (AS1–AS4) have dedicated test tasks
- All 4 edge cases covered by explicit test tasks
- All 7 anti-patterns have validation tasks
- Zero hard sleeps, fragile locators, or shared state
- 100% thread-safe parallel execution (4 threads tested)
- Allure reports capture steps, screenshots, and logs

---

## Phase 1: Setup (T001–T003)

**Goal**: Validate project structure and environment prerequisites are met.

**Independent Test**: `mvn clean verify` succeeds; pom.xml dependencies resolve; testng.xml is valid.

**Acceptance Criteria**:
- Java 17 is installed and configured
- Maven build succeeds with all dependencies resolved
- Project directory structure matches specification
- Configuration files (testng.xml, pom.xml, env.properties) are present and valid
- No broken imports or compilation errors

### Tasks

- [x] T001 Verify Java 17 installation and Maven 3.8+ availability; document versions in `README.md`

- [x] T002 Validate `pom.xml` dependencies (Selenium 4.46, TestNG 7.8, Allure 2.19, WebDriverManager 5.4.1); run `mvn dependency:tree` and confirm no conflicts in `pom.xml`

- [x] T003 Create/verify project directory structure: `src/main/java/com/project/{drivers,pages,utils}/`, `src/test/java/com/project/{tests,listeners,data}/`, `config/`, `specs/` in project root `d:\studio\sdd_selenium`

---

## Phase 2: Foundational (T004–T010)

**Goal**: Establish core framework infrastructure that all feature tests depend on.

**Blocking Dependencies**: Phase 2 must complete before Phase 3 user story tasks.

**Independent Test**: Each foundational component can be unit-tested independently; all components must pass thread-safety validation.

**Acceptance Criteria**:
- DriverManager provides thread-local WebDriver isolation
- BasePage encapsulates all WebDriver mechanics
- WaitUtils intelligently handles dynamic UI states without fixed sleeps
- ReportLogger structures all logs for Allure integration
- All utilities are thread-safe (tested with 4+ concurrent threads)
- No hard-coded timeouts, fragile locators, or shared state

### Tasks

- [x] T004 [P1] Create `src/main/java/com/project/drivers/DriverFactory.java` to instantiate WebDriver for Chrome, Firefox, Safari; support browser parameter from configuration; use WebDriverManager for automatic driver management (no manual binary setup); ensure no `Thread.sleep()` in factory in `src/main/java/com/project/drivers/DriverFactory.java`

- [x] T005 [P1] Create `src/main/java/com/project/drivers/DriverManager.java` with ThreadLocal<WebDriver> pattern: `setDriver(driver)`, `getDriver()`, `quitDriver()`; ensure thread isolation for parallel execution; document thread-safety guarantees in class JavaDoc in `src/main/java/com/project/drivers/DriverManager.java`

- [x] T006 [P1] Create `src/main/java/com/project/pages/BasePage.java` abstract class with protected methods: `findElement(By)`, `clickElement()`, `sendKeys()`, `getText()`, `waitForElementVisible()`, `waitForUrlContains()`, `takeScreenshot()`, `logStep()` using ReportLogger; encapsulate all WebDriver code; all locators private; all methods tested for thread-safety in `src/main/java/com/project/pages/BasePage.java`

- [x] T007 [P1] Extend `src/main/java/com/project/utils/WaitUtils.java` (or create if not exists) with custom ExpectedConditions: `untilRecordsLoaded()` (polls for personnel records in DOM), `untilLoadingIndicatorGone()` (waits for spinner to disappear), `untilUrlContains(String fragment)` (validates URL navigation), `untilElementClickable(By)` (explicit wait before click); default timeout 10 seconds; no `Thread.sleep()` calls in `src/main/java/com/project/utils/WaitUtils.java`

- [x] T008 [P1] Create/extend `src/main/java/com/project/utils/ReportLogger.java` with `log(String message)` method that writes to both stdout (SLF4J) and Allure step attachment; ensure thread-safe logging (ThreadLocal if needed); verify logs appear in Allure reports in `src/main/java/com/project/utils/ReportLogger.java`

- [x] T009 Create `src/test/java/com/project/tests/base/BaseTest.java` abstract class with @BeforeMethod/@AfterMethod lifecycle: setUp() calls `DriverFactory.createDriver()` and `DriverManager.setDriver(driver)`, tearDown() calls `DriverManager.quitDriver()`; handle exceptions gracefully; ensure no test state leaks in `src/test/java/com/project/tests/base/BaseTest.java`

- [x] T010 [P] Create/configure `testng.xml` with parallel execution settings: `parallel="tests"`, `thread-count="4"` (or auto-detect CPU count); register TestListener for failure capture; verify configuration is valid in `testng.xml`

---

## Phase 3: User Story 1 (T011–T025)

**User Story**: "As an end user accessing the directory, I want to easily locate and view the "People" section so that I can identify staff members by their names, roles, and titles." **(Priority: P1)**

**Goal**: Implement navigation, personnel list display, and pagination UI testing for the "People" section.

**Independent Test**: User Story 1 can be fully tested in isolation; personnel data must be pre-populated in test environment; no other directory sections (Institutions, Departments) required.

**Acceptance Criteria**:
- Navigation discovery: "People" option visible and clickable (AS1 → T011)
- Routing: Navigating to People section loads correct page without errors (AS2 → T012)
- List rendering: Personnel records display with name and role/title (AS3 → T013, AS4 → T014)
- Pagination: Navigation controls work correctly across first, middle, last pages (T015–T020)
- Edge cases: Empty results, special characters, boundary conditions (T021–T024)
- Anti-patterns: Zero hard sleeps, robust locators, thread-safe parallel execution (T026–T028)
- All Allure reports capture screenshots and logs at critical junctures

### Sub-Phase 3A: Page Object Implementation (T011–T014)

#### Acceptance Scenario 1 (AS1) – Navigation Visibility

**Scenario**: Given the user is on the main directory landing page, when they inspect the available navigation categories, then the "People" option must be clearly visible and accessible.

**Test Task**:

- [x] T011 [P1] [US1] Create `src/main/java/com/project/pages/DirectoryNavigationPage.java` extending BasePage with public methods: `openDirectory()` (navigates to base URL), `isPeopleOptionVisible()` (checks visibility of "People" link), `selectPeopleSection()` (clicks "People" and returns PeopleSectionPage), `getAvailableNavigationOptions()` (returns list of nav options); use stable locators (data-testid) per navigation-api.md contract; all locators private; validate implementation against page-objects.md contract in `src/main/java/com/project/pages/DirectoryNavigationPage.java`

#### Acceptance Scenario 2 (AS2) – Routing Without Errors

**Scenario**: Given the user has located and interacted with the "People" navigation option, when the directory page loads, then the system must successfully route to the "People" section without errors.

**Test Task**:

- [x] T012 [P1] [US1] Create `src/main/java/com/project/pages/PeopleSectionPage.java` extending BasePage with public methods: `waitForRecordsToLoad()` (uses WaitUtils.untilRecordsLoaded()), `getVisibleRecords()` (returns List<PersonnelRecord>), `getRecordCount()` (returns int), `getCurrentPageNumber()` (returns 1-indexed page), `getTotalPages()` (returns total), `navigateToPage(int pageNumber)`, `goToNextPage()`, `goToPreviousPage()`, `isNextPageButtonEnabled()`, `isPreviousPageButtonEnabled()`, `hasRecords()` (checks if records present); compose PaginationComponent; all locators private; validate against page-objects.md in `src/main/java/com/project/pages/PeopleSectionPage.java`

#### Acceptance Scenario 3 (AS3) – List Rendering

**Scenario**: Given the "People" section has loaded, when the user reviews the displayed content, then a list of personnel records must be rendered with each individual record containing at minimum their full name and organizational role or academic title.

**Test Task**:

- [x] T013 [P1] [US1] Create `src/main/java/com/project/pages/components/PaginationComponent.java` encapsulating pagination logic: `getCurrentPageNumber()`, `getTotalPages()`, `goToNextPage()`, `goToPreviousPage()`, `goToPage(int pageNumber)`, `isNextPageEnabled()`, `isPreviousPageEnabled()`; use stable locators per navigation-api.md; all locators private; ensure thread-safe (no shared state); validate against page-objects.md in `src/main/java/com/project/pages/components/PaginationComponent.java`

#### Acceptance Scenario 4 (AS4) – Data Display

**Scenario**: Given a personnel record is displayed in the list, when the user reviews the record content, then the person's name and role/title attributes must be visible and properly formatted without truncation or overlapping UI elements.

**Test Task**:

- [x] T014 [P1] [US1] Create `src/main/java/com/project/pages/components/PersonnelRecord.java` immutable value object with properties: id, fullName, roleTitle, department (optional), email (optional), officeLocation (optional); getters only (no setters); `toString()` for logging; factory method in PeopleSectionPage to create instances from WebElement in `src/main/java/com/project/pages/components/PersonnelRecord.java`

### Sub-Phase 3B: Test Data & Providers (T015–T016)

**Task**:

- [x] T015 Create `src/test/java/com/project/data/PersonnelDataProvider.java` with @DataProvider methods for parameterized tests: `paginationScenarios()` (page 1, page 2, page N), `specialCharactersScenarios()` (names/titles with accents, long names), `boundaryScenarios()` (single page, exact 2 pages, partial last page); load test data from JSON files in `src/test/resources/test-data/` per data-model.md in `src/test/java/com/project/data/PersonnelDataProvider.java`

- [x] T016 Create test data JSON files in `src/test/resources/test-data/`:
  - `personnel-single-page.json` (8 records, single page)
  - `personnel-multi-page.json` (65 records, 4 pages)
  - `personnel-boundary.json` (40 records, exactly 2 pages)
  - `personnel-special-chars.json` (10 records with Spanish characters, long titles)
  - `personnel-empty.json` (empty results scenario)
  
  Each file matches structure in data-model.md with PersonnelRecord fields and PaginationMetadata in `src/test/resources/test-data/`

### Sub-Phase 3C: Test Scenarios & Coverage (T017–T024)

**Functional Requirements Mapping**:
- FR-001: Navigation visibility → T011 (AS1)
- FR-002: Successful routing → T012 (AS2)
- FR-003: Data loading with indicators → T017, T018
- FR-004: Full name display without truncation → T013, T014 (AS3, AS4)
- FR-005: Role/title display → T013, T014 (AS3, AS4)
- FR-006: TDD & no regressions → T020–T025
- FR-007: Pagination support → T015–T020

**Test Tasks**:

- [x] T017 [P1] [US1] Create test method `test_navigateToPeopleAndViewRecords()` in `src/test/java/com/project/tests/DirectoryPeopleNavigationTests.java` (extends BaseTest): verify "People" option visible (AS1), click to navigate (AS2), validate records loaded within 3 seconds (SC-002), verify record count > 0, log each step with ReportLogger, capture screenshot of loaded section, assert no exceptions in `src/test/java/com/project/tests/DirectoryPeopleNavigationTests.java`

- [x] T018 [P1] [US1] Create test method `test_firstPageRecordsDisplay()` in DirectoryPeopleNavigationTests: navigate to People section, validate page 1 loaded, verify record count matches pageSize (or less if single page), extract first record, assert name and role/title are non-empty and visible without truncation (AS4), capture screenshot in `src/test/java/com/project/tests/DirectoryPeopleNavigationTests.java`

- [x] T019 [P1] [US1] Create test method `test_multiPageNavigation()` in DirectoryPeopleNavigationTests: navigate to page 1, verify Previous button disabled, click Next, verify page 2 loaded with different records, verify Previous button now enabled, validate record count, capture screenshots per page in `src/test/java/com/project/tests/DirectoryPeopleNavigationTests.java`

- [x] T020 [P1] [US1] Create test method `test_paginationBoundaryConditions()` in DirectoryPeopleNavigationTests using @DataProvider: test single-page scenario (Previous/Next both disabled), test exact 2-page boundary (page 2: exactly 20 records, Next disabled), test multi-page with partial last page (page 4: 5 records, Next disabled), validate pagination metadata per dataset, capture page transitions in `src/test/java/com/project/tests/DirectoryPeopleNavigationTests.java`

- [x] T021 [P1] [US1] Create test method `test_specialCharactersRendering()` in DirectoryPeopleNavigationTests: navigate to People section with special-chars test data, validate records with Spanish characters (ñ, á, é, etc.) render correctly, verify long titles wrap without truncation or breaking layout, capture screenshot of special-chars names, assert no encoding errors in `src/test/java/com/project/tests/DirectoryPeopleNavigationTests.java`

- [x] T022 [P] [US1] Create test method `test_firstToLastPageNavigation()` in DirectoryPeopleNavigationTests: navigate through all pages sequentially using Next button, track record IDs to ensure no duplicates between pages, on last page validate Next button disabled and Previous button enabled, verify total page count matches expected value from metadata in `src/test/java/com/project/tests/DirectoryPeopleNavigationTests.java`

- [x] T023 [P] [US1] Create test method `test_directPageJump()` in DirectoryPeopleNavigationTests: navigate to page 1, then directly jump to page 3 using numbered page button (if available in pagination UI), verify page 3 loaded with correct records, validate page number indicator matches clicked page in `src/test/java/com/project/tests/DirectoryPeopleNavigationTests.java`

- [x] T024 [P] [US1] Create test method `test_emptyResultsHandling()` (optional, if applicable): if test environment has empty results scenario, validate "No personnel records" message displays, verify pagination controls are hidden/disabled, confirm no JavaScript errors in browser console in `src/test/java/com/project/tests/DirectoryPeopleNavigationTests.java`

### Sub-Phase 3D: Advanced Test Coverage (T025)

**Task**:

- [x] T025 [P] [US1] Create test method `test_parallelExecutionThreadSafety()` in DirectoryPeopleNavigationTests: annotate as @Test and configure TestNG to run multiple parallel instances (4+ threads), each thread navigates to People section independently, verifies records loaded correctly, validates DriverManager provides thread-local isolation (no cross-contamination), confirm Allure reports show independent timelines per thread in `src/test/java/com/project/tests/DirectoryPeopleNavigationTests.java`

---

## Phase 4: Polish & Validation (T026–T028)

**Goal**: Validate anti-pattern compliance, framework stability, and documentation completeness.

**Independent Test**: Each validation task runs independently; all must pass before release.

**Acceptance Criteria**:
- Zero instances of `Thread.sleep()` in framework or tests
- All locators use stable attributes (data-testid, aria-label, stable IDs)
- Parallel execution (4 threads) produces no flakiness or cross-contamination
- Allure reports include screenshots and step logs
- Documentation complete and accurate
- Constitution compliance checklist passes

### Tasks

- [x] T026 [P1] [US1] Audit codebase for anti-pattern violations: scan `src/main/java/` and `src/test/java/` for `Thread.sleep()` calls using grep; scan for fragile XPath patterns (absolute paths, index-based selectors); verify all locators use stable attributes; document findings in `../../.idea/ANTI_PATTERN_AUDIT.md` in project root; run `mvn checkstyle:check` (or similar linting); report any violations and remediate before merge in `d:\studio\sdd_selenium\ANTI_PATTERN_AUDIT.md`

- [x] T027 [P1] [US1] Validate thread-safety by running full test suite on 4 concurrent threads: execute `mvn test -DthreadCount=4 -DparallelTest=true` (TestNG parallel mode); verify all tests pass without intermittent failures (run 3 times to confirm stability); check Allure reports show independent timelines per thread; verify no DriverManager state pollution; document thread-safety validation results in `../../.idea/THREAD_SAFETY_VALIDATION.md` in project root in `d:\studio\sdd_selenium\THREAD_SAFETY_VALIDATION.md`

- [x] T028 [P1] [US1] Generate final Allure report and validate completeness: run `mvn allure:report`, inspect `target/allure-results/` for screenshots and step logs, verify all test scenarios (T017–T025) have at least one passing run with attachments, confirm Navigation Page Object tests, Personnel List Page Object tests, and Pagination tests all produce reports with evidence, generate HTML report with `mvn allure:serve` and document report location in `../../.idea/ALLURE_REPORT_LOCATION.md` in `d:\studio\sdd_selenium\ALLURE_REPORT_LOCATION.md`


---

## Task Dependencies & Execution Order

```
Phase 1 (Setup): T001 → T002 → T003
     ↓
Phase 2 (Foundational): T004 → T005 → T006 → T007 → T008 → T009 → T010
     ↓
Phase 3 (User Story 1):
  Sub-Phase 3A (Page Objects):
    T011 → T012 → T013 → T014
     ↓
  Sub-Phase 3B (Test Data):
    T015 → T016 (can run in parallel with T011-T014 after T010 complete)
     ↓
  Sub-Phase 3C (Test Scenarios):
    T017 → T018 → T019 → T020 → T021 → T022 → T023 → T024
     ↓
  Sub-Phase 3D (Advanced):
    T025 (depends on T017–T024)
     ↓
Phase 4 (Polish): T026 → T027 → T028
```

**Parallelization Opportunities**:
- After T010: T015–T016 can run in parallel with T011–T014
- After T012–T013: T017–T025 tests are parallelizable (different test methods, independent data providers)
- T026–T028 run sequentially (gates for release)

---

## Immediate Executability Checklist

Each task includes:
- ✅ Clear acceptance criteria (testable outcomes)
- ✅ Exact file paths for all artifacts (source files, test files, resources)
- ✅ Dependencies explicitly stated (which task must complete first)
- ✅ No [NEEDS CLARIFICATION] markers
- ✅ Time estimate (small: <1hr, medium: 1–4hrs, large: >4hrs)
- ✅ Link to specification document (spec.md, plan.md, research.md, data-model.md, quickstart.md, contracts/)

### Estimated Time Breakdown

| Phase | Tasks | Duration | Effort |
|-------|-------|----------|--------|
| **Phase 1: Setup** | T001–T003 | 1–2 hours | Small |
| **Phase 2: Foundational** | T004–T010 | 6–8 hours | Medium–Large |
| **Phase 3A: Page Objects** | T011–T014 | 4–6 hours | Medium |
| **Phase 3B: Test Data** | T015–T016 | 1–2 hours | Small |
| **Phase 3C: Test Scenarios** | T017–T024 | 6–8 hours | Medium–Large |
| **Phase 3D: Advanced** | T025 | 2–3 hours | Medium |
| **Phase 4: Polish** | T026–T028 | 2–3 hours | Medium |
| **TOTAL** | T001–T028 | 22–32 hours | 3–5 days |

---

## Functional Requirements Coverage

| FR | Requirement | Task(s) | Acceptance Criteria |
|-----|------------|---------|-------------------|
| **FR-001** | Render "People" navigation option as visible & interactive | T011 | Navigation link visible on directory homepage |
| **FR-002** | Route to "People" section without errors | T012 | Page loads successfully after clicking; no JavaScript errors |
| **FR-003** | Retrieve & display personnel list with loading indicators | T017, T018 | Records appear within 3 seconds; loading spinner handled |
| **FR-004** | Display full name without truncation | T014, T018 | Name visible in full; no truncation or overflow |
| **FR-005** | Display role/title with dynamic length handling | T014, T018 | Role visible without truncation; wrapping works correctly |
| **FR-006** | Support TDD methodology; no regressions | T017–T025 | All test scenarios pass; existing features unaffected |
| **FR-007** | Support pagination (Next/Previous/numbered pages) | T015–T023 | All pagination controls functional; boundary states correct |

---

## Edge Cases & Anti-Pattern Coverage

| Scenario | Type | Task(s) | Validation |
|----------|------|---------|-----------|
| **Single-page data** | Edge Case | T020 | All records fit on page 1; pagination disabled |
| **Multi-page data** | Edge Case | T019, T020 | Next/Previous navigation works across pages |
| **Exact boundary (2 pages)** | Edge Case | T020 | Page 2 has exactly 20 records; Next disabled |
| **Partial last page** | Edge Case | T020, T022 | Last page has < 20 records; all visible |
| **Special characters** | Edge Case | T021 | Spanish characters (ñ, á, é) render correctly |
| **Empty results** | Edge Case | T024 | "No records" message displays gracefully |
| **Hard sleeps** | Anti-Pattern | T026 | Audit: zero `Thread.sleep()` in codebase |
| **Fragile locators** | Anti-Pattern | T026 | Audit: all locators use stable attributes (data-testid) |
| **Shared static state** | Anti-Pattern | T027 | Validation: ThreadLocal isolation verified; 4-thread tests pass |
| **Dependent tests** | Anti-Pattern | T025 | Parallel execution test: each thread independent |

---

## Success Criteria Summary

**MVP Scope** (Minimum Viable Product – Phase 1 + 2 + 3A + 3B + 3C):
- ✅ Setup complete: Java, Maven, dependencies verified (T001–T003)
- ✅ Foundational infrastructure: DriverManager, BasePage, WaitUtils, ReportLogger, BaseTest, TestNG config (T004–T010)
- ✅ Page Objects: DirectoryNavigationPage, PeopleSectionPage, PaginationComponent, PersonnelRecord (T011–T014)
- ✅ Test data: 5 datasets covering single-page, multi-page, boundaries, special chars, empty (T015–T016)
- ✅ Test scenarios: Navigation, rendering, pagination, boundaries, special characters (T017–T024)
- ✅ Time estimate: 3–5 business days

**Enhancement Scope** (Phase 3D + 4):
- ✅ Advanced: Parallel execution thread-safety validation (T025)
- ✅ Polish: Anti-pattern audit, thread-safety validation, Allure report (T026–T028)
- ✅ Time estimate: +1–2 business days (total: 4–7 business days for full scope)

---

## References & Artifacts

| Artifact | Location | Purpose |
|----------|----------|---------|
| **Feature Specification** | `specs/003-directory-people-navigation/spec.md` | Requirements, acceptance scenarios, edge cases |
| **Implementation Plan** | `specs/003-directory-people-navigation/plan.md` | Technical context, architecture decisions, project structure |
| **Technical Research** | `specs/003-directory-people-navigation/research.md` | Unknowns resolved, pagination strategy, driver management |
| **Data Model** | `specs/003-directory-people-navigation/data-model.md` | Entity definitions, test datasets, validation rules |
| **Quickstart Guide** | `specs/003-directory-people-navigation/quickstart.md` | Environment setup, test execution, troubleshooting |
| **Page Object Contract** | `specs/003-directory-people-navigation/contracts/page-objects.md` | POM hierarchy, public interfaces, locator contracts |
| **API/HTML Contract** | `specs/003-directory-people-navigation/contracts/navigation-api.md` | Backend API structure, HTML expectations, data attributes |
| **Project Constitution** | `.specify/memory/constitution.md` | Architectural principles, governance constraints |

---

## Validation Checklist (Pre-Merge)

- [ ] All tasks T001–T028 completed and passing
- [ ] Zero `Thread.sleep()` in framework and tests
- [ ] All locators use stable attributes (data-testid, aria-label, stable IDs)
- [ ] DriverManager provides thread-local isolation (verified with 4-thread parallel test)
- [ ] All test scenarios (T017–T025) produce Allure reports with screenshots and logs
- [ ] Page Objects follow POM pattern: locators private, business methods public
- [ ] Test data externalized in JSON; no hardcoded values in test code
- [ ] BaseTest lifecycle: @BeforeMethod/@AfterMethod (not @BeforeClass/@AfterClass)
- [ ] TestNG configured for parallel execution with listeners
- [ ] Anti-pattern audit complete; all violations remediated
- [ ] Constitution compliance checklist passes
- [ ] No regressions to existing directory features (if any)
- [ ] Allure report generated and reviewed for completeness

---

**Status**: Tasks Generated ✅  
**Last Updated**: 2026-07-25  
**Next Phase**: Execute Phase 1 setup tasks (T001–T003)  
**Audience**: Automation Engineers, QA Analysts, Technical Leads
