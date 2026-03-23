package com.revplay.revplay_analytics_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TopSongProjection {

    private Long songId;
    private Long playCount;

}