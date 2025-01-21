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

    public void changePassword(User user, ChangePasswordDto dto) {
        throwIfInvalid(user, dto);
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
}
