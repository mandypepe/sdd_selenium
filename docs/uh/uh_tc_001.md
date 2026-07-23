TC-001 - Load people directory page successfully
User Story (SMART Framework):
As a system user, I want to access the main people directory page so that I can view the initial list of available records.
    Specific: Load the target URL ([https://www.uci.cu/index.php/directorio/personas](https://www.uci.cu/index.php/directorio/personas)) and verify data rendering on the UI.
    Measurable: The load must occur without HTTP errors and display at least the expected initial records (e.g., Abel Velázquez).
    Achievable: This is a public, read-only endpoint, making technical validation straightforward and accessible.
    Relevant: Validating this page is a critical flow to ensure the operability of the institutional contact search.
    Time-bound: This test case can be fully implemented and validated within the current iteration or sprint.
User Story Acceptance Criteria:
    Navigate to [https://www.uci.cu/index.php/directorio/personas](https://www.uci.cu/index.php/directorio/personas).
    The page must load completely without displaying visible HTTP errors.
    The page title and the "Directorio" section must be clearly visible.
    The "Personas" section must be present in the main content area.
    An initial list with real names (e.g., "Abel Velázquez Pratts", "Ada Isabel Llaneras Pulido") must be displayed.
Tasks and Acceptance Criteria:
    Review testing requirements and environment setup
    Acceptance Criteria: Testing environment is fully configured and technical requirements are clearly understood.
    Design test case for directory page load validation
    Acceptance Criteria: Step-by-step test case is documented, reviewed, and approved.
    Implement automated navigation script to directory URL
    Acceptance Criteria: Script successfully opens the browser and loads the target URL.
    Implement HTTP status code validation
    Acceptance Criteria: Status code 200 or OK validation is correctly integrated into the test script.
    Implement page title and directory section verification
    Acceptance Criteria: The test accurately locates and validates the expected title elements.
    Implement 'Personas' section visibility check
    Acceptance Criteria: The test appropriately fails if the "Personas" UI component is missing from the DOM or hidden.
    Implement data extraction for initial user list validation
    Acceptance Criteria: The test verifies the existence of specific records (e.g., Abel Velázquez) within the grid.
    Execute test suite in staging environment
    Acceptance Criteria: Test suite executed entirely without interruptions or false positives.
    Review test results and debug potential issues
    Acceptance Criteria: All logs reviewed; any failures are properly analyzed and reported.
    Document test evidence and finalize report
    Acceptance Criteria: Evidence (screenshots, logs, or video) is attached to the final report and linked to the story.