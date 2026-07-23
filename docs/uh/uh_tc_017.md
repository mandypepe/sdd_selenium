TC-017 - Navigate using the Next button

User Story (Description):

    Objective: Check forward progress control.
    As a user, I want to use the 'Next' button to navigate through the pages sequentially so that I can progress through the flow without skipping any content.
    Original Priority: P0

Acceptance Criteria:

    Must navigate to the next page when clicking 'Next'.

    Must not skip any pages during navigation.

    The URL and/or visual state must reflect the change accurately.

Derived Tasks and Acceptance Criteria:

    Task: Design UI/UX for the Next button state and transitions

        AC: Figma designs provided for all button states; Transition animations defined.

    Task: Implement frontend logic for sequential page routing

        AC: Router is configured with sequential paths; Navigation functions accept next/previous commands.

    Task: Bind Next button click event to navigation action

        AC: Clicking the button triggers the routing function; No console errors on click.

    Task: Update URL state dynamically upon navigation

        AC: URL changes instantly when navigating; Deep linking to the new URL loads the correct step.

    Task: Implement visual state changes for active page

        AC: Active step indicator matches current page; Previous step is marked as completed.

    Task: Implement logic to handle the final page edge case

        AC: Button becomes disabled on the last step; Users cannot force navigation past the last step.

    Task: Write unit tests for the navigation routing logic

        AC: Test coverage > 80% for routing service; Tests assert that index increments by exactly 1.

    Task: Write UI automation tests for the Next button behavior

        AC: Automated test clicks Next from Page 1 to Page 2; Test asserts URL and visual state changes.

    Task: Perform manual QA testing across supported browsers

        AC: Passes manual QA on all target browsers; No visual regressions found.