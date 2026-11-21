package com.joysistvi.recordingapp.view;

import com.joysistvi.recordingapp.model.Artist;
import com.joysistvi.recordingapp.service.ArtistService;

import java.util.List;

// View(Presentation Tier) -> Console menus, Scanner inputs, and formatted output
public class ArtistView {

    private final ArtistService artistService;

    public ArtistView(ArtistService artistService) {
        this.artistService = artistService;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n===== ARTISTS =====");
            System.out.println("1. View All Artists");
            System.out.println("2. Create Artist");
            System.out.println("3. Update Artist");
            System.out.println("4. Delete Artist");
            System.out.println("5. Archive Artist");
            System.out.println("6. Restore Artist");
            System.out.println("7. Search Artists");
            System.out.println("0. Back");
            String choice = ConsoleInput.readString("Choose: ").trim();

            switch (choice) {
                case "1":
                    displayAllArtists();
                    break;
                case "2":
                    promptCreateArtist();
                    break;
                case "3":
                    promptUpdateArtist();
                    break;
                case "4":
                    promptDeleteArtist();
                    break;
                case "5":
                    promptArchiveArtist();
                    break;
                case "6":
                    promptRestoreArtist();
                    break;
                case "7":
                    promptSearchArtists();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("\nInvalid choice.");
            }
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
        int id = ConsoleInput.readInt("Enter artist ID to update: ");
        String name = ConsoleInput.readString("Enter new artist name: ");

        boolean updated = artistService.updateArtist(id, name);

        if (updated) {
            System.out.println("\nArtist ID " + id + " updated successfully!");
            displayAllArtists();
        } else {
            System.out.println("Failed to update artist.");
        }
    }

    public void promptDeleteArtist() {
        System.out.println("\n----- Delete Artist -----");
        int id = ConsoleInput.readInt("Enter artist ID to delete: ");

        boolean deleted = artistService.deleteArtist(id);

        if (deleted) {
            System.out.println("\nArtist ID " + id + " deleted successfully!");
            displayAllArtists();
        } else {
            System.out.println("Failed to delete artist.");
        }
    }

    public void promptArchiveArtist() {
        System.out.println("\n----- Archive Artist -----");
        int id = ConsoleInput.readInt("Enter artist ID to archive: ");

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

        // display archived artists first
        List<Artist> archivedArtists = artistService.getAllArchivedArtists();

        if (archivedArtists.isEmpty()) {
            System.out.println("No archived artists found.");
            return;
        }

        System.out.println("\n----- Archived Artists -----");
        printArtists(archivedArtists);

        int id = ConsoleInput.readInt("Enter artist ID to restore: ");

        boolean restored = artistService.restoreArtist(id);

        if (restored) {
            System.out.println("\nArtist ID " + id + " restored successfully!");
            displayAllArtists();
        } else {
            System.out.println("Failed to restore artist.");
        }
    }

    public void promptSearchArtists() {
        System.out.println("\n----- Search Artists -----");
        String keyword = ConsoleInput.readString("Enter keyword: ");

        if (keyword == null || keyword.trim().isEmpty()) {
            artistService.searchArtists(keyword); // shows the validation error
            return;
        }

        List<Artist> artists = artistService.searchArtists(keyword);

        System.out.println("\n----- Search Artists (Keyword: '" + keyword.trim() + "') -----");
        printArtists(artists);
    }

    private void printArtists(List<Artist> artists) {
        if (artists == null || artists.isEmpty()) {
            System.out.println("No artists found.");
            return;
        }

        System.out.printf("%-4s %-20s%n", "ID", "Artist Name");

        for (Artist artist : artists) {
            System.out.printf("%-4d %-20s%n",
                    artist.getId(),
                    artist.getName());
        }
    }
}