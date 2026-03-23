package com.revplay.revplay_analytics_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PLAYBACK-SERVICE")
public interface PlaybackClient {

    @GetMapping("/revplay/playback/user/{userId}/plays/count")
    Long getPlayCount(@PathVariable Long userId);
}
