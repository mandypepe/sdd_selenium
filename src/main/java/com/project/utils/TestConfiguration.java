package com.project.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class for managing test configuration including offline mode and connectivity settings.
 * Provides centralized configuration management for test execution modes.
 */
public class TestConfiguration {
    
    private static final String CONFIG_FILE = "/config/test-data.properties";
    private static Properties properties;
    
    static {
        loadProperties();
    }
    
    private static void loadProperties() {
        properties = new Properties();
        try (InputStream input = TestConfiguration.class.getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                properties.load(input);
                ReportLogger.log("Test configuration loaded from: " + CONFIG_FILE);
            } else {
                ReportLogger.log("Configuration file not found: " + CONFIG_FILE + ". Using defaults.");
                setDefaultProperties();
            }
        } catch (IOException e) {
            ReportLogger.log("Error loading configuration: " + e.getMessage() + ". Using defaults.");
            setDefaultProperties();
        }
    }
    
    private static void setDefaultProperties() {
        properties.setProperty("test.offline.mode", "false");
        properties.setProperty("test.connectivity.timeout", "10000");
        properties.setProperty("test.retry.attempts", "3");
        properties.setProperty("test.skip.on.connectivity.failure", "true");
    }
    
    /**
     * Check if tests should run in offline mode
     */
    public static boolean isOfflineMode() {
        return Boolean.parseBoolean(properties.getProperty("test.offline.mode", "false"));
    }
    
    /**
     * Get connectivity timeout in milliseconds
     */
    public static int getConnectivityTimeout() {
        return Integer.parseInt(properties.getProperty("test.connectivity.timeout", "10000"));
    }
    
    /**
     * Get number of retry attempts for connectivity
     */
    public static int getRetryAttempts() {
        return Integer.parseInt(properties.getProperty("test.retry.attempts", "3"));
    }
    
    /**
     * Check if tests should be skipped on connectivity failure
     */
    public static boolean shouldSkipOnConnectivityFailure() {
        return Boolean.parseBoolean(properties.getProperty("test.skip.on.connectivity.failure", "true"));
    }
    
    /**
     * Get property value with default
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Set property value (for runtime configuration)
     */
    public static void setProperty(String key, String value) {
        properties.setProperty(key, value);
        ReportLogger.log("Test configuration updated: " + key + " = " + value);
    }
    
    /**
     * Enable offline mode for testing
     */
    public static void enableOfflineMode() {
        setProperty("test.offline.mode", "true");
        ReportLogger.log("OFFLINE MODE ENABLED: Tests will skip external dependencies");
    }
    
    /**
     * Disable offline mode for testing
     */
    public static void disableOfflineMode() {
        setProperty("test.offline.mode", "false");
        ReportLogger.log("ONLINE MODE ENABLED: Tests will require external dependencies");
    }
    
    /**
     * Get all configuration properties as string for debugging
     */
    public static String getConfigurationSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Test Configuration:\n");
        summary.append("  Offline Mode: ").append(isOfflineMode()).append("\n");
        summary.append("  Connectivity Timeout: ").append(getConnectivityTimeout()).append("ms\n");
        summary.append("  Retry Attempts: ").append(getRetryAttempts()).append("\n");
        summary.append("  Skip on Connectivity Failure: ").append(shouldSkipOnConnectivityFailure()).append("\n");
        return summary.toString();
    }
}