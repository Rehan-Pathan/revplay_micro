package com.revplay.revplay_user_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArtistDTO {

    private Long id;
    private String artistName;
    private String bio;
    private String genre;
    private String profilePicture;

}
