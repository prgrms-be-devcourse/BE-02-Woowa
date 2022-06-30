package com.example.woowa.restaurant.category.service;

import com.example.woowa.restaurant.category.dto.request.CategoryCreateRequest;
import com.example.woowa.restaurant.category.dto.request.CategoryUpdateRequest;
import com.example.woowa.restaurant.category.dto.response.CategoryCreateResponse;
import com.example.woowa.restaurant.category.dto.response.CategoryFindResponse;
import com.example.woowa.restaurant.category.entity.Category;
import com.example.woowa.restaurant.category.mapper.CategoryMapper;
import com.example.woowa.restaurant.category.repository.CategoryRepository;
import com.example.woowa.restaurant.restaurant.dto.response.RestaurantFindResponse;
import com.example.woowa.restaurant.restaurant.mapper.RestaurantMapper;
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
    private final CategoryMapper categoryMapper;
    private final RestaurantMapper restaurantMapper;

    @Transactional
    public CategoryCreateResponse createCategory(CategoryCreateRequest categoryCreateRequest) {
        Category category = categoryRepository
            .save(categoryMapper.toEntity(categoryCreateRequest));
        return categoryMapper.toCreateResponseDto(category);
    }

    public List<CategoryFindResponse> findCategories() {
        return categoryRepository.findAll().stream()
            .map(categoryMapper::toFindResponseDto)
            .collect(Collectors.toList());
    }

    public CategoryFindResponse findCategoryById(Long categoryId) {
        return categoryMapper.toFindResponseDto(categoryRepository.findById(categoryId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 아이디입니다.")));
    }

    public List<RestaurantFindResponse> findRestaurantsByCategoryId(Long categoryId) {
        return findCategoryEntityById(categoryId).getRestaurantCategories().stream()
            .map(restaurantCategory -> restaurantMapper.toFindResponseDto(restaurantCategory.getRestaurant()))
            .collect(Collectors.toList());
    }

    @Transactional
    public void changeCategoryName(Long categoryId, CategoryUpdateRequest categoryUpdateRequest) {
        findCategoryEntityById(categoryId).changeName(categoryUpdateRequest.getName());
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    public Category findCategoryEntityById(Long categoryId) {
        return categoryRepository.findById(categoryId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 아이디입니다."));
    }

}

