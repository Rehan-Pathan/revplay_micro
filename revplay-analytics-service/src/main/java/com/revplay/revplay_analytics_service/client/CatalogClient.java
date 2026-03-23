package com.revplay.revplay_analytics_service.client;

import com.revplay.revplay_analytics_service.dto.response.SongResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CATALOG-SERVICE")
public interface CatalogClient {

    @GetMapping("/revplay/catalog/songs/{songId}")
    SongResponse getSong(@PathVariable Long songId);

    @GetMapping("/revplay/catalog/songs/artist/{artistId}/songs/count")
    Long getArtistSongCount(@PathVariable Long artistId);

    @GetMapping("/revplay/catalog/albums/artist/{artistId}/albums/count")
    Long getArtistAlbumCount(@PathVariable Long artistId);

}