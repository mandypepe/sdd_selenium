(TC-016)Validate Paginated Navigation - Navigate to Page 2 

User Story:
As a user browsing the directory/people list, I want to navigate to page 2 and subsequent pages, so that I can view all available records without losing my current search or filter context.

Acceptance Criteria (SMART):

    When the user opens the directory page, pagination controls must be visible.

    When the user clicks on 'Page 2', the list must update to display the second set of records.

    The records on Page 2 must be distinct from Page 1 (advancing the list correctly).

    The directory/people context (e.g., active filters or search terms) must be preserved when transitioning between pages.

    The page load time for the second page must be under 2 seconds.

Tasks & Acceptance Criteria (Titles in English):

    Task: Analyze current pagination implementation in the directory page

        Description: Analyze the current pagination implementation to determine necessary adjustments for page 2 transition.

        Acceptance Criteria: Technical analysis document completed and shared with the team.

    Task: Implement frontend logic for Page 2 navigation click event

        Description: Develop the frontend logic required to handle the click event on the 'Page 2' button.

        Acceptance Criteria: The 'Page 2' button correctly triggers the navigation event without errors.

    Task: Ensure state management retains directory and people context during pagination

        Description: Implement state management updates to ensure that filters and search terms are not cleared when changing pages.

        Acceptance Criteria: Active filters and directory search terms are retained after clicking Page 2.

    Task: Connect pagination UI with the backend API to fetch subsequent records

        Description: Connect the frontend pagination UI with the backend API to correctly request the next set of data.

        Acceptance Criteria: The UI successfully requests the correct data block from the backend.

    Task: Validate offset and limit parameters in backend query for Page 2

        Description: Validate that the backend query correctly handles offset and limit parameters to fetch the exact page 2 records.

        Acceptance Criteria: The backend correctly interprets pagination parameters and returns distinct records.

    Task: Implement loading skeleton or spinner during page transition

        Description: Add a loading skeleton or spinner to improve UX while the next page of records is fetched.

        Acceptance Criteria: A spinner or skeleton is displayed correctly while fetching data.

    Task: Write unit tests for frontend pagination context preservation

        Description: Create unit tests to verify that the frontend state components properly preserve context.

        Acceptance Criteria: Test coverage for state components is above 80%.

    Task: Write integration tests for API pagination response (TC-016)

        Description: Create integration tests ensuring the API returns correctly paginated data according to TC-016.

        Acceptance Criteria: Integration tests pass successfully for the API pagination endpoints.

    Task: Perform QA manual testing for Page 2 navigation and record validation

        Description: Execute manual QA tests checking the page 2 navigation, context preservation, and correct data display.

        Acceptance Criteria: Manual tests pass without critical or high bugs.

    Task: Review and refactor code for performance optimization

        Description: Refactor frontend code and optimize API calls to ensure page loads in under 2 seconds.

        Acceptance Criteria: Page load performance is under 2 seconds during transition.