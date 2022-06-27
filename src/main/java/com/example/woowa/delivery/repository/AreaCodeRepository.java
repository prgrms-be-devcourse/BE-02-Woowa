package com.example.woowa.delivery.repository;

import com.example.woowa.delivery.entity.AreaCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AreaCodeRepository extends JpaRepository<AreaCode, Long> {
    Optional<AreaCode> findByDefaultAddress(String defaultAddress);
    Optional<AreaCode> findByCode(String code);
}
