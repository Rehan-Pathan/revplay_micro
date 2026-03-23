package com.revplay.revplay_playback_service.repository;

import com.revplay.revplay_playback_service.model.ListeningHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ListeningHistoryRepository extends JpaRepository<ListeningHistory, Long> {

    List<ListeningHistory> findByUserIdOrderByPlayedAtDesc(Long userId);

    Page<ListeningHistory> findByUserIdOrderByPlayedAtDesc(Long userId, Pageable pageable);

    List<ListeningHistory> findTop20ByUserIdOrderByPlayedAtDesc(Long userId);

    Optional<ListeningHistory> findByUserIdAndSongId(Long userId, Long songId);

    long countByUserId(Long userId);

    void deleteByUserId(Long userId);

}