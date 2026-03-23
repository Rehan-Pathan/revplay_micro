package com.revplay.revplay_user_service.transformer;

import com.revplay.revplay_user_service.Enum.Gender;
import com.revplay.revplay_user_service.dto.request.RegisterRequest;
import com.revplay.revplay_user_service.dto.response.RegisterResponse;
import com.revplay.revplay_user_service.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

public class AuthTransformer {

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static User registerRequestToUser(RegisterRequest registerRequest) {

        return User
                .builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .gender(Gender.valueOf(registerRequest.getGender()))
                .password(passwordEncoder().encode(registerRequest.getPassword()))
                .bio(registerRequest.getBio())
                .enabled(true)
                .roles(new HashSet<>())
                .build();
    }

    public static RegisterResponse userToRegisterResponse(User user) {
        return RegisterResponse
                .builder()
                .username(user.getUsername())
                .id(user.getId())
                .email(user.getEmail())
                .profilePicture(user.getProfilePicture())
                .bio(user.getBio())
                .build();

    }
}
