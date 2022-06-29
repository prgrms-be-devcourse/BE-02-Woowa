package com.example.woowa.order.order.dto.customer;

import com.example.woowa.order.order.dto.cart.CartSaveRequest;
import com.example.woowa.order.order.enums.PaymentType;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderSaveRequest {

    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])[a-zA-z0-9]{5,10}$", message = "최소 5글자에서 10글자, 특수문자를 제외한 영숫자가 포함된 아이디가 아닙니다.")
    private String loginId;

    @NotNull
    Long restaurantId;

    @NotNull
    Long voucherId;

    @PositiveOrZero
    int usePoint;

    @NotNull
    PaymentType paymentType;

    @Valid
    @NotNull
    List<CartSaveRequest> carts;
}
