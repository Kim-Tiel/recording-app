package com.joysistvi.recordingapp.service;

import com.joysistvi.recordingapp.model.Album;
import com.joysistvi.recordingapp.repository.AlbumRepository;

import java.util.ArrayList;
import java.util.List;

public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;

    public AlbumServiceImpl(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    @Override
    public List<Album> getAllActiveAlbums() {
        return albumRepository.findAllActive();
    }

    @Override
    public List<Album> getAllArchivedAlbums() {
        return albumRepository.findAllArchived();
    }

    @Override
    public boolean createAlbum(String name, int year, int artistId) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("\nValidation Error: Album name is required.");
            return false;
        }

        if (year < 1900 || year > 2100) {
            System.out.println("\nValidation Error: Year must be between 1900 and 2100.");
            return false;
        }

        if (artistId <= 0) {
            System.out.println("\nValidation Error: Artist ID must be greater than 0.");
            return false;
        }

        Album album = new Album(name.trim(), year, artistId);
        return albumRepository.save(album);
    }

    @Override
    public boolean updateAlbum(int id, String name, int year, int artistId) {
        if (id <= 0) {
            System.out.println("\nValidation Error: ID must be greater than 0.");
            return false;
        }

        if (name == null || name.trim().isEmpty()) {
            System.out.println("\nValidation Error: Album name is required.");
            return false;
        }

        if (year < 1900 || year > 2100) {
            System.out.println("\nValidation Error: Year must be between 1900 and 2100.");
            return false;
        }

        if (artistId <= 0) {
            System.out.println("\nValidation Error: Artist ID must be greater than 0.");
            return false;
        }

        Album album = new Album(id, name.trim(), year, artistId);
        return albumRepository.update(album);
    }

    @Override
    public boolean deleteAlbum(int id) {
        if (id <= 0) {
            System.out.println("\nValidation Error: ID must be greater than 0.");
            return false;
        }

        return albumRepository.deleteById(id);
    }

    @Override
    public boolean archiveAlbum(int id) {
        if (id <= 0) {
            System.out.println("\nValidation Error: ID must be greater than 0.");
            return false;
        }

        return albumRepository.archiveById(id);
    }

    @Override
    public boolean restoreAlbum(int id) {
        if (id <= 0) {
            System.out.println("\nValidation Error: ID must be greater than 0.");
            return false;
        }

        return albumRepository.restoreById(id);
    }

    @Override
    public List<Album> searchAlbums(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("\nValidation Error: Search keyword is required.");
            return new ArrayList<>();
        }

        return albumRepository.searchByName(keyword.trim());
    }
}
