package com.met.account.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;
    static final String CLAIM_KEY_ROLES = "roles";
    static final String CLAIM_KEY_ID = "id";

    @Value("${jwt.secret}")
    private String secret;

    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public String getIdFromToken(String token) {
        String id;
        try {
            final Claims claims = getClaimsFromToken(token);
            id = (String)claims.get(CLAIM_KEY_ID);
        } catch (Exception e) {
            id = null;
        }
        return id;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    public List<GrantedAuthority> getRoles(String token) {
        List<GrantedAuthority> roles = new ArrayList<>();
        try {
            final Claims claims = getClaimsFromToken(token);
            roles = ((List<String>) claims.get(CLAIM_KEY_ROLES)).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        } catch (Exception e) {
            roles = null;
        }
        return roles;
    }
}

