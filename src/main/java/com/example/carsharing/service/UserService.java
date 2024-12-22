package com.example.carsharing.service;

import com.example.carsharing.model.User;
import java.util.Optional;

public interface UserService {
    User add(User user);

    User getById(Long userId);

    Optional<User> getByEmail(String email);

    boolean isPresentByEmail(String email);

    User updateRole(Long id, User.Role role);

    User update(User user);
}
