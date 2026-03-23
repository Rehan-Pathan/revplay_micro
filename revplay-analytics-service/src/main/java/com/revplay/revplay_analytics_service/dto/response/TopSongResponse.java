package com.revplay.revplay_analytics_service.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopSongResponse {

    private Long songId;
    private String title;
    private Long playCount;

}