package com.example.woowa.order.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.entity.CustomerGrade;
import com.example.woowa.customer.customer.service.CustomerService;
import com.example.woowa.customer.voucher.service.VoucherEntityService;
import com.example.woowa.order.order.entity.Cart;
import com.example.woowa.order.order.entity.Order;
import com.example.woowa.order.order.enums.OrderStatus;
import com.example.woowa.order.order.enums.PaymentType;
import com.example.woowa.order.order.repository.OrderRepository;
import com.example.woowa.restaurant.menu.entity.Menu;
import com.example.woowa.restaurant.menu.enums.MenuStatus;
import com.example.woowa.restaurant.menu.service.MenuService;
import com.example.woowa.restaurant.menugroup.entity.MenuGroup;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import com.example.woowa.restaurant.restaurant.service.RestaurantService;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    CustomerService customerService;

    @Mock
    RestaurantService restaurantService;

    @Mock
    VoucherEntityService voucherEntityService;

    @Mock
    MenuService menuService;

    OrderService orderService;

    Order order;
    Customer customer;
    Restaurant restaurant;
    List<Menu> menus;
    List<Cart> carts;


    @BeforeEach
    void init() {
        orderService = new OrderService(orderRepository, customerService, restaurantService,
                voucherEntityService, menuService);
        customer = initCustomer();
        restaurant = initRestaurant();
        menus = initMenus();
        carts = initCarts();
        order = initOrder();
    }

    @Test
    @DisplayName("주문을 생성한다.")
    void addOrderTest() {
        // Given
        PaymentType paymentType = PaymentType.CREDIT_CARD;
        int usePoint = 0;
        Long orderId = 1L;
        Long restaurantId = 1L;

        Long menuId1 = 1L;
        Long menuId2 = 2L;

        Menu menu1 = menus.get(0);
        Menu menu2 = menus.get(1);

        int menu1Quantity = 1;
        int menu2Quantity = 2;

        int beforeDiscountPrice =
                menu1.getPrice() * menu1Quantity + menu2.getPrice() * menu2Quantity;

        given(customerService.findCustomerEntity(any())).willReturn(customer);
        given(restaurantService.findRestaurantById(any())).willReturn(restaurant);
        given(orderRepository.save(any())).willReturn(order);
        given(menuService.findMenuById(menuId2)).willReturn(menu2);
        given(menuService.findMenuById(menuId1)).willReturn(menu1);
        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

        Map<Long, Integer> cartMap = new HashMap<>();
        cartMap.put(menuId1, menu1Quantity);
        cartMap.put(menuId2, menu2Quantity);

        // When
        orderService.addOrder(customer.getLoginId(), restaurantId, null, usePoint, paymentType,
                cartMap);

        // Then
        Order findOrder = orderService.findOrderById(orderId);
        assertThat(findOrder.getBeforeDiscountTotalPrice()).isEqualTo(beforeDiscountPrice);
        assertThat(findOrder.getVoucherDiscountPrice()).isEqualTo(0);
        assertThat(findOrder.getAfterDiscountTotalPrice()).isEqualTo(
                beforeDiscountPrice - usePoint);
        assertThat(findOrder.getOrderStatus()).isEqualTo(OrderStatus.PAYMENT_COMPLETED);
        assertThat(findOrder.getPaymentType()).isEqualTo(paymentType);
        assertThat(findOrder.getUsedPoint()).isEqualTo(usePoint);
        assertThat(findOrder.getCookingTime()).isNull();
    }

    @Test
    @DisplayName("주문을 단건 조회한다.")
    void findOrderByIdTest() {
        // Given
        Long orderId = 1L;
        Order copyOrder = initOrder();

        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

        // When
        Order findOrder = orderService.findOrderById(orderId);

        // Then
        assertThat(findOrder).usingRecursiveComparison().isEqualTo(copyOrder);
    }

    @Test
    @DisplayName("존재하지 않는 주문을 조회하려 하면 예외가 발생한다.")
    void findOrderByNotExistsIdTest() {
        // Given
        Long orderId = 1L;

        given(orderRepository.findById(orderId)).willReturn(Optional.empty());

        // When // Then
        assertThatThrownBy(() -> orderService.findOrderById(orderId))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("특정 레스토랑의 주문 내역을 조회한다.")
    void findOrderByRestaurantTest() {
        // Given
        Long restaurantId = 1L;
        given(restaurantService.findRestaurantById(restaurantId)).willReturn(restaurant);
        given(orderRepository.findByRestaurant(restaurant)).willReturn(
                Collections.singletonList(order));

        // When
        List<Order> orders = orderService.findOrderByRestaurant(restaurantId);

        // Then
        assertThat(orders.size()).isEqualTo(1);
        assertThat(orders).contains(order);
    }

    @Test
    @DisplayName("존재하지 않는 레스토랑의 주문 내역을 조회하려 하면 예외가 발생한다.")
    void findOrderByNotExistsRestaurantTest() {
        // Given
        Long wrongRestaurantId = -1L;
        given(restaurantService.findRestaurantById(wrongRestaurantId)).willThrow(
                IllegalArgumentException.class);

        // When // Then
        assertThatThrownBy(() -> orderService.findOrderByRestaurant(wrongRestaurantId))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("특정 회원의 주문을 조회한다.")
    void findOrderByCustomerTest() {
        // Given
        String loginId = customer.getLoginId();
        given(customerService.findCustomerEntity(loginId)).willReturn(customer);
        given(orderRepository.findByCustomer(customer)).willReturn(
                Collections.singletonList(order));

        // When
        List<Order> orders = orderService.findOrderByCustomer(loginId);

        // Then
        assertThat(orders.size()).isEqualTo(1);
        assertThat(orders).contains(order);
    }

    @Test
    @DisplayName("존재하지 않는 회원의 주문을 조회하려 하면 예외가 발생한다.")
    void findOrderByNotExistsCustomerTest() {
        // Given
        String wrongLoginId = customer.getLoginId();
        given(customerService.findCustomerEntity(wrongLoginId)).willThrow(RuntimeException.class);

        // When // Then
        assertThatThrownBy(() -> orderService.findOrderByCustomer(wrongLoginId))
                .isExactlyInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("주문을 취소한다.")
    void cancelOrderTest() {
        // Given
        Long orderId = 1L;
        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

        // When
        orderService.cancelOrder(orderId);

        // Then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCEL);
    }

    @Test
    @DisplayName("존재하지 않는 주문을 취소하려 하면 예외가 발생한다.")
    void cancelOrderNotExistsIdTest() {
        // Given
        Long wrongOrderId = -1L;
        given(orderRepository.findById(wrongOrderId)).willReturn(Optional.empty());

        // When // Then
        assertThatThrownBy(() -> orderService.cancelOrder(wrongOrderId))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문을 수락한다.")
    void acceptOrderTest() {
        // Given
        Long orderId = 1L;
        int cookingTime = 30;
        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

        // When
        orderService.acceptOrder(orderId, cookingTime);

        // Then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.ACCEPTED);
        assertThat(order.getCookingTime()).isEqualTo(30);
    }

    @Test
    @DisplayName("존재하지 않는 주문을 수락하려 하면 예외가 발생한다.")
    void acceptOrderNotExistsIdTest() {
        // Given
        Long wrongOrderId = -1L;
        int cookingTime = 30;
        given(orderRepository.findById(wrongOrderId)).willReturn(Optional.empty());

        // When // Then
        assertThatThrownBy(() -> orderService.acceptOrder(wrongOrderId, cookingTime))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    private Customer initCustomer() {
        CustomerGrade customerGrade = new CustomerGrade(4, "고마운분", 1000, 1);
        return null;
//        return new Customer("dev12", "Programmers123!", "2000-01-01", customerGrade);
    }

    private Restaurant initRestaurant() {
        return Restaurant.createRestaurant("김밥나라", "000-00-00000",
                LocalTime.of(9, 0, 0), LocalTime.of(23, 0, 0),
                false, "00-000-0000",
                "안녕하세요 저희 김밥나라는 정성을 다해 요리합니다.", "서울 특별시 강남구");
    }

    private List<Cart> initCarts() {
        return List.of(new Cart(menus.get(0), 1), new Cart(menus.get(1), 2));
    }

    private List<Menu> initMenus() {
        MenuGroup menuGroup = MenuGroup.createMenuGroup(restaurant, "김밥류", "김밥류입니다.");
        Menu menu1 = Menu.createMenu(menuGroup, "참치 깁밥", 4000, "맛있는 참치 김밥", false, MenuStatus.SALE);
        Menu menu2 = Menu.createMenu(menuGroup, "계란 깁밥", 4500, "맛있는 계란 김밥", false, MenuStatus.SALE);
        Menu menu3 = Menu.createMenu(menuGroup, "치즈 깁밥", 4500, "맛있는 치즈 김밥", false, MenuStatus.SALE);
        return List.of(menu1, menu2, menu3);
    }

    private Order initOrder() {
        return Order.createOrder(customer, restaurant, null, 0, PaymentType.CREDIT_CARD, carts);
    }
}