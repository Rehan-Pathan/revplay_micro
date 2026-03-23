package com.revplay.revplay_catalog_service.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.revplay.revplay_catalog_service.Enum.Genre;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadSongRequest {

    private String title;

    private Long albumId;

    private Genre genre;

    private int duration;

    private String releaseDate;

}
