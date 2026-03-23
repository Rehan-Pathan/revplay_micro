package com.revplay.revplay_analytics_service.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SongResponse {

    private Long id;
    private String title;
    private Long artistId;

}