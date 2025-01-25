package com.odsi.be.model.credentials;

public record CredentialsDto(
        String username,
        String password,
        String email,
        String tfaCode) {
}