package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class Hotel {
    private Long id;
    private String name;
    private String region; // 서울/부산/강원/제주/기타
    private String address;
    private String phone;
    private String homepageUrl;
    private double rating;       // 0~5
    private String officialGrade; // ex) 특급
    private Integer gradeLevel;   // 1~5
    private int avgPricePerNight;
    private List<String> amenities;
}
