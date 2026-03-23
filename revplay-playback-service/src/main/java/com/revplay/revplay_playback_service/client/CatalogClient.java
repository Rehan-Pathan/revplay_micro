package com.revplay.revplay_playback_service.client;

import com.revplay.revplay_playback_service.dto.response.SongResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "CATALOG-SERVICE")
public interface CatalogClient {

    @PostMapping("/revplay/catalog/songs/batch")
    List<Object> getSongsByIds(@RequestBody List<Long> songIds);

    @GetMapping("/revplay/catalog/songs/{songId}")
    SongResponse getSong(@PathVariable Long songId);

}