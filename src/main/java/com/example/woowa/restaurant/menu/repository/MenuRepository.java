package com.example.woowa.restaurant.menu.repository;

import com.example.woowa.restaurant.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

}
