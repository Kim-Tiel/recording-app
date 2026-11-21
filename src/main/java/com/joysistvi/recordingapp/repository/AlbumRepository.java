package com.joysistvi.recordingapp.repository;

import com.joysistvi.recordingapp.model.Album;

import java.util.List;

// Repository(Data Tier) -> Data Access Logic and Data Persistence
public interface AlbumRepository {

    List<Album> findAllActive();

    List<Album> findAllArchived();

    boolean save(Album album);

    boolean update(Album album);

    boolean deleteById(int id);

    boolean archiveById(int id);

    boolean restoreById(int id);

    List<Album> searchByName(String keyword);
}