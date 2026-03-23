package com.revplay.revplay_playback_service.model;

import com.revplay.revplay_playback_service.Enum.RepeatMode;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "playback_state")
public class PlaybackState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long currentSongId;

    private boolean playing;

    private boolean shuffleEnabled;

    @Enumerated(EnumType.STRING)
    private RepeatMode repeatMode;
}