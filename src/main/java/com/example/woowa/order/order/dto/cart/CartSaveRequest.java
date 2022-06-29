package com.example.woowa.order.order.dto.cart;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CartSaveRequest {

    @NotNull
    Long menuId;

    @NotNull
    @Positive
    Integer quantity;
}
