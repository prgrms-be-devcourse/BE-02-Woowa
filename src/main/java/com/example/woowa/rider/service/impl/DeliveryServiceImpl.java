package com.example.woowa.rider.service.impl;

import com.example.woowa.rider.repository.DeliveryRepository;
import com.example.woowa.rider.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
}
