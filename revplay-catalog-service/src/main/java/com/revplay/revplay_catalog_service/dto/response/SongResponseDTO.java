package com.revplay.revplay_catalog_service.dto.response;

import com.revplay.revplay_catalog_service.Enum.Genre;
import com.revplay.revplay_catalog_service.Enum.Visibility;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongResponseDTO {

    private Long id;
    private String title;
    private Genre genre;
    private int duration;
    private String audioUrl;
    private String coverArtUrl;

    private Long artistId;
    private String artistName;

    private Long albumId;
    private String albumName;

    private Visibility visibility;

}