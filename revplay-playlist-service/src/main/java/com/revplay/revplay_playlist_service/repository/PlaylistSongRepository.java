package com.revplay.revplay_playlist_service.repository;

import com.revplay.revplay_playlist_service.model.PlaylistSong;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, Long> {

    List<PlaylistSong> findByPlaylistIdOrderByPositionAsc(Long playlistId);

    int countByPlaylistId(Long playlistId);

    boolean existsByPlaylistIdAndSongId(Long playlistId, Long songId);

    Optional<PlaylistSong> findByPlaylistIdAndSongId(Long playlistId, Long songId);

    void deleteByPlaylistId(Long playlistId);
}