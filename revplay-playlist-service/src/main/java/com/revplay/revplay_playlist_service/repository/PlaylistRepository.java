package com.revplay.revplay_playlist_service.repository;

import com.revplay.revplay_playlist_service.Enum.Visibility;
import com.revplay.revplay_playlist_service.model.Playlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    List<Playlist> findByUserId(Long userId);
    Page<Playlist> findByUserId(Long userId, Pageable pageable);
    List<Playlist> findByVisibility(Visibility visibility);
    Page<Playlist> findByVisibilityAndUserIdNot(
            Visibility visibility,
            Long userId,
            Pageable pageable
    );
    int countById(Long playlistId);

    long countByUserId(Long userId);

    Page<Playlist> findByVisibilityAndNameContainingIgnoreCase(Visibility visibility, String q, Pageable pageable);
}