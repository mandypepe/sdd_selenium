package com.project.data;

import com.project.utils.TestDataLoader;

/**
 * Data provider class that loads test data from external configuration files.
 * Eliminates hardcoded values and provides centralized test data management.
 */
public class DirectoryTestData {

    // URLs
    public static final String DIRECTORY_URL = TestDataLoader.getString("directory.base.url");
    public static final String UNIVERSITIES_URL = TestDataLoader.getString("universities.base.url");
    public static final String PHONES_URL = TestDataLoader.getString("phones.base.url");

    // Page Titles and Headers
    public static final String EXPECTED_PAGE_TITLE = TestDataLoader.getString("directory.page.title");
    public static final String EXPECTED_SECTION_HEADER = TestDataLoader.getString("directory.section.header");
    public static final String UNIVERSITIES_PAGE_TITLE = TestDataLoader.getString("universities.page.title");
    public static final String UNIVERSITIES_SECTION_HEADER = TestDataLoader.getString("universities.section.header");
    public static final String PHONES_PAGE_TITLE = TestDataLoader.getString("phones.page.title");
    public static final String PHONES_SECTION_HEADER = TestDataLoader.getString("phones.section.header");

    // Expected Person Names
    public static final String EXPECTED_PERSON_NAME_1 = TestDataLoader.getString("expected.person.name.1");
    public static final String EXPECTED_PERSON_NAME_2 = TestDataLoader.getString("expected.person.name.2");
    public static final String EXPECTED_PERSON_NAME_3 = TestDataLoader.getString("expected.person.name.3");
    public static final String EXPECTED_PERSON_NAME_4 = TestDataLoader.getString("expected.person.name.4");
    public static final String EXPECTED_PERSON_NAME_5 = TestDataLoader.getString("expected.person.name.5");

    // Empty State Messages
    public static final String EMPTY_STATE_NO_RESULTS = TestDataLoader.getString("empty.state.message.no.results");
    public static final String EMPTY_STATE_NO_DATA = TestDataLoader.getString("empty.state.message.no.data");
    public static final String EMPTY_STATE_EMPTY = TestDataLoader.getString("empty.state.message.empty");

    // Alphabet Filter Data
    public static final String[] ALPHABET_LETTERS = TestDataLoader.getStringArray("alphabet.letters");
    public static final String[] SPECIAL_CHARACTERS = TestDataLoader.getStringArray("special.characters");

    // Performance Thresholds
    public static final long PERFORMANCE_THRESHOLD_PAGE_LOAD = TestDataLoader.getLong("performance.threshold.page.load");
    public static final long PERFORMANCE_THRESHOLD_FILTER_RESPONSE = TestDataLoader.getLong("performance.threshold.filter.response");
    public static final long PERFORMANCE_THRESHOLD_PAGINATION_LOAD = TestDataLoader.getLong("performance.threshold.pagination.load");

    // Pagination Data
    public static final int PAGINATION_DEFAULT_PAGE_SIZE = TestDataLoader.getInt("pagination.default.page.size");
    public static final int PAGINATION_MAX_PAGES = TestDataLoader.getInt("pagination.max.pages");

    // Search Test Data
    public static final String SEARCH_VALID_TERM = TestDataLoader.getString("search.valid.term");
    public static final String SEARCH_INVALID_TERM = TestDataLoader.getString("search.invalid.term");
    public static final String SEARCH_EMPTY_TERM = TestDataLoader.getString("search.empty.term");

    // Accessibility Data
    public static final double ACCESSIBILITY_CONTRAST_RATIO_MINIMUM = 4.5; // Default value
    public static final boolean ACCESSIBILITY_ALT_TEXT_REQUIRED = TestDataLoader.getBoolean("accessibility.alt.text.required");

    // Browser Configuration
    public static final String BROWSER_DEFAULT = TestDataLoader.getString("browser.default");
    public static final boolean BROWSER_HEADLESS = TestDataLoader.getBoolean("browser.headless");
    public static final int BROWSER_TIMEOUT_SECONDS = TestDataLoader.getInt("browser.timeout.seconds");

    // Test Environment Configuration
    public static final String TEST_ENVIRONMENT = TestDataLoader.getString("test.environment");
    public static final int TEST_PARALLEL_THREADS = TestDataLoader.getInt("test.parallel.threads");
    public static final int TEST_RETRY_COUNT = TestDataLoader.getInt("test.retry.count");

    /**
     * Utility method to get a person name by index
     */
    public static String getExpectedPersonName(int index) {
        switch (index) {
            case 1: return EXPECTED_PERSON_NAME_1;
            case 2: return EXPECTED_PERSON_NAME_2;
            case 3: return EXPECTED_PERSON_NAME_3;
            case 4: return EXPECTED_PERSON_NAME_4;
            case 5: return EXPECTED_PERSON_NAME_5;
            default: return "";
        }
    }

    /**
     * Utility method to get empty state message by type
     */
    public static String getEmptyStateMessage(String type) {
        switch (type.toLowerCase()) {
            case "no.results": return EMPTY_STATE_NO_RESULTS;
            case "no.data": return EMPTY_STATE_NO_DATA;
            case "empty": return EMPTY_STATE_EMPTY;
            default: return EMPTY_STATE_NO_RESULTS;
        }
    }

    /**
     * Utility method to check if performance threshold is exceeded
     */
    public static boolean isPerformanceThresholdExceeded(String metric, long actualTime) {
        long threshold = 0;
        switch (metric.toLowerCase()) {
            case "page.load": threshold = PERFORMANCE_THRESHOLD_PAGE_LOAD; break;
            case "filter.response": threshold = PERFORMANCE_THRESHOLD_FILTER_RESPONSE; break;
            case "pagination.load": threshold = PERFORMANCE_THRESHOLD_PAGINATION_LOAD; break;
        }
        return actualTime > threshold;
    }

    /**
     * Utility method to get performance threshold for a metric
     */
    public static long getPerformanceThreshold(String metric) {
        switch (metric.toLowerCase()) {
            case "page.load": return PERFORMANCE_THRESHOLD_PAGE_LOAD;
            case "filter.response": return PERFORMANCE_THRESHOLD_FILTER_RESPONSE;
            case "pagination.load": return PERFORMANCE_THRESHOLD_PAGINATION_LOAD;
            default: return 3000; // default threshold
        }
    }
}