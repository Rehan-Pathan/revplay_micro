package com.revplay.revplay_catalog_service.dto.response;

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
