package com.example.woowa.order.order.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.woowa.order.order.dto.cart.CartResponse;
import com.example.woowa.order.order.dto.cart.CartSaveRequest;
import com.example.woowa.order.order.dto.cart.CartSummeryResponse;
import com.example.woowa.order.order.entity.Cart;
import com.example.woowa.restaurant.menu.entity.Menu;
import com.example.woowa.restaurant.menu.enums.MenuStatus;
import com.example.woowa.restaurant.menu.repository.MenuRepository;
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
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CartMapperTest {


    @Autowired
    CartMapper mapper;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    MenuGroupRepository menuGroupRepository;
    @Autowired
    RestaurantRepository restaurantRepository;

    Menu menu1, menu2;
    MenuGroup menuGroup;

    @BeforeEach
    void init() {
        String name = "김밥나라";
        String businessNumber = "000-00-00000";
        LocalTime openingTime = LocalTime.of(9, 0, 0);
        LocalTime closingTime = LocalTime.of(23, 0, 0);
        String phoneNumber = "00-000-0000";
        String description = "안녕하세요 저희 김밥나라는 정성을 다해 요리합니다.";
        String address = "서울 특별시 강남구";

        Restaurant restaurant = restaurantRepository.save(
                Restaurant.createRestaurant(name, businessNumber, openingTime,
                        closingTime,
                        false, phoneNumber,
                        description, address));

        menuGroup = menuGroupRepository.save(
                MenuGroup.createMenuGroup(restaurant, "김밥류", "맛잇는 김밥"));
        menu1 = menuRepository.save(
                Menu.createMenu(menuGroup, "참치 김밥", 4500, "맛있는 참치 김밥", true, MenuStatus.SALE));
        menu2 = menuRepository.save(
                Menu.createMenu(menuGroup, "치즈 김밥", 5000, "맛있는 치즈 김밥", true, MenuStatus.SALE));
    }

    @Test
    @DisplayName("Cart entity -> CartResponse 변환을 테스트한다.")
    void toCartResponseTest() {
        // Given
        Cart cart = new Cart(menu1, 3);

        // When
        CartResponse response = mapper.toCartResponse(cart);

        // Then
        assertThat(response.getMenuName()).isEqualTo(cart.getMenu().getTitle());
        assertThat(response.getQuantity()).isEqualTo(cart.getQuantity());
        assertThat(response.getTotalPrice())
                .isEqualTo(cart.getMenu().getPrice() * cart.getQuantity());

    }

    @Test
    @DisplayName("Cart entity -> CartSummeryResponse 변환을 테스트한다.")
    void toCartSummeryResponseTest() {
        // Given
        Cart cart = new Cart(menu1, 3);

        // When
        CartSummeryResponse response = mapper.toCartSummeryResponse(cart);

        // Then
        assertThat(response.getMenuTitle()).isEqualTo(cart.getMenu().getTitle());
        assertThat(response.getQuantity()).isEqualTo(cart.getQuantity());
    }

    @Test
    @DisplayName("Cart entity list -> CartResponse list 변환을 테스트한다.")
    void toCartResponseListTest() {
        // Given
        List<Cart> carts = List.of(
                new Cart(menu1, 3),
                new Cart(menu2, 2)
        );

        // When
        List<CartResponse> responses = mapper.toCartResponseList(carts);

        // Then
        assertThat(responses.size()).isEqualTo(carts.size());
    }

    @Test
    @DisplayName("CartSaveRequest -> Cart entity 변환을 테스트한다.")
    void toCartTest() {
        // Given
        CartSaveRequest request = new CartSaveRequest(menu1.getId(), 3);

        // When
        Cart cart = mapper.toCart(request);

        // Then
        assertThat(cart.getMenu().getId()).isEqualTo(request.getMenuId());
        assertThat(cart.getQuantity()).isEqualTo(request.getQuantity());
    }

    @Test
    @DisplayName("CartSaveRequest list -> Cart list 변환을 테스트한다.")
    void toCartListTest() {
        // Given
        List<CartSaveRequest> requests = List.of(
                new CartSaveRequest(menu1.getId(), 3),
                new CartSaveRequest(menu2.getId(), 5)
        );

        // When
        List<Cart> carts = mapper.toCartList(requests);

        // Then
        assertThat(carts.size()).isEqualTo(requests.size());
    }
}