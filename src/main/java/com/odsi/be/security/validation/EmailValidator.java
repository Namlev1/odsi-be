package com.odsi.be.security.validation;

import org.springframework.stereotype.Service;

@Service
public class EmailValidator {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    public boolean isValid(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        return email.matches(EMAIL_REGEX);
    }
}
