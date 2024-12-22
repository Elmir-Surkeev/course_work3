package com.example.carsharing.service.impl;

import com.example.carsharing.model.User;
import com.example.carsharing.repository.UserRepository;
import com.example.carsharing.service.UserService;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    @Transactional
    public User add(User user) {
        if (userRepository.getUserByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User already exists with email: "
                    + user.getEmail());
        }
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User getById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException("Not found user by id: " + userId)
        );
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public boolean isPresentByEmail(String email) {
        return userRepository.getUserByEmail(email).isPresent();
    }

    @Override
    @Transactional
    public User updateRole(Long id, User.Role role) {
        User userFromDb = userRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Not found user with id: " + id)
        );
        userFromDb.setRole(role);
        return userFromDb;
    }

    @Override
    @Transactional
    public User update(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        User userFromDb = userRepository.findById(user.getId()).orElseThrow(
                () -> new NoSuchElementException("Not found profile info for user: " + user));
        userFromDb.setId(user.getId())
                .setEmail(user.getEmail())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setPassword(encoder.encode(user.getPassword()));
        return userFromDb;
    }
}
