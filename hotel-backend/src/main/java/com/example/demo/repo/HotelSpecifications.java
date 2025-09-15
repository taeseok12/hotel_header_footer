package com.example.demo.repo;

import com.example.demo.entity.HotelEntity;
import org.springframework.data.jpa.domain.Specification;

public class HotelSpecifications {

    public static Specification<HotelEntity> filter(String q,
                                                    String region,
                                                    Boolean regionExact,   // ✅ 추가
                                                    Integer gradeMin,
                                                    Boolean hasHomepage) {
        return (root, query, cb) -> {
            var p = cb.conjunction();

            // q: name / region / address 부분일치
            if (q != null && !q.isBlank()) {
                String like = "%" + q.toLowerCase().trim() + "%";
                p.getExpressions().add(
                    cb.or(
                        cb.like(cb.lower(root.get("name")), like),
                        cb.like(cb.lower(root.get("region")), like),
                        cb.like(cb.lower(root.get("address")), like)
                    )
                );
            }

            // region: regionExact=true면 정확 일치(대소문자 무시), 아니면 부분일치
            if (region != null && !region.isBlank()) {
                String r = region.trim();
                if (Boolean.TRUE.equals(regionExact)) {
                    p.getExpressions().add(cb.equal(cb.lower(root.get("region")), r.toLowerCase()));
                } else {
                    String rlike = "%" + r.toLowerCase() + "%";
                    p.getExpressions().add(cb.like(cb.lower(root.get("region")), rlike));
                }
            }

            if (gradeMin != null) {
                p.getExpressions().add(cb.greaterThanOrEqualTo(root.get("gradeLevel"), gradeMin));
            }

            if (hasHomepage != null && hasHomepage) {
                p.getExpressions().add(cb.and(
                    cb.isNotNull(root.get("homepageUrl")),
                    cb.notEqual(cb.trim(root.get("homepageUrl")), "")
                ));
            }

            return p;
        };
    }
}
