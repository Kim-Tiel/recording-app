package com.joysistvi.recordingapp.service;

import com.joysistvi.recordingapp.model.User;
import com.joysistvi.recordingapp.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {

    // Constructor Injection
    // Using Composition Service is loosely coupled to the Repository
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllActiveUsers() {
        return userRepository.findAllActive();
    }

    @Override
    public List<User> getAllArchivedUsers() {
        return userRepository.findAllArchived();
    }

    @Override
    public boolean createUser(String username, String rawPassword, String email) {
        // Guard Clauses (validate BEFORE hashing)
        if (username == null || username.trim().isEmpty()) {
            System.out.println("\nValidation Error: Username is required.");
            return false;
        }

        if (email == null || email.trim().isEmpty() || !email.contains("@") || !email.contains(".")) {
            System.out.println("\nValidation Error: Valid email is required.");
            return false;
        }

        if (rawPassword == null || rawPassword.trim().length() < 6) {
            System.out.println("\nValidation Error: Password must be at least 6 characters.");
            return false;
        }

        String hashedPassword = BCrypt.hashpw(rawPassword.trim(), BCrypt.gensalt());

        User user = new User(username.trim(), hashedPassword, email.trim(), "USER");
        return userRepository.save(user);
    }

    @Override
    public boolean updateUser(int id, String username, String email) {
        // Guard Clauses
        if (id <= 0) {
            System.out.println("\nValidation Error: ID must be greater than 0.");
            return false;
        }

        if (username == null || username.trim().isEmpty()) {
            System.out.println("\nValidation Error: Username is required.");
            return false;
        }

        if (email == null || email.trim().isEmpty() || !email.contains("@") || !email.contains(".")) {
            System.out.println("\nValidation Error: Valid email is required.");
            return false;
        }

        User user = new User(id, username.trim(), email.trim());
        return userRepository.update(user);
    }

    @Override
    public boolean deleteUser(int id) {
        // Guard Clause
        if (id <= 0) {
            System.out.println("\nValidation Error: ID must be greater than 0.");
            return false;
        }

        return userRepository.deleteById(id);
    }

    @Override
    public boolean archiveUser(int id) {
        // Guard Clause
        if (id <= 0) {
            System.out.println("\nValidation Error: ID must be greater than 0.");
            return false;
        }

        return userRepository.archiveById(id);
    }

    @Override
    public boolean restoreUser(int id) {
        // Guard Clause
        if (id <= 0) {
            System.out.println("\nValidation Error: ID must be greater than 0.");
            return false;
        }

        return userRepository.restoreById(id);
    }

    @Override
    public List<User> searchUsers(String keyword) {
        // Guard Clause
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("\nValidation Error: Search keyword is required.");
            return new ArrayList<>();
        }

        return userRepository.searchByUsername(keyword.trim());
    }

    @Override
    public User loginUser(String username, String candidatePassword) {
        // Guard Clauses
        if (username == null || username.trim().isEmpty()) {
            System.out.println("\nValidation Error: Username is required.");
            return null;
        }

        if (candidatePassword == null || candidatePassword.isEmpty()) {
            System.out.println("\nValidation Error: Password is required.");
            return null;
        }

        User user = userRepository.findByUsername(username.trim());

        if (user == null) {
            return null; // unknown username
        }

        try {
            if (BCrypt.checkpw(candidatePassword, user.getPassword())) {
                return user; // correct password
            }
        } catch (IllegalArgumentException e) {
            // old users (admin, kim, anderson) are not BCrypt hashes -> treat as failed login
        }

        return null; // wrong password
    }
}