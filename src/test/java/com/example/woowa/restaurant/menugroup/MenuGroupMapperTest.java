package com.example.woowa.restaurant.menugroup;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.woowa.restaurant.menugroup.dto.MenuGroupListResponse;
import com.example.woowa.restaurant.menugroup.dto.MenuGroupResponse;
import com.example.woowa.restaurant.menugroup.entity.MenuGroup;
import com.example.woowa.restaurant.menugroup.repository.MenuGroupRepository;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import com.example.woowa.restaurant.restaurant.repository.RestaurantRepository;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuGroupMapperTest {

    @Autowired
    MenuGroupMapper mapper;
    @Autowired
    MenuGroupRepository menuGroupRepository;
    @Autowired
    RestaurantRepository restaurantRepository;
    Restaurant restaurant;

    @BeforeEach
    void init() {
        restaurant = restaurantRepository.save(
                Restaurant.createRestaurant("김밥나라", "000-00-00000",
                        LocalTime.of(9, 0, 0), LocalTime.of(23, 0, 0),
                        false, "00-000-0000",
                        "안녕하세요 저희 김밥나라는 정성을 다해 요리합니다.", "서울 특별시 강남구"));
    }

    @Test
    @DisplayName("MenuGroup entity -> MenuGroupResponse 변환을 테스트한다.")
    void toMenuGroupResponseTest() {
        // Given
        MenuGroup menuGroup = menuGroupRepository.save(
                MenuGroup.createMenuGroup(restaurant, "김밥류", "맛있는 김밥"));

        // When
        MenuGroupResponse response = mapper.toMenuGroupResponse(menuGroup);

        // Then
        assertThat(response).usingRecursiveComparison()
                .comparingOnlyFields("id", "description", "title").isEqualTo(menuGroup);
    }

    @Test
    @DisplayName("menuGroup list -> MenuGroupResponse list 변환을 테스트한다.")
    void toMenuGroupResponseListTest() {
        // Given
        List<MenuGroup> menuGroups = List.of(
                menuGroupRepository.save(
                        MenuGroup.createMenuGroup(restaurant, "김밥류", "맛있는 김밥")),
                menuGroupRepository.save(
                        MenuGroup.createMenuGroup(restaurant, "라면류", "맛있는 라면")),
                menuGroupRepository.save(
                        MenuGroup.createMenuGroup(restaurant, "찌개류", "맛있는 찌개"))
        );

        // When
        List<MenuGroupResponse> response = mapper.toMenuGroupResponseList(menuGroups);

        // Then
        assertThat(response.size()).isEqualTo(menuGroups.size());
    }

    @Test
    @DisplayName("menuGroup list -> MenuGroupResponse 변환을 테스트한다.")
    void toMenuGroupListResponseTest() {
        // Given
        List<MenuGroup> menuGroups = List.of(
                menuGroupRepository.save(
                        MenuGroup.createMenuGroup(restaurant, "김밥류", "맛있는 김밥")),
                menuGroupRepository.save(
                        MenuGroup.createMenuGroup(restaurant, "라면류", "맛있는 라면")),
                menuGroupRepository.save(
                        MenuGroup.createMenuGroup(restaurant, "찌개류", "맛있는 찌개"))
        );

        // When
        MenuGroupListResponse response = mapper.toMenuGroupListResponse(menuGroups);

        // Then
        assertThat(response.getMenuGroups().size()).isEqualTo(menuGroups.size());
    }
}