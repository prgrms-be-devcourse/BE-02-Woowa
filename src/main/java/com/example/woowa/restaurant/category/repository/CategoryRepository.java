package com.example.woowa.restaurant.category.repository;

import com.example.woowa.restaurant.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
