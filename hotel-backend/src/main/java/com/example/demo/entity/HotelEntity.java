package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * MariaDB 테이블: hotels
 * (id, name, rating, official_grade, grade_level, region, address, phone,
 *  homepage_url, latitude, longitude, canonical_key, cover_image_type,
 *  cover_image_url, cover_image_tier, created_at, updated_at)
 */
@Getter
@Setter
@Entity
@Table(name = "hotels")
public class HotelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    // DECIMAL(2,1) 혹은 (3,1) 등 — DB 정의에 맞게 들어갑니다.
    @Column(precision = 2, scale = 1)
    private BigDecimal rating;

    @Column(name = "official_grade", length = 20)
    private String officialGrade;

    @Column(name = "grade_level")
    private Integer gradeLevel;

    @Column(length = 50)
    private String region;

    @Column(length = 200)
    private String address;

    @Column(length = 30)
    private String phone;

    @Column(name = "homepage_url", length = 300)
    private String homepageUrl;

    // DECIMAL(10,7)
    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(name = "canonical_key", length = 64)
    private String canonicalKey;

    // ❗ enum → String 으로 변경 (DB 값 불일치로 인한 500 방지)
    @Column(name = "cover_image_type", length = 16)
    private String coverImageType;

    @Column(name = "cover_image_url", length = 500)
    private String coverImageUrl;

    // ❗ enum → String 으로 변경 (DB 값이 C1/C2/C3 외일 가능성 대비)
    @Column(name = "cover_image_tier", length = 8)
    private String coverImageTier;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
