package com.example.carsharing.controller;

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
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authService;
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationDto requestDto) {
        return userMapper.mapToDto(authService.register(requestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid UserLoginDto userLoginDto)
            throws AuthenticationException {
        User user = authService.login(userLoginDto.getLogin(),
                userLoginDto.getPassword());
        String token = jwtTokenProvider.createToken(user.getEmail(),
                List.of(user.getRole().name()));
        return new ResponseEntity<>(Map.of("token", token), HttpStatus.OK);
    }
}
