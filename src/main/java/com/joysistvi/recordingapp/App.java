package com.joysistvi.recordingapp;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.model.User;
import com.joysistvi.recordingapp.repository.AlbumRepository;
import com.joysistvi.recordingapp.repository.AlbumRepositoryImpl;
import com.joysistvi.recordingapp.repository.ArtistRepository;
import com.joysistvi.recordingapp.repository.ArtistRepositoryImpl;
import com.joysistvi.recordingapp.repository.PlaylistRepository;
import com.joysistvi.recordingapp.repository.PlaylistRepositoryImpl;
import com.joysistvi.recordingapp.repository.SongRepository;
import com.joysistvi.recordingapp.repository.SongRepositoryImpl;
import com.joysistvi.recordingapp.repository.UserRepository;
import com.joysistvi.recordingapp.repository.UserRepositoryImpl;
import com.joysistvi.recordingapp.service.AlbumService;
import com.joysistvi.recordingapp.service.AlbumServiceImpl;
import com.joysistvi.recordingapp.service.ArtistService;
import com.joysistvi.recordingapp.service.ArtistServiceImpl;
import com.joysistvi.recordingapp.service.PlaylistService;
import com.joysistvi.recordingapp.service.PlaylistServiceImpl;
import com.joysistvi.recordingapp.service.SongService;
import com.joysistvi.recordingapp.service.SongServiceImpl;
import com.joysistvi.recordingapp.service.UserService;
import com.joysistvi.recordingapp.service.UserServiceImpl;
import com.joysistvi.recordingapp.view.AlbumView;
import com.joysistvi.recordingapp.view.ArtistView;
import com.joysistvi.recordingapp.view.ConsoleInput;
import com.joysistvi.recordingapp.view.PlaylistView;
import com.joysistvi.recordingapp.view.SongView;
import com.joysistvi.recordingapp.view.UserView;

// Entry Point
public class App {

    private final SongView songView;
    private final ArtistView artistView;
    private final AlbumView albumView;
    private final UserView userView;
    private final PlaylistView playlistView;

    private User currentUser;

    public App() {
        DbConnection dbConnection = new DbConnection();

        // Wiring (bottom-up): DbConnection -> Repository -> Service -> View
        SongRepository songRepository = new SongRepositoryImpl(dbConnection);
        SongService songService = new SongServiceImpl(songRepository);
        this.songView = new SongView(songService);

        ArtistRepository artistRepository = new ArtistRepositoryImpl(dbConnection);
        ArtistService artistService = new ArtistServiceImpl(artistRepository);
        this.artistView = new ArtistView(artistService);

        AlbumRepository albumRepository = new AlbumRepositoryImpl(dbConnection);
        AlbumService albumService = new AlbumServiceImpl(albumRepository);
        this.albumView = new AlbumView(albumService);

        UserRepository userRepository = new UserRepositoryImpl(dbConnection);
        UserService userService = new UserServiceImpl(userRepository);
        this.userView = new UserView(userService);

        PlaylistRepository playlistRepository = new PlaylistRepositoryImpl(dbConnection);
        PlaylistService playlistService = new PlaylistServiceImpl(playlistRepository);
        this.playlistView = new PlaylistView(playlistService);
    }

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    public void run() {
        while (true) {
            currentUser = showAuthMenuAndAuthenticate();

            if (currentUser == null) {
                System.out.println("\nGoodbye!");
                return;
            }

            dashboard();

            System.out.println("\nLogged out. Goodbye, " + currentUser.getUsername() + "!");
            currentUser = null;
        }
    }

    private User showAuthMenuAndAuthenticate() {
        while (true) {
            System.out.println("\n=== RECORDING STUDIO ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("0. Exit");

            int choice = promptForInt("Choice: ");
            switch (choice) {
                case 1 -> {
                    User user = userView.promptLoginUser();
                    if (user != null) {
                        return user;
                    }
                }
                case 2 -> userView.promptCreateUser();
                case 0 -> {
                    return null;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void dashboard() {
        while (true) {
            System.out.println("\n=== DASHBOARD ===");
            System.out.println("1. Song Management");
            System.out.println("2. Artist Management");
            System.out.println("3. Album Management");
            System.out.println("4. Users Management");
            System.out.println("5. Playlist Management");
            System.out.println("0. Logout");

            int choice = promptForInt("Choice: ");
            switch (choice) {
                case 1 -> songView.handleMenu();
                case 2 -> artistView.handleMenu();
                case 3 -> albumView.handleMenu();
                case 4 -> userView.handleMenu();
                case 5 -> playlistView.handleMenu();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private int promptForInt(String prompt) {
        return ConsoleInput.readInt(prompt);
    }
}