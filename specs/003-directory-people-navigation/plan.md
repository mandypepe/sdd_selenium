# Implementation Plan: Directory People Navigation Verification

**Branch**: `feature/qa-003-directory-people-navigation`  
**Date**: 2026-07-25  
**Specification**: [specs/003-directory-people-navigation/spec.md](spec.md)

**Input**: Feature specification from `/specs/003-directory-people-navigation/spec.md`

---

## Summary

This plan describes the implementation strategy for TC-003: Directory People Navigation Verification, an automated UI test suite validating the "People" section of an institutional directory. The feature ensures users can discover the "People" navigation option and view paginated personnel records with name and role/title attributes.

**Key Characteristics**:
- **Scope**: Navigation discovery, personnel list rendering, pagination validation
- **Technology Stack**: Java 17, Maven, Selenium 4.46.0, TestNG 7.8.0, Allure 2.19.0
- **Testing Methodology**: TDD (Test-Driven Development), Page Object Model, ThreadLocal driver management, data-driven parameterization
- **Parallel Execution**: 1–4 concurrent threads; no shared state via ThreadLocal driver isolation
- **Reporting**: Allure 2.x with step logs and screenshot attachments

---

## Technical Context

**Language/Version**: Java 17 (project requirement from Constitution)

**Primary Dependencies**:
- Selenium WebDriver 4.46.0 (browser automation)
- TestNG 7.8.0 (test framework with parallel execution and data providers)
- WebDriverManager 5.4.1 (automatic driver management; no manual binary setup)
- Allure 2.19.0 (detailed test reporting)
- SLF4J 2.0.9 (structured logging)

**Storage**: N/A (tests read from public directory; no persistent data storage)

**Testing Framework**: TestNG with `@Test`, `@DataProvider`, `@BeforeMethod`, `@AfterMethod`, and custom listeners

**Target Platform**: Cross-browser (Chrome mandatory, Firefox/Safari optional); desktop/responsive web

**Project Type**: E2E automation test suite (companion to application under test)

**Performance Goals**:
- Individual test execution: ≤ 10 seconds per scenario (no fixed sleeps)
- Full suite on 4 threads: ≤ 60 seconds (depends on environment)
- Page load time: ≤ 3 seconds (from spec requirement SC-002)
- Parallel test isolation: 100% (no state pollution across threads)

**Constraints**:
- No `Thread.sleep()` (Constitution Principle IV); all waits dynamic
- No shared static WebDriver instances (Constitution Principle II); ThreadLocal only
- Locators must be stable (not brittle to minor layout changes)
- Test data externalized (Constitution Principle IV); no hardcoded values in tests
- All WebDriver code encapsulated in Page Objects (Constitution Principle I)

**Scale/Scope**:
- Test dataset covers: single-page (< 20 records), multi-page (60+ records), boundary cases (40 records exactly), edge cases (empty, special characters)
- Page Object hierarchy: BasePage → DirectoryNavigationPage, PeopleSectionPage; composed with PaginationComponent
- Estimated test count: 8–12 test scenarios (happy path, pagination edge cases, data validation)

---

## Constitution Check

**Gate: Must pass before Phase 0 research. Re-check after Phase 1 design.**

### Principle I: Page Object Model (POM) & OOP

✅ **Compliant**. Plan includes:
- Abstract `BasePage` with common WebDriver operations (protected methods)
- Specialized page objects: `DirectoryNavigationPage`, `PeopleSectionPage`
- Reusable component: `PaginationComponent` (composition pattern)
- All By locators private; only public business methods exposed
- Value object `PersonnelRecord` for data representation

**Implementation Guidance**: `src/main/java/com/project/pages/` with subpackage for components

---

### Principle II: Thread-Safe Driver Management (ThreadLocal)

✅ **Compliant**. Plan includes:
- Existing `DriverManager` with `ThreadLocal<WebDriver>` (documented in Constitution)
- Setup: `DriverManager.setDriver(driver)` in `BaseTest.setUp()`
- Teardown: `DriverManager.quitDriver()` in `BaseTest.tearDown()`
- Page objects retrieve driver via `DriverManager.getDriver()` (never cached locally)
- No static driver fields; thread-bound isolation enforced

**Implementation Guidance**: Extend existing DriverManager if needed; no new static state

---

### Principle III: Test-First Discipline

✅ **Compliant**. Plan follows TDD:
- Tests written before implementation (spec → test → page object → pass)
- Test scenarios defined in data model and research
- All test cases cover behavior specified in feature spec
- Code review checklist verifies test coverage

**Implementation Guidance**: Write tests with descriptive names; implement page objects to make tests pass

---

### Principle IV: Wait Synchronization (No Hard Sleeps)

✅ **Compliant**. Plan includes:
- All waits use `WebDriverWait` + `ExpectedConditions` (or custom `WaitUtils`)
- Custom wait conditions for pagination: `untilRecordsLoaded()`, `untilLoadingIndicatorGone()`
- Timeout default: 10 seconds (no arbitrary delays)
- No `Thread.sleep()` anywhere in the codebase

**Implementation Guidance**: Extend `WaitUtils` with pagination-specific conditions

---

### Principle V: Reporting & Observability

✅ **Compliant**. Plan includes:
- Step logging via `ReportLogger.log(message)` for key UI interactions
- Screenshot capture at critical points: navigation, data load, page transitions
- Allure integration for detailed reports (step traces, attachments, timelines)
- TestListener hooks for automatic failure capture (screenshot + logs)
- Traceability: test name, duration, pass/fail, attachments per run

**Implementation Guidance**: Integrate with existing Allure setup; extend TestListener for screenshot capture

---

### Gate Violations: ❌ NONE

All principles are satisfied by the planned architecture. No justification needed.

---

## Project Structure

### Documentation (this feature)

```
specs/003-directory-people-navigation/
├── plan.md                          # This file
├── spec.md                          # Feature specification (input)
├── research.md                      # Phase 0: Technical research (GENERATED)
├── data-model.md                    # Phase 1: Entity definitions, test data (GENERATED)
├── quickstart.md                    # Phase 1: Setup & execution guide (GENERATED)
├── contracts/
│   ├── navigation-api.md            # API/HTML contract for backend (GENERATED)
│   └── page-objects.md              # Page Object design contract (GENERATED)
└── tasks.md                         # Phase 2: Actionable implementation tasks (NOT in /speckit.plan)
```

### Source Code (repository root)

```
src/main/java/com/project/
├── drivers/
│   ├── DriverFactory.java           # Browser driver creation (Chrome, Firefox, Safari)
│   └── DriverManager.java           # ThreadLocal driver lifecycle management
├── pages/
│   ├── BasePage.java                # Abstract base with common WebDriver operations
│   ├── DirectoryNavigationPage.java  # Navigation & section selection
│   ├── PeopleSectionPage.java        # Personnel list & pagination
│   └── components/
│       ├── PaginationComponent.java  # Reusable pagination control abstraction
│       └── PersonnelRecord.java      # Value object for personnel data
└── utils/
    ├── WaitUtils.java               # Intelligent wait conditions + extensions
    ├── ReportLogger.java            # Structured logging (Allure + stdout)
    └── ScreenshotUtils.java         # Screenshot capture for debugging

src/test/java/com/project/
├── tests/
│   ├── base/
│   │   └── BaseTest.java            # Abstract base with lifecycle (@BeforeMethod, @AfterMethod)
│   └── DirectoryPeopleNavigationTests.java  # Test scenarios
├── listeners/
│   └── TestListener.java            # Event hooks (onTestFailure captures screenshot)
└── data/
    └── PersonnelDataProvider.java    # @DataProvider for parameterized tests

config/
└── env.properties                   # Environment configuration (baseUrl, timeouts)

testng.xml                           # TestNG suite configuration (parallel settings, listeners)
pom.xml                              # Maven dependencies & build configuration
```

**Structure Decision**: Single project (monolithic) approach. All automation code co-located in one Maven project (`sdd_selenium`). No separate frontend/backend splits; this is a pure E2E test framework.

---

## Architecture Decisions

### 1. Page Object Model with Composite Components

**Decision**: Use BasePage → specialized pages → composed components pattern.

**Rationale**:
- Encapsulates WebDriver mechanics; tests contain only business logic
- Components (PaginationComponent) reusable across multiple page objects
- Single responsibility: navigation page, list page, pagination logic separate
- Maintainability: locator changes affect only the relevant page object

**Implementation**:
- `BasePage`: Abstract; defines protected methods for common WebDriver operations
- `DirectoryNavigationPage`: Handles navigation and section selection
- `PeopleSectionPage`: Manages personnel list, record extraction, pagination delegation
- `PaginationComponent`: Encapsulates all pagination button/link interactions

---

### 2. ThreadLocal Driver Management for Parallel Execution

**Decision**: Leverage existing `DriverManager` with `ThreadLocal<WebDriver>`.

**Rationale**:
- Each test thread owns its driver instance; no cross-contamination
- Enables safe parallel execution (1–4 threads on typical system)
- Constitution Principle II mandate

**Implementation**:
- `setUp()`: `DriverManager.setDriver(DriverFactory.createDriver(browser))`
- Tests/Pages: Always call `DriverManager.getDriver()` (never cache locally)
- `tearDown()`: `DriverManager.quitDriver()` (safe cleanup under all circumstances)

---

### 3. Data-Driven Testing via TestNG @DataProvider

**Decision**: Parameterize test scenarios with external test data.

**Rationale**:
- Tests can be run independently for each dataset (e.g., page 1, page 2, page 3)
- Test data decoupled from test logic
- Allure reports show individual runs per dataset
- Reduced code duplication

**Implementation**:
- `PersonnelDataProvider`: Centralized data source
- `@Test(dataProvider = "paginationScenarios")` in test methods
- Example: Dataset 1 (single page), Dataset 2 (multi-page), Dataset 3 (boundary)

---

### 4. Advanced Synchronization with Custom Wait Conditions

**Decision**: Use `WebDriverWait` + `ExpectedConditions` + custom `WaitUtils` extensions.

**Rationale**:
- Eliminates flaky fixed sleeps; tests run fast when app is fast, fail fast when it's slow
- Custom conditions handle pagination-specific states (records loaded, spinner gone)
- Configurable timeouts (default 10s; override only when justified)

**Implementation**:
- `WaitUtils.untilVisible(element)`: Element presence + displayedness
- `WaitUtils.untilRecordsLoaded()`: Custom condition; polls for personnel records
- `WaitUtils.untilLoadingIndicatorGone()`: Waits for spinner to disappear
- `WaitUtils.untilUrlContains(fragment)`: Confirms navigation via URL change

---

### 5. Allure + ReportLogger for Observability

**Decision**: Structured logging with Allure integration for root-cause visibility.

**Rationale**:
- TestListener captures screenshot on failure automatically
- ReportLogger logs each step to both Allure and stdout
- Detailed reports: timelines, attachments, step traces
- Audit trail for debugging and compliance

**Implementation**:
- `ReportLogger.log("Step: Navigating to page 2...")` for key steps
- `takeScreenshot("page-2-loaded.png")` at critical junctures
- `TestListener.onTestFailure()` → automatic screenshot capture
- Allure report generation: `mvn allure:report`

---

## Implementation Phases

### Phase 1: Base Infrastructure & Framework Setup

**Objective**: Establish driver management, base page class, and utility infrastructure.

**Deliverables**:
- Extend/verify `DriverManager` class with ThreadLocal pattern
- Create `BasePage` abstract class with protected methods (click, sendKeys, wait, etc.)
- Create `WaitUtils` with custom conditions (untilRecordsLoaded, untilLoadingIndicatorGone)
- Create `ReportLogger` for structured logging
- Create `ScreenshotUtils` for screenshot capture
- Extend `TestListener` to capture failure screenshots
- Verify existing `DriverFactory` supports Chrome (mandatory) and optionally Firefox/Safari

**Acceptance Criteria**:
- DriverManager isolates drivers per thread (unit test with ThreadLocal verification)
- BasePage methods accessible to subclasses; no direct WebDriver calls in tests
- WaitUtils conditions poll dynamically (no fixed sleeps)
- Allure reports generated successfully

**Effort**: 1–2 days

---

### Phase 2: Page Object Implementation

**Objective**: Build page objects for directory navigation, people section, and pagination.

**Deliverables**:
- `DirectoryNavigationPage`: Methods for openDirectory(), selectPeopleSection(), isPeopleOptionVisible()
- `PeopleSectionPage`: Methods for waitForRecordsToLoad(), getVisibleRecords(), getCurrentPageNumber(), navigateToPage(), goToNextPage(), goToPreviousPage()
- `PaginationComponent`: Methods for pagination control interaction (next, previous, page selection)
- `PersonnelRecord`: Value object representing a personnel entry

**Implementation Details**:
- All By locators private; only business methods public
- Locators use stable data-testid attributes (from HTML contract)
- Synchronization via WaitUtils (no hard sleeps)
- Page objects delegate driver calls to DriverManager.getDriver()

**Acceptance Criteria**:
- Page objects compile without errors
- All public methods documented with Javadoc
- Locators match HTML contract (data-testid, ids, aria-labels)
- No static state or thread-unsafe patterns

**Effort**: 2–3 days

---

### Phase 3: Test Scenario Implementation

**Objective**: Write test cases covering happy path, pagination, and edge cases.

**Deliverables**:
- `DirectoryPeopleNavigationTests` class with test methods:
  - `test_navigateToPeopleSection()` — Verify "People" option is visible and clickable
  - `test_viewPersonnelList()` — Verify personnel records display with name and role
  - `test_singlePageDisplay()` — Validate single-page scenario (< 20 records)
  - `test_multiPageNavigation()` — Navigate through pages; verify content changes
  - `test_paginationButtonStates()` — Verify prev/next buttons enabled/disabled correctly
  - `test_lastPagePartialRecords()` — Validate last page may have fewer records
  - `test_specialCharactersRendering()` — Verify accented names/roles render correctly
  - `test_dataLoadingTimeout()` — Verify records load within 3 seconds
- `PersonnelDataProvider`: Data provider with test datasets (single-page, multi-page, boundary, special chars)

**Implementation Details**:
- TDD approach: write test first (fails), implement page object (passes)
- Use data-driven approach via @DataProvider
- Step logging via ReportLogger.log()
- Screenshot capture at critical points

**Acceptance Criteria**:
- All tests pass locally (Maven `mvn test`)
- Allure reports show individual runs per dataset
- Screenshots attached for all failure scenarios
- No Thread.sleep() in any test

**Effort**: 2–3 days

---

### Phase 4: Data Externalization & Test Data Management

**Objective**: Externalize test data into JSON or Properties files; manage test datasets.

**Deliverables**:
- `test-data/personnel-test-sets.json` or `test-data/personnel-test-sets.properties` with:
  - Single-page dataset (8 records)
  - Multi-page dataset (65 records, 4 pages)
  - Boundary dataset (40 records, exactly 2 pages)
  - Special characters dataset (10 records with accents, long names)
- `TestDataLoader` utility to load external test data
- Update `PersonnelDataProvider` to load from external files

**Implementation Details**:
- JSON structure: array of record objects with id, fullName, roleTitle, department, etc.
- Properties format: key-value pairs (alternative to JSON)
- TestDataLoader reads from `test-data/` directory
- Tests remain independent; data-driven via DataProvider

**Acceptance Criteria**:
- Test data files exist in project
- TestDataLoader successfully loads and parses data
- Tests pass with externalized data
- No hardcoded test data in test methods

**Effort**: 1 day

---

### Phase 5: Parallel Execution Validation & Hardening

**Objective**: Verify tests run safely in parallel; add robustness.

**Deliverables**:
- Configure `testng.xml` for parallel execution (threads=4)
- Run full test suite on 4 threads; verify 100% pass rate
- Add retry logic (optional): `@Test(retryAnalyzer = RetryAnalyzer.class)` for transient failures
- Stress test: run suite 10 times consecutively; verify stability
- Document parallel execution setup in quickstart.md

**Implementation Details**:
- ThreadLocal driver isolation ensures no cross-contamination
- Each test independent; no inter-test dependencies
- Allure reports capture individual timelines per thread
- Monitor for race conditions or timing-dependent failures

**Acceptance Criteria**:
- All tests pass on 1 thread and 4 threads
- Allure reports show no flakiness (100% pass rate over 10 consecutive runs)
- Execution time on 4 threads ≤ 60 seconds (full suite)
- No timeout or resource conflicts

**Effort**: 1–2 days

---

## Risk Mitigation

| Risk | Probability | Impact | Mitigation |
|------|-------------|--------|-----------|
| **Fragile Locators** | Medium | High | Use stable semantic attributes (data-testid); avoid XPath indices; monitor for UI changes |
| **Pagination State Sync Failures** | Medium | Medium | Implement multiple confirmation strategies (URL, button state, record count); retry logic |
| **Cross-Browser Incompatibility** | Low | Medium | Leverage WebDriverManager for automatic driver management; test on Chrome (mandatory) + Firefox (optional) |
| **False Negatives (Flaky Tests)** | Medium | High | Replace fixed waits with intelligent polling; capture screenshots on failure; run suite 10x to validate |
| **Test Data Consistency** | Low | Medium | Bypass UI for data setup; use API/database seeding; maintain seed fixtures as code artifacts |
| **Parallel Execution Conflicts** | Low | High | ThreadLocal driver isolation + @BeforeMethod/@AfterMethod + test autonomy; no shared state |
| **Performance Regression** | Low | Medium | Track execution time in Allure reports; set baselines; alert on degradation |
| **Locator Drift Over Time** | Medium | Medium | Use Page Object encapsulation; centralize locators; code review checklist for locator stability |

---

## Success Metrics

### Functional Validation

✅ **100% Feature Coverage**: All scenarios from spec implemented and passing
- [ ] "People" option visibility and accessibility
- [ ] Personnel list rendering (name, role/title)
- [ ] Pagination navigation (next, previous, page selection)
- [ ] Page boundary handling (first, last page)
- [ ] Data loading within SLA (3 seconds per SC-002)

### Test Quality Metrics

✅ **Test Stability**: Zero regressions across 10 consecutive runs
- [ ] 100% pass rate on single thread
- [ ] 100% pass rate on 4 parallel threads
- [ ] No timeout exceptions or race conditions
- [ ] Consistent execution time ± 10%

✅ **Code Quality**: Constitution compliance verified
- [ ] All Page Objects have private locators, public business methods
- [ ] All waits use WebDriverWait (no Thread.sleep)
- [ ] All drivers managed via ThreadLocal (DriverManager)
- [ ] All test data externalized (no hardcoding)
- [ ] Code review checklist 100% satisfied

✅ **Observability**: Allure reports include full traceability
- [ ] All tests have step logs (ReportLogger)
- [ ] Failures include screenshots
- [ ] Allure timeline shows execution per thread
- [ ] Root cause visible (logs + screenshots)

### Performance Targets

- Individual test execution: ≤ 10 seconds
- Full suite (8 tests, single thread): ≤ 60 seconds
- Full suite (8 tests, 4 threads): ≤ 30 seconds
- Parallel startup overhead: < 5 seconds

---

## Risk Mitigation: Complexity Justification

**No complexity violations detected.**

All architectural decisions align with Constitution principles. No deviations requiring justification.

| Potential Complexity | Reason Included | Simpler Alternative Rejected |
|-|-|-|
| N/A — All decisions justified by principles | — | — |

---

## Development Workflow

### Code Organization

```
Feature branch: feature/qa-003-directory-people-navigation
├── Commits:
│   1. feat: setup base infrastructure (DriverManager, BasePage, WaitUtils)
│   2. feat: implement DirectoryNavigationPage and PeopleSectionPage
│   3. feat: implement PaginationComponent and PersonnelRecord
│   4. test: add DirectoryPeopleNavigationTests with all scenarios
│   5. test: add PersonnelDataProvider with test datasets
│   6. test: externalize test data to JSON files
│   7. test: verify parallel execution (threads=4)
```

### Testing & Validation

```
Local Development:
  1. mvn clean test                           # Run all tests
  2. mvn allure:serve                         # Review reports
  3. mvn test -DthreadCount=4 -DparallelTest # Verify parallel

CI/CD (GitHub Actions):
  1. Trigger on PR: run mvn test
  2. Generate Allure reports
  3. Block merge if tests fail or compliance violated
```

### Code Review Checklist

All PRs must verify:

1. **Test Quality**:
   - [ ] Test name is descriptive (e.g., `test_navigateToPeopleAndViewRecords`)
   - [ ] Test uses only business-focused assertions (no WebDriver mechanics)
   - [ ] Test data is externalized (no hardcoded values)
   - [ ] No `Thread.sleep()` calls

2. **Page Object Compliance**:
   - [ ] All By locators are `private`
   - [ ] Only business methods are `public`
   - [ ] Methods return meaningful types (void, boolean, String, PersonnelRecord, List)

3. **Driver & Thread Safety**:
   - [ ] `DriverManager.getDriver()` used (never static references)
   - [ ] `@BeforeMethod`/`@AfterMethod` used (not `@BeforeClass`/`@AfterClass`)
   - [ ] Driver lifecycle properly managed (set in setUp, quit in tearDown)

4. **Waits & Synchronization**:
   - [ ] No `Thread.sleep()` (exceptions with justification only)
   - [ ] `WebDriverWait` or `WaitUtils` used for all waits
   - [ ] Timeout values reasonable (default 10s; override justified)

5. **Reporting & Observability**:
   - [ ] Critical steps logged via `ReportLogger.log()`
   - [ ] Listener hooks properly implemented
   - [ ] No console output (use ReportLogger or Allure)

6. **Build & Execution**:
   - [ ] All tests pass locally: `mvn test`
   - [ ] Maven dependencies resolve
   - [ ] TestNG XML correctly configured

---

## Documentation & Knowledge Transfer

### Generated Artifacts

- ✅ **research.md**: Technical research; resolves unknowns
- ✅ **data-model.md**: Entity definitions; test data structure
- ✅ **contracts/navigation-api.md**: API/HTML contract
- ✅ **contracts/page-objects.md**: Page Object design contract
- ✅ **quickstart.md**: Local setup and execution guide
- ✅ **plan.md**: This file — comprehensive implementation strategy

### Additional Documentation

- **AGENTS.md**: Developer workflow guidance (to be updated post-implementation)
- **constitution.md**: Architectural principles (reference; no changes needed)

---

## Dependencies & Prerequisites

### External Dependencies

- **Selenium WebDriver 4.46.0**: Browser automation
- **TestNG 7.8.0**: Test framework
- **WebDriverManager 5.4.1**: Automatic driver lifecycle
- **Allure 2.19.0**: Reporting
- **SLF4J 2.0.9**: Logging

### Internal Dependencies

- Existing `DriverManager` (ThreadLocal pattern)
- Existing `DriverFactory` (Chrome support; extend for Firefox/Safari if needed)
- Existing project structure and conventions

### Environment Setup

- Java 17+ installed
- Maven 3.8.0+ installed
- Chrome browser (latest)
- Test environment with populated personnel directory

---

## Go-Live & Monitoring

### Pre-Merge Validation

1. ✅ All tests pass locally (single & parallel)
2. ✅ Allure reports generated successfully
3. ✅ Code review checklist 100% satisfied
4. ✅ Constitution compliance verified
5. ✅ Documentation complete (research.md, data-model.md, contracts/, quickstart.md)

### Post-Merge Integration

1. Feature branch merged to main
2. CI/CD pipeline runs full suite (GitHub Actions)
3. Allure reports published to artifact repository
4. Team notified of test suite readiness

### Ongoing Maintenance

- **Quarterly**: Review test data for relevance; refresh if personnel records change
- **Monthly**: Monitor Allure trends; identify flaky tests; improve page object locators
- **Upon UI Change**: Update locators and page objects; revalidate tests
- **Performance**: Track execution time; alert on degradation

---

## Next Steps

### Immediate (Week 1)

1. ✅ Phase 0 complete: research.md finalized
2. ✅ Phase 1 design complete: data-model.md, contracts/, quickstart.md
3. **Phase 2 (NEXT)**: Implementation begins
   - Start Phase 1: Infrastructure setup (DriverManager, BasePage, WaitUtils)
   - Create tasks.md via `/speckit.tasks` command

### Short-term (Week 2–3)

4. Phase 1 complete: Page objects implemented and tested
5. Phase 2 complete: All test scenarios passing locally
6. Phase 3 complete: Data externalized; parallel execution validated

### Medium-term (Week 4)

7. Code review & approval
8. Merge to main branch
9. CI/CD integration
10. Handoff to QA team

---

## Appendix: Configuration Examples

### testng.xml (Parallel Execution)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<suite name="Directory_People_Navigation_Suite" parallel="tests" thread-count="4">
  <listeners>
    <listener class-name="com.project.listeners.TestListener" />
  </listeners>
  
  <test name="DirectoryPeopleNavigationTest">
    <parameter name="browser" value="chrome" />
    <classes>
      <class name="com.project.tests.DirectoryPeopleNavigationTests" />
    </classes>
  </test>
</suite>
```

### pom.xml (Key Dependencies)

```xml
<properties>
  <selenium.version>4.46.0</selenium.version>
  <testng.version>7.8.0</testng.version>
  <allure.version>2.19.0</allure.version>
</properties>

<dependencies>
  <dependency>
    <groupId>org.seleniumhq.selenium</groupId>
    <artifactId>selenium-java</artifactId>
    <version>${selenium.version}</version>
  </dependency>
  
  <dependency>
    <groupId>org.testng</groupId>
    <artifactId>testng</artifactId>
    <version>${testng.version}</version>
    <scope>test</scope>
  </dependency>
  
  <dependency>
    <groupId>io.github.bonigarcia</groupId>
    <artifactId>webdrivermanager</artifactId>
    <version>5.4.1</version>
  </dependency>
  
  <dependency>
    <groupId>io.qameta.allure</groupId>
    <artifactId>allure-testng</artifactId>
    <version>${allure.version}</version>
  </dependency>
</dependencies>
```

---

**Status**: Implementation Plan Complete ✅  
**Timestamp**: 2026-07-25  
**Phase Status**: Phase 0 & Phase 1 Complete → Ready for Phase 2 Implementation  
**Next Action**: Generate tasks.md via `/speckit.tasks` command
