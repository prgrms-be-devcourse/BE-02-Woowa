package com.example.woowa.order.order.mapper;

import com.example.woowa.customer.customer.service.CustomerService;
import com.example.woowa.customer.voucher.service.VoucherService;
import com.example.woowa.order.order.dto.customer.OrderCustomerResponse;
import com.example.woowa.order.order.dto.customer.OrderListCustomerResponse;
import com.example.woowa.order.order.dto.customer.OrderSummeryResponse;
import com.example.woowa.order.order.dto.restaurant.OrderListRestaurantResponse;
import com.example.woowa.order.order.dto.restaurant.OrderRestaurantResponse;
import com.example.woowa.order.order.dto.statistics.OrderStatistics;
import com.example.woowa.order.order.dto.statistics.OrderStatisticsResponse;
import com.example.woowa.order.order.entity.Order;
import com.example.woowa.restaurant.restaurant.service.RestaurantService;
import java.util.List;
import java.util.Objects;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {CartMapper.class, VoucherService.class,
        RestaurantService.class, CustomerService.class})
public interface OrderMapper {

    @Mappings({
            @Mapping(target = "menus", source = "carts"),
            @Mapping(target = "totalDiscountPrice", expression = "java(order.getVoucherDiscountPrice() + order.getUsedPoint())"),
            @Mapping(target = "orderStatus", expression = "java(getOrderStatusStringForRestaurant(order))")
    })
    OrderRestaurantResponse toOrderRestaurantResponse(Order order);

    OrderListRestaurantResponse toOrderListRestaurantResponse(Boolean hasNextPage, Integer size,
            List<Order> orders);

    @Mappings({
            @Mapping(target = "orderStatus", expression = "java(getOrderStatusStringForCustomer(order))"),
            @Mapping(target = "restaurantName", source = "restaurant.name")
    })
    OrderSummeryResponse toOrderSummeryResponse(Order order);

    OrderListCustomerResponse toOrderListCustomerResponse(Boolean hasNextPage, Integer size,
            List<Order> orders);

    @Mappings({
            @Mapping(target = "orderStatus", expression = "java(getOrderStatusStringForCustomer(order))"),
            @Mapping(target = "menus", source = "carts")
    })
    OrderCustomerResponse toOrderCustomerResponse(Order order);

    @Mapping(target = "discountPrice", expression = "java(orderStatistics.getVoucherDiscountPrice() + orderStatistics.getUsedPoint())")
    OrderStatisticsResponse toOrderStatisticsResponse(OrderStatistics orderStatistics);

    /**
     * 가게에서 주문 내역을 조회시에 주문 상태는 다음 중 하나이다 - 주문 취소, 결제 완료, 배차 대기 중, 배차 등록, 픽업 완료, 배달 완료
     */
    default String getOrderStatusStringForRestaurant(Order order) {
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
    default String getOrderStatusStringForCustomer(Order order) {
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
