package com.example.woowa.restaurant_advertisement.repository;

import com.example.woowa.restaurant_advertisement.entity.RestaurantAdvertisement;
import com.example.woowa.restaurant_advertisement.entity.RestaurnatAdvertisementId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantAdvertisementRepository extends JpaRepository<RestaurantAdvertisement, RestaurnatAdvertisementId> {

}
