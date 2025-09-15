package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {
    public static class RefreshMeta {
        public final Long userId;
        public final Instant exp;
        public RefreshMeta(Long userId, Instant exp) {
            this.userId = userId; this.exp = exp;
        }
    }

    private final Map<String, RefreshMeta> refreshStore = new ConcurrentHashMap<>();

    public String issue(Long userId, long validSeconds) {
        String rt = UUID.randomUUID().toString();
        refreshStore.put(rt, new RefreshMeta(userId, Instant.now().plusSeconds(validSeconds)));
        return rt;
    }

    public Long verify(String refreshToken) {
        RefreshMeta meta = refreshStore.get(refreshToken);
        if (meta == null) return null;
        if (meta.exp.isBefore(Instant.now())) {
            refreshStore.remove(refreshToken);
            return null;
        }
        return meta.userId;
    }

    public void revoke(String refreshToken) {
        if (refreshToken != null) refreshStore.remove(refreshToken);
    }
}
