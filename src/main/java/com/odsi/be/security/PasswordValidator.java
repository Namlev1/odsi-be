package com.odsi.be.security;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class PasswordValidator {
    public final Set<String> POPULAR_PASSWORDS = new HashSet<>();

    @PostConstruct
    public void init() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/main/resources/static/1000-passwords.txt"));
            POPULAR_PASSWORDS.addAll(lines);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load popular passwords", e);
        }
    }

    private double entropy(String password) {
        // Frequency
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : password.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }

        // Entropy
        double entropy = 0.0;
        int length = password.length();
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            double frequency = (double) entry.getValue() / length;
            entropy -= frequency * (Math.log(frequency) / Math.log(2));
        }

        return entropy;

    }

    public boolean isValid(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }
        }
        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar
                && !POPULAR_PASSWORDS.contains(password) && (entropy(password) > 2.5);
    }
}
