# data-model.md — Alphabetical "Any" Filter (TC-006)

Purpose: Describe the core entities and validation rules relevant to the feature for design and tests.

Entities
--------

1) DirectoryRecord
- Description: Represents an individual entry in the people directory returned by the UI.
- Fields:
  - id: string (unique identifier; not required to be DB id for UI tests)
  - firstName: string
  - lastName: string
  - displayName: string (concatenation or preformatted name shown in UI)
  - email: string (optional)
  - roleTitle: string (optional)
  - department: string (optional)
  - profileUrl: string (optional, link to full profile)
- Validation rules:
  - displayName must be non-empty for visible records
  - Names may contain accented characters and special letters (Ñ, á, é, etc.) — tests must assert proper rendering

2) FilterControl (AlphabetFilter)
- Description: UI control that allows selecting a letter (A-Z, Ñ) or "Any" (Cualquiera)
- Fields / API (component-level):
  - activeLetter: string (e.g., "A", "Ñ", "any")
  - selectLetter(letter: string): void
  - selectAny(): void
  - isActive(letter: string): boolean
- Validation rules:
  - Selecting "Any" sets activeLetter to "any" and the result set should include records starting with different letters
  - Switching from a letter to "Any" resets pagination to the first page (FR-005)

3) PaginationState
- Description: Tracks the current visible page and total pages.
- Fields:
  - currentPage: int (1-indexed for assertions)
  - totalPages: int
  - pageSize: int (number of records per page)
  - totalRecords: int
- Validation rules:
  - When totalRecords > pageSize, totalPages >= 2
  - After applying "Any", currentPage must be 1 (when coming from a different letter)

State transitions
-----------------
- Event: selectLetter(L)
  - Precondition: none
  - Postcondition: FilterControl.activeLetter == L, PaginationState.currentPage == 1

- Event: selectAny()
  - Precondition: none
  - Postcondition: FilterControl.activeLetter == "any", PaginationState.currentPage == 1, visible DirectoryRecords include mixed initial letters

Notes
-----
- The test harness uses fixture-driven scenarios (src/test/resources/test-data/*) for deterministic pagination and record counts. Use PersonnelDataProvider to load expected totals and pages.

