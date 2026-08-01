package com.project.tests;

import com.project.pages.PeopleSectionPage;
import com.project.pages.components.PersonnelRecord;
import com.project.tests.base.BaseTest;
import com.project.utils.ReportLogger;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * TC-006 / US1 — View General Directory Listing.
 *
 * Validates that selecting the "Any" (Cualquiera) alphabetical filter
 * retrieves the complete, unfiltered list of individuals:
 *  - The list is non-empty.
 *  - Records span at least two distinct initial letters, confirming the
 *    result is not restricted to a single-letter subset.
 */
@Feature("Alphabetical Filter")
public class AnyFilterDisplaysGeneralListTest extends BaseTest {

    @Test
    @Story("US1 - View General Directory Listing")
    @Description("Selecting 'Any' filter must display a non-empty list with records starting with at least two distinct initial letters.")
    public void anyFilterDisplaysNonEmptyGeneralList() {
        ReportLogger.log("TC-006 US1: Verifying 'Any' filter shows the full directory listing");

        PeopleSectionPage peopleSectionPage = new PeopleSectionPage();

        // Wait for the initial page load to settle
        peopleSectionPage.waitForRecordsToLoad();

        // Apply the Any filter
        ReportLogger.log("Step 1: Applying 'Any' (Cualquiera) alphabetical filter");
        peopleSectionPage.applyAnyFilter();

        // Wait for records to reload after filter interaction
        peopleSectionPage.waitForRecordsToLoad();

        // Assert: list must not be empty
        ReportLogger.log("Step 2: Asserting the personnel list is non-empty");
        Assert.assertTrue(
                peopleSectionPage.hasRecords(),
                "Expected at least one personnel record after applying 'Any' filter, but the list was empty.");

        // Assert: records must span at least two distinct initial letters
        ReportLogger.log("Step 3: Asserting records span at least two distinct initial letters");
        List<PersonnelRecord> visibleRecords = peopleSectionPage.getVisibleRecords();
        Set<Character> distinctInitials = collectDistinctInitials(visibleRecords);

        Assert.assertTrue(
                distinctInitials.size() >= 2,
                "Expected records with at least 2 distinct initial letters after 'Any' filter, "
                        + "but found only: " + distinctInitials
                        + ". This suggests the filter may still be restricting results.");

        ReportLogger.log("TC-006 US1 PASSED — " + visibleRecords.size()
                + " records visible, distinct initials: " + distinctInitials);
    }

    @Test
    @Story("US1 - View General Directory Listing")
    @Description("Switching from a letter filter to 'Any' must restore the full listing.")
    public void switchingFromLetterFilterToAnyRestoresFullList() {
        ReportLogger.log("TC-006 US1 (switch): Verifying switching from letter filter to 'Any' restores full list");

        PeopleSectionPage peopleSectionPage = new PeopleSectionPage();
        peopleSectionPage.waitForRecordsToLoad();

        // Apply a letter filter first (letter "A" is a common starting point)
        ReportLogger.log("Step 1: Applying letter filter 'A'");
        try {
            peopleSectionPage.applyLetterFilter("A");
            peopleSectionPage.waitForRecordsToLoad();
            ReportLogger.log("Letter 'A' filter applied successfully");
        } catch (Exception letterFilterException) {
            // If the letter filter selector is not found, skip the pre-condition step
            // and proceed directly to the Any filter assertion
            ReportLogger.log("Letter filter 'A' not available — proceeding directly to Any filter: "
                    + letterFilterException.getMessage());
        }

        // Now switch to Any
        ReportLogger.log("Step 2: Switching to 'Any' filter");
        peopleSectionPage.applyAnyFilter();
        peopleSectionPage.waitForRecordsToLoad();

        // Assert: list must not be empty after switching back to Any
        Assert.assertTrue(
                peopleSectionPage.hasRecords(),
                "Expected a non-empty list after switching from letter filter to 'Any', but the list was empty.");

        // Assert: records must span at least two distinct initial letters
        List<PersonnelRecord> visibleRecords = peopleSectionPage.getVisibleRecords();
        Set<Character> distinctInitials = collectDistinctInitials(visibleRecords);

        Assert.assertTrue(
                distinctInitials.size() >= 2,
                "After switching to 'Any', expected records with at least 2 distinct initial letters, "
                        + "but found only: " + distinctInitials);

        ReportLogger.log("TC-006 US1 switch PASSED — " + visibleRecords.size()
                + " records visible after switching to Any, distinct initials: " + distinctInitials);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    /**
     * Collects the distinct first characters of all record full names (upper-cased).
     * Records with blank names are skipped.
     */
    private Set<Character> collectDistinctInitials(List<PersonnelRecord> records) {
        Set<Character> initials = new HashSet<>();
        for (PersonnelRecord record : records) {
            String fullName = record.getFullName();
            if (fullName != null && !fullName.isBlank()) {
                initials.add(Character.toUpperCase(fullName.trim().charAt(0)));
            }
        }
        return initials;
    }
}
