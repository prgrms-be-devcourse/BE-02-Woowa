package com.example.woowa.order.order.converter;

import com.example.woowa.order.order.dto.cart.CartResponse;
import com.example.woowa.order.order.dto.cart.CartSummeryResponse;
import com.example.woowa.order.order.dto.customer.OrderCustomerResponse;
import com.example.woowa.order.order.dto.customer.OrderListCustomerResponse;
import com.example.woowa.order.order.dto.customer.OrderSummeryResponse;
import com.example.woowa.order.order.dto.restaurant.OrderListRestaurantResponse;
import com.example.woowa.order.order.dto.restaurant.OrderRestaurantResponse;
import com.example.woowa.order.order.dto.statistics.OrderStatistics;
import com.example.woowa.order.order.dto.statistics.OrderStatisticsResponse;
import com.example.woowa.order.order.entity.Order;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.data.domain.Slice;

public abstract class OrderConverter {

    public static OrderRestaurantResponse toOrderRestaurantResponse(Order order) {
        List<CartResponse> menus = order.getCarts().stream()
                .map(cart -> new CartResponse(cart.getMenu().getTitle(), cart.getQuantity(),
                        cart.getMenu().getPrice() * cart.getQuantity()))
                .collect(Collectors.toList());

        return new OrderRestaurantResponse(order.getCreatedAt(), menus,
                order.getOrderPrice(), order.getDeliveryFee(), order.getAfterDiscountTotalPrice(),
                order.getVoucherDiscountPrice() + order.getUsedPoint(),
                getOrderStatusStringForRestaurant(order));
    }

    public static OrderListRestaurantResponse toOrderListRestaurantResponse(
            Slice<Order> orderSlice) {

        List<OrderRestaurantResponse> responses = orderSlice.getContent().stream()
                .map(OrderConverter::toOrderRestaurantResponse).collect(
                        Collectors.toList());

        return new OrderListRestaurantResponse(orderSlice.hasNext(),
                orderSlice.getNumberOfElements(),
                responses);
    }

    public static OrderListCustomerResponse toOrderListCustomerResponse(Slice<Order> orderSlice) {
        List<OrderSummeryResponse> responses = orderSlice.getContent().stream()
                .map(OrderConverter::toOrderSummeryResponse).collect(
                        Collectors.toList());

        return new OrderListCustomerResponse(orderSlice.hasNext(), orderSlice.getNumberOfElements(),
                responses);
    }

    public static OrderSummeryResponse toOrderSummeryResponse(Order order) {
        List<CartSummeryResponse> carts = order.getCarts().stream()
                .map(cart -> new CartSummeryResponse(cart.getMenu().getTitle(),
                        cart.getQuantity()))
                .collect(Collectors.toList());

        return new OrderSummeryResponse(order.getId(), order.getCreatedAt(),
                getOrderStatusStringForCustomer(order), order.getRestaurant().getName(),
                order.getAfterDiscountTotalPrice(), carts);
    }

    public static OrderCustomerResponse toOrderCustomerResponse(Order order) {
        List<CartResponse> menus = order.getCarts().stream()
                .map(CartConverter::toCartResponse)
                .collect(Collectors.toList());

        return new OrderCustomerResponse(order.getCreatedAt(), menus,
                order.getOrderPrice(), order.getDeliveryFee(),
                order.getVoucherDiscountPrice(), order.getUsedPoint(),
                getOrderStatusStringForCustomer(order),
                order.getDeliveryAddress());
    }

    public static OrderStatisticsResponse toOrderStatisticsResponse(
            OrderStatistics orderStatistics) {
        return new OrderStatisticsResponse(orderStatistics.getOrderCount(),
                orderStatistics.getOrderPrice(),
                orderStatistics.getVoucherDiscountPrice() + orderStatistics.getUsedPoint());
    }

    /**
     * 가게에서 주문 내역을 조회시에 주문 상태는 다음 중 하나이다 - 주문 취소, 결제 완료, 배차 대기 중, 배차 등록, 픽업 완료, 배달 완료
     */
    private static String getOrderStatusStringForRestaurant(Order order) {
        switch (order.getOrderStatus()) {
            case CANCEL:
            case PAYMENT_COMPLETED:
                return order.getOrderStatus().getDescription();
            default:
                return order.getDelivery().getDeliveryStatus().getDescription();
        }
    }

    /**
     * 손님이 주문 내역을 조회시에 주문 상태는 다음 중 하나이다 - 주문 취소, 결제 완료, 주문 승인, 픽업 완료, 배달 완료
     */
    private static String getOrderStatusStringForCustomer(Order order) {
        if (Objects.isNull(order.getDelivery())) {
            return order.getOrderStatus().getDescription();
        }

        switch (order.getDelivery().getDeliveryStatus()) {
            case DELIVERY_PICKUP:
            case DELIVERY_FINISH:
                return order.getDelivery().getDeliveryStatus().getDescription();
            default:
                return order.getOrderStatus().getDescription();
        }
    }
}
