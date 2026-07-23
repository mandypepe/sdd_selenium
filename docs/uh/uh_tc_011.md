TC-011 - Validate structure and data layout of individual person records
User Story (SMART Format)

As a people directory user, I want each individual record to display information in a structured, legible, and complete format (full name, position, and academic title if applicable), so that I can correctly identify institutional contacts without display glitches or cut-off fields.

    Specific: Validate that each rendered person record/card in the directory displays a legible full name and, when applicable, properly aligned position and academic title without text truncation or UI overlaps.

    Measurable: 100% of sampled records must pass DOM checks for full name, position, title, and CSS alignment/overflow properties.

    Achievable: DOM properties and CSS rules are directly verifiable using UI automation and inspection tools.

    Relevant: Ensures readability, usability, and professional presentation of institutional contact data (Priority P0).

    Time-bound: Will be fully developed, automated, and executed within the current iteration (Iteration 27).

User Story Acceptance Criteria

    Open the main people directory page or filtered results view.

    Every listed person record must mandatorily display the full name.

    If the record includes a job position (e.g., "Profesor", "Director") or academic title (e.g., "Máster en Ciencias", "Dr.C."), these must be clearly visible, legible, and properly aligned.

    There must be no truncated, cut-off, or overlapping text fields.

    The visual layout must maintain responsive consistency without horizontal overflow or line overlap.

    DOM HTML tags must properly structure full name, position, and title in separate text nodes.

Derived Tasks and Acceptance Criteria

    Review UI design specifications and record layout requirements

        Acceptance Criteria: UI specifications, data structure, and card design reviewed and understood.

    Identify DOM locators for full name, position, and title elements

        Acceptance Criteria: CSS/XPath selectors for full name, position, and title validated in browser console.

    Design test cases for record structure and visual alignment verification

        Acceptance Criteria: Test cases documented with detailed steps and UI assertions.

    Implement automated script to extract and assert full name visibility

        Acceptance Criteria: Script validates 100% of visible records have a non-empty full name field.

    Implement automated assertions for job position and academic title rendering

        Acceptance Criteria: Code assertions confirm position and title display aligned when present in backend data.

    Implement DOM element bounding box and CSS overflow check for text truncation

        Acceptance Criteria: Test automatically detects if any element has text-overflow or cut-off text.

    Implement responsive layout check across different viewport resolutions

        Acceptance Criteria: Record structure remains aligned across Desktop, Tablet, and Mobile resolutions.

    Execute UI automation test suite across multiple pages of records

        Acceptance Criteria: Suite executed in staging testing a broad sample of person cards across multiple pages.

    Analyze test execution results and report UI alignment or truncation bugs

        Acceptance Criteria: Test reports analyzed; any visual or data rendering bugs logged in Azure DevOps.

    Consolidate test execution evidence and update Azure DevOps story

        Acceptance Criteria: Evidence (screenshots, reports) attached in Azure DevOps and tasks marked as completed.