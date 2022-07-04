package com.example.woowa.order.order.dto.customer;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderListCustomerResponse {

    private final Boolean hasNextPage;

    private final Integer size;

    private final List<OrderSummeryResponse> orders;
}
