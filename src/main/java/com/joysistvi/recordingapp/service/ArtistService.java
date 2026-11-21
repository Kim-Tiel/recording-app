package com.joysistvi.recordingapp.service;

import com.joysistvi.recordingapp.model.Artist;

import java.util.List;

// Service(Application Tier) -> Business rules, validation, guard clauses
public interface ArtistService {

    List<Artist> getAllActiveArtists();

    List<Artist> getAllArchivedArtists();

    boolean createArtist(String name);

    boolean updateArtist(int id, String name);

    boolean deleteArtist(int id);

    boolean archiveArtist(int id);

    boolean restoreArtist(int id);

    List<Artist> searchArtists(String keyword);
}