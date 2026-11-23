package com.joysistvi.recordingapp.view;

import com.joysistvi.recordingapp.model.Song;
import com.joysistvi.recordingapp.service.SongService;

import java.util.List;

public class SongView {

    private final SongService songService;

    public SongView(SongService songService){
        this.songService = songService;
    }

    public void handleMenu() {
        while (true) {
            System.out.println("\n===== SONGS =====");
            System.out.println("1. View All Songs");
            System.out.println("2. Create Song");
            System.out.println("3. Update Song");
            System.out.println("4. Delete Song");
            System.out.println("5. Archive Song");
            System.out.println("6. Restore Song");
            System.out.println("7. Search Songs");
            System.out.println("0. Back");
            String choice = ConsoleInput.readString("Choose: ").trim();

            switch (choice) {
                case "1":
                    displayAllSongs();
                    break;
                case "2":
                    promptCreateSong();
                    break;
                case "3":
                    promptUpdateSong();
                    break;
                case "4":
                    promptDeleteSong();
                    break;
                case "5":
                    promptArchiveSong();
                    break;
                case "6":
                    promptRestoreSong();
                    break;
                case "7":
                    promptSearchSongs();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("\nInvalid choice.");
            }
        }
    }

    public void displayAllSongs(){
        List<Song> songs = songService.getAllActiveSongs();
        System.out.println("\n----- Songs ----");
        printSongs(songs);
    }

    public void promptCreateSong(){
        System.out.println("\n----- Create New Song -----");
        String title = ConsoleInput.readString("Enter song title: ");
        String genre = ConsoleInput.readString("Enter song genre: ");
        String songLength = ConsoleInput.readString("Enter song length (e.g 3:25): ");
        int albumId = promptForInt("Enter album ID: ");

        if (songService.createSong(title, genre, songLength, albumId)) {
            System.out.println("\nSong created successfully.");
        } else {
            System.out.println("\nFailed to create song.");
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
            System.out.println("\nSong updated successfully.");
        } else {
            System.out.println("\nFailed to update song.");
        }
    }

    public void promptSearchSongs() {
        System.out.println("\n----- Search Song -----");
        String keyword = ConsoleInput.readString("Enter song title keyword: ");

        List<Song> songs = songService.searchSongs(keyword);

        if (songs.isEmpty()) {
            System.out.println("\nNo songs found.");
            return;
        }

        System.out.println("\n---- SEARCH RESULTS ----");
        printSongs(songs);
    }

    public void promptArchiveSong() {
        System.out.println("\n----- Archive Song -----");
        int id = promptForInt("Enter song ID to archive: ");

        if (songService.archiveSong(id)) {
            System.out.println("\nSong archived successfully.");
        } else {
            System.out.println("\nFailed to archive song.");
        }
    }

    public void promptRestoreSong() {
        List<Song> archivedSongs = songService.getAllArchivedSongs();

        if (archivedSongs.isEmpty()) {
            System.out.println("\nNo archived songs to restore.");
            return;
        }

        System.out.println("\n----- Restore Song -----");
        printSongs(archivedSongs);

        int id = promptForInt("Enter song ID to restore: ");

        if (songService.restoreSong(id)) {
            System.out.println("\nSong restored successfully.");
        } else {
            System.out.println("\nFailed to restore song.");
        }
    }

    public void promptDeleteSong() {
        System.out.println("\n----- Delete Song -----");
        int id = promptForInt("Enter song ID to permanently delete: ");

        if (songService.deleteSong(id)) {
            System.out.println("\nSong deleted permanently.");
        } else {
            System.out.println("\nFailed to delete song.");
        }
    }

    private void printSongs(List<Song> songs) {
        System.out.printf("%-5s %-30s %-15s %-8s %-25s%n", "ID", "TITLE", "GENRE", "LENGTH", "ALBUM");
        System.out.println("-------------------------------------------------------------------------------------");
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
