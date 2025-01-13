package com.odsi.be.controllers;

import com.odsi.be.model.credentials.CredentialsDto;
import com.odsi.be.model.user.UserDto;
import com.odsi.be.security.auth.UserAuthProvider;
import com.odsi.be.security.jwt.TokenBlacklistService;
import com.odsi.be.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {
    private final UserService userService;
    private final UserAuthProvider userAuthProvider;
    private final TokenBlacklistService tokenBlacklistService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CredentialsDto credentialsDto) {
        // todo exception handling
        UserDto user = userService.login(credentialsDto);

        user.setToken(userAuthProvider.createToken(user.getName()));
        return ResponseEntity.ok(user);
    }

    @PostMapping("auth/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        String extractedToken = token.replace("Bearer ", "");
        tokenBlacklistService.blacklistToken(extractedToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/aa")
    public String aa() {
        return "aa";
    }
}
