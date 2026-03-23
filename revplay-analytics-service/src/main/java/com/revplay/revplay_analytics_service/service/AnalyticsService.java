package com.revplay.revplay_analytics_service.service;

import com.revplay.revplay_analytics_service.client.*;
import com.revplay.revplay_analytics_service.dto.request.TrackPlayRequest;
import com.revplay.revplay_analytics_service.dto.response.*;
import com.revplay.revplay_analytics_service.model.SongPlay;
import com.revplay.revplay_analytics_service.repository.SongPlayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final SongPlayRepository songPlayRepository;
    private final CatalogClient catalogClient;
    private final UserClient userClient;
    private final FavoriteClient favoriteClient;
    private final PlaybackClient playbackClient;
    private final PlaylistClient playlistClient;

    public void trackSongPlay(TrackPlayRequest request) {

        SongPlay play = SongPlay.builder()
                .songId(request.getSongId())
                .artistId(request.getArtistId())
                .userId(request.getUserId())
                .build();

        songPlayRepository.save(play);
    }

    public long getTotalPlaysForArtist(Long artistId) {

        return songPlayRepository.countByArtistId(artistId);

    }

    public List<TopSongResponse> getTopSongs(Long artistId) {

        List<TopSongProjection> topSongs =
                songPlayRepository.findTopSongsByArtist(artistId);

        return topSongs.stream()
                .map(song -> {

                    SongResponse catalogSong =
                            catalogClient.getSong(song.getSongId());

                    return TopSongResponse.builder()
                            .songId(song.getSongId())
                            .title(catalogSong.getTitle())
                            .playCount(song.getPlayCount())
                            .build();

                })
                .toList();
    }

    public Page<TopListenerResponse> getTopListeners(Long artistId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<TopListenerProjection> listeners =
                songPlayRepository.findTopListenersByArtistId(artistId, pageable);

        List<TopListenerResponse> responses = listeners.stream()
                .map(listener -> {

                    UserResponse user = userClient.getUser(listener.getUserId());

                    return TopListenerResponse.builder()
                            .userId(listener.getUserId())
                            .username(user.getUsername())
                            .playCount(listener.getPlayCount())
                            .build();

                })
                .toList();

        return new PageImpl<>(responses, pageable, listeners.getTotalElements());
    }

    public List<PlayTrendResponse> getDailyTrends(Long artistId) {
        return songPlayRepository.getDailyTrends(artistId);
    }

    public List<PlayTrendResponse> getWeeklyTrends(Long artistId) {
        return songPlayRepository.getWeeklyTrends(artistId);
    }

    public List<PlayTrendResponse> getMonthlyTrends(Long artistId) {
        return songPlayRepository.getMonthlyTrends(artistId);
    }

    public ArtistSummaryResponse getArtistSummary(Long artistId) {

        Long totalPlays = songPlayRepository.getTotalPlays(artistId);

        Long totalListeners = songPlayRepository.getTotalListeners(artistId);

        Long totalSongs = catalogClient.getArtistSongCount(artistId);

        Long totalAlbums = catalogClient.getArtistAlbumCount(artistId);

        return ArtistSummaryResponse.builder()
                .totalPlays(totalPlays)
                .totalListeners(totalListeners)
                .totalSongs(totalSongs)
                .totalAlbums(totalAlbums)
                .build();
    }

    public UserStatsResponse getUserStats(Long userId){

        Long favoriteSongs = favoriteClient.getFavoriteCount(userId);

        Long totalPlayCount = playbackClient.getPlayCount(userId);

        Long totalPlaylists = playlistClient.getPlaylistCount(userId);

        return UserStatsResponse.builder()
                .favoriteSongs(favoriteSongs)
                .totalPlayCount(totalPlayCount)
                .totalPlaylists(totalPlaylists)
                .build();
    }


    public List<FavoritedUserResponse> getFavoritedUsers(Long songId, Long artistId) {

        // verify artist owns the song
        SongResponse song = catalogClient.getSong(songId);

        if (!song.getArtistId().equals(artistId)) {
            throw new RuntimeException("Unauthorized access to song analytics");
        }

        List<Long> userIds = favoriteClient.getUsersWhoFavorited(songId);

        return userIds.stream()
                .map(id -> {
                    UserResponse user = userClient.getUser(id);

                    return FavoritedUserResponse.builder()
                            .userId(user.getId())
                            .username(user.getUsername())
                            .build();
                })
                .toList();
    }

}