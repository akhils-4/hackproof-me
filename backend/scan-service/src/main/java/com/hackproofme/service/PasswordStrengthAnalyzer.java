package com.hackproofme.service;


import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PasswordStrengthAnalyzer {

    private static final String SPECIAL_CHARS = "!(){}[]:;<>?,@#$%^&*+=_-~`|./\\";
    private static final List<String> COMMON_PASSWORDS = Arrays.asList(
            "password", "123456", "qwerty", "admin", "letmein",
            "welcome", "monkey", "1234567890", "abc123", "password123"
    );

    public PasswordAnalysisResult analyze(String password) {
        if (password == null || password.isEmpty()) {
            return PasswordAnalysisResult.builder()
                    .score(0)
                    .suggestions(List.of("No password provided for analysis"))
                    .build();
        }

        List<String> suggestions = new ArrayList<>();
        int score = 0;

        // Length check (max 30 points)
        int length = password.length();
        if (length < 8) {
            suggestions.add("Password should be at least 8 characters long");
            score += length * 2;
        } else if (length < 12) {
            suggestions.add("Consider using 12+ characters for better security");
            score += 20;
        } else if (length >= 16) {
            score += 30;
        } else {
            score += 25;
        }

        // Character diversity (max 40 points)
        boolean hasLower = false, hasUpper = false, hasDigit = false, hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c)) hasLower = true;
            if (Character.isUpperCase(c)) hasUpper = true;
            if (Character.isDigit(c)) hasDigit = true;
            if (SPECIAL_CHARS.indexOf(c) >= 0) hasSpecial = true;
        }

        if (hasLower) score += 10;
        else suggestions.add("Add lowercase letters");

        if (hasUpper) score += 10;
        else suggestions.add("Add uppercase letters");

        if (hasDigit) score += 10;
        else suggestions.add("Add numbers");

        if (hasSpecial) score += 10;
        else suggestions.add("Add special characters (!@#$%^&*)");

        // Pattern detection (max 20 points)
        if (!containsSequentialChars(password) && !containsRepetitiveChars(password)) {
            score += 20;
        } else {
            suggestions.add("Avoid sequential or repetitive characters");
            score += 5;
        }

        // Common password check (max 10 points)
        if (!isCommonPassword(password.toLowerCase())) {
            score += 10;
        } else {
            suggestions.add("This is a commonly used password - choose something unique");
            score = Math.min(score, 30);
        }

        score = Math.min(score, 100);

        return PasswordAnalysisResult.builder()
                .score(score)
                .suggestions(suggestions)
                .build();
    }

    private boolean containsSequentialChars(String password) {
        for (int i = 0; i < password.length() - 2; i++) {
            char c1 = password.charAt(i);
            char c2 = password.charAt(i + 1);
            char c3 = password.charAt(i + 2);

            if (c2 == c1 + 1 && c3 == c2 + 1) {
                return true;
            }
        }
        return false;
    }

    private boolean containsRepetitiveChars(String password) {
        for (int i = 0; i < password.length() - 2; i++) {
            if (password.charAt(i) == password.charAt(i + 1) &&
                    password.charAt(i) == password.charAt(i + 2)) {
                return true;
            }
        }
        return false;
    }

    private boolean isCommonPassword(String password) {
        return COMMON_PASSWORDS.stream()
                .anyMatch(common -> password.contains(common));
    }

    @lombok.Data
    @lombok.Builder
    public static class PasswordAnalysisResult {
        private int score;
        private List<String> suggestions;
    }
}
