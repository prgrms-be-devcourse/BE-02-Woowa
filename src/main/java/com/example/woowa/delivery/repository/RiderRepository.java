package com.example.woowa.delivery.repository;

import com.example.woowa.delivery.entity.Rider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiderRepository extends JpaRepository<Rider, Long> {
}
