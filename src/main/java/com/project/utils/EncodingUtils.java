package com.project.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * EncodingUtils
 * 
 * Provides UTF-8 encoding support for special characters in web testing.
 * Specifically designed to handle Spanish alphabet characters including Ñ.
 * 
 * Supports TC-008 Directory Alphabetical Index Filter Validation.
 */
public class EncodingUtils {
    
    /**
     * UTF-8 charset name
     */
    public static final String UTF_8 = "UTF-8";
    
    /**
     * Map of special characters to their URL-encoded equivalents
     */
    private static final Map<String, String> ENCODING_MAP = new HashMap<>();
    
    static {
        // Initialize common Spanish character encodings
        ENCODING_MAP.put("Ñ", "%C3%91");
        ENCODING_MAP.put("ñ", "%C3%B1");
        ENCODING_MAP.put("Á", "%C3%81");
        ENCODING_MAP.put("á", "%C3%A1");
        ENCODING_MAP.put("É", "%C3%89");
        ENCODING_MAP.put("é", "%C3%A9");
        ENCODING_MAP.put("Í", "%C3%8D");
        ENCODING_MAP.put("í", "%C3%AD");
        ENCODING_MAP.put("Ó", "%C3%93");
        ENCODING_MAP.put("ó", "%C3%B3");
        ENCODING_MAP.put("Ú", "%C3%9A");
        ENCODING_MAP.put("ú", "%C3%BA");
        ENCODING_MAP.put("Ü", "%C3%9C");
        ENCODING_MAP.put("ü", "%C3%BC");
    }
    
    /**
     * Encode a string using UTF-8 for URL parameters
     * 
     * @param input The string to encode
     * @return UTF-8 encoded string
     * @throws EncodingException if encoding fails
     */
    public static String encodeUTF8(String input) throws EncodingException {
        if (input == null) {
            return null;
        }
        
        try {
            return URLEncoder.encode(input, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new EncodingException("UTF-8 encoding not supported", e);
        }
    }
    
    /**
     * Encode a string using UTF-8 with fallback handling
     * 
     * @param input The string to encode
     * @return UTF-8 encoded string, or original string if encoding fails
     */
    public static String encodeUTF8Safe(String input) {
        try {
            return encodeUTF8(input);
        } catch (EncodingException e) {
            ReportLogger.log("Warning: UTF-8 encoding failed for input: " + input + ", using original string");
            return input;
        }
    }
    
    /**
     * Get URL-encoded version of a special character
     * 
     * @param character The character to encode
     * @return URL-encoded character, or null if not found
     */
    public static String getURLEncodedCharacter(String character) {
        return ENCODING_MAP.get(character);
    }
    
    /**
     * Check if a character requires special UTF-8 encoding
     * 
     * @param character The character to check
     * @return true if the character requires special encoding
     */
    public static boolean requiresSpecialEncoding(String character) {
        return ENCODING_MAP.containsKey(character);
    }
    
    /**
     * Encode a string with special character handling
     * Uses predefined encoding map for known special characters
     * 
     * @param input The string to encode
     * @return Encoded string with special characters properly handled
     */
    public static String encodeWithSpecialCharacters(String input) {
        if (input == null) {
            return null;
        }
        
        String result = input;
        for (Map.Entry<String, String> entry : ENCODING_MAP.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }
        
        return result;
    }
    
    /**
     * Validate UTF-8 encoding for Spanish alphabet characters
     * 
     * @param character The character to validate
     * @return true if the character can be properly UTF-8 encoded
     */
    public static boolean validateUTF8Encoding(String character) {
        if (character == null || character.isEmpty()) {
            return false;
        }
        
        try {
            String encoded = URLEncoder.encode(character, StandardCharsets.UTF_8.name());
            String decoded = java.net.URLDecoder.decode(encoded, StandardCharsets.UTF_8.name());
            return character.equals(decoded);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get UTF-8 byte representation of a character
     * 
     * @param character The character to convert
     * @return byte array in UTF-8 encoding
     */
    public static byte[] getUTF8Bytes(String character) {
        if (character == null) {
            return new byte[0];
        }
        
        return character.getBytes(StandardCharsets.UTF_8);
    }
    
    /**
     * Validate that a string contains only valid UTF-8 characters
     * 
     * @param input The string to validate
     * @return true if all characters are valid UTF-8
     */
    public static boolean isValidUTF8(String input) {
        if (input == null) {
            return false;
        }
        
        try {
            byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
            String reconstructed = new String(bytes, StandardCharsets.UTF_8);
            return input.equals(reconstructed);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get encoding information for a character
     * 
     * @param character The character to analyze
     * @return EncodingInfo object with encoding details
     */
    public static EncodingInfo getEncodingInfo(String character) {
        EncodingInfo info = new EncodingInfo();
        info.character = character;
        info.isValidUTF8 = validateUTF8Encoding(character);
        info.requiresSpecialEncoding = requiresSpecialEncoding(character);
        info.urlEncoded = getURLEncodedCharacter(character);
        info.utf8Bytes = getUTF8Bytes(character);
        
        return info;
    }
    
    /**
     * Custom exception for encoding errors
     */
    public static class EncodingException extends Exception {
        public EncodingException(String message) {
            super(message);
        }
        
        public EncodingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    /**
     * Information about character encoding
     */
    public static class EncodingInfo {
        public String character;
        public boolean isValidUTF8;
        public boolean requiresSpecialEncoding;
        public String urlEncoded;
        public byte[] utf8Bytes;
        
        @Override
        public String toString() {
            return String.format(
                "EncodingInfo{character='%s', isValidUTF8=%s, requiresSpecialEncoding=%s, urlEncoded='%s'}",
                character, isValidUTF8, requiresSpecialEncoding, urlEncoded
            );
        }
    }
}