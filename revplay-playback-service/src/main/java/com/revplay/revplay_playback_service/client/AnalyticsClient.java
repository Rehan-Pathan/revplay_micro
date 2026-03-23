package com.revplay.revplay_playback_service.client;

import com.revplay.revplay_playback_service.dto.request.TrackPlayRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ANALYTICS-SERVICE")
public interface AnalyticsClient {

    @PostMapping("/revplay/analytics/track")
    void trackPlay(@RequestBody TrackPlayRequest request);

}