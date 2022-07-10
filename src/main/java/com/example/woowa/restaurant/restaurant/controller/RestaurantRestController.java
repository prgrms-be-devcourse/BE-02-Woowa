package com.example.woowa.restaurant.restaurant.controller;

import com.example.woowa.restaurant.restaurant.dto.request.RestaurantCreateRequest;
import com.example.woowa.restaurant.restaurant.dto.request.RestaurantUpdateRequest;
import com.example.woowa.restaurant.restaurant.dto.response.RestaurantCreateResponse;
import com.example.woowa.restaurant.restaurant.dto.response.RestaurantFindResponse;
import com.example.woowa.restaurant.restaurant.service.RestaurantService;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(value = "baemin/v1/")
@RestController
public class RestaurantRestController {

    private final RestaurantService restaurantService;

    @PostMapping(value = "owners/{ownerId}/restaurants", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantCreateResponse> createRestaurantByOwnerId(final @PathVariable Long ownerId,
        final @Valid @RequestBody RestaurantCreateRequest restaurantCreateRequest) {
        RestaurantCreateResponse newRestaurant = restaurantService.createRestaurantByOwnerId(ownerId,
            restaurantCreateRequest);
        return new ResponseEntity<>(newRestaurant, HttpStatus.CREATED);
    }

    @GetMapping(value = "owners/{ownerId}/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RestaurantFindResponse>> findAllRestaurantsByOwnerId(final @PathVariable Long ownerId) {
        List<RestaurantFindResponse> restaurants = restaurantService.findRestaurantsByOwnerId(ownerId);
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @PutMapping(value = "owners/{ownerId}/restaurants/{restaurantId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateRestaurantByOwnerIdAndRestaurantId(final @PathVariable Long ownerId, final @PathVariable Long restaurantId,
        final @Valid @RequestBody RestaurantUpdateRequest restaurantUpdateRequest) {
        restaurantService.updateRestaurantById(ownerId, restaurantId, restaurantUpdateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "owners/{ownerId}/restaurants/{restaurantId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteRestaurantByOwnerIdAndRestaurantId(final @PathVariable Long ownerId,
        final @PathVariable Long restaurantId) {
        restaurantService.deleteRestaurantByOwnerIdAndRestaurantId(ownerId, restaurantId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "advertisements/{advertisementId}/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RestaurantFindResponse>> findAllRestaurantsByAdvertisementId(final @PathVariable Long advertisementId) {
        List<RestaurantFindResponse> restaurants = restaurantService.findRestaurantsByAdvertisementId(advertisementId);
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @GetMapping(value = "categories/{categoryId}/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RestaurantFindResponse>> findAllRestaurantsByCategoryId(final @PathVariable Long categoryId) {
        List<RestaurantFindResponse> restaurants = restaurantService.findRestaurantsByCategoryId(categoryId);
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @GetMapping(value = "restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RestaurantFindResponse>> findAllRestaurants(@RequestParam(required = false) Long areaCodeId) {
        if (Objects.nonNull(areaCodeId)) {
            return ResponseEntity.ok(restaurantService.findRestaurantByAreaCode(areaCodeId));
        }
        List<RestaurantFindResponse> restaurants = restaurantService.findRestaurants();
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @GetMapping(value = "restaurants/{restaurantId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantFindResponse> findRestaurantById(final @PathVariable Long restaurantId) {
        RestaurantFindResponse restaurant = restaurantService.findRestaurantById(restaurantId);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @PatchMapping(value = "owners/{ownerId}/restaurants/{restaurantId}")
    public ResponseEntity<Void> changeRestaurantState(final @PathVariable Long ownerId, final @PathVariable Long restaurantId,
        final @RequestParam(value = "isOpen") Boolean isOpen) {
        if (isOpen) {
            restaurantService.openRestaurant(ownerId, restaurantId);
        }
        else {
            restaurantService.closeRestaurant(ownerId, restaurantId);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(value = "owners/{ownerId}/restaurants/{restaurantId}/categories/add")
    public ResponseEntity<Void> addCategory(final @PathVariable Long ownerId, final @PathVariable Long restaurantId,
        final @RequestParam String categoryId) {
        restaurantService.addCategory(ownerId, restaurantId, Long.parseLong(categoryId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(value = "owners/{ownerId}/restaurants/{restaurantId}/categories/remove")
    public ResponseEntity<Void> removeCategory(final @PathVariable Long ownerId, final @PathVariable Long restaurantId,
        final @RequestParam String cateogoryId) {
        restaurantService.removeCategory(ownerId, restaurantId, Long.parseLong(cateogoryId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
