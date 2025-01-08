package com.odsi.be.security;

import com.odsi.be.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserRepository userRepository;

    // TODO change to Argon2
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        int saltLength = 16; // salt length in bytes
//        int hashLength = 32; // hash length in bytes
//        int parallelism = 1; // currently not supported by Spring Security
//        int memoryCost = 1 << 14; // memory costs (16 MB)
//        int iterations = 5; // number of iterations
//
//        return new Argon2PasswordEncoder(saltLength, hashLength, parallelism, memoryCost, iterations);
//    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByName(username).orElseThrow(() -> new UsernameNotFoundException("User '" + username + "' not found."));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/registration").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(withDefaults())
                .httpBasic(withDefaults())
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
