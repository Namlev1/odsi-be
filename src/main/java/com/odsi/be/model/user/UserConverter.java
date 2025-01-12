package com.odsi.be.model.user;

import org.springframework.stereotype.Service;

@Service
public class UserConverter {

    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
