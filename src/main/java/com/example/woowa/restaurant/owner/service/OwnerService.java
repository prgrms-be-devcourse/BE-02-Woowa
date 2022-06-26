package com.example.woowa.restaurant.owner.service;

import com.example.woowa.restaurant.owner.entity.Owner;
import com.example.woowa.restaurant.owner.repository.OwnerRepository;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import com.example.woowa.restaurant.restaurant.service.RestaurantService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final RestaurantService restaurantService;

    @Transactional
    public Long createOwner(String loginId, String loginPassword, String name, String phoneNumber) {
        return ownerRepository.save(new Owner(loginId, loginPassword, name, phoneNumber)).getId();
    }

    public List<Owner> findOwners() {
        return ownerRepository.findAll();
    }

    public Owner findOwnerById(Long ownerId) {
        return ownerRepository.findById(ownerId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사장님 아이디입니다."));
    }

    public Owner findOwnerByLoginId(String loginId) {
        return ownerRepository.findOwnerByLoginId(loginId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사장님 로그인 아이디입니다."));
    }

    public List<Restaurant> findRestaurantsByOwnerId(Long ownerId) {
        return new ArrayList<>(findOwnerById(ownerId).getRestaurants());
    }

    @Transactional
    public void changeOwnerLoginPassword(Long ownerId, String loginPassword) {
        findOwnerById(ownerId).changeLoginPassword(loginPassword);
    }

    @Transactional
    public void changeOwnerLoginPassword(String loginId, String loginPassword) {
        findOwnerByLoginId(loginId).changeLoginPassword(loginPassword);
    }

    @Transactional
    public void changeOwnerName(Long ownerId, String name) {
        findOwnerById(ownerId).changeName(name);
    }

    @Transactional
    public void changePhoneNumber(Long ownerId, String phoneNumber) {
        findOwnerById(ownerId).changePhoneNumber(phoneNumber);
    }

    @Transactional
    public void deleteOwner(Long ownerId) {
        ownerRepository.deleteById(ownerId);
    }

    @Transactional
    public void registerRestaurant(Long ownerId, Long restaurantId) {
        Owner owner = findOwnerById(ownerId);
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);

        owner.addRestaurant(restaurant);
    }

    @Transactional
    public void removeRestaurant(Long ownerId, Long restaurantId) {
        Owner owner = findOwnerById(ownerId);
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);

        owner.getRestaurants().remove(restaurant);
    }

}
