# research.md — Alphabetical "Any" Filter (TC-006)

Date: 2026-07-28
Feature: specs/004-alphabetical-filter-any/spec.md
Branch: 004-alphabetical-filter-any

Purpose
-------
Resolve open technical questions required to design the TDD implementation for the "Any" (Cualquiera) alphabetical filter and its pagination behavior.

Decisions (consolidated)
------------------------

1) Selector strategy for the alphabetical filter control

- Decision: Implement a new AlphabetFilterComponent that first attempts to select elements by a stable data attribute if present (e.g. `data-testid="alpha-filter"` or `data-letter="any"`).
- Fallback 1: select by visible link text `Cualquiera` (Spanish) using By.linkText("Cualquiera") if the application renders that exact label.
- Fallback 2: CSS selector anchored to a known container (e.g. `.menu-alfabeto a`, `.alfabeto a` or `.alphabet-filter a`) — verify in runtime via quick console inspection.

Rationale: data-* attributes are robust across presentation changes. Link text fallback is acceptable for bilingual-specified UI where the Spanish label is stable.

Evidence: repository contains DirectoryPage/PeopleSectionPage and PersonList components but no alphabet component; multiple tests reference alphabetical filters and placeholders exist (e.g., AlphabetFilterLetterATest). No explicit filter selector was found in code; docs (docs/uh/uh_tc_006.md) reference the label 'Cualquiera'.

2) Canonical URL/query parameters for filter + pagination

- Decision: Treat pagination as already implemented by PaginationComponent (it parses and uses `?page=` which is 0-indexed in URL -> 1-indexed to users). For filter state, adopt `?letter=` as the canonical query param (value: `any` for Cualquiera). Example: `/directorio/personas?letter=any&page=1`.
- Implementation note: The tests will not rely exclusively on URL query parameters; they will use Page Object API to click the filter and validate UI state and pager state. URL parsing used as a secondary assertion (when present).

Rationale: `?letter=` is explicit and easy to read; `any` is portable across locales. PaginationComponent already handles `?page=` and fallback to URL parsing is implemented.

3) Test data & seeding

- Decision: Use existing JSON fixtures under `src/test/resources/test-data/` and PersonnelDataProvider (already existing) to run deterministic scenarios. For integration or E2E against a running staging backend, further coordination may be required; but for the TDD flow we will use fixtures.

Evidence: PersonnelDataProvider lists scenarios and loads `personnel-multi-page.json`, `personnel-single-page.json`, etc.

4) Page object & component placement

- Decision: Implement new `AlphabetFilterComponent` at `src/main/java/com/project/pages/components/AlphabetFilterComponent.java` with the following API:
  - selectAny(): void
  - selectLetter(char letter): void
  - isAnySelected(): boolean
  - getActiveLetter(): String

- PeopleSectionPage will compose AlphabetFilterComponent and expose helper flows (e.g., `applyAnyFilter()` which delegates to the component and waits for records to load).

Rationale: keeps single-responsibility for filter logic, makes tests readable and POM-compliant.

5) Pagination reset behavior

- Decision: Tests will assert that switching from a letter filter (e.g., 'M' on page 5) to 'Any' resets the view to page 1 (per FR-005). This will be implemented as an explicit assertion after filter click, using PaginationComponent.getCurrentPageNumber() or URL parsing fallback.

6) Network assertions and environment

- Decision: Include optional network/HTTP response validations behind a test flag. By default the TDD unit will use fixture-driven rendering to avoid network flakiness.

Actionable outcomes
-------------------
- Implement AlphabetFilterComponent with selector-first approach described above.
- Update PeopleSectionPage to compose the component and add `applyAnyFilter()` (delegating + waiting for records).
- Create failing TDD tests in AnyFilterAndPaginationTest that use PersonnelDataProvider scenarios (multi-page fixture) to verify:
  - Clicking Any renders records and page count > 0
  - Pagination remains visible and functional
  - Switching from a letter filter to Any resets to page 1

Notes / Unanswered questions
---------------------------
- If the running staging environment uses a different label than `Cualquiera` (e.g., `Any` in English), tests should prefer data attributes. If you expect multiple locales in CI, please confirm whether data-testid attributes are present in the UI.

If you want, I can:
- Implement the AlphabetFilterComponent skeleton and the failing TDD test (create tasks and code), or
- Only produce the plan artifacts (this run) and leave implementation for a follow-up.

