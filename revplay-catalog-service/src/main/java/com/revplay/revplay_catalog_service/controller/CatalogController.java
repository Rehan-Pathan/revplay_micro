package com.revplay.revplay_catalog_service.controller;

import com.revplay.revplay_catalog_service.dto.response.ArtistCatalogDTO;
import com.revplay.revplay_catalog_service.dto.response.HomeResponse;
import com.revplay.revplay_catalog_service.dto.response.SongResponse;
import com.revplay.revplay_catalog_service.dto.response.SongResponseDTO;
import com.revplay.revplay_catalog_service.model.Song;
import com.revplay.revplay_catalog_service.service.CatalogService;
import com.revplay.revplay_catalog_service.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/revplay/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;
    private final SongService songService;

    @GetMapping("/artists/{artistId}")
    public ResponseEntity<ArtistCatalogDTO> getArtistCatalog(
            @PathVariable Long artistId) {

        return ResponseEntity.ok(
                catalogService.getArtistCatalog(artistId)
        );
    }

    @GetMapping("/home")
    public ResponseEntity<HomeResponse> getHome() {

        return ResponseEntity.ok(
                catalogService.getHome()
        );
    }

    @PostMapping("/songs/batch")
    public List<Song> getSongsByIds(@RequestBody List<Long> ids) {
        return catalogService.findAllById(ids);
    }

    @GetMapping("/songs/{songId}")
    public SongResponse getSong(@PathVariable Long songId) {

        Song song = songService.getSong(songId);

        return SongResponse.builder()
                .id(songId)
                .title(song.getTitle())
                .artistId(song.getArtistId())
                .build();


    }


    @GetMapping("/get-all-songs")
    public ResponseEntity<Page<SongResponseDTO>> browseSongs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {

        return ResponseEntity.ok(catalogService.browseAllSongs(page, size, sortBy, direction));
    }

}