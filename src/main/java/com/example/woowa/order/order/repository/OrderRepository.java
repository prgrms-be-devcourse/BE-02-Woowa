package com.example.woowa.order.order.repository;

import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.delivery.enums.DeliveryStatus;
import com.example.woowa.order.order.dto.statistics.OrderStatistics;
import com.example.woowa.order.order.entity.Order;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import java.time.LocalDateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT o FROM Order o WHERE o.restaurant = :restaurant AND o.createdAt BETWEEN :from AND :end ORDER BY o.createdAt desc")
    Slice<Order> findByRestaurant(@Param("restaurant") Restaurant restaurant,
            @Param("from") LocalDateTime from, @Param("end") LocalDateTime end,
            Pageable pageable);

    @Query(value = "SELECT o FROM Order o JOIN FETCH o.restaurant r WHERE o.customer = :customer AND o.createdAt BETWEEN :from AND :end ORDER BY o.createdAt desc")
    Slice<Order> findByCustomer(@Param("customer") Customer customer,
            @Param("from") LocalDateTime from, @Param("end") LocalDateTime end, Pageable pageable);

    @Query(value = "SELECT new com.example.woowa.order.order.dto.statistics.OrderStatistics(count(o), sum(o.orderPrice), sum(o.voucherDiscountPrice), sum(o.usedPoint)) FROM Order o WHERE o.restaurant = :restaurant AND o.delivery.deliveryStatus = :deliveryStatus AND o.createdAt BETWEEN :from AND :end")
    OrderStatistics findOrderStatistics(@Param("restaurant") Restaurant restaurant, @Param("from")
    LocalDateTime from, @Param("end") LocalDateTime end,
            @Param("deliveryStatus") DeliveryStatus deliveryStatus);
}
