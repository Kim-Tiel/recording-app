package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.model.Playlist;
import com.joysistvi.recordingapp.model.Song;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistRepositoryImpl implements PlaylistRepository {

    private final DbConnection dbConnection;

    public PlaylistRepositoryImpl(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public List<Playlist> findAllActive() {
        List<Playlist> playlists = new ArrayList<>();
        String query = "SELECT p.id, p.name, p.user_id, u.username, p.is_archived " +
                "FROM playlists p " +
                "JOIN users u ON p.user_id = u.id " +
                "WHERE p.is_archived = 0 " +
                "ORDER BY p.id ASC";

        try (Connection connection = dbConnection.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                playlists.add(mapPlaylist(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("\nRead Playlists Error: " + e.getMessage());
        }

        return playlists;
    }

    @Override
    public List<Playlist> findAllArchived() {
        List<Playlist> playlists = new ArrayList<>();
        String query = "SELECT p.id, p.name, p.user_id, u.username, p.is_archived " +
                "FROM playlists p " +
                "JOIN users u ON p.user_id = u.id " +
                "WHERE p.is_archived = 1 " +
                "ORDER BY p.id ASC";

        try (Connection connection = dbConnection.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                playlists.add(mapPlaylist(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("\nRead Archived Playlists Error: " + e.getMessage());
        }

        return playlists;
    }

    @Override
    public boolean save(Playlist playlist) {
        String query = "INSERT INTO playlists (name, user_id) VALUES (?, ?)";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, playlist.getName());
            preparedStatement.setInt(2, playlist.getUserId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("\nCreate Playlist Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Playlist playlist) {
        String query = "UPDATE playlists SET name = ?, user_id = ? WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, playlist.getName());
            preparedStatement.setInt(2, playlist.getUserId());
            preparedStatement.setInt(3, playlist.getId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("\nUpdate Playlist Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteById(int id) {
        String deleteSongsQuery = "DELETE FROM playlist_songs WHERE playlist_id = ?";
        String deletePlaylistQuery = "DELETE FROM playlists WHERE id = ?";

        try (Connection connection = dbConnection.connect()) {
            connection.setAutoCommit(false);

            try (PreparedStatement deleteSongs = connection.prepareStatement(deleteSongsQuery);
                 PreparedStatement deletePlaylist = connection.prepareStatement(deletePlaylistQuery)) {
                deleteSongs.setInt(1, id);
                deleteSongs.executeUpdate();

                deletePlaylist.setInt(1, id);
                boolean deleted = deletePlaylist.executeUpdate() > 0;

                connection.commit();
                return deleted;
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.out.println("\nDelete Playlist Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean archiveById(int id) {
        String query = "UPDATE playlists SET is_archived = 1 WHERE id = ? AND is_archived = 0";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("\nArchive Playlist Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean restoreById(int id) {
        String query = "UPDATE playlists SET is_archived = 0 WHERE id = ? AND is_archived = 1";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("\nRestore Playlist Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Playlist> searchByName(String keyword) {
        List<Playlist> playlists = new ArrayList<>();
        String query = "SELECT p.id, p.name, p.user_id, u.username, p.is_archived " +
                "FROM playlists p " +
                "JOIN users u ON p.user_id = u.id " +
                "WHERE p.is_archived = 0 AND p.name LIKE ? " +
                "ORDER BY p.id ASC";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + keyword + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    playlists.add(mapPlaylist(resultSet));
                }
            }
        } catch (SQLException e) {
            System.out.println("\nSearch Playlist Error: " + e.getMessage());
        }

        return playlists;
    }

    @Override
    public List<Song> findSongsByPlaylistId(int playlistId) {
        List<Song> songs = new ArrayList<>();
        String query = "SELECT s.id, s.title, s.genre, s.length, s.album_id, al.name AS album_name " +
                "FROM playlist_songs ps " +
                "JOIN songs s ON ps.song_id = s.id " +
                "JOIN albums al ON s.album_id = al.id " +
                "WHERE ps.playlist_id = ? AND s.is_archived = 0 " +
                "ORDER BY s.id ASC";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, playlistId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    String genre = resultSet.getString("genre");
                    String length = resultSet.getString("length");
                    int albumId = resultSet.getInt("album_id");
                    String albumName = resultSet.getString("album_name");

                    songs.add(new Song(id, title, genre, length, albumId, albumName));
                }
            }
        } catch (SQLException e) {
            System.out.println("\nRead Playlist Songs Error: " + e.getMessage());
        }

        return songs;
    }

    @Override
    public List<Playlist> findByUserId(int userId) {
        List<Playlist> playlists = new ArrayList<>();
        String query = "SELECT p.id, p.name, p.user_id, u.username, p.is_archived " +
                "FROM playlists p " +
                "JOIN users u ON p.user_id = u.id " +
                "WHERE p.is_archived = 0 AND p.user_id = ? " +
                "ORDER BY p.id ASC";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    playlists.add(mapPlaylist(resultSet));
                }
            }
        } catch (SQLException e) {
            System.out.println("\nRead User Playlists Error: " + e.getMessage());
        }

        return playlists;
    }

    @Override
    public boolean addSongToPlaylist(int playlistId, int songId) {
        String query = "INSERT INTO playlist_songs (playlist_id, song_id) VALUES (?, ?)";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, playlistId);
            preparedStatement.setInt(2, songId);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("\nAdd Song to Playlist Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean removeSongFromPlaylist(int playlistId, int songId) {
        String query = "DELETE FROM playlist_songs WHERE playlist_id = ? AND song_id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, playlistId);
            preparedStatement.setInt(2, songId);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("\nRemove Song from Playlist Error: " + e.getMessage());
            return false;
        }
    }

    private Playlist mapPlaylist(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        int userId = resultSet.getInt("user_id");
        String username = resultSet.getString("username");
        boolean archived = resultSet.getBoolean("is_archived");

        return new Playlist(id, name, userId, username, archived);
    }
}
