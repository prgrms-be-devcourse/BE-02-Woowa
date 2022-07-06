package com.example.woowa.restaurant.advertisement.service;

import com.example.woowa.common.exception.NotFoundException;
import com.example.woowa.restaurant.advertisement.dto.request.AdvertisementCreateRequest;
import com.example.woowa.restaurant.advertisement.dto.request.AdvertisementUpdateRequest;
import com.example.woowa.restaurant.advertisement.dto.response.AdvertisementCreateResponse;
import com.example.woowa.restaurant.advertisement.dto.response.AdvertisementFindResponse;
import com.example.woowa.restaurant.advertisement.entity.Advertisement;
import com.example.woowa.restaurant.advertisement.mapper.AdvertisementMapper;
import com.example.woowa.restaurant.advertisement.repository.AdvertisementRepository;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import com.example.woowa.restaurant.restaurant.repository.RestaurantRepository;
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

    private final RestaurantAdvertisementRepository restaurantAdvertisementRepository;
    private final RestaurantRepository restaurantRepository;
    private final AdvertisementRepository advertisementRepository;

    private final AdvertisementMapper advertisementMapper;

    @Transactional
    public AdvertisementCreateResponse createAdvertisement(AdvertisementCreateRequest advertisementCreateRequest) {
        Advertisement advertisement = advertisementRepository.save(
            advertisementMapper.toEntity(advertisementCreateRequest));
        return advertisementMapper.toCreateResponse(advertisement);
    }

    public List<AdvertisementFindResponse> findAdvertisements() {
        return advertisementRepository.findAll().stream()
            .map(advertisementMapper::toFindResponse)
            .collect(Collectors.toList());
    }

    public AdvertisementFindResponse findAdvertisementById(Long advertisementId) {
        return advertisementMapper.toFindResponse(findAdvertisementEntityById(advertisementId));
    }

    @Transactional
    public void updateAdvertisementById(Long advertisementId, AdvertisementUpdateRequest advertisementUpdateRequest) {
        Advertisement advertisement = findAdvertisementEntityById(advertisementId);
        advertisementMapper.updateEntity(advertisementUpdateRequest, advertisement);
    }

    @Transactional
    public void deleteAdvertisementById(Long advertisementId) {
        advertisementRepository.deleteById(advertisementId);
    }

    @Transactional
    public void includeRestaurantInAdvertisement(Long advertisementId, Long restaurantId) {
        Advertisement advertisement = findAdvertisementEntityById(advertisementId);
        Restaurant restaurant = findRestaurantEntityById(restaurantId);

        RestaurantAdvertisement restaurantAdvertisement = new RestaurantAdvertisement(restaurant, advertisement);
    }

    @Transactional
    public void excludeRestaurantOutOfAdvertisement(Long advertisementId, Long restaurantId) {
        Advertisement advertisement = findAdvertisementEntityById(advertisementId);
        Restaurant restaurant = findRestaurantEntityById(restaurantId);

        RestaurantAdvertisement restaurantAdvertisement = restaurantAdvertisementRepository.findById(
                new RestaurantAdvertisementId(restaurantId, advertisementId))
            .orElseThrow(() -> new NotFoundException("가게(" + restaurantId + ")가 광고(" + restaurantId + ")에 포함되어 있지 않습니다."));

        advertisement.removeRestaurantAdvertisement(restaurantAdvertisement);
        restaurant.getRestaurantAdvertisements().remove(restaurantAdvertisement);
    }

    public Advertisement findAdvertisementEntityById(Long advertisementId) {
        return advertisementRepository.findById(advertisementId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 광고 아이디입니다."));
    }

    public Restaurant findRestaurantEntityById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 restaurantId 입니다."));
    }

}
