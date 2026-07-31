package com.project.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * PerformanceUtils
 * 
 * Provides performance measurement and tracking utilities for test execution.
 * Supports TC-008 Directory Alphabetical Index Filter Validation with
 * <3 minute total execution time target.
 */
public class PerformanceUtils {
    
    /**
     * Performance targets for TC-008
     */
    public static final long TOTAL_EXECUTION_TIME_TARGET_MS = 180000; // 3 minutes
    public static final long PER_LETTER_PROCESSING_TARGET_MS = 10000; // 10 seconds
    public static final long EMPTY_STATE_DETECTION_TARGET_MS = 1000; // 1 second
    public static final long RESULT_VALIDATION_TARGET_MS = 2000; // 2 seconds
    
    /**
     * Performance measurement storage
     */
    private static final Map<String, Long> startTimes = new HashMap<>();
    private static final Map<String, Long> endTimes = new HashMap<>();
    private static final Map<String, Long> durations = new HashMap<>();
    
    /**
     * Start timing a performance measurement
     * 
     * @param operationName The name of the operation being timed
     */
    public static void startTimer(String operationName) {
        startTimes.put(operationName, System.currentTimeMillis());
        ReportLogger.log("Performance: Started timing operation: " + operationName);
    }
    
    /**
     * End timing a performance measurement
     * 
     * @param operationName The name of the operation being timed
     * @return The duration in milliseconds
     */
    public static long endTimer(String operationName) {
        long endTime = System.currentTimeMillis();
        endTimes.put(operationName, endTime);
        
        Long startTime = startTimes.get(operationName);
        if (startTime == null) {
            ReportLogger.log("Warning: No start time found for operation: " + operationName);
            return 0;
        }
        
        long duration = endTime - startTime;
        durations.put(operationName, duration);
        
        ReportLogger.log("Performance: Operation '" + operationName + "' completed in " + duration + "ms");
        return duration;
    }
    
    /**
     * Get the duration of a completed operation
     * 
     * @param operationName The name of the operation
     * @return Duration in milliseconds, or 0 if not found
     */
    public static long getDuration(String operationName) {
        return durations.getOrDefault(operationName, 0L);
    }
    
    /**
     * Get the duration in a human-readable format
     * 
     * @param operationName The name of the operation
     * @return Formatted duration string
     */
    public static String getFormattedDuration(String operationName) {
        long durationMs = getDuration(operationName);
        return formatDuration(durationMs);
    }
    
    /**
     * Format milliseconds into human-readable format
     * 
     * @param durationMs Duration in milliseconds
     * @return Formatted duration string
     */
    public static String formatDuration(long durationMs) {
        if (durationMs < 1000) {
            return durationMs + "ms";
        } else if (durationMs < 60000) {
            return String.format("%.2fs", durationMs / 1000.0);
        } else {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(durationMs);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(durationMs) % 60;
            return String.format("%dm %ds", minutes, seconds);
        }
    }
    
    /**
     * Check if an operation meets its performance target
     * 
     * @param operationName The name of the operation
     * @param targetMs The target duration in milliseconds
     * @return true if the operation meets or exceeds the target
     */
    public static boolean meetsTarget(String operationName, long targetMs) {
        long duration = getDuration(operationName);
        boolean meetsTarget = duration <= targetMs;
        
        ReportLogger.log(String.format(
            "Performance Target Check: '%s' - Actual: %dms, Target: %dms, Result: %s",
            operationName, duration, targetMs, meetsTarget ? "PASS" : "FAIL"
        ));
        
        return meetsTarget;
    }
    
    /**
     * Check if per-letter processing meets target
     * 
     * @param letter The letter being processed
     * @return true if processing meets the 10-second target
     */
    public static boolean meetsPerLetterTarget(String letter) {
        String operationName = "process_letter_" + letter;
        return meetsTarget(operationName, PER_LETTER_PROCESSING_TARGET_MS);
    }
    
    /**
     * Check if total execution meets target
     * 
     * @return true if total execution meets the 3-minute target
     */
    public static boolean meetsTotalExecutionTarget() {
        return meetsTarget("total_execution", TOTAL_EXECUTION_TIME_TARGET_MS);
    }
    
    /**
     * Start timing total execution
     */
    public static void startTotalExecutionTimer() {
        startTimer("total_execution");
    }
    
    /**
     * End timing total execution
     * 
     * @return Total execution duration in milliseconds
     */
    public static long endTotalExecutionTimer() {
        return endTimer("total_execution");
    }
    
    /**
     * Start timing letter processing
     * 
     * @param letter The letter being processed
     */
    public static void startLetterProcessing(String letter) {
        startTimer("process_letter_" + letter);
    }
    
    /**
     * End timing letter processing
     * 
     * @param letter The letter being processed
     * @return Processing duration in milliseconds
     */
    public static long endLetterProcessing(String letter) {
        return endTimer("process_letter_" + letter);
    }
    
    /**
     * Start timing empty state detection
     * 
     * @param letter The letter being checked
     */
    public static void startEmptyStateDetection(String letter) {
        startTimer("empty_state_" + letter);
    }
    
    /**
     * End timing empty state detection
     * 
     * @param letter The letter being checked
     * @return Detection duration in milliseconds
     */
    public static long endEmptyStateDetection(String letter) {
        return endTimer("empty_state_" + letter);
    }
    
    /**
     * Generate performance report
     * 
     * @return Formatted performance report
     */
    public static String generatePerformanceReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== Performance Report ===\n");
        
        // Total execution time
        long totalDuration = getDuration("total_execution");
        report.append(String.format("Total Execution Time: %s (Target: <3 minutes)\n", 
            formatDuration(totalDuration)));
        
        // Per-letter processing times
        report.append("\nPer-Letter Processing Times:\n");
        for (String key : durations.keySet()) {
            if (key.startsWith("process_letter_")) {
                String letter = key.substring("process_letter_".length());
                long duration = durations.get(key);
                boolean meetsTarget = duration <= PER_LETTER_PROCESSING_TARGET_MS;
                report.append(String.format("  Letter %s: %s %s\n", 
                    letter, formatDuration(duration), meetsTarget ? "✓" : "✗"));
            }
        }
        
        // Empty state detection times
        report.append("\nEmpty State Detection Times:\n");
        for (String key : durations.keySet()) {
            if (key.startsWith("empty_state_")) {
                String letter = key.substring("empty_state_".length());
                long duration = durations.get(key);
                boolean meetsTarget = duration <= EMPTY_STATE_DETECTION_TARGET_MS;
                report.append(String.format("  Letter %s: %s %s\n", 
                    letter, formatDuration(duration), meetsTarget ? "✓" : "✗"));
            }
        }
        
        // Summary
        report.append("\n=== Summary ===\n");
        report.append(String.format("Total Operations: %d\n", durations.size()));
        report.append(String.format("Total Execution Target Met: %s\n", 
            meetsTotalExecutionTarget() ? "YES" : "NO"));
        
        return report.toString();
    }
    
    /**
     * Log performance report
     */
    public static void logPerformanceReport() {
        ReportLogger.log(generatePerformanceReport());
    }
    
    /**
     * Clear all performance measurements
     */
    public static void clearMeasurements() {
        startTimes.clear();
        endTimes.clear();
        durations.clear();
        ReportLogger.log("Performance: All measurements cleared");
    }
    
    /**
     * Get performance statistics
     * 
     * @return PerformanceStats object with summary statistics
     */
    public static PerformanceStats getPerformanceStats() {
        PerformanceStats stats = new PerformanceStats();
        stats.totalOperations = durations.size();
        stats.totalExecutionTime = getDuration("total_execution");
        stats.meetsTotalTarget = meetsTotalExecutionTarget();
        
        // Calculate per-letter statistics
        int letterCount = 0;
        long totalLetterTime = 0;
        int lettersMeetingTarget = 0;
        
        for (String key : durations.keySet()) {
            if (key.startsWith("process_letter_")) {
                letterCount++;
                long duration = durations.get(key);
                totalLetterTime += duration;
                if (duration <= PER_LETTER_PROCESSING_TARGET_MS) {
                    lettersMeetingTarget++;
                }
            }
        }
        
        stats.lettersProcessed = letterCount;
        stats.averageLetterTime = letterCount > 0 ? totalLetterTime / letterCount : 0;
        stats.lettersMeetingTarget = lettersMeetingTarget;
        
        return stats;
    }
    
    /**
     * Performance statistics summary
     */
    public static class PerformanceStats {
        public int totalOperations;
        public long totalExecutionTime;
        public boolean meetsTotalTarget;
        public int lettersProcessed;
        public long averageLetterTime;
        public int lettersMeetingTarget;
        
        @Override
        public String toString() {
            return String.format(
                "PerformanceStats{totalOperations=%d, totalExecutionTime=%s, meetsTotalTarget=%s, lettersProcessed=%d, averageLetterTime=%s, lettersMeetingTarget=%d}",
                totalOperations, formatDuration(totalExecutionTime), meetsTotalTarget, 
                lettersProcessed, formatDuration(averageLetterTime), lettersMeetingTarget
            );
        }
        
        private static String formatDuration(long durationMs) {
            return PerformanceUtils.formatDuration(durationMs);
        }
    }
}