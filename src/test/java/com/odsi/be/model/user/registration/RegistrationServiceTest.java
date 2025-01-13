package com.odsi.be.model.user.registration;

import com.odsi.be.model.credentials.CredentialsConverter;
import com.odsi.be.model.credentials.CredentialsDto;
import com.odsi.be.model.credentials.CredentialsService;
import com.odsi.be.model.user.User;
import com.odsi.be.model.user.UserRepository;
import com.odsi.be.security.PasswordValidator;
import com.odsi.be.security.UsernameValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RegistrationServiceTest {

    @Mock
    private CredentialsConverter converter;
    @Mock
    private UserRepository repository;
    @Mock
    private PasswordValidator passwordValidator;
    @Mock
    private UsernameValidator usernameValidator;

    @InjectMocks
    private CredentialsService registrationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterValidUser() {
        CredentialsDto dto = new CredentialsDto("ValidUser123", "Valid1!Password");
        User user = new User();

        when(converter.toEntity(dto)).thenReturn(user);
        when(passwordValidator.isValid(dto.password())).thenReturn(true);
        when(usernameValidator.isValid(dto.username())).thenReturn(true);

        registrationService.register(dto);

        verify(repository, times(1)).save(user);
    }

    @Test
    void testRegisterInvalidPassword() {
        CredentialsDto dto = new CredentialsDto("ValidUser123", "weakpass");

        when(passwordValidator.isValid(dto.password())).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> registrationService.register(dto));

        assertEquals("Password is too weak", exception.getMessage());
        verify(repository, never()).save(any(User.class));
    }

    @Test
    void testRegisterInvalidUsername() {
        CredentialsDto dto = new CredentialsDto("Invalid@User", "Valid1!Password");

        when(passwordValidator.isValid(dto.password())).thenReturn(true);
        when(usernameValidator.isValid(dto.username())).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> registrationService.register(dto));

        assertEquals("Invalid username", exception.getMessage());
        verify(repository, never()).save(any(User.class));
    }
}