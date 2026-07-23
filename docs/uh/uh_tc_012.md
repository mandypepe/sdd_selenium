TC-012 - Validate rendering of names with accents and special characters

User Story (SMART Format):
As a system user,
I want to correctly view names containing accents and special characters (e.g., Velázquez, Álvarez),
so that I avoid rendering errors, corrupted characters (like Ã¡, Ã©, ), and maintain data display integrity across the platform.

Acceptance Criteria:

    When opening the list view, names with accents must be displayed correctly.

    Corrupted characters like Ã¡, Ã©, or  must not appear in any view.

    The following specific examples must be tested and displayed accurately: "Velázquez", "Álvarez", "Pérez", "Dihigo", "Fernández [uci.cu]".

Derived Tasks and Acceptance Criteria:

    Task: Analyze current character encoding implementation in backend

        Acceptance Criteria: Document or note on the ticket identifying the root cause of the encoding issue in the backend.

    Task: Update database collation or character set configurations if necessary

        Acceptance Criteria: The database correctly stores special characters (UTF-8 configuration verified).

    Task: Modify API payload serialization to enforce UTF-8 encoding

        Acceptance Criteria: API endpoints return valid UTF-8 encoded JSON payloads with intact characters.

    Task: Implement frontend decoding and rendering adjustments for accents

        Acceptance Criteria: The frontend renders special characters without alterations, matching the API payload perfectly.

    Task: Create automated unit tests for accented name processing

        Acceptance Criteria: Unit tests are created, integrated into the pipeline, and passing successfully using accented strings.

    Task: Perform manual testing of the list view with sample accented names

        Acceptance Criteria: Evidence of successful manual testing (screenshots) is attached to the ticket.

    Task: Verify data integrity for names with external domains

        Acceptance Criteria: Brackets and appended domains in composite names like 'Fernández [uci.cu]' are rendered without spacing or character failures.

    Task: Deploy fixes to the staging environment and execute regression suite

        Acceptance Criteria: Successful deployment to staging without breaking existing list view features or tables.

    Task: Document encoding standards and guidelines for future development

        Acceptance Criteria: Wiki page created or updated with UTF-8 guidelines for the project.

    Task: Code review and merge pull requests for the rendering fix

        Acceptance Criteria: Pull requests approved by at least one technical reviewer and successfully merged into the main branch.