package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    private final DbConnection dbConnection;

    public UserRepositoryImpl(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public List<User> findAllActive() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users WHERE is_archived = 0 ORDER BY id ASC";

        try (Connection connection = dbConnection.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                String role = resultSet.getString("role");
                boolean archived = resultSet.getBoolean("is_archived");
                users.add(new User(id, username, password, email, role, archived));
            }
        } catch (SQLException e) {
            System.out.println("\nRead Users Error: " + e.getMessage());
        }

        return users;
    }

    @Override
    public List<User> findAllArchived() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users WHERE is_archived = 1 ORDER BY id ASC";

        try (Connection connection = dbConnection.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                String role = resultSet.getString("role");
                boolean archived = resultSet.getBoolean("is_archived");
                users.add(new User(id, username, password, email, role, archived));
            }
        } catch (SQLException e) {
            System.out.println("\nRead Archived Users Error: " + e.getMessage());
        }

        return users;
    }

    @Override
    public User findByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ? AND is_archived = 0";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String password = resultSet.getString("password");
                    String email = resultSet.getString("email");
                    String role = resultSet.getString("role");
                    boolean archived = resultSet.getBoolean("is_archived");
                    return new User(id, resultSet.getString("username"), password, email, role, archived);
                }
            }
        } catch (SQLException e) {
            System.out.println("\nFind User Error: " + e.getMessage());
        }

        return null;
    }

    @Override
    public boolean save(User user) {
        String query = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getRole());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("\nCreate User Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(User user) {
        String query = "UPDATE users SET username = ?, email = ? WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setInt(3, user.getId());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("\nUpdate User Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteById(int id) {
        String query = "DELETE FROM users WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("\nDelete User Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean archiveById(int id) {
        String query = "UPDATE users SET is_archived = 1 WHERE id = ? AND is_archived = 0";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("\nArchive User Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean restoreById(int id) {
        String query = "UPDATE users SET is_archived = 0 WHERE id = ? AND is_archived = 1";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("\nRestore User Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<User> searchByUsername(String keyword) {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users WHERE is_archived = 0 AND username LIKE ? ORDER BY id ASC";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, "%" + keyword + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String email = resultSet.getString("email");
                    String role = resultSet.getString("role");
                    boolean archived = resultSet.getBoolean("is_archived");
                    users.add(new User(id, username, password, email, role, archived));
                }
            }
        } catch (SQLException e) {
            System.out.println("\nSearch User Error: " + e.getMessage());
        }

        return users;
    }
}