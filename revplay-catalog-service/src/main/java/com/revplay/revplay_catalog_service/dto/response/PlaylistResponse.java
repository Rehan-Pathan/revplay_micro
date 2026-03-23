package com.revplay.revplay_catalog_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistResponse {

    private Long id;

    private String name;

    private String description;

    private String ownerUsername;

    private Long ownerId;

    private int totalSongs;

    private String visibility;

}
