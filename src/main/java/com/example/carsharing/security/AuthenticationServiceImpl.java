package com.example.carsharing.security;

import com.example.carsharing.dto.request.UserRegistrationDto;
import com.example.carsharing.exception.AuthenticationException;
import com.example.carsharing.model.User;
import com.example.carsharing.service.UserService;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User register(UserRegistrationDto user) {
        if (userService.isPresentByEmail(user.getEmail())) {
            throw new RuntimeException("User are already exist with email: " + user.getEmail());
        }
        User newUser = new User()
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setRole(User.Role.CUSTOMER);
        return userService.add(newUser);
    }

    @Override
    public User login(String login, String password) throws AuthenticationException {
        Optional<User> user = userService.getByEmail(login);
        if (user.isEmpty() || !passwordEncoder.matches(password, user.get().getPassword())) {
            throw new AuthenticationException("Incorrect username or password!!!");
        }
        return user.get();
    }
}
