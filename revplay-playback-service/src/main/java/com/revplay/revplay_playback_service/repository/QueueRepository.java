package com.revplay.revplay_playback_service.repository;

import com.revplay.revplay_playback_service.model.QueueSong;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QueueRepository extends JpaRepository<QueueSong, Long> {

    List<QueueSong> findByUserIdOrderByPositionAsc(Long userId);

    int countByUserId(Long userId);

    Optional<QueueSong> findByUserIdAndSongId(Long userId, Long songId);

    void deleteByUserId(Long userId);

    Optional<QueueSong> findFirstByUserIdAndPositionGreaterThanOrderByPositionAsc(
            Long userId,
            int position
    );
    Optional<QueueSong> findFirstByUserIdAndPositionLessThanOrderByPositionDesc(
            Long userId,
            int position
    );
}