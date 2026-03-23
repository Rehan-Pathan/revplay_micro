package com.revplay.revplay_playlist_service.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReorderSongRequest {

    private Long songId;

    private int newPosition;

}