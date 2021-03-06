package com.example.woowa;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.woowa.admin.dto.AdminCreateRequest;
import com.example.woowa.admin.service.AdminService;
import com.example.woowa.customer.customer.dto.CustomerAddressCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerFindResponse;
import com.example.woowa.customer.customer.dto.CustomerGradeCreateRequest;
import com.example.woowa.customer.customer.service.CustomerGradeService;
import com.example.woowa.customer.customer.service.CustomerService;
import com.example.woowa.customer.voucher.dto.VoucherFindResponse;
import com.example.woowa.customer.voucher.service.VoucherService;
import com.example.woowa.delivery.dto.DeliveryResponse;
import com.example.woowa.delivery.dto.RiderCreateRequest;
import com.example.woowa.delivery.entity.AreaCode;
import com.example.woowa.delivery.enums.DeliveryStatus;
import com.example.woowa.delivery.repository.AreaCodeRepository;
import com.example.woowa.delivery.service.DeliveryService;
import com.example.woowa.delivery.service.RiderService;
import com.example.woowa.order.order.dto.cart.CartSaveRequest;
import com.example.woowa.order.order.dto.customer.OrderListCustomerRequest;
import com.example.woowa.order.order.dto.customer.OrderSaveRequest;
import com.example.woowa.order.order.dto.restaurant.OrderAcceptRequest;
import com.example.woowa.order.order.dto.restaurant.OrderListRestaurantRequest;
import com.example.woowa.order.order.dto.statistics.OrderStatisticsRequest;
import com.example.woowa.order.order.enums.PaymentType;
import com.example.woowa.order.order.service.OrderService;
import com.example.woowa.order.review.dto.ReviewCreateRequest;
import com.example.woowa.order.review.dto.ReviewFindResponse;
import com.example.woowa.order.review.service.ReviewService;
import com.example.woowa.restaurant.advertisement.dto.request.AdvertisementCreateRequest;
import com.example.woowa.restaurant.advertisement.enums.RateType;
import com.example.woowa.restaurant.advertisement.enums.UnitType;
import com.example.woowa.restaurant.advertisement.service.AdvertisementService;
import com.example.woowa.restaurant.category.dto.request.CategoryCreateRequest;
import com.example.woowa.restaurant.category.dto.response.CategoryFindResponse;
import com.example.woowa.restaurant.category.service.CategoryService;
import com.example.woowa.restaurant.menu.dto.MenuSaveRequest;
import com.example.woowa.restaurant.menu.service.MenuService;
import com.example.woowa.restaurant.menugroup.dto.MenuGroupSaveRequest;
import com.example.woowa.restaurant.menugroup.service.MenuGroupService;
import com.example.woowa.restaurant.owner.dto.request.OwnerCreateRequest;
import com.example.woowa.restaurant.owner.dto.response.OwnerFindResponse;
import com.example.woowa.restaurant.owner.service.OwnerService;
import com.example.woowa.restaurant.restaurant.dto.request.RestaurantCreateRequest;
import com.example.woowa.restaurant.restaurant.dto.response.RestaurantCreateResponse;
import com.example.woowa.restaurant.restaurant.dto.response.RestaurantFindResponse;
import com.example.woowa.restaurant.restaurant.service.RestaurantService;
import com.example.woowa.security.repository.RoleRepository;
import com.example.woowa.security.user.Role;
import com.example.woowa.security.user.UserRole;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class BDDTest {

    @Autowired
    AdminService adminService;

    @Autowired
    AdvertisementService advertisementService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    OwnerService ownerService;

    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerGradeService customerGradeService;

    @Autowired
    RiderService riderService;

    @Autowired
    VoucherService voucherService;

    @Autowired
    OrderService orderService;

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    DeliveryService deliveryService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AreaCodeRepository areaCodeRepository;

    @Autowired
    MenuService menuService;

    @Autowired
    MenuGroupService menuGroupService;

    @Autowired
    ReviewService reviewService;

    RestaurantCreateResponse restaurant;
    Long menuGroupId;
    Long menuId;
    CustomerFindResponse customer;
    Long orderId;
    Long areaCodeId;
    Long riderId;
    Long deliveryId;

    @Test
    @DisplayName("???????????? ????????????.")
    @Order(0)
    void _0() {
        AdminCreateRequest adminCreateRequest = new AdminCreateRequest("dev12", "Programmers12!");
        adminService.createAdmin(adminCreateRequest);

        roleRepository.save(new Role(UserRole.ROLE_OWNER.toString()));
        roleRepository.save(new Role(UserRole.ROLE_ADMIN.toString()));
        roleRepository.save(new Role(UserRole.ROLE_RIDER.toString()));
        roleRepository.save(new Role(UserRole.ROLE_CUSTOMER.toString()));
    }

    @Test
    @DisplayName("??????????????? ????????????.")
    @Order(1)
    void _1() {
        CategoryCreateRequest korean = new CategoryCreateRequest("??????");
        CategoryCreateRequest chinese = new CategoryCreateRequest("??????");
        categoryService.createCategory(korean);
        categoryService.createCategory(chinese);
    }


    @Test
    @DisplayName("???????????? ??????????????????.")
    @Order(2)
    void _2() {
        OwnerCreateRequest ownerCreateRequest = new OwnerCreateRequest("tTest@12341234",
            "tT@1234567890", "?????????", "010-1234-1234");
        ownerService.createOwner(ownerCreateRequest);
    }

    @Test
    @DisplayName("???????????? ????????? ????????????.")
    @Order(3)
    void _3() {
        List<OwnerFindResponse> owners = ownerService.findOwners();
        List<Long> categoryIds = categoryService.findCategories().stream()
            .map(CategoryFindResponse::getId).collect(Collectors.toList());
        RestaurantCreateRequest restaurantCreateRequest = new RestaurantCreateRequest("??????",
            "760-15-00993", LocalTime.now(), LocalTime.now().plusHours(5), true, "010-1111-1234",
            "???????????? ??????", "??????????????? ????????? ?????????", categoryIds);
        restaurant = restaurantService.createRestaurantByOwnerId(
            owners.get(0).getId(), restaurantCreateRequest);
    }

    @Test
    @DisplayName("???????????? ?????? ????????? ????????????.")
    @Order(4)
    void _4() {
        List<RestaurantFindResponse> restauransNotPermitted = restaurantService.findRestaurantsIsPermittedIsFalse();
        Long newRestaurant = restauransNotPermitted.get(0).getId();
        adminService.permitRestaurant(newRestaurant);
    }

    @Test
    @DisplayName("???????????? ?????? ????????? ????????????")
    @Order(5)
    void _5() {
        AdvertisementCreateRequest ultraCall = new AdvertisementCreateRequest("????????????",
            UnitType.MOTHLY.getType(), RateType.FLAT.getType(), 88000, "???????????? ??????", 10);
        AdvertisementCreateRequest openList = new AdvertisementCreateRequest("???????????????",
            UnitType.PER_ORDER.getType(), RateType.PERCENT.getType(), 10, "??????????????? ??????", 10);
        advertisementService.createAdvertisement(ultraCall);
        advertisementService.createAdvertisement(openList);
    }

    @Test
    @DisplayName("???????????? ????????? ????????? ??? ??????.")
    @Order(6)
    void _6() {
        AreaCode areaCode = areaCodeRepository.save(new AreaCode("1", "??????????????? ????????? ?????????", false));
        areaCodeId = areaCode.getId();
    }

    @Test
    @DisplayName("?????? ????????? ????????????.")
    @Order(7)
    void _7() {
        CustomerGradeCreateRequest customerGradeCreateRequest = new CustomerGradeCreateRequest(5,
            "??????", 3000, 2);
        customerGradeService.createCustomerGrade(
            customerGradeCreateRequest);
    }


    @Test
    @DisplayName("????????? ????????????.")
    @Order(8)
    void _8() {
        CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest(
            "??????????????? ????????? ?????????", "?????? 101???", "???");
        CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest("dev12",
            "Programmers123!", "2000-01-01", customerAddressCreateRequest);
        customer = customerService.createCustomer(
            customerCreateRequest);
    }

    @Test
    @DisplayName("????????? ?????? ????????? ?????? ?????? ????????? ?????? ??? ??????.")
    @Order(9)
    void _9() {
        voucherService.registerMonthlyVoucher("dev12");
    }

    @Test
    @DisplayName("????????? ????????? ??????????????? ????????? ??? ??????.")
    @Order(10)
    void _10() {
        Long restaurantId = restaurant.getId();
        MenuGroupSaveRequest request = new MenuGroupSaveRequest("?????????", "????????? ?????? ?????????");
        menuGroupId = menuGroupService.addMenuGroup(restaurantId, request);
    }

    @Test
    @DisplayName("????????? ????????? ????????? ????????? ??? ??????.")
    @Order(11)
    void _11() {
        MenuSaveRequest request = new MenuSaveRequest(menuGroupId, "?????? ??????", "????????? ?????? ???????????????.",
            4500);
        menuId = menuService.addMenu(request);
    }

    @Test
    @DisplayName("????????? ?????? ????????? ????????? ??? ??????.")
    @Order(12)
    void _12() {
        restaurantService.setDeliveryArea(restaurant.getId(), areaCodeId, 3000);
    }

    @Test
    @DisplayName("??????????????? ?????? ????????? ????????? ????????? ????????????.")
    @Order(13)
    void _13() {
        restaurantService.findRestaurantByAreaCode(areaCodeId);
    }

    @Test
    @DisplayName("????????? ????????????.")
    @Order(14)
    void _14() {
        List<VoucherFindResponse> vouchers = voucherService.findUserVoucher(
            customer.getLoginId());
        CartSaveRequest cartSaveRequest = new CartSaveRequest(menuId, 3);
        OrderSaveRequest orderSaveRequest = new OrderSaveRequest(customer.getLoginId(),
            restaurant.getId(), vouchers.get(0).getId(), 0,
            PaymentType.CREDIT_CARD, "??????????????? ????????? ?????????", Collections.singletonList(cartSaveRequest)
        );

        orderId = orderService.addOrder(orderSaveRequest);
    }

    @Test
    @DisplayName("????????? ????????????.")
    @Order(15)
    void _15() {
        orderService.acceptOrder(orderId, new OrderAcceptRequest(30));
    }


    @Test
    @DisplayName("???????????? ????????? ??? ??????.")
    @Order(16)
    void _16() {

        RiderCreateRequest riderCreateRequest = new RiderCreateRequest("testId1234", "passwordA1!",
            "name",
            "010-1234-5678");
        riderId = riderService.save(riderCreateRequest);
    }

    @Test
    @DisplayName("???????????? ????????? ?????? ??? ?????? ????????? ????????? ??? ??????.")
    @Order(17)
    void _17() {
        riderService.changeIsDelivery(riderId, true);
    }

    @Test
    @DisplayName("???????????? ?????????????????? ??????????????? ????????? ??? ??????.")
    @Order(18)
    void _18() {
        PageRequest pageRequest = PageRequest.of(0, 20);
        Page<DeliveryResponse> deliveryResponsePage = deliveryService.findWaitingDelivery(
            pageRequest);
        deliveryId = deliveryResponsePage.stream().findFirst().get().getId();
    }

    @Test
    @DisplayName("???????????? ?????? ????????? ????????? ??? ??????.")
    @Order(19)
    void _19() {
        riderService.addRiderAreaCode(riderId, areaCodeId);
    }

    @Test
    @DisplayName("???????????? ????????? ????????? ??? ??????.")
    @Order(20)
    void _20() {
        deliveryService.acceptDelivery(deliveryId, riderId, 10, 10);
        DeliveryResponse deliveryResponse = deliveryService.findResponseById(deliveryId);

        assertThat(deliveryResponse.getDeliveryStatus()).isEqualTo(
            DeliveryStatus.DELIVERY_REGISTRATION);
    }

    @Test
    @DisplayName("???????????? ??????????????? ????????? ??? ??????.")
    @Order(21)
    void _21() {
        deliveryService.delay(riderId, 10);
    }

    @Test
    @DisplayName("???????????? ????????? ????????? ?????? ????????? ????????? ??? ??????.")
    @Order(22)
    void _22() {
        deliveryService.pickUp(riderId);
        DeliveryResponse deliveryResponse = deliveryService.findResponseById(deliveryId);

        assertThat(deliveryResponse.getDeliveryStatus()).isEqualTo(
            DeliveryStatus.DELIVERY_PICKUP);
    }

    @Test
    @DisplayName("???????????? ?????? ????????? ?????? ????????? ????????? ??? ??????.")
    @Order(23)
    void _23() {
        deliveryService.finish(riderId);
        DeliveryResponse deliveryResponse = deliveryService.findResponseById(deliveryId);

        assertThat(deliveryResponse.getDeliveryStatus()).isEqualTo(DeliveryStatus.DELIVERY_FINISH);
    }

    @Test
    @DisplayName("????????? ?????? ????????? ????????????.")
    @Order(24)
    void _24() {
        OrderListCustomerRequest request = new OrderListCustomerRequest(customer.getLoginId(), 0, 1,
            LocalDate.now(), LocalDate.now().plusMonths(1));
        orderService.findOrderByCustomer(request);
    }

    @Test
    @DisplayName("?????? ???????????? ?????? ?????? ?????????  ?????? ????????????.")
    @Order(25)
    void _25() {
        orderService.findDetailOrderForCustomer(
            orderId);
    }

    @Test
    @DisplayName("????????? ?????? ????????? ????????????.")
    @Order(26)
    void _26() {
        OrderListRestaurantRequest request = new OrderListRestaurantRequest(restaurant.getId(), 0,
            1,
            LocalDate.now(), LocalDate.now().plusMonths(1));
        orderService.findOrderByRestaurant(request);
    }

    @Test
    @DisplayName("????????? ?????? ????????? ?????? ????????????.")
    @Order(27)
    void _27() {
        orderService.findDetailOrderByIdForRestaurant(orderId);
    }

    @Test
    @DisplayName("????????? ?????? ????????? ????????????.")
    @Order(28)
    void _28() {
        OrderStatisticsRequest request = new OrderStatisticsRequest(restaurant.getId(),
            LocalDate.now(), LocalDate.now().plusMonths(1));
        orderService.findOrderStatistics(request);
    }

    @Test
    @DisplayName("????????? ????????? ??????????????? ?????? ????????? ????????? ?????? ?????? ????????? ????????? ??? ??????.")
    @Order(29)
    void _29() {
        ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest("???????????? ???????????????.", 5);
        reviewService.createReview("dev12", orderId,
            reviewCreateRequest);
    }

    @Test
    @DisplayName("????????? ?????? ????????? ????????? ??? ??????.")
    @Order(30)
    void _30() {
        List<ReviewFindResponse> result = reviewService.findUserReview("dev12");
    }
}
