package com.joysistvi.recordingapp.service;

import com.joysistvi.recordingapp.model.Song;
import com.joysistvi.recordingapp.repository.SongRepository;

import java.util.ArrayList;
import java.util.List;

public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;

    public SongServiceImpl(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @Override
    public List<Song> getAllActiveSongs(){
        return songRepository.findAllActive();
    }

    @Override
    public List<Song> getAllArchivedSongs() {
        return songRepository.findAllArchived();
    }

    @Override
    public boolean createSong(String title, String genre, String songLength, int albumId) {
        if(title == null || title.trim().isEmpty()) {
            System.out.println("\nValidation Error: Song title is required.");
            return false;
        }

        if(genre == null || genre.trim().isEmpty()) {
            System.out.println("\nValidation Error: Song genre is required.");
            return false;
        }

        if(songLength == null || songLength.trim().isEmpty()) {
            System.out.println("\nValidation Error: Song length is required.");
            return false;
        }

        if (albumId <= 0) {
            System.out.println("\nValidation Error: Album ID must be greater than 0.");
            return false;
        }

        Song song = new Song(title.trim(), genre.trim(), songLength.trim(), albumId);
        return songRepository.save(song);
    }

    @Override
    public boolean updateSong(int id, String title, String genre, String songLength, int albumId) {

        if (id <= 0) {
            System.out.println("\nValidation Error: ID must be greater than 0.");
            return false;
        }

        if(title == null || title.trim().isEmpty()) {
            System.out.println("\nValidation Error: Song title is required.");
            return false;
        }

        if(genre == null || genre.trim().isEmpty()) {
            System.out.println("\nValidation Error: Song genre is required.");
            return false;
        }

        if(songLength == null || songLength.trim().isEmpty()) {
            System.out.println("\nValidation Error: Song length is required.");
            return false;
        }

        if (albumId <= 0) {
            System.out.println("\nValidation Error: Album ID must be greater than 0.");
            return false;
        }

        Song song = new Song(id, title.trim(), genre.trim(), songLength.trim(), albumId);
        return songRepository.update(song);
    }

    @Override
    public boolean deleteSong(int id) {
        if (id <= 0) {
            System.out.println("\nValidation Error: ID must be greater than 0.");
            return false;
        }

        return songRepository.deleteById(id);
    }

    @Override
    public boolean archiveSong(int id) {
        if (id <= 0) {
            System.out.println("\nValidation Error: ID must be greater than 0.");
            return false;
        }

        return songRepository.archiveById(id);
    }

    @Override
    public boolean restoreSong(int id) {
        if (id <= 0) {
            System.out.println("\nValidation Error: ID must be greater than 0.");
            return false;
        }

        return songRepository.restoreById(id);
    }

    @Override
    public List<Song> searchSongs(String keyword) {
        if(keyword == null || keyword.trim().isEmpty()) {
            System.out.println("\nValidation Error: Search keyword is required.");
            return new ArrayList<>();
        }

        return songRepository.searchByTitle(keyword.trim());
    }
}
