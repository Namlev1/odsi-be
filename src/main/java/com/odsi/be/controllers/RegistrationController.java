package com.odsi.be.controllers;

import com.odsi.be.model.credentials.CredentialsDto;
import com.odsi.be.model.credentials.RegisterResponseDto;
import com.odsi.be.services.CredentialsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/registration")
@RequiredArgsConstructor
public class RegistrationController {
    private final CredentialsService service;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody CredentialsDto dto) {
        try {
            RegisterResponseDto response = service.register(dto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
