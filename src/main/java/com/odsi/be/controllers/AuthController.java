package com.odsi.be.controllers;

import com.odsi.be.model.credentials.CredentialsDto;
import com.odsi.be.model.user.UserDto;
import com.odsi.be.security.jwt.TokenBlacklistService;
import com.odsi.be.services.LoginService;
import com.odsi.be.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {
    private final UserService userService;
    private final TokenBlacklistService tokenBlacklistService;
    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CredentialsDto credentialsDto) {
        try {
            UserDto user = loginService.login(credentialsDto);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("auth/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        String extractedToken = token.replace("Bearer ", "");
        tokenBlacklistService.blacklistToken(extractedToken);
        return ResponseEntity.ok().build();
    }
}
