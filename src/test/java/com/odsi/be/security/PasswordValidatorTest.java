package com.odsi.be.security;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordValidatorTest {
    private final PasswordValidator validator = new PasswordValidator();

    @Test
    void testValidPassword() {
        assertTrue(validator.isValid("Valid1!Password"));
    }

    @Test
    void testShortPassword() {
        assertFalse(validator.isValid("Short1!"));
    }

    @Test
    void testNoUppercase() {
        assertFalse(validator.isValid("nouppercase1!"));
    }

    @Test
    void testNoLowercase() {
        assertFalse(validator.isValid("NOLOWERCASE1!"));
    }

    @Test
    void testNoDigit() {
        assertFalse(validator.isValid("NoDigit!"));
    }

    @Test
    void testNoSpecialChar() {
        assertFalse(validator.isValid("NoSpecialChar1"));
    }

    @Test
    void testPopularPassword() {
        validator.POPULAR_PASSWORDS.addAll(Set.of("123456", "password", "123456789"));
        assertFalse(validator.isValid("password"));
    }

    @Test
    void testLowEntropyPassword() {
        assertFalse(validator.isValid("aaaaaaaA1!"));
    }

}