package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.UserProfile;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.TokenService;
import com.example.demo.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;
    private final UserService userService;

    private void setRefreshCookie(String refresh, HttpServletResponse res) {
        Cookie c = new Cookie("refreshToken", refresh);
        c.setHttpOnly(true);
        c.setPath("/api/auth"); // 경로 제한
        c.setMaxAge((int)Duration.ofDays(7).toSeconds()); // 7일
        res.addCookie(c);
    }

    private void clearRefreshCookie(HttpServletResponse res) {
        Cookie c = new Cookie("refreshToken", "");
        c.setHttpOnly(true);
        c.setPath("/api/auth");
        c.setMaxAge(0);
        res.addCookie(c);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req, HttpServletResponse res) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        UserDetails ud = (UserDetails) auth.getPrincipal();

        var demo = userService.getDemoUser();
        String at = jwtUtil.createAccessToken(demo.getId(), demo.getLoginId(), demo.getNickname());
        String rt = tokenService.issue(demo.getId(), Duration.ofDays(7).toSeconds()); // 7일
        setRefreshCookie(rt, res);

        var profile = new UserProfile(demo.getId(), demo.getLoginId(), demo.getNickname(), new String[]{"ROLE_USER"});
        return ResponseEntity.ok(new LoginResponse(at, profile));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(value = "refreshToken", required = false) String refreshToken) {
        Long uid = tokenService.verify(refreshToken);
        if (uid == null) return ResponseEntity.status(401).body(new java.util.HashMap<>() {{
            put("message","리프레시 토큰이 유효하지 않습니다.");
        }});
        var demo = userService.getDemoUser();
        String at = jwtUtil.createAccessToken(uid, demo.getLoginId(), demo.getNickname());
        return ResponseEntity.ok(java.util.Map.of("accessToken", at));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(value = "refreshToken", required = false) String refreshToken,
                                    HttpServletResponse res) {
        tokenService.revoke(refreshToken);
        clearRefreshCookie(res);
        return ResponseEntity.ok().build();
    }
}
