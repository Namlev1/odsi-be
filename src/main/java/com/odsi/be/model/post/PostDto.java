package com.odsi.be.model.post;

public record PostDto(Long id, String title, String content, String username, String signature) {
}