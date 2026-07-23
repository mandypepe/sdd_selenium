TC-022 - Search by Name with Accents and Special Characters

User Story:
As a system user, I want to be able to search for names containing accents or special characters (e.g., Álvarez, Pérez, Velázquez, Gonzalez), so that I can find the correct records regardless of whether I enter the search term with or without accent marks.

Acceptance Criteria:

    The search must return correct results when entering a name with the exact accent (e.g., "Álvarez" returns "Álvarez").

    The search must return correct results when entering the name without an accent when the record has one (e.g., "Alvarez" returns "Álvarez").

    The search must be case-insensitive.

    The search response time must be under 2 seconds.

Derived Tasks & Acceptance Criteria:

    Backend: Update search query to support accent-insensitive matching

        Criteria: The query uses unaccent functions or proper collation to normalize the search string.

    Backend: Implement database collation or normalizer for special characters

        Criteria: The configured database collation successfully allows accent-insensitive (AI) comparisons without heavy performance degradation.

    Backend: Add unit tests for exact accent search cases

        Criteria: Code coverage is above 80%. Tests for exact accent searches pass successfully.

    Backend: Add unit tests for non-accent search matching accented records

        Criteria: Tests assert that a search without accents (e.g., "Alvarez") correctly returns the object containing the accent.

    API: Update search endpoint documentation with accent behavior

        Criteria: The search endpoint details examples with and without accents in the Swagger portal.

    Frontend: Ensure search input field accepts special characters

        Criteria: It is possible to type and submit special characters from the UI without front-end validation errors.

    Frontend: Implement correct URL encoding for search queries

        Criteria: Query string parameters (e.g., ?name=Álvarez) are properly URL-encoded and received intact by the backend.

    QA: Create test cases for exact accent match scenarios

        Criteria: Test cases are documented and linked in the project's test management tool.

    QA: Create test cases for accent-insensitive match scenarios

        Criteria: Test cases cover combinations like Gonzalez, Velazquez, and Perez to ensure robustness.

    QA: Execute manual testing and verify search performance

        Criteria: All tests pass successfully and the response time remains under the 2-second threshold.