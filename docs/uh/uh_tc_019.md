TC-019 - Navigate back from an intermediate page

User Story (SMART):

    As a system user interacting with paginated lists,
    I want to be able to navigate back to the previous page from an intermediate page (e.g., page 3 or higher),
    So that I can review previously seen information without losing pagination consistency or encountering duplicated data on the screen.

Acceptance Criteria:

    The system must allow the user to navigate back using the interface back button/control from page 3 (or higher) to the previous page.

    The information displayed on the previous page upon backward navigation must remain exactly the same, with no duplicate or missing records.

    Pagination controls must remain logical and consistent (e.g., correctly highlighting the current active page number).

    Load times upon backward navigation must not visually degrade compared to the original navigation time.

Derived Tasks:

    Task: TC-019 - Design backwards navigation flow

        Description: Design the UX/UI flow for backwards navigation ensuring high usability.

        Acceptance Criteria: Mockups or navigation flow diagrams documented and approved by the UX team.

    Task: TC-019 - Implement UI button for backwards navigation

        Description: Implement the back button/control in the frontend for result pages.

        Acceptance Criteria: Button is visible, accessible, and fully functional on paginated result interfaces.

    Task: TC-019 - Update routing logic for pagination state

        Description: Update routing logic to properly maintain and restore the page state.

        Acceptance Criteria: The URL correctly reflects the current page upon executing the backward action.

    Task: TC-019 - Ensure data fetching uses exact previous parameters

        Description: Ensure the frontend requests or loads data using the exact previous state parameters.

        Acceptance Criteria: Rendered information suffers no duplication or omissions when going back.

    Task: TC-019 - Fix backend endpoint pagination logic if required

        Description: Review and adjust backend offset/limit logic if there are flaws causing record duplication.

        Acceptance Criteria: The endpoint returns the exact and deterministic dataset based on limit and offset.

    Task: TC-019 - Implement unit tests for routing component

        Description: Create unit tests for the router logic handling pagination states.

        Acceptance Criteria: Unit test coverage for the modified component is above 80%.

    Task: TC-019 - Implement integration tests for backward navigation

        Description: Develop integration tests validating the navigation flow from page 3 -> page 2.

        Acceptance Criteria: Integration tests execute successfully in the CI/CD pipeline.

    Task: TC-019 - Verify performance of data load on back navigation

        Description: Validate that rendering and response times do not degrade when navigating back.

        Acceptance Criteria: Load times are maintained within acceptable thresholds (e.g., < 2 seconds).

    Task: TC-019 - Perform QA manual validation on intermediate pages

        Description: Perform manual and exploratory testing to ensure pagination consistency from intermediate pages.

        Acceptance Criteria: QA Sign-off obtained confirming no severe bugs related to duplication or state exist.

    Task: TC-019 - Code review and merge PRs

        Description: Peer code review and merging to the main repository branch.

        Acceptance Criteria: Pull Requests are approved by at least one peer and merged without conflicts.