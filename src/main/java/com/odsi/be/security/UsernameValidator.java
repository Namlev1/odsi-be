package com.odsi.be.security;

import org.springframework.stereotype.Service;

@Service
public class UsernameValidator {
    public boolean isValid(String username) {
//         4-20 len, allow characters and numbers,
//         first char cannot be a number
        return username.matches("^[a-zA-Z][a-zA-Z0-9]{3,19}$");
    }
}
