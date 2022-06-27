package com.example.woowa.delivery.service;

import com.example.woowa.delivery.entity.AreaCode;
import com.example.woowa.delivery.entity.DeliveryArea;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import com.example.woowa.restaurant.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DeliveryAreaServiceTest {
    @Autowired
    DeliveryAreaService deliveryAreaService;

    @Autowired
    AreaCodeService areaCodeService;

    @Autowired
    RestaurantRepository restaurantRepository;

    @BeforeEach
    @DisplayName("저장")
    public void save() {
        AreaCode areaCode = areaCodeService.findByAddress("서울특별시 종로구");
        Restaurant restaurant = Restaurant.createRestaurant("테스트 레스토랑", "1234567890",
                LocalTime.now(), LocalTime.now(), true,
                "010-123-4567", "테스트용 임시 레스토랑 생성입니다.", "서울시 종로구");

        DeliveryArea deliveryArea = new DeliveryArea(areaCode, restaurant);
        restaurant.addDeliveryArea(deliveryArea);
        areaCode.addDeliveryArea(deliveryArea);

        restaurantRepository.save(restaurant);
        deliveryAreaService.save(deliveryArea);
    }

    @Test
    @DisplayName("AreaCode를 통해 DeliveryArea를 조회할 수 있다.")
    @Transactional
    public void findByAreaCode() {
        String address = "서울특별시 종로구";
        AreaCode areaCode = areaCodeService.findByAddress(address);
        DeliveryArea deliveryArea = areaCode.getDeliveryAreaList().get(0);

        assertThat(deliveryArea.getAreaCode().getDefaultAddress()).isEqualTo(address);
        assertThat(deliveryArea.getDeliveryFee()).isEqualTo(0);
    }

    @Test
    @DisplayName("Restaurant을 통해 DeliveryArea를 조회할 수 있다.")
    @Transactional
    public void findByRestaurant() {
        String name = "테스트 레스토랑";
        List<Restaurant> restaurantList = restaurantRepository.findByName(name);
        Restaurant restaurant = restaurantList.get(0);
        DeliveryArea deliveryArea = restaurant.getDeliveryAreas().get(0);

        assertThat(deliveryArea.getDeliveryFee()).isEqualTo(0);
        assertThat(deliveryArea.getRestaurant().getName()).isEqualTo(name);
    }
}