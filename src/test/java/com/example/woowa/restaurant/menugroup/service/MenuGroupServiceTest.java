package com.example.woowa.restaurant.menugroup.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.example.woowa.restaurant.menugroup.MenuGroupMapperImpl;
import com.example.woowa.restaurant.menugroup.dto.MenuGroupListResponse;
import com.example.woowa.restaurant.menugroup.dto.MenuGroupResponse;
import com.example.woowa.restaurant.menugroup.dto.MenuGroupSaveRequest;
import com.example.woowa.restaurant.menugroup.dto.MenuGroupUpdateRequest;
import com.example.woowa.restaurant.menugroup.entity.MenuGroup;
import com.example.woowa.restaurant.menugroup.repository.MenuGroupRepository;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import com.example.woowa.restaurant.restaurant.service.RestaurantService;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    MenuGroupRepository menuGroupRepository;

    @Mock
    RestaurantService restaurantService;

    MenuGroupService menuGroupService;

    Restaurant restaurant;

    MenuGroup menuGroup;

    MenuGroupMapperImpl mapper;

    @BeforeEach
    void init() {
        mapper = new MenuGroupMapperImpl();
        menuGroupService = new MenuGroupService(menuGroupRepository, restaurantService,
                mapper);

        String name = "김밥나라";
        String businessNumber = "000-00-00000";
        LocalTime openingTime = LocalTime.of(9, 0, 0);
        LocalTime closingTime = LocalTime.of(23, 0, 0);
        String phoneNumber = "00-000-0000";
        String description = "안녕하세요 저희 김밥나라는 정성을 다해 요리합니다.";
        String address = "서울 특별시 강남구";

        restaurant = Restaurant.createRestaurant(name, businessNumber, openingTime, closingTime,
                false, phoneNumber,
                description, address);

        menuGroup = MenuGroup.createMenuGroup(restaurant, "김밥류", "국내산 쌀 사용");
    }

    @Test
    @DisplayName("특정 레스토랑의 메뉴그룹을 조회한다.")
    void findMenuGroupByRestaurantTest() {
        // Given
        Long restaurantId = 1L;
        given(restaurantService.findRestaurantById(restaurantId)).willReturn(restaurant);
        given(menuGroupRepository.findByRestaurant(restaurant)).willReturn(
                Collections.singletonList(menuGroup));

        // When
        MenuGroupListResponse response = menuGroupService.findMenuGroupByRestaurant(restaurantId);

        // Then
        assertThat(response.getMenuGroups().size()).isEqualTo(1);
        assertThat(response.getMenuGroups()).usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(Collections.singletonList(mapper.toMenuGroupResponse(menuGroup)));
    }

    @Test
    @DisplayName("메뉴그룹을 저장한다.")
    void addMenuGroupTest() {
        // Given
        Long restaurantId = 1L;

        given(menuGroupRepository.save(any())).willReturn(menuGroup);
        given(restaurantService.findRestaurantById(restaurantId)).willReturn(restaurant);

        // When
        menuGroupService.addMenuGroup(restaurantId, new MenuGroupSaveRequest(menuGroup.getTitle(),
                menuGroup.getDescription()));

        // Then
        then(menuGroupRepository).should().save(any());
    }

    @Test
    @DisplayName("존재하지 않는 레스토랑에 메뉴그룹을 추가하려 하면 예외가 발생한다.")
    void addMenuGroupNotExistsRestaurantTest() {
        // Given
        Long wrongRestaurantId = -1L;
        when(restaurantService.findRestaurantById(wrongRestaurantId)).thenThrow(
                IllegalArgumentException.class);

        // When // Then
        assertThatThrownBy(
                () -> menuGroupService.addMenuGroup(wrongRestaurantId,
                        new MenuGroupSaveRequest(menuGroup.getTitle(),
                                menuGroup.getDescription())))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴그룹을 ID로 단건 조회한다.")
    void findMenuGroupById() {
        // Given
        Long menuGroupId = 1L;
        when(menuGroupRepository.findById(menuGroupId)).thenReturn(Optional.of(menuGroup));

        // When
        MenuGroupResponse response = menuGroupService.findMenuById(menuGroupId);

        // Then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(mapper.toMenuGroupResponse(menuGroup));
    }

    @Test
    @DisplayName("존재하지 않는 ID로 메뉴 그룹을 조회하면 예외가 발생한다.")
    void findMenuGroupByNotExistsIdTest() {
        // Given
        Long wrongMenuGroupId = -1L;
        when(menuGroupRepository.findById(wrongMenuGroupId)).thenReturn(Optional.empty());

        // When // Then
        assertThatThrownBy(() -> menuGroupService.findMenuById(wrongMenuGroupId))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴그룹 정보를 업데이트 한다.")
    void updateMenuGroupTest() {
        // Given
        Long menuGroupId = 1L;
        String newTitle = "찌개류";
        String newDescription = "";
        given(menuGroupRepository.findById(menuGroupId)).willReturn(Optional.of(menuGroup));

        // When
        menuGroupService.updateMenuGroup(menuGroupId,
                new MenuGroupUpdateRequest(newTitle, newDescription));

        // Then
        MenuGroup findMenuGroup = menuGroupService.findMenuGroupEntityById(menuGroupId);
        assertThat(findMenuGroup.getTitle()).isEqualTo(newTitle);
        assertThat(findMenuGroup.getDescription()).isEqualTo(null);
    }

    @Test
    @DisplayName("존재하지 않는 메뉴그룹을 업데이트하려 하면 예외가 발생한다.")
    void updateMenuGroupNotExistsIdTest() {
        // Given
        Long wrongMenuGroupId = -1L;
        String newTitle = "찌개류";
        String newDescription = "";
        when(menuGroupRepository.findById(wrongMenuGroupId)).thenReturn(Optional.empty());

        // When // Then
        assertThatThrownBy(
                () -> menuGroupService.updateMenuGroup(wrongMenuGroupId,
                        new MenuGroupUpdateRequest(newTitle, newDescription)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴그룹을 삭제한다.")
    void deleteMenuGroupTest() {
        // Given
        Long menuGroupId = 1L;
        given(menuGroupRepository.findById(menuGroupId)).willReturn(Optional.of(menuGroup));

        // When
        menuGroupService.deleteMenuGroup(menuGroupId);

        // Then
        then(menuGroupRepository).should().delete(menuGroup);
    }

    @Test
    @DisplayName("존재하지 않는 메뉴그룹을 삭제하려 하면 예외가 발생한다.")
    void deleteMenuGroupNotExistsIdTest() {
        // Given
        Long wrongMenuGroupId = -1L;
        given(menuGroupRepository.findById(wrongMenuGroupId)).willReturn(Optional.empty());

        // When // Then
        assertThatThrownBy(() -> menuGroupService.deleteMenuGroup(wrongMenuGroupId))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}