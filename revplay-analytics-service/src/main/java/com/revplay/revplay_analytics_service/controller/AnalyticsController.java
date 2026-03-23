package com.revplay.revplay_analytics_service.controller;

import com.revplay.revplay_analytics_service.dto.request.TrackPlayRequest;
import com.revplay.revplay_analytics_service.dto.response.*;
import com.revplay.revplay_analytics_service.service.AnalyticsService;
import com.revplay.revplay_security.model.JwtUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/revplay/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @PostMapping("/track")
    public ResponseEntity<Void> trackPlay(
            @RequestBody TrackPlayRequest request) {

        analyticsService.trackSongPlay(request);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/artist/plays")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<Long> getTotalPlays(Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Long artistId = jwtUser.getArtistId();

        return ResponseEntity.ok(
                analyticsService.getTotalPlaysForArtist(artistId)
        );
    }

    @GetMapping("/artist/top-songs")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<List<TopSongResponse>> getTopSongs(
            Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Long artistId = jwtUser.getArtistId();

        return ResponseEntity.ok(
                analyticsService.getTopSongs(artistId)
        );
    }

    @GetMapping("/artist/top-listeners")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<Page<TopListenerResponse>> getTopListeners(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Long artistId = jwtUser.getArtistId();

        return ResponseEntity.ok(
                analyticsService.getTopListeners(artistId,page,size)
        );
    }

    @GetMapping("/artist/trends/daily")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<List<PlayTrendResponse>> getDailyTrends(
            Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Long artistId = jwtUser.getArtistId();

        return ResponseEntity.ok(
                analyticsService.getDailyTrends(artistId)
        );
    }

    @GetMapping("/artist/trends/weekly")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<List<PlayTrendResponse>> getWeeklyTrends(
            Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Long artistId = jwtUser.getArtistId();

        return ResponseEntity.ok(
                analyticsService.getWeeklyTrends(artistId)
        );
    }

    @GetMapping("/artist/trends/monthly")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<List<PlayTrendResponse>> getMonthlyTrends(
            Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Long artistId = jwtUser.getArtistId();

        return ResponseEntity.ok(
                analyticsService.getMonthlyTrends(artistId)
        );
    }

    @GetMapping("/artist/summary")
    public ResponseEntity<ArtistSummaryResponse> getArtistSummary(
            Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Long artistId = jwtUser.getArtistId();

        return ResponseEntity.ok(
                analyticsService.getArtistSummary(artistId)
        );
    }

    @GetMapping("/user/stats")
    public ResponseEntity<UserStatsResponse> getUserStats(
            Authentication authentication){

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        return ResponseEntity.ok(
                analyticsService.getUserStats(jwtUser.getUserId())
        );
    }

    @GetMapping("/artist/songs/{songId}/favorited-users")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<List<FavoritedUserResponse>> getFavoritedUsers(
            @PathVariable Long songId,
            Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        Long artistId = jwtUser.getArtistId();

        return ResponseEntity.ok(
                analyticsService.getFavoritedUsers(songId, artistId)
        );
    }
}