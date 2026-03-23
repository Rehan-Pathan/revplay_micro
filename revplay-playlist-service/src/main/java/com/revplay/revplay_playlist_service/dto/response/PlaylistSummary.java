package com.revplay.revplay_playlist_service.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class PlaylistSummary {

    private Long playlistId;

    private String name;

    private int followerCount;

    private int songCount;

}
