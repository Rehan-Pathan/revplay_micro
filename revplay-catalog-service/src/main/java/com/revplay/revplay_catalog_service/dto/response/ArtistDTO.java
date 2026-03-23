package com.revplay.revplay_catalog_service.dto.response;

import lombok.Data;

@Data
public class ArtistDTO {

    private Long id;
    private String artistName;
    private String bio;
    private String genre;
    private String profilePicture;

}
