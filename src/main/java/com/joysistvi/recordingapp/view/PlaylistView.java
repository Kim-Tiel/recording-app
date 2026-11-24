package com.joysistvi.recordingapp.view;

import com.joysistvi.recordingapp.model.Playlist;
import com.joysistvi.recordingapp.model.Song;
import com.joysistvi.recordingapp.service.PlaylistService;
import com.joysistvi.recordingapp.service.SongService;

import java.util.List;

public class PlaylistView {

    private final PlaylistService playlistService;
    private final SongService songService;
    private int currentUserId;

    public PlaylistView(PlaylistService playlistService, SongService songService) {
        this.playlistService = playlistService;
        this.songService = songService;
    }

    public void handleMenu(int userId) {
        this.currentUserId = userId;

        while (true) {
            ConsoleInput.clearScreen();
            System.out.println("\n=== PLAYLIST MANAGEMENT ===");
            System.out.println("1. View All Active Playlists");
            System.out.println("2. Create My Playlist");
            System.out.println("3. Update Playlist");
            System.out.println("4. Search Playlists");
            System.out.println("5. Archive Playlist (Soft Delete)");
            System.out.println("6. Restore Archived Playlist");
            System.out.println("7. Delete Playlist (Permanent)");
            System.out.println("8. View My Playlist (with Songs)");
            System.out.println("9. Manage Songs in My Playlist");
            System.out.println("0. Back");

            int choice = promptForInt("Choose an option: ");
            switch (choice) {
                case 1 -> displayAllPlaylists();
                case 2 -> promptCreatePlaylist(currentUserId);
                case 3 -> promptUpdatePlaylist();
                case 4 -> promptSearchPlaylists();
                case 5 -> promptArchivePlaylist();
                case 6 -> promptRestorePlaylist();
                case 7 -> promptDeletePlaylist();
                case 8 -> handleViewUserPlaylists(currentUserId);
                case 9 -> handleManageSongsInPlaylist(currentUserId);
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }

            ConsoleInput.pause();
        }
    }

    public void handleUserMenu(int userId) {
        this.currentUserId = userId;

        while (true) {
            ConsoleInput.clearScreen();
            System.out.println("\n=== MY PLAYLIST ===");
            System.out.println("1. View My Playlist");
            System.out.println("2. Create My Playlist");
            System.out.println("3. Delete My Playlist");
            System.out.println("4. Add / Remove Songs in My Playlist");
            System.out.println("0. Back");

            int choice = promptForInt("Choose an option: ");
            switch (choice) {
                case 1 -> handleViewUserPlaylists(currentUserId);
                case 2 -> promptCreatePlaylist(currentUserId);
                case 3 -> promptDeleteUserPlaylist(currentUserId);
                case 4 -> handleManageSongsInPlaylist(currentUserId);
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }

            ConsoleInput.pause();
        }
    }

    public void promptDeleteUserPlaylist(int userId) {
        System.out.println("\n----- Delete My Playlist -----");
        List<Playlist> userPlaylists = playlistService.getPlaylistsByUser(userId);

        if (userPlaylists.isEmpty()) {
            System.out.println("No playlist found to delete.");
            return;
        }

        Playlist playlist = userPlaylists.get(0);

        if (playlistService.deletePlaylist(playlist.getId())) {
            System.out.println("\nPlaylist " + playlist.getName() + " deleted successfully!");
        } else {
            System.out.println("Failed to delete playlist.");
        }
    }

    public void displayAllPlaylists() {
        List<Playlist> playlists = playlistService.getAllActivePlaylists();

        System.out.println("\n----- Playlists -----");
        printPlaylists(playlists);
    }

    public void promptCreatePlaylist(int userId) {
        System.out.println("\n----- Create My Playlist -----");
        String name = ConsoleInput.readString("Enter playlist name: ");

        boolean created = playlistService.createPlaylist(name, userId);

        if (created) {
            System.out.println("\nPlaylist " + name.trim() + " created successfully!");
            displayAllPlaylists();
        } else {
            System.out.println("Failed to create playlist.");
        }
    }

    public void promptUpdatePlaylist() {
        displayAllPlaylists();
        System.out.println("\n----- Update Playlist -----");
        int id = promptForInt("Enter playlist ID to update: ");
        String name = ConsoleInput.readString("Enter new playlist name: ");
        int userId = promptForInt("Enter new user ID (owner): ");

        boolean updated = playlistService.updatePlaylist(id, name, userId);

        if (updated) {
            System.out.println("\nPlaylist ID " + id + " updated successfully!");
            displayAllPlaylists();
        } else {
            System.out.println("Failed to update playlist.");
        }
    }

    public void promptSearchPlaylists() {
        System.out.println("\n----- Search Playlists -----");
        String keyword = ConsoleInput.readString("Enter keyword: ");

        if (keyword == null || keyword.trim().isEmpty()) {
            playlistService.searchPlaylists(keyword);
            return;
        }

        List<Playlist> playlists = playlistService.searchPlaylists(keyword);

        System.out.println("\n----- Search Playlists (Keyword: '" + keyword.trim() + "') -----");
        printPlaylists(playlists);
    }

    public void promptArchivePlaylist() {
        displayAllPlaylists();
        System.out.println("\n----- Archive Playlist -----");
        int id = promptForInt("Enter playlist ID to archive: ");

        boolean archived = playlistService.archivePlaylist(id);

        if (archived) {
            System.out.println("\nPlaylist ID " + id + " archived successfully!");
            displayAllPlaylists();
        } else {
            System.out.println("Failed to archive playlist.");
        }
    }

    public void promptRestorePlaylist() {
        System.out.println("\n----- Restore Playlist -----");

        List<Playlist> archivedPlaylists = playlistService.getAllArchivedPlaylists();

        if (archivedPlaylists.isEmpty()) {
            System.out.println("No archived playlists found.");
            return;
        }

        System.out.println("\n----- Archived Playlists -----");
        printPlaylists(archivedPlaylists);

        int id = promptForInt("Enter playlist ID to restore: ");

        boolean restored = playlistService.restorePlaylist(id);

        if (restored) {
            System.out.println("\nPlaylist ID " + id + " restored successfully!");
            displayAllPlaylists();
        } else {
            System.out.println("Failed to restore playlist.");
        }
    }

    public void promptDeletePlaylist() {
        displayAllPlaylists();
        System.out.println("\n----- Delete Playlist -----");
        int id = promptForInt("Enter playlist ID to delete: ");

        boolean deleted = playlistService.deletePlaylist(id);

        if (deleted) {
            System.out.println("\nPlaylist ID " + id + " deleted successfully!");
            displayAllPlaylists();
        } else {
            System.out.println("Failed to delete playlist.");
        }
    }

    public void handleViewUserPlaylists(int userId) {
        List<Playlist> userPlaylists = playlistService.getPlaylistsByUser(userId);

        if (userPlaylists.isEmpty()) {
            System.out.println("\nNo playlist found. Please create a playlist first.");
            return;
        }

        for (Playlist playlist : userPlaylists) {
            System.out.println("\nPlaylist: " + playlist.getName());
            List<Song> songs = playlistService.getSongsInPlaylist(playlist.getId());
            printSongs("----- Current Songs in Playlist -----", songs);
        }
    }

    public void handleManageSongsInPlaylist(int userId) {
        List<Playlist> userPlaylists = playlistService.getPlaylistsByUser(userId);

        if (userPlaylists.isEmpty()) {
            System.out.println("\nNo playlist found. Please create a playlist first.");
            return;
        }

        Playlist playlist = userPlaylists.get(0);

        while (true) {
            ConsoleInput.clearScreen();
            System.out.println("\n=== MANAGE SONGS (" + playlist.getName() + ") ===");
            System.out.println("1. Add Song to Playlist");
            System.out.println("2. Remove Song from Playlist");
            System.out.println("0. Back");

            int choice = promptForInt("Choose an option: ");
            switch (choice) {
                case 1 -> promptAddSong(playlist);
                case 2 -> promptRemoveSong(playlist);
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }

            ConsoleInput.pause();
        }
    }

    private void promptAddSong(Playlist playlist) {
        printSongs("----- Available Songs -----", songService.getAllActiveSongs());
        int songId = promptForInt("Enter song ID to add: ");

        if (playlistService.addSongToPlaylist(playlist.getId(), songId)) {
            System.out.println("\nSong added to playlist.");
            printSongs("----- Current Songs in Playlist -----", playlistService.getSongsInPlaylist(playlist.getId()));
        } else {
            System.out.println("Failed to add song to playlist.");
        }
    }

    private void promptRemoveSong(Playlist playlist) {
        List<Song> songs = playlistService.getSongsInPlaylist(playlist.getId());

        if (songs.isEmpty()) {
            System.out.println("\nNo songs in this playlist yet.");
            return;
        }

        printSongs("----- Current Songs in Playlist -----", songs);
        int songId = promptForInt("Enter song ID to remove: ");

        if (playlistService.removeSongFromPlaylist(playlist.getId(), songId)) {
            System.out.println("\nSong removed from playlist.");
        } else {
            System.out.println("Failed to remove song from playlist.");
        }
    }

    private void printPlaylists(List<Playlist> playlists) {
        if (playlists == null || playlists.isEmpty()) {
            System.out.println("No playlists found.");
            return;
        }

        String border = ConsoleInput.border(5, 25, 20);

        System.out.println(border);
        System.out.printf("| %-5s | %-25s | %-20s |%n", "ID", "Playlist Name", "Owner");
        System.out.println(border);

        for (Playlist playlist : playlists) {
            System.out.printf("| %-5d | %-25s | %-20s |%n",
                    playlist.getId(),
                    ConsoleInput.truncate(playlist.getName(), 25),
                    ConsoleInput.truncate(playlist.getUsername(), 20));
        }

        System.out.println(border);
    }

    private void printSongs(String header, List<Song> songs) {
        System.out.println("\n" + header);

        if (songs == null || songs.isEmpty()) {
            System.out.println("No songs found.");
            return;
        }

        String border = ConsoleInput.border(5, 25, 15, 8, 22);

        System.out.println(border);
        System.out.printf("| %-5s | %-25s | %-15s | %-8s | %-22s |%n", "ID", "Title", "Genre", "Length", "Album");
        System.out.println(border);

        for (Song song : songs) {
            System.out.printf("| %-5d | %-25s | %-15s | %-8s | %-22s |%n",
                    song.getId(),
                    ConsoleInput.truncate(song.getTitle(), 25),
                    ConsoleInput.truncate(song.getGenre(), 15),
                    ConsoleInput.truncate(song.getLength(), 8),
                    ConsoleInput.truncate(song.getAlbumName(), 22));
        }

        System.out.println(border);
    }

    private int promptForInt(String prompt) {
        return ConsoleInput.readInt(prompt);
    }
}
