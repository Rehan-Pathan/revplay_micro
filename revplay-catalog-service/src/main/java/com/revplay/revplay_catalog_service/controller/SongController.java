package com.revplay.revplay_catalog_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revplay.revplay_catalog_service.Enum.Genre;
import com.revplay.revplay_catalog_service.Enum.Visibility;
import com.revplay.revplay_catalog_service.dto.request.UpdateSongRequest;
import com.revplay.revplay_catalog_service.dto.request.UpdateVisibilityRequest;
import com.revplay.revplay_catalog_service.dto.request.UploadSongRequest;
import com.revplay.revplay_catalog_service.dto.response.SearchResponse;
import com.revplay.revplay_catalog_service.dto.response.SongResponse;
import com.revplay.revplay_catalog_service.dto.response.SongResponseDTO;
import com.revplay.revplay_catalog_service.model.Song;
import com.revplay.revplay_catalog_service.service.SongService;
import com.revplay.revplay_security.model.JwtUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/revplay/catalog/songs")
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    @PostMapping(
            value = "/upload",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<SongResponseDTO> uploadSong(
            @RequestParam("data") String data,
            @RequestParam("audio") MultipartFile audio,
            @RequestParam(value = "cover", required = false) MultipartFile cover,
            Authentication authentication
    ) throws Exception {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        Long artistId = jwtUser.getArtistId();

        UploadSongRequest request = new ObjectMapper().readValue(data, UploadSongRequest.class);

        SongResponseDTO response = songService.uploadSong(request, audio, cover, artistId);

        return ResponseEntity.ok(response);
    }

    @GetMapping()
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<List<SongResponseDTO>> getArtistSongs(Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        Long artistId = jwtUser.getArtistId();

        List<SongResponseDTO> songs = songService.getSongsByArtist(artistId);

        return ResponseEntity.ok(songs);
    }

    @DeleteMapping("/{songId}")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<String> deleteSong(
            @PathVariable Long songId,
            Authentication authentication
    ) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        Long artistId = jwtUser.getArtistId();

        songService.deleteSong(songId, artistId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{songId}")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<SongResponseDTO> updateSong(
            @PathVariable Long songId,
            @RequestBody UpdateSongRequest request,
            Authentication authentication
    ) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        Long artistId = jwtUser.getArtistId();

        SongResponseDTO song = songService.updateSong(songId, request, artistId);

        return ResponseEntity.ok(song);
    }

    @PatchMapping("/{songId}/visibility")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<SongResponseDTO> updateVisibility(
            @PathVariable Long songId,
            @RequestBody UpdateVisibilityRequest request,
            Authentication authentication
    ) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        Long artistId = jwtUser.getArtistId();

        SongResponseDTO song = songService.updateVisibility(songId, request.getVisibility(), artistId);

        return ResponseEntity.ok(song);
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<Page<SongResponseDTO>> getSongsByGenre(
            @PathVariable Genre genre,
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                songService.getSongsByGenre(genre, pageable)
        );
    }

    @GetMapping("/search-song")
    public ResponseEntity<List<Song>> searchSongs(@RequestParam String keyword) {
        return ResponseEntity.ok(songService.searchSongs(keyword));
    }

    @GetMapping("/albums/{albumId}")
    public ResponseEntity<List<Song>> getAlbumSongs(@PathVariable Long albumId) {
        return ResponseEntity.ok(songService.getAlbumSongs(albumId));
    }

    @GetMapping("/details/{songId}")
    public ResponseEntity<Song> getSong(
            @PathVariable Long songId) {

        return ResponseEntity.ok(
                songService.getSong(songId)
        );
    }

    @GetMapping("/artist/{artistId}/songs/count")
    public ResponseEntity<Long> getArtistSongCount(
            @PathVariable Long artistId) {

        return ResponseEntity.ok(
                songService.getArtistSongCount(artistId)
        );
    }

    @GetMapping("/artists/{artistId}")
    public ResponseEntity<List<Song>> getAllArtistSongs(@PathVariable Long artistId) {
        return ResponseEntity.ok(songService.getAllArtistSongs(artistId));
    }

    @GetMapping("/search")
    public ResponseEntity<SearchResponse> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(
                songService.search(q, page, size)
        );
    }


    @GetMapping("/filter")
    public ResponseEntity<List<SongResponseDTO>> filterSongs(
            @RequestParam(required = false) Genre genre,
            @RequestParam(required = false) Long artistId,
            @RequestParam(required = false) Integer year
    ) {

        return ResponseEntity.ok(
                songService.filterSongs(genre, artistId, year)
        );
    }

}
