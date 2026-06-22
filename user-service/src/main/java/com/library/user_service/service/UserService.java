package com.library.user_service.service;

import com.library.user_service.entity.User;
import com.library.user_service.exception.ResourceNotFoundException;
import com.library.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Create
    public User createUser(User user) {
        // Business logic: Ensure default role if none provided
        if (user.getRole() == null) {
            user.setRole("ROLE_USER");
        }
        return userRepository.save(user);
    }

    // Read All
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Read One (with Exception Handling)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    // Read by Username (needed for Spring Security later)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    // Update
    public User updateUser(Long id, User userDetails) {
        User existingUser = getUserById(id);
        existingUser.setUsername(userDetails.getUsername());
        if (userDetails.getPassword() != null) {
            existingUser.setPassword(userDetails.getPassword());
        }
        return userRepository.save(existingUser);
    }

    // Delete
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
}