package com.joysistvi.recordingapp.view;

import com.joysistvi.recordingapp.model.Song;
import com.joysistvi.recordingapp.service.SongService;

import java.util.List;

public class SongView {

    private final SongService songService;

    public SongView(SongService songService) {
        this.songService = songService;
    }

    public void handleMenu() {
        while (true) {
            System.out.println("\n=== SONG MANAGEMENT ===");
            System.out.println("1. View All Active Songs");
            System.out.println("2. Create Song");
            System.out.println("3. Update Song");
            System.out.println("4. Search Songs");
            System.out.println("5. Archive Song (Soft Delete)");
            System.out.println("6. Restore Archived Song");
            System.out.println("7. Delete Song (Permanent)");
            System.out.println("0. Back");

            int choice = promptForInt("Choose an option: ");
            switch (choice) {
                case 1 -> displayAllSongs();
                case 2 -> promptCreateSong();
                case 3 -> promptUpdateSong();
                case 4 -> promptSearchSongs();
                case 5 -> promptArchiveSong();
                case 6 -> promptRestoreSong();
                case 7 -> promptDeleteSong();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public void displayAllSongs() {
        List<Song> songs = songService.getAllActiveSongs();

        System.out.println("\n----- Songs -----");
        printSongs(songs);
    }

    public void promptCreateSong() {
        System.out.println("\n----- Create New Song -----");
        String title = ConsoleInput.readString("Enter song title: ");
        String genre = ConsoleInput.readString("Enter song genre: ");
        String songLength = ConsoleInput.readString("Enter song length (e.g. 3:25): ");
        int albumId = promptForInt("Enter album ID: ");

        if (songService.createSong(title, genre, songLength, albumId)) {
            System.out.println("\nSong " + title.trim() + " created successfully!");
            displayAllSongs();
        } else {
            System.out.println("Failed to create song.");
        }
    }

    public void promptUpdateSong() {
        System.out.println("\n----- Update Song -----");
        int id = promptForInt("Enter song ID to update: ");
        String title = ConsoleInput.readString("Enter new title: ");
        String genre = ConsoleInput.readString("Enter new genre: ");
        String songLength = ConsoleInput.readString("Enter new length (e.g. 3:25): ");
        int albumId = promptForInt("Enter new album ID: ");

        if (songService.updateSong(id, title, genre, songLength, albumId)) {
            System.out.println("\nSong ID " + id + " updated successfully!");
            displayAllSongs();
        } else {
            System.out.println("Failed to update song.");
        }
    }

    public void promptSearchSongs() {
        System.out.println("\n----- Search Songs -----");
        String keyword = ConsoleInput.readString("Enter song title keyword: ");

        if (keyword == null || keyword.trim().isEmpty()) {
            songService.searchSongs(keyword);
            return;
        }

        List<Song> songs = songService.searchSongs(keyword);

        System.out.println("\n----- Search Songs (Keyword: '" + keyword.trim() + "') -----");
        printSongs(songs);
    }

    public void promptArchiveSong() {
        System.out.println("\n----- Archive Song -----");
        int id = promptForInt("Enter song ID to archive: ");

        if (songService.archiveSong(id)) {
            System.out.println("\nSong ID " + id + " archived successfully!");
            displayAllSongs();
        } else {
            System.out.println("Failed to archive song.");
        }
    }

    public void promptRestoreSong() {
        System.out.println("\n----- Restore Song -----");

        List<Song> archivedSongs = songService.getAllArchivedSongs();

        if (archivedSongs.isEmpty()) {
            System.out.println("No archived songs found.");
            return;
        }

        System.out.println("\n----- Archived Songs -----");
        printSongs(archivedSongs);

        int id = promptForInt("Enter song ID to restore: ");

        if (songService.restoreSong(id)) {
            System.out.println("\nSong ID " + id + " restored successfully!");
            displayAllSongs();
        } else {
            System.out.println("Failed to restore song.");
        }
    }

    public void promptDeleteSong() {
        System.out.println("\n----- Delete Song -----");
        int id = promptForInt("Enter song ID to delete: ");

        if (songService.deleteSong(id)) {
            System.out.println("\nSong ID " + id + " deleted successfully!");
            displayAllSongs();
        } else {
            System.out.println("Failed to delete song.");
        }
    }

    private void printSongs(List<Song> songs) {
        if (songs == null || songs.isEmpty()) {
            System.out.println("No songs found.");
            return;
        }

        System.out.printf("%-5s %-30s %-15s %-8s %-25s%n", "ID", "TITLE", "GENRE", "LENGTH", "ALBUM");

        for (Song song : songs) {
            System.out.printf("%-5d %-30s %-15s %-8s %-25s%n",
                    song.getId(),
                    song.getTitle(),
                    song.getGenre(),
                    song.getLength(),
                    song.getAlbumName());
        }
    }

    private int promptForInt(String prompt) {
        return ConsoleInput.readInt(prompt);
    }
}
