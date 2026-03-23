package com.revplay.revplay_user_service.controller;

import com.revplay.revplay_user_service.dto.request.UserProfileUpdateRequest;
import com.revplay.revplay_user_service.dto.response.RegisterResponse;
import com.revplay.revplay_user_service.dto.response.UserResponse;
import com.revplay.revplay_user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/revplay/auth/user")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    @PutMapping(value = "/update-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RegisterResponse> updateMyProfile(
            @RequestParam("data") String data,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage
    ) throws Exception {
        UserProfileUpdateRequest request =
                new ObjectMapper().readValue(data, UserProfileUpdateRequest.class);

        return ResponseEntity.ok(
                userService.updateProfile(request, profileImage)
        );
    }

    @GetMapping("/get-profile")
    public ResponseEntity<RegisterResponse> getMyProfile() {
        return ResponseEntity.ok(userService.getProfile());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long userId) {

        return ResponseEntity.ok(
                userService.getUserById(userId)
        );
    }
}
