package com.revplay.revplay_favorites_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "CATALOG-SERVICE")
public interface CatalogClient {

    @PostMapping("/revplay/catalog/songs/batch")
    List<Object> getSongsByIds(@RequestBody List<Long> songIds);

}