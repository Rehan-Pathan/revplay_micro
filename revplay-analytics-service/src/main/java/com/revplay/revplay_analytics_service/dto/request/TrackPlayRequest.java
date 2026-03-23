package com.revplay.revplay_analytics_service.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrackPlayRequest {

    private Long songId;

    private Long artistId;

    private Long userId;

}