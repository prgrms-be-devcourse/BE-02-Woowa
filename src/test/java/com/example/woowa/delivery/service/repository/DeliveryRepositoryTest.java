package com.example.woowa.delivery.service.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.woowa.TestInitUtil;
import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.entity.CustomerAddress;
import com.example.woowa.customer.customer.entity.CustomerGrade;
import com.example.woowa.customer.customer.repository.CustomerGradeRepository;
import com.example.woowa.customer.customer.repository.CustomerRepository;
import com.example.woowa.customer.voucher.entity.Voucher;
import com.example.woowa.customer.voucher.repository.VoucherRepository;
import com.example.woowa.delivery.entity.Delivery;
import com.example.woowa.delivery.enums.DeliveryStatus;
import com.example.woowa.delivery.repository.DeliveryRepository;
import com.example.woowa.delivery.repository.RiderRepository;
import com.example.woowa.order.order.entity.Cart;
import com.example.woowa.order.order.entity.Order;
import com.example.woowa.order.order.repository.OrderRepository;
import com.example.woowa.restaurant.menu.entity.Menu;
import com.example.woowa.restaurant.menu.repository.MenuRepository;
import com.example.woowa.restaurant.menugroup.entity.MenuGroup;
import com.example.woowa.restaurant.menugroup.repository.MenuGroupRepository;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import com.example.woowa.restaurant.restaurant.repository.RestaurantRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({SpringExtension.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeliveryRepositoryTest {

    @Autowired
    DeliveryRepository deliveryRepository;

    @Autowired
    RiderRepository riderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    VoucherRepository voucherRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Autowired
    CustomerGradeRepository customerGradeRepository;

    Delivery delivery = null;

    @BeforeEach
    @DisplayName("order를 저장할 수 있다.")
    public void save() {
        CustomerGrade customerGrade = TestInitUtil.initCustomerGrade();
        Customer customer = TestInitUtil.initCustomer(customerGrade);
        Voucher voucher = TestInitUtil.initVoucher();
        Restaurant restaurant = TestInitUtil.initRestaurant();
        MenuGroup menuGroup = TestInitUtil.initMenuGroup(restaurant);
        List<Menu> menuList = TestInitUtil.initMenus(menuGroup);
        List<Cart> cartList = TestInitUtil.initCarts(menuList);
        Order order = TestInitUtil.initOrder(customer, restaurant, voucher, cartList);
        CustomerAddress customerAddress = customer.getCustomerAddresses().get(0);
        delivery = TestInitUtil.initDelivery(customer, customerAddress, restaurant, order);

        customerGradeRepository.save(customerGrade);
        customerRepository.save(customer);
        voucherRepository.save(voucher);
        restaurantRepository.save(restaurant);
        menuGroupRepository.save(menuGroup);
        menuRepository.saveAll(menuList);
        orderRepository.save(order);
        Delivery saveDelivery = deliveryRepository.save(delivery);

        assertThat(saveDelivery).usingRecursiveComparison().isEqualTo(delivery);
    }

    @Test
    public void findById() {
        Delivery retrieveDelivery = deliveryRepository.findById(this.delivery.getId())
            .orElseThrow(RuntimeException::new);

        assertThat(retrieveDelivery).usingRecursiveComparison().isEqualTo(delivery);
        assertThat(retrieveDelivery.getOrder()).isNotNull();
        assertThat(retrieveDelivery.getRider()).isNotNull();
        assertThat(retrieveDelivery.getDeliveryStatus()).isEqualTo(DeliveryStatus.DELIVERY_WAITING);
    }

}