package com.example.demo.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String accessToken;
    private UserProfile user;

    public LoginResponse(String accessToken, UserProfile user) {
        this.accessToken = accessToken; this.user = user;
    }
}
