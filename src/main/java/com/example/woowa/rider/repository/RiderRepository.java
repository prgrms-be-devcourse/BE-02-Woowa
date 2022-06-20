package com.example.woowa.rider.repository;

import com.example.woowa.rider.entity.Rider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiderRepository extends JpaRepository<Rider, Long> {
}
