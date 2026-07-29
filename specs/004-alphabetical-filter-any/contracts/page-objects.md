# contracts/page-objects.md

Purpose: Define the interface contract for the Page Objects and Components required by the "Any" alphabetical filter feature.

AlphabetFilterComponent (contract)
----------------------------------
Location: src/main/java/com/project/pages/components/AlphabetFilterComponent.java

Public API:
- void selectAny()
  - Behaviour: Clicks the UI control for the "Any" option (Cualquiera) and waits for the personnel list to refresh.
- void selectLetter(String letter)
  - Behaviour: Clicks the UI control for the requested letter and waits for the personnel list to refresh.
- String getActiveLetter()
  - Returns the currently active letter identifier (e.g., "A", "Ñ", "any").
- boolean isAnySelected()
  - Returns true when the "Any" option is currently active.

Implementation notes:
- Prefer locating elements using stable attributes (e.g., `data-testid` or `data-letter`).
- Synchronization: implementations must wait for the PeopleSectionPage.waitForRecordsToLoad() signal where appropriate (or use WebDriverWait on the expected DOM change).

PeopleSectionPage (extension)
-----------------------------
Additions to existing PeopleSectionPage API (contract):
- void applyAnyFilter()
  - Behaviour: Delegates to AlphabetFilterComponent.selectAny() and then waits for records to load.
- void applyLetterFilter(String letter)
  - Behaviour: Delegates to AlphabetFilterComponent.selectLetter(letter) and waits for records.

Preconditions & Postconditions
- Preconditions: Page must be visible and PeopleSectionPage.waitForRecordsToLoad() must succeed before applying filters in the test flow.
- Postconditions: After applying a filter, PaginationComponent.getCurrentPageNumber() should be 1 (reset behavior) when switching filters.

Test contracts
--------------
Tests will assert the following contract-level behaviors (not implementation internals):
- selectAny() results in a non-empty personnel list when fixtures contain records.
- Pagination controls remain visible and functional after selectAny().
- Switching from a letter filter to Any resets the page to 1.

