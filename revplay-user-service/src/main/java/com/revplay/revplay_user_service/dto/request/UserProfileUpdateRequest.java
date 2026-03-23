package com.revplay.revplay_user_service.dto.request;

import lombok.Data;

@Data
public class UserProfileUpdateRequest {
    private String username;
    private String bio;
}
