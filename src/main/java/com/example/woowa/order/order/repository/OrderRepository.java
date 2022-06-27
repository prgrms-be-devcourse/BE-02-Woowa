package com.example.woowa.order.order.repository;

import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.order.order.entity.Order;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByRestaurant(Restaurant restaurant);

    List<Order> findByCustomer(Customer customer);
}
