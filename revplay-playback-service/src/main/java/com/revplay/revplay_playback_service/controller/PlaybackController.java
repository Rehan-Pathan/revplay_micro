package com.revplay.revplay_playback_service.controller;

import com.revplay.revplay_playback_service.dto.request.AddToQueueRequest;
import com.revplay.revplay_playback_service.dto.request.RepeatModeRequest;
import com.revplay.revplay_playback_service.model.QueueSong;
import com.revplay.revplay_playback_service.service.PlaybackService;
import com.revplay.revplay_security.model.JwtUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/revplay/playback")
@RequiredArgsConstructor
public class PlaybackController {

    private final PlaybackService playbackService;

    @PostMapping("/queue")
    public ResponseEntity<QueueSong> addToQueue(
            @RequestBody AddToQueueRequest request,
            Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Long userId = jwtUser.getUserId();

        return ResponseEntity.ok(
                playbackService.addToQueue(userId, request)
        );
    }

    @GetMapping("/queue")
    public ResponseEntity<List<Object>> getQueue(
            Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Long userId = jwtUser.getUserId();

        return ResponseEntity.ok(
                playbackService.getQueue(userId)
        );
    }

    @DeleteMapping("/queue/{songId}")
    public ResponseEntity<Void> removeFromQueue(
            @PathVariable Long songId,
            Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Long userId = jwtUser.getUserId();

        playbackService.removeFromQueue(userId, songId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/queue")
    public ResponseEntity<Void> clearQueue(
            Authentication authentication) {

        if (authentication == null || !(authentication.getPrincipal() instanceof JwtUser jwtUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = jwtUser.getUserId();

        playbackService.clearQueue(userId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/play/{songId}")
    public ResponseEntity<Void> playSong(
            @PathVariable Long songId,
            Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Long userId = jwtUser.getUserId();

        playbackService.playSong(userId, songId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/pause")
    public ResponseEntity<Void> pauseSong(
            Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Long userId = jwtUser.getUserId();

        playbackService.pauseSong(userId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/next")
    public ResponseEntity<Void> playNext(
            Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Long userId = jwtUser.getUserId();

        playbackService.playNext(userId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/previous")
    public ResponseEntity<Void> playPrevious(
            Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Long userId = jwtUser.getUserId();

        playbackService.playPrevious(userId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/shuffle")
    public ResponseEntity<Void> toggleShuffle(
            Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Long userId = jwtUser.getUserId();

        playbackService.toggleShuffle(userId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/repeat")
    public ResponseEntity<Void> updateRepeatMode(
            @RequestBody RepeatModeRequest request,
            Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Long userId = jwtUser.getUserId();

        playbackService.updateRepeatMode(userId, request.getRepeatMode());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/history")
    public ResponseEntity<Page<Object>> getHistory(
            Authentication authentication,
            Pageable pageable
    ) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Long userId = jwtUser.getUserId();

        return ResponseEntity.ok(
                playbackService.getListeningHistory(userId, pageable)
        );
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Object>> getRecentlyPlayed(
            Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Long userId = jwtUser.getUserId();

        return ResponseEntity.ok(
                playbackService.getRecentlyPlayed(userId)
        );
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearHistory(Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        Long userId = jwtUser.getUserId();

        playbackService.clearHistory(userId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}/plays/count")
    public ResponseEntity<Long> getPlayCount(@PathVariable Long userId) {

        return ResponseEntity.ok(
                playbackService.getPlayCount(userId)
        );
    }
}