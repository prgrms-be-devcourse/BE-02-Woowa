package com.example.woowa.restaurant.category.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.woowa.restaurant.restaurant.service.RestaurantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class CategoryServiceTest {

    @Autowired
    CategoryService categoryService;

    @Autowired
    RestaurantService restaurantService;

    @Test
    void createCategory() {
    }

    @Test
    void findCategories() {
    }

    @Test
    void findCategoryById() {
    }

    @Test
    void findRestaurantsByCategoryId() {
    }

    @Test
    void changeCategoryName() {
    }

    @Test
    void deleteCategory() {
    }

    @Test
    void findCategoryEntityById() {
    }

}