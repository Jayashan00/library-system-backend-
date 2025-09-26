package com.library.librarysystem.service;

import com.library.librarysystem.model.User;
import com.library.librarysystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Error: Email is already in use!");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Set.of("ROLE_USER"));
        return userRepository.save(user);
    }
}