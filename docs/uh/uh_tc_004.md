TC-004 - Validate navigation to "Universidades" section
User Story (SMART Format):
As a directory user, I want to click on the "Universidades" link/section so that I can navigate to that information without losing the main navigation menu.
    Specific: Successfully navigate from the directory to the "Universidades" section and verify the page load.
    Measurable: The system loads the new view without HTTP error codes and keeps the directory menu functional.
    Achievable: The link is present in the DOM and can be clicked and validated via standard automation tools.
    Relevant: Ensures connectivity between cross-sections (Priority P1), avoiding broken user flows.
    Time-bound: The test will be fully implemented and validated within the current sprint.
Acceptance Criteria:
    Open the main directory page.
    Click on the "Universidades" link or tab.
    The link must respond and initiate navigation.
    The new section must load without displaying server errors (e.g., 404, 500) or blank pages.
    The navigation must not break or incorrectly hide the main directory menu.
Tasks and Acceptance Criteria:
    Review navigation requirements and destination URL for "Universidades"
    Acceptance Criteria: Requirements documented and target URL identified.
    Identify DOM selectors for the "Universidades" link or button
    Acceptance Criteria: CSS/XPath selectors validated in the browser.
    Design test case for cross-section navigation validation
    Acceptance Criteria: Test case drafted with steps and expected assertions.
    Implement automated script to click the "Universidades" link
    Acceptance Criteria: Script correctly simulates a click on the target element.
    Implement explicit waits for target page load
    Acceptance Criteria: Script does not fail due to timeouts during view transition.
    Implement assertions to verify the target URL or section
    Acceptance Criteria: Test validates that the active view is effectively "Universidades".
    Implement HTTP error validation (404, 500) on target page
    Acceptance Criteria: Test fails if 404 or 500 errors are detected on the new page.
    Implement DOM check to ensure directory menu remains intact
    Acceptance Criteria: Navigation menu remains visible and correctly structured after load.
    Execute navigation test suite in staging environment
    Acceptance Criteria: Execution completed without infrastructure errors.
    Analyze test execution logs and debug navigation issues
    Acceptance Criteria: Results analyzed, issues properly reported or fixed.
    Consolidate test evidence and update user story status
    Acceptance Criteria: Evidence (logs/screenshots) attached and ADO ticket updated.