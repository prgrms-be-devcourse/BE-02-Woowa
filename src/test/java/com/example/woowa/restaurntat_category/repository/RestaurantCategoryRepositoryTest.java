package com.example.woowa.restaurntat_category.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.woowa.restaurant.category.entity.Category;
import com.example.woowa.restaurant.category.repository.CategoryRepository;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import com.example.woowa.restaurant.restaurant.repository.RestaurantRepository;
import com.example.woowa.restaurant.restaurntat_category.entity.RestaurantCategory;
import com.example.woowa.restaurant.restaurntat_category.entity.RestaurantCategoryId;
import java.time.LocalTime;

import com.example.woowa.restaurant.restaurntat_category.repository.RestaurantCategoryRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RestaurantCategoryRepositoryTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RestaurantCategoryRepository restaurantCategoryRepository;

    @Test
    @DisplayName("restaurant과 category의 기본키를 복합키로 가진 restaurant_category테이블을 생성한다.")
    public void testSaveRestaurantCategory() {
        // Given
        Restaurant restaurant = restaurantRepository.save(

        Restaurant.createRestaurant("테스트 레스토랑", "1234567890",
                LocalTime.now(), LocalTime.now(), true,
                "010-123-4567", "테스트용 임시 레스토랑 생성입니다.", "서울시 종로구"));

        Category category = categoryRepository.save(new Category("한식"));

        // When
        restaurantCategoryRepository.save(new RestaurantCategory(restaurant, category));

        // Then
        RestaurantCategoryId restaurantCategoryId = new RestaurantCategoryId(restaurant.getId(), category.getId());
        RestaurantCategory restaurantCategory = restaurantCategoryRepository.findById(restaurantCategoryId).get();

        assertThat(restaurantCategory.getRestaurant().getId()).isEqualTo(restaurant.getId());
        assertThat(restaurantCategory.getCategory().getId()).isEqualTo(category.getId());
    }

}