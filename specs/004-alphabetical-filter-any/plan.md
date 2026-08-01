# Implementation Plan: Alphabetical "Any" Filter (TC-006)

Feature spec: specs/004-alphabetical-filter-any/spec.md
Branch: 004-alphabetical-filter-any

---

## 1. Summary

This implementation plan translates the business-focused Feature Spec (TC-006) into a short, test-first implementation roadmap for the automation team. Deliverables: plan.md (this file), research.md, data-model.md, contracts/*, quickstart.md, and tasks.md (implementation tasks created in a follow-up).

## 2. Technical Context

- Test framework: Java + Maven + TestNG + Selenium (project constitution)
- Page Object strategy: POM (Page Objects + components) — enforced by constitution
- Where tests live: src/test/java/com/project/tests/
- Test data: src/test/resources/test-data/ (JSON fixtures) — available

Open/unknown items (marked as NEEDS CLARIFICATION):

- Filter control DOM locators: NEEDS CLARIFICATION
  - Rationale: The repository includes PeopleSectionPage and PersonList components but no explicit AlphabetFilter component. Tests in this area are placeholders. Research will decide the selector strategy and component API.

- Deep linking / query parameter format for paging & filter state: NEEDS CLARIFICATION
  - Rationale: Pagination component parses ?page= N for navigation. The canonical query param for the alphabetical filter (e.g., `?letter=A` or `?alpha=Cualquiera`) is not formally documented in code. Research will recommend a canonical approach and fallback handling.

- Test environment & data seeding method: RESOLVED (see research.md)
  - Rationale: The repository provides JSON fixtures under src/test/resources/test-data which are consumed by data providers. We'll use these fixtures for deterministic test scenarios.

## 3. Constitution Check (gates)

Refer to .specify/memory/constitution.md for governing principles. Summary of applicable gates:

- POM abstraction: tests must call Page Objects only. (COMPLIANT — repo follows POM; existing DirectoryPage/PeopleSectionPage/Components present)
- ThreadLocal driver management: must use DriverManager (COMPLIANT/OK)
- Test-first discipline: tests must be written first (we will create failing tests first as part of implementation tasks)
- Wait synchronization: use WaitUtils / WebDriverWait; no Thread.sleep (COMPLIANT — components use WebDriverWait)
- Reporting & Observability: Allure attachments & screenshots on failure (PARTIAL — TestListener needs screenshot enhancement; implement as separate task)

Gate evaluation: No blocking constitutional violations identified for this plan. The plan will adhere to POM and Wait rules. Any related infra improvements (TestListener screenshots) are tracked separately and do not block this feature.

## 4. Phase 0 — Outline & Research

Goal: Resolve NEEDS CLARIFICATION items and choose precise selectors & test strategies before design.

Research tasks (generated into research.md):

- Research 1: Identify or define stable selectors for the alphabetical filter control. Decision: prefer data-testid attributes when available. Fallback: link text 'Cualquiera' (Spanish) or a CSS selector matching the filter bar (e.g., `.alfabeto a[data-letter="any"]` or `.alfabeto a:contains("Cualquiera")`).

- Research 2: Confirm canonical URL/query parameter format for filter state and pagination. Decision: pagination uses `?page=` (0-indexed) as implemented by PaginationComponent; for filter state prefer `?letter=` (string) — research will include URL parsing fallback logic.

- Research 3: Validate test-data fixtures and DataProvider usage. Decision: use existing src/test/resources/test-data/*.json fixtures via PersonnelDataProvider.paginationScenarios to produce deterministic scenarios.

- Research 4: Identify where to place new Page Object / Component: implement AlphabetFilterComponent under src/main/java/com/project/pages/components/ with API `selectAny()`, `selectLetter(char)`, `getActiveLetter()`.

Phase 0 outcome: research.md created and decisions recorded. All previously marked NEEDS CLARIFICATION will be resolved there.

## 5. Phase 1 — Design & Contracts

Prerequisite: research.md completed with decisions.

Planned artifacts to create (in this feature folder):
- data-model.md — entity definitions for DirectoryRecord, FilterControl, PaginationState
- contracts/page-objects.md — interface contract for new AlphabetFilterComponent and any additions to PeopleSectionPage
- quickstart.md — minimal validation guide to run tests for this feature

Design tasks (high level):
- Create AlphabetFilterComponent (component API in contracts)
- Add methods to PeopleSectionPage to interact with AlphabetFilterComponent (composition)
- Write failing tests (TDD): AnyFilterAndPaginationTest (expanded) using PersonnelDataProvider fixtures
- Implement page object methods and synchronization
- Run tests locally and iterate until green

## 6. Risks & Mitigations

- Selector fragility: mitigate by preferring data-* attributes and creating robust CSS/XPath fallbacks.
- Environment differences (staging vs. local): mitigate by using fixtures for deterministic tests and guarding external network assertions behind environment flags.

## 7. Output

Planned generated files (this run):
- plan.md (this file)
- research.md
- data-model.md
- contracts/page-objects.md
- quickstart.md

Next steps: create these artifacts (research.md -> resolve clarifications), then produce tasks.md and implement tests. Commit artifacts to branch `004-alphabetical-filter-any`.
