package com.example.woowa.order.order.dto.restaurant;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderListRestaurantResponse {

    private Boolean hasNextPage;

    private Integer size;

    private List<OrderRestaurantResponse> orders;

}
