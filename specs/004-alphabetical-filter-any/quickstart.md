# quickstart.md — Validate "Any" Filter (TC-006)

Purpose: Minimal instructions to run the validation scenarios that prove the feature works end-to-end using the existing automation framework.

Prerequisites
- Java 17 and Maven installed
- Project dependencies resolved: `mvn -q -DskipTests=false test-compile`
- Optional: A running instance of the web application (staging) if you want integration tests; otherwise tests run using fixture-driven data.

Run the TDD validation (fixture-driven)
1. Build tests: mvn -DskipTests=false test-compile
2. Execute the feature test(s):
   - Run only AnyFilterAndPaginationTest (placeholder will be replaced with real tests):
     `mvn -Dtest=com.project.tests.uh.AnyFilterAndPaginationTest test`
3. Expected outcomes:
   - Tests fail initially (TDD) asserting the missing component / selector behavior
   - After implementing the AlphabetFilterComponent and wiring to PeopleSectionPage, tests should pass using the fixture files in `src/test/resources/test-data/`.

Debugging tips
- Open browser console and validate that the alphabetical filter includes a stable attribute (data-testid or data-letter).
- If selectors differ across environments, add environment guards or prefer data-* attributes in the app for test stability.

Notes
- This quickstart is intentionally lightweight: implementation details are in code and contracts.
- Do not include production data or credentials in tests; use fixture JSON.

