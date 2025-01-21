package com.odsi.be.model.changePassword;

public record ChangePasswordDto(
        String oldPassword,
        String newPassword,
        String confirmPassword
) {
}
