package com.revplay.revplay_playlist_service.client;

import com.revplay.revplay_playlist_service.dto.response.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE")
public interface UserClient {

    @GetMapping("/revplay/auth/user/{id}")
    UserDTO getUser(@PathVariable Long id);
}
