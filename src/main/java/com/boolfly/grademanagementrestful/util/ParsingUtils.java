package com.boolfly.grademanagementrestful.util;

public final class ParsingUtils {
    private ParsingUtils() {
        // private constructor
    }

    public static Double parseDouble(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return Double.valueOf(value);
    }
}
