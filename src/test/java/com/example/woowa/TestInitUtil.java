package com.example.woowa;

import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.entity.CustomerAddress;
import com.example.woowa.customer.customer.entity.CustomerGrade;
import com.example.woowa.customer.voucher.entity.Voucher;
import com.example.woowa.customer.voucher.enums.EventType;
import com.example.woowa.customer.voucher.enums.VoucherType;
import com.example.woowa.delivery.entity.AreaCode;
import com.example.woowa.delivery.entity.Delivery;
import com.example.woowa.delivery.entity.Rider;
import com.example.woowa.delivery.entity.RiderAreaCode;
import com.example.woowa.order.order.entity.Cart;
import com.example.woowa.order.order.entity.Order;
import com.example.woowa.order.order.enums.PaymentType;
import com.example.woowa.restaurant.menu.entity.Menu;
import com.example.woowa.restaurant.menu.enums.MenuStatus;
import com.example.woowa.restaurant.menugroup.entity.MenuGroup;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class TestInitUtil {

    public static CustomerGrade initCustomerGrade() {
        return new CustomerGrade(4, "고마운분", 1000, 1);
    }

    public static Customer initCustomer(CustomerGrade customerGrade) {

        Customer customer = new Customer("dev12", "Programmers123!", LocalDate.of(1997, 05, 17),
            customerGrade);

        AreaCode areaCode = new AreaCode("12344", "서울시 관악구 신림동", true);
        CustomerAddress customerAddress = new CustomerAddress(areaCode, "아파트 101호", "회사",
            customer);
        customer.addCustomerAddress(customerAddress);
        return customer;
    }

    public static Customer initCustomer() {
        CustomerGrade customerGrade = initCustomerGrade();
        Customer customer = new Customer("dev12", "Programmers123!", LocalDate.of(1997, 05, 17),
            customerGrade);

        AreaCode areaCode = new AreaCode("12344", "서울시 관악구 신림동", true);
        CustomerAddress customerAddress = new CustomerAddress(areaCode, "아파트 101호", "회사",
            customer);
        customer.addCustomerAddress(customerAddress);
        return customer;
    }

    public static Voucher initVoucher() {
        return new Voucher(VoucherType.PERCENT, EventType.MONTH, 2,
            LocalDateTime.now().plusDays(1));
    }

    public static Restaurant initRestaurant() {
        return Restaurant.createRestaurant("김밥나라", "000-00-00000",
            LocalTime.of(9, 0, 0), LocalTime.of(23, 0, 0),
            false, "00-000-0000",
            "안녕하세요 저희 김밥나라는 정성을 다해 요리합니다.", "서울 특별시 강남구");
    }


    public static List<Cart> initCarts(List<Menu> menuList) {
        Menu firstMenu = menuList.get(0);
        Menu secondMenu = menuList.get(1);
        return List.of(new Cart(firstMenu, 1), new Cart(secondMenu, 2));
    }

    public static List<Cart> initCarts() {
        List<Menu> menuList = initMenus();
        Menu firstMenu = menuList.get(0);
        Menu secondMenu = menuList.get(1);
        return List.of(new Cart(firstMenu, 1), new Cart(secondMenu, 2));
    }

    public static MenuGroup initMenuGroup(Restaurant restaurant) {
        return MenuGroup.createMenuGroup(restaurant, "김밥류", "김밥류입니다.");
    }

    public static MenuGroup initMenuGroup() {
        Restaurant restaurant = initRestaurant();
        return MenuGroup.createMenuGroup(restaurant, "김밥류", "김밥류입니다.");
    }

    public static List<Menu> initMenus(MenuGroup menuGroup) {
        Menu menu1 = Menu.createMenu(menuGroup, "참치 깁밥", 4000, "맛있는 참치 김밥", false, MenuStatus.SALE);
        Menu menu2 = Menu.createMenu(menuGroup, "계란 깁밥", 4500, "맛있는 계란 김밥", false, MenuStatus.SALE);
        Menu menu3 = Menu.createMenu(menuGroup, "치즈 깁밥", 4500, "맛있는 치즈 김밥", false, MenuStatus.SALE);
        return List.of(menu1, menu2, menu3);
    }

    public static List<Menu> initMenus() {
        MenuGroup menuGroup = initMenuGroup();
        Menu menu1 = Menu.createMenu(menuGroup, "참치 깁밥", 4000, "맛있는 참치 김밥", false, MenuStatus.SALE);
        Menu menu2 = Menu.createMenu(menuGroup, "계란 깁밥", 4500, "맛있는 계란 김밥", false, MenuStatus.SALE);
        Menu menu3 = Menu.createMenu(menuGroup, "치즈 깁밥", 4500, "맛있는 치즈 김밥", false, MenuStatus.SALE);
        return List.of(menu1, menu2, menu3);
    }

    public static Order initOrder(Customer customer, Restaurant restaurant, Voucher voucher,
        List<Cart> carts) {
        String deliveryAddress = " ";
        int deliveryFee = 1000;
        return Order.createOrder(customer, restaurant, null, deliveryAddress, 0,
            PaymentType.CREDIT_CARD, carts, deliveryFee);
    }

    public static Order initOrder() {
        Customer customer = initCustomer();
        Restaurant restaurant = initRestaurant();
        Voucher voucher = initVoucher();
        List<Cart> carts = initCarts();
        String deliveryAddress = " ";
        int deliveryFee = 1000;
        return Order.createOrder(customer, restaurant, null, deliveryAddress, 0,
            PaymentType.CREDIT_CARD, carts, deliveryFee);
    }

    public static Delivery initDelivery(Customer customer, CustomerAddress customerAddress,
        Restaurant restaurant, Order order) {
        Delivery delivery = Delivery.createDelivery(order, restaurant.getAddress(),
            customerAddress.getAddress(), 0);
        return delivery;
    }

    public static Delivery initDelivery() {
        Order order = initOrder();
        Restaurant restaurant = initRestaurant();
        Customer customer = initCustomer();
        CustomerAddress customerAddress = customer.getCustomerAddresses().get(0);
        Delivery delivery = Delivery.createDelivery(order, restaurant.getAddress(),
            customerAddress.getAddress(), 0);
        return delivery;
    }

    public static Rider initRider() {
        Rider rider = Rider.createRider("RiderId", "RiderPassword", "rider", "010-1234-5678");
        AreaCode firstAreaCode = new AreaCode("12344", "서울시 관악구 신림동", true);
        AreaCode secondAreaCode = new AreaCode("12345", "서울시 관악구 봉천동", true);
        RiderAreaCode firstRiderAreaCode = new RiderAreaCode(rider, firstAreaCode);
        RiderAreaCode secondRiderAreaCode = new RiderAreaCode(rider, secondAreaCode);

        return rider;
    }
}
