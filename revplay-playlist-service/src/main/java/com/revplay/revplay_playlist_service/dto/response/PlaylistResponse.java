package com.revplay.revplay_playlist_service.dto.response;

import com.revplay.revplay_playlist_service.Enum.Visibility;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlaylistResponse {

    private Long id;
    private String name;
    private String description;
    private Visibility visibility;
    private Long ownerId;
    private String ownerUsername;
    private int totalSongs;
}