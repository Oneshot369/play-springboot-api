package com.springapi.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.springapi.demo.model.entity.UserEntity;
import com.springapi.demo.repos.UserRepositoryInterface;
import com.springapi.demo.services.UserSecurityService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserSecurityServiceTest {

    @Mock
    private UserRepositoryInterface userRepo;

    @InjectMocks
    private UserSecurityService userSecurityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_UserExistsAndIsAdmin_ReturnsUserDetails() {
        // Arrange
        String username = "adminUser";
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword("encryptedPassword");
        userEntity.setAdmin(true);

        when(userRepo.findByUsername(username)).thenReturn(List.of(userEntity));

        // Act
        UserDetails userDetails = userSecurityService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("encryptedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ADMIN")));

        verify(userRepo, times(1)).updateLastLoginTime(anyString(), eq(userEntity.getId()));
    }

    @Test
    void loadUserByUsername_UserExistsButNotAdmin_ReturnsUserDetails() {
        // Arrange
        String username = "regularUser";
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword("encryptedPassword");
        userEntity.setAdmin(false);

        when(userRepo.findByUsername(username)).thenReturn(List.of(userEntity));

        // Act
        UserDetails userDetails = userSecurityService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("encryptedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().isEmpty());

        verify(userRepo, times(1)).updateLastLoginTime(anyString(), eq(userEntity.getId()));
    }

    @Test
    void loadUserByUsername_UserDoesNotExist_ThrowsUsernameNotFoundException() {
        // Arrange
        String username = "nonExistentUser";

        when(userRepo.findByUsername(username)).thenReturn(Collections.emptyList());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userSecurityService.loadUserByUsername(username));

        assertEquals("User not found with username: " + username, exception.getMessage());
        verify(userRepo, never()).updateLastLoginTime(anyString(), anyLong());
    }

    @Test
    void loadUserByUsername_MultipleUsersFound_ReturnsNull() {
        // Arrange
        String username = "duplicateUser";
        UserEntity user1 = new UserEntity();
        user1.setUsername(username);

        UserEntity user2 = new UserEntity();
        user2.setUsername(username);

        when(userRepo.findByUsername(username)).thenReturn(List.of(user1, user2));

        // Act
        UserDetails userDetails = userSecurityService.loadUserByUsername(username);

        // Assert
        assertNull(userDetails);
        verify(userRepo, never()).updateLastLoginTime(anyString(), anyLong());
    }
}