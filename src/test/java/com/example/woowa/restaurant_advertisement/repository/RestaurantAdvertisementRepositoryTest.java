package com.example.woowa.restaurant_advertisement.repository;

import static org.assertj.core.api.Assertions.assertThat;


import com.example.woowa.restaurant.advertisement.entity.Advertisement;
import com.example.woowa.restaurant.advertisement.enums.RateType;
import com.example.woowa.restaurant.advertisement.enums.UnitType;
import com.example.woowa.restaurant.advertisement.repository.AdvertisementRepository;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import com.example.woowa.restaurant.restaurant.repository.RestaurantRepository;
import com.example.woowa.restaurant.restaurant_advertisement.entity.RestaurantAdvertisement;
import com.example.woowa.restaurant.restaurant_advertisement.entity.RestaurantAdvertisementId;
import com.example.woowa.restaurant.restaurant_advertisement.repository.RestaurantAdvertisementRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;

@SpringBootTest
class RestaurantAdvertisementRepositoryTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Autowired
    private RestaurantAdvertisementRepository restaurantAdvertisementRepository;

    @Test
    @DisplayName("restaurant과 advertisement의 기본키를 복합키로 가진 restaurant_advertisement테이블을 생성한다.")
    void testSaveRestaurantAdvertisement() {
        // Given
        Restaurant restaurant = restaurantRepository.save(Restaurant.createRestaurant("테스트 레스토랑1", "1234567890",
            LocalTime.now(), LocalTime.now().plusHours(10), true,
            "010-123-4567", "테스트용 임시 레스토랑 생성입니다.", "서울시 종로구"));

        Advertisement advertisement = advertisementRepository.save(
            new Advertisement("울트라콜", UnitType.MOTHLY, RateType.PERCENT, 10, "test ad", 10)
        );

        // When
        restaurantAdvertisementRepository.save(new RestaurantAdvertisement(restaurant, advertisement));

        // Then

        RestaurantAdvertisementId restaurantAdvertisementId = new RestaurantAdvertisementId(
            restaurant.getId(), advertisement.getId());
        RestaurantAdvertisement restaurantAdvertisement = restaurantAdvertisementRepository.findById(
                restaurantAdvertisementId).get();

        assertThat(restaurantAdvertisement.getRestaurant().getId()).isEqualTo(restaurant.getId());
        assertThat(restaurantAdvertisement.getAdvertisement().getId()).isEqualTo(advertisement.getId());
    }

}