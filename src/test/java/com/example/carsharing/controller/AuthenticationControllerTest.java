package com.example.carsharing.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.carsharing.dto.request.UserLoginDto;
import com.example.carsharing.dto.request.UserRegistrationDto;
import com.example.carsharing.dto.response.UserResponseDto;
import com.example.carsharing.exception.AuthenticationException;
import com.example.carsharing.model.User;
import com.example.carsharing.security.AuthenticationService;
import com.example.carsharing.security.jwt.JwtTokenProvider;
import com.example.carsharing.service.mapper.UserMapper;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {
    @Mock
    private AuthenticationService authService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @InjectMocks
    private AuthenticationController authController;
    private User user;
    private UserRegistrationDto userRegistrationDto;
    private UserLoginDto userLoginDto;
    private UserResponseDto userResponseDto;

    @BeforeEach
    void setUp() {
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("test@test.com");
        userRegistrationDto.setFirstName("test_name");
        userRegistrationDto.setLastName("test_lastName");
        userRegistrationDto.setPassword("string123");
        userRegistrationDto.setRepeatPassword("string123");

        userLoginDto = new UserLoginDto();
        userLoginDto.setLogin("test@test.com");
        userLoginDto.setPassword("string123");

        user = new User();
        user.setId(1L);
        user.setEmail(userRegistrationDto.getEmail());
        user.setFirstName(userRegistrationDto.getFirstName());
        user.setLastName(userRegistrationDto.getLastName());
        user.setPassword(userRegistrationDto.getPassword());
        user.setRole(User.Role.CUSTOMER);

        userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setFirstName(user.getFirstName());
        userResponseDto.setLastName(user.getLastName());
        userResponseDto.setRole(user.getRole().name());
    }

    @Test
    void register_validUserRegistrationDto_Ok() {
        when(authService.register(any(UserRegistrationDto.class))).thenReturn(user);
        when(userMapper.mapToDto(any(User.class))).thenReturn(userResponseDto);
        UserResponseDto actual = authController.register(userRegistrationDto);
        assertEquals(userResponseDto, actual);
    }

    @Test
    void login_validUserLoginDto_Ok() throws AuthenticationException {
        when(authService.login(any(String.class), any(String.class))).thenReturn(user);
        when(jwtTokenProvider.createToken(user.getEmail(),
                List.of(user.getRole().name()))).thenReturn("jwt.token");
        ResponseEntity<Object> actual = authController.login(userLoginDto);
        assertEquals(new ResponseEntity<>(Map.of("token",
                "jwt.token"), HttpStatus.OK), actual);
    }
}
