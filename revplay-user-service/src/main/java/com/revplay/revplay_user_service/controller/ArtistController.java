package com.revplay.revplay_user_service.controller;

import com.revplay.revplay_user_service.dto.request.ArtistProfileRequest;
import com.revplay.revplay_user_service.dto.response.ArtistDTO;
import com.revplay.revplay_user_service.dto.response.ArtistResponse;
import com.revplay.revplay_user_service.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@RestController
@RequestMapping("/revplay/auth/artist")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    private static final Logger logger =
            LoggerFactory.getLogger(ArtistController.class);

    @PutMapping(value = "/update-profile",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ArtistResponse> updateArtistProfile(

            @RequestParam("artist") String data,

            @RequestParam(value = "profilePicture", required = false)
            MultipartFile profilePicture,

            @RequestParam(value = "bannerImage", required = false)
            MultipartFile bannerImage
    ) {
        logger.info("updating artist profile");
        ArtistProfileRequest request = new ObjectMapper().readValue(data, ArtistProfileRequest.class);
        return ResponseEntity.ok(
                artistService.updateArtistProfile(request, profilePicture, bannerImage)
        );
    }

    @GetMapping("/get-profile")
    public ResponseEntity<ArtistResponse> getArtistProfile(){
        logger.info("fetching artist profile");
        return ResponseEntity.ok(artistService.getArtistProfile());
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<ArtistResponse>> getAllArtist(){
        logger.info("fetching all artist");
        return ResponseEntity.ok(artistService.getAllArtist());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ArtistDTO> getArtist(@PathVariable Long id){
        return ResponseEntity.ok(artistService.getArtist(id));
    }

    @GetMapping("/search/artists")
    public ResponseEntity<Page<ArtistDTO>> searchArtists(
            @RequestParam String q,
            Pageable pageable
    ) {

        return ResponseEntity.ok(artistService.searchArtist(q,pageable));
    }
}
