package com.revplay.revplay_user_service.service;

import com.revplay.revplay_user_service.dto.request.UserProfileUpdateRequest;
import com.revplay.revplay_user_service.dto.response.RegisterResponse;
import com.revplay.revplay_user_service.dto.response.UserResponse;
import com.revplay.revplay_user_service.model.User;
import com.revplay.revplay_user_service.repository.UserRepository;
import com.revplay.revplay_user_service.transformer.AuthTransformer;
import com.revplay.revplay_user_service.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    public String getCurrentEmail() {
        return SecurityUtils.getCurrentEmail();
    }

    @Transactional
    public RegisterResponse updateProfile(UserProfileUpdateRequest request, MultipartFile profileImage) {

        User user = userRepository.findByEmail(getCurrentEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            user.setUsername(request.getUsername().trim());
        }

        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }

        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = cloudinaryService.uploadFile(profileImage, "profile_images");
            user.setProfilePicture(imageUrl);
        }

        return AuthTransformer.userToRegisterResponse(user);
    }

    public RegisterResponse getProfile() {

        User user = userRepository.findByEmail(getCurrentEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return AuthTransformer.userToRegisterResponse(user);
    }

    public UserResponse getUserById(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
