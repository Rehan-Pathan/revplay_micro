package com.revplay.revplay_playlist_service.dto.request;

import com.revplay.revplay_playlist_service.Enum.Visibility;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePlaylistRequest {

    private String name;

    private String description;

    private Visibility visibility;

}
