package com.example.demo.controller;

import com.example.demo.security.JwtAuthenticationFilter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/my")
public class MyController {

    private final Map<Long, Set<Long>> wishlists = new ConcurrentHashMap<>();

    @GetMapping("/wishlist")
    public ResponseEntity<List<Long>> getWishlist(Authentication auth) {
        Long uid = ((JwtAuthenticationFilter.SimpleUser)auth.getPrincipal()).id();
        return ResponseEntity.ok(new ArrayList<>(wishlists.getOrDefault(uid, Set.of())));
    }

    @PostMapping("/wishlist")
    public ResponseEntity<?> addWishlist(Authentication auth, @Valid @RequestBody WishlistReq req) {
        Long uid = ((JwtAuthenticationFilter.SimpleUser)auth.getPrincipal()).id();
        wishlists.computeIfAbsent(uid, k -> new HashSet<>()).add(req.getHotelId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/wishlist/{hotelId}")
    public ResponseEntity<?> removeWishlist(Authentication auth, @PathVariable Long hotelId) {
        Long uid = ((JwtAuthenticationFilter.SimpleUser)auth.getPrincipal()).id();
        wishlists.computeIfAbsent(uid, k -> new HashSet<>()).remove(hotelId);
        return ResponseEntity.ok().build();
    }

    @Data
    public static class WishlistReq {
        @NotNull
        private Long hotelId;
    }
}
