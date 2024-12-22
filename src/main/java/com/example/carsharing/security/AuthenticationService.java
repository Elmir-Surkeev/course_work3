package com.example.carsharing.security;

import com.example.carsharing.dto.request.UserRegistrationDto;
import com.example.carsharing.exception.AuthenticationException;
import com.example.carsharing.model.User;

public interface AuthenticationService {
    User register(UserRegistrationDto user);

    User login(String login, String password) throws AuthenticationException;
}
