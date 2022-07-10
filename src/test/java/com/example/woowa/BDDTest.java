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
    @DisplayName("관리자를 생성한다.")
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
    @DisplayName("카테고리를 생성한다.")
    @Order(1)
    void _1() {
        CategoryCreateRequest korean = new CategoryCreateRequest("한식");
        CategoryCreateRequest chinese = new CategoryCreateRequest("중식");
        categoryService.createCategory(korean);
        categoryService.createCategory(chinese);
    }


    @Test
    @DisplayName("사장님이 회원가입한다.")
    @Order(2)
    void _2() {
        OwnerCreateRequest ownerCreateRequest = new OwnerCreateRequest("tTest@12341234",
            "tT@1234567890", "사장님", "010-1234-1234");
        ownerService.createOwner(ownerCreateRequest);
    }

    @Test
    @DisplayName("사장님이 가게를 생성한다.")
    @Order(3)
    void _3() {
        List<OwnerFindResponse> owners = ownerService.findOwners();
        List<Long> categoryIds = categoryService.findCategories().stream()
            .map(CategoryFindResponse::getId).collect(Collectors.toList());
        RestaurantCreateRequest restaurantCreateRequest = new RestaurantCreateRequest("한식",
            "760-15-00993", LocalTime.now(), LocalTime.now().plusHours(5), true, "010-1111-1234",
            "테스트용 가게", "서울특별시 동작구 상도동", categoryIds);
        restaurant = restaurantService.createRestaurantByOwnerId(
            owners.get(0).getId(), restaurantCreateRequest);
    }

    @Test
    @DisplayName("어드민이 가게 등록을 승인한다.")
    @Order(4)
    void _4() {
        List<RestaurantFindResponse> restauransNotPermitted = restaurantService.findRestaurantsIsPermittedIsFalse();
        Long newRestaurant = restauransNotPermitted.get(0).getId();
        adminService.permitRestaurant(newRestaurant);
    }

    @Test
    @DisplayName("어드민이 광고 상품을 등록한다")
    @Order(5)
    void _5() {
        AdvertisementCreateRequest ultraCall = new AdvertisementCreateRequest("울트라콜",
            UnitType.MOTHLY.getType(), RateType.FLAT.getType(), 88000, "울트라콜 광고", 10);
        AdvertisementCreateRequest openList = new AdvertisementCreateRequest("오픈리스트",
            UnitType.PER_ORDER.getType(), RateType.PERCENT.getType(), 10, "오픈리스트 광고", 10);
        advertisementService.createAdvertisement(ultraCall);
        advertisementService.createAdvertisement(openList);
    }

    @Test
    @DisplayName("관리자는 지역을 등록할 수 있다.")
    @Order(6)
    void _6() {
        AreaCode areaCode = areaCodeRepository.save(new AreaCode("1", "서울특별시 동작구 상도동", false));
        areaCodeId = areaCode.getId();
    }

    @Test
    @DisplayName("고객 등급을 등록한다.")
    @Order(7)
    void _7() {
        CustomerGradeCreateRequest customerGradeCreateRequest = new CustomerGradeCreateRequest(5,
            "일반", 3000, 2);
        customerGradeService.createCustomerGrade(
            customerGradeCreateRequest);
    }


    @Test
    @DisplayName("고객을 등록한다.")
    @Order(8)
    void _8() {
        CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest(
            "서울특별시 동작구 상도동", "빌라 101호", "집");
        CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest("dev12",
            "Programmers123!", "2000-01-01", customerAddressCreateRequest);
        customer = customerService.createCustomer(
            customerCreateRequest);
    }

    @Test
    @DisplayName("고객은 고객 등급에 따라 정기 쿠폰을 얻을 수 있다.")
    @Order(9)
    void _9() {
        voucherService.registerMonthlyVoucher("dev12");
    }

    @Test
    @DisplayName("사장은 가게에 메뉴그룹를 추가할 수 있다.")
    @Order(10)
    void _10() {
        Long restaurantId = restaurant.getId();
        MenuGroupSaveRequest request = new MenuGroupSaveRequest("김밥류", "맛있는 김밥 메뉴들");
        menuGroupId = menuGroupService.addMenuGroup(restaurantId, request);
    }

    @Test
    @DisplayName("사장은 가게에 메뉴를 추가할 수 있다.")
    @Order(11)
    void _11() {
        MenuSaveRequest request = new MenuSaveRequest(menuGroupId, "참치 김밥", "맛있는 참치 김밥입니다.",
            4500);
        menuId = menuService.addMenu(request);
    }

    @Test
    @DisplayName("사장은 배달 지역을 추가할 수 있다.")
    @Order(12)
    void _12() {
        restaurantService.setDeliveryArea(restaurant.getId(), areaCodeId, 3000);
    }

    @Test
    @DisplayName("주소지에서 배달 가능한 가게의 목록을 조회한다.")
    @Order(13)
    void _13() {
        restaurantService.findRestaurantByAreaCode(areaCodeId);
    }

    @Test
    @DisplayName("주문을 생성한다.")
    @Order(14)
    void _14() {
        List<VoucherFindResponse> vouchers = voucherService.findUserVoucher(
            customer.getLoginId());
        CartSaveRequest cartSaveRequest = new CartSaveRequest(menuId, 3);
        OrderSaveRequest orderSaveRequest = new OrderSaveRequest(customer.getLoginId(),
            restaurant.getId(), vouchers.get(0).getId(), 0,
            PaymentType.CREDIT_CARD, "서울특별시 동작구 상도동", Collections.singletonList(cartSaveRequest)
        );

        orderId = orderService.addOrder(orderSaveRequest);
    }

    @Test
    @DisplayName("주문을 수락한다.")
    @Order(15)
    void _15() {
        orderService.acceptOrder(orderId, new OrderAcceptRequest(30));
    }


    @Test
    @DisplayName("라이더를 등록할 수 있다.")
    @Order(16)
    void _16() {

        RiderCreateRequest riderCreateRequest = new RiderCreateRequest("testId1234", "passwordA1!",
            "name",
            "010-1234-5678");
        riderId = riderService.save(riderCreateRequest);
    }

    @Test
    @DisplayName("라이더는 주문을 받을 수 있는 상태로 변경할 수 있다.")
    @Order(17)
    void _17() {
        riderService.changeIsDelivery(riderId, true);
    }

    @Test
    @DisplayName("라이더는 배달대기중인 주문목록을 조회할 수 있다.")
    @Order(18)
    void _18() {
        PageRequest pageRequest = PageRequest.of(0, 20);
        Page<DeliveryResponse> deliveryResponsePage = deliveryService.findWaitingDelivery(
            pageRequest);
        deliveryId = deliveryResponsePage.stream().findFirst().get().getId();
    }

    @Test
    @DisplayName("라이더는 배달 지역을 추가할 수 있다.")
    @Order(19)
    void _19() {
        riderService.addRiderAreaCode(riderId, areaCodeId);
    }

    @Test
    @DisplayName("라이더는 배달을 수락할 수 있다.")
    @Order(20)
    void _20() {
        deliveryService.acceptDelivery(deliveryId, riderId, 10, 10);
        DeliveryResponse deliveryResponse = deliveryService.findResponseById(deliveryId);

        assertThat(deliveryResponse.getDeliveryStatus()).isEqualTo(
            DeliveryStatus.DELIVERY_REGISTRATION);
    }

    @Test
    @DisplayName("라이더는 배달시간을 지연할 수 있다.")
    @Order(21)
    void _21() {
        deliveryService.delay(riderId, 10);
    }

    @Test
    @DisplayName("라이더는 배달을 픽업시 배달 상태를 변경할 수 있다.")
    @Order(22)
    void _22() {
        deliveryService.pickUp(riderId);
        DeliveryResponse deliveryResponse = deliveryService.findResponseById(deliveryId);

        assertThat(deliveryResponse.getDeliveryStatus()).isEqualTo(
            DeliveryStatus.DELIVERY_PICKUP);
    }

    @Test
    @DisplayName("라이더는 배달 완료시 배달 상태를 변경할 수 있다.")
    @Order(23)
    void _23() {
        deliveryService.finish(riderId);
        DeliveryResponse deliveryResponse = deliveryService.findResponseById(deliveryId);

        assertThat(deliveryResponse.getDeliveryStatus()).isEqualTo(DeliveryStatus.DELIVERY_FINISH);
    }

    @Test
    @DisplayName("고객의 주문 목록을 조회한다.")
    @Order(24)
    void _24() {
        OrderListCustomerRequest request = new OrderListCustomerRequest(customer.getLoginId(), 0, 1,
            LocalDate.now(), LocalDate.now().plusMonths(1));
        orderService.findOrderByCustomer(request);
    }

    @Test
    @DisplayName("고객 입장에서 주문 상세 내역을  단건 조회한다.")
    @Order(25)
    void _25() {
        orderService.findDetailOrderForCustomer(
            orderId);
    }

    @Test
    @DisplayName("가게의 주문 목록을 조회한다.")
    @Order(26)
    void _26() {
        OrderListRestaurantRequest request = new OrderListRestaurantRequest(restaurant.getId(), 0,
            1,
            LocalDate.now(), LocalDate.now().plusMonths(1));
        orderService.findOrderByRestaurant(request);
    }

    @Test
    @DisplayName("가게의 상세 내역을 단건 조회한다.")
    @Order(27)
    void _27() {
        orderService.findDetailOrderByIdForRestaurant(orderId);
    }

    @Test
    @DisplayName("가게의 주문 통계를 조회한다.")
    @Order(28)
    void _28() {
        OrderStatisticsRequest request = new OrderStatisticsRequest(restaurant.getId(),
            LocalDate.now(), LocalDate.now().plusMonths(1));
        orderService.findOrderStatistics(request);
    }

    @Test
    @DisplayName("고객은 주문한 배달완료된 주문 완료된 음식에 대한 가게 리뷰를 작성할 수 있다.")
    @Order(29)
    void _29() {
        ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest("정말정말 맛있습니다.", 5);
        reviewService.createReview("dev12", orderId,
            reviewCreateRequest);
    }

    @Test
    @DisplayName("고객은 가게 리뷰를 조회할 수 있다.")
    @Order(30)
    void _30() {
        List<ReviewFindResponse> result = reviewService.findUserReview("dev12");
    }
}
