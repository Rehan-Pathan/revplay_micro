package com.revplay.revplay_playback_service.dto.request;

import com.revplay.revplay_playback_service.Enum.RepeatMode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RepeatModeRequest {

    private RepeatMode repeatMode;

}