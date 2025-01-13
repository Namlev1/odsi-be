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

    public UserDto findByName(String name) {
        User user = userRepository.findByName(name)
                .orElseThrow();
        return userConverter.toDto(user);
    }

    public UserDto login(CredentialsDto credentialsDto) {
        User user = userRepository.findByName(credentialsDto.username())
                .orElseThrow();
        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()), user.getPassword())) {
            return userConverter.toDto(user);
        }

        throw new RuntimeException("Invalid password");
    }
}
