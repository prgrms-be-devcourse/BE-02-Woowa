package com.example.woowa.order.order.service;

import com.example.woowa.common.exception.ErrorMessage;
import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.service.CustomerService;
import com.example.woowa.customer.voucher.entity.Voucher;
import com.example.woowa.customer.voucher.service.VoucherEntityService;
import com.example.woowa.delivery.enums.DeliveryStatus;
import com.example.woowa.order.order.converter.OrderConverter;
import com.example.woowa.order.order.dto.cart.CartSaveRequest;
import com.example.woowa.order.order.dto.customer.OrderCustomerResponse;
import com.example.woowa.order.order.dto.customer.OrderListCustomerRequest;
import com.example.woowa.order.order.dto.customer.OrderListCustomerResponse;
import com.example.woowa.order.order.dto.customer.OrderSaveRequest;
import com.example.woowa.order.order.dto.restaurant.OrderListRestaurantRequest;
import com.example.woowa.order.order.dto.restaurant.OrderListRestaurantResponse;
import com.example.woowa.order.order.dto.restaurant.OrderRestaurantResponse;
import com.example.woowa.order.order.dto.statistics.OrderStatistics;
import com.example.woowa.order.order.dto.statistics.OrderStatisticsRequest;
import com.example.woowa.order.order.dto.statistics.OrderStatisticsResponse;
import com.example.woowa.order.order.entity.Cart;
import com.example.woowa.order.order.entity.Order;
import com.example.woowa.order.order.repository.OrderRepository;
import com.example.woowa.restaurant.menu.entity.Menu;
import com.example.woowa.restaurant.menu.service.MenuService;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import com.example.woowa.restaurant.restaurant.service.RestaurantService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
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
    public Long addOrder(OrderSaveRequest request) {
        Customer findCustomer = customerService.findCustomerEntity(request.getLoginId());
        Restaurant findRestaurant = restaurantService.findRestaurantById(request.getRestaurantId());
        Long voucherId = request.getVoucherId();
        Voucher findVoucher =
                Objects.isNull(voucherId) ? null : voucherEntityService.findVoucherById(voucherId);

        List<Cart> carts = request.getCarts().stream().map(this::toCart)
                .collect(Collectors.toList());

        Order order = Order.createOrder(findCustomer, findRestaurant, findVoucher,
                request.getUsePoint(),
                request.getPaymentType(), carts);

        return orderRepository.save(order).getId();
    }

    public OrderListRestaurantResponse findOrderByRestaurant(OrderListRestaurantRequest request) {
        validatePeriod(request.getFrom(), request.getEnd());
        Restaurant findRestaurant = restaurantService.findRestaurantById(request.getRestaurantId());
        Slice<Order> orderSlice = orderRepository.findByRestaurant(findRestaurant,
                LocalDateTime.of(request.getFrom(), LocalTime.of(0, 0)),
                LocalDateTime.of(request.getEnd(), LocalTime.of(23, 59)),
                PageRequest.of(request.getPageNum(), request.getSize()));

        return OrderConverter.toOrderListRestaurantResponse(orderSlice);
    }

    public OrderListCustomerResponse findOrderByCustomer(OrderListCustomerRequest request) {
        validatePeriod(request.getFrom(), request.getEnd());
        Customer findCustomer = customerService.findCustomerEntity(request.getLoginId());
        Slice<Order> orderSlice = orderRepository.findByCustomer(findCustomer,
                LocalDateTime.of(request.getFrom(), LocalTime.of(0, 0)),
                LocalDateTime.of(request.getEnd(), LocalTime.of(23, 59)),
                PageRequest.of(request.getPageNum(), request.getSize()));

        return OrderConverter.toOrderListCustomerResponse(orderSlice);
    }

    public OrderCustomerResponse findDetailOrderForCustomer(Long orderId) {
        return OrderConverter.toOrderCustomerResponse(findOrderById(orderId));
    }

    public OrderRestaurantResponse findDetailOrderByIdForRestaurant(Long orderId) {
        return OrderConverter.toOrderRestaurantResponse(findOrderById(orderId));
    }

    public OrderStatisticsResponse findOrderStatistics(OrderStatisticsRequest request) {
        validatePeriod(request.getFrom(), request.getEnd());
        Restaurant findRestaurant = restaurantService.findRestaurantById(request.getRestaurantId());
        OrderStatistics orderStatistics = orderRepository.findOrderStatistics(findRestaurant,
                LocalDateTime.of(request.getFrom(), LocalTime.of(0, 0)),
                LocalDateTime.of(request.getEnd(), LocalTime.of(23, 59)),
                DeliveryStatus.DELIVERY_FINISH);

        return OrderConverter.toOrderStatisticsResponse(orderStatistics);
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

    private void validatePeriod(LocalDate from, LocalDate end) {
        if (from.isAfter(end)) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_PERIOD_VALUE.getMessage());
        }
    }

    private Cart toCart(CartSaveRequest request) {
        Menu menu = menuService.findMenuEntityById(request.getMenuId());
        return new Cart(menu, request.getQuantity());
    }
}
