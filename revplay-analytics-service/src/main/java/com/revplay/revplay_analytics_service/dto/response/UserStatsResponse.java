package com.revplay.revplay_analytics_service.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserStatsResponse {

    private Long favoriteSongs;

    private Long totalPlayCount;

    private Long totalPlaylists;

}
