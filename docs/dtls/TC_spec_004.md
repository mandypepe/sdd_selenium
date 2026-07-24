<!-- Implementation is mandatory: docs/dtls/TC_spec_004.md is associated with src/test/java/com/project/tests/UniversitiesSectionNavigationTest.java -->
# Branch name suggestion: feature/TC-004_validate-universities-navigation

## Feature Specification
**Feature Branch**: `feature/TC-004_validate-universities-navigation`  
**Created**: 2026-07-23  
**Status**: Draft  
**Input**: User description: "TC-004 — Validate navigation to Universities section from the main directory."

## Feature Summary
This specification defines the automated validation for the "Universidades" navigation link located in the main directory portal. The objective is to ensure that users can seamlessly transition from the directory page to the Universities section without encountering loading errors or structural disruptions to the main directory menu. Additionally, this document establishes the foundational architectural guidelines, best practices, and anti-patterns to be strictly followed during the test automation implementation, ensuring a resilient, scalable, and maintainable testing framework.

## Target Users
- **Quality Assurance Engineers**: To implement, maintain, and execute the automated validation.
- **Product Owners**: To ensure the critical user journey is consistently monitored and functioning.
- **End Users (Students/Staff)**: Who indirectly benefit from a highly available and functional directory navigation experience.

## User Scenarios & Testing _(mandatory)_

### User Story 1 - Validate "Universidades" Navigation (Priority: P1)
As a portal user, I want to click on the "Universidades" link from the main directory so that I can access the universities section without errors and keep the main directory menu intact.
**Why this priority**: This is a core navigation pathway. If broken, users cannot access the primary academic listings, leading to critical user journey failure.
**Independent Test**: Can be fully tested by starting at the directory index, executing the navigation action, and verifying the resulting page state and menu integrity independently of other portal sections.
**Acceptance Scenarios**:
1. **Given** the user is on the main directory page, **When** the user clicks on the "Universidades" link, **Then** the system navigates to the corresponding section successfully.
2. **Given** the "Universidades" section is loading, **When** the navigation completes, **Then** the new section loads without any visible errors.
3. **Given** the user has navigated to the "Universidades" section, **When** the user inspects the page structure, **Then** the main directory menu remains fully intact and functional.

### Framework Architecture & Best Practices Requirements

1. **Page Object Model (POM) & OOP Principles**:
   - Establish an abstract base structure encapsulating standard browser interactions (clicking, typing, waiting).
   - Strictly enforce Abstraction (hiding engine and synchronization logic inside base structures) and Encapsulation (keeping locators private inside page objects and exposing only public business action methods).
2. **Native Driver Integration**:
   - Leverage native browser driver management. Do NOT include third-party manager libraries or manual driver executables in the repository.
3. **Advanced Synchronization**:
   - Completely eliminate fixed pauses or hardcoded thread suspensions.
   - Implement dynamic wrapper methods for synchronization to gracefully handle dynamic UI states and transient exceptions.
4. **Data-Driven Testing (DDT)**:
   - Decouple all test data from the execution logic using parameterized execution structures.
   - Externalize test data into standardized external files (e.g., JSON), parsing them dynamically.
5. **Thread-Safe Execution & Clean Teardown**:
   - Wrap the browser instance inside a thread-safe manager to support parallel test execution without state pollution.
   - Enforce a resilient teardown phase that securely terminates the session to prevent orphaned browser processes.
6. **Reporting & Test Listeners**:
   - Integrate standardized visual reporting.
   - Create custom event listeners that detect execution failures and automatically capture visual evidence (screenshots) to attach to the final report.

### Anti-Patterns to Address

**1. Fixed Wait Times**
- **The Problem:** Degrades suite speed and reliability by forcing the execution to halt regardless of whether the system is already ready, leading to flaky tests on slower environments.
- **Bad Practice (Anti-Pattern):** Hardcoding fixed-time sleep commands to pause execution.
- **Good Practice (Refactored Approach):** Utilizing dynamic synchronization mechanisms that poll the application state and proceed immediately once expected conditions are met.
- **Golden Rule:** Never pause the execution; always wait for a state.

**2. Fragile Locators**
- **The Problem:** Absolute paths or auto-generated selectors break immediately when minor UI structural changes occur, causing false-positive test failures.
- **Bad Practice (Anti-Pattern):** Relying on absolute hierarchical paths or dynamic, unreadable element attributes.
- **Good Practice (Refactored Approach):** Implementing resilient locators such as dedicated testing attributes, relative semantic paths, or stable unique identifiers.
- **Golden Rule:** Selectors must be resilient to cosmetic UI changes.

**3. Coupled & Dependent Test Cases**
- **The Problem:** When tests depend on the state left by previous tests, a single failure cascades into multiple failures, making debugging a nightmare.
- **Bad Practice (Anti-Pattern):** Designing test scenarios that require a previous test to succeed in order to run.
- **Good Practice (Refactored Approach):** Redesigning for full test autonomy with strictly isolated setup and teardown phases for each scenario.
- **Golden Rule:** Every test must be capable of running independently in any order.

**4. Static Instances in Parallel Execution**
- **The Problem:** Using shared static objects for browser sessions causes state pollution and cross-talk when executing tests simultaneously, leading to unpredictable crashes.
- **Bad Practice (Anti-Pattern):** Declaring the execution engine as a global static variable.
- **Good Practice (Refactored Approach):** Implementing thread-safe isolation for each execution instance to ensure parallel safety.
- **Golden Rule:** Isolate execution environments completely during parallel runs.

**5. Monolithic Page Objects ("God Classes")**
- **The Problem:** Creating single, massive files that represent entirely complex applications makes maintenance impossible and violates the Single Responsibility Principle.
- **Bad Practice (Anti-Pattern):** Putting all locators and methods for an entire module into one massive structure.
- **Good Practice (Refactored Approach):** Refactoring into component-based models where distinct UI widgets have their own manageable structures.
- **Golden Rule:** Separate concerns by modeling UI components, not just entire pages.

**6. Preparing Test Data via the UI**
- **The Problem:** Navigating through the UI to create prerequisites is slow, error-prone, and distracts from the actual feature being tested.
- **Bad Practice (Anti-Pattern):** Running long UI workflows purely to set up data for the main assertion.
- **Good Practice (Refactored Approach):** Shifting data setup to backend interfaces, direct database insertions, or mock services before the UI test begins.
- **Golden Rule:** Setup data through the fastest, most reliable backend layer available.

**7. Inverted Test Pyramid**
- **The Problem:** Relying entirely on end-to-end UI tests for all validations leads to a slow, brittle, and expensive testing suite.
- **Bad Practice (Anti-Pattern):** Validating every minor error message and edge case through the browser.
- **Good Practice (Refactored Approach):** Rebalancing validations across lower layers (unit, integration), reserving the browser automation exclusively for critical end-to-end happy paths.
- **Golden Rule:** Push validations as low in the testing architecture as possible.

## Usage Scenarios

### Edge Cases
- What happens if the network latency is extremely high during the transition to the "Universidades" section?
- How does the system handle the scenario where the target section is temporarily unavailable (e.g., HTTP 500 or 404)?
- What is the behavior if the portal is accessed via a mobile viewport; does the directory menu structure behave differently?

## Functional Requirements (Must be testable)

### Functional Requirements
- **FR-001**: The system MUST verify the presence and interactability of the "Universidades" link on the main directory page.
- **FR-002**: The system MUST confirm that clicking the link initiates a state change leading to the Universities section.
- **FR-003**: The system MUST evaluate the resulting page to ensure no server or client-side load errors are displayed.
- **FR-004**: The system MUST validate that the primary directory navigation menu remains structurally present and usable after the transition.
- **FR-005**: The testing framework MUST automatically capture visual evidence (screenshots) if any of the above validations fail.
- **FR-006**: The execution engine MUST isolate the test session to allow parallel execution without interfering with other concurrent tests.

### Key Entities _(include if feature involves data)_
- **DirectoryPage**: Represents the initial state containing the navigation menu and the target link.
- **UniversitiesSection**: Represents the expected destination state after interaction.
- **ExecutionReport**: The centralized artifact aggregating test results, execution times, and failure evidence.

## Success Criteria (Measurable and technology-agnostic)

### Measurable Outcomes
- **SC-001**: The automated test execution for this scenario must complete in under 30 seconds under normal network conditions.
- **SC-002**: The test must demonstrate 100% independence, capable of passing consistently when run in isolation or concurrently with 10 other tests.
- **SC-003**: The test scenario must maintain a flakiness rate of 0% over 50 consecutive executions in a stable environment.
- **SC-004**: In the event of a deliberate failure injection, the system must generate a full failure report with attached visual evidence in 100% of cases.

## Assumptions (Your informed assumptions)
- The target web application is deployed in an environment accessible by the test execution infrastructure.
- Standard locator strategies (such as IDs or robust semantic tags) are present or can be inferred from the current DOM structure of the directory.
- The execution infrastructure has sufficient resources to handle isolated, thread-safe test runs without hardware bottlenecks.

## Out of Scope
- Validating the content accuracy or data integrity within the "Universidades" section itself.
- Performance or load testing of the portal under high concurrent user traffic.
- Security vulnerability scanning of the directory page.
- Automating other links present in the directory menu (restricted strictly to TC-004).
---

## Framework Architecture & Best Practices Requirements

To ensure a robust, scalable, and maintainable automation framework, the implementation MUST adhere to the following architectural guidelines:

1. **Page Object Model (POM) & OOP Principles**:
   - Establish an abstract base structure encapsulating standard interactions (clicking, typing, waiting).
   - Strictly enforce Abstraction by hiding low-level driver actions within the base structures.
   - Strictly enforce Encapsulation by keeping element locators strictly private inside page objects, exposing only public business action methods to the test classes.

2. **Native Driver Integration**:
   - Leverage native browser management tools bundled with modern automation libraries.
   - Third-party binary managers or manual executable setups must NOT be used.

3. **Advanced Synchronization**:
   - Fixed or hardcoded sleep commands are strictly prohibited.
   - Implement intelligent, dynamic wrapper methods to gracefully handle dynamic UI states, loading indicators, and transient elements, proceeding exactly when the application is ready.

4. **Data-Driven Testing (DDT)**:
   - Decouple all test data (URLs, expected names, expected titles) from the execution logic.
   - Externalize test data into readable configuration files (JSON, Properties) and inject them dynamically into the test methods.

5. **Thread-Safe Driver Management & Clean Teardown**:
   - Ensure execution instances are fully thread-safe to support concurrent parallel execution without cross-contamination of application state.
   - Enforce a resilient teardown process that guarantees the safe termination of browser sessions under all circumstances (pass, fail, or crash).

6. **Reporting & Test Listeners**:
   - Integrate comprehensive reporting tools to provide visibility into test executions.
   - Implement custom event listeners that monitor test outcomes and automatically trigger visual evidence capture when discrepancies or failures are detected.

---

## Anti-Patterns to Address

The following practices degrade test stability and maintainability and MUST be avoided:

### 1. Fixed Wait Times
- **The Problem:** Halting execution for an arbitrary number of seconds makes tests artificially slow if the application loads quickly, and highly fragile if the application takes longer than the hardcoded limit.
- **Bad Practice:** Pausing the execution thread unconditionally for a predefined duration.
- **Good Practice:** Utilizing dynamic synchronization that continuously polls the application state and resumes execution the moment the specific required condition is met.
- **Golden Rule:** Never hardcode time; wait for states, not seconds.

### 2. Fragile Locators
- **The Problem:** Relying on absolute hierarchical paths or dynamic auto-generated styles means any minor structural change in the UI will break the test, causing false negatives.
- **Bad Practice:** Locating elements by tracing their exact position from the root of the document or using randomly generated class strings.
- **Good Practice:** Targeting elements using stable, semantic attributes, dedicated testing identifiers (`data-testid`), or relative paths that are immune to minor layout shifts.
- **Golden Rule:** If a locator looks like a complex mathematical equation, it is too fragile.

### 3. Coupled & Dependent Test Cases
- **The Problem:** Tests that rely on the state left behind by previous tests cannot be run in parallel, and a failure in one test will cause a cascading failure across the entire suite.
- **Bad Practice:** Designing a test flow where Step B requires Step A to have executed successfully in a separate test method.
- **Good Practice:** Designing fully autonomous tests where each method handles its own data setup, execution, and cleanup independently.
- **Golden Rule:** Any test must be capable of running independently, in any order, at any time.

### 4. Shared Execution State in Parallel Execution
- **The Problem:** Using globally shared execution instances causes parallel tests to override each other's commands, leading to unpredictable behavior and random crashes.
- **Bad Practice:** Declaring a single, static instance of the execution controller for the entire suite.
- **Good Practice:** Isolating the execution controller per thread using thread-safe data structures.
- **Golden Rule:** Execution controllers must be thread-local, not global.

### 5. Monolithic Page Objects ("God Classes")
- **The Problem:** Creating massive structural classes that represent entire multi-faceted screens makes the framework impossible to maintain, read, or extend.
- **Bad Practice:** Storing hundreds of element locators and behaviors for a complex application view in a single file.
- **Good Practice:** Breaking down complex views into smaller, reusable component objects (e.g., NavigationBar, DataTable, Footer) that can be assembled as needed.
- **Golden Rule:** Favor composition over inheritance; map components, not entire screens.

### 6. Preparing Test Data via the UI
- **The Problem:** Navigating through UI wizards to create prerequisite data before testing the actual feature is extremely slow and exponentially increases the risk of test failure due to unrelated UI glitches.
- **Bad Practice:** Using UI automation to click through forms merely to set up a required user account for a different test.
- **Good Practice:** Bypassing the UI and preparing test data instantly and reliably through direct API calls or secure database injections during the setup phase.
- **Golden Rule:** Reserve UI automation for validating UI features; use APIs for data setup.

### 7. Inverted Test Pyramid
- **The Problem:** Over-relying on slow, brittle end-to-end UI tests for every single validation (like form field boundary checks) drastically slows down the feedback loop for developers.
- **Bad Practice:** Automating every conceivable permutation and edge case through the browser interface.
- **Good Practice:** Pushing exhaustive logical and edge-case validations down to unit and integration layers, reserving end-to-end UI automation strictly for critical user journeys and happy paths.
- **Golden Rule:** UI tests are for critical user flows; unit tests are for exhaustive logic validation.