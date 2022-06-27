package com.example.woowa.order.order.service;

import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.service.CustomerService;
import com.example.woowa.customer.voucher.entity.Voucher;
import com.example.woowa.customer.voucher.service.VoucherEntityService;
import com.example.woowa.order.order.entity.Cart;
import com.example.woowa.order.order.entity.Order;
import com.example.woowa.order.order.enums.PaymentType;
import com.example.woowa.order.order.repository.OrderRepository;
import com.example.woowa.restaurant.menu.entity.Menu;
import com.example.woowa.restaurant.menu.service.MenuService;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import com.example.woowa.restaurant.restaurant.service.RestaurantService;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final RestaurantService restaurantService;
    private final VoucherEntityService voucherEntityService;
    private final MenuService menuService;

    @Transactional
    public Long addOrder(String loginId, Long restaurantId, Long voucherId, int usePoint,
            PaymentType paymentType, Map<Long, Integer> cartMap) {
        List<Cart> carts = convertToCarts(cartMap);
        Customer findCustomer = customerService.findCustomerEntity(loginId);
        Restaurant findRestaurant = restaurantService.findRestaurantById(restaurantId);
        Voucher findVoucher =
                Objects.isNull(voucherId) ? null : voucherEntityService.findVoucherById(voucherId);

        Order order = Order.createOrder(findCustomer, findRestaurant, findVoucher, usePoint,
                paymentType,
                carts);

        return orderRepository.save(order).getId();
    }

    public List<Order> findOrderByRestaurant(Long restaurantId) {
        Restaurant findRestaurant = restaurantService.findRestaurantById(restaurantId);
        return orderRepository.findByRestaurant(findRestaurant);
    }

    public List<Order> findOrderByCustomer(String loginId) {
        Customer findCustomer = customerService.findCustomerEntity(loginId);
        return orderRepository.findByCustomer(findCustomer);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        findOrderById(orderId).cancelOrder();
    }

    @Transactional
    public void acceptOrder(Long orderId, int cookingTime) {
        findOrderById(orderId).acceptOrder(cookingTime);
    }

    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 orderId 입니다."));
    }

    private List<Cart> convertToCarts(Map<Long, Integer> cartMap) {
        return cartMap.keySet().stream().map(menuId -> {
            Menu findMenu = menuService.findMenuById(menuId);
            int quantity = cartMap.get(menuId);
            return new Cart(findMenu, quantity);
        }).collect(Collectors.toList());
    }
}
