package com.example.woowa.restaurant.menu.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.woowa.restaurant.menu.dto.MenuResponse;
import com.example.woowa.restaurant.menu.dto.MenuSaveRequest;
import com.example.woowa.restaurant.menu.entity.Menu;
import com.example.woowa.restaurant.menu.enums.MenuStatus;
import com.example.woowa.restaurant.menu.repository.MenuRepository;
import com.example.woowa.restaurant.menugroup.entity.MenuGroup;
import com.example.woowa.restaurant.menugroup.repository.MenuGroupRepository;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import com.example.woowa.restaurant.restaurant.repository.RestaurantRepository;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuMapperTest {

    @Autowired
    MenuMapper mapper;
    @Autowired
    MenuGroupRepository menuGroupRepository;
    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    MenuRepository menuRepository;

    Restaurant restaurant;
    MenuGroup menuGroup;

    @BeforeEach
    void init() {
        restaurant = restaurantRepository.save(
                Restaurant.createRestaurant("김밥나라", "000-00-00000",
                        LocalTime.of(9, 0, 0), LocalTime.of(23, 0, 0),
                        false, "00-000-0000",
                        "안녕하세요 저희 김밥나라는 정성을 다해 요리합니다.", "서울 특별시 강남구"));

        menuGroup = menuGroupRepository.save(
                MenuGroup.createMenuGroup(restaurant, "김밥류", "맛있는 김밥"));
    }

    @Test
    @DisplayName("Menu entity -> MenuResponse 변환을 테스트 한다.")
    void toMenuResponseTest() {
        // Given
        Menu menu = menuRepository.save(
                Menu.createMenu(menuGroup, "참치 김밥", 4500, "맛있는 참치 김밥", false, MenuStatus.SALE));

        // When
        MenuResponse menuResponse = mapper.toMenuResponse(menu);

        // Then
        assertThat(menuResponse.getId()).isEqualTo(menu.getId());
        assertThat(menuResponse.getTitle()).isEqualTo(menu.getTitle());
        assertThat(menuResponse.getDescription()).isEqualTo(menu.getDescription());
        assertThat(menuResponse.getPrice()).isEqualTo(menu.getPrice());
    }

    @Test
    @DisplayName("MenuSaveRequest -> Menu entity 변환을 테스트 한다.")
    void toMenuTest() {
        // Given
        Long menuGroupId = menuGroup.getId();
        String title = "참치 김밥";
        String description = "맛있는 참치 김밥";
        int price = 4500;

        // When
        MenuSaveRequest request = new MenuSaveRequest(menuGroupId, title, description, price);
        Menu menu = mapper.toMenu(request);

        // Then
        assertThat(menu.getMenuGroup()).isEqualTo(menuGroup);
        assertThat(menu.getTitle()).isEqualTo(title);
        assertThat(menu.getDescription()).isEqualTo(description);
        assertThat(menu.getPrice()).isEqualTo(price);
        assertThat(menu.getIsMain()).isEqualTo(false);
        assertThat(menu.getMenuStatus()).isEqualTo(MenuStatus.SALE);

    }
}