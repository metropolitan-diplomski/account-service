package com.met.account.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    public String getAuthUserId() {
        JwtUser principal = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return principal.getId();
    }

    public String getAuthUserUsername() {
        JwtUser principal = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return principal.getUsername();
    }

    public boolean isUserAdmin() {
        JwtUser principal = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return principal.getRoles().contains("ROLE_ADMIN");
    }
}

