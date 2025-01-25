package com.odsi.be.services;

import com.odsi.be.model.credentials.CredentialsConverter;
import com.odsi.be.model.credentials.CredentialsDto;
import com.odsi.be.model.credentials.RegisterResponseDto;
import com.odsi.be.model.user.User;
import com.odsi.be.model.user.UserRepository;
import com.odsi.be.security.validation.EmailValidator;
import com.odsi.be.security.validation.PasswordValidator;
import com.odsi.be.security.validation.UsernameValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log
public class CredentialsService {
    private final CredentialsConverter converter;
    private final UserRepository repository;
    private final PasswordValidator passwordValidator;
    private final UsernameValidator usernameValidator;
    private final EmailValidator emailValidator;
    private final TwoFactorAuthenticationService tfaService;

    // No sql injection, because "username" doesn't allow '()',
    // and password is encoded anyway.
    public RegisterResponseDto register(CredentialsDto dto) throws IllegalArgumentException {
        if (!passwordValidator.isValid(dto.password())) {
            throw new IllegalArgumentException("Invalid password");
        }
        if (!usernameValidator.isValid(dto.username())) {
            throw new IllegalArgumentException("Invalid username");
        }
        if (!emailValidator.isValid(dto.email())) {
            throw new IllegalArgumentException("Invalid email");
        }
        if (repository.existsByName(dto.username())) {
            throw new IllegalArgumentException("User with this name already exists");
        }

        User user = converter.toEntity(dto);
        String secret = tfaService.generateNewSecret();
        user.setSecret(secret);
        repository.save(user);
        log.info("Created new user " + user.getName() + " with ID: " + user.getId());
        return new RegisterResponseDto(tfaService.generateQrCodeImageUri(secret, user.getName()));
    }
}
