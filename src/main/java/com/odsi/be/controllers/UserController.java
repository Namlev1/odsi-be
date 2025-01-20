package com.odsi.be.controllers;

import com.odsi.be.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<?> getUserByName(@PathVariable String username) {
        try {
            return ResponseEntity.ok(userService.getUserByName(username));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Invalid user ID");
        }
    }
}
