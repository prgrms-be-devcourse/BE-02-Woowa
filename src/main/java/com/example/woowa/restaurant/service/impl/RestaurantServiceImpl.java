package com.example.woowa.restaurant.service.impl;

import com.example.woowa.restaurant.repository.RestaurantRepository;
import com.example.woowa.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
}
