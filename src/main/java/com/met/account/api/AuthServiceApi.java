package com.met.account.api;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("auth-service")
public interface AuthServiceApi {
}
