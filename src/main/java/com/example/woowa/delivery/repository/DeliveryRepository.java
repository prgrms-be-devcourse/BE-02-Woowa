package com.example.woowa.delivery.repository;

import com.example.woowa.delivery.entity.Delivery;
import com.example.woowa.delivery.enums.DeliveryStatus;
import com.example.woowa.order.order.entity.Order;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Page<Delivery> findByDeliveryStatus(PageRequest pageRequest, DeliveryStatus deliveryStatus);

    List<Delivery> findByOrder(Order order);

    
}
