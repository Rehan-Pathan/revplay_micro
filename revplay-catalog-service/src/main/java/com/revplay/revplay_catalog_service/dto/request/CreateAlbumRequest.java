package com.revplay.revplay_catalog_service.dto.request;

import com.revplay.revplay_catalog_service.Enum.Genre;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateAlbumRequest {

    private String name;
    private String description;
    private Genre genre;
    private String releaseDate;

}
