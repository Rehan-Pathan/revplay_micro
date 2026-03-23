package com.revplay.revplay_analytics_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "song_plays")
public class SongPlay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long songId;

    private Long artistId;

    private Long userId;

    private LocalDateTime playedAt;

    @PrePersist
    public void onCreate() {
        playedAt = LocalDateTime.now();
    }
}