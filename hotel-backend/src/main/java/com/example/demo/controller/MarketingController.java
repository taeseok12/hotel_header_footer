package com.example.demo.controller;

import com.example.demo.dto.SubscribeRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/marketing")
public class MarketingController {

    private final CopyOnWriteArrayList<String> subscribed = new CopyOnWriteArrayList<>();

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@Valid @RequestBody SubscribeRequest req) {
        subscribed.add(req.getEmail()); // 중복 허용
        return ResponseEntity.ok().build();
    }
}
