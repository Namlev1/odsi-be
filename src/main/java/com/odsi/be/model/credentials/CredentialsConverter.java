package com.odsi.be.model.credentials;

import com.odsi.be.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CredentialsConverter {
    private final PasswordEncoder passwordEncoder;

    public User toEntity(CredentialsDto dto) {
        return User.builder()
                .name(dto.username())
                .password(passwordEncoder.encode(dto.password()))
                .build();
    }
}
