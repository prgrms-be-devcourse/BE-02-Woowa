package com.example.woowa.order.order.mapper;

import com.example.woowa.order.order.dto.cart.CartResponse;
import com.example.woowa.order.order.dto.cart.CartSaveRequest;
import com.example.woowa.order.order.dto.cart.CartSummeryResponse;
import com.example.woowa.order.order.entity.Cart;
import com.example.woowa.restaurant.menu.service.MenuService;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {MenuService.class})
public interface CartMapper {

    @Mappings({
            @Mapping(target = "menuName", expression = "java(cart.getMenu().getTitle())"),
            @Mapping(target = "totalPrice", expression = "java(cart.getMenu().getPrice() * cart.getQuantity())")
    })
    CartResponse toCartResponse(Cart cart);

    @Mapping(target = "menuTitle", source = "menu.title")
    CartSummeryResponse toCartSummeryResponse(Cart cart);

    List<CartResponse> toCartResponseList(List<Cart> carts);

    @Mappings({
            @Mapping(target = "menu", source = "menuId"),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "order", ignore = true)
    })
    Cart toCart(CartSaveRequest request);

    List<Cart> toCartList(List<CartSaveRequest> requests);
}
