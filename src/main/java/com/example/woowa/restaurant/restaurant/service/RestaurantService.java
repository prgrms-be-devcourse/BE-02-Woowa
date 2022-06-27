package com.example.woowa.restaurant.restaurant.service;

import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import com.example.woowa.restaurant.restaurant.repository.RestaurantRepository;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public Restaurant findRestaurantById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 restaurantId 입니다."));
    }

    @Transactional
    public void removeRestaurant(Long restaurantId) {
        Restaurant findRestaurant = findRestaurantById(restaurantId);
        restaurantRepository.delete(findRestaurant);
    }

    @Transactional
    public void updateBusinessHours(Long restaurantId, LocalTime openingTime,
            LocalTime closingTime) {
        findRestaurantById(restaurantId).updateBusinessHours(openingTime, closingTime);
    }

    @Transactional
    public void openRestaurant(Long restaurantId) {
        findRestaurantById(restaurantId).openRestaurant();
    }

    @Transactional
    public void closeRestaurant(Long restaurantId) {
        findRestaurantById(restaurantId).closeRestaurant();
    }

    @Transactional
    public void changeRestaurantDescription(Long restaurantId, String newDescription) {
        findRestaurantById(restaurantId).changeDescription(newDescription);
    }

    @Transactional
    public void changeRestaurantPhoneNumber(Long restaurantId, String newPhoneNumber) {
        findRestaurantById(restaurantId).changePhoneNumber(newPhoneNumber);
    }
}
