package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.model.Song;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongRepositoryImpl implements SongRepository {

    private final DbConnection dbConnection;

    public SongRepositoryImpl(DbConnection dbConnection){
        this.dbConnection = dbConnection;
    }

    @Override
    public List<Song> findAllActive() {
        List<Song> songs = new ArrayList<>();
        String query = "SELECT songs.id, songs.title, songs.genre, songs.length AS song_length, songs.album_id, albums.name AS album_name, songs.is_archived " +
                "FROM songs " +
                "JOIN albums on songs.album_id = albums.id " +
                "WHERE songs.is_archived = 0 ORDER BY songs.id ASC";

        try (Connection connection = dbConnection.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String genre = resultSet.getString("genre");
                String songLength = resultSet.getString("song_length");
                int albumId = resultSet.getInt("album_id");
                String albumName = resultSet.getString("album_name");
                boolean archived = resultSet.getBoolean("is_archived");
                songs.add(new Song(
                        id, title, genre, songLength, albumId, albumName, archived
                ));
            }
        } catch (SQLException e) {
            System.out.println("\nRead Songs Error: " + e.getMessage());
        }

        return songs;
    }

    @Override
    public List<Song> findAllArchived() {
        List<Song> songs = new ArrayList<>();
        String query = "SELECT songs.id, songs.title, songs.genre, songs.length AS song_length, songs.album_id, albums.name AS album_name, songs.is_archived " +
                "FROM songs " +
                "JOIN albums on songs.album_id = albums.id " +
                "WHERE songs.is_archived = 1 ORDER BY songs.id ASC";

        try (Connection connection = dbConnection.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String genre = resultSet.getString("genre");
                String songLength = resultSet.getString("song_length");
                int albumId = resultSet.getInt("album_id");
                String albumName = resultSet.getString("album_name");
                boolean archived = resultSet.getBoolean("is_archived");
                songs.add(new Song(
                        id, title, genre, songLength, albumId, albumName, archived
                ));
            }
        } catch (SQLException e) {
            System.out.println("\nRead Songs Error: " + e.getMessage());
        }

        return songs;
    }

    @Override
    public boolean save(Song song){
        String query = "INSERT INTO songs (title, genre, length, album_id) VALUES (?, ?, ?, ?)";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedState = connection.prepareStatement(query)){

            preparedState.setString(1, song.getTitle());
            preparedState.setString(2, song.getGenre());
            preparedState.setString(3, song.getLength());
            preparedState.setInt(4, song.getAlbumId());

            return preparedState.executeUpdate() > 0;
        }catch (SQLException e) {
            System.out.println("\n Create Song Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Song song){
        String query = "UPDATE songs SET title = ?, genre = ?, length = ?, album_id = ? WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setString(1, song.getTitle());
            preparedStatement.setString(2, song.getGenre());
            preparedStatement.setString(3, song.getLength());
            preparedStatement.setInt(4, song.getAlbumId());
            preparedStatement.setInt(5, song.getId());

            return preparedStatement.executeUpdate() > 0;

        }catch (SQLException e){
            System.out.println("\nUpdate Song Error: " + e.getMessage());
            return  false;
        }
    }

    @Override
    public boolean deleteById(int id) {
        String query = "DELETE FROM songs WHERE id = ?";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedState = connection.prepareStatement(query)){

            preparedState.setInt(1, id);

            return preparedState.executeUpdate() > 0;
        }catch (SQLException e){
            System.out.println("\nDelete Song Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean archiveById(int id){
        String query = "UPDATE songs SET is_archived = 1 WHERE id = ? AND is_archived = 0";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;
        }catch (SQLException e){
            System.out.println("\nArchive Song Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean restoreById(int id){
        String query = "UPDATE songs SET is_archived = 0 WHERE id = ? AND is_archived = 1";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;
        }catch (SQLException e){
            System.out.println("\nRestore Song Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Song> searchByTitle(String keyword) {
        List<Song> songs = new ArrayList<>();
        String query = "SELECT songs.id, songs.title, songs.genre, songs.length AS song_length, songs.album_id, albums.name AS album_name, songs.is_archived " +
                "FROM songs " +
                "JOIN albums on songs.album_id = albums.id " +
                "WHERE songs.is_archived = 0 AND songs.title LIKE ? ORDER BY songs.id ASC";

        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + keyword + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    String genre = resultSet.getString("genre");
                    String songLength = resultSet.getString("song_length");
                    int albumId = resultSet.getInt("album_id");
                    String albumName = resultSet.getString("album_name");
                    boolean archived = resultSet.getBoolean("is_archived");

                    songs.add(new Song(id, title, genre, songLength, albumId, albumName, archived));
                }
            }
        } catch (SQLException e) {
            System.out.println("\n Search Song Error: " + e.getMessage());
        }
        return songs;
    }
}
