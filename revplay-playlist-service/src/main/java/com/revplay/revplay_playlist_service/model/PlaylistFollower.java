package com.revplay.revplay_playlist_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "playlist_followers")
public class PlaylistFollower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long playlistId;

    private Long userId;

    private LocalDateTime followedAt;

    @PrePersist
    public void onCreate() {
        followedAt = LocalDateTime.now();
    }
}