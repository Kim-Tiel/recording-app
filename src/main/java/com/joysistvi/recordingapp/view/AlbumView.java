package com.joysistvi.recordingapp.view;

import com.joysistvi.recordingapp.model.Album;
import com.joysistvi.recordingapp.service.AlbumService;

import java.util.List;

// View(Presentation Tier) -> Console menus, Scanner inputs, and formatted output
public class AlbumView {

    private final AlbumService albumService;

    public AlbumView(AlbumService albumService) {
        this.albumService = albumService;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n===== ALBUMS =====");
            System.out.println("1. View All Albums");
            System.out.println("2. Create Album");
            System.out.println("3. Update Album");
            System.out.println("4. Delete Album");
            System.out.println("5. Archive Album");
            System.out.println("6. Restore Album");
            System.out.println("7. Search Albums");
            System.out.println("0. Back");
            String choice = ConsoleInput.readString("Choose: ").trim();

            switch (choice) {
                case "1":
                    displayAllAlbums();
                    break;
                case "2":
                    promptCreateAlbum();
                    break;
                case "3":
                    promptUpdateAlbum();
                    break;
                case "4":
                    promptDeleteAlbum();
                    break;
                case "5":
                    promptArchiveAlbum();
                    break;
                case "6":
                    promptRestoreAlbum();
                    break;
                case "7":
                    promptSearchAlbums();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("\nInvalid choice.");
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
        int year = ConsoleInput.readInt("Enter year: ");
        int artistId = ConsoleInput.readInt("Enter artist ID: ");

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
        int id = ConsoleInput.readInt("Enter album ID to update: ");
        String name = ConsoleInput.readString("Enter new album name: ");
        int year = ConsoleInput.readInt("Enter new year: ");
        int artistId = ConsoleInput.readInt("Enter new artist ID: ");

        boolean updated = albumService.updateAlbum(id, name, year, artistId);

        if (updated) {
            System.out.println("\nAlbum ID " + id + " updated successfully!");
            displayAllAlbums();
        } else {
            System.out.println("Failed to update album.");
        }
    }

    public void promptDeleteAlbum() {
        System.out.println("\n----- Delete Album -----");
        int id = ConsoleInput.readInt("Enter album ID to delete: ");

        boolean deleted = albumService.deleteAlbum(id);

        if (deleted) {
            System.out.println("\nAlbum ID " + id + " deleted successfully!");
            displayAllAlbums();
        } else {
            System.out.println("Failed to delete album.");
        }
    }

    public void promptArchiveAlbum() {
        System.out.println("\n----- Archive Album -----");
        int id = ConsoleInput.readInt("Enter album ID to archive: ");

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

        // display archived albums first
        List<Album> archivedAlbums = albumService.getAllArchivedAlbums();

        if (archivedAlbums.isEmpty()) {
            System.out.println("No archived albums found.");
            return;
        }

        System.out.println("\n----- Archived Albums -----");
        printAlbums(archivedAlbums);

        int id = ConsoleInput.readInt("Enter album ID to restore: ");

        boolean restored = albumService.restoreAlbum(id);

        if (restored) {
            System.out.println("\nAlbum ID " + id + " restored successfully!");
            displayAllAlbums();
        } else {
            System.out.println("Failed to restore album.");
        }
    }

    public void promptSearchAlbums() {
        System.out.println("\n----- Search Albums -----");
        String keyword = ConsoleInput.readString("Enter keyword: ");

        if (keyword == null || keyword.trim().isEmpty()) {
            albumService.searchAlbums(keyword); // shows the validation error
            return;
        }

        List<Album> albums = albumService.searchAlbums(keyword);

        System.out.println("\n----- Search Albums (Keyword: '" + keyword.trim() + "') -----");
        printAlbums(albums);
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
}