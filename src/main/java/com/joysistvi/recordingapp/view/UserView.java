package com.joysistvi.recordingapp.view;

import com.joysistvi.recordingapp.model.User;
import com.joysistvi.recordingapp.service.UserService;

import java.util.List;

// View(Presentation Tier) -> Console menus, Scanner inputs, and formatted output
public class UserView {

    private final UserService userService;

    public UserView(UserService userService) {
        this.userService = userService;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n===== USERS =====");
            System.out.println("1. View All Users");
            System.out.println("2. Register User");
            System.out.println("3. Update User");
            System.out.println("4. Delete User");
            System.out.println("5. Archive User");
            System.out.println("6. Restore User");
            System.out.println("7. Search Users");
            System.out.println("8. Login");
            System.out.println("0. Back");
            String choice = ConsoleInput.readString("Choose: ").trim();

            switch (choice) {
                case "1":
                    displayAllUsers();
                    break;
                case "2":
                    promptCreateUser();
                    break;
                case "3":
                    promptUpdateUser();
                    break;
                case "4":
                    promptDeleteUser();
                    break;
                case "5":
                    promptArchiveUser();
                    break;
                case "6":
                    promptRestoreUser();
                    break;
                case "7":
                    promptSearchUsers();
                    break;
                case "8":
                    promptLoginUser();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("\nInvalid choice.");
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
        int id = ConsoleInput.readInt("Enter user ID to update: ");
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

    public void promptDeleteUser() {
        System.out.println("\n----- Delete User -----");
        int id = ConsoleInput.readInt("Enter user ID to delete: ");

        boolean deleted = userService.deleteUser(id);

        if (deleted) {
            System.out.println("\nUser ID " + id + " deleted successfully!");
            displayAllUsers();
        } else {
            System.out.println("Failed to delete user.");
        }
    }

    public void promptArchiveUser() {
        System.out.println("\n----- Archive User -----");
        int id = ConsoleInput.readInt("Enter user ID to archive: ");

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

        // display archived users first
        List<User> archivedUsers = userService.getAllArchivedUsers();

        if (archivedUsers.isEmpty()) {
            System.out.println("No archived users found.");
            return;
        }

        System.out.println("\n----- Archived Users -----");
        printUsers(archivedUsers);

        int id = ConsoleInput.readInt("Enter user ID to restore: ");

        boolean restored = userService.restoreUser(id);

        if (restored) {
            System.out.println("\nUser ID " + id + " restored successfully!");
            displayAllUsers();
        } else {
            System.out.println("Failed to restore user.");
        }
    }

    public void promptSearchUsers() {
        System.out.println("\n----- Search Users -----");
        String keyword = ConsoleInput.readString("Enter keyword: ");

        if (keyword == null || keyword.trim().isEmpty()) {
            userService.searchUsers(keyword); // shows the validation error
            return;
        }

        List<User> users = userService.searchUsers(keyword);

        System.out.println("\n----- Search Users (Keyword: '" + keyword.trim() + "') -----");
        printUsers(users);
    }

    public void promptLoginUser() {
        System.out.println("\n----- Login -----");
        String username = ConsoleInput.readString("Enter username: ");
        String password = ConsoleInput.readString("Enter password: ");

        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            userService.loginUser(username, password); // shows the validation error
            return;
        }

        User user = userService.loginUser(username, password);

        if (user != null) {
            System.out.println("\nLogin successful! Welcome, " + user.getUsername() + " (Role: " + user.getRole() + ")");
        } else {
            System.out.println("\nLogin failed: Invalid username or password.");
        }
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
}