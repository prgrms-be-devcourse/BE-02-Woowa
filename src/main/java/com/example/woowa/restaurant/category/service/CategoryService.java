package com.example.woowa.restaurant.category.service;

import com.example.woowa.restaurant.category.entity.Category;
import com.example.woowa.restaurant.category.repository.CategoryRepository;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import com.example.woowa.restaurant.restaurntat_category.entity.RestaurantCategory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Long createCategory(String categoryName) {
        return categoryRepository.save(new Category(categoryName)).getId();
    }

    public List<Category> findCategories() {
        return categoryRepository.findAll();
    }

    public Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 아이디입니다."));
    }

    public Category findCategoryByName(String categoryName) {
        return categoryRepository.findCategoryByName(categoryName)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 이름입니다."));
    }

    public List<Restaurant> findRestaurantsByCategoryId(Long categoryId) {
        return findCategoryById(categoryId).getRestaurantCategories().stream()
            .map(RestaurantCategory::getRestaurant)
            .collect(Collectors.toList());
    }

    public List<Restaurant> findRestaurantsByCategoryName(String categoryName) {
        return findCategoryByName(categoryName).getRestaurantCategories().stream()
            .map(RestaurantCategory::getRestaurant)
            .collect(Collectors.toList());
    }

    @Transactional
    public void changeCategoryName(Long categoryId, String newName) {
        findCategoryById(categoryId).changeName(newName);
    }

    @Transactional
    public void changeCategoryName(String oldName, String newName) {
        findCategoryByName(oldName).changeName(newName);
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

}

