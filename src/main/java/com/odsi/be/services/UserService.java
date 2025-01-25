package com.odsi.be.services;

import com.odsi.be.model.user.User;
import com.odsi.be.model.user.UserDetailsConverter;
import com.odsi.be.model.user.UserDetailsDto;
import com.odsi.be.model.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserDetailsConverter userDetailsConverter;

    public User findByName(String name) {
        return userRepository.findByName(name)
                .orElseThrow();
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public UserDetailsDto getUserByName(String username) {
        return userDetailsConverter.toDto(findByName(username));
    }

    public void addFailedAttempt(User user) {
        user.addFailedAttempt();
        userRepository.save(user);
    }

    public void unlock(User user) {
        user.unlock();
        userRepository.save(user);
    }
}
