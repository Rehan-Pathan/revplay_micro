package com.revplay.revplay_playback_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "listening_history",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"userId", "songId"})
        }
)
public class ListeningHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long songId;

    private LocalDateTime playedAt;

    @PrePersist
    public void onCreate() {
        playedAt = LocalDateTime.now();
    }
}