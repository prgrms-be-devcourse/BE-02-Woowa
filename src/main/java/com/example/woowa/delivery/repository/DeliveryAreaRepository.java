package com.example.woowa.delivery.repository;

import com.example.woowa.delivery.entity.DeliveryArea;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeliveryAreaRepository extends JpaRepository<DeliveryArea, Long> {

    @Query("SELECT da FROM DeliveryArea da JOIN FETCH da.areaCode ac WHERE da.restaurant = :restaurant AND ac.defaultAddress = :address")
    Optional<DeliveryArea> findByRestaurantAndAddress(@Param("restaurant") Restaurant restaurant,
            @Param("address") String defaultAddress);
}
