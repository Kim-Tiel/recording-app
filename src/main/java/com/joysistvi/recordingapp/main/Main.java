package com.joysistvi.recordingapp.main;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/recording_studio_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    static int albumCreatedId;
    static int artistCreatedId;


    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // ===================== ALBUMS CRUD =====================

    public static void viewAllAlbums() {
        String sql = "SELECT id, name, year, artist_id FROM albums WHERE is_archived = 0";

        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("\n----- Albums -----");
            System.out.printf("%-4s %-25s %-6s %-10s%n", "ID", "Album Name", "Year", "Artist ID");

            while (resultSet.next()) {
                System.out.printf("%-4d %-25s %-6d %-10d%n",
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("year"),
                        resultSet.getInt("artist_id"));
            }
        } catch (SQLException e) {
            System.out.println("View Albums: " + e.getMessage());
        }
    }

    public static void createAlbum(String name, int year, int artistId) {

        if (name == null || name.trim().isEmpty()) {
            System.out.println("\nValidation Error: Album name is required.");
            return;
        }

        if (year < 1900 || year > 2100) {
            System.out.println("\nValidation Error: Year must be between 1900 and 2100.");
            return;
        }

        if (artistId <= 0) {
            System.out.println("\nValidation Error: Artist ID must be greater than 0.");
            return;
        }

        String sql = "INSERT INTO albums (name, year, artist_id) VALUES (?, ?, ?)";

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, year);
            preparedStatement.setInt(3, artistId);

            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    albumCreatedId = generatedKeys.getInt(1);
                }
            }
            System.out.println("\nAlbum: " + name + " created successfully!");
            viewAllAlbums();
        } catch (SQLException e) {
            System.out.println("Create Album: " + e.getMessage());
        }
    }

    public static void updateAlbum(int id, String name, int year, int artistId) {

        if (id <= 0) {
            System.out.println("\nValidation Error: ID must be greater than 0.");
            return;
        }

        if (name == null || name.trim().isEmpty()) {
            System.out.println("\nValidation Error: Album name is required.");
            return;
        }

        if (year < 1900 || year > 2100) {
            System.out.println("\nValidation Error: Year must be between 1900 and 2100.");
            return;
        }

        if (artistId <= 0) {
            System.out.println("\nValidation Error: Artist ID must be greater than 0.");
            return;
        }

        String sql = "UPDATE albums SET name = ?, year = ?, artist_id = ? WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, year);
            preparedStatement.setInt(3, artistId);
            preparedStatement.setInt(4, id);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("\nAlbum ID " + id + " updated successfully!");
                viewAllAlbums();
            } else {
                System.out.println("No album found with ID " + id + ".");
            }
        } catch (SQLException e) {
            System.out.println("Update Album: " + e.getMessage());
        }
    }

    public static void deleteAlbum(int id) {

        if (id <= 0) {
            System.out.println("\nValidation Error: ID must be greater than 0.");
            return;
        }

        String sql = "DELETE FROM albums WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("\nAlbum ID " + id + " deleted successfully!");
                viewAllAlbums();
            } else {
                System.out.println("No album found with ID " + id + ".");
            }
        } catch (SQLException e) {
            System.out.println("Delete Album: " + e.getMessage());
        }
    }

    public static void archiveAlbum(int id) {

        if (id <= 0) {
            System.out.println("\nValidation Error: ID must be greater than 0.");
            return;
        }

        String sql = "UPDATE albums SET is_archived = 1 WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("\nAlbum ID " + id + " archived successfully!");
                viewAllAlbums();
            } else {
                System.out.println("No album found with ID " + id + ".");
            }
        } catch (SQLException e) {
            System.out.println("Archive Album: " + e.getMessage());
        }
    }

    public static void restoreAlbum(int id) {

        if (id <= 0) {
            System.out.println("\nValidation Error: ID must be greater than 0.");
            return;
        }

        String sql = "UPDATE albums SET is_archived = 0 WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("\nAlbum ID " + id + " restored successfully!");
                viewAllAlbums();
            } else {
                System.out.println("No album found with ID " + id + ".");
            }
        } catch (SQLException e) {
            System.out.println("Restore Album: " + e.getMessage());
        }
    }

    public static void searchAlbum(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("\nValidation Error: Search keyword is required.");
            return;
        }

        String sql = "SELECT id, name, year, artist_id FROM albums WHERE name LIKE ? AND is_archived = 0";

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, "%" + keyword + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.println("\n----- Search Results: \"" + keyword + "\" -----");
                System.out.printf("%-4s %-25s %-6s %-10s%n", "ID", "Album Name", "Year", "Artist ID");

                boolean found = false;
                while (resultSet.next()) {
                    found = true;
                    System.out.printf("%-4d %-25s %-6d %-10d%n",
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getInt("year"),
                            resultSet.getInt("artist_id"));
                }
                if (!found) {
                    System.out.println("No albums found.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Search Album: " + e.getMessage());
        }
    }

    // ===================== ARTISTS CRUD =====================

    public static void viewAllArtists() {
        String sql = "SELECT id, name FROM artists WHERE is_archived = 0";

        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("\n----- Artists -----");
            System.out.printf("%-4s %-25s%n", "ID", "Artist Name");

            while (resultSet.next()) {
                System.out.printf("%-4d %-25s%n",
                        resultSet.getInt("id"),
                        resultSet.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("View Artists: " + e.getMessage());
        }
    }

    public static void createArtist(String name) {

        if (name == null || name.trim().isEmpty()) {
            System.out.println("\nValidation Error: Artist name is required.");
            return;
        }

        String sql = "INSERT INTO artists (name) VALUES (?)";

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, name);

            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    artistCreatedId = generatedKeys.getInt(1);
                }
            }
            System.out.println("\nArtist: " + name + " created successfully!");
            viewAllArtists();
        } catch (SQLException e) {
            System.out.println("Create Artist: " + e.getMessage());
        }
    }

    public static void updateArtist(int id, String name) {

        if (id <= 0) {
            System.out.println("\nValidation Error: ID must be greater than 0.");
            return;
        }

        if (name == null || name.trim().isEmpty()) {
            System.out.println("\nValidation Error: Artist name is required.");
            return;
        }

        String sql = "UPDATE artists SET name = ? WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, id);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("\nArtist ID " + id + " updated successfully!");
                viewAllArtists();
            } else {
                System.out.println("No artist found with ID " + id + ".");
            }
        } catch (SQLException e) {
            System.out.println("Update Artist: " + e.getMessage());
        }
    }

    public static void deleteArtist(int id) {

        if (id <= 0) {
            System.out.println("\nValidation Error: ID must be greater than 0.");
            return;
        }

        String sql = "DELETE FROM artists WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("\nArtist ID " + id + " deleted successfully!");
                viewAllArtists();
            } else {
                System.out.println("No artist found with ID " + id + ".");
            }
        } catch (SQLException e) {
            System.out.println("Delete Artist: " + e.getMessage());
        }
    }

    public static void archiveArtist(int id) {
        if (id <= 0) {
            System.out.println("\nValidation Error: ID must be greater than 0.");
            return;
        }

        String sql = "UPDATE artists SET is_archived = 1 WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("\nArtist ID " + id + " archived successfully!");
                viewAllArtists();
            } else {
                System.out.println("No artist found with ID " + id + ".");
            }
        } catch (SQLException e) {
            System.out.println("Archive Artist: " + e.getMessage());
        }
    }

    public static void restoreArtist(int id) {
        if (id <= 0) {
            System.out.println("\nValidation Error: ID must be greater than 0.");
            return;
        }

        String sql = "UPDATE artists SET is_archived = 0 WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("\nArtist ID " + id + " restored successfully!");
                viewAllArtists();
            } else {
                System.out.println("No artist found with ID " + id + ".");
            }
        } catch (SQLException e) {
            System.out.println("Restore Artist: " + e.getMessage());
        }
    }

    public static void searchArtist(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("\nValidation Error: Search keyword is required.");
            return;
        }

        String sql = "SELECT id, name FROM artists WHERE name LIKE ? AND is_archived = 0";

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, "%" + keyword.trim() + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.println("\n----- Search Results: \"" + keyword.trim() + "\" -----");
                System.out.printf("%-4s %-25s%n", "ID", "Artist Name");

                boolean found = false;
                while (resultSet.next()) {
                    found = true;
                    System.out.printf("%-4d %-25s%n",
                            resultSet.getInt("id"),
                            resultSet.getString("name"));
                }
                if (!found) {
                    System.out.println("No artists found.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Search Artist: " + e.getMessage());
        }
    }

    // ===================== USERS =====================

    public static void createUser(String uname, String rawPassword, String email) {

        // Guard Clause
        if (uname == null || uname.trim().isEmpty()) {
            System.out.println("\nValidation Error: Username is required.");
            return;
        }

        if (email == null || email.trim().isEmpty() || !email.contains("@") || !email.contains(".")) {
            System.out.println("\nValidation Error: Valid email is required.");
            return;
        }

        if (rawPassword == null || rawPassword.trim().length() < 6) {
            System.out.println("\nValidation Error: Password must be at least 6 characters.");
            return;
        }

        String hashedPassword = BCrypt.hashpw(rawPassword.trim(), BCrypt.gensalt());

        String query = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, 'USER')";

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, uname.trim());
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, email.trim());

            preparedStatement.executeUpdate();
            System.out.println("\nUser: " + uname + " created successfully!");
        } catch (SQLException e) {
            System.out.println("\nCreate User Error: " + e.getMessage());
        }
    }

    public static void loginUser(String uname, String candidatePassword) {
        if (uname == null || uname.trim().isEmpty()) {
            System.out.println("\nValidation Error: Username is required.");
            return;
        }

        if (candidatePassword == null || candidatePassword.isEmpty()) {
            System.out.println("\nValidation Error: Password is required.");
            return;
        }

        String sql = "SELECT username, password, role FROM users WHERE username = ?";

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, uname.trim());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next() && BCrypt.checkpw(candidatePassword, resultSet.getString("password"))) {
                    System.out.println("\nLogin successful! Welcome, " + resultSet.getString("username")
                            + " (Role: " + resultSet.getString("role") + ")");
                } else {
                    System.out.println("\nLogin failed: Invalid username or password.");
                }
            }
        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("\nLogin Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // --- Testing Albums CRUD ---
        viewAllAlbums();

        createAlbum("", 2000, 4); // Album name is required
        createAlbum("Maharot", 1800, 4); // Year must be between 1900 and 2100
        createAlbum("Maharot", 2000, 0); // Artist ID must be greater than 0
        createAlbum("Maharot", 2000, 4); // created successfully!

        updateAlbum(0, "Maharot (Remastered)", 2000, 4); // ID must be greater than 0
        updateAlbum(albumCreatedId, "", 2000, 4); // Album name is required
        updateAlbum(albumCreatedId, "Maharot (Remastered)", 2200, 4);  // Year must be between 1900 and 2100
        updateAlbum(albumCreatedId, "Maharot (Remastered)", 2000, -1); // Artist ID must be greater than 0
        updateAlbum(albumCreatedId, "Maharot (Remastered)", 2000, 4); // updated successfully!

        archiveAlbum(0); // ID must be greater than 0
        archiveAlbum(albumCreatedId); // archived successfully!

        restoreAlbum(0); // ID must be greater than 0
        restoreAlbum(albumCreatedId); // restored successfully!

        searchAlbum("  "); // Search keyword is required
        searchAlbum("Maharot"); // search results

        deleteAlbum(0); // ID must be greater than 0
        deleteAlbum(albumCreatedId); // deleted successfully!


        // --- Testing Artists CRUD ---
        viewAllArtists();

        createArtist(""); // Artist name is required
        createArtist("Silent Sanctuary"); // created successfully!

        updateArtist(0, "Silent Sanctuary Band"); // ID must be greater than 0
        updateArtist(artistCreatedId, ""); // Artist name is required
        updateArtist(artistCreatedId, "Silent Sanctuary Band"); // updated successfully!

        archiveArtist(0); // ID must be greater than 0
        archiveArtist(artistCreatedId); // archived successfully!

        restoreArtist(0); // ID must be greater than 0
        restoreArtist(artistCreatedId); // restored successfully!

        searchArtist("  "); // Search keyword is required
        searchArtist("Silent"); // search results

        deleteArtist(0); // ID must be greater than 0
        deleteArtist(artistCreatedId); // deleted successfully!


        // --- Testing Users ---
        String uname = "kim" + albumCreatedId + artistCreatedId;
        String email = uname + "@gmail.com";

        createUser("", "pass123", email); // Username is required
        createUser(uname, "pass123", "kimgmail"); // Valid email is required
        createUser(uname, "pass123", "kim@gmail"); // Valid email is required (no ".")
        createUser(uname, "123", email); // at least 6 characters
        createUser(uname, "pass123", email); // created successfully!

        loginUser("", "pass123"); // Username is required
        loginUser(uname, ""); // Password is required
        loginUser(uname, "wrongpass"); // Login failed
        loginUser(uname, "pass123"); // Login successful!
    }
}