package com.odsi.be.controllers;

import com.odsi.be.model.registration.CredentialsDto;
import com.odsi.be.model.user.UserDto;
import com.odsi.be.security.UserAuthProvider;
import com.odsi.be.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {
    private final UserService userService;
    private final UserAuthProvider userAuthProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CredentialsDto credentialsDto) {
        // todo exception handling
        UserDto user = userService.login(credentialsDto);

        user.setToken(userAuthProvider.createToken(user.getName()));
        return ResponseEntity.ok(user);
    }

    // todo register
}
