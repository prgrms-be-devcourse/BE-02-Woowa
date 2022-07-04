package com.example.woowa.restaurant.owner.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.woowa.restaurant.owner.entity.Owner;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import com.example.woowa.restaurant.restaurant.repository.RestaurantRepository;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OwnerServiceTest {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    @DisplayName("저장된 모든 사장님 객체를 조회할 수 있다.")
    void findOwers() {
        Long ownerId = ownerService.createOwner("abcd1234", "12345678", "test", "010-1234-5678");
        List<Owner> owners = ownerService.findOwners();

        assertThat(owners.get(0).getId()).isEqualTo(ownerId);
    }

    @Test
    @DisplayName("아이디를 통해 사장님 객체를 조회할 수 있다.")
    void findOwnerById() {
        Long ownerId = ownerService.createOwner("abcd1234", "12345678", "test", "010-1234-5678");
        Owner owner = ownerService.findOwnerById(ownerId);

        assertThat(owner.getId()).isEqualTo(ownerId);
    }

    @Test
    @DisplayName("로그인아이디를 통해 사장님 객체를 조회할 수 있다.")
    void findOwnerByLoginId() {
        Long ownerId = ownerService.createOwner("abcd1234", "12345678", "test", "010-1234-5678");
        Owner owner = ownerService.findOwnerByLoginId("abcd1234");

        assertThat(owner.getId()).isEqualTo(ownerId);
    }

    @Test
    @DisplayName("새로운 사장님 객체를 생성할 수 있다.")
    void createOwner() {
        Long ownerId = ownerService.createOwner("abcd1234", "12345678", "test", "010-1234-5678");
        Owner owner = ownerService.findOwnerById(ownerId);

        assertThat(owner.getId()).isEqualTo(ownerId);
    }

    @Test
    @DisplayName("사장님 객체의 로그인 비밀번호를 변경할 수 있다.")
    void changeOwnerLoginPassword() {
        Long ownerId = ownerService.createOwner("abcd1234", "12345678", "test", "010-1234-5678");
        ownerService.changeOwnerLoginPassword(ownerId, "abcd4321");
        Owner owner = ownerService.findOwnerById(ownerId);

        assertThat(owner.getLoginPassword()).isEqualTo("abcd4321");
    }

    @Test
    @DisplayName("사장님 객체의 로그인 비밀번호를 변경할 수 있다.")
    void changeOwnerName() {
        Long ownerId = ownerService.createOwner("abcd1234", "12345678", "test", "010-1234-5678");
        ownerService.changeOwnerName(ownerId, "tset");
        Owner owner = ownerService.findOwnerById(ownerId);

        assertThat(owner.getName()).isEqualTo("tset");
    }

    @Test
    @DisplayName("사장님 객체의 전화번호를 변경할 수 있다.")
    void changePhoneNumber() {
        Long ownerId = ownerService.createOwner("abcd1234", "12345678", "test", "010-1234-5678");
        ownerService.changePhoneNumber(ownerId, "010-5678-1234");
        Owner owner = ownerService.findOwnerById(ownerId);

        assertThat(owner.getPhoneNumber()).isEqualTo("010-5678-1234");
    }

    @Test
    @DisplayName("저장된 사장님 객체를 삭제할 수 있다.")
    void deleteOwner() {
        Long ownerId = ownerService.createOwner("abcd1234", "12345678", "test", "010-1234-5678");
        ownerService.deleteOwner(ownerId);

        List<Owner> owners = ownerService.findOwners();
        assertThat(owners.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("사장님은 새로운 가게를 등록할 수 있다.")
    void registerRestaurant() {
        Long ownerId = ownerService.createOwner("abcd1234", "12345678", "test", "010-1234-5678");

        Restaurant restaurant1 = restaurantRepository.save(Restaurant.createRestaurant("테스트 레스토랑1", "1234567890",
            LocalTime.now(), LocalTime.now().plusHours(10), true,
            "010-123-4567", "테스트용 임시 레스토랑 생성입니다.", "서울시 종로구"));
        Restaurant restaurant2 = restaurantRepository.save(Restaurant.createRestaurant("테스트 레스토랑2", "1234567890",
            LocalTime.now(), LocalTime.now().plusHours(10), true,
            "010-123-4567", "테스트용 임시 레스토랑 생성입니다.", "서울시 종로구"));;

        ownerService.registerRestaurant(ownerId, restaurant1.getId());
        ownerService.registerRestaurant(ownerId, restaurant2.getId());

        assertThat(ownerService.findRestaurantsByOwnerId(ownerId).size()).isEqualTo(2);
    }

    @Test
    @DisplayName("사장님은 등록된 가게를 삭제할 수 있다.")
    void removeRestaurant() {
        Long ownerId = ownerService.createOwner("abcd1234", "12345678", "test", "010-1234-5678");

        Restaurant restaurant1 = restaurantRepository.save(Restaurant.createRestaurant("테스트 레스토랑1", "1234567890",
            LocalTime.now(), LocalTime.now().plusHours(10), true,
            "010-123-4567", "테스트용 임시 레스토랑 생성입니다.", "서울시 종로구"));
        Restaurant restaurant2 = restaurantRepository.save(Restaurant.createRestaurant("테스트 레스토랑2", "1234567890",
            LocalTime.now(), LocalTime.now().plusHours(10), true,
            "010-123-4567", "테스트용 임시 레스토랑 생성입니다.", "서울시 종로구"));;

        ownerService.registerRestaurant(ownerId, restaurant1.getId());
        ownerService.registerRestaurant(ownerId, restaurant2.getId());
        ownerService.removeRestaurant(ownerId, restaurant1.getId());

        List<Restaurant> restaurants = ownerService.findRestaurantsByOwnerId(ownerId);
        Optional<Restaurant> deletedRestaurant = restaurantRepository.findById(restaurant1.getId());

        assertThat(restaurants.size()).isEqualTo(1);
        assertThat(deletedRestaurant.isPresent()).isFalse();
    }

}