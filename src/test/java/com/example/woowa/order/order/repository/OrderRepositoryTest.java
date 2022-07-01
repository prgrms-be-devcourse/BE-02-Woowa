package com.example.woowa.order.order.repository;

import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.entity.CustomerGrade;
import com.example.woowa.customer.customer.repository.CustomerGradeRepository;
import com.example.woowa.customer.customer.repository.CustomerRepository;
import com.example.woowa.delivery.enums.DeliveryStatus;
import com.example.woowa.delivery.repository.DeliveryRepository;
import com.example.woowa.order.order.dto.statistics.OrderStatistics;
import com.example.woowa.order.order.entity.Cart;
import com.example.woowa.order.order.entity.Order;
import com.example.woowa.order.order.enums.PaymentType;
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
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

//TODO 딜리버리 상태 변경 가능할 때 테스트 검증(현재는 쿼리 수행만 확인)
@SpringBootTest
@Transactional
class OrderRepositoryTest {

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

    Long restaurantId;
    Long customerId;

    List<Cart> carts;
    Order order;


    @Test
    @DisplayName("기간별 레스토랑 주문 정보를 조회한다.")
    void findByRestaurantTest() {
        // Given
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);

        // When
        Slice<Order> orderSlice = orderRepository.findByRestaurant(restaurant.get(),
                LocalDateTime.of(2022, 6, 25, 0, 0),
                LocalDateTime.now(),
                PageRequest.of(0, 3));

        // Then
//        List<Order> content = orderSlice.getContent();
//        assertThat(content.size()).isEqualTo(1);
//        assertThat(content).contains(order);
//
//        Order findOrder = content.get(0);
//        assertThat(findOrder.getCarts()).containsAll(carts);
    }

    @Test
    @DisplayName("기간별 회원의 주문 정보를 조회한다.")
    void findByCustomerTest() {
        // Given
        Optional<Customer> customer = customerRepository.findById(customerId);

        // When
        Slice<Order> orderSlice = orderRepository.findByCustomer(customer.get(),
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now(), PageRequest.of(0, 1));

        // Then
//        assertThat(orderSlice.getContent().size()).isEqualTo(1);
//        assertThat(orderSlice.getContent()).containsExactly(order);
    }

    @Test
    @DisplayName("기간별 가게의 매출 정보를 조회힌다.")
    void findOrderStatisticsTest() {
        // Given
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);

        // When
        OrderStatistics orderStatistics = orderRepository.findOrderStatistics(restaurant.get(),
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now(),
                DeliveryStatus.DELIVERY_FINISH);

        // Then
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

        restaurantId = restaurant.getId();

        MenuGroup menuGroup1 = menuGroupRepository.save(
                MenuGroup.createMenuGroup(restaurant, "밥", null));
        MenuGroup menuGroup2 = menuGroupRepository.save(
                MenuGroup.createMenuGroup(restaurant, "면", null));

        Menu menu1 = menuRepository.save(
                Menu.createMenu(menuGroup1, "김치 볶음밥", 10000, "맛있어요", true, MenuStatus.SALE));
        Menu menu2 = menuRepository.save(
                Menu.createMenu(menuGroup1, "비빔밥", 10000, "맛있어요", true, MenuStatus.SALE));
        Menu menu3 = menuRepository.save(
                Menu.createMenu(menuGroup2, "라면", 10000, "맛있어요", true, MenuStatus.SALE));
        Menu menu4 = menuRepository.save(
                Menu.createMenu(menuGroup2, "국수", 10000, "맛있어요", true, MenuStatus.SALE));

        CustomerGrade customerGrade = gradeRepository.save(new CustomerGrade(4, "고마운분", 1000, 1));

        Customer customer = customerRepository.save(
                new Customer("dev12", "Programmers123!", LocalDate.of(1999, 1, 1),
                        customerGrade));
        customerId = customer.getId();

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