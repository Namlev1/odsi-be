package com.odsi.be.controllers;

import com.odsi.be.model.changePassword.ChangePasswordDto;
import com.odsi.be.model.user.User;
import com.odsi.be.services.ChangePasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/change-pass")
public class ChangePasswordController {
    private final ChangePasswordService changePasswordService;

    @PostMapping
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal User user, @RequestBody ChangePasswordDto dto) {
        try {
            changePasswordService.changePassword(user, dto);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
