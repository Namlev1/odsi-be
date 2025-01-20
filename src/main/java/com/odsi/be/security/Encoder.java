package com.odsi.be.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class Encoder implements PasswordEncoder {

    private final PasswordEncoder encoder;
    @Value("${security.pepper:secret-value}")
    private String PEPPER;

    public Encoder() {
        this.encoder = new Argon2PasswordEncoder(
                16, // Memory in KB (e.g., 16 * 1024 for 16 MB)
                32, // Salt length
                1,  // Parallelism (threads)
                32, // Hash length
                3   // Iterations (key stretching)
        );
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return encoder.encode(rawPassword + PEPPER);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword + PEPPER, encodedPassword);
    }
}
