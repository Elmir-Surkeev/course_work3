package com.example.carsharing.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.carsharing.model.User;
import com.example.carsharing.repository.UserRepository;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceImplTest {
    private static final Long USER_ID = 1L;
    private static final String USER_MAIL = "test@example.com";
    private static final String NEW_USER_EMAIL = "newEmail";
    private static final String USER_PASSWORD = "password";
    private static final String NEW_USER_PASSWORD = "newPassword";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final User.Role CUSTOMER_ROLE = User.Role.CUSTOMER;
    private static final User.Role MANAGER_ROLE = User.Role.MANAGER;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;
    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(USER_ID);
        user.setEmail(USER_MAIL);
        user.setPassword(USER_PASSWORD);
        user.setRole(CUSTOMER_ROLE);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void add_ok() {
        when(passwordEncoder.encode(any())).thenReturn(ENCODED_PASSWORD);
        userService.add(user);
        verify(userRepository).save(user);
    }

    @Test
    void getById_ok() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        User foundUser = userService.getById(USER_ID);
        assertEquals(foundUser.getId(), user.getId());
    }

    @Test
    void getByEmail_ok() {
        when(userRepository.getUserByEmail(USER_MAIL)).thenReturn(Optional.of(user));
        Optional<User> foundUser = userService.getByEmail(USER_MAIL);
        assertTrue(foundUser.isPresent());
        assertEquals(user.getEmail(), foundUser.get().getEmail());
    }

    @Test
    void isPresentByEmail_ok() {
        when(userRepository.getUserByEmail(USER_MAIL)).thenReturn(Optional.of(user));
        boolean isUserPresent = userService.isPresentByEmail(USER_MAIL);
        assertTrue(isUserPresent);
    }

    @Test
    void updateRole_ok() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        User updatedUser = userService.updateRole(USER_ID, MANAGER_ROLE);
        assertEquals(MANAGER_ROLE, updatedUser.getRole());
    }

    @Test
    void update_ok() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(any())).thenReturn(ENCODED_PASSWORD);
        User newUser = new User();
        newUser.setId(USER_ID);
        newUser.setEmail(NEW_USER_EMAIL);
        newUser.setPassword(NEW_USER_PASSWORD);
        userService.update(newUser);
        assertEquals(NEW_USER_EMAIL, user.getEmail());
        assertEquals(ENCODED_PASSWORD, user.getPassword());
    }

    @Test
    void add_userAlreadyExists_notOk() {
        when(userRepository.getUserByEmail(USER_MAIL)).thenReturn(Optional.of(user));
        assertThrows(IllegalArgumentException.class, () -> userService.add(user));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getById_userDoesNotExist_notOk() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> userService.getById(USER_ID));
    }

    @Test
    void getByEmail_userDoesNotExist_notOk() {
        when(userRepository.getUserByEmail(USER_MAIL)).thenReturn(Optional.empty());
        Optional<User> foundUser = userService.getByEmail(USER_MAIL);
        assertFalse(foundUser.isPresent());
    }

    @Test
    void isPresentByEmail_userDoesNotExist_notOk() {
        when(userRepository.getUserByEmail(USER_MAIL)).thenReturn(Optional.empty());
        boolean isUserPresent = userService.isPresentByEmail(USER_MAIL);
        assertFalse(isUserPresent);
    }

    @Test
    void updateRole_userDoesNotExist_notOk() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class,
                () -> userService.updateRole(USER_ID, MANAGER_ROLE));
    }

    @Test
    void update_userDoesNotExist_notOk() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
        User newUser = new User();
        newUser.setId(USER_ID);
        newUser.setEmail(NEW_USER_EMAIL);
        newUser.setPassword(NEW_USER_PASSWORD);
        assertThrows(NoSuchElementException.class,
                () -> userService.update(newUser));
    }

    @Test
    void update_newUserIsNull_notOk() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.update(null));
        verify(userRepository, never()).save(any(User.class));
    }
}
