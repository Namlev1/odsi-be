package com.odsi.be.security;

import com.odsi.be.security.validation.UsernameValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UsernameValidatorTest {
    private final UsernameValidator validator = new UsernameValidator();

    @Test
    void testValidUsername() {
        assertTrue(validator.isValid("ValidUser123"));
    }

    @Test
    void testShortUsername() {
        assertFalse(validator.isValid("abc"));
    }

    @Test
    void testLongUsername() {
        assertFalse(validator.isValid("ThisUsernameIsWayTooLong123"));
    }

    @Test
    void testUsernameStartingWithNumber() {
        assertFalse(validator.isValid("1InvalidUsername"));
    }

    @Test
    void testUsernameWithSpecialCharacters() {
        assertFalse(validator.isValid("Invalid@User"));
    }

    @Test
    void testUsernameWithOnlyLetters() {
        assertTrue(validator.isValid("OnlyLetters"));
    }

    @Test
    void testUsernameWithOnlyNumbers() {
        assertFalse(validator.isValid("12345678"));
    }
}