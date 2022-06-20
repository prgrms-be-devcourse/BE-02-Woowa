package com.example.woowa.advertisement.service.impl;

import com.example.woowa.advertisement.repository.AdvertisementRepository;
import com.example.woowa.advertisement.service.AdvertisementService;
import org.springframework.stereotype.Service;

@Service
public class AdvertisementServiceImpl implements AdvertisementService {

    private final AdvertisementRepository advertisementRepository;

    public AdvertisementServiceImpl(
        AdvertisementRepository advertisementRepository) {
        this.advertisementRepository = advertisementRepository;
    }

}
