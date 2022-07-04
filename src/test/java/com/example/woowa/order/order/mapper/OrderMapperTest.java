package com.example.woowa.order.order.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.entity.CustomerGrade;
import com.example.woowa.customer.customer.repository.CustomerGradeRepository;
import com.example.woowa.customer.customer.repository.CustomerRepository;
import com.example.woowa.delivery.enums.DeliveryStatus;
import com.example.woowa.delivery.repository.DeliveryRepository;
import com.example.woowa.order.order.dto.customer.OrderCustomerResponse;
import com.example.woowa.order.order.dto.customer.OrderListCustomerResponse;
import com.example.woowa.order.order.dto.customer.OrderSummeryResponse;
import com.example.woowa.order.order.dto.restaurant.OrderListRestaurantResponse;
import com.example.woowa.order.order.dto.restaurant.OrderRestaurantResponse;
import com.example.woowa.order.order.dto.statistics.OrderStatistics;
import com.example.woowa.order.order.dto.statistics.OrderStatisticsResponse;
import com.example.woowa.order.order.entity.Cart;
import com.example.woowa.order.order.entity.Order;
import com.example.woowa.order.order.enums.PaymentType;
import com.example.woowa.order.order.repository.OrderRepository;
import com.example.woowa.restaurant.menu.entity.Menu;
import com.example.woowa.restaurant.menu.enums.MenuStatus;
import com.example.woowa.restaurant.menu.repository.MenuRepository;
import com.example.woowa.restaurant.menugroup.entity.MenuGroup;
import com.example.woowa.restaurant.menugroup.repository.MenuGroupRepository;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import com.example.woowa.restaurant.restaurant.repository.RestaurantRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderMapperTest {

    @Autowired
    OrderMapper mapper;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    MenuGroupRepository menuGroupRepository;
    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    CustomerGradeRepository gradeRepository;
    @Autowired
    DeliveryRepository deliveryRepository;

    Order order;
    Restaurant restaurant;
    Customer customer;
    List<Cart> carts;
    Menu menu1, menu2, menu3, menu4;

    @Test
    @DisplayName("Order entity -> OrderRestaurantResponse 변환을 테스트한다.")
    void toOrderRestaurantResponseTest() {
        // When
        OrderRestaurantResponse response = mapper.toOrderRestaurantResponse(order);

        // Then
        assertThat(response.getTotalDiscountPrice())
                .isEqualTo(order.getVoucherDiscountPrice() + order.getUsedPoint());
        assertThat(response.getCreatedAt())
                .isEqualTo(order.getCreatedAt());
        assertThat(response.getOrderPrice())
                .isEqualTo(order.getOrderPrice());
        assertThat(response.getDeliveryFee())
                .isEqualTo(order.getDeliveryFee());
        assertThat(response.getAfterDiscountTotalPrice())
                .isEqualTo(order.getAfterDiscountTotalPrice());
        assertThat(response.getMenus().size()).isEqualTo(order.getCarts().size());
    }

    @Test
    @DisplayName("OrderRestaurantResponse 변환을 테스트한다.")
    void toOrderListRestaurantResponseTest() {
        // Given
        Slice<Order> orderSlice = orderRepository.findByRestaurant(restaurant,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now(), PageRequest.of(0, 1));

        // When
        OrderListRestaurantResponse response = mapper.toOrderListRestaurantResponse(
                orderSlice.hasNext(), orderSlice.getNumberOfElements(), orderSlice.getContent());

        // Then
        assertThat(response.getHasNextPage()).isEqualTo(orderSlice.hasNext());
        assertThat(response.getSize()).isEqualTo(orderSlice.getNumberOfElements());
        assertThat(response.getOrders().size())
                .isEqualTo(orderSlice.getNumberOfElements());
    }

    @Test
    @DisplayName("OrderListCustomerResponse 변환을 테스트한다.")
    void toOrderListCustomerResponseTest() {
        // Given
        Slice<Order> orderSlice = orderRepository.findByCustomer(customer,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now(), PageRequest.of(0, 1));

        // When
        OrderListCustomerResponse response = mapper.toOrderListCustomerResponse(
                orderSlice.hasNext(), orderSlice.getNumberOfElements(), orderSlice.getContent());

        // Then
        assertThat(response.getHasNextPage()).isEqualTo(orderSlice.hasNext());
        assertThat(response.getSize()).isEqualTo(orderSlice.getNumberOfElements());
        assertThat(response.getOrders().size())
                .isEqualTo(orderSlice.getNumberOfElements());
    }

    @Test
    @DisplayName("Order entity -> OrderSummeryResponse 변환을 테스트한다.")
    void toOrderSummeryResponseTest() {
        // When
        OrderSummeryResponse response = mapper.toOrderSummeryResponse(order);

        // Then
        assertThat(response.getOrderStatus()).isEqualTo(
                mapper.getOrderStatusStringForCustomer(order));
        assertThat(response.getId()).isEqualTo(order.getId());
        assertThat(response.getRestaurantName()).isEqualTo(order.getRestaurant().getName());
        assertThat(response.getCarts().size()).isEqualTo(order.getCarts().size());
    }

    @Test
    @DisplayName("Order entity -> OrderCustomerResponse 변환을 테스트한다.")
    void toOrderCustomerResponseTest() {
        // When
        OrderCustomerResponse response = mapper.toOrderCustomerResponse(order);

        // Then
        assertThat(response).usingRecursiveComparison().ignoringFields("menus", "orderStatus")
                .isEqualTo(order);
        assertThat(response.getMenus().size()).isEqualTo(order.getCarts().size());
        assertThat(response.getOrderStatus()).isEqualTo(
                mapper.getOrderStatusStringForCustomer(order));
    }

    @Test
    @DisplayName("OrderStatistics -> OrderStatisticsResponse 변환을 테스트한다.")
    void toOrderStatisticsResponseTest() {
        // Given
        OrderStatistics orderStatistics = orderRepository.findOrderStatistics(restaurant,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now(), DeliveryStatus.DELIVERY_FINISH);

        // When
        OrderStatisticsResponse response = mapper.toOrderStatisticsResponse(
                orderStatistics);

        // Then
        assertThat(response.getOrderCount()).isEqualTo(orderStatistics.getOrderCount());
        assertThat(response.getOrderPrice()).isEqualTo(orderStatistics.getOrderPrice());
        assertThat(response.getDiscountPrice()).isEqualTo(
                orderStatistics.getVoucherDiscountPrice() + orderStatistics.getUsedPoint());
    }

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

        MenuGroup menuGroup1 = menuGroupRepository.save(
                MenuGroup.createMenuGroup(restaurant, "밥", null));
        MenuGroup menuGroup2 = menuGroupRepository.save(
                MenuGroup.createMenuGroup(restaurant, "면", null));

        menu1 = menuRepository.save(
                Menu.createMenu(menuGroup1, "김치 볶음밥", 10000, "맛있어요", true, MenuStatus.SALE));
        menu2 = menuRepository.save(
                Menu.createMenu(menuGroup1, "비빔밥", 10000, "맛있어요", true, MenuStatus.SALE));
        menu3 = menuRepository.save(
                Menu.createMenu(menuGroup2, "라면", 10000, "맛있어요", true, MenuStatus.SALE));
        menu4 = menuRepository.save(
                Menu.createMenu(menuGroup2, "국수", 10000, "맛있어요", true, MenuStatus.SALE));

        CustomerGrade customerGrade = gradeRepository.save(new CustomerGrade(4, "고마운분", 1000, 1));

        customer = customerRepository.save(
                new Customer("dev12", "Programmers123!", LocalDate.of(1999, 1, 1),
                        customerGrade));

        carts = List.of(new Cart(menu1, 1),
                new Cart(menu2, 1),
                new Cart(menu3, 1),
                new Cart(menu3, 1),
                new Cart(menu4, 1),
                new Cart(menu4, 1));

        order = orderRepository.save(
                Order.createOrder(customer, restaurant, null, "서을 특별시 강남구", 0,
                        PaymentType.CREDIT_CARD,
                        carts, 3000));
    }
}