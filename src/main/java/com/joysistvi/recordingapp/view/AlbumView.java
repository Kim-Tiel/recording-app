package com.joysistvi.recordingapp.view;

import com.joysistvi.recordingapp.model.Album;
import com.joysistvi.recordingapp.service.AlbumService;

import java.util.List;

public class AlbumView {

    private final AlbumService albumService;

    public AlbumView(AlbumService albumService) {
        this.albumService = albumService;
    }

    public void handleMenu() {
        while (true) {
            System.out.println("\n=== ALBUM MANAGEMENT ===");
            System.out.println("1. View All Active Albums");
            System.out.println("2. Create Album");
            System.out.println("3. Update Album");
            System.out.println("4. Search Albums");
            System.out.println("5. Archive Album (Soft Delete)");
            System.out.println("6. Restore Archived Album");
            System.out.println("7. Delete Album (Permanent)");
            System.out.println("0. Back");

            int choice = promptForInt("Choose an option: ");
            switch (choice) {
                case 1 -> displayAllAlbums();
                case 2 -> promptCreateAlbum();
                case 3 -> promptUpdateAlbum();
                case 4 -> promptSearchAlbums();
                case 5 -> promptArchiveAlbum();
                case 6 -> promptRestoreAlbum();
                case 7 -> promptDeleteAlbum();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public void displayAllAlbums() {
        List<Album> albums = albumService.getAllActiveAlbums();

        System.out.println("\n----- Albums -----");
        printAlbums(albums);
    }

    public void promptCreateAlbum() {
        System.out.println("\n----- Create New Album -----");
        String name = ConsoleInput.readString("Enter album name: ");
        int year = promptForInt("Enter year: ");
        int artistId = promptForInt("Enter artist ID: ");

        boolean created = albumService.createAlbum(name, year, artistId);

        if (created) {
            System.out.println("\nAlbum " + name.trim() + " created successfully!");
            displayAllAlbums();
        } else {
            System.out.println("Failed to create album.");
        }
    }

    public void promptUpdateAlbum() {
        System.out.println("\n----- Update Album -----");
        int id = promptForInt("Enter album ID to update: ");
        String name = ConsoleInput.readString("Enter new album name: ");
        int year = promptForInt("Enter new year: ");
        int artistId = promptForInt("Enter new artist ID: ");

        boolean updated = albumService.updateAlbum(id, name, year, artistId);

        if (updated) {
            System.out.println("\nAlbum ID " + id + " updated successfully!");
            displayAllAlbums();
        } else {
            System.out.println("Failed to update album.");
        }
    }

    public void promptSearchAlbums() {
        System.out.println("\n----- Search Albums -----");
        String keyword = ConsoleInput.readString("Enter keyword: ");

        if (keyword == null || keyword.trim().isEmpty()) {
            albumService.searchAlbums(keyword);
            return;
        }

        List<Album> albums = albumService.searchAlbums(keyword);

        System.out.println("\n----- Search Albums (Keyword: '" + keyword.trim() + "') -----");
        printAlbums(albums);
    }

    public void promptArchiveAlbum() {
        System.out.println("\n----- Archive Album -----");
        int id = promptForInt("Enter album ID to archive: ");

        boolean archived = albumService.archiveAlbum(id);

        if (archived) {
            System.out.println("\nAlbum ID " + id + " archived successfully!");
            displayAllAlbums();
        } else {
            System.out.println("Failed to archive album.");
        }
    }

    public void promptRestoreAlbum() {
        System.out.println("\n----- Restore Album -----");

        List<Album> archivedAlbums = albumService.getAllArchivedAlbums();

        if (archivedAlbums.isEmpty()) {
            System.out.println("No archived albums found.");
            return;
        }

        System.out.println("\n----- Archived Albums -----");
        printAlbums(archivedAlbums);

        int id = promptForInt("Enter album ID to restore: ");

        boolean restored = albumService.restoreAlbum(id);

        if (restored) {
            System.out.println("\nAlbum ID " + id + " restored successfully!");
            displayAllAlbums();
        } else {
            System.out.println("Failed to restore album.");
        }
    }

    public void promptDeleteAlbum() {
        System.out.println("\n----- Delete Album -----");
        int id = promptForInt("Enter album ID to delete: ");

        boolean deleted = albumService.deleteAlbum(id);

        if (deleted) {
            System.out.println("\nAlbum ID " + id + " deleted successfully!");
            displayAllAlbums();
        } else {
            System.out.println("Failed to delete album.");
        }
    }

    private void printAlbums(List<Album> albums) {
        if (albums == null || albums.isEmpty()) {
            System.out.println("No albums found.");
            return;
        }

        System.out.printf("%-4s %-22s %-8s %-20s%n", "ID", "Name", "Year", "Artist");

        for (Album album : albums) {
            System.out.printf("%-4d %-22s %-8d %-20s%n",
                    album.getId(),
                    album.getName(),
                    album.getYear(),
                    album.getArtistName());
        }
    }

    private int promptForInt(String prompt) {
        return ConsoleInput.readInt(prompt);
    }
}
