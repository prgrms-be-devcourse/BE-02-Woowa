package com.example.woowa.order.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.entity.CustomerGrade;
import com.example.woowa.customer.customer.service.CustomerService;
import com.example.woowa.customer.voucher.entity.Voucher;
import com.example.woowa.customer.voucher.enums.EventType;
import com.example.woowa.customer.voucher.enums.VoucherType;
import com.example.woowa.customer.voucher.service.VoucherEntityService;
import com.example.woowa.delivery.entity.Delivery;
import com.example.woowa.delivery.enums.DeliveryStatus;
import com.example.woowa.delivery.service.DeliveryAreaService;
import com.example.woowa.delivery.service.DeliveryEntityService;
import com.example.woowa.order.order.dto.cart.CartResponse;
import com.example.woowa.order.order.dto.cart.CartSaveRequest;
import com.example.woowa.order.order.dto.cart.CartSummeryResponse;
import com.example.woowa.order.order.dto.customer.OrderCustomerResponse;
import com.example.woowa.order.order.dto.customer.OrderListCustomerRequest;
import com.example.woowa.order.order.dto.customer.OrderListCustomerResponse;
import com.example.woowa.order.order.dto.customer.OrderSaveRequest;
import com.example.woowa.order.order.dto.customer.OrderSummeryResponse;
import com.example.woowa.order.order.dto.restaurant.OrderAcceptRequest;
import com.example.woowa.order.order.dto.restaurant.OrderListRestaurantRequest;
import com.example.woowa.order.order.dto.restaurant.OrderListRestaurantResponse;
import com.example.woowa.order.order.dto.restaurant.OrderRestaurantResponse;
import com.example.woowa.order.order.dto.statistics.OrderStatistics;
import com.example.woowa.order.order.dto.statistics.OrderStatisticsRequest;
import com.example.woowa.order.order.dto.statistics.OrderStatisticsResponse;
import com.example.woowa.order.order.entity.Cart;
import com.example.woowa.order.order.entity.Order;
import com.example.woowa.order.order.enums.OrderStatus;
import com.example.woowa.order.order.enums.PaymentType;
import com.example.woowa.order.order.mapper.CartMapper;
import com.example.woowa.order.order.mapper.OrderMapper;
import com.example.woowa.order.order.repository.OrderRepository;
import com.example.woowa.restaurant.menu.entity.Menu;
import com.example.woowa.restaurant.menu.enums.MenuStatus;
import com.example.woowa.restaurant.menu.service.MenuService;
import com.example.woowa.restaurant.menugroup.entity.MenuGroup;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import com.example.woowa.restaurant.restaurant.service.RestaurantService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

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
    DeliveryAreaService deliveryAreaService;

    @Mock
    MenuService menuService;

    @Mock
    DeliveryEntityService deliveryEntityService;
    @Mock
    CartMapper cartMapper;
    @Mock
    OrderMapper orderMapper;
    ;

    OrderService orderService;

    Order order;
    Customer customer;
    Restaurant restaurant;
    List<Menu> menus;
    List<Cart> carts;

    Delivery delivery;
    Voucher voucher;
    List<CartResponse> cartResponses;

    @BeforeEach
    void init() {
        orderService = new OrderService(orderRepository, customerService, restaurantService,
                voucherEntityService, deliveryAreaService, deliveryEntityService,
                orderMapper, cartMapper);
        customer = initCustomer();
        restaurant = initRestaurant();
        menus = initMenus();
        carts = initCarts();
        order = initOrder();
        delivery = initDelivery(order);
        voucher = initVoucher();
        cartResponses = initCartResponses();
    }

    @Test
    @DisplayName("주문을 생성한다.")
    void addOrderTest() {
        // Given
        PaymentType paymentType = PaymentType.CREDIT_CARD;
        int usePoint = 0;
        Long orderId = 1L;
        Long restaurantId = 1L;
        int deliveryFee = 3000;

        Long menuId1 = 1L;
        Long menuId2 = 2L;
        Long voucherId = 1L;

        Menu menu1 = menus.get(0);
        Menu menu2 = menus.get(1);

        int orderPrice =
                menu1.getPrice() * 1 + menu2.getPrice() * 2;

        List<CartSaveRequest> cartSaveRequests = List.of(
                new CartSaveRequest(menuId1, 1),
                new CartSaveRequest(menuId2, 2)
        );

        given(customerService.findCustomerEntity(any())).willReturn(customer);
        given(restaurantService.findRestaurantById(any())).willReturn(restaurant);
        given(orderRepository.save(any())).willReturn(order);
        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));
        given(deliveryAreaService.getDeliveryFee(restaurant, "서울특별시 강남구")).willReturn(deliveryFee);
        given(voucherEntityService.findVoucherById(voucherId)).willReturn(voucher);
        given(cartMapper.toCart(cartSaveRequests.get(0))).willReturn(carts.get(0));
        given(cartMapper.toCart(cartSaveRequests.get(1))).willReturn(carts.get(1));

        OrderSaveRequest orderSaveRequest = new OrderSaveRequest(customer.getLoginId(),
                restaurantId, voucherId, usePoint, paymentType, "서울특별시 강남구",
                cartSaveRequests
        );

        // When
        orderService.addOrder(orderSaveRequest);

        // Then
        Order findOrder = orderService.findOrderById(orderId);
        assertThat(findOrder.getOrderPrice()).isEqualTo(orderPrice);
        assertThat(findOrder.getVoucherDiscountPrice()).isEqualTo(0);
        assertThat(findOrder.getAfterDiscountTotalPrice()).isEqualTo(
                orderPrice + deliveryFee - usePoint);
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
        initDelivery(copyOrder);

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
        LocalDate from = LocalDate.now().minusMonths(1);
        LocalDate end = LocalDate.now();
        PageRequest pageable = PageRequest.of(0, 3);

        given(restaurantService.findRestaurantById(restaurantId)).willReturn(restaurant);
        Slice<Order> slice = new SliceImpl(Collections.singletonList(order), pageable,
                false);

        List<OrderRestaurantResponse> orderRestaurantResponses = List.of(
                new OrderRestaurantResponse(order.getCreatedAt(), cartResponses,
                        order.getOrderPrice(), order.getDeliveryFee(),
                        order.getAfterDiscountTotalPrice(),
                        order.getVoucherDiscountPrice() + order.getUsedPoint(),
                        orderMapper.getOrderStatusStringForRestaurant(order))
        );

        given(orderRepository.findByRestaurant(restaurant,
                LocalDateTime.of(from, LocalTime.of(0, 0)),
                LocalDateTime.of(end, LocalTime.of(23, 59)), pageable)).willReturn(
                slice);

        given(orderMapper.toOrderListRestaurantResponse(slice.hasNext(),
                slice.getNumberOfElements(), slice.getContent())).willReturn(
                new OrderListRestaurantResponse(slice.hasNext(), slice.getNumberOfElements(),
                        orderRestaurantResponses));

        // When
        OrderListRestaurantResponse response = orderService.findOrderByRestaurant(
                new OrderListRestaurantRequest(restaurantId, 0, 3,
                        from, end));

        // Then
        assertThat(response.getHasNextPage()).isFalse();
        assertThat(response.getSize()).isEqualTo(1);
        assertThat(response.getOrders()).usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(orderRestaurantResponses);
    }

    @Test
    @DisplayName("존재하지 않는 레스토랑의 주문 내역을 조회하려 하면 예외가 발생한다.")
    void findOrderByNotExistsRestaurantTest() {
        // Given
        Long wrongRestaurantId = -1L;
        LocalDate from = LocalDate.now().minusMonths(1);
        LocalDate end = LocalDate.now();
        given(restaurantService.findRestaurantById(wrongRestaurantId)).willThrow(
                IllegalArgumentException.class);

        // When // Then
        assertThatThrownBy(() -> orderService.findOrderByRestaurant(
                new OrderListRestaurantRequest(wrongRestaurantId, 0, 3, from, end)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("레스토랑의 주문 내역을 조회할 때 조회의 시작일이 마감일보다 이전이 아니면 예외가 발생한다.")
    void findOrderByRestaurantInvalidPeriodTest() {
        // Given
        Long restaurantId = 1L;
        LocalDate from = LocalDate.now();
        LocalDate end = LocalDate.now().minusMonths(1);

        // When // Then
        assertThatThrownBy(() -> orderService.findOrderByRestaurant(
                new OrderListRestaurantRequest(restaurantId, 0, 3, from, end)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("특정 회원의 주문을 조회한다.")
    void findOrderByCustomerTest() {
        // Given
        String loginId = customer.getLoginId();
        LocalDate from = LocalDate.now().minusMonths(1);
        LocalDate end = LocalDate.now();
        PageRequest pageable = PageRequest.of(0, 3);
        SliceImpl<Order> orderSlice = new SliceImpl<>(Collections.singletonList(order), pageable,
                false);

        given(customerService.findCustomerEntity(loginId)).willReturn(customer);
        given(orderRepository.findByCustomer(customer, LocalDateTime.of(from, LocalTime.of(0, 0)),
                LocalDateTime.of(end, LocalTime.of(23, 59)),
                pageable)).willReturn(orderSlice
        );

        List<CartSummeryResponse> summeryResponses = carts.stream()
                .map(cart -> new CartSummeryResponse(cart.getMenu().getTitle(), cart.getQuantity()))
                .collect(
                        Collectors.toList());

        List<OrderSummeryResponse> orderSummeryResponses = orderSlice.getContent().stream()
                .map(o -> new OrderSummeryResponse(o.getId(), o.getCreatedAt(),
                        orderMapper.getOrderStatusStringForCustomer(order),
                        order.getRestaurant().getName(), order.getAfterDiscountTotalPrice(),
                        summeryResponses)).collect(
                        Collectors.toList());

        given(orderMapper.toOrderListCustomerResponse(orderSlice.hasNext(),
                orderSlice.getNumberOfElements(), orderSlice.getContent()))
                .willReturn(new OrderListCustomerResponse(orderSlice.hasNext(),
                        orderSlice.getNumberOfElements(), orderSummeryResponses));

        // When
        OrderListCustomerResponse response = orderService.findOrderByCustomer(
                new OrderListCustomerRequest(loginId, 0, 3,
                        from, end));

        // Then
        assertThat(response.getHasNextPage()).isFalse();
        assertThat(response.getSize()).isEqualTo(1);
        assertThat(response.getOrders()).usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(orderSummeryResponses);
    }

    @Test
    @DisplayName("존재하지 않는 회원의 주문을 조회하려 하면 예외가 발생한다.")
    void findOrderByNotExistsCustomerTest() {
        // Given
        String wrongLoginId = customer.getLoginId();
        LocalDate from = LocalDate.now().minusMonths(1);
        LocalDate end = LocalDate.now();

        given(customerService.findCustomerEntity(wrongLoginId)).willThrow(RuntimeException.class);

        // When // Then
        assertThatThrownBy(() -> orderService.findOrderByCustomer(
                new OrderListCustomerRequest(wrongLoginId, 0, 3, from, end)))
                .isExactlyInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("회원의 주문을 조회할 때 조회 기간의 시작일이 마감일보다 이전이 아니면 예외가 발생한다.")
    void findOrderByCustomerInvalidPeriodTest() {
        // Given
        String longinId = customer.getLoginId();
        LocalDate from = LocalDate.now();
        LocalDate end = LocalDate.now().minusMonths(1);

        // When // Then
        assertThatThrownBy(() -> orderService.findOrderByCustomer(
                new OrderListCustomerRequest(longinId, 0, 3, from, end)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("회원의 특정 주문을 조회한다.")
    void findDetailOrderCustomerTest() {
        // Given
        Long orderId = 1L;
        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));
        given(orderMapper.toOrderCustomerResponse(order)).willReturn(
                new OrderCustomerResponse(order.getCreatedAt(), cartResponses,
                        order.getOrderPrice(), order.getDeliveryFee(),
                        order.getVoucherDiscountPrice(), order.getUsedPoint(),
                        order.getOrderStatus().getDescription(),
                        order.getDeliveryAddress()));

        // When
        OrderCustomerResponse response = orderService.findDetailOrderForCustomer(orderId);

        // Then
        assertThat(response.getMenus().size()).isEqualTo(carts.size());
        assertThat(response.getUsedPoint()).isEqualTo(order.getUsedPoint());
        assertThat(response.getOrderPrice()).isEqualTo(
                order.getOrderPrice());
        assertThat(response.getVoucherDiscountPrice()).isEqualTo(
                order.getVoucherDiscountPrice());
    }

    @Test
    @DisplayName("회원의 존재하지 않는 특정 주문을 조회하려 하면 예외가 발생한다.")
    void findDetailOrderForCustomerNotExistsTest() {
        // Given
        Long wrongOrderId = -1L;
        given(orderRepository.findById(wrongOrderId)).willReturn(Optional.empty());

        // When // Then
        assertThatThrownBy(() -> orderService.findDetailOrderForCustomer(wrongOrderId))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("가게의 특정 주문을 조회한다.")
    void findDetailOrderForRestaurantTest() {
        // Given
        Long orderId = 1L;

        OrderRestaurantResponse orderRestaurantResponse = new OrderRestaurantResponse(
                order.getCreatedAt(),
                cartResponses,
                order.getOrderPrice(), order.getDeliveryFee(),
                order.getAfterDiscountTotalPrice(),
                order.getVoucherDiscountPrice() + order.getUsedPoint(),
                orderMapper.getOrderStatusStringForRestaurant(order)
        );

        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));
        given(orderMapper.toOrderRestaurantResponse(order)).willReturn(
                orderRestaurantResponse);

        // When
        OrderRestaurantResponse response = orderService.findDetailOrderByIdForRestaurant(
                orderId);

        // Then
        assertThat(response.getMenus().size()).isEqualTo(carts.size());
        assertThat(response.getTotalDiscountPrice()).isEqualTo(
                order.getUsedPoint() + order.getVoucherDiscountPrice());
        assertThat(response.getOrderPrice()).isEqualTo(
                order.getOrderPrice());
        assertThat(response.getAfterDiscountTotalPrice()).isEqualTo(
                order.getAfterDiscountTotalPrice());
    }

    @Test
    @DisplayName("가게의 존재하지 않는 특정 주문을 조회하려 하면 예외가 발생한다.")
    void findDetailOrderForRestaurantNotExistsTest() {
        // Given
        Long wrongOrderId = -1L;
        given(orderRepository.findById(wrongOrderId)).willReturn(Optional.empty());

        // When // Then
        assertThatThrownBy(() -> orderService.findDetailOrderByIdForRestaurant(
                wrongOrderId))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("가게의 기간별 매출 정보를 조회한다.")
    void findOrderStatisticsTest() {
        // Given
        Long restaurantId = 1L;
        LocalDate from = LocalDate.now().minusMonths(1);
        LocalDate end = LocalDate.now();
        long orderCount = 100L;
        long orderPrice = 1_000_000L;
        long voucherDiscountPrice = 100_000L;
        long usedPoint = 10_000L;

        OrderStatistics orderStatistics = new OrderStatistics(orderCount, orderPrice,
                voucherDiscountPrice,
                usedPoint);

        given(restaurantService.findRestaurantById(restaurantId)).willReturn(restaurant);
        given(orderRepository.findOrderStatistics(restaurant,
                LocalDateTime.of(from, LocalTime.of(0, 0)),
                LocalDateTime.of(end, LocalTime.of(23, 59)),
                DeliveryStatus.DELIVERY_FINISH))
                .willReturn(orderStatistics);
        given(orderMapper.toOrderStatisticsResponse(orderStatistics)).willReturn(
                new OrderStatisticsResponse(orderStatistics.getOrderCount(),
                        orderStatistics.getOrderPrice(),
                        orderStatistics.getVoucherDiscountPrice()
                                + orderStatistics.getUsedPoint()));

        // When
        OrderStatisticsResponse statistics = orderService.findOrderStatistics(
                new OrderStatisticsRequest(restaurantId, from, end));

        // Then
        assertThat(statistics.getOrderCount()).isEqualTo(orderCount);
        assertThat(statistics.getOrderPrice()).isEqualTo(orderPrice);
        assertThat(statistics.getDiscountPrice()).isEqualTo(voucherDiscountPrice + usedPoint);
    }

    @Test
    @DisplayName("존재하지 않는 가게의 기간별 매출 정보를 조회하려 하면 예외가 발생한다.")
    void findOrderStatisticsNotExistsRestaurantTest() {
        // Given
        Long wrongRestaurantId = -1L;
        LocalDate from = LocalDate.now().minusMonths(1);
        LocalDate end = LocalDate.now();

        given(restaurantService.findRestaurantById(wrongRestaurantId)).willThrow(
                IllegalArgumentException.class);

        // When // Then
        assertThatThrownBy(() -> orderService.findOrderStatistics(
                new OrderStatisticsRequest(wrongRestaurantId, from, end)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("가게의 기간별 매출 정보를 조회할 떄 조회 기간의 시작일이 마감일보다 이전이 아니면 예외가 발생한다.")
    void findOrderStatisticsInvalidPeriodTest() {
        // Given
        Long restaurantId = 1L;
        LocalDate from = LocalDate.now();
        LocalDate end = LocalDate.now().minusMonths(1);

        // When // Then
        assertThatThrownBy(() -> orderService.findOrderStatistics(
                new OrderStatisticsRequest(restaurantId, from, end)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
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
        given(deliveryEntityService.saveDelivery(order)).willReturn(delivery);

        // When
        orderService.acceptOrder(orderId, new OrderAcceptRequest(cookingTime));

        // Then
        then(deliveryEntityService).should().saveDelivery(order);
        assertThat(order.getDelivery()).isEqualTo(delivery);
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
        assertThatThrownBy(
                () -> orderService.acceptOrder(wrongOrderId, new OrderAcceptRequest(cookingTime)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    private Customer initCustomer() {
        CustomerGrade customerGrade = new CustomerGrade(4, "고마운분", 1000, 1);
        return new Customer("dev12", "Programmers123!", LocalDate.of(1999, 1, 1), customerGrade);
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
        return Order.createOrder(customer, restaurant, null, "서울특별시 강남구", 0,
                PaymentType.CREDIT_CARD, carts, 3000);
    }

    private Delivery initDelivery(Order order) {
        return Delivery.createDelivery(order, order.getRestaurant().getAddress(),
                order.getDeliveryAddress(), order.getDeliveryFee());
    }

    private List<CartResponse> initCartResponses() {
        return carts.stream()
                .map(cart -> new CartResponse(cart.getMenu().getTitle(), cart.getQuantity(),
                        cart.getMenu().getPrice() * cart.getQuantity()))
                .collect(Collectors.toList());
    }

    private Voucher initVoucher() {
        return new Voucher(VoucherType.FiXED, EventType.SPECIAL, 1000,
                LocalDateTime.now().plusMonths(1));
    }
}