package com.example.woowa.restaurant.menugroup.repository;

import com.example.woowa.restaurant.menugroup.entity.MenuGroup;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {

    List<MenuGroup> findByRestaurant(Restaurant restaurant);
}
