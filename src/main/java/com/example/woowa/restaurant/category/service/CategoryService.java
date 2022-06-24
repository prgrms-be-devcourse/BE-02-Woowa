package com.example.woowa.restaurant.category.service;

import com.example.woowa.restaurant.category.entity.Category;
import com.example.woowa.restaurant.category.repository.CategoryRepository;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import com.example.woowa.restaurant.restaurntat_category.entity.RestaurantCategory;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> findCategories() {
        return categoryRepository.findAll();
    }

    public Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 아이디입니다."));
    }

    public Category findCategoryByName(String name) {
        return categoryRepository.findCategoryByName(name)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 이름입니다."));
    }

    public List<Restaurant> findRestaurantsByCategoryId(Long categoryId) {
        return findCategoryById(categoryId).getRestaurantCategories().stream()
            .filter(restaurantCategory -> Objects.equals(restaurantCategory.getCategory().getId(), categoryId))
            .map(RestaurantCategory::getRestaurant)
            .collect(Collectors.toList());
    }

    public List<Restaurant> findRestaurantsByCategoryName(String name) {
        return findCategoryByName(name).getRestaurantCategories().stream()
            .filter(restaurantCategory -> Objects.equals(restaurantCategory.getCategory().getName(), name))
            .map(RestaurantCategory::getRestaurant)
            .collect(Collectors.toList());
    }

    @Transactional
    public Long createCategory(String name) {
        return categoryRepository.save(new Category(name)).getId();
    }

    @Transactional
    public void changeCategoryName(Long categoryId, String newName) {
        findCategoryById(categoryId).changeCategoryName(newName);
    }

    @Transactional
    public void changeCategoryName(String oldName, String newName) {
        findCategoryByName(oldName).changeCategoryName(newName);
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

}

