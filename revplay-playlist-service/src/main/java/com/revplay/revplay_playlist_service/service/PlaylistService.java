package com.revplay.revplay_playlist_service.service;

import com.revplay.revplay_playlist_service.Enum.Visibility;
import com.revplay.revplay_playlist_service.client.CatalogClient;
import com.revplay.revplay_playlist_service.client.UserClient;
import com.revplay.revplay_playlist_service.dto.request.CreatePlaylistRequest;
import com.revplay.revplay_playlist_service.dto.request.UpdatePlaylistRequest;
import com.revplay.revplay_playlist_service.dto.response.PlaylistDetailsResponse;
import com.revplay.revplay_playlist_service.dto.response.PlaylistResponse;
import com.revplay.revplay_playlist_service.dto.response.PlaylistSummary;
import com.revplay.revplay_playlist_service.dto.response.UserDTO;
import com.revplay.revplay_playlist_service.model.Playlist;
import com.revplay.revplay_playlist_service.model.PlaylistFollower;
import com.revplay.revplay_playlist_service.model.PlaylistSong;
import com.revplay.revplay_playlist_service.repository.PlaylistFollowerRepository;
import com.revplay.revplay_playlist_service.repository.PlaylistRepository;
import com.revplay.revplay_playlist_service.repository.PlaylistSongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final PlaylistSongRepository playlistSongRepository;
    private final CatalogClient catalogClient;
    private final UserClient userClient;
    private final PlaylistFollowerRepository playlistFollowerRepository;

    public PlaylistResponse createPlaylist(CreatePlaylistRequest request, Long userId) {

        Playlist playlist = Playlist.builder()
                .name(request.getName())
                .description(request.getDescription())
                .visibility(request.getVisibility())
                .userId(userId)
                .build();

        Playlist savedPlaylist = playlistRepository.save(playlist);
        int totalSongs = playlistSongRepository.countByPlaylistId(savedPlaylist.getId());

        UserDTO userDTO = userClient.getUser(playlist.getUserId());
        return PlaylistResponse.builder()
                .id(playlist.getId())
                .name(playlist.getName())
                .description(playlist.getDescription())
                .visibility(playlist.getVisibility())
                .ownerId(userId)
                .ownerUsername(userDTO.getUsername())
                .totalSongs(totalSongs)
                .build();

    }

    public Page<PlaylistResponse> getMyPlaylists(int page, int size, String sortBy, String direction, Long userId) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return playlistRepository.findByUserId(userId, pageable)
                .map(playlist -> {

                    UserDTO user = userClient.getUser(playlist.getUserId());

                    return PlaylistResponse.builder()
                            .id(playlist.getId())
                            .name(playlist.getName())
                            .description(playlist.getDescription())
                            .visibility(playlist.getVisibility())
                            .ownerId(userId)
                            .ownerUsername(user.getUsername())
                            .totalSongs(playlistSongRepository.countByPlaylistId(playlist.getId()))
                            .build();
                });
    }

    public PlaylistDetailsResponse getPlaylistSongs(Long playlistId) {

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        List<PlaylistSong> playlistSongs =
                playlistSongRepository.findByPlaylistIdOrderByPositionAsc(playlistId);

        List<Long> songIds = playlistSongs.stream()
                .map(PlaylistSong::getSongId)
                .toList();

        List<Object> songs = catalogClient.getSongsByIds(songIds);
        UserDTO user = userClient.getUser(playlist.getUserId());
        PlaylistResponse playlistResponse = PlaylistResponse.builder()
                .id(playlist.getId())
                .name(playlist.getName())
                .description(playlist.getDescription())
                .visibility(playlist.getVisibility())
                .ownerId(playlist.getUserId())
                .ownerUsername(user.getUsername())
                .totalSongs(songIds.size())
                .build();

        return PlaylistDetailsResponse.builder()
                .playlist(playlistResponse)
                .songs(songs)
                .build();
    }

    public void reorderSong(Long playlistId, Long songId, int newPosition, Long userId) {

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        // ownership validation
        if (!playlist.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized playlist access");
        }

        List<PlaylistSong> songs =
                playlistSongRepository.findByPlaylistIdOrderByPositionAsc(playlistId);

        PlaylistSong targetSong = songs.stream()
                .filter(song -> song.getSongId().equals(songId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Song not in playlist"));

        int currentPosition = targetSong.getPosition();

        if (newPosition == currentPosition) {
            return;
        }

        for (PlaylistSong song : songs) {

            if (newPosition < currentPosition) {

                // moving up
                if (song.getPosition() >= newPosition && song.getPosition() < currentPosition) {
                    song.setPosition(song.getPosition() + 1);
                }

            } else {

                // moving down
                if (song.getPosition() <= newPosition && song.getPosition() > currentPosition) {
                    song.setPosition(song.getPosition() - 1);
                }
            }
        }

        targetSong.setPosition(newPosition);

        playlistSongRepository.saveAll(songs);
    }

    public Playlist updatePlaylist(Long playlistId,
                                   UpdatePlaylistRequest request,
                                   Long userId) {

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        // ownership validation
        if (!playlist.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized playlist access");
        }

        playlist.setName(request.getName());
        playlist.setDescription(request.getDescription());
        playlist.setVisibility(request.getVisibility());

        return playlistRepository.save(playlist);
    }

    public Playlist updatePlaylistVisibility(Long playlistId,
                                             Visibility visibility,
                                             Long userId) {

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        // ownership validation
        if (!playlist.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized playlist access");
        }

        playlist.setVisibility(visibility);

        return playlistRepository.save(playlist);
    }

    public void followPlaylist(Long playlistId, Long userId) {

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        if (playlist.getVisibility() == Visibility.PRIVATE) {
            throw new RuntimeException("Cannot follow private playlist");
        }

        if (playlistFollowerRepository.existsByPlaylistIdAndUserId(playlistId, userId)) {
            throw new RuntimeException("Already following playlist");
        }

        PlaylistFollower follower = PlaylistFollower.builder()
                .playlistId(playlistId)
                .userId(userId)
                .build();

        playlistFollowerRepository.save(follower);

        playlist.setFollowerCount(playlist.getFollowerCount() + 1);

        playlistRepository.save(playlist);
    }

    public void unfollowPlaylist(Long playlistId, Long userId) {

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        PlaylistFollower follower = playlistFollowerRepository
                .findByPlaylistIdAndUserId(playlistId, userId)
                .orElseThrow(() -> new RuntimeException("You are not following this playlist"));

        playlistFollowerRepository.delete(follower);

        // decrease follower count safely
        if (playlist.getFollowerCount() > 0) {
            playlist.setFollowerCount(playlist.getFollowerCount() - 1);
            playlistRepository.save(playlist);
        }
    }

    public List<Playlist> getPublicPlaylists() {

        return playlistRepository.findByVisibility(Visibility.PUBLIC);

    }

    public Page<PlaylistResponse> getPublicPlaylists(Long userId, Pageable pageable) {


        Page<Playlist> playlistPage = playlistRepository.findByVisibilityAndUserIdNot(
                Visibility.PUBLIC,
                userId,
                pageable
        );

        return playlistPage.map(
                playlist -> {
                    UserDTO userDTO = userClient.getUser(playlist.getUserId());
                    int totalSongs = playlistSongRepository.countByPlaylistId(playlist.getId());
                    return PlaylistResponse.builder()
                            .id(playlist.getId())
                            .name(playlist.getName())
                            .description(playlist.getDescription())
                            .visibility(playlist.getVisibility())
                            .ownerId(playlist.getUserId())
                            .ownerUsername(userDTO.getUsername())
                            .totalSongs(totalSongs)
                            .build();
                }
        );


    }

    public Page<PlaylistSummary> getPublicPlaylistsWithStats(Long userId, Pageable pageable) {

        Page<Playlist> playlists =
                playlistRepository.findByVisibilityAndUserIdNot(
                        Visibility.PUBLIC,
                        userId,
                        pageable
                );

        return playlists.map(p -> new PlaylistSummary(
                p.getId(),
                p.getName(),
                p.getFollowerCount(),
                playlistSongRepository.countByPlaylistId(p.getId())
        ));
    }

    public Long getPlaylistCount(Long userId) {

        return playlistRepository.countByUserId(userId);

    }

    @Transactional
    public void deleteMyPlaylist(Long playlistId, Long userId) {

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        if (playlist.getUserId() == null || !playlist.getUserId().equals(userId)) {
            throw new RuntimeException("You are not allowed to delete this playlist");
        }

        playlistSongRepository.deleteByPlaylistId(playlistId);
        playlistRepository.delete(playlist);
    }

    public Page<PlaylistResponse> searchPlaylist(String q, Pageable pageable) {

        Page<Playlist> playlistPage = playlistRepository
                .findByVisibilityAndNameContainingIgnoreCase(
                        Visibility.PUBLIC,
                        q,
                        pageable
                );

        return playlistPage.map(
                playlist -> {
                    UserDTO userDTO = userClient.getUser(playlist.getUserId());
                    int totalSongs = playlistSongRepository.countByPlaylistId(playlist.getId());
                    return PlaylistResponse.builder()
                            .id(playlist.getId())
                            .name(playlist.getName())
                            .description(playlist.getDescription())
                            .visibility(playlist.getVisibility())
                            .ownerId(playlist.getUserId())
                            .ownerUsername(userDTO.getUsername())
                            .totalSongs(totalSongs)
                            .build();
                }
        );
    }
}

