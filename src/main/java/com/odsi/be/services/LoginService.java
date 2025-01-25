package com.odsi.be.services;

import com.odsi.be.model.credentials.CredentialsDto;
import com.odsi.be.model.user.User;
import com.odsi.be.model.user.UserConverter;
import com.odsi.be.model.user.UserDto;
import com.odsi.be.security.auth.UserAuthProvider;
import com.odsi.be.security.validation.PasswordValidator;
import com.odsi.be.security.validation.UsernameValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserConverter userConverter;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TwoFactorAuthenticationService tfaService;
    private final UserAuthProvider userAuthProvider;
    private final PasswordValidator passwordValidator;
    private final UsernameValidator usernameValidator;

    public UserDto login(CredentialsDto credentialsDto) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        User user;
        try {
            user = userService.findByName(credentialsDto.username());
        } catch (RuntimeException e) {
            throw new RuntimeException("Invalid credentials");
        }

        if (user.isLocked()) {
            throw new RuntimeException("This user is locked. Please follow instructions at your email account.");
        }

        if (!passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()), user.getPassword())) {
            userService.addFailedAttempt(user);
            throw new RuntimeException("Invalid credentials");
        }

        userService.unlock(user);
        UserDto userDto = userConverter.toDto(user);

        if (credentialsDto.tfaCode() != null) {
            processTfaCode(credentialsDto, user, userDto);
        }
        return userDto;
    }

    private void processTfaCode(CredentialsDto credentialsDto, User user, UserDto userDto) {
        if (tfaService.isOtpNotValid(user.getSecret(), credentialsDto.tfaCode())) {
            throw new RuntimeException("Invalid code");
        }
        userDto.setToken(userAuthProvider.createToken(user.getName()));
    }

    private void throwIfInvalid(CredentialsDto dto) {
        if (!passwordValidator.isValid(dto.password())
                || !usernameValidator.isValid(dto.username())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        if (!isTfaValid(dto)) {
            throw new IllegalArgumentException("Invalid 2FA code");
        }
    }

    private boolean isTfaValid(CredentialsDto dto) {
        String tfaCode = dto.tfaCode();
        return tfaCode == null || tfaCode.matches("\\d{6}");
    }
}
