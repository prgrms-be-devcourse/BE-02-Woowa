package com.example.woowa.order.order.dto.cart;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class CartSaveRequest {

    @NotNull(message = "주문할 메뉴 ID는 필수입니다.")
    private final Long menuId;

    @NotNull(message = "주문 수량은 필수입니다.")
    @Positive(message = "주문 수량은 0보다 커야합니다.")
    private final Integer quantity;
}
