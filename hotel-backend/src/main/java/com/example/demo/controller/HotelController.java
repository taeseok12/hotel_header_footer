package com.example.demo.controller;

import com.example.demo.dto.HotelDetail;
import com.example.demo.dto.PagedResponse;
import com.example.demo.model.Hotel;
import com.example.demo.service.HotelQueryService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hotels")
public class HotelController {

    private final HotelQueryService service;

    /** 호텔 검색 (q/region/regionExact/gradeMin/hasHomepage + 페이징) */
    @GetMapping("/search")
    public ResponseEntity<PagedResponse<Hotel>> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) Boolean regionExact,   // ✅ 추가
            @RequestParam(required = false) Integer gradeMin,
            @RequestParam(required = false) Boolean hasHomepage,
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "12") @Min(1) @Max(100) Integer size
    ) {
        return ResponseEntity.ok(service.search(q, region, regionExact, gradeMin, hasHomepage, page, size));
    }

    /** 단건 조회 */
    @GetMapping("/{id}")
    public ResponseEntity<HotelDetail> get(@PathVariable Long id) {
        Hotel h = service.getOne(id);
        if (h == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new HotelDetail(h));
    }
}
