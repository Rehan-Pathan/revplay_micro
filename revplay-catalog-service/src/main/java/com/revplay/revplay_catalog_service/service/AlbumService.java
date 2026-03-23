package com.revplay.revplay_catalog_service.service;

import com.revplay.revplay_catalog_service.client.UserClient;
import com.revplay.revplay_catalog_service.dto.request.CreateAlbumRequest;
import com.revplay.revplay_catalog_service.dto.request.UpdateAlbumRequest;
import com.revplay.revplay_catalog_service.dto.response.AlbumResponse;
import com.revplay.revplay_catalog_service.dto.response.AlbumResponseDTO;
import com.revplay.revplay_catalog_service.dto.response.ArtistDTO;
import com.revplay.revplay_catalog_service.dto.response.SongResponseDTO;
import com.revplay.revplay_catalog_service.model.Album;
import com.revplay.revplay_catalog_service.model.Song;
import com.revplay.revplay_catalog_service.repository.AlbumRepository;
import com.revplay.revplay_catalog_service.repository.SongRepository;
import com.revplay.revplay_catalog_service.transformer.AlbumTransformer;
import com.revplay.revplay_catalog_service.transformer.SongTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final SongRepository songRepository;
    private final CloudinaryService cloudinaryService;
    private final SongTransformer songTransformer;
    private final UserClient userClient;


    public AlbumResponse createAlbum(CreateAlbumRequest request, MultipartFile cover, Long artistId) {


        String coverImageUrl = null;

        if (cover != null && !cover.isEmpty()) {
            coverImageUrl = cloudinaryService.uploadFile(cover, "revplay/covers");
        }

        Album album = Album.builder()
                .name(request.getName())
                .description(request.getDescription())
                .genre(request.getGenre())
                .coverImage(coverImageUrl)
                .releaseDate(LocalDate.parse(request.getReleaseDate()))
                .artistId(artistId)
                .build();

        Album savedAlbum = albumRepository.save(album);

        List<Song> songs = songRepository.findByAlbumId(savedAlbum.getArtistId());
        List<SongResponseDTO> songResponseDTOS = songs.stream().map(songTransformer::songToSongResponseDTO).toList();
        ArtistDTO artistDTO = userClient.getArtist(artistId);
        return AlbumTransformer.toAlbumResponse(savedAlbum, songResponseDTOS, artistDTO);
    }

    public List<AlbumResponse> getAlbums(Long artistId) {
        List<Album> albums = albumRepository.findByArtistId(artistId);
        ArtistDTO artistDTO = userClient.getArtist(artistId);

        return albums.stream().map(album -> {

            List<Song> songs = songRepository.findByAlbumId(album.getId());

            List<SongResponseDTO> songResponses =
                    songs.stream()
                            .map(songTransformer::songToSongResponseDTO)
                            .toList();

            return AlbumTransformer.toAlbumResponse(album, songResponses, artistDTO);

        }).toList();

    }

    public AlbumResponse updateAlbum(Long albumId, UpdateAlbumRequest request, Long artistId) {

        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("Album not found"));

        if (!album.getArtistId().equals(artistId)) {
            throw new RuntimeException("Unauthorized to update this album");
        }

        album.setName(request.getName());
        album.setDescription(request.getDescription());

        Album savedAlbum = albumRepository.save(album);

        List<Song> songs = songRepository.findByAlbumId(savedAlbum.getArtistId());
        List<SongResponseDTO> songResponseDTOS = songs.stream().map(songTransformer::songToSongResponseDTO).toList();
        ArtistDTO artistDTO = userClient.getArtist(artistId);
        return AlbumTransformer.toAlbumResponse(savedAlbum, songResponseDTOS, artistDTO);
    }

    @Transactional
    public void deleteAlbum(Long albumId, Long artistId) {

        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("Album not found"));

        if (!album.getArtistId().equals(artistId)) {
            throw new RuntimeException("Unauthorized to delete this album");
        }

        List<Song> songs = songRepository.findByAlbumId(albumId);

        for (Song song : songs) {
            if (!song.getArtistId().equals(artistId)) {
                throw new RuntimeException("Unauthorized song access");
            }
            song.setAlbumId(null);
        }

        songRepository.saveAll(songs);

        albumRepository.delete(album);
    }

    @Transactional
    public Song addSongToAlbum(Long albumId, Long songId, Long artistId) {

        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("Album not found"));

        if (!album.getArtistId().equals(artistId)) {
            throw new RuntimeException("Unauthorized album access");
        }

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        if (!song.getArtistId().equals(artistId)) {
            throw new RuntimeException("Unauthorized song access");
        }

        song.setAlbumId(albumId);

        return songRepository.save(song);
    }

    @Transactional
    public Song removeSongFromAlbum(Long albumId, Long songId, Long artistId) {

        Song song = songRepository
                .findByIdAndAlbumIdAndArtistId(songId, albumId, artistId)
                .orElseThrow(() -> new RuntimeException("Song not found in this album"));

        song.setAlbumId(null);

        return songRepository.save(song);
    }

    public Long getArtistAlbumCount(Long artistId) {

        return albumRepository.countByArtistId(artistId);

    }

    public List<AlbumResponseDTO> getAllAlbums() {
        return albumRepository.findAll().stream().map(
                AlbumTransformer::albumToAlbumResponseDTO
        ).toList();
    }
}
