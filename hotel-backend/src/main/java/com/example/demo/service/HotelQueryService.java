package com.example.demo.service;

import com.example.demo.dto.PagedResponse;
import com.example.demo.model.Hotel;
import com.example.demo.repo.HotelJpaRepository;
import com.example.demo.repo.HotelSpecifications;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HotelQueryService {

    private final HotelJpaRepository repo;

    public PagedResponse<Hotel> search(String q,
                                       String region,
                                       Boolean regionExact,       // ✅ 추가
                                       Integer gradeMin,
                                       Boolean hasHomepage,
                                       int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
        var spec = HotelSpecifications.filter(q, region, regionExact, gradeMin, hasHomepage);

        var res = repo.findAll(spec, pageable);

        List<Hotel> content = res.getContent().stream().map(e -> {
            Hotel h = new Hotel();
            h.setId(e.getId());
            h.setName(e.getName());
            h.setRegion(e.getRegion());
            h.setAddress(e.getAddress());
            h.setPhone(e.getPhone());
            h.setHomepageUrl(e.getHomepageUrl());
            h.setRating(e.getRating() != null ? e.getRating().doubleValue() : 0.0);
            h.setOfficialGrade(e.getOfficialGrade());
            h.setGradeLevel(e.getGradeLevel());
            h.setAvgPricePerNight(0);
            h.setAmenities(List.of());
            return h;
        }).toList();

        return new PagedResponse<>(content, res.getNumber(), res.getSize(), res.getTotalElements());
    }

    public Hotel getOne(Long id) {
        var e = repo.findById(id).orElse(null);
        if (e == null) return null;

        Hotel h = new Hotel();
        h.setId(e.getId());
        h.setName(e.getName());
        h.setRegion(e.getRegion());
        h.setAddress(e.getAddress());
        h.setPhone(e.getPhone());
        h.setHomepageUrl(e.getHomepageUrl());
        h.setRating(e.getRating() != null ? e.getRating().doubleValue() : 0.0);
        h.setOfficialGrade(e.getOfficialGrade());
        h.setGradeLevel(e.getGradeLevel());
        h.setAvgPricePerNight(0);
        h.setAmenities(List.of());
        return h;
    }
}
