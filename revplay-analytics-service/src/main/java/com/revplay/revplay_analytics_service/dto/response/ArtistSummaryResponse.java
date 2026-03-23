package com.revplay.revplay_analytics_service.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArtistSummaryResponse {

    private Long totalPlays;

    private Long totalListeners;

    private Long totalSongs;

    private Long totalAlbums;

}
