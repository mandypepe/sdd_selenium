# AGENTS — Quick Guide for AI Agents (sdd_selenium)

This file explains the essentials for an AI agent to be immediately productive in this Java + Maven + TestNG + Selenium repository.

## 1) Overview
- **Purpose**: End-to-End Selenium test framework in Java (Java 17, Maven). See `README.md` and `pom.xml`.
- **Main Structure**:
  - `src/main/java/com/project/drivers` — WebDriver creation and management (`DriverFactory`, `DriverManager`).
  - `src/main/java/com/project/pages` — Page Objects (e.g., `LoginPage`, `DashboardPage`). Reusable components in `pages/components`.
  - `src/main/java/com/project/utils` — Utilities (`WaitUtils`, `ReportLogger`).
  - `src/test/java` — Tests, listeners, and test data (`tests`, `listeners`, `data`).

## 2) Execution Flow and Design Rationale
- Initialization occurs in `BaseTest.setUp` (uses `DriverFactory.createDriver(browser)` and `DriverManager.setDriver`).
- `DriverManager` uses `ThreadLocal<WebDriver>` (enables parallel test execution). Always retrieve the driver with `DriverManager.getDriver()` when needed from utilities or listeners.
- Page Objects contain only interactions; assertions are in test classes (`LoginTests`, `DashboardTests`).

## 3) Build / Test / Debug Commands
- Execute the full test suite (local/CI):

```bash
mvn test
```

- Execute the suite defined in `testng.xml` (configured in the Surefire plugin). To run a single test class (quick terminal mode):

```bash
mvn -Dtest=com.project.tests.LoginTests test
```

- Requirements: Java 17 and Maven (see `pom.xml` properties). WebDriverManager will automatically download drivers.

## 4) Project-Specific Conventions and Patterns
- **Default Browser and Limited Support**: `DriverFactory` currently implements Chrome only (see line 12 in `DriverFactory.java`). To add Firefox/Edge support, extend the `createDriver` method.
- **Execution Parameters**: Parameters are injected from `testng.xml` (e.g., `baseUrl`, `browser`), not from `config/*.properties` at runtime. `config/env.qa.properties` exists as a reference.
- **Logging and Reports**: `ReportLogger.log(...)` adds attachments to Allure (if Allure is present) and also writes to stdout. Agents can use `ReportLogger` to append diagnostic information.
- **Waits**: `WaitUtils` is the wrapper used; pages use `By` locators and do not automatically perform waits. Add `WaitUtils` to Page Objects where necessary.

## 5) External Integrations and Dependencies
- **Main Maven Dependencies**: Selenium, WebDriverManager, TestNG, Allure, SLF4J-simple. See `pom.xml`:
  - `selenium-java`: 4.46.0
  - `webdrivermanager`: 5.4.1
  - `testng`: 7.8.0
  - `allure-testng`: 2.19.0
  - `slf4j-simple`: 2.0.9
- **Listeners**: `com.project.listeners.TestListener` is configured in `testng.xml` and emits basic traces to stdout.

## 6) Common Extensions an Agent Can Implement
- **Add Screenshots on Failure**: `TestListener.onTestFailure` can use `DriverManager.getDriver()` and `ReportLogger` to attach screenshots.
- **Add New Browsers**: Modify `DriverFactory.java` (path: `src/main/java/com/project/drivers/DriverFactory.java`) to support Firefox, Edge, or Safari.
- **Data Externalization**: `LoginData` is currently hardcoded; to integrate `config/env.qa.properties`, write a small utility reader in `utils` and replace static usages in tests.
- **Custom Wait Conditions**: Extend `WaitUtils` with additional wait conditions as needed for specific selectors or states.

## 7) Debug & Local Development
- **IDE Debugging**: Set a breakpoint in `BaseTest.setUp` after `DriverFactory.createDriver(browser)`. Run the test from the IDE (TestNG runner) or use `mvn -Dtest=... -DskipTests=false test`.
- **Inspect CI Failures**: Look for listener output and Allure attachments (if configured). `ReportLogger` already adds attachments that Allure can understand.

## 8) Gotchas and Recommendations
- **Selector Stability**: Tests assume the page has specific selectors (`id="login-btn"`, `h1.dashboard-title`). If the AUT (Application Under Test) changes, update `LoginPage`/`DashboardPage`.
- **Test Data**: `LoginData` contains example credentials; do not rely on these values for production/real data. Use environment-based configuration.
- **Configuration Management**: `testng.xml` controls parameters; changing `config/*.properties` does not affect execution until properties loading is implemented.
- **Base URL**: Currently set to `https://www.uci.cu/index.php/directorio/personas` in `testng.xml` — update as needed for your target environment.

## 9) Where to Find More Information
- **Driver and Parallelization Changes**: `src/main/java/com/project/drivers/*`
- **Test Entry Points**: `src/test/java/com/project/tests/*` and `testng.xml`
- **Report Hooks**: `src/main/java/com/project/utils/ReportLogger.java` and `src/test/java/com/project/listeners/TestListener.java`
- **Page Objects**: `src/main/java/com/project/pages/LoginPage.java`, `DashboardPage.java`, and components under `pages/components/`

## 10) Implementation Tasks for Agents
For specific modifications (e.g., add screenshots on failures, parameterize via properties, support multiple browsers), request the task and provide the required changes with tests:
## Spec Kit Flow Autonomy
- Treat `.github/agents/*.agent.md` and `.github/prompts/*.prompt.md` as the authoritative flow definitions for Spec Kit, and execute them directly.
- Do not require `speckit.orchestrator` as a mandatory entry point. Select the correct phase yourself from user intent, the current artifact set under `.specify/` and `specs/`, and the strict phase order below.
- Preserve the exact phase order:
  1. `speckit.constitution`
  2. `speckit.specify`
  3. `speckit.clarify`
  4. `speckit.plan`
  5. `speckit.tasks`
  6. `speckit.analyze`
  7. `speckit.checklist`
  8. `speckit.implement`
- If the user asks for a later phase but a prerequisite artifact is missing, stop and recommend the previous required phase instead of guessing or skipping ahead.
- If the user provides a specific file parameter (for example `--file [path]`), use that file as the target when the phase supports it; otherwise infer the active feature from the workspace artifacts.
- Never ask the user to switch agents manually. Pick the phase, validate prerequisites, and continue.
## Spec Kit Flow Navigation
- Treat the following phase order as the canonical navigation path:
  1. `/speckit.constitution`
  2. `/speckit.specify`
  3. `/speckit.clarify`
  4. `/speckit.plan`
  5. `/speckit.tasks`
  6. `/speckit.analyze`
  7. `/speckit.checklist`
  8. `/speckit.implement`
- When a phase completes, always suggest the next valid phase in the sequence instead of leaving the user to infer it.
- If a phase prompt or `agent.md` conflicts with this file, use the `agent.md` for execution behavior, and use this file as the navigation guide for phase ordering and prerequisites.
- If the required prerequisite artifact for a requested phase is missing, do not skip ahead; recommend the immediately previous required phase.

### Canonical Handoffs

| Current phase | Next valid phase(s) |
|---|---|
| `speckit.constitution` | `speckit.specify` |
| `speckit.specify` | `speckit.clarify` / `speckit.plan` |
| `speckit.clarify` | `speckit.plan` |
| `speckit.plan` | `speckit.tasks` |
| `speckit.tasks` | `speckit.analyze` / `speckit.implement` |
| `speckit.analyze` | `speckit.checklist` |
| `speckit.checklist` | `speckit.implement` |
### Phase Contracts
#### 1. `speckit.constitution`
- **Input**: governance or principle updates, constitutional change requests.
- **Output**: `.specify/memory/constitution.md`.
- **Validate**: no unresolved placeholders, version/date updated, dependent templates remain aligned.
- **Notes**: update synced templates or guidance if the constitution changes required behavior.
#### 2. `speckit.specify`
- **Input**: feature description; optional `--file [path]` when the source text is stored in a file.
- **Output**: `specs/<feature>/spec.md` and the feature checklist.
- **Validate**: spec is complete, testable, bounded, and free of more than three unresolved clarifications.
- **Notes**: infer branch/feature name from the description when not supplied; keep the result focused on user value.
#### 3. `speckit.clarify`
- **Input**: existing `specs/<feature>/spec.md`; optional `--file [path]` to target a specific spec file.
- **Output**: updated `specs/<feature>/spec.md` with clarifications recorded inline.
- **Validate**: clarifications resolve real ambiguity, contradictions are removed, `[NEEDS CLARIFICATION]` markers are eliminated or reduced to the smallest justified set, and the spec remains internally consistent.
- **Notes**: ask only when the uncertainty materially changes scope, UX, data, or validation; resolve each `[NEEDS CLARIFICATION: ...]` marker inline in the spec and do not leave stale alternatives behind.
#### 4. `speckit.plan`
- **Input**: clarified `specs/<feature>/spec.md` plus constitution; optional `--file [path]` if a specific spec should be planned.
- **Output**: `specs/<feature>/plan.md` and, when applicable, `research.md`, `data-model.md`, `contracts/`, `quickstart.md`.
- **Validate**: constitution check passes, technical assumptions are documented, and design artifacts match the spec.
- **Notes**: resolve unknowns before design; keep architecture aligned with the repo's existing layers and conventions.
#### 5. `speckit.tasks`
- **Input**: `specs/<feature>/spec.md` and `specs/<feature>/plan.md`; optional supporting artifacts (`research.md`, `data-model.md`, `contracts/`, `quickstart.md`).
- **Output**: `specs/<feature>/tasks.md`.
- **Validate**: tasks are dependency-ordered, immediately executable, grouped by user story, and use the required checkbox/ID/story format.
- **Notes**: include only tasks that can be executed without extra context.
#### 6. `speckit.analyze`
- **Input**: `specs/<feature>/spec.md`, `specs/<feature>/plan.md`, `.specify/memory/constitution.md`, and `specs/<feature>/tasks.md`.
- **Output**: read-only analysis report.
- **Validate**: detect inconsistencies, duplication, missing coverage, and constitution conflicts without modifying files.
- **Notes**: if tasks are incomplete or missing, stop and request the missing prerequisite phase.
#### 7. `speckit.checklist`
- **Input**: current feature requirements plus optional plan/tasks context; optional `--file [path]` if a specific spec is targeted.
- **Output**: `specs/<feature>/checklists/*.md`.
- **Validate**: checklist items test the quality of the requirements, not the implementation.
- **Notes**: create requirement-quality checks for clarity, completeness, consistency, measurability, and coverage.
#### 8. `speckit.implement`
- **Input**: `specs/<feature>/tasks.md` plus supporting artifacts as needed.
- **Output**: code changes that satisfy the task list.
- **Validate**: each task is completed in dependency order, existing build/lint gates pass, and task checkboxes are updated.
- **Notes**: use extension hooks when present; do not drift outside the planned scope.
#### 9. `speckit.taskstoissues`
- **Input**: completed `specs/<feature>/tasks.md`.
- **Output**: GitHub issues in the repository that matches the remote.
- **Validate**: only proceed when the remote is a GitHub URL and the repository matches exactly.
## 2) `/speckit.specify`
### Primary Input
- Free-form feature description text (`$ARGUMENTS`).
### Optional Input
- `AGENTS.md` defines support for `--file [path]` to load the feature description from a file.
### Execution Flow
1. Checks `hooks.before_specify` in `.specify/extensions.yml`.
  - In this repository, the file does not exist, so this step is skipped.
2. Generates a short name using 2 to 4 words.
3. Runs:
```bash
.specify/scripts/bash/create-new-feature.sh --json --short-name "<name>" "<feature>"
```
- If `branch_numbering=timestamp`, it adds `--timestamp`.
- In this repository, `init-options.json` indicates sequential numbering.
4. Creates the branch, the `specs/<branch>/` folder, and `spec.md` from the template.
5. Completes the specification and creates the quality checklist:
  - `specs/<branch>/checklists/requirements.md`
### Outputs / Artifacts
- Script JSON output:
  - `BRANCH_NAME`
  - `SPEC_FILE`
  - `FEATURE_NUM`
- File: `specs/<feature>/spec.md`
- File: `specs/<feature>/checklists/requirements.md`
### Detailed Validations
- Maximum of 3 `[NEEDS CLARIFICATION]` markers.
- Iterates up to 3 times to improve specification quality.
- If critical questions remain, asks them in a single block with A/B/C options.
### Common Errors
- Empty description.
- Branch name collision.
- Invalid branch or not being on a feature branch during later validations.
### Expected Handoff
- `/speckit.clarify` or `/speckit.plan`
---

## 3) `/speckit.clarify`

### Primary Input
- Active specification: `specs/<feature>/spec.md`, derived by `check-prerequisites`.

### Optional Input
- `--file [path]` to point to another specification file.

### Execution Flow
1. Runs:

```bash
.specify/scripts/bash/check-prerequisites.sh --json --paths-only
```

2. Analyzes ambiguity using a taxonomy such as:
  - Scope
  - Data
  - UX
  - Non-functional requirements
  - Integration
  - Edge cases
  - Other relevant categories
3. Asks one question at a time, with a maximum of 5 total questions.
4. Inserts the answers into `## Clarifications` and updates the affected sections in the specification.

### Outputs / Artifacts
- Incrementally updated `spec.md`.
  - The file is saved after each answer.
- Report with the following categories:
  - `Resolved`
  - `Deferred`
  - `Clear`
  - `Outstanding`

### Critical Details
- If there is no high-impact ambiguity, it finishes without asking questions.
- It does not create a new specification if one is missing.
  - In that case, `/speckit.specify` must be executed first.

### Expected Handoff
- `/speckit.plan`

---

## 4) `/speckit.plan`

### Primary Input
- `spec.md`
- Constitution file: `.specify/memory/constitution.md`

### Execution Flow
1. Executes `before_plan` hooks, if present.
2. Runs:

```bash
.specify/scripts/bash/setup-plan.sh --json
```

3. Reads the `plan-template.md` template.
4. Phase 0: generates `research.md`.
5. Phase 1: generates:
  - `data-model.md`
  - `contracts/`
  - `quickstart.md`
6. Runs the following command to update the agent context:

```bash
.specify/scripts/bash/update-agent-context.sh copilot
```

- In Copilot, this points to `.github/copilot-instructions.md`.

### Outputs / Artifacts
- `specs/<feature>/plan.md`
- `specs/<feature>/research.md`
- `specs/<feature>/data-model.md`
- `specs/<feature>/contracts/`
- `specs/<feature>/quickstart.md`

### `setup-plan` JSON Output
- `FEATURE_SPEC`
- `IMPL_PLAN`
- `SPECS_DIR`
- `BRANCH`
- `HAS_GIT`

### Common Errors
- Constitutional gates are not justified.
- Technical clarifications remain unresolved.

### Expected Handoff
- `/speckit.tasks`
- Optionally, checklist generation.

---

## 5) `/speckit.tasks`

### Primary Input
Mandatory:
- `plan.md`
- `spec.md`

Optional:
- `research.md`
- `data-model.md`
- `contracts/`
- `quickstart.md`

### Execution Flow
1. Executes `before_tasks` hooks, if present.
2. Runs:

```bash
.specify/scripts/bash/check-prerequisites.sh --json
```

3. Generates `tasks.md`, organized by:
  - User stories (`US1`, `US2`, ...)
  - Dependencies
  - Parallelization opportunities

### Outputs / Artifacts
- `specs/<feature>/tasks.md`

### Strict Task Line Format

```markdown
- [ ] T### [P?] [US#?] Description including file path
```

### Key Details
- Tests are optional unless explicitly requested in the specification.
- Required phases:
  1. Setup
  2. Foundational
  3. User Stories
  4. Polish

### Expected Handoff
- `/speckit.analyze` or `/speckit.implement`

---

## 6) `/speckit.analyze`

### Primary Input
- `spec.md`
- `plan.md`
- `tasks.md`
- Constitution file

### Execution Flow
1. Runs:

```bash
.specify/scripts/bash/check-prerequisites.sh --json --require-tasks --include-tasks
```

2. Performs a read-only analysis of:
  - Duplicates
  - Ambiguities
  - Coverage gaps
  - Conflicts with the constitution
  - Inconsistencies

### Output
- Markdown report in the console.
- It does not modify files.

The report includes:
- Findings table with:
  - `ID`
  - `Severity`
  - `Location`
  - `Recommendation`
- Requirement-to-task coverage mapping.
- Metrics such as:
  - Coverage percentage
  - Ambiguity count
  - Critical finding count

### Critical Details
- Conflicts with constitutional `MUST` requirements are classified as `CRITICAL`.
- Findings are limited to 50 items, followed by an overflow summary if needed.
- At the end, it offers a remediation plan but does not apply changes.

### Expected Handoff
- Correct the affected artifacts, or continue with `/speckit.implement`.

---

## 7) `/speckit.checklist`

### Primary Input
- Feature context.
- Domain argument, for example:
  - Security
  - UX
  - Performance

### Execution Flow
1. Runs:

```bash
.specify/scripts/bash/check-prerequisites.sh --json
```

2. Asks up to 3 initial questions.
3. Asks up to 2 follow-up questions if needed.
4. Generates a checklist at:

```text
specs/<feature>/checklists/<domain>.md
```

### Outputs / Artifacts
- New checklist file, or appended content in the existing checklist.
- Sequential `CHK###` IDs.

### Most Important Detail
- This command evaluates requirements quality, including:
  - Clarity
  - Completeness
  - Measurability

It does **not** validate functional implementation tests.

### Strong Rules
- It must not replace existing content; it only appends.
- At least 80% of items must include traceability references, such as:
  - `[Spec §X]`
  - `[Gap]`
  - Other equivalent references

---

## 8) `/speckit.implement`

### Primary Input
Mandatory:
- `tasks.md`

Additional feature artifacts are also used when available.

### Execution Flow
1. Executes `before_implement` hooks, if present.
2. Runs:

```bash
.specify/scripts/bash/check-prerequisites.sh --json --require-tasks --include-tasks
```

3. If `checklists/` exists, calculates completed versus pending checklist items.
4. If pending checklist items exist, stops and asks for confirmation before continuing.
5. Executes tasks according to phases and dependencies.
6. Marks completed tasks as `[X]` in `tasks.md`.
7. Executes `after_implement` hooks, if present.

### Outputs / Artifacts
- Code changes.
- Updated `tasks.md`.

### Detailed Rules
- Enforces phase order.
- Respects `[P]` only when there are no file conflicts or dependency conflicts.
- Includes validation of ignore files according to the detected stack, such as:
  - `.gitignore`
  - `.dockerignore`
  - Other relevant ignore files

### Errors / Blocking Conditions
- Missing `tasks.md` or missing prerequisites.
- Incomplete checklist without explicit consent to continue.

- If a phase prompt and the agent prompt differ, follow the agent file for execution behavior and use the prompt file only as the command wrapper.
## Framework Architecture & Best Practices Requirements:
      1. **Page Object Model (POM) & OOP Principles**:
         - Create an abstract `BasePage` class encapsulating standard WebDriver interactions (`click`, `type`, `waitForElement`).
         - Strictly enforce **Abstraction** (hiding `WebDriver` and `WebDriverWait` logic inside `BasePage`) and **Encapsulation** (keeping `By` locators `private` inside page objects and exposing only `public` business action methods).
      2. **Selenium Manager Integration**:
         - Leverage native Selenium 4.6+ driver management. Do NOT include third-party `WebDriverManager` libraries or manual driver executables (`chromedriver.exe`).
      3. **Advanced Synchronization**:
         - Completely eliminate `Thread.sleep()`.
         - Implement wrapper methods for `WebDriverWait` and `FluentWait` inside `BasePage` to gracefully handle dynamic UI states and transient exceptions (e.g., `StaleElementReferenceException`).
      4. **Data-Driven Testing (DDT)**:
         - Decouple all test data from code logic. Use TestNG `@DataProvider` (or JUnit 5 `@ParameterizedTest`).
         - Externalize test data into JSON or Properties files, parsing them using Jackson or Gson.
      5. **ThreadLocal Driver & Clean Teardown**:
         - Wrap the `WebDriver` instance inside a `ThreadLocal<WebDriver>` thread-safe manager class to support parallel test execution without state pollution.
         - Enforce a resilient `@AfterMethod` / `@AfterEach` teardown that executes `driver.quit()` safely to prevent orphaned browser processes.
      6. **Reporting & Test Listeners**:
         - Integrate Allure Reports / ExtentReports.
         - Create a custom `ITestListener` implementation that listens for `onTestFailure` and automatically captures screenshots using `TakesScreenshot` to attach to the execution report.
## Anti-Patterns to Address:
      1. **Fixed Wait Times (`Thread.sleep`)** → Replace with `WebDriverWait` and `ExpectedConditions`.
      2. **Fragile Locators (Absolute XPaths / Auto-generated CSS)** → Replace with resilient locators (`data-testid`, relative XPaths, stable attributes).
      3. **Coupled & Dependent Test Cases** → Redesign for full test autonomy with isolated `setup()` and `teardown()`.
      4. **Static `WebDriver` Instances in Parallel Execution** → Implement `ThreadLocal<WebDriver>` for thread-safe parallel execution.
      5. **Monolithic Page Objects ("God Classes")** → Refactor into the Component Object Model or Screenplay Pattern.
      6. **Preparing Test Data via the UI** → Shift data setup to REST APIs or direct Database insertion.
      7. **Inverted Test Pyramid (Over-testing at UI Layer)** → Rebalance validations across Unit, API, and UI layers (reserving Selenium for E2E happy paths).
      ### Required Structure for Each Anti-Pattern:
      - **The Problem:** Explain why it degrades suite speed, reliability, or maintainability.
      - **Bad Practice (Anti-Pattern Code):** A realistic Java snippet showing the flawed implementation.
      - **Good Practice (Refactored Code):** A clean Java snippet demonstrating the recommended solution (using modern conventions with JUnit 5 / TestNG).
      - **Golden Rule:** A single concise summary principle for automation engineers.
      ### Constraints & Formatting:
      - Ensure all Java code is readable, idiomatic, and adheres to modern Selenium 4 standards.
      - Keep theoretical explanations concise and actionable.