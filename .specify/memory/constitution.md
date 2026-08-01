<!-- Sync Impact Report - Constitution v1.0.0 (INITIAL CREATION)
Generated: 2026-07-23
From Template: .specify/templates/constitution-template.md
Project: sdd_selenium (Selenium E2E Test Framework)
========================================

Version: 1.0.0 (NEW)
Principles Created: 5 (Page Object Model, Thread-Safe Driver Management, Test-First Discipline, Wait Synchronization, Reporting & Observability)
Sections Added: Technology Stack, Development Workflow, Quality Gates
Templates Updated: All dependent templates remain as templates; runtime guidance in AGENTS.md

========================================
-->

# sdd_selenium Constitution

Governing document for the End-to-End Selenium Testing Framework. Establishes architectural principles, quality standards, and development practices for automation engineers building robust, maintainable, and reliable test suites.

## Core Principles

### I. Page Object Model (POM) Abstraction

**Non-Negotiable Rule**: All page interactions MUST be encapsulated within Page Objects; tests MUST contain only assertions and business logic orchestration.

Page Objects abstract away Selenium mechanics (WebDriver, By locators, waits) behind clean, business-focused public methods. Implementation details (private By locators, internal waits) are hidden from test classes. This ensures:
- **Maintainability**: Locator changes affect only the Page Object, not N test methods.
- **Reusability**: Common UI components (e.g., buttons, forms) can be shared via component sub-packages.
- **Readability**: Tests read like plain English scenarios, not WebDriver boilerplate.

**Guidance**: Leverage `src/main/java/com/project/pages/` and `src/main/java/com/project/pages/components/` for page and component definitions.

### II. Thread-Safe Driver Management (ThreadLocal)

**Non-Negotiable Rule**: WebDriver instances MUST be managed via `ThreadLocal<WebDriver>` in `DriverManager`; single-threaded static driver references are forbidden.

This pattern enables parallel test execution without state pollution. Each thread owns its driver instance. `DriverManager.getDriver()` retrieves the thread-bound driver; `DriverManager.quitDriver()` releases it safely.

**Guidance**:
- Initialization in `BaseTest.setUp()`: `DriverManager.setDriver(DriverFactory.createDriver(browser))`.
- Cleanup in `BaseTest.tearDown()`: `DriverManager.quitDriver()`.
- From utilities/listeners: always call `DriverManager.getDriver()`, never cache driver instances locally.

### III. Test-First Discipline (NON-NEGOTIABLE)

**Non-Negotiable Rule**: TDD mandatory; tests are written, reviewed, and approved BEFORE implementation; the Red-Green-Refactor cycle is strictly enforced.

Every new feature or fix starts with a test scenario. The test fails until the underlying page object or utility is implemented. Code reviews verify test coverage for the intended behavior.

**Guidance**:
- Write test case in `src/test/java/com/project/tests/` with `@Test` and clear name.
- Approve test logic with stakeholders (test data, expected outcomes, edge cases).
- Implement Page Objects and utilities to make test pass.
- Refactor for clarity and performance once green.

### IV. Wait Synchronization (No Hard Sleeps)

**Non-Negotiable Rule**: `Thread.sleep()` is banned; all waits MUST use `WebDriverWait` with `ExpectedConditions` or custom conditions via `WaitUtils`.

Hard waits reduce test speed, mask instability, and create false failures. Dynamic waits (polling + timeout) match UI readiness and fail fast on real issues.

**Guidance**:
- Use `WaitUtils.untilVisible(element)` for element appearance.
- Use `WaitUtils.untilUrlContains(fraction)` for navigation confirmation.
- Extend `WaitUtils` with custom conditions as needed (e.g., for custom loading spinners).
- Timeout default: 10 seconds; override in Page Objects only when justifiable (high-latency systems).

### V. Reporting & Observability (Mandatory)

**Non-Negotiable Rule**: Every test run MUST produce Allure attachments and structured logs via `ReportLogger`; on failure, screenshot + step logs are captured.

Observability enables fast root-cause analysis and audit trails. `ReportLogger.log(message)` appends to both Allure and stdout; `TestListener.onTestFailure()` can attach screenshots.

**Guidance**:
- Use `ReportLogger.log("Step: entering username...")` for key steps.
- Extend `TestListener.onTestFailure()` to call `takeScreenshot()` and attach via `Allure.addAttachment()`.
- Allure reports are generated in `allure-results/` and can be viewed via `allure serve allure-results/`.
- All test runs include traceability: test name, duration, pass/fail, attachments.

## Technology Stack Requirements

**Mandatory Stack**:
- Java 17 (or later)
- Maven 3.8+ (dependency & build management)
- Selenium 4.46.0+ (WebDriver protocol)
- TestNG 7.8.0+ (test framework, listeners, data providers)
- WebDriverManager 5.4.1+ (automatic driver download & setup)
- Allure 2.19.0+ (reporting)
- SLF4J 2.0.9+ (logging)

**Browser Support**:
- Chrome is mandatory (fully configured in `DriverFactory`).
- Firefox/Edge/Safari: optional; extend `DriverFactory.createDriver()` to add support following the Chrome pattern.

**Configuration**:
- Test parameters (baseUrl, browser) injected via `testng.xml`, not runtime properties.
- Reference configs in `config/env.*.properties`; integration via property loader in future phases.
- All test data externalized from code (avoid hardcoding credentials; use `LoginData` or property-based approach).

## Development Workflow

### Test Execution

**Local Testing**:
```bash
mvn test                                              # Full suite
mvn -Dtest=com.project.tests.LoginTests test        # Single class
```

**CI/CD**:
- GitHub Actions workflows defined in `.github/workflows/` (if present).
- Surefire plugin configured to run `testng.xml` suite.
- Test reports published to Allure for tracking and trend analysis.

### Code Organization

```
src/main/java/com/project/
├── drivers/          # DriverFactory, DriverManager
├── pages/            # LoginPage, DashboardPage, etc.
│   └── components/   # Reusable UI components (buttons, modals, etc.)
└── utils/            # WaitUtils, ReportLogger, custom utilities

src/test/java/com/project/
├── tests/            # LoginTests, DashboardTests, etc.
├── tests/base/       # BaseTest with @BeforeMethod/@AfterMethod lifecycle
├── listeners/        # TestListener for hooks
└── data/             # LoginData and other test data classes
```

### Parallel Execution

- TestNG supports parallel execution (see `testng.xml` suites).
- ThreadLocal driver ensures test isolation; no global state.
- Concurrency limit recommended: 1–4 threads (depends on system resources and AUT throttling).

## Quality Gates

### Code Review Checklist

All PRs must verify:

1. **Test Quality**:
   - [ ] Test name is descriptive and follows naming convention (e.g., `test_validLoginWithCorrectCredentials`).
   - [ ] Test uses only business-focused assertions (no WebDriver mechanics).
   - [ ] Test data is externalized (no hardcoded values in test method).

2. **Page Object Compliance**:
   - [ ] All Selenium interactions (WebDriver, By, WebDriverWait) are in Page Objects.
   - [ ] All By locators are `private`; only business methods are `public`.
   - [ ] Page Object methods return meaningful types (void, boolean, String, or another page object).

3. **Driver & Thread Safety**:
   - [ ] DriverManager.getDriver() is used, never static driver references.
   - [ ] No @BeforeClass / @AfterClass static setup; use @BeforeMethod / @AfterMethod.
   - [ ] Driver lifecycle is properly managed (set in setUp, quit in tearDown).

4. **Waits & Synchronization**:
   - [ ] No `Thread.sleep()` calls (except in rare exceptional cases with justification).
   - [ ] WebDriverWait or WaitUtils used for dynamic waits.
   - [ ] Timeout values are reasonable (default 10s; override only with justification).

5. **Reporting & Observability**:
   - [ ] Critical steps logged via `ReportLogger.log()`.
   - [ ] Listener hooks are properly implemented (onTestFailure captures screenshot, etc.).
   - [ ] No console.out() calls; all output via ReportLogger or Allure.

6. **Build & Execution**:
   - [ ] All tests pass locally: `mvn test`.
   - [ ] Maven dependencies resolve (no conflicts, no deprecated libraries).
   - [ ] TestNG XML is correctly configured (listeners, parameters, suite structure).

### Complexity Justification

When adding new features or modifying existing code:
- **Complexity increases?** Document rationale in PR comments or commit message.
- **New browser support?** Update DriverFactory and document in AGENTS.md.
- **New wait condition?** Add to WaitUtils with inline comments explaining use case.
- **Architectural change?** Reference this constitution and explain how alignment is maintained.

## Governance

### Amendment Process

1. **Proposal**: Raise an issue or RFC explaining the constitutional change (e.g., add new principle, update tech stack).
2. **Discussion**: Core team reviews and validates alignment with project goals.
3. **Documentation**: Update `.specify/memory/constitution.md` (this file) with version bump.
4. **Propagation**: Sync dependent artifacts (AGENTS.md, templates, runtime guidance).
5. **Commit**: Use conventional commit message: `docs: amend constitution to v<VERSION> (<reason>)`

### Compliance Review

- **Frequency**: Upon PR merge; spot checks during sprint reviews.
- **Owner**: Code review team verifies checklist items above.
- **Escalation**: If constitutional breach detected, PR is blocked until resolved.

### Templates & Guidance

- **Authoritative Guidance**: `.github/AGENTS.md` for AI agent workflows; update post-amendment.
- **Spec Kit Phases**: Phases defined in `.specify/` and guided by phase contracts; follow `speckit.constitution` → `speckit.specify` → … flow.
- **Runtime Development**: See `README.md` and `AGENTS.md` for local development, debugging, and CI/CD details.

### Versioning Policy

Constitution versioning follows **Semantic Versioning (MAJOR.MINOR.PATCH)**:

- **MAJOR**: Principle removal, redefinition, or fundamental architectural change (e.g., discard POM, move to BDD framework).
- **MINOR**: New principle added, section expanded, or non-breaking technology stack update (e.g., upgrade Selenium minor version).
- **PATCH**: Clarifications, wording improvements, typo fixes, non-semantic refinements (e.g., update example URLs).

---

**Version**: 1.0.0 | **Ratified**: 2026-07-23 | **Last Amended**: 2026-07-23

**Principles**: 5 (POM, ThreadLocal Driver, Test-First, Wait Sync, Reporting)  
**Technology**: Java 17, Maven, Selenium 4.46.0, TestNG 7.8.0, Allure 2.19.0  
**Status**: ACTIVE — All automation engineers MUST comply with these principles and quality gates.
