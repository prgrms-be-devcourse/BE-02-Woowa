package com.example.woowa.restaurant.advertisement.service;

import com.example.woowa.restaurant.advertisement.entity.Advertisement;
import com.example.woowa.restaurant.advertisement.enums.RateType;
import com.example.woowa.restaurant.advertisement.enums.UnitType;
import com.example.woowa.restaurant.advertisement.repository.AdvertisementRepository;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import com.example.woowa.restaurant.restaurant.service.RestaurantService;
import com.example.woowa.restaurant.restaurant_advertisement.entity.RestaurantAdvertisement;
import com.example.woowa.restaurant.restaurant_advertisement.entity.RestaurantAdvertisementId;
import com.example.woowa.restaurant.restaurant_advertisement.repository.RestaurantAdvertisementRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final RestaurantAdvertisementRepository restaurantAdvertisementRepository;
    private final RestaurantService restaurantService;

    @Transactional
    public Long createAdvertisement(String name, UnitType unitType, RateType rateType, Integer rate, String description) {
        return advertisementRepository.save(new Advertisement(name, unitType, rateType, rate, description)).getId();
    }

    public List<Advertisement> findAdvertisements() {
        return advertisementRepository.findAll();
    }

    public Advertisement findAdvertisementById(Long advertisementId) {
        return advertisementRepository.findById(advertisementId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 광고 아이디입니다."));
    }

    public Advertisement findAdvertisementByName(String advertisementName) {
        return advertisementRepository.findAdvertisementByName(advertisementName)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 광고 이름입니다."));
    }

    public List<Restaurant> findRestaurantsByAdvertisementId(Long advertisementId) {
        return findAdvertisementById(advertisementId).getRestaurantAdvertisements().stream()
            .map(RestaurantAdvertisement::getRestaurant)
            .collect(Collectors.toList());
    }

    public List<Restaurant> findRestaurantByAdvertisementName(String advertisementName) {
        return findAdvertisementByName(advertisementName).getRestaurantAdvertisements().stream()
            .map(RestaurantAdvertisement::getRestaurant)
            .collect(Collectors.toList());
    }

    @Transactional
    public void changeAdvertisementName(Long advertisementId, String advertisementName) {
        findAdvertisementById(advertisementId).changeName(advertisementName);
    }

    @Transactional
    public void changeAdvertisementName(String oldName, String newName) {
        findAdvertisementByName(oldName).changeName(newName);
    }

    @Transactional
    public void changeAdvertisementUnitType(Long advertisementId, UnitType unitType) {
        findAdvertisementById(advertisementId).changeUnitType(unitType);
    }

    @Transactional
    public void changeAdvertisementRateType(Long advertisementId, RateType rateType) {
        findAdvertisementById(advertisementId).changeRateType(rateType);
    }

    @Transactional
    public void changeAdvertisementRate(Long advertisementId, Integer rate) {
        findAdvertisementById(advertisementId).changeRate(rate);
    }

    @Transactional
    public void changeAdvertisementDescription(Long advertisementId, String description) {
        findAdvertisementById(advertisementId).changeDescription(description);
    }

    @Transactional
    public void deleteAdvertisement(Long advertisementId) {
        advertisementRepository.deleteById(advertisementId);
    }

    @Transactional
    public void includeRestaurantInAdvertisement(Long advertisementId, Long restaurantId) {
        Advertisement advertisement = findAdvertisementById(advertisementId);
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
        restaurantAdvertisementRepository.save(new RestaurantAdvertisement(restaurant, advertisement));
    }

    @Transactional
    public void excludeRestaurantInAdvertisement(Long advertisementId, Long restaurantId) {
        RestaurantAdvertisement restaurantAdvertisement = restaurantAdvertisementRepository.findById(
                new RestaurantAdvertisementId(restaurantId, advertisementId))
            .orElseThrow(() -> new IllegalArgumentException("부적절한 정보입니다."));

        findAdvertisementById(advertisementId).getRestaurantAdvertisements().remove(restaurantAdvertisement);
        restaurantService.findRestaurantById(restaurantId).getRestaurantAdvertisements().remove(restaurantAdvertisement);
    }

}
