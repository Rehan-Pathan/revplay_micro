package com.revplay.revplay_playlist_service.controller;

import com.revplay.revplay_playlist_service.dto.request.*;
import com.revplay.revplay_playlist_service.dto.response.PlaylistDetailsResponse;
import com.revplay.revplay_playlist_service.dto.response.PlaylistResponse;
import com.revplay.revplay_playlist_service.dto.response.PlaylistSummary;
import com.revplay.revplay_playlist_service.model.Playlist;
import com.revplay.revplay_playlist_service.model.PlaylistSong;
import com.revplay.revplay_playlist_service.service.PlaylistService;
import com.revplay.revplay_playlist_service.service.PlaylistSongService;
import com.revplay.revplay_security.model.JwtUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/revplay/playlists")
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;
    private final PlaylistSongService playlistSongService;

    @PostMapping
    @PreAuthorize("hasRole('LISTENER')")
    public ResponseEntity<PlaylistResponse> createPlaylist(
            @RequestBody CreatePlaylistRequest request,
            Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Long userId = jwtUser.getUserId();

        return ResponseEntity.ok(
                playlistService.createPlaylist(request, userId)
        );
    }

    @PostMapping("/{playlistId}/songs")
    @PreAuthorize("hasRole('LISTENER')")
    public ResponseEntity<PlaylistSong> addSongToPlaylist(
            @PathVariable Long playlistId,
            @RequestBody AddSongRequest request,
            Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Long userId = jwtUser.getUserId();

        return ResponseEntity.ok(
                playlistSongService.addSongToPlaylist(
                        playlistId,
                        request.getSongId(),
                        userId
                )
        );
    }

    @GetMapping("/{playlistId}/songs")
    @PreAuthorize("hasRole('LISTENER')")
    public ResponseEntity<PlaylistDetailsResponse> getPlaylistSongs(
            @PathVariable Long playlistId) {

        return ResponseEntity.ok(
                playlistService.getPlaylistSongs(playlistId)
        );
    }

    @DeleteMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<Void> removeSongFromPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long songId,
            Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Long userId = jwtUser.getUserId();

        playlistSongService.removeSongFromPlaylist(playlistId, songId, userId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{playlistId}/reorder")
    @PreAuthorize("hasRole('LISTENER')")
    public ResponseEntity<Void> reorderSong(
            @PathVariable Long playlistId,
            @RequestBody ReorderSongRequest request,
            Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Long userId = jwtUser.getUserId();

        playlistService.reorderSong(
                playlistId,
                request.getSongId(),
                request.getNewPosition(),
                userId
        );

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{playlistId}")
    @PreAuthorize("hasRole('LISTENER')")
    public ResponseEntity<Playlist> updatePlaylist(
            @PathVariable Long playlistId,
            @RequestBody UpdatePlaylistRequest request,
            Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Long userId = jwtUser.getUserId();

        return ResponseEntity.ok(
                playlistService.updatePlaylist(
                        playlistId,
                        request,
                        userId
                )
        );
    }

    @PatchMapping("/{playlistId}/visibility")
    @PreAuthorize("hasRole('LISTENER')")
    public ResponseEntity<Playlist> updateVisibility(
            @PathVariable Long playlistId,
            @RequestBody UpdateVisibilityRequest request,
            Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Long userId = jwtUser.getUserId();

        return ResponseEntity.ok(
                playlistService.updatePlaylistVisibility(
                        playlistId,
                        request.getVisibility(),
                        userId
                )
        );
    }

    @PostMapping("/{playlistId}/follow")
    @PreAuthorize("hasRole('LISTENER')")
    public ResponseEntity<Void> followPlaylist(
            @PathVariable Long playlistId,
            Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Long userId = jwtUser.getUserId();

        playlistService.followPlaylist(playlistId, userId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{playlistId}/follow")
    @PreAuthorize("hasRole('LISTENER')")
    public ResponseEntity<Void> unfollowPlaylist(
            @PathVariable Long playlistId,
            Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Long userId = jwtUser.getUserId();

        playlistService.unfollowPlaylist(playlistId, userId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<PlaylistResponse>> getMyPlaylists(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            Authentication authentication
    ) {
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Long userId = jwtUser.getUserId();

        return ResponseEntity.ok(playlistService.getMyPlaylists(page, size, sortBy, direction, userId));
    }

    @GetMapping("/public")
    @PreAuthorize("hasRole('LISTENER')")
    public ResponseEntity<List<Playlist>> getPublicPlaylists() {

        return ResponseEntity.ok(
                playlistService.getPublicPlaylists()
        );
    }

    @GetMapping("/public/paged")
    @PreAuthorize("hasRole('LISTENER')")
    public ResponseEntity<Page<PlaylistResponse>> getPublicPlaylists(
            Pageable pageable,
            Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Long userId = jwtUser.getUserId();

        return ResponseEntity.ok(
                playlistService.getPublicPlaylists(userId, pageable)
        );
    }

    @GetMapping("/public/stats")
    @PreAuthorize("hasRole('LISTENER')")
    public ResponseEntity<Page<PlaylistSummary>> getPublicPlaylistsWithStats(
            Pageable pageable,
            Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Long userId = jwtUser.getUserId();

        return ResponseEntity.ok(
                playlistService.getPublicPlaylistsWithStats(userId, pageable)
        );
    }

    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> getPlaylistCount(@PathVariable Long userId) {

        return ResponseEntity.ok(
                playlistService.getPlaylistCount(userId)
        );
    }

    @DeleteMapping("/{playlistId}")
    public ResponseEntity<String> deletePlaylist(@PathVariable Long playlistId, Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        Long userId = jwtUser.getUserId();
        playlistService.deleteMyPlaylist(playlistId, userId);
        return ResponseEntity.ok("Playlist deleted successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PlaylistResponse>> searchPlaylists(
            @RequestParam String q,
            Pageable pageable
    ) {

        return ResponseEntity.ok(playlistService.searchPlaylist(q,pageable));
    }
}