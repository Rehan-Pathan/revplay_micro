package com.revplay.revplay_favorites_service.repository;

import com.revplay.revplay_favorites_service.model.FavoriteSong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<FavoriteSong, Long> {

    boolean existsByUserIdAndSongId(Long userId, Long songId);

    Optional<FavoriteSong> findByUserIdAndSongId(Long userId, Long songId);

    List<FavoriteSong> findByUserId(Long userId);

    long countByUserId(Long userId);

    List<FavoriteSong> findBySongId(Long songId);

}