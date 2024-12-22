package com.example.carsharing.controller;

import com.example.carsharing.dto.request.UserRegistrationDto;
import com.example.carsharing.dto.response.UserResponseDto;
import com.example.carsharing.model.User;
import com.example.carsharing.service.UserService;
import com.example.carsharing.service.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PutMapping("/{id}/role")
    public UserResponseDto updateRole(@PathVariable Long id, @RequestParam User.Role role) {
        return userMapper.mapToDto(userService.updateRole(id, role));
    }

    @GetMapping("/me")
    public UserResponseDto getProfileInfo(Authentication authentication) {
        return userMapper.mapToDto(userService.getByEmail(authentication.getName()).get());
    }

    @PatchMapping("/me")
    public UserResponseDto updateProfileInfo(Authentication authentication,
                                             @RequestBody UserRegistrationDto userDto) {
        Long userId = userService.getByEmail(authentication.getName()).get().getId();

        return userMapper.mapToDto(
                userService.update(
                        userMapper.mapToEntity(userDto).setId(userId)
                )
        );
    }
}
