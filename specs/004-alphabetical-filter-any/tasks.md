# Tasks for feature: Alphabetical "Any" Filter (TC-006)

Feature branch: 004-alphabetical-filter-any
Feature path: specs/004-alphabetical-filter-any
Implementation style: TDD-first (create failing tests first, implement code, verify tests pass)

PHASE 1 — Setup

- [ ] T001 Initialize feature branch 004-alphabetical-filter-any (ensure checkout) — path: specs/004-alphabetical-filter-any/
- [ ] T002 Create this tasks file at specs/004-alphabetical-filter-any/tasks.md (add the TDD-first checklist below) — path: specs/004-alphabetical-filter-any/tasks.md

PHASE 2 — Foundational (blocking prerequisites)

- [ ] T003 [P] Create AlphabetFilterComponent skeleton in src/main/java/com/project/pages/components/AlphabetFilterComponent.java with package `com.project.pages.components` and public methods: `void selectAny()`, `void selectLetter(String letter)`, `String getActiveLetter()`, `boolean isAnySelected()`; methods may throw UnsupportedOperationException or return a safe default so tests compile — path: src/main/java/com/project/pages/components/AlphabetFilterComponent.java

- [ ] T004 [P] Add composition to PeopleSectionPage: add `private final AlphabetFilterComponent alphabetFilterComponent;` initialize in constructor and add delegating methods `public void applyAnyFilter()` and `public void applyLetterFilter(String letter)` that call the component and then `waitForRecordsToLoad()` — file to change: src/main/java/com/project/pages/PeopleSectionPage.java

- [ ] T005 [P] Add minimal contract note (no-op if already present) verifying the AlphabetFilterComponent contract in specs/004-alphabetical-filter-any/contracts/page-objects.md — path: specs/004-alphabetical-filter-any/contracts/page-objects.md

PHASE 3 — User Stories (priority order)

User Story US1 (P1) — View General Directory Listing
- Goal: Selecting "Any" shows the full/unfiltered directory and does not render an empty result set.
- Independent Test Criteria: From any letter filter state, selecting Any results in a visible list with > 0 records and at least two distinct initial letters across returned names.

TDD cycle 1 (US1)
- [ ] T006 [US1] Create a failing TestNG test class `AnyFilterDisplaysGeneralListTest` (test name) that uses the fixture-driven harness. File: src/test/java/com/project/tests/AnyFilterDisplaysGeneralListTest.java. Test should:
    - navigate to the directory page (use existing Page Objects)
    - call `PeopleSectionPage.applyAnyFilter()`
    - wait for records via `PeopleSectionPage.waitForRecordsToLoad()`
    - assert `PeopleSectionPage.hasRecords()` is true
    - assert that `PeopleSectionPage.getVisibleRecords()` returns names with at least two distinct initial characters (compute initials from names)

- [ ] T007 [US1] Run the failing test to confirm it fails initially (command context: pom.xml). Command example to run and observe failure: `mvn -Dtest=com.project.tests.AnyFilterDisplaysGeneralListTest test` — path: pom.xml

- [ ] T008 [US1] Implement `AlphabetFilterComponent.selectAny()` (and necessary selector fallbacks) in src/main/java/com/project/pages/components/AlphabetFilterComponent.java. Implementation details:
    - Try locating the control by stable attribute: `By.cssSelector("[data-letter='any']")` or `By.cssSelector("[data-testid='alpha-filter'] [data-letter='any']")`
    - Fallback to `By.linkText("Cualquiera")` (Spanish label) if data-* selector not present
    - After clicking, wait for PeopleSectionPage.waitForRecordsToLoad() (or wait for view-content to be present)
    - Keep the implementation defensive (catch and wrap exceptions in a meaningful error)

- [ ] T009 [US1] Update PeopleSectionPage.applyAnyFilter() to delegate to `alphabetFilterComponent.selectAny()` and then `waitForRecordsToLoad()`; update imports and compile — file: src/main/java/com/project/pages/PeopleSectionPage.java

- [ ] T010 [US1] Re-run the test and verify it passes (command context: pom.xml). If failures remain, iterate on selectors or synchronization until the assertions in T006 pass — path: pom.xml

User Story US2 (P2) — Pagination Integrity with General List
- Goal: When the Any filter is active, pagination controls remain functional and switching from a letter filter to Any resets to page 1.
- Independent Test Criteria: With the multi-page fixture, applying Any reports the expected total pages and allows navigating to subsequent pages while preserving the Any selection.

TDD cycle 2 (US2)
- [ ] T011 [US2] Create a failing TestNG test class `AnyFilterPaginationTest` at src/test/java/com/project/tests/AnyFilterPaginationTest.java. Test should be DataProvider-driven using `PersonnelDataProvider.paginationScenarios()` and, for each scenario:
    - Apply Any filter via `PeopleSectionPage.applyAnyFilter()`
    - Assert `PeopleSectionPage.getTotalPages()` equals `PersonnelDataProvider.getExpectedTotalPages(...)`
    - Click `PeopleSectionPage.goToNextPage()` and assert `PeopleSectionPage.getCurrentPageNumber()` increments and `PeopleSectionPage.hasRecords()` remains true
    - Assert the active filter remains Any (via `AlphabetFilterComponent.isAnySelected()`/`getActiveLetter()`)

- [ ] T012 [US2] Run the failing test to confirm it fails initially (command context: pom.xml). Command example: `mvn -Dtest=com.project.tests.AnyFilterPaginationTest test` — path: pom.xml

- [ ] T013 [US2] Implement pagination-reset behavior when switching filters. Options (choose one consistent approach):
    - In `AlphabetFilterComponent.selectAny()` call `driver.get()` or update query param to `?letter=any&page=0` when applicable, or
    - In `PeopleSectionPage.applyAnyFilter()` after delegating to the component, call `paginationComponent.goToPage(1)` to ensure visible page is the first. Update files:
      - src/main/java/com/project/pages/components/AlphabetFilterComponent.java
      - src/main/java/com/project/pages/PeopleSectionPage.java

- [ ] T014 [US2] Re-run the pagination tests and verify they pass for all DataProvider scenarios (command context: pom.xml). If flakey, add waits or URL parsing fallback assertions — path: pom.xml

PHASE 4 — Polish & Cross-Cutting Concerns

- [ ] T015 [P] Add a lightweight contract test for the AlphabetFilterComponent at src/test/java/com/project/tests/contracts/AlphabetFilterComponentContractTest.java that asserts method signatures and basic non-empty behaviour (isAnySelected() does not throw, selectAny() returns control to Page Object) — path: src/test/java/com/project/tests/contracts/AlphabetFilterComponentContractTest.java

- [ ] T016 [P] Update quickstart.md with a short section describing how to run the new tests locally and which fixtures to use (edit: specs/004-alphabetical-filter-any/quickstart.md) — path: specs/004-alphabetical-filter-any/quickstart.md

- [ ] T017 [P] Improve failure observability: add screenshot-on-failure to the TestNG TestListener or enhance ReportLogger to attach screenshots for these tests (file(s): src/main/java/com/project/utils/ReportLogger.java and/or src/test/java/com/project/listeners/TestListener.java) — path: src/main/java/com/project/utils/ReportLogger.java

DEPENDENCIES (implicit ordering)

- Phase 1 -> Phase 2 -> US1 -> US2 -> Polish
- Example execution order (serial): T001 -> T003/T004 (foundational) -> T006 -> T007 -> T008 -> T009 -> T010 -> T011 -> T012 -> T013 -> T014 -> T015 -> T016 -> T017

PARALLEL OPPORTUNITIES (examples)

- [P] T003 (component skeleton) and T004 (PeopleSectionPage composition) can be developed in parallel with careful coordination if changes are merged together before T006.
- [P] T006 (writing tests for US1) and T011 (writing tests for US2) can be authored in parallel because they are test files in different classes and use different assertions — implementation may block them from passing, but writing tests is parallelizable.

IMPLEMENTATION STRATEGY

- MVP scope: Implement US1 first (T006-T010). If timeboxed, stop after T010 (Any filter shows non-empty general list and tests green).
- Incremental delivery: keep each TDD cycle small (create failing test -> implement minimal code -> run & fix) so CI feedback is quick.
- Keep Page Objects thin: component encapsulates selector strategy; PeopleSectionPage only composes and delegates.

FORMAT VALIDATION

- All tasks above follow the required checklist line format `- [ ] T### [P?] [US#?] Description with file path`.

Notes & Clarifications required (if any)

- Selector strategy: prefer `data-letter` or `data-testid` attributes. If those are not present in the runtime UI, the implementer should use the `By.linkText("Cualquiera")` fallback described in research.md and adjust timeouts.
- If you prefer me to also implement the code changes (component + page object + tests) I can proceed after you confirm.

