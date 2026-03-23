package com.revplay.revplay_playback_service.service;

import com.revplay.revplay_playback_service.Enum.RepeatMode;
import com.revplay.revplay_playback_service.client.AnalyticsClient;
import com.revplay.revplay_playback_service.client.CatalogClient;
import com.revplay.revplay_playback_service.dto.request.AddToQueueRequest;
import com.revplay.revplay_playback_service.dto.request.TrackPlayRequest;
import com.revplay.revplay_playback_service.dto.response.SongResponse;
import com.revplay.revplay_playback_service.model.ListeningHistory;
import com.revplay.revplay_playback_service.model.PlaybackState;
import com.revplay.revplay_playback_service.model.QueueSong;
import com.revplay.revplay_playback_service.repository.ListeningHistoryRepository;
import com.revplay.revplay_playback_service.repository.PlaybackStateRepository;
import com.revplay.revplay_playback_service.repository.QueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaybackService {

    private final QueueRepository queueRepository;
    private final CatalogClient catalogClient;
    private final AnalyticsClient analyticsClient;
    private final PlaybackStateRepository playbackStateRepository;
    private final ListeningHistoryRepository historyRepository;

    public QueueSong addToQueue(Long userId, AddToQueueRequest request) {

        int position = queueRepository.countByUserId(userId) + 1;

        QueueSong queueSong = QueueSong.builder()
                .userId(userId)
                .songId(request.getSongId())
                .position(position)
                .build();

        return queueRepository.save(queueSong);
    }

    public List<Object> getQueue(Long userId) {

        List<QueueSong> queue = queueRepository
                .findByUserIdOrderByPositionAsc(userId);

        List<Long> songIds = queue.stream()
                .map(QueueSong::getSongId)
                .toList();

        return catalogClient.getSongsByIds(songIds);
    }

    public void removeFromQueue(Long userId, Long songId) {

        QueueSong queueSong = queueRepository
                .findByUserIdAndSongId(userId, songId)
                .orElseThrow(() -> new RuntimeException("Song not in queue"));

        int removedPosition = queueSong.getPosition();

        queueRepository.delete(queueSong);

        // reorder remaining songs
        List<QueueSong> songs =
                queueRepository.findByUserIdOrderByPositionAsc(userId);

        for (QueueSong song : songs) {

            if (song.getPosition() > removedPosition) {

                song.setPosition(song.getPosition() - 1);

                queueRepository.save(song);
            }
        }
    }

    @Transactional
    public void clearQueue(Long userId) {

        queueRepository.deleteByUserId(userId);

    }

    public void playSong(Long userId, Long songId) {

        PlaybackState state = playbackStateRepository
                .findByUserId(userId)
                .orElse(
                        PlaybackState.builder()
                                .userId(userId)
                                .shuffleEnabled(false)
                                .repeatMode(RepeatMode.OFF)
                                .build()
                );

        state.setCurrentSongId(songId);
        state.setPlaying(true);

        playbackStateRepository.save(state);

        saveOrUpdateHistory(userId, songId);

        SongResponse song = catalogClient.getSong(songId);

        TrackPlayRequest request = TrackPlayRequest.builder()
                .songId(songId)
                .artistId(song.getArtistId())
                .userId(userId)
                .build();

        analyticsClient.trackPlay(request);
    }

    public void pauseSong(Long userId) {

        PlaybackState state = playbackStateRepository
                .findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Playback state not found"));

        state.setPlaying(false);

        playbackStateRepository.save(state);
    }

    public void playNext(Long userId) {

        PlaybackState state = playbackStateRepository
                .findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Playback state not found"));

        QueueSong current = queueRepository
                .findByUserIdAndSongId(userId, state.getCurrentSongId())
                .orElseThrow(() -> new RuntimeException("Current song not in queue"));

        QueueSong nextSong = queueRepository
                .findFirstByUserIdAndPositionGreaterThanOrderByPositionAsc(
                        userId,
                        current.getPosition()
                )
                .orElseThrow(() -> new RuntimeException("No next song in queue"));

        state.setCurrentSongId(nextSong.getSongId());
        state.setPlaying(true);

        playbackStateRepository.save(state);

        saveOrUpdateHistory(userId, nextSong.getSongId());
    }

    public void playPrevious(Long userId) {

        PlaybackState state = playbackStateRepository
                .findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Playback state not found"));

        QueueSong current = queueRepository
                .findByUserIdAndSongId(userId, state.getCurrentSongId())
                .orElseThrow(() -> new RuntimeException("Current song not in queue"));

        QueueSong previousSong = queueRepository
                .findFirstByUserIdAndPositionLessThanOrderByPositionDesc(
                        userId,
                        current.getPosition()
                )
                .orElseThrow(() -> new RuntimeException("No previous song in queue"));

        state.setCurrentSongId(previousSong.getSongId());
        state.setPlaying(true);

        playbackStateRepository.save(state);

        saveOrUpdateHistory(userId, previousSong.getSongId());
    }

    public void toggleShuffle(Long userId) {

        PlaybackState state = playbackStateRepository
                .findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Playback state not found"));

        boolean newShuffleState = !state.isShuffleEnabled();

        state.setShuffleEnabled(newShuffleState);

        playbackStateRepository.save(state);

        if (newShuffleState) {

            List<QueueSong> queue = queueRepository.findByUserIdOrderByPositionAsc(userId);

            Collections.shuffle(queue);

            int position = 1;

            for (QueueSong song : queue) {

                song.setPosition(position++);

            }

            queueRepository.saveAll(queue);
        }
    }

    public void updateRepeatMode(Long userId, RepeatMode repeatMode) {

        PlaybackState state = playbackStateRepository
                .findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Playback state not found"));

        state.setRepeatMode(repeatMode);

        playbackStateRepository.save(state);
    }

    public Page<Object> getListeningHistory(Long userId, Pageable pageable) {

        Page<ListeningHistory> historyPage =
                historyRepository.findByUserIdOrderByPlayedAtDesc(userId, pageable);

        List<Long> songIds = historyPage.getContent()
                .stream()
                .map(ListeningHistory::getSongId)
                .collect(Collectors.toList());

        List<Object> songs = catalogClient.getSongsByIds(songIds);

        return new PageImpl<>(songs, pageable, historyPage.getTotalElements());
    }


    public List<Object> getRecentlyPlayed(Long userId) {

        List<ListeningHistory> history =
                historyRepository.findTop20ByUserIdOrderByPlayedAtDesc(userId);

        List<Long> songIds = history.stream()
                .map(ListeningHistory::getSongId)
                .collect(Collectors.toList());

        return catalogClient.getSongsByIds(songIds);
    }

    @Transactional
    public void clearHistory(Long userId) {

        historyRepository.deleteByUserId(userId);

    }

    private void saveOrUpdateHistory(Long userId, Long songId) {

        ListeningHistory history = historyRepository
                .findByUserIdAndSongId(userId, songId)
                .orElse(null);

        if (history != null) {

            history.setPlayedAt(java.time.LocalDateTime.now());
            historyRepository.save(history);

        } else {

            history = ListeningHistory.builder()
                    .userId(userId)
                    .songId(songId)
                    .build();

            historyRepository.save(history);
        }
    }

    public Long getPlayCount(Long userId) {

        return historyRepository.countByUserId(userId);

    }
}