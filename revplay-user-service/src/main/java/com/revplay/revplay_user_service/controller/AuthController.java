package com.revplay.revplay_user_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.revplay.revplay_user_service.dto.request.LoginRequest;
import com.revplay.revplay_user_service.dto.request.RegisterRequest;
import com.revplay.revplay_user_service.dto.response.LoginResponse;
import com.revplay.revplay_user_service.dto.response.RegisterResponse;
import com.revplay.revplay_user_service.model.User;
import com.revplay.revplay_user_service.repository.UserRepository;
import com.revplay.revplay_user_service.security.JwtUtil;
import com.revplay.revplay_user_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/revplay/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    private static final Logger logger =
            LoggerFactory.getLogger(AuthController.class);

    @PostMapping(value = "/register/user",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<RegisterResponse> registerUser(
            @RequestParam("user") String data,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture
    ) throws JsonProcessingException {
        logger.info("registering user...........");
        RegisterRequest userRequest = new ObjectMapper().readValue(data, RegisterRequest.class);
        return ResponseEntity.ok(authService.registerUser(userRequest, profilePicture, "LISTENER"));
    }

    @PostMapping(value = "/register/artist",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<RegisterResponse> registerArtist(
            @RequestParam("user") String data,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture
    ) throws JsonProcessingException {
        logger.info("registering artist...........");
        RegisterRequest userRequest = new ObjectMapper().readValue(data, RegisterRequest.class);
        return ResponseEntity.ok(authService.registerUser(userRequest, profilePicture, "ARTIST"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        logger.info("started login...........");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow();
        return ResponseEntity.ok(
                new LoginResponse(jwt, user.getUsername(), user.getEmail(), roles)
        );
    }

}
