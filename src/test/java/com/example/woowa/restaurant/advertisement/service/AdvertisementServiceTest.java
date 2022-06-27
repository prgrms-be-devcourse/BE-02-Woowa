package com.example.woowa.restaurant.advertisement.service;

import com.example.woowa.restaurant.advertisement.entity.Advertisement;
import com.example.woowa.restaurant.advertisement.enums.RateType;
import com.example.woowa.restaurant.advertisement.enums.UnitType;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import com.example.woowa.restaurant.restaurant.repository.RestaurantRepository;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class AdvertisementServiceTest {

    @Autowired
    private AdvertisementService advertisementService;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    @DisplayName("새로운 광고를 생성할 수 있다.")
    void createAdvertisement() {
        Long advertisementId = advertisementService.createAdvertisement("울트라콜", UnitType.PER_ORDER,
            RateType.PERCENT, 7, "test ad");

        Advertisement advertisement = advertisementService.findAdvertisementById(advertisementId);

        assertThat(advertisement.getId()).isEqualTo(advertisementId);
    }

    @Test
    @DisplayName("생성된 모든 광고 정보를 조회할 수 있다.")
    void findAdvertisements() {
        Long advertisementId1 = advertisementService.createAdvertisement("울트라콜", UnitType.PER_ORDER,
            RateType.PERCENT, 7, "test ad");
        Long advertisementId2 = advertisementService.createAdvertisement("오픈리스트", UnitType.PER_ORDER,
            RateType.PERCENT, 7, "test ad");

        List<Advertisement> advertisements = advertisementService.findAdvertisements();

        assertThat(advertisements.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("아이디를 통해 광고를 조회할 수 있다.")
    void findAdvertisementById() {
        Long advertisementId = advertisementService.createAdvertisement("울트라콜", UnitType.PER_ORDER,
            RateType.PERCENT, 7, "test ad");

        Advertisement advertisement = advertisementService.findAdvertisementById(advertisementId);

        assertThat(advertisement.getId()).isEqualTo(advertisementId);
    }

    @Test
    @DisplayName("이름을 통해 광고를 조회할 수 있다.")
    void findAdvertisementByName() {
        Long advertisementId = advertisementService.createAdvertisement("울트라콜", UnitType.PER_ORDER,
            RateType.PERCENT, 7, "test ad");

        Advertisement advertisement = advertisementService.findAdvertisementByTitle("울트라콜");

        assertThat(advertisement.getId()).isEqualTo(advertisementId);
    }

    @Test
    @DisplayName("광고 아이디를 통해 해당 광고에 포함된 모든 가게를 조회할 수 있다.")
    void findRestaurantsByAdvertisementId() {
        Long advertisementId = advertisementService.createAdvertisement("울트라콜", UnitType.PER_ORDER,
            RateType.PERCENT, 7, "test ad");
        Restaurant restaurant = restaurantRepository.save(Restaurant.createRestaurant("테스트 레스토랑", "1234567890",
            LocalTime.now(), LocalTime.now(), true,
            "010-123-4567", "테스트용 임시 레스토랑 생성입니다.", "서울시 종로구"));

//        advertisementService.includeRestaurantInAdvertisement(advertisementId, restaurant.getId());

        List<Restaurant> restaurants = advertisementService.findRestaurantsByAdvertisementId(
            advertisementId);
        assertThat(restaurants.get(0).getId()).isEqualTo(restaurant.getId());
    }

//    @Test
//    void findRestaurantByAdvertisementName() {
//        Long advertisementId = advertisementService.createAdvertisement("울트라콜", UnitType.PER_ORDER,
//            RateType.PERCENT, 7, "test ad");
//        Restaurant restaurant = restaurantRepository.save(Restaurant.createRestaurant("테스트 레스토랑", "1234567890",
//            LocalTime.now(), LocalTime.now(), true,
//            "010-123-4567", "테스트용 임시 레스토랑 생성입니다.", "서울시 종로구"));
//
//        advertisementService.includeRestaurantInAdvertisement(advertisementId, restaurant.getId());
//
//        List<Restaurant> restaurants = advertisementService.findRestaurantByAdvertisementTitle("울트라콜");
//        assertThat(restaurants.get(0).getId()).isEqualTo(restaurant.getId());
//    }

    @Test
    @DisplayName("광고명을 변경할 수 있다.")
    void changeAdvertisementName() {
        Long advertisementId = advertisementService.createAdvertisement("울트라콜", UnitType.PER_ORDER,
            RateType.PERCENT, 7, "test ad");

        advertisementService.changeAdvertisementTitle(advertisementId, "오픈리스트");
        Advertisement advertisement = advertisementService.findAdvertisementById(
            advertisementId);

        assertThat(advertisement.getTitle()).isEqualTo("오픈리스트");
    }

    @Test
    @DisplayName("광고의 적용단위를 변경할 수 있다.")
    void changeAdvertisementUnitType() {
        Long advertisementId = advertisementService.createAdvertisement("울트라콜", UnitType.PER_ORDER,
            RateType.PERCENT, 7, "test ad");

        advertisementService.changeAdvertisementUnitType(advertisementId, UnitType.MOTHLY);
        Advertisement advertisement = advertisementService.findAdvertisementById(
            advertisementId);

        assertThat(advertisement.getUnitType()).isEqualTo(UnitType.MOTHLY);
    }

    @Test
    @DisplayName("광고의 청구유형을 변경할 수 있다.")
    void changeAdvertisementRateType() {
        Long advertisementId = advertisementService.createAdvertisement("울트라콜", UnitType.PER_ORDER,
            RateType.PERCENT, 7, "test ad");

        advertisementService.changeAdvertisementRateType(advertisementId, RateType.FLAT);
        Advertisement advertisement = advertisementService.findAdvertisementById(
            advertisementId);

        assertThat(advertisement.getRateType()).isEqualTo(RateType.FLAT);
    }

    @Test
    @DisplayName("광고료를 변경할 수 있다.")
    void changeAdvertisementRate() {
        Long advertisementId = advertisementService.createAdvertisement("울트라콜", UnitType.PER_ORDER,
            RateType.PERCENT, 7, "test ad");

        advertisementService.changeAdvertisementRate(advertisementId, 10);
        Advertisement advertisement = advertisementService.findAdvertisementById(
            advertisementId);

        assertThat(advertisement.getRate()).isEqualTo(10);
    }

    @Test
    @DisplayName("광고 설명을 변경할 수 있다.")
    void changeAdvertisementDescription() {
        Long advertisementId = advertisementService.createAdvertisement("울트라콜", UnitType.PER_ORDER,
            RateType.PERCENT, 7, "test ad");

        advertisementService.changeAdvertisementDescription(advertisementId, "da tset");
        Advertisement advertisement = advertisementService.findAdvertisementById(
            advertisementId);

        assertThat(advertisement.getDescription()).isEqualTo("da tset");
    }

    @Test
    @DisplayName("광고를 삭제할 수 있다.")
    void deleteAdvertisement() {
        Long advertisementId = advertisementService.createAdvertisement("울트라콜", UnitType.PER_ORDER,
            RateType.PERCENT, 7, "test ad");

        advertisementService.deleteAdvertisement(advertisementId);

        assertThatThrownBy(() -> advertisementService.findAdvertisementById(advertisementId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 광고 아이디입니다.");
    }

//    @Test
//    @DisplayName("가게를 광고에 포함시킬 수 있다.")
//    void includeRestaurantInAdvertisement() {
//        Long advertisementId = advertisementService.createAdvertisement("울트라콜", UnitType.PER_ORDER,
//            RateType.PERCENT, 7, "test ad");
//        Restaurant restaurant = restaurantRepository.save(Restaurant.createRestaurant("테스트 레스토랑", "1234567890",
//            LocalTime.now(), LocalTime.now(), true,
//            "010-123-4567", "테스트용 임시 레스토랑 생성입니다.", "서울시 종로구"));
//
//        advertisementService.includeRestaurantInAdvertisement(advertisementId, restaurant.getId());
//
//        List<Restaurant> restaurants = advertisementService.findRestaurantsByAdvertisementId(
//            advertisementId);
//        assertThat(restaurants.get(0).getId()).isEqualTo(restaurant.getId());
//    }
//
//    @Test
//    @DisplayName("가게를 광고에서 제외시킬 수 있다.")
//    void excludeRestaurantInAdvertisement() {
//        Long advertisementId = advertisementService.createAdvertisement("울트라콜", UnitType.PER_ORDER,
//            RateType.PERCENT, 7, "test ad");
//        Restaurant restaurant = restaurantRepository.save(Restaurant.createRestaurant("테스트 레스토랑", "1234567890",
//            LocalTime.now(), LocalTime.now(), true,
//            "010-123-4567", "테스트용 임시 레스토랑 생성입니다.", "서울시 종로구"));
//
//        advertisementService.includeRestaurantInAdvertisement(advertisementId, restaurant.getId());
//
//        List<Restaurant> restaurantsAfterInclude = advertisementService.findRestaurantsByAdvertisementId(
//            advertisementId);
//        assertThat(restaurantsAfterInclude.get(0).getId()).isEqualTo(restaurant.getId());
//
//        advertisementService.excludeRestaurantInAdvertisement(advertisementId, restaurant.getId());
//
//        List<Restaurant> restaurantsAfterExclude = advertisementService.findRestaurantsByAdvertisementId(
//            advertisementId);
//        assertThat(restaurantsAfterExclude.isEmpty()).isTrue();
//    }

}