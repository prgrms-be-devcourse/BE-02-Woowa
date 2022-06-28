package com.example.woowa.restaurant.restaurant.service;

import com.example.woowa.restaurant.category.entity.Category;
import com.example.woowa.restaurant.category.service.CategoryService;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import com.example.woowa.restaurant.restaurant.repository.RestaurantRepository;
import com.example.woowa.restaurant.restaurntat_category.entity.RestaurantCategory;
import com.example.woowa.restaurant.restaurntat_category.entity.RestaurantCategoryId;
import com.example.woowa.restaurant.restaurntat_category.repository.RestaurantCategoryRepository;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantCategoryRepository restaurantCategoryRepository;
    private final CategoryService categoryService;

    @Transactional
    public Long createRestaurant(String name, String businessNumber, LocalTime openingTime,
        LocalTime closingTime, Boolean isOpen, String phoneNumber, String description,
        String address, List<Long> categories) {
        Restaurant restaurant = restaurantRepository.save(
            Restaurant.createRestaurant(name, businessNumber, openingTime, closingTime,
                isOpen, phoneNumber, description, address));

        categories.forEach(categoryId -> {
            Category category = categoryService.findCategoryById(categoryId);
            RestaurantCategory restaurantCategory = new RestaurantCategory(restaurant, category);
        });

        return restaurant.getId();
    }

    public Restaurant findRestaurantById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 restaurantId 입니다."));
    }

    @Transactional
    public void removeRestaurant(Long restaurantId) {
        Restaurant findRestaurant = findRestaurantById(restaurantId);
        restaurantRepository.delete(findRestaurant);
    }

    @Transactional
    public void updateBusinessHours(Long restaurantId, LocalTime openingTime,
            LocalTime closingTime) {
        findRestaurantById(restaurantId).updateBusinessHours(openingTime, closingTime);
    }

    @Transactional
    public void openRestaurant(Long restaurantId) {
        findRestaurantById(restaurantId).openRestaurant();
    }

    @Transactional
    public void closeRestaurant(Long restaurantId) {
        findRestaurantById(restaurantId).closeRestaurant();
    }

    @Transactional
    public void changeRestaurantDescription(Long restaurantId, String newDescription) {
        findRestaurantById(restaurantId).changeDescription(newDescription);
    }

    @Transactional
    public void changeRestaurantPhoneNumber(Long restaurantId, String newPhoneNumber) {
        findRestaurantById(restaurantId).changePhoneNumber(newPhoneNumber);
    }

    @Transactional
    public void addCategory(Long restaurantId, Long categoryId) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        Category category = categoryService.findCategoryById(categoryId);

        RestaurantCategory restaurantCategory = new RestaurantCategory(restaurant, category);
    }

    @Transactional
    public void removeCategory(Long restaurantId, Long categoryId) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        Category category = categoryService.findCategoryById(categoryId);

        RestaurantCategory restaurantCategory = restaurantCategoryRepository.findById(
            new RestaurantCategoryId(restaurantId, categoryId)).get();

        restaurant.getRestaurantCategories().remove(restaurantCategory);
        category.getRestaurantCategories().remove(restaurantCategory);
    }

    public List<Category> findCategories(Long restaurantId) {
        return findRestaurantById(restaurantId).getRestaurantCategories().stream()
            .map(RestaurantCategory::getCategory)
            .collect(Collectors.toList());
    }

}
