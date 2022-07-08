package com.example.woowa.restaurant.restaurant.service;

import com.example.woowa.common.exception.ErrorMessage;
import com.example.woowa.common.exception.NotFoundException;
import com.example.woowa.delivery.entity.AreaCode;
import com.example.woowa.delivery.entity.DeliveryArea;
import com.example.woowa.delivery.service.AreaCodeService;

import com.example.woowa.restaurant.category.entity.Category;
import com.example.woowa.restaurant.category.service.CategoryService;
import com.example.woowa.restaurant.owner.entity.Owner;
import com.example.woowa.restaurant.owner.service.OwnerService;
import com.example.woowa.restaurant.restaurant.dto.request.RestaurantCreateRequest;
import com.example.woowa.restaurant.restaurant.dto.request.RestaurantUpdateRequest;
import com.example.woowa.restaurant.restaurant.dto.response.RestaurantCreateResponse;
import com.example.woowa.restaurant.restaurant.dto.response.RestaurantFindResponse;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import com.example.woowa.restaurant.restaurant.mapper.RestaurantMapper;
import com.example.woowa.restaurant.restaurant.repository.RestaurantRepository;
import com.example.woowa.restaurant.restaurntat_category.entity.RestaurantCategory;
import com.example.woowa.restaurant.restaurntat_category.entity.RestaurantCategoryId;
import com.example.woowa.restaurant.restaurntat_category.repository.RestaurantCategoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantCategoryRepository restaurantCategoryRepository;

    private final CategoryService categoryService;
    private final OwnerService ownerService;
    private final AreaCodeService areaCodeService;

    private final RestaurantMapper restaurantMapper;

    @Transactional
    public RestaurantCreateResponse createRestaurantByOwnerId(Long ownerId,
        RestaurantCreateRequest restaurantCreateRequest) {
        Owner owner = ownerService.findOwnerEntityById(ownerId);
        Restaurant restaurant = restaurantRepository.save(
            restaurantMapper.toEntity(restaurantCreateRequest));
        restaurantCreateRequest.getCategoryIds().forEach(categoryId -> {
            Category category = categoryService.findCategoryEntityById(categoryId);
            RestaurantCategory restaurantCategory = new RestaurantCategory(restaurant, category);
        });
        owner.addRestaurant(restaurant);

        return restaurantMapper.toCreateResponseDto(restaurant);
    }

    public List<RestaurantFindResponse> findRestaurants() {
        return restaurantRepository.findAll().stream()
            .map(restaurantMapper::toFindResponseDto)
            .collect(Collectors.toList());
    }

    public List<RestaurantFindResponse> findRestaurantsByOwnerId(Long ownerId) {
        Owner owner = ownerService.findOwnerEntityById(ownerId);
        return restaurantRepository.findByOwner(owner).stream().
            map(restaurantMapper::toFindResponseDto).
            collect(Collectors.toList());
    }

    public RestaurantFindResponse findRestaurantById(Long restaurantId) {
        return restaurantMapper.toFindResponseDto(findRestaurantEntityById(restaurantId));
    }

    public List<RestaurantFindResponse> findRestaurantsByAdvertisementId(Long advertisementId) {
        return restaurantRepository.findByAdvertisementId(advertisementId).stream()
            .map(restaurantMapper::toFindResponseDto)
            .collect(Collectors.toList());
    }

    public List<RestaurantFindResponse> findRestaurantsByCategoryId(Long categoryId) {
        return restaurantRepository.findByCategoryId(categoryId).stream()
            .map(restaurantMapper::toFindResponseDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteRestaurantByOwnerIdAndRestaurantId(Long ownerId, Long restaurantId) {
        Owner owner = ownerService.findOwnerEntityById(ownerId);
        Restaurant restaurant = findRestaurantEntityByOwnerIdAndRestaurantId(ownerId, restaurantId);

        owner.removeRestaurant(restaurant);
    }

    @Transactional
    public void updateRestaurantById(Long ownerId, Long restaurantId,
        RestaurantUpdateRequest restaurantUpdateRequest) {
        Restaurant restaurant = findRestaurantEntityByOwnerIdAndRestaurantId(ownerId, restaurantId);
        restaurantMapper.updateEntity(restaurantUpdateRequest, restaurant);
    }

    @Transactional
    public void openRestaurant(Long ownerId, Long restaurantId) {
        Restaurant restaurant = findRestaurantEntityByOwnerIdAndRestaurantId(ownerId, restaurantId);
        restaurant.openRestaurant();
    }

    @Transactional
    public void closeRestaurant(Long ownerId, Long restaurantId) {
        Restaurant restaurant = findRestaurantEntityByOwnerIdAndRestaurantId(ownerId, restaurantId);
        restaurant.closeRestaurant();
    }

    @Transactional
    public void setPermitted(Long restaurantId) {
        Restaurant restaurant = findRestaurantEntityById(restaurantId);
        restaurant.setPermitted();
    }

    @Transactional
    public void addCategory(Long ownerId, Long restaurantId, Long categoryId) {
        Restaurant restaurant = findRestaurantEntityByOwnerIdAndRestaurantId(ownerId, restaurantId);
        Category category = categoryService.findCategoryEntityById(categoryId);

        RestaurantCategory restaurantCategory = new RestaurantCategory(restaurant, category);
    }

    @Transactional
    public void removeCategory(Long ownerId, Long restaurantId, Long categoryId) {
        Restaurant restaurant = findRestaurantEntityByOwnerIdAndRestaurantId(ownerId, restaurantId);
        Category category = categoryService.findCategoryEntityById(categoryId);

        RestaurantCategory restaurantCategory = restaurantCategoryRepository.findById(
                new RestaurantCategoryId(restaurantId, categoryId))
            .orElseThrow(() -> new IllegalArgumentException("이 가게는 해당 카테고리에 속하지 않습니다."));

        restaurantCategoryRepository.delete(restaurantCategory);
    }

    public Restaurant findRestaurantEntityByOwnerIdAndRestaurantId(Long ownerId,
        Long restaurantId) {
        return ownerService.findOwnerEntityById(ownerId).getRestaurants().stream().
            filter(r -> r.getId() == restaurantId).
            findFirst().
            orElseThrow(() ->
                new NotFoundException(
                    "사장님(" + ownerId + ")은 가게(" + restaurantId + ")를 소유하고 있지 않습니다."));
    }

    @Transactional
    public void setDeliveryArea(Long restaurantId, Long areaCodeId, Integer deleiveryFee) {
        Restaurant restaurant = findRestaurantEntityById(restaurantId);
        AreaCode areaCode = areaCodeService.findEntityById(areaCodeId);

        DeliveryArea deliveryArea = new DeliveryArea(areaCode, restaurant, deleiveryFee);
    }

    public Restaurant findRestaurantEntityById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_RESTAURANT.getMessage()));
    }

    public List<RestaurantFindResponse> findRestaurantsIsPermittedIsFalse() {
        return restaurantRepository.findRestaurantByIsPermittedIsFalse().stream()
            .map(restaurantMapper::toFindResponseDto)
            .collect(Collectors.toList());
    }

    public List<RestaurantFindResponse> findRestaurantByAreaCode(Long areaCodeId) {
        return restaurantRepository.findAll().stream().filter(restaurant -> {
                for (DeliveryArea deliveryArea : restaurant.getDeliveryAreas()) {
                    if (deliveryArea.getAreaCode().getId().equals(areaCodeId)) {
                        return true;
                    }
                }
                return false;
            }).map(restaurantMapper::toFindResponseDto)
            .collect(Collectors.toList());
    }

}
