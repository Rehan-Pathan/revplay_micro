package com.revplay.revplay_catalog_service.transformer;

import com.revplay.revplay_catalog_service.client.UserClient;
import com.revplay.revplay_catalog_service.dto.response.ArtistDTO;
import com.revplay.revplay_catalog_service.dto.response.SongResponse;
import com.revplay.revplay_catalog_service.dto.response.SongResponseDTO;
import com.revplay.revplay_catalog_service.model.Album;
import com.revplay.revplay_catalog_service.model.Song;
import com.revplay.revplay_catalog_service.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SongTransformer {

    private final AlbumRepository albumRepository;
    private final UserClient userClient;

    public SongResponseDTO songToSongResponseDTO(Song song){

        String albumName = null;

        if (song.getAlbumId() != null) {
            Album album = albumRepository.findById(song.getAlbumId())
                    .orElse(null);

            if (album != null) {
                albumName = album.getName();
            }
        }

        ArtistDTO artist = userClient.getArtist(song.getArtistId());

        return SongResponseDTO.builder()
                .id(song.getId())
                .title(song.getTitle())
                .genre(song.getGenre())
                .duration(song.getDuration())
                .audioUrl(song.getAudioUrl())
                .coverArtUrl(song.getCoverImage())
                .artistId(song.getArtistId())
                .artistName(artist.getArtistName())
                .albumId(song.getAlbumId())
                .albumName(albumName)
                .visibility(song.getVisibility())
                .build();
    }

}