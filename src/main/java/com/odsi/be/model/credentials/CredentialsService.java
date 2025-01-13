package com.odsi.be.model.credentials;

import com.odsi.be.model.user.User;
import com.odsi.be.model.user.UserRepository;
import com.odsi.be.security.PasswordValidator;
import com.odsi.be.security.UsernameValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CredentialsService {
    private final CredentialsConverter converter;
    private final UserRepository repository;
    private final PasswordValidator passwordValidator;
    private final UsernameValidator usernameValidator;

    // No sql injection, because "username" doesn't allow '()',
    // and password is encoded anyway.
    public void register(CredentialsDto dto) throws IllegalArgumentException {
        if (!passwordValidator.isValid(dto.password())) {
            throw new IllegalArgumentException("Invalid password");
        }
        if (!usernameValidator.isValid(dto.username())) {
            throw new IllegalArgumentException("Invalid username");
        }
        if (repository.existsByName(dto.username())) {
            throw new IllegalArgumentException("User with this name already exists");
        }
        User user = converter.toEntity(dto);
        repository.save(user);
    }
}
