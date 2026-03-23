package com.revplay.revplay_playback_service.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongResponse {

    private Long id;
    private Long artistId;
    private String title;

}
