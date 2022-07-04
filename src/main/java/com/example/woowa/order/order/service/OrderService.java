package com.example.woowa.order.order.service;

import com.example.woowa.common.exception.ErrorMessage;
import com.example.woowa.common.exception.NotFoundException;
import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.service.CustomerService;
import com.example.woowa.customer.voucher.entity.Voucher;
import com.example.woowa.customer.voucher.service.VoucherEntityService;
import com.example.woowa.delivery.entity.Delivery;
import com.example.woowa.delivery.enums.DeliveryStatus;
import com.example.woowa.delivery.service.DeliveryAreaService;
import com.example.woowa.delivery.service.DeliveryEntityService;
import com.example.woowa.order.order.dto.customer.OrderCustomerResponse;
import com.example.woowa.order.order.dto.customer.OrderListCustomerRequest;
import com.example.woowa.order.order.dto.customer.OrderListCustomerResponse;
import com.example.woowa.order.order.dto.customer.OrderSaveRequest;
import com.example.woowa.order.order.dto.restaurant.OrderAcceptRequest;
import com.example.woowa.order.order.dto.restaurant.OrderListRestaurantRequest;
import com.example.woowa.order.order.dto.restaurant.OrderListRestaurantResponse;
import com.example.woowa.order.order.dto.restaurant.OrderRestaurantResponse;
import com.example.woowa.order.order.dto.statistics.OrderStatistics;
import com.example.woowa.order.order.dto.statistics.OrderStatisticsRequest;
import com.example.woowa.order.order.dto.statistics.OrderStatisticsResponse;
import com.example.woowa.order.order.entity.Cart;
import com.example.woowa.order.order.entity.Order;
import com.example.woowa.order.order.mapper.CartMapper;
import com.example.woowa.order.order.mapper.OrderMapper;
import com.example.woowa.order.order.repository.OrderRepository;
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
    private final DeliveryAreaService deliveryAreaService;
    private final DeliveryEntityService deliveryEntityService;
    private final OrderMapper orderMapper;
    private final CartMapper cartMapper;


    @Transactional
    public Long addOrder(OrderSaveRequest request) {
        Customer findCustomer = customerService.findCustomerEntity(request.getLoginId());
        Restaurant findRestaurant = restaurantService.findRestaurantById(request.getRestaurantId());
        Long voucherId = request.getVoucherId();
        Voucher findVoucher =
                Objects.isNull(voucherId) ? null : voucherEntityService.findVoucherById(voucherId);

        List<Cart> carts = request.getCarts().stream().map(cartMapper::toCart)
                .collect(Collectors.toList());

        int deliveryFee = deliveryAreaService.getDeliveryFee(findRestaurant,
                request.getDeliveryAddress());

        Order order = Order.createOrder(findCustomer, findRestaurant, findVoucher,
                request.getDeliveryAddress(),
                request.getUsePoint(),
                request.getPaymentType(), carts, deliveryFee);

        return orderRepository.save(order).getId();
    }

    public OrderListRestaurantResponse findOrderByRestaurant(OrderListRestaurantRequest request) {
        validatePeriod(request.getFrom(), request.getEnd());
        Restaurant findRestaurant = restaurantService.findRestaurantById(request.getRestaurantId());
        Slice<Order> orderSlice = orderRepository.findByRestaurant(findRestaurant,
                LocalDateTime.of(request.getFrom(), LocalTime.of(0, 0)),
                LocalDateTime.of(request.getEnd(), LocalTime.of(23, 59)),
                PageRequest.of(request.getPageNum(), request.getSize()));

        return orderMapper.toOrderListRestaurantResponse(orderSlice.hasNext(),
                orderSlice.getNumberOfElements(), orderSlice.getContent());
    }

    public OrderListCustomerResponse findOrderByCustomer(OrderListCustomerRequest request) {
        validatePeriod(request.getFrom(), request.getEnd());
        Customer findCustomer = customerService.findCustomerEntity(request.getLoginId());
        Slice<Order> orderSlice = orderRepository.findByCustomer(findCustomer,
                LocalDateTime.of(request.getFrom(), LocalTime.of(0, 0)),
                LocalDateTime.of(request.getEnd(), LocalTime.of(23, 59)),
                PageRequest.of(request.getPageNum(), request.getSize()));

        return orderMapper.toOrderListCustomerResponse(orderSlice.hasNext(),
                orderSlice.getNumberOfElements(), orderSlice.getContent());
    }

    public OrderCustomerResponse findDetailOrderForCustomer(Long orderId) {
        return orderMapper.toOrderCustomerResponse(findOrderById(orderId));
    }

    public OrderRestaurantResponse findDetailOrderByIdForRestaurant(Long orderId) {
        return orderMapper.toOrderRestaurantResponse(findOrderById(orderId));
    }

    public OrderStatisticsResponse findOrderStatistics(OrderStatisticsRequest request) {
        validatePeriod(request.getFrom(), request.getEnd());
        Restaurant findRestaurant = restaurantService.findRestaurantById(request.getRestaurantId());
        OrderStatistics orderStatistics = orderRepository.findOrderStatistics(findRestaurant,
                LocalDateTime.of(request.getFrom(), LocalTime.of(0, 0)),
                LocalDateTime.of(request.getEnd(), LocalTime.of(23, 59)),
                DeliveryStatus.DELIVERY_FINISH);

        return orderMapper.toOrderStatisticsResponse(orderStatistics);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        findOrderById(orderId).cancelOrder();
    }

    @Transactional
    public void acceptOrder(Long orderId, OrderAcceptRequest request) {
        Order order = findOrderById(orderId);
        Delivery delivery = deliveryEntityService.saveDelivery(order);
        order.acceptOrder(request.getCookingTime(), delivery);
    }

    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 Order 입니다."));
    }

    private void validatePeriod(LocalDate from, LocalDate end) {
        if (from.isAfter(end)) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_PERIOD_VALUE.getMessage());
        }
    }
}
