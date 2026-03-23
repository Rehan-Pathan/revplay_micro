package com.revplay.revplay_catalog_service.dto.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@Builder
public class SearchResponse {

    private String query;

    private PageResponse<SongResponseDTO> songs;

    private PageResponse<ArtistDTO> artists;

    private PageResponse<AlbumResponseDTO> albums;

    private PageResponse<PlaylistResponse> playlists;
}
