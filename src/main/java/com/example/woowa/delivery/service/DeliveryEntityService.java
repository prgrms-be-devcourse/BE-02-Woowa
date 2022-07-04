package com.example.woowa.delivery.service;

import com.example.woowa.delivery.entity.Delivery;
import com.example.woowa.delivery.repository.DeliveryRepository;
import com.example.woowa.order.order.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryEntityService {

    private final DeliveryRepository deliveryRepository;

    @Transactional
    public Delivery saveDelivery(Order order) {
        return deliveryRepository.save(
                Delivery.createDelivery(order, order.getRestaurant().getAddress(),
                        order.getDeliveryAddress(),
                        order.getDeliveryFee()));
    }
}
