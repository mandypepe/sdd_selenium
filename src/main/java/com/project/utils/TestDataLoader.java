package com.project.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class for loading test data from external configuration files.
 * Eliminates hardcoded test data and provides centralized configuration management.
 */
public class TestDataLoader {

    private static final String CONFIG_FILE = "config/test-data.properties";
    private static Properties properties;

    static {
        loadProperties();
    }

    /**
     * Loads properties from the configuration file
     */
    private static void loadProperties() {
        properties = new Properties();
        try (InputStream input = TestDataLoader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                properties.load(input);
                ReportLogger.log("Successfully loaded test data from: " + CONFIG_FILE);
            } else {
                ReportLogger.log("Warning: Could not find test data file: " + CONFIG_FILE);
            }
        } catch (IOException e) {
            ReportLogger.log("Error loading test data: " + e.getMessage());
        }
    }

    /**
     * Gets a string property value
     */
    public static String getString(String key) {
        return properties.getProperty(key, "");
    }

    /**
     * Gets a string property value with default
     */
    public static String getString(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Gets an integer property value
     */
    public static int getInt(String key) {
        return getInt(key, 0);
    }

    /**
     * Gets an integer property value with default
     */
    public static int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            ReportLogger.log("Invalid integer value for key '" + key + "', using default: " + defaultValue);
            return defaultValue;
        }
    }

    /**
     * Gets a boolean property value
     */
    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * Gets a boolean property value with default
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = properties.getProperty(key, String.valueOf(defaultValue));
        return Boolean.parseBoolean(value);
    }

    /**
     * Gets a long property value
     */
    public static long getLong(String key) {
        return getLong(key, 0L);
    }

    /**
     * Gets a long property value with default
     */
    public static long getLong(String key, long defaultValue) {
        try {
            return Long.parseLong(properties.getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            ReportLogger.log("Invalid long value for key '" + key + "', using default: " + defaultValue);
            return defaultValue;
        }
    }

    /**
     * Gets a double property value with default
     */
    public static double getDouble(String key, double defaultValue) {
        try {
            return Double.parseDouble(properties.getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            ReportLogger.log("Invalid double value for key '" + key + "', using default: " + defaultValue);
            return defaultValue;
        }
    }

    /**
     * Gets a comma-separated string as an array
     */
    public static String[] getStringArray(String key) {
        String value = properties.getProperty(key, "");
        if (value.trim().isEmpty()) {
            return new String[0];
        }
        return value.split(",");
    }

    /**
     * Reloads properties from the configuration file
     */
    public static void reload() {
        loadProperties();
    }

    /**
     * Checks if a property key exists
     */
    public static boolean containsKey(String key) {
        return properties.containsKey(key);
    }

    /**
     * Gets all property keys
     */
    public static java.util.Set<String> getAllKeys() {
        return properties.stringPropertyNames();
    }

    /**
     * Prints all loaded properties (for debugging)
     */
    public static void printAllProperties() {
        ReportLogger.log("=== Test Data Properties ===");
        for (String key : properties.stringPropertyNames()) {
            ReportLogger.log(key + " = " + properties.getProperty(key));
        }
        ReportLogger.log("=== End Test Data Properties ===");
    }
}