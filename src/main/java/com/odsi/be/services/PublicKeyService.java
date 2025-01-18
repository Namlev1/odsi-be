package com.odsi.be.services;

import com.odsi.be.exceptions.InvalidPublicKeyException;
import com.odsi.be.model.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PublicKeyService {
    private final UserService userService;


    @Transactional
    public void addPublicKey(User user, String pubKey) {
        throwIfInvalid(pubKey);
        user.setPublicKey(pubKey);
        userService.save(user);
    }

    @Transactional
    public void removePublicKey(User user) {
        user.setPublicKey("");
        userService.save(user);
    }

    public String getPublicKey(User user) {
        return user.getPublicKey();
    }

    public String getPublicKeyByUserId(Long id) {
        User user = userService.findById(id);
        return user.getPublicKey();
    }

    private void throwIfInvalid(String pubKey) {
        if (pubKey == null) {
            throw new InvalidPublicKeyException("Public key cannot be empty.");
        }

        String trimmedKey = pubKey.trim();

        if (trimmedKey.isEmpty()) {
            throw new InvalidPublicKeyException("Public key cannot be empty.");
        }

        // Check for correct PEM format (basic validation for BEGIN/END)
        if (!trimmedKey.startsWith("-----BEGIN PUBLIC KEY-----") || !trimmedKey.endsWith("-----END PUBLIC KEY-----")) {
            throw new InvalidPublicKeyException("Invalid public key format.");
        }

    }
}
