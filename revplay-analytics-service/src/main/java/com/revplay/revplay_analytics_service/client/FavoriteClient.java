package com.revplay.revplay_analytics_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "FAVORITES-SERVICE")
public interface FavoriteClient {

    @GetMapping("/revplay/favorites/user/{userId}/count")
    Long getFavoriteCount(@PathVariable Long userId);

    @GetMapping("/revplay/favorites/song/{songId}/users")
    List<Long> getUsersWhoFavorited(@PathVariable Long songId);
}
