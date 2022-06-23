package com.example.woowa.restaurant.restaurntat_category.repository;

import com.example.woowa.restaurant.restaurntat_category.entity.RestaurantCategory;
import com.example.woowa.restaurant.restaurntat_category.entity.RestaurantCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantCategoryRepository extends JpaRepository<RestaurantCategory, RestaurantCategoryId> {

}
