package com.example.demo.repo;

import com.example.demo.entity.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/** DB 접근(JPA) — 스프링이 구현체를 자동생성하여 쿼리 실행 */
public interface HotelJpaRepository
        extends JpaRepository<HotelEntity, Long>, JpaSpecificationExecutor<HotelEntity> {
}
