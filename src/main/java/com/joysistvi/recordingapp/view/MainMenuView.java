package com.joysistvi.recordingapp.view;

public class MainMenuView {

    private final ArtistView artistView;
    private final AlbumView albumView;
    private final UserView userView;

    public MainMenuView(ArtistView artistView, AlbumView albumView, UserView userView) {
        this.artistView = artistView;
        this.albumView = albumView;
        this.userView = userView;
    }

    public void start() {
        while (true) {
            System.out.println("\n===== RECORDING STUDIO =====");
            System.out.println("1. Artists");
            System.out.println("2. Albums");
            System.out.println("3. Users");
            System.out.println("0. Exit");
            String choice = ConsoleInput.readString("Choose: ").trim();

            switch (choice) {
                case "1":
                    artistView.handleMenu();
                    break;
                case "2":
                    albumView.handleMenu();
                    break;
                case "3":
                    userView.handleMenu();
                    break;
                case "0":
                    System.out.println("\nGoodbye!");
                    return;
                default:
                    System.out.println("\nInvalid choice.");
            }
        }
    }
}