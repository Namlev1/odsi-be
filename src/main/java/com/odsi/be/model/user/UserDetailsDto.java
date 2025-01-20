package com.odsi.be.model.user;

public record UserDetailsDto(
        String username,
        Long id,
        String pubKey
) {
}
