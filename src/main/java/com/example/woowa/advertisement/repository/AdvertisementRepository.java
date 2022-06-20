package com.example.woowa.advertisement.repository;

import com.example.woowa.advertisement.entity.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {

}
