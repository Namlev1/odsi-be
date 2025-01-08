package com.odsi.be.model.user.registration;

import com.odsi.be.model.user.User;
import com.odsi.be.model.user.UserRepository;
import com.odsi.be.security.PasswordValidator;
import com.odsi.be.security.UsernameValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final RegistrationConverter converter;
    private final UserRepository repository;

    public void register(RegistrationDto dto) throws IllegalArgumentException {
        if (!PasswordValidator.isValid(dto.password())) {
            throw new IllegalArgumentException("Password is too weak");
        }
        if (!UsernameValidator.isValid(dto.username())) {
            throw new IllegalArgumentException("Invalid username");
        }
        User user = converter.toEntity(dto);
        repository.save(user);
    }
}
