package com.revplay.revplay_catalog_service.client;

import com.revplay.revplay_catalog_service.dto.response.ArtistDTO;
import com.revplay.revplay_catalog_service.dto.response.PageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "USER-SERVICE")
public interface UserClient {

    @GetMapping("/users/email/{email}")
    ArtistDTO getArtistByEmail(@PathVariable String email);

    @GetMapping("/revplay/auth/artist/get/{id}")
    ArtistDTO getArtist(@PathVariable Long id);

    @GetMapping("/revplay/auth/artist/search/artists")
    PageResponse<ArtistDTO> searchArtists(
            @RequestParam String q,
            @RequestParam int page,
            @RequestParam int size
    );
}
