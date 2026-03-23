package com.revplay.revplay_catalog_service.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlbumResponseDTO {
    private Long id;
    private String name;
}
