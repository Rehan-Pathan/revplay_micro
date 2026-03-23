package com.revplay.revplay_playback_service.repository;

import com.revplay.revplay_playback_service.model.PlaybackState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaybackStateRepository extends JpaRepository<PlaybackState, Long> {

    Optional<PlaybackState> findByUserId(Long userId);

}