package com.example.woowa.restaurant.category.controller;

import com.example.woowa.restaurant.category.dto.request.CategoryCreateRequest;
import com.example.woowa.restaurant.category.dto.request.CategoryUpdateRequest;
import com.example.woowa.restaurant.category.dto.response.CategoryCreateResponse;
import com.example.woowa.restaurant.category.dto.response.CategoryFindResponse;
import com.example.woowa.restaurant.category.service.CategoryService;
import com.example.woowa.restaurant.restaurant.dto.response.RestaurantFindResponse;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(value = "baemin/v1/categories")
@RestController
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryCreateResponse> createCategory(@Valid @RequestBody CategoryCreateRequest categoryCreateRequest) {
        CategoryCreateResponse newCategory = categoryService.createCategory(categoryCreateRequest);
        return new ResponseEntity<>(newCategory, HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryFindResponse>> findAllCategories() {
        List<CategoryFindResponse> categories = categoryService.findCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping(value = "{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryFindResponse> findCategory(@PathVariable Long categoryId) {
        CategoryFindResponse category = categoryService.findCategoryById(categoryId);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @GetMapping(value = "{categoryId}/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RestaurantFindResponse>> findRestaurantByCategoryId(@PathVariable Long categoryId) {
        List<RestaurantFindResponse> restaurants = categoryService.findRestaurantsByCategoryId(categoryId);
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @PutMapping(value = "/{categoryId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> changeCategoryName(@PathVariable Long categoryId,
        @RequestBody @Valid CategoryUpdateRequest categoryUpdateRequest) {
        categoryService.changeCategoryName(categoryId, categoryUpdateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
