package com.library.lending_service.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class FeignAuthInterceptor implements RequestInterceptor {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Override
    public void apply(RequestTemplate template) {
        // 1. Generate a temporary M2M Token
        String serviceToken = generateServiceToken();

        // 2. Attach it to the Feign request headers!
        template.header("Authorization", "Bearer " + serviceToken);
    }

    private String generateServiceToken() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "ROLE_ADMIN"); // We grant the service ADMIN rights so it can read user data

        return Jwts.builder()
                .claims(claims)
                .subject("lending-service-internal")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey())
                .compact();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}