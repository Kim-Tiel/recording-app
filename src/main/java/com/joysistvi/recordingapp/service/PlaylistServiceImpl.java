package com.joysistvi.recordingapp.service;

import com.joysistvi.recordingapp.model.Playlist;
import com.joysistvi.recordingapp.model.Song;
import com.joysistvi.recordingapp.repository.PlaylistRepository;

import java.util.ArrayList;
import java.util.List;

public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository playlistRepository;

    public PlaylistServiceImpl(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    @Override
    public List<Playlist> getAllActivePlaylists() {
        return playlistRepository.findAllActive();
    }

    @Override
    public List<Playlist> getAllArchivedPlaylists() {
        return playlistRepository.findAllArchived();
    }

    @Override
    public boolean createPlaylist(String name, int userId) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("\nValidation Error: Playlist name is required.");
            return false;
        }

        if (userId <= 0) {
            System.out.println("\nValidation Error: User ID must be greater than 0.");
            return false;
        }

        if (!playlistRepository.findByUserId(userId).isEmpty()) {
            System.out.println("\nValidation Error: User already has a playlist.");
            return false;
        }

        Playlist playlist = new Playlist(name.trim(), userId);
        return playlistRepository.save(playlist);
    }

    @Override
    public boolean updatePlaylist(int id, String name, int userId) {
        if (id <= 0) {
            System.out.println("\nValidation Error: Playlist ID must be greater than 0.");
            return false;
        }

        if (name == null || name.trim().isEmpty()) {
            System.out.println("\nValidation Error: Playlist name is required.");
            return false;
        }

        if (userId <= 0) {
            System.out.println("\nValidation Error: User ID must be greater than 0.");
            return false;
        }

        Playlist playlist = new Playlist(id, name.trim(), userId);
        return playlistRepository.update(playlist);
    }

    @Override
    public boolean deletePlaylist(int id) {
        if (id <= 0) {
            System.out.println("\nValidation Error: Playlist ID must be greater than 0.");
            return false;
        }

        return playlistRepository.deleteById(id);
    }

    @Override
    public boolean archivePlaylist(int id) {
        if (id <= 0) {
            System.out.println("\nValidation Error: Playlist ID must be greater than 0.");
            return false;
        }

        return playlistRepository.archiveById(id);
    }

    @Override
    public boolean restorePlaylist(int id) {
        if (id <= 0) {
            System.out.println("\nValidation Error: Playlist ID must be greater than 0.");
            return false;
        }

        return playlistRepository.restoreById(id);
    }

    @Override
    public List<Playlist> searchPlaylists(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("\nValidation Error: Search keyword is required.");
            return new ArrayList<>();
        }

        return playlistRepository.searchByName(keyword.trim());
    }

    @Override
    public List<Song> getSongsInPlaylist(int playlistId) {
        if (playlistId <= 0) {
            System.out.println("\nValidation Error: Playlist ID must be greater than 0.");
            return new ArrayList<>();
        }

        return playlistRepository.findSongsByPlaylistId(playlistId);
    }

    @Override
    public List<Playlist> getPlaylistsByUser(int userId) {
        if (userId <= 0) {
            System.out.println("\nValidation Error: User ID must be greater than 0.");
            return new ArrayList<>();
        }

        return playlistRepository.findByUserId(userId);
    }

    @Override
    public boolean addSongToPlaylist(int playlistId, int songId) {
        if (playlistId <= 0) {
            System.out.println("\nValidation Error: Playlist ID must be greater than 0.");
            return false;
        }

        if (songId <= 0) {
            System.out.println("\nValidation Error: Song ID must be greater than 0.");
            return false;
        }

        return playlistRepository.addSongToPlaylist(playlistId, songId);
    }

    @Override
    public boolean removeSongFromPlaylist(int playlistId, int songId) {
        if (playlistId <= 0) {
            System.out.println("\nValidation Error: Playlist ID must be greater than 0.");
            return false;
        }

        if (songId <= 0) {
            System.out.println("\nValidation Error: Song ID must be greater than 0.");
            return false;
        }

        return playlistRepository.removeSongFromPlaylist(playlistId, songId);
    }
}
