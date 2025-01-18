package com.odsi.be.controllers;

import com.odsi.be.model.pubKey.PubKeyDto;
import com.odsi.be.model.user.User;
import com.odsi.be.services.PublicKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pubkey")
@RequiredArgsConstructor
public class PublicKeyController {
    private final PublicKeyService publicKeyService;

    @PostMapping("/me")
    public ResponseEntity<?> updateKey(@AuthenticationPrincipal User user, @RequestBody PubKeyDto pubKeyDto) {
        try {
            publicKeyService.addPublicKey(user, pubKeyDto.pubKey());
            return ResponseEntity.ok("Public key updated successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getKey(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(publicKeyService.getPublicKey(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getKey(@PathVariable Long id) {
        return ResponseEntity.ok(publicKeyService.getPublicKeyByUserId(id));
    }

    // todo decide if you want this actually, this revokes current signed posts.
    @DeleteMapping("/me")
    public ResponseEntity<?> removeKey(@AuthenticationPrincipal User user) {
        publicKeyService.removePublicKey(user);
        return ResponseEntity.ok().build();
    }
}
