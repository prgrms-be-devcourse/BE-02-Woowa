package com.example.woowa.restaurant.category.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.woowa.restaurant.category.dto.request.CategoryCreateRequest;
import com.example.woowa.restaurant.category.dto.request.CategoryUpdateRequest;
import com.example.woowa.restaurant.category.dto.response.CategoryCreateResponse;
import com.example.woowa.restaurant.category.dto.response.CategoryFindResponse;
import com.example.woowa.restaurant.category.entity.Category;
import com.example.woowa.restaurant.category.mapper.CategoryMapper;
import com.example.woowa.restaurant.category.repository.CategoryRepository;
import com.example.woowa.restaurant.owner.dto.request.OwnerCreateRequest;
import com.example.woowa.restaurant.owner.dto.response.OwnerCreateResponse;
import com.example.woowa.restaurant.owner.mapper.OwnerMapper;
import com.example.woowa.restaurant.owner.repository.OwnerRepository;
import com.example.woowa.restaurant.owner.service.OwnerService;
import com.example.woowa.restaurant.restaurant.dto.request.RestaurantCreateRequest;
import com.example.woowa.restaurant.restaurant.dto.response.RestaurantFindResponse;
import com.example.woowa.restaurant.restaurant.mapper.RestaurantMapper;
import com.example.woowa.restaurant.restaurant.repository.RestaurantRepository;
import com.example.woowa.restaurant.restaurant.service.RestaurantService;
import com.example.woowa.restaurant.restaurntat_category.repository.RestaurantCategoryRepository;
import com.example.woowa.security.repository.RoleRepository;
import com.example.woowa.security.repository.UserRepository;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class CategoryServiceTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    RestaurantCategoryRepository restaurantCategoryRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    private CategoryMapper categoryMapper = Mappers.getMapper(CategoryMapper.class);

    private RestaurantMapper restaurantMapper = Mappers.getMapper(RestaurantMapper.class);

    private OwnerMapper ownerMapper = Mappers.getMapper(OwnerMapper.class);

    @Test
    @DisplayName("새로운 카테고리를 생성한다.")
    void testCreateCategory() {
        // Mocked
        CategoryRepository mockedCategoryRepository = mock(CategoryRepository.class);
        CategoryService categoryService = new CategoryService(mockedCategoryRepository, categoryMapper, null);

        // Given
        CategoryCreateRequest categoryCreateRequest = new CategoryCreateRequest("한식");
        Category korean = makeCategories("한식").get(0);
        CategoryCreateResponse manualConversion = new CategoryCreateResponse(korean.getId(),
            korean.getName(), korean.getCreatedAt());

        // When
        when(mockedCategoryRepository.save(any(Category.class))).thenReturn(korean);
        CategoryCreateResponse category = categoryService.createCategory(categoryCreateRequest);

        // Then
        verify(mockedCategoryRepository, times(1)).save(any(Category.class));
        assertThat(category).usingRecursiveComparison().isEqualTo(manualConversion);
    }

    @Test
    @DisplayName("모든 카테고리를 반환한다.")
    void testFindCategories() {
        // Mocked
        CategoryRepository mockedCategoryRepository = mock(CategoryRepository.class);
        CategoryService categoryService = new CategoryService(mockedCategoryRepository, categoryMapper, null);

        // Given
        List<Category> testers = makeCategories("한식", "중식");
        List<CategoryFindResponse> manualConversion = testers.stream()
            .map(category ->
                new CategoryFindResponse(category.getId(), category.getName(),
                    category.getCreatedAt(), category.getUpdatedAt()))
            .collect(Collectors.toList());

        // When
        when(mockedCategoryRepository.findAll()).thenReturn(testers);
        List<CategoryFindResponse> categories = categoryService.findCategories();

        // Then
        verify(mockedCategoryRepository, times(1)).findAll();
        for (int i = 0; i < categories.size(); i++)
            assertThat(categories.get(i)).usingRecursiveComparison().isEqualTo(manualConversion.get(i));
    }

    @Test
    @DisplayName("요청한 아이디를 가진 카테고리를 반환한다.")
    void testFindCategoryById() {
        // Mocked
        CategoryRepository mockedCategoryRepository = mock(CategoryRepository.class);
        CategoryService categoryService = new CategoryService(mockedCategoryRepository, categoryMapper, null);

        // Given
        Category korean = makeCategories("한식").get(0);
        CategoryFindResponse manualConversion =
            new CategoryFindResponse(korean.getId(), korean.getName(), korean.getCreatedAt(), korean.getUpdatedAt());

        // When
        when(mockedCategoryRepository.findById(1L)).thenReturn(Optional.of(korean));
        CategoryFindResponse category = categoryService.findCategoryById(1L);

        // Then
        verify(mockedCategoryRepository, times(1)).findById(anyLong());
        assertThat(category).usingRecursiveComparison().isEqualTo(manualConversion);
    }

    @Test
    @DisplayName("특정 카테고리명을 가진 모든 가게를 해당 카테고리 아이디로 조회할 수 있다.")
    void testFindRestaurantsByCategoryId() {
        // Given
        CategoryService categoryService = new CategoryService(categoryRepository, categoryMapper,
            restaurantMapper);
        CategoryCreateResponse korean = categoryService.createCategory(
            new CategoryCreateRequest("한식"));
        CategoryCreateResponse chinese = categoryService.createCategory(
            new CategoryCreateRequest("중식"));

        RestaurantService restaurantService = new RestaurantService(restaurantRepository, restaurantCategoryRepository,
            categoryService, restaurantMapper);
        OwnerService ownerService = new OwnerService(roleRepository, userRepository, ownerRepository, restaurantService, ownerMapper, restaurantMapper, new BCryptPasswordEncoder());
        OwnerCreateResponse owner = ownerService.createOwner(
            new OwnerCreateRequest("abcdefg12345", "aA1234567890", "오너", "010-1111-2222"));

            ownerService.registerRestaurant(owner.getId(), new RestaurantCreateRequest("a",
                "1234567890",
                LocalTime.now(),
                LocalTime.now().plusHours(1),
                true,
                "010-3333-4444",
                "test1",
                "서울시 종로구",
                List.of(korean.getId(), chinese.getId())));
            ownerService.registerRestaurant(owner.getId(), new RestaurantCreateRequest("b",
                "1234567890",
                LocalTime.now(),
                LocalTime.now().plusHours(1),
                true,
                "010-5555-6666",
                "test2",
                "서울시 종로구",
                List.of(korean.getId())));

        // When
        List<RestaurantFindResponse> koreanRestaurants = categoryService.findRestaurantsByCategoryId(
            korean.getId());
        List<RestaurantFindResponse> chineseRestaurants = categoryService.findRestaurantsByCategoryId(
            chinese.getId());

        // Then
        assertThat(koreanRestaurants.size()).isEqualTo(2);
        assertThat(chineseRestaurants.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("저장된 카테고리명을 요청된 이름으로 변경한다.")
    void testChangeCategoryName() {
        // Given
        CategoryService categoryService = new CategoryService(categoryRepository, categoryMapper, null);
        CategoryCreateResponse beforeUpdating = categoryService.createCategory(
            new CategoryCreateRequest("한식"));

        // When
        CategoryUpdateRequest categoryUpdateRequest = new CategoryUpdateRequest("중식");
        categoryService.updateCategoryById(beforeUpdating.getId(), categoryUpdateRequest);
        CategoryFindResponse afterUpdating = categoryService.findCategoryById(
            beforeUpdating.getId());

        // Then
        assertThat(afterUpdating.getId()).isEqualTo(beforeUpdating.getId());
        assertThat(afterUpdating.getName()).isEqualTo(categoryUpdateRequest.getName());
    }

    static List<Category> makeCategories(String... name) {
        return Arrays.stream(name).map(Category::new).collect(Collectors.toList());
    }

}