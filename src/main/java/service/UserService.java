package com.indiapost.brsrplatform.service;

import com.indiapost.brsrplatform.entity.User;
import com.indiapost.brsrplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        user.setIsActive(true);
        userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}