TC-020 - Combined pagination with alphabetical filter

User Story
As a system user,
I want to use alphabetical filtering combined with pagination
So that I can navigate through large sets of filtered results without losing my selected search criteria (e.g., navigating to page 2 while keeping the letter "A" filter active).

Acceptance Criteria

    Specific: Given the user selects an alphabetical filter (e.g., 'A'), when the results span multiple pages and the user navigates to page 2, then the filter 'A' must remain applied to the displayed results.

    Measurable: The filter must not reset to "Any" without explicitly warning or indicating it to the user.

    Achievable & Relevant: The results displayed on subsequent pages must strictly match the active alphabetical filter and not mix results starting with other letters.

    Time-Bound: Prioritized as P0 / Priority 2, resolution and validation required within the current iteration.

Tasks & Acceptance Criteria

    Task Title: Analyze frontend pagination and filter state management

        AC: Analysis document completed outlining why state drops on page turn.

    Task Title: Update backend API to accept filter and page parameters concurrently

        AC: API endpoint successfully processes and returns data using both alphabetical and page query strings.

    Task Title: Implement state persistence for alphabetical filter during page navigation

        AC: Filter state is accurately stored in the UI component or URL query parameters across page changes.

    Task Title: Develop frontend UI to reflect active filter on all pages

        AC: The UI correctly highlights the active letter (e.g., 'A') as selected, even when on page 2 or higher.

    Task Title: Write unit tests for pagination logic with active filters

        AC: Unit tests added for pagination components with >80% code coverage.

    Task Title: Write unit tests for alphabetical filtering logic with active pagination

        AC: Unit tests added for filter components interacting with page parameters with >80% code coverage.

    Task Title: Perform integration testing for frontend and backend combined parameters

        AC: Integration test suite runs and passes without parameter conflict errors.

    Task Title: Conduct manual QA testing for TC-020 scenario

        AC: QA executes manual steps (Select A, go to page 2, verify) and signs off successfully.

    Task Title: Create automated E2E test for pagination and filter combination

        AC: Cypress/Selenium End-to-End test accurately covers this flow and passes in the pipeline.

    Task Title: Deploy changes to staging environment for final review

        AC: Code is successfully deployed to staging, ready for final stakeholder approval.