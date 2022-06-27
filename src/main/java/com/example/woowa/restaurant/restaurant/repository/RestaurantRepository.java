package com.example.woowa.restaurant.restaurant.repository;

import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByName(String name);
}
