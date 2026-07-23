TC-009 - Validate 'Ñ' special character filtering support
User Story (SMART Format)
As a people directory user, I want to select the letter 'Ñ' in the alphabetical filter so that I can view matching records without breaking the URL, character encoding, or UI layout.
    Specific: Validate special character 'Ñ' support in the alphabetical filter, verifying UTF-8 encoding in the URL, network requests, and visual response.
    Measurable: Verify that the network request returns a successful HTTP status code (200 OK) without encoding glitches (e.g., malformed %C3%91 or '?') and that the UI renders matching records or a functional 'no results' message.
    Achievable: The functionality uses the exposed filter API and can be verified using UI/network automation scripts.
    Relevant: Mitigates the critical risk of failure in Spanish special characters (Priority P0), ensuring UTF-8 application robustness.
    Time-bound: Fully executed and validated within the current iteration (Iteration 27).
User Story Acceptance Criteria
    Open the main people directory page.
    Select the letter 'Ñ' from the alphabetical filter.
    The URL must handle UTF-8 character encoding correctly (e.g., percent-encoding %C3%91 or well-formed Ñ) without breaking or corrupting.
    The network response must return HTTP status code 200 OK without server errors (500) or missing resource errors (404).
    If people whose first name or surname starts with 'Ñ' exist (e.g., Núñez, Ñiguez), they must render correctly in the list.
    If no matching records exist, the application must display a clear, functional 'no results found' message instead of a blank screen or unhandled error.
    Surrounding UI elements and text must not show character corruption or weird glyphs (e.g., , ?, etc.).
Derived Tasks and Acceptance Criteria
    Review UTF-8 character encoding requirements for Spanish 'Ñ' filter
        Acceptance Criteria: Character encoding requirements and API specifications reviewed.
    Identify DOM locators for 'Ñ' filter button and target URL parameters
        Acceptance Criteria: DOM locators and network inspectors tested in browser console.
    Design test cases for 'Ñ' special character filtering and encoding validation
        Acceptance Criteria: Test cases documented with positive, negative, and corrupt character scenarios.
    Implement automated navigation script to select 'Ñ' from alphabetical filter
        Acceptance Criteria: Script triggers click event and interacts with the 'Ñ' filter button.
    Implement URL string parsing assertion for UTF-8 percent-encoding
        Acceptance Criteria: Script confirms URL contains the 'Ñ' parameter properly encoded in UTF-8 (%C3%91).
    Implement network response code interception for 'Ñ' filter query
        Acceptance Criteria: Verified that request returns no 400, 404, or 500 errors in network console.
    Implement DOM assertions for 'Ñ' matching records rendering
        Acceptance Criteria: Test validates returned names start with 'Ñ' or 'ñ'.
    Implement fallback assertion for empty result state message handling
        Acceptance Criteria: Test confirms explicit no-match message is visibly rendered in UI.
    Implement character corruption check on rendered DOM text nodes
        Acceptance Criteria: Assertion checking for absence of strange or broken glyphs (e.g., , ?).
    Execute 'Ñ' character filtering test suite in staging environment
        Acceptance Criteria: Suite successfully executed inside the continuous integration pipeline.
    Analyze execution logs, screenshots, and report any encoding defects to the team
        Acceptance Criteria: Logs analyzed and UTF-8/UI defects logged if applicable.
    Consolidate execution evidence and close work items in Azure DevOps
        Acceptance Criteria: Evidence attached to User Story in Azure DevOps and work items closed.