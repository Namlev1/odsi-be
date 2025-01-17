package com.odsi.be.services;

import com.odsi.be.model.credentials.CredentialsDto;
import com.odsi.be.model.user.User;
import com.odsi.be.model.user.UserConverter;
import com.odsi.be.model.user.UserDto;
import com.odsi.be.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;
    private final TwoFactorAuthenticationService tfaService;

    public User findByName(String name) {
        return userRepository.findByName(name)
                .orElseThrow();
    }

    public UserDto login(CredentialsDto credentialsDto) {
        User user = userRepository.findByName(credentialsDto.username())
                .orElseThrow();
        if (!passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        if (tfaService.isOtpNotValid(user.getSecret(), credentialsDto.tfaCode())) {
            throw new RuntimeException("Invalid code");
        }
        return userConverter.toDto(user);
    }
}
