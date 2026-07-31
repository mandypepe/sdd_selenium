# Data Model: Directory Alphabetical Index Filter Validation

**Date**: 2026-07-30  
**Feature**: TC-008 Directory Alphabetical Index Filter Validation  
**Purpose**: Define data structures and test data for comprehensive alphabet validation

## Core Data Entities

### 1. AlphabetLetter
**Description**: Represents a single character in the Spanish alphabet filter

```java
public class AlphabetLetter {
    private final char character;
    private final String displayName;
    private final String dataLetterValue;
    private final boolean isSpecialCharacter;
    
    // Constructor and getters
    public AlphabetLetter(char character, String displayName, String dataLetterValue, boolean isSpecialCharacter) {
        this.character = character;
        this.displayName = displayName;
        this.dataLetterValue = dataLetterValue;
        this.isSpecialCharacter = isSpecialCharacter;
    }
    
    public char getCharacter() { return character; }
    public String getDisplayName() { return displayName; }
    public String getDataLetterValue() { return dataLetterValue; }
    public boolean isSpecialCharacter() { return isSpecialCharacter; }
    
    @Override
    public String toString() { return displayName; }
}
```

**Complete Spanish Alphabet Dataset**:
```java
public static final List<AlphabetLetter> SPANISH_ALPHABET = Arrays.asList(
    new AlphabetLetter('A', "A", "a", false),
    new AlphabetLetter('B', "B", "b", false),
    new AlphabetLetter('C', "C", "c", false),
    new AlphabetLetter('D', "D", "d", false),
    new AlphabetLetter('E', "E", "e", false),
    new AlphabetLetter('F', "F", "f", false),
    new AlphabetLetter('G', "G", "g", false),
    new AlphabetLetter('H', "H", "h", false),
    new AlphabetLetter('I', "I", "i", false),
    new AlphabetLetter('J', "J", "j", false),
    new AlphabetLetter('K', "K", "k", false),
    new AlphabetLetter('L', "L", "l", false),
    new AlphabetLetter('M', "M", "m", false),
    new AlphabetLetter('N', "N", "n", false),
    new AlphabetLetter('Ñ', "Ñ", "ñ", true),  // Special character
    new AlphabetLetter('O', "O", "o", false),
    new AlphabetLetter('P', "P", "p", false),
    new AlphabetLetter('Q', "Q", "q", false),
    new AlphabetLetter('R', "R", "r", false),
    new AlphabetLetter('S', "S", "s", false),
    new AlphabetLetter('T', "T", "t", false),
    new AlphabetLetter('U', "U", "u", false),
    new AlphabetLetter('V', "V", "v", false),
    // Note: W is not in traditional Spanish alphabet, skipping
    new AlphabetLetter('X', "X", "x", false),
    new AlphabetLetter('Y', "Y", "y", false),
    new AlphabetLetter('Z', "Z", "z", false)
);
// Total: 27 characters (26 letters + Ñ)
```

### 2. FilterResult
**Description**: Represents the result of applying an alphabet filter

```java
public class FilterResult {
    private final AlphabetLetter letter;
    private final int recordCount;
    private final List<String> personNames;
    private final String emptyStateMessage;
    private final boolean hasError;
    private final long responseTimeMs;
    
    // Constructor and getters
    public FilterResult(AlphabetLetter letter, int recordCount, List<String> personNames, 
                       String emptyStateMessage, boolean hasError, long responseTimeMs) {
        this.letter = letter;
        this.recordCount = recordCount;
        this.personNames = new ArrayList<>(personNames);
        this.emptyStateMessage = emptyStateMessage;
        this.hasError = hasError;
        this.responseTimeMs = responseTimeMs;
    }
    
    // Getters...
    public boolean isEmpty() { return recordCount == 0; }
    public boolean hasResults() { return recordCount > 0; }
}
```

### 3. AlphabetTestScenario
**Description**: Defines test scenarios for alphabet validation

```java
public class AlphabetTestScenario {
    private final String scenarioName;
    private final List<AlphabetLetter> lettersToTest;
    private final boolean expectResults;
    private final String description;
    
    public enum ScenarioType {
        FULL_ALPHABET,      // Test all 27 characters
        LETTERS_WITH_RESULTS, // Test only letters expected to have results
        LETTERS_WITHOUT_RESULTS, // Test only letters expected to be empty
        SPECIAL_CHARACTERS,  // Test only Ñ and other special characters
        PERFORMANCE_TEST     // Test execution time targets
    }
    
    // Constructor and getters...
}
```

## Test Data Providers

### 1. AlphabetDataProvider
**Purpose**: Provides test data for different alphabet validation scenarios

```java
public class AlphabetDataProvider {
    
    @DataProvider(name = "spanishAlphabet")
    public Object[][] spanishAlphabetProvider() {
        Object[][] data = new Object[SPANISH_ALPHABET.size()][1];
        for (int i = 0; i < SPANISH_ALPHABET.size(); i++) {
            data[i][0] = SPANISH_ALPHABET.get(i);
        }
        return data;
    }
    
    @DataProvider(name = "alphabetWithExpectedResults")
    public Object[][] alphabetWithExpectedResultsProvider() {
        // Letters typically expected to have personnel results
        List<AlphabetLetter> lettersWithResults = Arrays.asList(
            SPANISH_ALPHABET.get(0),  // A
            SPANISH_ALPHABET.get(4),  // E
            SPANISH_ALPHABET.get(8),  // I
            SPANISH_ALPHABET.get(11), // L
            SPANISH_ALPHABET.get(12), // M
            SPANISH_ALPHABET.get(17), // R
            SPANISH_ALPHABET.get(18)  // S
        );
        
        return createDataProviderArray(lettersWithResults);
    }
    
    @DataProvider(name = "alphabetWithoutExpectedResults")
    public Object[][] alphabetWithoutExpectedResultsProvider() {
        // Letters that might not have results (adjust based on actual data)
        List<AlphabetLetter> lettersWithoutResults = Arrays.asList(
            SPANISH_ALPHABET.get(10), // K
            SPANISH_ALPHABET.get(21), // W (if present)
            SPANISH_ALPHABET.get(25)  // Z
        );
        
        return createDataProviderArray(lettersWithoutResults);
    }
    
    @DataProvider(name = "specialCharacters")
    public Object[][] specialCharactersProvider() {
        List<AlphabetLetter> specialChars = SPANISH_ALPHABET.stream()
            .filter(AlphabetLetter::isSpecialCharacter)
            .collect(Collectors.toList());
        
        return createDataProviderArray(specialChars);
    }
    
    private Object[][] createDataProviderArray(List<AlphabetLetter> letters) {
        Object[][] data = new Object[letters.size()][1];
        for (int i = 0; i < letters.size(); i++) {
            data[i][0] = letters.get(i);
        }
        return data;
    }
}
```

### 2. TestConfigurationData
**Purpose**: Configuration data for test execution

```java
public class TestConfigurationData {
    // Performance targets
    public static final long MAX_EXECUTION_TIME_MS = 180000; // 3 minutes
    public static final long MAX_SINGLE_LETTER_TIME_MS = 10000; // 10 seconds per letter
    
    // Test environment
    public static final String BASE_URL = "https://www.uci.cu/index.php/directorio/personas";
    public static final String DEFAULT_BROWSER = "chrome";
    
    // Expected results (to be updated after UI inspection)
    public static final String EMPTY_STATE_MESSAGE = "No se encontraron resultados"; // Placeholder
    public static final By EMPTY_STATE_LOCATOR = By.cssSelector(".empty-state-message"); // Placeholder
    
    // Test data
    public static final int MIN_EXPECTED_RESULTS = 1;
    public static final int MAX_EXPECTED_RESULTS = 1000; // Pagination limit
    
    // Special character handling
    public static final String ENCODING_UTF_8 = "UTF-8";
    public static final String NTILDE_ENCODED = "%C3%91"; // URL encoded Ñ
    public static final String NTILDE_UNICODE = "Ñ";
}
```

## Expected Test Results

### 1. Successful Filter Application
```java
public class ExpectedFilterResult {
    public static FilterResult createSuccessfulResult(AlphabetLetter letter, List<String> names) {
        return new FilterResult(
            letter,
            names.size(),
            names,
            null, // No empty state message
            false, // No error
            System.currentTimeMillis() // Will be set during test
        );
    }
}
```

### 2. Empty State Result
```java
public class ExpectedEmptyResult {
    public static FilterResult createEmptyResult(AlphabetLetter letter) {
        return new FilterResult(
            letter,
            0,
            Collections.emptyList(),
            TestConfigurationData.EMPTY_STATE_MESSAGE,
            false, // No error
            System.currentTimeMillis()
        );
    }
}
```

### 3. Error State Result
```java
public class ExpectedErrorResult {
    public static FilterResult createErrorResult(AlphabetLetter letter, String errorMessage) {
        return new FilterResult(
            letter,
            -1, // Error indicator
            Collections.emptyList(),
            errorMessage,
            true, // Error occurred
            System.currentTimeMillis()
        );
    }
}
```

## Data Validation Rules

### 1. Alphabet Completeness
- **Rule**: All 27 Spanish alphabet characters must be present
- **Validation**: `SPANISH_ALPHABET.size() == 27`
- **Special Check**: Ñ character must be properly encoded

### 2. Result Consistency
- **Rule**: Record count must match actual list size
- **Validation**: `result.getRecordCount() == result.getPersonNames().size()`
- **Edge Case**: Empty results should have count = 0 and empty list

### 3. Performance Validation
- **Rule**: Total execution time < 3 minutes
- **Validation**: `totalTime < TestConfigurationData.MAX_EXECUTION_TIME_MS`
- **Per-Letter**: Each letter < 10 seconds

### 4. Encoding Validation
- **Rule**: Ñ character must maintain integrity
- **Validation**: URL encoding and Unicode display consistency
- **Check**: No corrupted characters or encoding errors

## Data Persistence Strategy

### 1. Test Results Storage
```java
public class TestResultsStorage {
    private final List<FilterResult> results = new ArrayList<>();
    private final long startTime;
    private long endTime;
    
    public void addResult(FilterResult result) {
        results.add(result);
    }
    
    public TestSummary getSummary() {
        return new TestSummary(
            results.size(),
            countSuccessfulResults(),
            countEmptyResults(),
            countErrorResults(),
            endTime - startTime
        );
    }
}
```

### 2. Performance Metrics
```java
public class PerformanceMetrics {
    private final Map<AlphabetLetter, Long> letterExecutionTimes = new HashMap<>();
    private long totalExecutionTime;
    
    public void recordLetterTime(AlphabetLetter letter, long timeMs) {
        letterExecutionTimes.put(letter, timeMs);
    }
    
    public boolean meetsPerformanceTarget() {
        return totalExecutionTime < TestConfigurationData.MAX_EXECUTION_TIME_MS;
    }
}
```

## External Data Dependencies

### 1. Configuration Files
- **Location**: `src/test/resources/config/test-config.properties`
- **Content**: URLs, browser settings, performance targets
- **Purpose**: Externalize hardcoded values

### 2. Test Data Files
- **Location**: `src/test/resources/data/alphabet-data.json`
- **Content**: Expected results, known empty letters
- **Purpose**: Maintain test data separate from code

### 3. Expected Results Database
- **Future Enhancement**: Store historical test results
- **Purpose**: Trend analysis and regression detection
- **Format**: JSON or CSV for easy analysis

This data model provides a comprehensive foundation for implementing the TC-008 alphabet validation while maintaining constitution compliance and supporting long-term maintainability.