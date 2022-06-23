package com.example.woowa.restaurant.advertisement.service;

import com.example.woowa.restaurant.advertisement.repository.AdvertisementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;

}
