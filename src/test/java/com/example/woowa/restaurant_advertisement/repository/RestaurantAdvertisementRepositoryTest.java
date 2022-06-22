package com.example.woowa.restaurant_advertisement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.woowa.advertisement.entity.Advertisement;
import com.example.woowa.advertisement.repository.AdvertisementRepository;
import com.example.woowa.restaurant.entity.Restaurant;
import com.example.woowa.restaurant.repository.RestaurantRepository;
import com.example.woowa.restaurant_advertisement.entity.RestaurantAdvertisement;
import com.example.woowa.restaurant_advertisement.entity.RestaurnatAdvertisementId;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        Restaurant restaurant = restaurantRepository.save(
            new Restaurant("테스트 레스토랑", "1234567890",
                LocalTime.now(), LocalTime.now(), true,
                "010-123-4567", "테스트용 임시 레스토랑 생성입니다.", "서울시 종로구"));

        Advertisement advertisement = advertisementRepository.save(
            new Advertisement("울트라콜", 10000, "테스트용 임시 광고입니다.")
        );

        // When
        restaurantAdvertisementRepository.save(new RestaurantAdvertisement(restaurant, advertisement));

        // Then
        RestaurnatAdvertisementId restaurnatAdvertisementId = new RestaurnatAdvertisementId(
            restaurant.getId(), advertisement.getId());
        RestaurantAdvertisement restaurantAdvertisement = restaurantAdvertisementRepository.findById(
            restaurnatAdvertisementId).get();

        assertThat(restaurantAdvertisement.getRestaurant().getId()).isEqualTo(restaurant.getId());
        assertThat(restaurantAdvertisement.getAdvertisement().getId()).isEqualTo(advertisement.getId());
    }

}