package com.revplay.revplay_favorites_service.service;

import com.revplay.revplay_favorites_service.client.CatalogClient;
import com.revplay.revplay_favorites_service.dto.request.AddFavoriteRequest;
import com.revplay.revplay_favorites_service.model.FavoriteSong;
import com.revplay.revplay_favorites_service.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final CatalogClient catalogClient;

    public FavoriteSong addFavorite(Long userId, AddFavoriteRequest request) {

        if (favoriteRepository.existsByUserIdAndSongId(userId, request.getSongId())) {
            throw new RuntimeException("Song already in favorites");
        }

        FavoriteSong favorite = FavoriteSong.builder()
                .userId(userId)
                .songId(request.getSongId())
                .build();

        return favoriteRepository.save(favorite);
    }

    public void removeFavorite(Long userId, Long songId) {

        FavoriteSong favorite = favoriteRepository
                .findByUserIdAndSongId(userId, songId)
                .orElseThrow(() -> new RuntimeException("Song not in favorites"));

        favoriteRepository.delete(favorite);
    }


    public List<Object> getMyFavorites(Long userId) {

        List<FavoriteSong> favorites = favoriteRepository.findByUserId(userId);

        List<Long> songIds = favorites.stream()
                .map(FavoriteSong::getSongId)
                .toList();

        return catalogClient.getSongsByIds(songIds);
    }

    public boolean isFavorite(Long userId, Long songId) {

        return favoriteRepository.existsByUserIdAndSongId(userId, songId);

    }

    public Long getFavoriteCount(Long userId) {

        return favoriteRepository.countByUserId(userId);

    }

    public List<Long> getUsersWhoFavorited(Long songId) {
        return favoriteRepository.findBySongId(songId)
                .stream()
                .map(FavoriteSong::getUserId)
                .toList();
    }
}