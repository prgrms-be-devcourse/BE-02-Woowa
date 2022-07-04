package com.example.woowa.order.order.dto.restaurant;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderListRestaurantResponse {

    private final Boolean hasNextPage;

    private final Integer size;

    private final List<OrderRestaurantResponse> orders;

}
