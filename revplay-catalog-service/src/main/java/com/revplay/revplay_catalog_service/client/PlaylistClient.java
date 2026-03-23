package com.revplay.revplay_catalog_service.client;

import com.revplay.revplay_catalog_service.dto.response.PageResponse;
import com.revplay.revplay_catalog_service.dto.response.PlaylistResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "PLAYLIST-SERVICE")
public interface PlaylistClient {

    @GetMapping("/revplay/playlists/search")
    PageResponse<PlaylistResponse> searchPlaylists(
            @RequestParam String q,
            @RequestParam int page,
            @RequestParam int size
    );

}