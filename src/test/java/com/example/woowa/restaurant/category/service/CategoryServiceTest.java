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
import com.example.woowa.restaurant.owner.repository.OwnerRepository;
import com.example.woowa.restaurant.restaurant.repository.RestaurantRepository;
import com.example.woowa.restaurant.restaurntat_category.repository.RestaurantCategoryRepository;
import com.example.woowa.security.repository.RoleRepository;
import com.example.woowa.security.repository.UserRepository;
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

    private final CategoryMapper categoryMapper = Mappers.getMapper(CategoryMapper.class);

    @Test
    @DisplayName("새로운 카테고리를 생성한다.")
    void testCreateCategory() {
        // Mocked
        CategoryRepository mockedCategoryRepository = mock(CategoryRepository.class);
        CategoryService categoryService = new CategoryService(mockedCategoryRepository, categoryMapper);


        // Given
        CategoryCreateRequest categoryCreateRequest = new CategoryCreateRequest("한식");
        Category korean = makeCategories("한식").get(0);
        CategoryCreateResponse manualConversion = new CategoryCreateResponse(korean.getId(),
            korean.getName(), korean.getCreatedAt());
        when(mockedCategoryRepository.save(any(Category.class))).thenReturn(korean);

        // When
        CategoryCreateResponse result = categoryService.createCategory(categoryCreateRequest);

        // Then
        verify(mockedCategoryRepository, times(1)).save(any(Category.class));
        assertThat(result).usingRecursiveComparison().isEqualTo(manualConversion);
    }

    @Test
    @DisplayName("모든 카테고리를 반환한다.")
    void testFindCategories() {
        // Mocked
        CategoryRepository mockedCategoryRepository = mock(CategoryRepository.class);
        CategoryService categoryService = new CategoryService(mockedCategoryRepository, categoryMapper);


        // Given
        List<Category> testers = makeCategories("한식", "중식");
        List<CategoryFindResponse> manualConversion = testers.stream()
            .map(category ->
                new CategoryFindResponse(category.getId(), category.getName(),
                    category.getCreatedAt(), category.getUpdatedAt()))
            .collect(Collectors.toList());
        when(mockedCategoryRepository.findAll()).thenReturn(testers);

        // When
        List<CategoryFindResponse> result = categoryService.findCategories();

        // Then
        verify(mockedCategoryRepository, times(1)).findAll();
        for (int i = 0; i < result.size(); i++)
            assertThat(result.get(i)).usingRecursiveComparison().isEqualTo(manualConversion.get(i));

    }

    @Test
    @DisplayName("요청한 아이디를 가진 카테고리를 반환한다.")
    void testFindCategoryById() {
        // Mocked
        CategoryRepository mockedCategoryRepository = mock(CategoryRepository.class);
        CategoryService categoryService = new CategoryService(mockedCategoryRepository, categoryMapper);

        // Given
        Category korean = makeCategories("한식").get(0);
        CategoryFindResponse manualConversion =
            new CategoryFindResponse(korean.getId(), korean.getName(), korean.getCreatedAt(), korean.getUpdatedAt());
        when(mockedCategoryRepository.findById(1L)).thenReturn(Optional.of(korean));

        // When
        CategoryFindResponse result = categoryService.findCategoryById(1L);

        // Then
        verify(mockedCategoryRepository, times(1)).findById(anyLong());
        assertThat(result).usingRecursiveComparison().isEqualTo(manualConversion);

    }

    @Test
    @DisplayName("저장된 카테고리명을 요청된 이름으로 변경한다.")
    void testUdateCategoryById() {
        // Given
        CategoryService categoryService = new CategoryService(categoryRepository, categoryMapper);
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
    
    public static List<Category> makeCategories(String... name) {
        return Arrays.stream(name).map(Category::new).collect(Collectors.toList());
    }

}