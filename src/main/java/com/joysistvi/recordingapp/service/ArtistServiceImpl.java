package com.joysistvi.recordingapp.service;

import com.joysistvi.recordingapp.model.Artist;
import com.joysistvi.recordingapp.repository.ArtistRepository;
import java.util.List;
import java.util.ArrayList;

public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;

    public ArtistServiceImpl(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public List<Artist> getAllActiveArtists() {
        return artistRepository.findAllActive();
    }

    @Override
    public boolean createArtist(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("\nValidation Error: Artist name is required.");
            return false;
        }

        Artist artist = new Artist(name.trim());
        return artistRepository.save(artist);
    }

    @Override
    public List<Artist> getAllArchivedArtists() {
        return artistRepository.findAllArchived();
    }

    @Override
    public boolean updateArtist(int id, String name) {
        if (id <= 0) {
            System.out.println("\nValidation Error: ID must be greater than 0.");
            return false;
        }

        if (name == null || name.trim().isEmpty()) {
            System.out.println("\nValidation Error: Artist name is required.");
            return false;
        }

        Artist artist = new Artist(id, name.trim());
        return artistRepository.update(artist);
    }

    @Override
    public boolean deleteArtist(int id) {
        if (id <= 0) {
            System.out.println("\nValidation Error: ID must be greater than 0.");
            return false;
        }

        return artistRepository.deleteById(id);
    }

    @Override
    public boolean archiveArtist(int id) {
        if (id <= 0) {
            System.out.println("\nValidation Error: ID must be greater than 0.");
            return false;
        }

        return artistRepository.archiveById(id);
    }

    @Override
    public boolean restoreArtist(int id) {
        if (id <= 0) {
            System.out.println("\nValidation Error: ID must be greater than 0.");
            return false;
        }

        return artistRepository.restoreById(id);
    }

    @Override
    public List<Artist> searchArtists(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("\nValidation Error: Search keyword is required.");
            return new ArrayList<>();
        }

        return artistRepository.searchByName(keyword.trim());
    }
}
