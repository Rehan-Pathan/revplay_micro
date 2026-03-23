package com.revplay.revplay_catalog_service.repository;

import com.revplay.revplay_catalog_service.model.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findByArtistId(Long artistId);

    List<Album> findTop10ByOrderByReleaseDateDesc();

    List<Album> findTop10ByOrderByCreatedAtDesc();

    long countByArtistId(Long artistId);

    Page<Album> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String name,
            String description,
            Pageable pageable
    );
}
