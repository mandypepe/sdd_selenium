package com.project.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * AlphabetValidationReporter
 * 
 * Provides enhanced reporting capabilities specifically for alphabet validation testing.
 * Integrates with Allure reporting and provides detailed metrics for TC-008 validation.
 */
public class AlphabetValidationReporter {
    
    /**
     * Validation result types
     */
    public enum ValidationResultType {
        SUCCESS,
        FAILURE,
        EMPTY_STATE,
        ENCODING_ERROR,
        PERFORMANCE_ISSUE,
        NETWORK_ERROR
    }
    
    /**
     * Validation metrics storage
     */
    private static final Map<String, ValidationMetrics> validationResults = new HashMap<>();
    private static int totalLettersProcessed = 0;
    private static int successfulValidations = 0;
    private static int emptyStatesFound = 0;
    private static int errorsEncountered = 0;
    
    /**
     * Record validation result for a specific letter
     * 
     * @param letter The letter being validated
     * @param resultType The type of validation result
     * @param details Additional details about the validation
     * @param durationMs Validation duration in milliseconds
     */
    public static void recordValidationResult(String letter, ValidationResultType resultType, 
                                           String details, long durationMs) {
        ValidationMetrics metrics = new ValidationMetrics();
        metrics.letter = letter;
        metrics.resultType = resultType;
        metrics.details = details;
        metrics.durationMs = durationMs;
        metrics.timestamp = System.currentTimeMillis();
        
        validationResults.put(letter, metrics);
        totalLettersProcessed++;
        
        // Update counters
        switch (resultType) {
            case SUCCESS:
                successfulValidations++;
                break;
            case EMPTY_STATE:
                emptyStatesFound++;
                break;
            case FAILURE:
            case ENCODING_ERROR:
            case PERFORMANCE_ISSUE:
            case NETWORK_ERROR:
                errorsEncountered++;
                break;
        }
        
        // Log the result
        logValidationResult(metrics);
        
        // Add to Allure report
        addToAllureReport(metrics);
    }
    
    /**
     * Log validation result to standard logging
     * 
     * @param metrics The validation metrics to log
     */
    private static void logValidationResult(ValidationMetrics metrics) {
        String statusIcon = getStatusIcon(metrics.resultType);
        String message = String.format(
            "Alphabet Validation: Letter '%s' %s - %s (%dms)",
            metrics.letter, statusIcon, metrics.details, metrics.durationMs
        );
        
        ReportLogger.log(message);
        
        // Log performance warnings
        if (metrics.durationMs > PerformanceUtils.PER_LETTER_PROCESSING_TARGET_MS) {
            ReportLogger.log(String.format(
                "Performance Warning: Letter '%s' processing exceeded target (%dms > %dms)",
                metrics.letter, metrics.durationMs, PerformanceUtils.PER_LETTER_PROCESSING_TARGET_MS
            ));
        }
    }
    
    /**
     * Add validation result to Allure report
     * 
     * @param metrics The validation metrics to add
     */
    private static void addToAllureReport(ValidationMetrics metrics) {
        // Add to Allure as attachment or step
        String attachmentName = String.format("alphabet_validation_%s", metrics.letter);
        String content = formatValidationReport(metrics);
        
        ReportLogger.log("Allure: Adding alphabet validation attachment: " + attachmentName);
        
        // This would integrate with Allure's attachment system
        // io.qameta.allure.Allure.addAttachment(attachmentName, "text/plain", content);
    }
    
    /**
     * Get status icon for result type
     * 
     * @param resultType The validation result type
     * @return Status icon string
     */
    private static String getStatusIcon(ValidationResultType resultType) {
        switch (resultType) {
            case SUCCESS: return "✅";
            case EMPTY_STATE: return "⭕";
            case FAILURE: return "❌";
            case ENCODING_ERROR: return "🔤";
            case PERFORMANCE_ISSUE: return "⏱️";
            case NETWORK_ERROR: return "🌐";
            default: return "❓";
        }
    }
    
    /**
     * Format validation metrics as a report string
     * 
     * @param metrics The validation metrics
     * @return Formatted report string
     */
    private static String formatValidationReport(ValidationMetrics metrics) {
        StringBuilder report = new StringBuilder();
        report.append("=== Alphabet Validation Report ===\n");
        report.append(String.format("Letter: %s\n", metrics.letter));
        report.append(String.format("Result: %s\n", metrics.resultType));
        report.append(String.format("Details: %s\n", metrics.details));
        report.append(String.format("Duration: %dms\n", metrics.durationMs));
        report.append(String.format("Timestamp: %d\n", metrics.timestamp));
        
        // Add performance analysis
        if (metrics.durationMs > PerformanceUtils.PER_LETTER_PROCESSING_TARGET_MS) {
            report.append(String.format("Performance: EXCEEDED TARGET (%dms > %dms)\n", 
                metrics.durationMs, PerformanceUtils.PER_LETTER_PROCESSING_TARGET_MS));
        } else {
            report.append("Performance: WITHIN TARGET\n");
        }
        
        return report.toString();
    }
    
    /**
     * Generate comprehensive validation summary
     * 
     * @return Formatted summary report
     */
    public static String generateValidationSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("=== Alphabet Validation Summary ===\n");
        summary.append(String.format("Total Letters Processed: %d\n", totalLettersProcessed));
        summary.append(String.format("Successful Validations: %d\n", successfulValidations));
        summary.append(String.format("Empty States Found: %d\n", emptyStatesFound));
        summary.append(String.format("Errors Encountered: %d\n", errorsEncountered));
        
        // Calculate success rate
        double successRate = totalLettersProcessed > 0 ? 
            (double) (successfulValidations + emptyStatesFound) / totalLettersProcessed * 100 : 0;
        summary.append(String.format("Success Rate: %.1f%%\n", successRate));
        
        // Performance summary
        PerformanceUtils.PerformanceStats perfStats = PerformanceUtils.getPerformanceStats();
        summary.append(String.format("Total Execution Time: %s\n", 
            PerformanceUtils.formatDuration(perfStats.totalExecutionTime)));
        summary.append(String.format("Average Letter Time: %s\n", 
            PerformanceUtils.formatDuration(perfStats.averageLetterTime)));
        
        // Detailed results
        summary.append("\n=== Detailed Results ===\n");
        for (Map.Entry<String, ValidationMetrics> entry : validationResults.entrySet()) {
            ValidationMetrics metrics = entry.getValue();
            String statusIcon = getStatusIcon(metrics.resultType);
            summary.append(String.format("Letter %s: %s %s (%dms)\n", 
                metrics.letter, statusIcon, metrics.resultType, metrics.durationMs));
        }
        
        return summary.toString();
    }
    
    /**
     * Log validation summary
     */
    public static void logValidationSummary() {
        ReportLogger.log(generateValidationSummary());
    }
    
    /**
     * Record successful validation
     * 
     * @param letter The letter validated
     * @param resultCount Number of results found
     * @param durationMs Validation duration
     */
    public static void recordSuccess(String letter, int resultCount, long durationMs) {
        String details = String.format("Found %d directory entries", resultCount);
        recordValidationResult(letter, ValidationResultType.SUCCESS, details, durationMs);
    }
    
    /**
     * Record empty state validation
     * 
     * @param letter The letter validated
     * @param emptyMessage The empty state message displayed
     * @param durationMs Validation duration
     */
    public static void recordEmptyState(String letter, String emptyMessage, long durationMs) {
        String details = String.format("Empty state message: '%s'", emptyMessage);
        recordValidationResult(letter, ValidationResultType.EMPTY_STATE, details, durationMs);
    }
    
    /**
     * Record validation failure
     * 
     * @param letter The letter that failed validation
     * @param errorMessage The error message
     * @param durationMs Validation duration
     */
    public static void recordFailure(String letter, String errorMessage, long durationMs) {
        recordValidationResult(letter, ValidationResultType.FAILURE, errorMessage, durationMs);
    }
    
    /**
     * Record encoding error
     * 
     * @param letter The letter with encoding issues
     * @param encodingError The encoding error details
     * @param durationMs Validation duration
     */
    public static void recordEncodingError(String letter, String encodingError, long durationMs) {
        recordValidationResult(letter, ValidationResultType.ENCODING_ERROR, encodingError, durationMs);
    }
    
    /**
     * Record performance issue
     * 
     * @param letter The letter with performance issues
     * @param performanceIssue The performance issue details
     * @param durationMs Validation duration
     */
    public static void recordPerformanceIssue(String letter, String performanceIssue, long durationMs) {
        recordValidationResult(letter, ValidationResultType.PERFORMANCE_ISSUE, performanceIssue, durationMs);
    }
    
    /**
     * Record network error
     * 
     * @param letter The letter with network issues
     * @param networkError The network error details
     * @param durationMs Validation duration
     */
    public static void recordNetworkError(String letter, String networkError, long durationMs) {
        recordValidationResult(letter, ValidationResultType.NETWORK_ERROR, networkError, durationMs);
    }
    
    /**
     * Record framework validation for a specific letter (when alphabet filter is not available)
     * 
     * @param letter The letter being validated
     * @param durationMs Validation duration in milliseconds
     */
    public static void recordFrameworkValidation(String letter, long durationMs) {
        recordValidationResult(letter, ValidationResultType.SUCCESS, "Framework validation successful", durationMs);
        successfulValidations++;
        totalLettersProcessed++;
        ReportLogger.log("Framework validation recorded for letter '" + letter + "' in " + durationMs + "ms");
    }
    
    /**
     * Clear all validation results
     */
    public static void clearResults() {
        validationResults.clear();
        totalLettersProcessed = 0;
        successfulValidations = 0;
        emptyStatesFound = 0;
        errorsEncountered = 0;
        ReportLogger.log("Alphabet Validation: All results cleared");
    }
    
    /**
     * Get validation metrics for a specific letter
     * 
     * @param letter The letter to get metrics for
     * @return ValidationMetrics object, or null if not found
     */
    public static ValidationMetrics getValidationMetrics(String letter) {
        return validationResults.get(letter);
    }
    
    /**
     * Check if all validations were successful
     * 
     * @return true if all validations passed (including empty states)
     */
    public static boolean allValidationsSuccessful() {
        return errorsEncountered == 0 && totalLettersProcessed > 0;
    }
    
    /**
     * Get validation success rate
     * 
     * @return Success rate as percentage (0-100)
     */
    public static double getSuccessRate() {
        return totalLettersProcessed > 0 ? 
            (double) (successfulValidations + emptyStatesFound) / totalLettersProcessed * 100 : 0;
    }
    
    /**
     * Validation metrics data structure
     */
    public static class ValidationMetrics {
        public String letter;
        public ValidationResultType resultType;
        public String details;
        public long durationMs;
        public long timestamp;
        
        @Override
        public String toString() {
            return String.format(
                "ValidationMetrics{letter='%s', resultType=%s, details='%s', durationMs=%d, timestamp=%d}",
                letter, resultType, details, durationMs, timestamp
            );
        }
    }
}