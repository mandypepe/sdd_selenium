TC-015: Implement Pagination for Search Results
User Story (SMART):As a platform user, I want to navigate through search results using a numerical pagination bar (1, 2, 3...) with 'Next' and 'Last' buttons, so that I can view large amounts of data in an organized and efficient manner within the current iteration.
User Story Acceptance Criteria:
    (TC-015) The initial page must load page 1 of the results by default.
    Pagination must display sequential numbers (e.g., 1 2 3 4 5 6 7 8 9 ...).
    'Next ›' and 'Last »' navigation buttons must exist and be fully functional.
    When on page 1, the 'Previous' button (if applicable) must be disabled.
    Visible records on the screen must strictly correspond to the currently selected page.
Derived Tasks:
    Analyze and design UI for pagination component
        Description: Analyze and design the pagination UI component ensuring it meets UX and design standards.
        Acceptance Criteria: Component designed in Figma or similar, and approved by the technical lead.
    Implement backend pagination logic
        Description: Create or update the backend endpoint to support pagination parameters (limit and offset).
        Acceptance Criteria: The endpoint returns the correct chunk of records along with total metadata for pagination calculations.
    Develop frontend pagination component
        Description: Develop the visual pagination component in the frontend UI.
        Acceptance Criteria: Component renders correctly in the view with numbers, Next, and Last buttons.
    Integrate frontend component with API
        Description: Connect the frontend component with the backend so user clicks dynamically change the displayed data.
        Acceptance Criteria: Clicking a page number triggers an API request and updates the screen data accordingly.
    Implement 'Next' and 'Last' button logic
        Description: Code the logic for 'Next' to advance exactly one page and 'Last' to jump to the final mathematical page.
        Acceptance Criteria: Both buttons navigate to the correct pages and disable automatically when reaching the final page.
    Implement default page 1 state (TC-015)
        Description: Ensure that loading the base URL sets the initial view to page 1 and renders the first batch of results.
        Acceptance Criteria: Page 1 is selected on load, displays the first records, and enables forward navigation.
    Write Unit Tests for backend pagination
        Description: Develop unit tests to validate backend pagination math and logic.
        Acceptance Criteria: Test coverage is above 80% on all created backend methods and services.
    Write Unit Tests for frontend component
        Description: Develop unit tests for the visual component state and user interactions.
        Acceptance Criteria: Tests pass for number clicks, Next/Last events, and default page 1 state validation.
    Perform accessibility (a11y) testing on pagination
        Description: Validate that the component can be navigated purely with a keyboard and is readable by screen readers.
        Acceptance Criteria: Accessibility score passes without any critical errors (WAI-ARIA compliance).
    Conduct code review and fix findings
        Description: Submit the code for review via a Pull Request (PR) and resolve any team feedback.
        Acceptance Criteria: PR is approved by at least one reviewer and successfully merged to the main branch.