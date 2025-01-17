package com.odsi.be.services;

import com.odsi.be.model.user.User;
import com.odsi.be.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public User findByName(String name) {
        return userRepository.findByName(name)
                .orElseThrow();
    }

}
