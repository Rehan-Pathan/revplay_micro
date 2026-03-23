package com.revplay.revplay_catalog_service.service;

import com.revplay.revplay_catalog_service.Enum.Genre;
import com.revplay.revplay_catalog_service.Enum.Visibility;
import com.revplay.revplay_catalog_service.client.PlaylistClient;
import com.revplay.revplay_catalog_service.client.UserClient;
import com.revplay.revplay_catalog_service.dto.request.UpdateSongRequest;
import com.revplay.revplay_catalog_service.dto.request.UploadSongRequest;
import com.revplay.revplay_catalog_service.dto.response.*;
import com.revplay.revplay_catalog_service.model.Song;
import com.revplay.revplay_catalog_service.repository.AlbumRepository;
import com.revplay.revplay_catalog_service.repository.SongRepository;
import com.revplay.revplay_catalog_service.transformer.AlbumTransformer;
import com.revplay.revplay_catalog_service.transformer.SongTransformer;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SongService {

    private final SongRepository songRepository;
    private final CloudinaryService cloudinaryService;
    private final SongTransformer songTransformer;
    private final AlbumRepository albumRepository;
    private final UserClient userClient;
    private final PlaylistClient playlistClient;

    public SongResponseDTO uploadSong(
            UploadSongRequest dto,
            MultipartFile audio,
            MultipartFile cover,
            Long artistId
    ) {

        String audioUrl = null;

        if (audio != null && !audio.isEmpty()) {
            audioUrl = cloudinaryService.uploadFile(audio, "revplay/audio");
        }

        String coverUrl = null;

        if (cover != null && !cover.isEmpty()) {
            coverUrl = cloudinaryService.uploadFile(cover, "revplay/covers");
        }

        Song song = Song.builder()
                .title(dto.getTitle())
                .albumId(dto.getAlbumId())
                .genre(dto.getGenre())
                .duration(dto.getDuration())
                .audioUrl(audioUrl)
                .coverImage(coverUrl)
                .artistId(artistId)
                .releaseDate(LocalDate.parse(dto.getReleaseDate()))
                .build();

        Song savedSong = songRepository.save(song);
        return songTransformer.songToSongResponseDTO(savedSong);
    }

    public List<SongResponseDTO> getSongsByArtist(Long artistId) {
        return songRepository.findByArtistId(artistId).stream()
                .map(songTransformer::songToSongResponseDTO).toList();
    }

    public void deleteSong(Long songId, Long artistId) {

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        if (!song.getArtistId().equals(artistId)) {
            throw new RuntimeException("Unauthorized to delete this song");
        }

        songRepository.delete(song);
    }

    public SongResponseDTO updateSong(Long songId, UpdateSongRequest request, Long artistId) {

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        if (!song.getArtistId().equals(artistId)) {
            throw new RuntimeException("Unauthorized to update this song");
        }

        if (request.getTitle() != null) {
            song.setTitle(request.getTitle());
        }

        if (request.getGenre() != null) {
            song.setGenre(request.getGenre());
        }

        if (request.getAlbumId() != null) {
            song.setAlbumId(request.getAlbumId());
        } else {
            song.setAlbumId(null);
        }

        return songTransformer.songToSongResponseDTO(songRepository.save(song));
    }

    public SongResponseDTO updateVisibility(Long songId, Visibility visibility, Long artistId) {

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        if (!song.getArtistId().equals(artistId)) {
            throw new RuntimeException("Unauthorized");
        }

        song.setVisibility(visibility);

        return songTransformer.songToSongResponseDTO(songRepository.save(song));
    }

    public List<Song> getSongsByGenre(Genre genre) {
        return songRepository.findByGenreAndVisibility(genre, Visibility.PUBLIC);
    }

    public Page<SongResponseDTO> getSongsByGenre(Genre genre, Pageable pageable) {

        Page<Song> songs = songRepository.findByGenreAndVisibility(genre, Visibility.PUBLIC,pageable);

        return songs.map(songTransformer::songToSongResponseDTO);
    }

    public List<Song> getAlbumSongs(Long albumId) {
        return songRepository.findByAlbumIdAndVisibility(albumId, Visibility.PUBLIC);
    }

    public List<Song> searchSongs(String keyword) {
        return songRepository.searchSongs(keyword);
    }

    public Song getSong(Long songId) {

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        if (song.getVisibility() != Visibility.PUBLIC) {
            throw new RuntimeException("Song not available");
        }

        return song;
    }

    public Long getArtistSongCount(Long artistId) {

        return songRepository.countByArtistId(artistId);

    }

    public  List<Song> getAllArtistSongs(Long artistId) {
        return songRepository.findByArtistIdAndVisibility(artistId, Visibility.PUBLIC);
    }

    public SearchResponse search(String q, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        PageResponse<SongResponseDTO> songs =
                toPageResponse(
                        songRepository
                                .searchPublicSongs(q, Visibility.PUBLIC, pageable)
                                .map(songTransformer::songToSongResponseDTO)
                );

        PageResponse<AlbumResponseDTO> albums =
                toPageResponse(
                        albumRepository
                                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(q, q, pageable)
                                .map(AlbumTransformer::albumToAlbumResponseDTO)
                );

        PageResponse<ArtistDTO> artists =
                userClient.searchArtists(q, page, size);

        PageResponse<PlaylistResponse> playlists =
                playlistClient.searchPlaylists(q, page, size);

        return SearchResponse.builder()
                .query(q)
                .songs(songs)
                .artists(artists)
                .albums(albums)
                .playlists(playlists)
                .build();
    }

    private <T> PageResponse<T> toPageResponse(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getNumber(),
                page.getSize()
        );
    }

    public List<SongResponseDTO> filterSongs(Genre genre, Long artistId, Integer year) {

        List<Song> songs = songRepository.filterSongs(genre, artistId, year);

        return songs.stream()
                .map(songTransformer::songToSongResponseDTO)
                .toList();
    }
}
