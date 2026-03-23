package com.revplay.revplay_user_service.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {

    private Long id;
    private String username;
    private String email;
    private String profilePicture;
    private String bio;

}
