package com.odsi.be.model.user.registration;

import com.odsi.be.model.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationConverterTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegistrationConverter registrationConverter;

    @Test
    void testConvertToEntity() {
        // Arrange
        RegistrationDto dto = new RegistrationDto("testUser", "testPassword");
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(dto.password())).thenReturn(encodedPassword);

        User expectedUser = User.builder()
                .name(dto.username())
                .password(encodedPassword)
                .build();

        // Act
        User actualUser = registrationConverter.toEntity(dto);

        // Assert
        assertEquals(expectedUser.getName(), actualUser.getName());
        assertEquals(expectedUser.getPassword(), actualUser.getPassword());
    }

    @Test
    void testConvertToEntityWithEmptyPassword() {
        // Arrange
        RegistrationDto dto = new RegistrationDto("testUser", "");
        String encodedPassword = "";
        when(passwordEncoder.encode(dto.password())).thenReturn(encodedPassword);

        User expectedUser = User.builder()
                .name(dto.username())
                .password(encodedPassword)
                .build();

        // Act
        User actualUser = registrationConverter.toEntity(dto);

        // Assert
        assertEquals(expectedUser.getName(), actualUser.getName());
        assertEquals(expectedUser.getPassword(), actualUser.getPassword());
    }

    @Test
    void testConvertToEntityWithNullPassword() {
        // Arrange
        RegistrationDto dto = new RegistrationDto("testUser", null);
        String encodedPassword = null;
        when(passwordEncoder.encode(dto.password())).thenReturn(encodedPassword);

        User expectedUser = User.builder()
                .name(dto.username())
                .password(encodedPassword)
                .build();

        // Act
        User actualUser = registrationConverter.toEntity(dto);

        // Assert
        assertEquals(expectedUser.getName(), actualUser.getName());
        assertEquals(expectedUser.getPassword(), actualUser.getPassword());
    }

    @Test
    void testConvertToEntityWithSpecialCharacters() {
        // Arrange
        RegistrationDto dto = new RegistrationDto("testUser", "p@ssw0rd!");
        String encodedPassword = "encodedSpecialPassword";
        when(passwordEncoder.encode(dto.password())).thenReturn(encodedPassword);

        User expectedUser = User.builder()
                .name(dto.username())
                .password(encodedPassword)
                .build();

        // Act
        User actualUser = registrationConverter.toEntity(dto);

        // Assert
        assertEquals(expectedUser.getName(), actualUser.getName());
        assertEquals(expectedUser.getPassword(), actualUser.getPassword());
    }

    @Test
    void testConvertToEntityWithLongPassword() {
        // Arrange
        String longPassword = "a".repeat(100);
        RegistrationDto dto = new RegistrationDto("testUser", longPassword);
        String encodedPassword = "encodedLongPassword";
        when(passwordEncoder.encode(dto.password())).thenReturn(encodedPassword);

        User expectedUser = User.builder()
                .name(dto.username())
                .password(encodedPassword)
                .build();

        // Act
        User actualUser = registrationConverter.toEntity(dto);

        // Assert
        assertEquals(expectedUser.getName(), actualUser.getName());
        assertEquals(expectedUser.getPassword(), actualUser.getPassword());
    }
}