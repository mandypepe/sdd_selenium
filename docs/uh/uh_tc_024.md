Implement Clickable Person Name for Profile Detail View

User Story (Description):
As a system user, I want to click on a person's name in the directory list so that I can view their detailed profile information without encountering broken links.

Acceptance Criteria (SMART):

    When the person's name has an associated profile, it is displayed as a clickable link.

    Clicking the link redirects the user to the correct individual profile page.

    If the person does not have a detailed profile, the name is displayed as normal plain text (not clickable).

    There are no broken links; navigation always directs to an existing profile or degrades gracefully.

Tasks and Task Acceptance Criteria:

    Title: Analyze the current directory list component

        AC: Analysis documented.

    Title: Design UI state for clickable and non-clickable names

        AC: Design or mockup approved.

    Title: Implement endpoint to check if the profile exists

        AC: API returns correct availability status.

    Title: Modify directory frontend to render link conditionally

        AC: UI matches the design state conditionally.

    Title: Implement routing to the individual profile detail page

        AC: Router handles detail path correctly.

    Title: Handle edge cases for missing data or API failures

        AC: Errors logged and handled securely.

    Title: Write unit tests for the conditional rendering logic

        AC: 80% coverage on new code.

    Title: Write integration tests for profile navigation (TC-024)

        AC: Integration tests pass in pipeline.

    Title: Perform manual QA testing across different browsers

        AC: Checked in Chrome, Firefox, Edge.

    Title: Document component changes and API updates

        AC: Documentation updated and published.