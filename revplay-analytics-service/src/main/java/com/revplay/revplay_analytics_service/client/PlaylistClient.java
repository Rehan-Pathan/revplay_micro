package com.revplay.revplay_analytics_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PLAYLIST-SERVICE")
public interface PlaylistClient {

    @GetMapping("/revplay/playlists/user/{userId}/count")
    Long getPlaylistCount(@PathVariable Long userId);
}
