package com.joysistvi.recordingapp.view;

import com.joysistvi.recordingapp.model.Playlist;
import com.joysistvi.recordingapp.service.PlaylistService;

import java.util.List;

public class PlaylistView {

    private final PlaylistService playlistService;

    public PlaylistView(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    public void handleMenu() {
        while (true) {
            System.out.println("\n=== PLAYLIST MANAGEMENT ===");
            System.out.println("1. View All Active Playlists");
            System.out.println("2. Create Playlist");
            System.out.println("3. Update Playlist");
            System.out.println("4. Search Playlists");
            System.out.println("5. Archive Playlist (Soft Delete)");
            System.out.println("6. Restore Archived Playlist");
            System.out.println("7. Delete Playlist (Permanent)");
            System.out.println("0. Back");

            int choice = promptForInt("Choose an option: ");
            switch (choice) {
                case 1 -> displayAllPlaylists();
                case 2 -> promptCreatePlaylist();
                case 3 -> promptUpdatePlaylist();
                case 4 -> promptSearchPlaylists();
                case 5 -> promptArchivePlaylist();
                case 6 -> promptRestorePlaylist();
                case 7 -> promptDeletePlaylist();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public void displayAllPlaylists() {
        List<Playlist> playlists = playlistService.getAllActivePlaylists();

        System.out.println("\n----- Playlists -----");
        printPlaylists(playlists);
    }

    public void promptCreatePlaylist() {
        System.out.println("\n----- Create New Playlist -----");
        String name = ConsoleInput.readString("Enter playlist name: ");
        int userId = promptForInt("Enter user ID (owner): ");

        boolean created = playlistService.createPlaylist(name, userId);

        if (created) {
            System.out.println("\nPlaylist " + name.trim() + " created successfully!");
            displayAllPlaylists();
        } else {
            System.out.println("Failed to create playlist.");
        }
    }

    public void promptUpdatePlaylist() {
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
            playlistService.searchPlaylists(keyword); // shows the validation error
            return;
        }

        List<Playlist> playlists = playlistService.searchPlaylists(keyword);

        System.out.println("\n----- Search Playlists (Keyword: '" + keyword.trim() + "') -----");
        printPlaylists(playlists);
    }

    public void promptArchivePlaylist() {
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

    private void printPlaylists(List<Playlist> playlists) {
        if (playlists == null || playlists.isEmpty()) {
            System.out.println("No playlists found.");
            return;
        }

        System.out.printf("%-4s %-25s %-20s%n", "ID", "Playlist Name", "Owner");

        for (Playlist playlist : playlists) {
            System.out.printf("%-4d %-25s %-20s%n",
                    playlist.getId(),
                    playlist.getName(),
                    playlist.getUsername());
        }
    }

    private int promptForInt(String prompt) {
        return ConsoleInput.readInt(prompt);
    }
}