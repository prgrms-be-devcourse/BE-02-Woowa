package com.example.woowa.delivery.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.woowa.delivery.entity.AreaCode;
import com.example.woowa.delivery.entity.DeliveryArea;
import com.example.woowa.restaurant.owner.entity.Owner;
import com.example.woowa.restaurant.owner.repository.OwnerRepository;
import com.example.woowa.restaurant.owner.service.OwnerService;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import com.example.woowa.restaurant.restaurant.repository.RestaurantRepository;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class DeliveryAreaRepositoryTest {

    @Autowired
    DeliveryAreaRepository deliveryAreaRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    AreaCodeRepository areaCodeRepository;

    @Autowired
    OwnerRepository ownerRepository;

    Restaurant restaurant;

    DeliveryArea deliveryArea;
    AreaCode savedAreaCode;
    @Autowired
    OwnerService ownerService;

    @BeforeEach
    void init() {
        savedAreaCode = areaCodeRepository.save(
                new AreaCode("1111010100", "서울특별시 종로구 청운동", false));


        restaurant = restaurantRepository.save(
                Restaurant.createRestaurant("김밥나라", "000-00-00000", LocalTime.of(9, 0, 0),
                        LocalTime.of(23, 0, 0), true, "000-0000-0000", "맛있는 김밥나라", "서울 특별시 종로구"));

        deliveryArea = deliveryAreaRepository.save(
                new DeliveryArea(savedAreaCode, restaurant, 3000));

        Owner owner = ownerRepository.save(
                new Owner("dev12", "programmers12!", "kim", "000-0000-0000"));
        restaurant.setOwner(owner);
    }

    @Test
    @DisplayName("레스토랑의 배달 가능 지역을 조회한다.")
    void findByRestaurantTest() {
        // Given

        // When
        Optional<DeliveryArea> deliveryArea = deliveryAreaRepository.findByRestaurantAndAddress(
                restaurant, "서울특별시 종로구 청운동");

        // Then
        assertThat(deliveryArea).isNotEmpty();
        assertThat(deliveryArea).usingRecursiveComparison().isEqualTo(deliveryArea);
    }

    @Test
    @DisplayName("지역코드별 배달 지역을 조회한다.")
    void findByAreaCodeTest() {
        // When
        List<DeliveryArea> deliveryAreas = deliveryAreaRepository.findByAreaCode(savedAreaCode);

        // Then
        assertThat(deliveryAreas.size()).isEqualTo(1);
        assertThat(deliveryAreas).containsExactly(deliveryArea);
    }
}