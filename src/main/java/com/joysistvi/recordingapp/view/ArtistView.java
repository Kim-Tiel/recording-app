package com.joysistvi.recordingapp.view;

import com.joysistvi.recordingapp.model.Artist;
import com.joysistvi.recordingapp.service.ArtistService;

import java.util.List;

public class ArtistView {

    private final ArtistService artistService;

    public ArtistView(ArtistService artistService) {
        this.artistService = artistService;
    }

    public void handleMenu() {
        while (true) {
            ConsoleInput.clearScreen();
            System.out.println("\n=== ARTIST MANAGEMENT ===");
            System.out.println("1. View All Active Artists");
            System.out.println("2. Create Artist");
            System.out.println("3. Update Artist");
            System.out.println("4. Search Artists");
            System.out.println("5. Archive Artist (Soft Delete)");
            System.out.println("6. Restore Archived Artist");
            System.out.println("7. Delete Artist (Permanent)");
            System.out.println("0. Back");

            int choice = promptForInt("Choose an option: ");
            switch (choice) {
                case 1 -> displayAllArtists();
                case 2 -> promptCreateArtist();
                case 3 -> promptUpdateArtist();
                case 4 -> promptSearchArtists();
                case 5 -> promptArchiveArtist();
                case 6 -> promptRestoreArtist();
                case 7 -> promptDeleteArtist();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }

            ConsoleInput.pause();
        }
    }

    public void displayAllArtists() {
        List<Artist> artists = artistService.getAllActiveArtists();

        System.out.println("\n----- Artists -----");
        printArtists(artists);
    }

    public void promptCreateArtist() {
        System.out.println("\n----- Create New Artist -----");
        String name = ConsoleInput.readString("Enter artist name: ");

        boolean created = artistService.createArtist(name);

        if (created) {
            System.out.println("\nArtist " + name.trim() + " created successfully!");
            displayAllArtists();
        } else {
            System.out.println("Failed to create artist.");
        }
    }

    public void promptUpdateArtist() {
        System.out.println("\n----- Update Artist -----");
        displayAllArtists();
        int id = promptForInt("Enter artist ID to update: ");

        Artist current = artistService.getArtistById(id);

        if (current == null) {
            System.out.println("Artist not found or invalid ID.");
            return;
        }

        String name = ConsoleInput.readString("Enter new artist name (press Enter to keep current [" + current.getName() + "]): ");

        if (name.trim().isEmpty()) {
            name = current.getName();
        } else {
            name = name.trim();
        }

        boolean updated = artistService.updateArtist(id, name);

        if (updated) {
            System.out.println("\nArtist ID " + id + " updated successfully!");
            displayAllArtists();
        } else {
            System.out.println("Failed to update artist.");
        }
    }

    public void promptSearchArtists() {
        System.out.println("\n----- Search Artists -----");
        String keyword = ConsoleInput.readString("Enter keyword: ");

        if (keyword == null || keyword.trim().isEmpty()) {
            artistService.searchArtists(keyword);
            return;
        }

        List<Artist> artists = artistService.searchArtists(keyword);

        System.out.println("\n----- Search Artists (Keyword: '" + keyword.trim() + "') -----");
        printArtists(artists);
    }

    public void promptArchiveArtist() {
        displayAllArtists();
        System.out.println("\n----- Archive Artist -----");
        int id = promptForInt("Enter artist ID to archive: ");

        boolean archived = artistService.archiveArtist(id);

        if (archived) {
            System.out.println("\nArtist ID " + id + " archived successfully!");
            displayAllArtists();
        } else {
            System.out.println("Failed to archive artist.");
        }
    }

    public void promptRestoreArtist() {
        System.out.println("\n----- Restore Artist -----");

        List<Artist> archivedArtists = artistService.getAllArchivedArtists();

        if (archivedArtists.isEmpty()) {
            System.out.println("No archived artists found.");
            return;
        }

        System.out.println("\n----- Archived Artists -----");
        printArtists(archivedArtists);

        int id = promptForInt("Enter artist ID to restore: ");

        boolean restored = artistService.restoreArtist(id);

        if (restored) {
            System.out.println("\nArtist ID " + id + " restored successfully!");
            displayAllArtists();
        } else {
            System.out.println("Failed to restore artist.");
        }
    }

    public void promptDeleteArtist() {
        displayAllArtists();
        System.out.println("\n----- Delete Artist -----");
        int id = promptForInt("Enter artist ID to delete: ");

        boolean deleted = artistService.deleteArtist(id);

        if (deleted) {
            System.out.println("\nArtist ID " + id + " deleted successfully!");
            displayAllArtists();
        } else {
            System.out.println("Failed to delete artist.");
        }
    }

    private void printArtists(List<Artist> artists) {
        if (artists == null || artists.isEmpty()) {
            System.out.println("No artists found.");
            return;
        }

        String border = ConsoleInput.border(5, 30);

        System.out.println(border);
        System.out.printf("| %-5s | %-30s |%n", "ID", "Artist Name");
        System.out.println(border);

        for (Artist artist : artists) {
            System.out.printf("| %-5d | %-30s |%n",
                    artist.getId(),
                    ConsoleInput.truncate(artist.getName(), 30));
        }

        System.out.println(border);
    }

    private int promptForInt(String prompt) {
        return ConsoleInput.readInt(prompt);
    }
}
