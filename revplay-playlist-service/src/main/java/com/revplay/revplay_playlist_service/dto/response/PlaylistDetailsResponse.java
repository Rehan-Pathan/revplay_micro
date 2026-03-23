package com.revplay.revplay_playlist_service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PlaylistDetailsResponse {

    private PlaylistResponse playlist;

    private List<Object> songs;
}
