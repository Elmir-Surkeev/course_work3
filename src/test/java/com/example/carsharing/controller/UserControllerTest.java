package com.example.carsharing.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.example.carsharing.dto.request.UserRegistrationDto;
import com.example.carsharing.dto.response.UserResponseDto;
import com.example.carsharing.model.User;
import com.example.carsharing.service.UserService;
import com.example.carsharing.service.mapper.UserMapper;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    private static final Long USER_ID = 1L;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    @Test
    void updateRole_validIdAndRole_ok() {
        User.Role updatedRole = User.Role.MANAGER;
        User updatedUser = new User();
        updatedUser.setId(USER_ID);
        updatedUser.setRole(updatedRole);

        when(userService.updateRole(USER_ID, updatedRole)).thenReturn(updatedUser);
        when(userMapper.mapToDto(updatedUser)).thenReturn(new UserResponseDto());

        UserResponseDto responseDto = userController.updateRole(USER_ID, updatedRole);

        assertNotNull(responseDto);
    }

    @Test
    void getProfileInfo_authenticatedUser_ok() {
        Authentication authentication = Mockito.mock(Authentication.class);
        String userEmail = "test@example.com";
        User user = new User();
        user.setId(USER_ID);

        when(authentication.getName()).thenReturn(userEmail);
        when(userService.getByEmail(userEmail)).thenReturn(Optional.of(user));
        when(userMapper.mapToDto(user)).thenReturn(new UserResponseDto());

        UserResponseDto responseDto = userController.getProfileInfo(authentication);

        assertNotNull(responseDto);
    }

    @Test
    void updateProfileInfo_authenticatedUserAndUserDto_ok() {
        Authentication authentication = Mockito.mock(Authentication.class);
        String userEmail = "test@example.com";
        User user = new User();
        user.setId(USER_ID);

        UserRegistrationDto userDto = new UserRegistrationDto();

        when(authentication.getName()).thenReturn(userEmail);
        when(userService.getByEmail(userEmail)).thenReturn(Optional.of(user));
        when(userMapper.mapToEntity(userDto)).thenReturn(user);
        when(userService.update(user)).thenReturn(user);
        when(userMapper.mapToDto(user)).thenReturn(new UserResponseDto());

        UserResponseDto responseDto = userController.updateProfileInfo(authentication, userDto);

        assertNotNull(responseDto);
    }
}
