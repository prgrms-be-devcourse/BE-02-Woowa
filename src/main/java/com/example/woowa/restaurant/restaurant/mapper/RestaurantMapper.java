package com.example.woowa.restaurant.restaurant.mapper;

import com.example.woowa.restaurant.restaurant.dto.request.RestaurantCreateRequest;
import com.example.woowa.restaurant.restaurant.dto.response.RestaurantFindResponse;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface RestaurantMapper {

    @Mapping(target = "categories",
        expression = "java(restaurant.getRestaurantCategories().stream().map(restaurantCategory -> restaurantCategory.getCategory().getName()).collect(java.util.stream.Collectors.toList()))")
    @Mapping(target = "ownerId", expression = "java(restaurant.getOwner().getId())")
    RestaurantFindResponse toFindResponseDto(Restaurant restaurant);

    default Restaurant toEntity(RestaurantCreateRequest restaurantCreateRequest) {
        return Restaurant.createRestaurant(
            restaurantCreateRequest.getName(),
            restaurantCreateRequest.getBusinessNumber(),
            restaurantCreateRequest.getOpeningTime(),
        restaurantCreateRequest.getClosingTime(),
            restaurantCreateRequest.getIsOpen(),
            restaurantCreateRequest.getPhoneNumber(),
            restaurantCreateRequest.getDescription(),
            restaurantCreateRequest.getAddress());
    }

}
