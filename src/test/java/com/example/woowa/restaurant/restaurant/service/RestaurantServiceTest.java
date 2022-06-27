package com.example.woowa.restaurant.restaurant.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.woowa.restaurant.menu.entity.Menu;
import com.example.woowa.restaurant.menu.enums.MenuStatus;
import com.example.woowa.restaurant.menugroup.entity.MenuGroup;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import com.example.woowa.restaurant.restaurant.repository.RestaurantRepository;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    RestaurantRepository restaurantRepository;

    RestaurantService restaurantService;

    Restaurant restaurant;

    Long restaurantId = 1L;

    @BeforeEach
    void beforeEach() {
        restaurantService = new RestaurantService(restaurantRepository);

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

        MenuGroup menuGroup1 = MenuGroup.createMenuGroup(restaurant, "밥", null);
        MenuGroup menuGroup2 = MenuGroup.createMenuGroup(restaurant, "면", null);

        Menu.createMenu(menuGroup1, "김치 볶음밥", 10000, "맛있어요", true, MenuStatus.SALE);
        Menu.createMenu(menuGroup1, "비빔밥", 10000, "맛있어요", true, MenuStatus.SALE);

        Menu.createMenu(menuGroup2, "라면", 10000, "맛있어요", true, MenuStatus.SALE);
        Menu.createMenu(menuGroup2, "국수", 10000, "맛있어요", true, MenuStatus.SALE);
    }

    @Test
    @DisplayName("ID 값으로 레스토랑을 단건 조회한다.")
    void findRestaurantByIdTest() {
        // Given
        Mockito.when(restaurantRepository.findById(restaurantId))
                .thenReturn(Optional.of(restaurant));

        // When
        Restaurant findRestaurant = restaurantService.findRestaurantById(restaurantId);

        // Then
        assertThat(findRestaurant).usingRecursiveComparison().isEqualTo(restaurant);
    }

    @Test
    @DisplayName("존재하지 않는 ID 값으로 레스토랑을 단건 조회하면 예외가 발생한다.")
    void findRestaurantByIdNotExistsIdTest() {
        // Given
        Long wrongRestaurantId = -1L;
        Mockito.when(restaurantRepository.findById(wrongRestaurantId))
                .thenReturn(Optional.empty());

        // When // Then
        assertThatThrownBy(
                () -> restaurantService.findRestaurantById(wrongRestaurantId)).isExactlyInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("인자로 받은 ID 값의 레스토랑을 삭제한다.")
    void removeRestaurantTest() {
        // Given
        Mockito.when(restaurantRepository.findById(restaurantId))
                .thenReturn(Optional.of(restaurant));

        // When
        restaurantService.removeRestaurant(restaurantId);

        // Then
        Mockito.verify(restaurantRepository).delete(restaurant);
    }

    @Test
    @DisplayName("존재하지 않는 ID의 레스토랑을 삭제하려 하면 예외가 발생한다.")
    void removeRestaurantNotExistsIdTest() {
        // Given
        Long wrongRestaurantId = -1L;
        Mockito.when(restaurantRepository.findById(wrongRestaurantId))
                .thenReturn(Optional.empty());

        // When // Then
        assertThatThrownBy(() -> restaurantService.removeRestaurant(wrongRestaurantId))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("가게 영업시간을 변경한다.")
    void updateBusinessHourTest() {
        // Given
        LocalTime newOpeningTime = LocalTime.of(6, 0, 0);
        LocalTime newClosingTime = LocalTime.of(22, 0, 0);

        Mockito.when(restaurantRepository.findById(restaurantId))
                .thenReturn(Optional.of(restaurant));

        // When
        restaurantService.updateBusinessHours(restaurantId, newOpeningTime, newClosingTime);

        // Then
        Restaurant findRestaurant = restaurantService.findRestaurantById(restaurantId);
        assertThat(findRestaurant.getOpeningTime()).isEqualTo(newOpeningTime);
        assertThat(findRestaurant.getClosingTime()).isEqualTo(newClosingTime);
    }

    @Test
    @DisplayName("오픈/클로즈 시간을 같은 시간으로 변경하려 하면 예외가 발생한다.")
    void updateBusinessHourFailTest() {
        // Given
        LocalTime newOpeningTime = LocalTime.of(10, 0, 0);
        LocalTime newClosingTime = LocalTime.of(10, 0, 0);

        Mockito.when(restaurantRepository.findById(restaurantId))
                .thenReturn(Optional.of(restaurant));

        // When // Then
        assertThatThrownBy(() -> restaurantService.updateBusinessHours(restaurantId, newOpeningTime,
                newClosingTime))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 ID의 가게 영업시간을 변경하려 하면 예외가 발생한다.")
    void updateBusinessHourNotExistsIdTest() {
        // Given
        Long wrongRestaurantId = -1L;
        LocalTime newOpeningTime = LocalTime.of(6, 0, 0);
        LocalTime newClosingTime = LocalTime.of(22, 0, 0);

        Mockito.when(restaurantRepository.findById(wrongRestaurantId))
                .thenReturn(Optional.empty());

        // When // Then
        assertThatThrownBy(
                () -> restaurantService.updateBusinessHours(wrongRestaurantId, newOpeningTime,
                        newClosingTime))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("가게 상태를 영업중으로 변경한다.")
    void openRestaurantTest() {
        // Given
        Mockito.when(restaurantRepository.findById(restaurantId))
                .thenReturn(Optional.of(restaurant));

        // When
        restaurantService.openRestaurant(restaurantId);

        // Then
        Restaurant findRestaurant = restaurantService.findRestaurantById(restaurantId);
        assertThat(findRestaurant.getIsOpen()).isTrue();
    }

    @Test
    @DisplayName("가게 상태를 영업 종료로 변경한다.")
    void closeRestaurantTest() {
        // Given
        Mockito.when(restaurantRepository.findById(restaurantId))
                .thenReturn(Optional.of(restaurant));

        // When
        restaurantService.closeRestaurant(restaurantId);

        // Then
        Restaurant findRestaurant = restaurantService.findRestaurantById(restaurantId);
        assertThat(findRestaurant.getIsOpen()).isFalse();
    }

    @Test
    @DisplayName("존재하지 않는 ID의 가게 영업 상태를 변경하려 하면 예외가 발생한다.")
    void changeRestaurantStatusNotExistsIdTest() {
        // Given
        Long wrongRestaurantId = -1L;

        Mockito.when(restaurantRepository.findById(wrongRestaurantId))
                .thenReturn(Optional.empty());

        // When // Then
        assertThatThrownBy(() -> restaurantService.openRestaurant(wrongRestaurantId))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("가게 설명을 수정한다.")
    void changeRestaurantDescriptionTest() {
        // Given
        String newDescription = "안녕하세요 김밥나라입니다.";

        Mockito.when(restaurantRepository.findById(restaurantId))
                .thenReturn(Optional.of(restaurant));

        // When
        restaurantService.changeRestaurantDescription(restaurantId, newDescription);

        // Then
        Restaurant findRestaurant = restaurantService.findRestaurantById(restaurantId);
        assertThat(findRestaurant.getDescription()).isEqualTo(newDescription);
    }

    @Test
    @DisplayName("존재하지 않는 ID의 가게 설명을 수정하려 하면 예외가 발생한다.")
    void changeRestaurantDescriptionNotExistsIdTest() {
        // Given
        String newDescription = "안녕하세요 김밥나라입니다.";
        Long wrongRestaurantId = -1L;

        Mockito.when(restaurantRepository.findById(wrongRestaurantId))
                .thenReturn(Optional.empty());

        // When // Then
        assertThatThrownBy(
                () -> restaurantService.changeRestaurantDescription(wrongRestaurantId,
                        newDescription))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("가게 전화번호를 변경한다.")
    void changeRestaurantPhoneNumberTest() {
        // Given
        String newPhoneNumber = "02-111-2222";

        Mockito.when(restaurantRepository.findById(restaurantId))
                .thenReturn(Optional.of(restaurant));

        // When
        restaurantService.changeRestaurantPhoneNumber(restaurantId, newPhoneNumber);

        // Then
        Restaurant findRestaurant = restaurantService.findRestaurantById(restaurantId);
        assertThat(findRestaurant.getPhoneNumber()).isEqualTo(newPhoneNumber);
    }

    @Test
    @DisplayName("존재하지 않는 ID의 가게 전화번호를 변경하려 하면 예외가 발생한다.")
    void changeRestaurantPhoneNumberNotExistsIdTest() {
        // Given
        String newPhoneNumber = "02-111-2222";
        Long wrongRestaurantId = -1L;

        Mockito.when(restaurantRepository.findById(wrongRestaurantId))
                .thenReturn(Optional.empty());

        // When // Then
        assertThatThrownBy(() -> restaurantService.changeRestaurantPhoneNumber(wrongRestaurantId,
                newPhoneNumber))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}