TC-013 - Validate rendering and association of job titles and academic degrees

User Story (SMART Format):
As a system user,
I want to see job titles and academic degrees correctly associated with each person in the list view,
so that I avoid visual confusion and ensure the displayed information is consistent and accurate.

Acceptance Criteria:

    The job title must be visually associated with the correct person in the list.

    The academic degree of one person must not appear to belong to an adjacent person.

    Titles and job roles must be displayed with consistent formatting and alignment.

Derived Tasks and Acceptance Criteria:

    Task: Review frontend component structure for list view

        Acceptance Criteria: DOM structure analyzed and problem areas documented on the ticket.

    Task: Identify CSS or HTML layout issues causing visual overlapping of titles

        Acceptance Criteria: Root CSS/HTML issue identified and documented.

    Task: Fix frontend layout to ensure correct visual association of job titles

        Acceptance Criteria: Job titles are unambiguously associated with the correct user with no room for visual confusion.

    Task: Fix frontend layout to ensure correct visual association of academic degrees

        Acceptance Criteria: Academic degrees do not overflow or mix with neighboring list records.

    Task: Standardize the display format for titles across the component

        Acceptance Criteria: Uniform styling is applied across the entire table/list for roles and degrees.

    Task: Verify API response payload correctly maps titles to user IDs

        Acceptance Criteria: Confirmed that the API has no mapping errors (the issue is purely visual, or fixed if data mismatch exists).

    Task: Write unit tests for the frontend list component rendering

        Acceptance Criteria: Frontend tests implemented and successfully passing in the pipeline.

    Task: Perform cross-browser manual testing to ensure layout consistency

        Acceptance Criteria: Screenshots attached evidencing proper layout in Chrome, Firefox, and Edge.

    Task: Deploy layout fixes to the staging environment

        Acceptance Criteria: The staging environment reflects the visual changes without impacting list performance.

    Task: Conduct code review and merge pull requests

        Acceptance Criteria: Code approved by at least one reviewer and merged without conflicts.