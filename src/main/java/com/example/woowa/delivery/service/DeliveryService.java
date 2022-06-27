package com.example.woowa.delivery.service;

import com.example.woowa.delivery.entity.AreaCode;
import com.example.woowa.delivery.entity.Delivery;
import com.example.woowa.delivery.entity.Rider;
import com.example.woowa.delivery.enums.DeliveryStatus;
import com.example.woowa.delivery.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public Long save(Delivery delivery) {
        deliveryRepository.save(delivery);
        return delivery.getId();
    }

    public List<Delivery> findAll() {
        return deliveryRepository.findAll();
    }

    public List<Delivery> findByDeliveryStatus(DeliveryStatus deliveryStatus) {
        return deliveryRepository.findByDeliveryStatus(deliveryStatus);
    }

    public void acceptDelivery(Rider rider, Delivery delivery) {

    }
}
