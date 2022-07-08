package com.example.woowa.order.order.controller;

import com.example.woowa.order.order.dto.customer.OrderCustomerResponse;
import com.example.woowa.order.order.dto.customer.OrderListCustomerRequest;
import com.example.woowa.order.order.dto.customer.OrderListCustomerResponse;
import com.example.woowa.order.order.dto.customer.OrderSaveRequest;
import com.example.woowa.order.order.dto.restaurant.OrderAcceptRequest;
import com.example.woowa.order.order.dto.restaurant.OrderListRestaurantRequest;
import com.example.woowa.order.order.dto.restaurant.OrderListRestaurantResponse;
import com.example.woowa.order.order.dto.restaurant.OrderRestaurantResponse;
import com.example.woowa.order.order.dto.statistics.OrderStatisticsRequest;
import com.example.woowa.order.order.dto.statistics.OrderStatisticsResponse;
import com.example.woowa.order.order.service.OrderService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class OrderApiController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<Void> addOrder(@RequestBody @Validated OrderSaveRequest request) {
        Long orderId = orderService.addOrder(request);
        return ResponseEntity.created(URI.create("/api/v1/orders" + orderId)).build();
    }

    @GetMapping("/restaurants/orders")
    public ResponseEntity<OrderListRestaurantResponse> findOrderByRestaurant(@RequestBody @Validated
    OrderListRestaurantRequest request) {
        return ResponseEntity.ok(orderService.findOrderByRestaurant(request));
    }

    @GetMapping("/customers/orders")
    public ResponseEntity<OrderListCustomerResponse> findOrderByCustomer(
            @RequestBody @Validated OrderListCustomerRequest request) {
        return ResponseEntity.ok(orderService.findOrderByCustomer(request));
    }

    @GetMapping("/customers/orders/{orderId}")
    public ResponseEntity<OrderCustomerResponse> findDetailOrderForCustomer(
            @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.findDetailOrderForCustomer(orderId));
    }

    @GetMapping("/restaurants/orders/{orderId}")
    public ResponseEntity<OrderRestaurantResponse> findDetailOrderForRestaurant(
            @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.findDetailOrderByIdForRestaurant(orderId));
    }

    @GetMapping("/restaurants/orders/statistics")
    public ResponseEntity<OrderStatisticsResponse> findOrderStatistics(@RequestBody @Validated
    OrderStatisticsRequest request) {
        return ResponseEntity.ok(orderService.findOrderStatistics(request));
    }

    @PatchMapping("/orders/{orderId}/accept")
    public ResponseEntity<Void> acceptOrder(@PathVariable Long orderId, @RequestBody @Validated
    OrderAcceptRequest request) {
        orderService.acceptOrder(orderId, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/orders/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok().build();
    }
}
