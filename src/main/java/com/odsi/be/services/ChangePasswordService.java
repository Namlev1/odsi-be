package com.odsi.be.services;

import com.odsi.be.exceptions.CredentialsException;
import com.odsi.be.model.changePassword.ChangePasswordDto;
import com.odsi.be.model.user.User;
import com.odsi.be.model.user.UserRepository;
import com.odsi.be.security.validation.PasswordValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;

@Service
@RequiredArgsConstructor
public class ChangePasswordService {
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidator passwordValidator;
    private final UserRepository userRepository;
    private final TwoFactorAuthenticationService tfaService;

    public void changePassword(User user, ChangePasswordDto dto) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        throwIfInvalid(user, dto);

        if (dto.tfaCode() == null) {
            return;
        }

        processTfaCode(user, dto);
        String encodedPassword = passwordEncoder.encode(dto.newPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    private void throwIfInvalid(User user, ChangePasswordDto dto) {
        if (!passwordValidator.isValid(dto.oldPassword())) {
            throw new CredentialsException("Old password is invalid format");
        }
        if (!passwordValidator.isValid(dto.newPassword())) {
            throw new CredentialsException("New password is invalid format");
        }
        if (!passwordEncoder.matches(CharBuffer.wrap(dto.oldPassword()), user.getPassword())) {
            throw new CredentialsException("Old password does not match");
        }
        if (!dto.newPassword().equals(dto.confirmPassword())) {
            throw new CredentialsException("New password does not match confirmed password");
        }
    }

    private void processTfaCode(User user, ChangePasswordDto dto) {
        if (tfaService.isOtpNotValid(user.getSecret(), dto.tfaCode())) {
            throw new RuntimeException("Invalid code");
        }
    }
}
