package com.revplay.revplay_playback_service.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackPlayRequest {

    private Long songId;

    private Long artistId;

    private Long userId;

}