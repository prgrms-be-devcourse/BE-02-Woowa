package com.example.woowa.delivery.service;

import com.example.woowa.delivery.entity.AreaCode;
import com.example.woowa.delivery.entity.Delivery;
import com.example.woowa.delivery.entity.DeliveryArea;
import com.example.woowa.delivery.repository.DeliveryAreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryAreaService {

    private final DeliveryAreaRepository deliveryAreaRepository;

    public void save(DeliveryArea deliveryArea) {
        deliveryAreaRepository.save(deliveryArea);
    }

}
