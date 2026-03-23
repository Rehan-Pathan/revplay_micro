package com.revplay.revplay_user_service.repository;

import com.revplay.revplay_user_service.model.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    Optional<Artist> findByUserId(Long id);

    Page<Artist> findByArtistNameContainingIgnoreCase(String q, Pageable pageable);
}
