package com.project.data;

import org.testng.annotations.DataProvider;
import java.util.Arrays;
import java.util.List;

/**
 * AlphabetDataProvider
 * 
 * Provides test data for Spanish alphabet validation including special characters.
 * Supports TC-008 Directory Alphabetical Index Filter Validation.
 * 
 * Data includes:
 * - Complete Spanish alphabet (A-Z + Ñ)
 * - Special character handling for UTF-8 encoding
 * - Performance test data
 * - Empty state test scenarios
 */
public class AlphabetDataProvider {
    
    /**
     * Complete Spanish alphabet with Ñ character
     * Order: A, B, C, D, E, F, G, H, I, J, K, L, M, N, Ñ, O, P, Q, R, S, T, U, V, W, X, Y, Z
     */
    public static final List<String> SPANISH_ALPHABET = Arrays.asList(
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
        "N", "Ñ", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    );
    
    /**
     * Special characters that require UTF-8 encoding validation
     */
    public static final List<String> SPECIAL_CHARACTERS = Arrays.asList(
        "Ñ", "ñ"
    );
    
    /**
     * Letters expected to have results (based on typical directory data)
     * This can be adjusted based on actual test environment data
     */
    public static final List<String> LETTERS_WITH_RESULTS = Arrays.asList(
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
        "N", "O", "P", "Q", "R", "S", "T", "U", "V", "X", "Y", "Z"
    );
    
    /**
     * Letters expected to have no results (empty state)
     * This can be adjusted based on actual test environment data
     */
    public static final List<String> LETTERS_WITH_NO_RESULTS = Arrays.asList(
        // Initially empty, to be populated based on actual test results
    );
    
    /**
     * Data provider for complete Spanish alphabet testing
     * Used by testAlphabetLetterFilter() method
     * 
     * @return Object[][] with each letter as test data
     */
    @DataProvider(name = "spanishAlphabet")
    public Object[][] spanishAlphabetProvider() {
        Object[][] data = new Object[SPANISH_ALPHABET.size()][1];
        for (int i = 0; i < SPANISH_ALPHABET.size(); i++) {
            data[i][0] = SPANISH_ALPHABET.get(i);
        }
        return data;
    }
    
    /**
     * Data provider for special character testing
     * Used by testSpecialCharacterHandling() method
     * 
     * @return Object[][] with special characters as test data
     */
    @DataProvider(name = "specialCharacters")
    public Object[][] specialCharactersProvider() {
        Object[][] data = new Object[SPECIAL_CHARACTERS.size()][1];
        for (int i = 0; i < SPECIAL_CHARACTERS.size(); i++) {
            data[i][0] = SPECIAL_CHARACTERS.get(i);
        }
        return data;
    }
    
    /**
     * Data provider for letters expected to have results
     * Used for positive test scenarios
     * 
     * @return Object[][] with letters that should have directory results
     */
    @DataProvider(name = "lettersWithResults")
    public Object[][] lettersWithResultsProvider() {
        Object[][] data = new Object[LETTERS_WITH_RESULTS.size()][1];
        for (int i = 0; i < LETTERS_WITH_RESULTS.size(); i++) {
            data[i][0] = LETTERS_WITH_RESULTS.get(i);
        }
        return data;
    }
    
    /**
     * Data provider for letters expected to have no results
     * Used for empty state testing
     * 
     * @return Object[][] with letters that should show empty state
     */
    @DataProvider(name = "lettersWithNoResults")
    public Object[][] lettersWithNoResultsProvider() {
        Object[][] data = new Object[LETTERS_WITH_NO_RESULTS.size()][1];
        for (int i = 0; i < LETTERS_WITH_NO_RESULTS.size(); i++) {
            data[i][0] = LETTERS_WITH_NO_RESULTS.get(i);
        }
        return data;
    }
    
    /**
     * Data provider for performance testing
     * Provides a subset of letters for performance validation
     * 
     * @return Object[][] with performance test data
     */
    @DataProvider(name = "performanceTestData")
    public Object[][] performanceTestProvider() {
        // Use first 5 letters for performance testing to keep test duration reasonable
        List<String> performanceLetters = SPANISH_ALPHABET.subList(0, Math.min(5, SPANISH_ALPHABET.size()));
        Object[][] data = new Object[performanceLetters.size()][1];
        for (int i = 0; i < performanceLetters.size(); i++) {
            data[i][0] = performanceLetters.get(i);
        }
        return data;
    }
    
    /**
     * Get the complete Spanish alphabet as a list
     * 
     * @return List<String> containing all Spanish alphabet characters
     */
    public static List<String> getSpanishAlphabet() {
        return SPANISH_ALPHABET;
    }
    
    /**
     * Get special characters that need UTF-8 encoding validation
     * 
     * @return List<String> containing special characters
     */
    public static List<String> getSpecialCharacters() {
        return SPECIAL_CHARACTERS;
    }
    
    /**
     * Check if a character is a special character requiring UTF-8 handling
     * 
     * @param character The character to check
     * @return true if the character requires special UTF-8 handling
     */
    public static boolean isSpecialCharacter(String character) {
        return SPECIAL_CHARACTERS.contains(character);
    }
    
    /**
     * Get the total count of Spanish alphabet characters
     * 
     * @return int total count (should be 27)
     */
    public static int getAlphabetCount() {
        return SPANISH_ALPHABET.size();
    }
    
    /**
     * Validate that the Spanish alphabet contains all expected characters
     * 
     * @return true if alphabet is complete and valid
     */
    public static boolean validateAlphabetCompleteness() {
        return SPANISH_ALPHABET.size() == 27 && 
               SPANISH_ALPHABET.contains("Ñ") &&
               SPANISH_ALPHABET.contains("A") &&
               SPANISH_ALPHABET.contains("Z");
    }
}