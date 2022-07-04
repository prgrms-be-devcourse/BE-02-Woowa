package com.example.woowa.restaurant.advertisement.repository;

import com.example.woowa.restaurant.advertisement.entity.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

}
