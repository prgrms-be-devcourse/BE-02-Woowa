package com.example.woowa.advertisement.service.impl;

import com.example.woowa.advertisement.repository.AdvertisementRepository;
import com.example.woowa.advertisement.service.AdvertisementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdvertisementServiceImpl implements AdvertisementService {

    private final AdvertisementRepository advertisementRepository;

}
