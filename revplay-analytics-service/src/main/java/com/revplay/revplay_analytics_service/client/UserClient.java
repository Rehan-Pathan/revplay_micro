package com.revplay.revplay_analytics_service.client;

import com.revplay.revplay_analytics_service.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE")
public interface UserClient {

    @GetMapping("/revplay/auth/user/{userId}")
    UserResponse getUser(@PathVariable Long userId);

}
