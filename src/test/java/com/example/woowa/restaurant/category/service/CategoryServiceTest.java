package com.example.woowa.restaurant.category.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.woowa.restaurant.category.entity.Category;
import com.example.woowa.restaurant.category.repository.CategoryRepository;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import com.example.woowa.restaurant.restaurant.repository.RestaurantRepository;
import com.example.woowa.restaurant.restaurntat_category.entity.RestaurantCategory;
import com.example.woowa.restaurant.restaurntat_category.repository.RestaurantCategoryRepository;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantCategoryRepository restaurantCategoryRepository;

    @Test
    @DisplayName("저장된 모든 카테고리를 조회할 수 있다.")
    void testFindCategories() {
        categoryService.createCategory("한식");
        categoryService.createCategory("중식");

        List<Category> categories = categoryService.findCategories();

        assertThat(categories.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("아이디를 통해 저장된 카테고리를 조회할 수 있다.")
    void findCategoryById() {
        Long koreanId = categoryService.createCategory("한식");
        Long chineseId = categoryService.createCategory("중식");

        assertThat(categoryService.findCategoryById(koreanId).getName()).isEqualTo("한식");
        assertThat(categoryService.findCategoryById(chineseId).getName()).isEqualTo("중식");
    }

    @Test
    @DisplayName("이름을 통해 저장된 카테고리를 조회할 수 있다.")
    void findCategoryByName() {
        Long koreanId = categoryService.createCategory("한식");
        Long chineseId = categoryService.createCategory("중식");

        assertThat(categoryService.findCategoryByName("한식").getId()).isEqualTo(koreanId);
        assertThat(categoryService.findCategoryByName("중식").getId()).isEqualTo(chineseId);
    }

    @Test
    @DisplayName("카테고리 아이디를 통해 연관된 가게를 조회할 수 있다.")
    void findRestaurantsByCategoryId() {
        Category korean = categoryRepository.save(new Category("한식"));

        Restaurant restaurant1 = Restaurant.createRestaurant("테스트 레스토랑1", "1234567890",
            LocalTime.now(), LocalTime.now(), true,
            "010-123-4567", "테스트용 임시 레스토랑 생성입니다.", "서울시 종로구");

        Restaurant restaurant2 = Restaurant.createRestaurant("테스트 레스토랑1", "1234567890",
            LocalTime.now(), LocalTime.now(), true,
            "010-123-4567", "테스트용 임시 레스토랑 생성입니다.", "서울시 종로구");

        restaurantRepository.save(restaurant1);
        restaurantRepository.save(restaurant2);

        RestaurantCategory relation1 = new RestaurantCategory(restaurant1, korean);
        relation1.setCategory(korean);
        relation1.setRestaurant(restaurant1);

        RestaurantCategory relation2 = new RestaurantCategory(restaurant2, korean);
        relation2.setCategory(korean);
        relation2.setRestaurant(restaurant2);

        restaurantCategoryRepository.save(relation1);
        restaurantCategoryRepository.save(relation2);

        List<Restaurant> restaurants = categoryService.findRestaurantsByCategoryId(korean.getId());
        assertThat(restaurants.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("카테고리 이름을 통해 연관된 가게를 조회할 수 있다.")
    void findRestaurantsByCategoryName() {
        Category korean = categoryRepository.save(new Category("한식"));

        Restaurant restaurant1 = Restaurant.createRestaurant("테스트 레스토랑1", "1234567890",
            LocalTime.now(), LocalTime.now(), true,
            "010-123-4567", "테스트용 임시 레스토랑 생성입니다.", "서울시 종로구");

        Restaurant restaurant2 = Restaurant.createRestaurant("테스트 레스토랑1", "1234567890",
            LocalTime.now(), LocalTime.now(), true,
            "010-123-4567", "테스트용 임시 레스토랑 생성입니다.", "서울시 종로구");

        restaurantRepository.save(restaurant1);
        restaurantRepository.save(restaurant2);

        RestaurantCategory relation1 = new RestaurantCategory(restaurant1, korean);
        relation1.setCategory(korean);
        relation1.setRestaurant(restaurant1);

        RestaurantCategory relation2 = new RestaurantCategory(restaurant2, korean);
        relation2.setCategory(korean);
        relation2.setRestaurant(restaurant2);

        restaurantCategoryRepository.save(relation1);
        restaurantCategoryRepository.save(relation2);

        List<Restaurant> restaurants = categoryService.findRestaurantsByCategoryName(korean.getName());
        assertThat(restaurants.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("새로운 카테고리를 생성할 수 있다.")
    void createCategory() {
        Long categoryId = categoryService.createCategory("양식");

        Category category = categoryService.findCategoryById(categoryId);

        assertThat(category.getName()).isEqualTo("양식");
    }

    @Test
    @DisplayName("카테고리 이름을 변경할 수 있다.")
    void changeCategoryName() {
        Long categoryId = categoryService.createCategory("양식");

        Category oldCategory = categoryService.findCategoryById(categoryId);
        categoryService.changeCategoryName(oldCategory.getName(), "한식");

        Category newCategory = categoryService.findCategoryById(categoryId);
        assertThat(newCategory.getName()).isEqualTo(newCategory.getName());
    }

    @Test
    @DisplayName("카테고리를 삭제할 수 있다.")
    void deleteCategory() {
        Long categoryId = categoryService.createCategory("양식");

        List<Category> beforeDeletion = categoryService.findCategories();
        assertThat(beforeDeletion.size()).isEqualTo(1);

        categoryService.deleteCategory(categoryId);

        List<Category> afterDeletion = categoryService.findCategories();
        assertThat(afterDeletion.size()).isEqualTo(0);
    }

}