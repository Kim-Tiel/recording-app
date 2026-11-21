package com.joysistvi.recordingapp.service;

import com.joysistvi.recordingapp.model.User;

import java.util.List;

// Service(Application Tier) -> Business rules, validation, guard clauses
public interface UserService {

    List<User> getAllActiveUsers();

    List<User> getAllArchivedUsers();

    boolean createUser(String username, String rawPassword, String email);

    boolean updateUser(int id, String username, String email);

    boolean deleteUser(int id);

    boolean archiveUser(int id);

    boolean restoreUser(int id);

    List<User> searchUsers(String keyword);

    User loginUser(String username, String candidatePassword);
}