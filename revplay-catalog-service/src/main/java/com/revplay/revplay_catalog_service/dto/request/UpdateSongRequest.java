package com.revplay.revplay_catalog_service.dto.request;

import com.revplay.revplay_catalog_service.Enum.Genre;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateSongRequest {

    private String title;

    private Genre genre;

    private Long albumId;

}
