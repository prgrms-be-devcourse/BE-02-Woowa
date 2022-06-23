package com.example.woowa.delivery.service.impl;

import com.example.woowa.delivery.repository.DeliveryRepository;
import com.example.woowa.delivery.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
}
