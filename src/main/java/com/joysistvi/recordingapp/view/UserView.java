package com.joysistvi.recordingapp.view;

import com.joysistvi.recordingapp.model.User;
import com.joysistvi.recordingapp.service.UserService;

import java.util.List;

public class UserView {

    private final UserService userService;

    public UserView(UserService userService) {
        this.userService = userService;
    }

    public void handleMenu() {
        while (true) {
            System.out.println("\n=== USER MANAGEMENT ===");
            System.out.println("1. View All Active Users");
            System.out.println("2. Register User");
            System.out.println("3. Update User");
            System.out.println("4. Search Users");
            System.out.println("5. Archive User (Soft Delete)");
            System.out.println("6. Restore Archived User");
            System.out.println("7. Delete User (Permanent)");
            System.out.println("0. Back");

            int choice = promptForInt("Choose an option: ");
            switch (choice) {
                case 1 -> displayAllUsers();
                case 2 -> promptCreateUser();
                case 3 -> promptUpdateUser();
                case 4 -> promptSearchUsers();
                case 5 -> promptArchiveUser();
                case 6 -> promptRestoreUser();
                case 7 -> promptDeleteUser();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public void displayAllUsers() {
        List<User> users = userService.getAllActiveUsers();

        System.out.println("\n----- Users -----");
        printUsers(users);
    }

    public void promptCreateUser() {
        System.out.println("\n----- Register New User -----");
        String username = ConsoleInput.readString("Enter username: ");
        String email = ConsoleInput.readString("Enter email: ");
        String password = ConsoleInput.readString("Enter password: ");

        boolean created = userService.createUser(username, password, email);

        if (created) {
            System.out.println("\nUser '" + username.trim() + "' created successfully!");
        } else {
            System.out.println("Failed to create user.");
        }
    }

    public void promptUpdateUser() {
        System.out.println("\n----- Update User -----");
        int id = promptForInt("Enter user ID to update: ");
        String username = ConsoleInput.readString("Enter new username: ");
        String email = ConsoleInput.readString("Enter new email: ");

        boolean updated = userService.updateUser(id, username, email);

        if (updated) {
            System.out.println("\nUser ID " + id + " updated successfully!");
            displayAllUsers();
        } else {
            System.out.println("Failed to update user.");
        }
    }

    public void promptSearchUsers() {
        System.out.println("\n----- Search Users -----");
        String keyword = ConsoleInput.readString("Enter keyword: ");

        if (keyword == null || keyword.trim().isEmpty()) {
            userService.searchUsers(keyword);
            return;
        }

        List<User> users = userService.searchUsers(keyword);

        System.out.println("\n----- Search Users (Keyword: '" + keyword.trim() + "') -----");
        printUsers(users);
    }

    public void promptArchiveUser() {
        System.out.println("\n----- Archive User -----");
        int id = promptForInt("Enter user ID to archive: ");

        boolean archived = userService.archiveUser(id);

        if (archived) {
            System.out.println("\nUser ID " + id + " archived successfully!");
            displayAllUsers();
        } else {
            System.out.println("Failed to archive user.");
        }
    }

    public void promptRestoreUser() {
        System.out.println("\n----- Restore User -----");

        List<User> archivedUsers = userService.getAllArchivedUsers();

        if (archivedUsers.isEmpty()) {
            System.out.println("No archived users found.");
            return;
        }

        System.out.println("\n----- Archived Users -----");
        printUsers(archivedUsers);

        int id = promptForInt("Enter user ID to restore: ");

        boolean restored = userService.restoreUser(id);

        if (restored) {
            System.out.println("\nUser ID " + id + " restored successfully!");
            displayAllUsers();
        } else {
            System.out.println("Failed to restore user.");
        }
    }

    public void promptDeleteUser() {
        System.out.println("\n----- Delete User -----");
        int id = promptForInt("Enter user ID to delete: ");

        boolean deleted = userService.deleteUser(id);

        if (deleted) {
            System.out.println("\nUser ID " + id + " deleted successfully!");
            displayAllUsers();
        } else {
            System.out.println("Failed to delete user.");
        }
    }

    public User promptLogin() {
        System.out.println("\n----- Login -----");
        String username = ConsoleInput.readString("Enter username: ");
        String password = ConsoleInput.readString("Enter password: ");

        User user = userService.loginUser(username, password);

        if (user != null) {
            System.out.println("\nLogin successful! Welcome, " + user.getUsername() + " (Role: " + user.getRole() + ")");
        } else {
            System.out.println("\nLogin failed: Invalid username or password.");
        }

        return user;
    }

    private void printUsers(List<User> users) {
        if (users == null || users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        System.out.printf("%-4s %-20s %-30s %-6s%n", "ID", "Username", "Email", "Role");

        for (User user : users) {
            System.out.printf("%-4d %-20s %-30s %-6s%n",
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole());
        }
    }

    private int promptForInt(String prompt) {
        return ConsoleInput.readInt(prompt);
    }
}
