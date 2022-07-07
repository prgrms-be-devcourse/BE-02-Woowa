package com.example.woowa.restaurant.restaurant.repository;

import com.example.woowa.restaurant.owner.entity.Owner;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByName(String name);

    List<Restaurant> findByOwner(Owner owner);

    @Query("SELECT r FROM Restaurant r WHERE r.id in "
        + "(SELECT ra.restaurant.id FROM RestaurantAdvertisement ra WHERE ra.advertisement.id = :advertisementId)")
    List<Restaurant> findByAdvertisementId(@Param("advertisementId") Long advertisementId);

    @Query("SELECT r FROM Restaurant r WHERE r.id in "
        + "(SELECT rc.restaurant.id FROM RestaurantCategory rc WHERE rc.category.id = :categoryId)")
    List<Restaurant> findByCategoryId(@Param("categoryId") Long categoryId);

    List<Restaurant> findRestaurantByIsPermittedIsFalse();

}
