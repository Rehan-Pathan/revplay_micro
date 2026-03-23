package com.revplay.revplay_playlist_service.service;

import com.revplay.revplay_playlist_service.model.Playlist;
import com.revplay.revplay_playlist_service.model.PlaylistSong;
import com.revplay.revplay_playlist_service.repository.PlaylistRepository;
import com.revplay.revplay_playlist_service.repository.PlaylistSongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaylistSongService {

    private final PlaylistSongRepository playlistSongRepository;
    private final PlaylistRepository playlistRepository;

    public PlaylistSong addSongToPlaylist(Long playlistId, Long songId, Long userId) {

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        if (!playlist.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized playlist access");
        }

        if (playlistSongRepository.existsByPlaylistIdAndSongId(playlistId, songId)) {
            throw new RuntimeException("Song already in playlist");
        }

        int position = playlistSongRepository.countByPlaylistId(playlistId) + 1;

        PlaylistSong playlistSong = PlaylistSong.builder()
                .playlistId(playlistId)
                .songId(songId)
                .position(position)
                .build();

        return playlistSongRepository.save(playlistSong);
    }

    public void removeSongFromPlaylist(Long playlistId, Long songId, Long userId) {

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        // ownership validation
        if (!playlist.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized playlist access");
        }

        PlaylistSong playlistSong = playlistSongRepository
                .findByPlaylistIdAndSongId(playlistId, songId)
                .orElseThrow(() -> new RuntimeException("Song not in playlist"));

        int removedPosition = playlistSong.getPosition();

        playlistSongRepository.delete(playlistSong);

        // reorder remaining songs
        List<PlaylistSong> songs =
                playlistSongRepository.findByPlaylistIdOrderByPositionAsc(playlistId);

        for (PlaylistSong song : songs) {

            if (song.getPosition() > removedPosition) {

                song.setPosition(song.getPosition() - 1);

                playlistSongRepository.save(song);
            }
        }
    }
}
