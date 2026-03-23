package com.revplay.revplay_catalog_service.transformer;

import com.revplay.revplay_catalog_service.dto.response.AlbumResponse;
import com.revplay.revplay_catalog_service.dto.response.AlbumResponseDTO;
import com.revplay.revplay_catalog_service.dto.response.ArtistDTO;
import com.revplay.revplay_catalog_service.dto.response.SongResponseDTO;
import com.revplay.revplay_catalog_service.model.Album;

import java.util.List;

public class AlbumTransformer {

    public static AlbumResponse toAlbumResponse(Album savedAlbum, List<SongResponseDTO> songResponseDTOS, ArtistDTO artistDTO) {
        return AlbumResponse.builder()
                .id(savedAlbum.getId())
                .name(savedAlbum.getName())
                .description(savedAlbum.getDescription())
                .coverArtUrl(savedAlbum.getCoverImage())
                .releaseDate(savedAlbum.getReleaseDate())
                .artistName(artistDTO.getArtistName())
                .songs(songResponseDTOS)
                .build();
    }

    public static AlbumResponseDTO albumToAlbumResponseDTO(Album album) {
        return AlbumResponseDTO.builder()
                .id(album.getId())
                .name(album.getName())
                .build();
    }
}
