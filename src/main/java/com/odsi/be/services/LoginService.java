package com.odsi.be.services;

import com.odsi.be.model.credentials.CredentialsDto;
import com.odsi.be.model.user.User;
import com.odsi.be.model.user.UserConverter;
import com.odsi.be.model.user.UserDto;
import com.odsi.be.security.auth.UserAuthProvider;
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

    public UserDto login(CredentialsDto credentialsDto) {
        User user = userService.findByName(credentialsDto.username());
        if (!passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
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
}
