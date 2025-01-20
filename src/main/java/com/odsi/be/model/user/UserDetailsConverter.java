package com.odsi.be.model.user;

import org.springframework.stereotype.Service;

@Service
public class UserDetailsConverter {
    public UserDetailsDto toDto(User user) {
        return new UserDetailsDto(
                user.getName(),
                user.getId(),
                user.getPublicKey()
        );
    }
}
