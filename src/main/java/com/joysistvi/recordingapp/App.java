package com.joysistvi.recordingapp;

import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.repository.AlbumRepository;
import com.joysistvi.recordingapp.repository.AlbumRepositoryImpl;
import com.joysistvi.recordingapp.repository.ArtistRepository;
import com.joysistvi.recordingapp.repository.ArtistRepositoryImpl;
import com.joysistvi.recordingapp.repository.UserRepository;
import com.joysistvi.recordingapp.repository.UserRepositoryImpl;
import com.joysistvi.recordingapp.service.AlbumService;
import com.joysistvi.recordingapp.service.AlbumServiceImpl;
import com.joysistvi.recordingapp.service.ArtistService;
import com.joysistvi.recordingapp.service.ArtistServiceImpl;
import com.joysistvi.recordingapp.service.UserService;
import com.joysistvi.recordingapp.service.UserServiceImpl;
import com.joysistvi.recordingapp.view.AlbumView;
import com.joysistvi.recordingapp.view.ArtistView;
import com.joysistvi.recordingapp.view.MainMenuView;
import com.joysistvi.recordingapp.view.UserView;

// Entry Point
public class App {

    public static void main(String[] args) {
        DbConnection dbConnection = new DbConnection();

        // Wiring (bottom-up): DbConnection -> Repository -> Service -> View
        ArtistRepository artistRepository = new ArtistRepositoryImpl(dbConnection);
        ArtistService artistService = new ArtistServiceImpl(artistRepository);
        ArtistView artistView = new ArtistView(artistService);

        AlbumRepository albumRepository = new AlbumRepositoryImpl(dbConnection);
        AlbumService albumService = new AlbumServiceImpl(albumRepository);
        AlbumView albumView = new AlbumView(albumService);

        UserRepository userRepository = new UserRepositoryImpl(dbConnection);
        UserService userService = new UserServiceImpl(userRepository);
        UserView userView = new UserView(userService);

        MainMenuView mainMenuView = new MainMenuView(artistView, albumView, userView);
        mainMenuView.start();
    }
}

// APPLICATION FLOW
/*
    Repository
        |
        v
    Service
        |
        v
    View
        |
        v
    Main Application

    Dependency Wiring:
    Repository --> Service --> View --> main()
*/