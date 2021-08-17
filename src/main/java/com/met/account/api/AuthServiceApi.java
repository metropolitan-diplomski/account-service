package com.met.account.api;

import com.met.account.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("auth-service")
public interface AuthServiceApi {
    @GetMapping("/auth/users/{id}")
    UserResponse getUserById(@PathVariable("id") String id, @RequestHeader("Authorization") String token);
}
