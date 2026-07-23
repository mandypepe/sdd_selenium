TC-035 - Validate and Improve Visual Contrast for Readability

User Story:

    As a user,
    I want the application text and UI elements to have sufficient visual contrast
    so that I can easily read names, roles, pagination, and filters without eye strain.

Acceptance Criteria:

    All text elements (including names, roles, pagination, and filters) must meet WCAG AA contrast ratio standards (minimum 4.5:1 for normal text).

    Active and inactive states for UI elements must be visually distinct and clearly distinguishable.

    There must be no overly light gray text on light backgrounds; it must be darkened to meet contrast requirements.

Derived Tasks & Acceptance Criteria:

    Task: Audit visual contrast of user names and roles text

        Description: Review current UI text for names and roles against WCAG contrast guidelines.

        Acceptance Criteria: Audit report created listing all text elements failing the 4.5:1 contrast ratio.

    Task: Audit visual contrast of pagination and filter controls

        Description: Check active, inactive, and hover states of pagination and filters for contrast issues.

        Acceptance Criteria: Issues identified and logged for pagination and filter states.

    Task: Update CSS color variables for light gray text

        Description: Increase the darkness of gray text in the CSS stylesheet to meet the 4.5:1 ratio.

        Acceptance Criteria: All formerly light gray text now passes contrast checker tools.

    Task: Redesign active and inactive states for filters

        Description: Ensure filter states are distinguishable without relying solely on subtle color changes (e.g., add borders or font-weight changes).

        Acceptance Criteria: Active and inactive filter states are visually distinct to users with color vision deficiencies.

    Task: Redesign active and inactive states for pagination

        Description: Update pagination UI for clarity so the current page stands out significantly from inactive pages.

        Acceptance Criteria: The current page indicator passes contrast checks and is immediately identifiable.

    Task: Test visual contrast using automated accessibility tools

        Description: Use Axe, Lighthouse, or similar tools to run a full contrast scan on the application views.

        Acceptance Criteria: 100% of automated contrast tests pass with zero critical or serious contrast violations.

    Task: Perform manual visual QA on different monitors

        Description: QA team to check the UI on high and low brightness screens to ensure text remains legible.

        Acceptance Criteria: Readability is confirmed across varied hardware configurations and lighting conditions.

    Task: Document contrast guidelines in UI component library

        Description: Update the design system specifications to reflect the new approved high-contrast colors.

        Acceptance Criteria: Contrast rules and safe color palettes are documented for future reference and approved by the PO.