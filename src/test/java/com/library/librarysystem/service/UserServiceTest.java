package com.library.librarysystem.service;

import com.library.librarysystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_shouldThrowException_whenEmailExists() {
        // Arrange: Setup the mock behavior
        String existingEmail = "user3@gmail.com";
        when(userRepository.existsByEmail(existingEmail)).thenReturn(true);

        // Act & Assert: Verify that the correct exception is thrown
        assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(existingEmail, "user123");
        });
    }
}