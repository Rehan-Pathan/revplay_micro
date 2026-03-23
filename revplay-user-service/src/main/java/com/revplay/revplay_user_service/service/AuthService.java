package com.revplay.revplay_user_service.service;

import com.revplay.revplay_user_service.Enum.RoleName;
import com.revplay.revplay_user_service.dto.request.LoginRequest;
import com.revplay.revplay_user_service.dto.request.RegisterRequest;
import com.revplay.revplay_user_service.dto.response.LoginResponse;
import com.revplay.revplay_user_service.dto.response.RegisterResponse;
import com.revplay.revplay_user_service.model.Role;
import com.revplay.revplay_user_service.model.User;
import com.revplay.revplay_user_service.repository.ArtistRepository;
import com.revplay.revplay_user_service.repository.RoleRepository;
import com.revplay.revplay_user_service.repository.UserRepository;
import com.revplay.revplay_user_service.security.JwtUtil;
import com.revplay.revplay_user_service.transformer.ArtistTransformer;
import com.revplay.revplay_user_service.transformer.AuthTransformer;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Builder
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CloudinaryService cloudinaryService;
    private final RoleRepository roleRepository;
    private final ArtistRepository artistRepository;

    public RegisterResponse registerUser(RegisterRequest request, MultipartFile profilePicture, String role) {
        User user = AuthTransformer.registerRequestToUser(request);

        if (profilePicture != null && !profilePicture.isEmpty()) {
            String url = cloudinaryService.uploadFile(profilePicture, "profile_images");
            user.setProfilePicture(url);
        }
        Role userRole;
        if (role.equals("LISTENER")) {
            userRole = roleRepository.findByName(RoleName.LISTENER)
                    .orElseThrow(() -> new RuntimeException("Role LISTENER not found"));
        } else {
            userRole = roleRepository.findByName(RoleName.ARTIST)
                    .orElseThrow(() -> new RuntimeException("Role ARTIST not found"));
        }
        user.getRoles().add(userRole);

        User savedUser = userRepository.save(user);

        if(role.equals("ARTIST")){
            artistRepository.save(ArtistTransformer.artistRequestToArtist(request, savedUser));

        }

        return AuthTransformer.userToRegisterResponse(savedUser);

    }


}
