package com.odsi.be.services;

import com.odsi.be.model.credentials.CredentialsConverter;
import com.odsi.be.model.credentials.CredentialsDto;
import com.odsi.be.model.credentials.RegisterResponseDto;
import com.odsi.be.model.user.User;
import com.odsi.be.model.user.UserRepository;
import com.odsi.be.security.validation.PasswordValidator;
import com.odsi.be.security.validation.UsernameValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CredentialsService {
    private final CredentialsConverter converter;
    private final UserRepository repository;
    private final PasswordValidator passwordValidator;
    private final UsernameValidator usernameValidator;
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
        if (repository.existsByName(dto.username())) {
            throw new IllegalArgumentException("User with this name already exists");
        }

        User user = converter.toEntity(dto);
        String secret = tfaService.generateNewSecret();
        user.setSecret(secret);
        repository.save(user);
        return new RegisterResponseDto(tfaService.generateQrCodeImageUri(secret));
    }
}
