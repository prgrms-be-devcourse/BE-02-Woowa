package com.example.woowa.delivery.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.woowa.TestInitUtil;
import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.entity.CustomerAddress;
import com.example.woowa.customer.customer.entity.CustomerGrade;
import com.example.woowa.customer.voucher.entity.Voucher;
import com.example.woowa.delivery.dto.DeliveryResponse;
import com.example.woowa.delivery.entity.Delivery;
import com.example.woowa.order.order.entity.Cart;
import com.example.woowa.order.order.entity.Order;
import com.example.woowa.restaurant.menu.entity.Menu;
import com.example.woowa.restaurant.menugroup.entity.MenuGroup;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class DeliveryMapperTest {

    private final DeliveryMapper deliveryMapper = Mappers.getMapper(DeliveryMapper.class);

    @Test
    @DisplayName("Delivery를 DeliveryResponse로 변환할 수 있다.")
    public void toResponse() {
        CustomerGrade customerGrade = TestInitUtil.initCustomerGrade();
        Customer customer = TestInitUtil.initCustomer(customerGrade);
        Voucher voucher = TestInitUtil.initVoucher();
        Restaurant restaurant = TestInitUtil.initRestaurant();
        MenuGroup menuGroup = TestInitUtil.initMenuGroup(restaurant);
        List<Menu> menuList = TestInitUtil.initMenus(menuGroup);
        List<Cart> cartList = TestInitUtil.initCarts(menuList);
        Order order = TestInitUtil.initOrder(customer, restaurant, voucher, cartList);
        CustomerAddress customerAddress = customer.getCustomerAddresses().get(0);
        Delivery delivery = TestInitUtil.initDelivery(customer, customerAddress, restaurant, order);

        DeliveryResponse deliveryResponse = deliveryMapper.toResponse(delivery);

        assertThat(deliveryResponse.getId()).isEqualTo(delivery.getId());
        assertThat(deliveryResponse.getDeliveryFee()).isEqualTo(delivery.getDeliveryFee());
        assertThat(deliveryResponse.getDeliveryStatus()).isEqualTo(delivery.getDeliveryStatus());
        assertThat(deliveryResponse.getArrivalTime()).isEqualTo(delivery.getArrivalTime());
        assertThat(deliveryResponse.getCustomerAddress()).isEqualTo(delivery.getCustomerAddress());
        assertThat(deliveryResponse.getRestaurantAddress()).isEqualTo(
            delivery.getRestaurantAddress());
    }
}