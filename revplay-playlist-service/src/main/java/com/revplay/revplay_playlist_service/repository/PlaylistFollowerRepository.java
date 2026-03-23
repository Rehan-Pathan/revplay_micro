package com.revplay.revplay_playlist_service.repository;

import com.revplay.revplay_playlist_service.model.PlaylistFollower;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaylistFollowerRepository extends JpaRepository<PlaylistFollower, Long> {

    Optional<PlaylistFollower> findByPlaylistIdAndUserId(Long playlistId, Long userId);

    boolean existsByPlaylistIdAndUserId(Long playlistId, Long userId);

}