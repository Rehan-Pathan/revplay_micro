package com.revplay.revplay_catalog_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revplay.revplay_catalog_service.dto.request.CreateAlbumRequest;
import com.revplay.revplay_catalog_service.dto.request.UpdateAlbumRequest;
import com.revplay.revplay_catalog_service.dto.response.AlbumResponse;
import com.revplay.revplay_catalog_service.dto.response.AlbumResponseDTO;
import com.revplay.revplay_catalog_service.model.Song;
import com.revplay.revplay_catalog_service.service.AlbumService;
import com.revplay.revplay_security.model.JwtUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/revplay/catalog/albums")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;


    @PostMapping(
            value = "/create",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<AlbumResponse> createAlbum(
            @RequestParam("data") String data,
            @RequestParam(value = "cover", required = false) MultipartFile cover,
            Authentication authentication) throws JsonProcessingException {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        Long artistId = jwtUser.getArtistId();
        CreateAlbumRequest request = new ObjectMapper().readValue(data, CreateAlbumRequest.class);

        return ResponseEntity.ok(
                albumService.createAlbum(request, cover, artistId)
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<List<AlbumResponse>> getAlbums(Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        Long artistId = jwtUser.getArtistId();

        return ResponseEntity.ok(
                albumService.getAlbums(artistId)
        );
    }


    @PostMapping(value = "/{albumId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<AlbumResponse> updateAlbum(
            @PathVariable Long albumId,
            @RequestParam("data") String data,
            @RequestParam(value = "cover", required = false) MultipartFile cover,
            Authentication authentication) throws JsonProcessingException {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        Long artistId = jwtUser.getArtistId();
        UpdateAlbumRequest request = new ObjectMapper().readValue(data, UpdateAlbumRequest.class);
        return ResponseEntity.ok(
                albumService.updateAlbum(albumId, request, artistId)
        );
    }


    @DeleteMapping("/{albumId}")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<Void> deleteAlbum(
            @PathVariable Long albumId,
            Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        Long artistId = jwtUser.getArtistId();

        albumService.deleteAlbum(albumId, artistId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{albumId}/songs/{songId}")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<Song> addSongToAlbum(
            @PathVariable Long albumId,
            @PathVariable Long songId,
            Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        Long artistId = jwtUser.getArtistId();

        return ResponseEntity.ok(
                albumService.addSongToAlbum(albumId, songId, artistId)
        );
    }

    @DeleteMapping("/{albumId}/songs/{songId}")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<Song> removeSongFromAlbum(
            @PathVariable Long albumId,
            @PathVariable Long songId,
            Authentication authentication) {

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        Long artistId = jwtUser.getArtistId();

        return ResponseEntity.ok(
                albumService.removeSongFromAlbum(albumId, songId, artistId)
        );
    }

    @GetMapping("/artist/{artistId}/albums/count")
    public ResponseEntity<Long> getArtistAlbumCount(
            @PathVariable Long artistId) {

        return ResponseEntity.ok(
                albumService.getArtistAlbumCount(artistId)
        );
    }


    @GetMapping("/get-all")
    public ResponseEntity<List<AlbumResponseDTO>> getAllAlbums(){
        return ResponseEntity.ok(albumService.getAllAlbums());
    }

}