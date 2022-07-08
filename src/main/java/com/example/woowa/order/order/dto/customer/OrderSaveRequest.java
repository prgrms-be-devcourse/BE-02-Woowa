package com.example.woowa.order.order.dto.customer;

import com.example.woowa.order.order.dto.cart.CartSaveRequest;
import com.example.woowa.order.order.enums.PaymentType;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class OrderSaveRequest {

    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])[a-zA-z0-9]{5,10}$", message = "최소 5글자에서 10글자, 특수문자를 제외한 영숫자가 포함된 아이디가 아닙니다.")
    private final String loginId;

    @NotNull(message = "레스토랑 ID는 필수로 입력되어야 합니다.")
    private final Long restaurantId;

    private final Long voucherId;

    @NotNull(message = "주문에 사용된 포인트는 필수입니다.")
    @PositiveOrZero(message = "사용된 포인트는 최소 0원 이상이어야 합니다.")
    private final Integer usePoint;

    @NotNull(message = "결제 수단은 필수입니다.")
    private final PaymentType paymentType;

    @NotBlank(message = "고객의 주문 주소는 필수입니다.")
    @Length(max = 100)
    private final String deliveryAddress;

    @Valid
    @NotNull(message = "장바구니는 필수입니다.")
    @Size(min = 1, message = "장바구니에는 최소 1개의 메뉴가 있어야 합니다.")
    private final List<CartSaveRequest> carts;
}
