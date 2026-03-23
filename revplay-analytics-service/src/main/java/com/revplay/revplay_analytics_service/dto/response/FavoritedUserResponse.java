package com.revplay.revplay_analytics_service.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavoritedUserResponse {

    private Long userId;
    private String username;
    private String displayName;
    private String profilePicture;

}
