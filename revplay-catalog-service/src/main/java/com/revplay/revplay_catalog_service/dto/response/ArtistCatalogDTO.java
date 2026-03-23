package com.revplay.revplay_catalog_service.dto.response;

import com.revplay.revplay_catalog_service.model.Album;
import com.revplay.revplay_catalog_service.model.Song;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ArtistCatalogDTO {

    private Long artistId;
    private List<Album> albums;
    private List<Song> songs;

}
