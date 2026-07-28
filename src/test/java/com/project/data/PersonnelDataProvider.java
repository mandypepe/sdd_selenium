package com.project.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.testng.annotations.DataProvider;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class PersonnelDataProvider {

    private static final Gson GSON = new Gson();

    /**
     * Provides pagination test scenarios from JSON files.
     * Each row: [String scenarioName, String jsonFile, int expectedPageCount, int expectedRecordsOnLastPage, boolean hasPagination]
     */
    @DataProvider(name = "paginationScenarios")
    public static Object[][] paginationScenarios() {
        return new Object[][] {
            { "Single page (8 records)", "test-data/personnel-single-page.json", 1, 8, false },
            { "Multi-page (65 records)", "test-data/personnel-multi-page.json", 4, 5, true },
            { "Boundary (40 records)", "test-data/personnel-boundary.json", 2, 20, true }
        };
    }

    /**
     * Provides special character test scenarios.
     * Each row: [String scenarioName, String expectedName, String expectedRole]
     */
    @DataProvider(name = "specialCharactersScenarios")
    public static Object[][] specialCharactersScenarios() {
        return new Object[][] {
            { "Spanish ñ character", "ñ", true },
            { "Accent á character", "á", true },
            { "Accent é character", "é", true },
            { "Accent í character", "í", true },
            { "Accent ó character", "ó", true },
            { "Accent ú character", "ú", true }
        };
    }

    /**
     * Provides boundary condition test data.
     * Each row: [String scenario, int pageNumber, boolean expectPrevEnabled, boolean expectNextEnabled]
     */
    @DataProvider(name = "boundaryScenarios")
    public static Object[][] boundaryScenarios() {
        return new Object[][] {
            { "First page", 1, false, true },
            { "Middle page", 2, true, true },
            { "Last page navigation", -1, true, false }  // -1 means navigate to last page
        };
    }

    /**
     * Loads and parses a JSON file from the classpath.
     * @param resourcePath path relative to resources, e.g. "test-data/personnel-single-page.json"
     * @return parsed JsonObject
     */
    public static JsonObject loadTestData(String resourcePath) {
        InputStream is = PersonnelDataProvider.class.getClassLoader().getResourceAsStream(resourcePath);
        if (is == null) {
            throw new RuntimeException("Test data file not found: " + resourcePath);
        }
        return GSON.fromJson(new InputStreamReader(is, StandardCharsets.UTF_8), JsonObject.class);
    }

    /**
     * Extracts the expected total records from a test data JSON file.
     */
    public static int getExpectedTotalRecords(String resourcePath) {
        JsonObject data = loadTestData(resourcePath);
        return data.getAsJsonObject("pagination").get("totalRecords").getAsInt();
    }

    /**
     * Extracts the expected total pages from a test data JSON file.
     */
    public static int getExpectedTotalPages(String resourcePath) {
        JsonObject data = loadTestData(resourcePath);
        return data.getAsJsonObject("pagination").get("totalPages").getAsInt();
    }
}
