package com.example.demo.security;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final Key key;
    private final long accessExp;

    public JwtUtil(
        @Value("${app.jwt.secret}") String secret,
        @Value("${app.jwt.access-exp-seconds}") long accessExpSeconds
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessExp = accessExpSeconds;
    }

    public String createAccessToken(Long uid, String username, String nickname) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(username)
                .addClaims(Map.of("uid", uid, "nickname", nickname))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(accessExp)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }
}
