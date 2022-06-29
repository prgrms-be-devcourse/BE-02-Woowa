package com.example.woowa.order.order.dto.customer;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderListCustomerResponse {

    private Boolean hasNextPage;

    private Integer size;

    private List<OrderCustomerResponse> menus;
}
