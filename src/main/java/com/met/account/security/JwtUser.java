package com.met.account.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtUser {
    private String username;
    private String id;
    private List<String> roles;
}

