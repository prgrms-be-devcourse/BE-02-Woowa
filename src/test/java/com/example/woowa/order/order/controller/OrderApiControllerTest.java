package com.example.woowa.order.order.controller;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.woowa.RestDocsConfiguration;
import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.entity.CustomerGrade;
import com.example.woowa.customer.customer.repository.CustomerGradeRepository;
import com.example.woowa.customer.customer.repository.CustomerRepository;
import com.example.woowa.customer.voucher.repository.VoucherRepository;
import com.example.woowa.delivery.entity.AreaCode;
import com.example.woowa.delivery.entity.DeliveryArea;
import com.example.woowa.delivery.repository.AreaCodeRepository;
import com.example.woowa.delivery.repository.DeliveryAreaRepository;
import com.example.woowa.order.order.dto.cart.CartSaveRequest;
import com.example.woowa.order.order.dto.customer.OrderListCustomerRequest;
import com.example.woowa.order.order.dto.customer.OrderSaveRequest;
import com.example.woowa.order.order.dto.restaurant.OrderAcceptRequest;
import com.example.woowa.order.order.dto.restaurant.OrderListRestaurantRequest;
import com.example.woowa.order.order.dto.statistics.OrderStatisticsRequest;
import com.example.woowa.order.order.entity.Cart;
import com.example.woowa.order.order.entity.Order;
import com.example.woowa.order.order.enums.PaymentType;
import com.example.woowa.order.order.repository.OrderRepository;
import com.example.woowa.restaurant.menu.entity.Menu;
import com.example.woowa.restaurant.menu.enums.MenuStatus;
import com.example.woowa.restaurant.menu.repository.MenuRepository;
import com.example.woowa.restaurant.menugroup.entity.MenuGroup;
import com.example.woowa.restaurant.menugroup.repository.MenuGroupRepository;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import com.example.woowa.restaurant.restaurant.repository.RestaurantRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@Transactional
class OrderApiControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    MenuGroupRepository menuGroupRepository;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    VoucherRepository voucherRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CustomerGradeRepository customerGradeRepository;
    @Autowired
    AreaCodeRepository areaCodeRepository;
    @Autowired
    DeliveryAreaRepository deliveryAreaRepository;
    @Autowired
    OrderRepository orderRepository;

    Restaurant restaurant;
    MenuGroup menuGroup;
    Menu menu;
    Customer customer;
    Order order;

    @BeforeEach
    void init() {
        AreaCode areaCode = areaCodeRepository.save(
                new AreaCode("1111000000", "서울특별시 종로구", false));

        CustomerGrade customerGrade = customerGradeRepository.save(
                new CustomerGrade(10, "VIP", 1000, 3));
        customer = customerRepository.save(
                new Customer("dev12", "Programmers123", LocalDate.of(2000, 1, 1), customerGrade));

        restaurant = restaurantRepository.save(
                Restaurant.createRestaurant("김밥나라", "000-00-00000",
                        LocalTime.of(9, 0, 0), LocalTime.of(23, 0, 0),
                        false, "00-000-0000",
                        "안녕하세요 저희 김밥나라는 정성을 다해 요리합니다.", "서울특별시 종로구"));

        DeliveryArea deliveryArea = deliveryAreaRepository.save(
                new DeliveryArea(areaCode, restaurant, 3000));

        menuGroup = menuGroupRepository.save(
                MenuGroup.createMenuGroup(restaurant, "김밥류", "맛잇는 김밥"));

        menu = menuRepository.save(
                Menu.createMenu(menuGroup, "참치 김밥", 4500, "맛있는 참치 김밥", false,
                        MenuStatus.SALE));

        order = orderRepository.save(Order.createOrder(customer, restaurant, null, "서울특별시 종로구", 0,
                PaymentType.CREDIT_CARD,
                Collections.singletonList(new Cart(menu, 2)), deliveryArea.getDeliveryFee()));

    }

    @Test
    @DisplayName("주문을 생성한다.")
    void addOrderTest() throws Exception {
        OrderSaveRequest orderSaveRequest = new OrderSaveRequest(customer.getLoginId(),
                restaurant.getId(),
                null, 0,
                PaymentType.CREDIT_CARD, "서울특별시 종로구",
                Collections.singletonList(new CartSaveRequest(menu.getId(), 2)));

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderSaveRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andDo(document("create-order",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(
                                        MediaType.APPLICATION_JSON_VALUE)
                        ),
                        requestFields(
                                fieldWithPath("loginId").type(JsonFieldType.STRING)
                                        .description("주문한 회원의 아이디"),
                                fieldWithPath("restaurantId").type(JsonFieldType.NUMBER)
                                        .description("주문한 레스토랑의 ID"),
                                fieldWithPath("voucherId").description(
                                        "사용한 쿠폰의 ID(쿠폰 미사용 주문일 경우 해당 필드는 null로 전달)"),
                                fieldWithPath("usePoint").description("주문에 사용한 회원의 포인트"),
                                fieldWithPath("paymentType").description("결제 수단"),
                                fieldWithPath("deliveryAddress").description("배달 주소"),
                                fieldWithPath("carts[]").type(JsonFieldType.ARRAY)
                                        .description("장바구니 메뉴 정보"),
                                fieldWithPath("carts[].menuId").type(JsonFieldType.NUMBER)
                                        .description("메뉴의 ID"),
                                fieldWithPath("carts[].quantity").type(JsonFieldType.NUMBER)
                                        .description("메뉴 수량")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("생성된 주문의 경로")
                        )
                ));
    }

    @Test
    @DisplayName("가게에서 주문을 조회한다.")
    void findOrderByRestaurantTest() throws Exception {
        OrderListRestaurantRequest request = new OrderListRestaurantRequest(
                restaurant.getId(), 0, 3, LocalDate.now().minusMonths(1),
                LocalDate.now());

        mockMvc.perform(get("/api/v1/restaurants/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("find-order-by-restaurant",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(
                                        MediaType.APPLICATION_JSON_VALUE),
                                headerWithName(HttpHeaders.ACCEPT).description(
                                        MediaType.APPLICATION_JSON_VALUE)
                        ),
                        requestFields(
                                fieldWithPath("restaurantId").type(JsonFieldType.NUMBER)
                                        .description("가게 ID"),
                                fieldWithPath("pageNum").type(JsonFieldType.NUMBER)
                                        .description("페이지 번호(0부터 시작)"),
                                fieldWithPath("size").type(JsonFieldType.NUMBER)
                                        .description("1페이지당 목록 개수"),
                                fieldWithPath("from").type(JsonFieldType.STRING)
                                        .description("조회 시작일(형식: yyyy-MM-dd)"),
                                fieldWithPath("end").type(JsonFieldType.STRING)
                                        .description("조회 종료일(형식: yyyy-MM-dd)")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(
                                        MediaType.APPLICATION_JSON_VALUE)
                        ),
                        responseFields(
                                fieldWithPath("hasNextPage").type(JsonFieldType.BOOLEAN)
                                        .description("다음 페이지가 있으면 TRUE"),
                                fieldWithPath("size").type(JsonFieldType.NUMBER)
                                        .description("주문 목록 개수"),
                                fieldWithPath("orders[]").type(JsonFieldType.ARRAY)
                                        .description("주묵 목록"),
                                fieldWithPath("orders[].createdAt").type(JsonFieldType.STRING)
                                        .description("주문 생성 시간"),
                                fieldWithPath("orders[].orderPrice").type(JsonFieldType.NUMBER)
                                        .description("할인전 주문 가격(배달비 제외)"),
                                fieldWithPath("orders[].deliveryFee").type(JsonFieldType.NUMBER)
                                        .description("배달비"),
                                fieldWithPath("orders[].afterDiscountTotalPrice").type(
                                        JsonFieldType.NUMBER).description("할인후 가격(배달비 포함"),
                                fieldWithPath("orders[].totalDiscountPrice").type(
                                        JsonFieldType.NUMBER).description("할인액(쿠폰 + 포인트 사용)"),
                                fieldWithPath("orders[].orderStatus").type(JsonFieldType.STRING)
                                        .description("주문 상태(PAYMENT_COMPLETED, ACCEPTED, CANCEL)"),
                                fieldWithPath("orders[].menus[]").type(JsonFieldType.ARRAY)
                                        .description("주문한 메뉴 정보"),
                                fieldWithPath("orders[].menus[].menuName").type(
                                                JsonFieldType.STRING)
                                        .description("메뉴명"),
                                fieldWithPath("orders[].menus[].quantity").type(
                                                JsonFieldType.NUMBER)
                                        .description("수량"),
                                fieldWithPath("orders[].menus[].totalPrice").type(
                                                JsonFieldType.NUMBER)
                                        .description("가격")
                        )
                ));
    }

    @Test
    @DisplayName("존재하지 않는 가게의 주문을 조회하려 하면 404 NotFound 를 응답한다.")
    void findOrderByRestaurantNotFoundTest() throws Exception {
        Long wrongRestaurantId = -1L;
        OrderListRestaurantRequest request = new OrderListRestaurantRequest(
                wrongRestaurantId, 0, 3, LocalDate.now().minusMonths(1),
                LocalDate.now());

        mockMvc.perform(get("/api/v1/restaurants/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("회원의 주문을 조회한다.")
    void findOrderByCustomerTest() throws Exception {

        OrderListCustomerRequest request = new OrderListCustomerRequest(
                customer.getLoginId(), 0, 1, LocalDate.now().minusMonths(1),
                LocalDate.now());

        mockMvc.perform(get("/api/v1/customers/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("find-order-by-customer",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(
                                        MediaType.APPLICATION_JSON_VALUE)
                        ),
                        requestFields(
                                fieldWithPath("loginId").type(JsonFieldType.STRING)
                                        .description("회원 로그인 ID"),
                                fieldWithPath("pageNum").type(JsonFieldType.NUMBER)
                                        .description("페이지 번호"),
                                fieldWithPath("size").type(JsonFieldType.NUMBER)
                                        .description("페이지당 목록 개수"),
                                fieldWithPath("from").type(JsonFieldType.STRING)
                                        .description("조회 시작일(형식: yyyy-MM-dd)"),
                                fieldWithPath("end").type(JsonFieldType.STRING)
                                        .description("조회 종료일(형식: yyyy-MM-dd)")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(
                                        MediaType.APPLICATION_JSON_VALUE)
                        ),
                        responseFields(
                                fieldWithPath("hasNextPage").type(JsonFieldType.BOOLEAN)
                                        .description("다음 페이지가 있으면 TRUE"),
                                fieldWithPath("size").type(JsonFieldType.NUMBER)
                                        .description("주문 목록 개수"),
                                fieldWithPath("orders[]").type(JsonFieldType.ARRAY)
                                        .description("주문 목록"),
                                fieldWithPath("orders[].id").type(JsonFieldType.NUMBER)
                                        .description("주문 ID"),
                                fieldWithPath("orders[].createdAt").type(JsonFieldType.STRING)
                                        .description("주문 시간(형식: yyyy-MM-dd HH:mm:ss)"),
                                fieldWithPath("orders[].orderStatus").type(JsonFieldType.STRING)
                                        .description("주문 상태"),
                                fieldWithPath("orders[].restaurantName").type(JsonFieldType.STRING)
                                        .description("가게명"),
                                fieldWithPath("orders[].afterDiscountTotalPrice").type(
                                                JsonFieldType.NUMBER)
                                        .description("최종 결제 가격(할인 적용, 배달비 포함)"),
                                fieldWithPath("orders[].carts[]").type(JsonFieldType.ARRAY)
                                        .description("주문한 메뉴 정보"),
                                fieldWithPath("orders[].carts[].menuTitle").type(
                                        JsonFieldType.STRING).description("메뉴명"),
                                fieldWithPath("orders[].carts[].quantity").type(
                                                JsonFieldType.NUMBER)
                                        .description("수량")
                        )
                ));
    }

    @Test
    @DisplayName("존재하지 않는 회원의 주문을 조회하려하면 404 NotFound 를 응답한다.")
    void findOrderForCustomerTest() throws Exception {
        String wrongCustomerId = "";
        OrderListCustomerRequest request = new OrderListCustomerRequest(
                wrongCustomerId, 0, 1, LocalDate.now().minusMonths(1),
                LocalDate.now());

        mockMvc.perform(get("/api/v1/customers/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("회원 주문을 단건 조회한다.")
    void findDetailOrderForCustomerTest() throws Exception {

        mockMvc.perform(get("/api/v1/customers/orders/{orderId}", order.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("find-detail-order-by-customer",
                        pathParameters(
                                parameterWithName("orderId").description("조회할 주문 ID")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description(
                                        MediaType.APPLICATION_JSON_VALUE)
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(
                                        MediaType.APPLICATION_JSON_VALUE)
                        ),
                        responseFields(
                                fieldWithPath("createdAt").type(JsonFieldType.STRING)
                                        .description("주문 생성 시간(형식: yyyy-MM-dd HH:mm:ss)"),
                                fieldWithPath("orderPrice").type(JsonFieldType.NUMBER)
                                        .description("할인전 주문 금액(배달비 제외)"),
                                fieldWithPath("deliveryFee").type(JsonFieldType.NUMBER)
                                        .description("배달비"),
                                fieldWithPath("voucherDiscountPrice").type(JsonFieldType.NUMBER)
                                        .description("쿠폰 할인 금액"),
                                fieldWithPath("usedPoint").type(JsonFieldType.NUMBER)
                                        .description("주문에 사용한 포인트"),
                                fieldWithPath("orderStatus").type(JsonFieldType.STRING)
                                        .description("주문 상태(PAYMENT_COMPLETED, ACCEPTED, CANCEL)"),
                                fieldWithPath("deliveryAddress").type(JsonFieldType.STRING)
                                        .description("배달 주소"),
                                fieldWithPath("menus[]").type(JsonFieldType.ARRAY)
                                        .description("주문한 메뉴 정보"),
                                fieldWithPath("menus[].menuName").type(JsonFieldType.STRING)
                                        .description("메뉴명"),
                                fieldWithPath("menus[].quantity").type(JsonFieldType.NUMBER)
                                        .description("수량"),
                                fieldWithPath("menus[].totalPrice").type(JsonFieldType.NUMBER)
                                        .description("가격")
                        )
                ));
    }

    @Test
    @DisplayName("존재하지 않는 주문을 회원이 단건 조회하려하면 404 NotFound 를 응답한다.")
    void findDetailOrderForCustomerNotFoundTest() throws Exception {
        Long wrongOrderId = -1L;
        mockMvc.perform(get("/api/v1/customers/orders/{orderId}", wrongOrderId)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("가게의 주문을 단건 조회한다.")
    void findDetailOrderForRestaurantTest() throws Exception {
        mockMvc.perform(get("/api/v1/restaurants/orders/{orderId}", order.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("find-detail-order-for-restaurant",
                        pathParameters(
                                parameterWithName("orderId").description("조회할 주문 ID")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description(
                                        MediaType.APPLICATION_JSON_VALUE)
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(
                                        MediaType.APPLICATION_JSON_VALUE)
                        ),
                        responseFields(
                                fieldWithPath("createdAt").type(JsonFieldType.STRING)
                                        .description("주문 생성 시간(형식: yyyy-MM-dd HH:mm:ss)"),
                                fieldWithPath("orderPrice").type(JsonFieldType.NUMBER)
                                        .description("할인전 주문 금액(배달비 제외)"),
                                fieldWithPath("deliveryFee").type(JsonFieldType.NUMBER)
                                        .description("배달비"),
                                fieldWithPath("afterDiscountTotalPrice").type(JsonFieldType.NUMBER)
                                        .description("할인후 최종 결제 가격(배달비 포함)"),
                                fieldWithPath("totalDiscountPrice").type(JsonFieldType.NUMBER)
                                        .description("총 할인 금액(쿠폰 + 포인트)"),
                                fieldWithPath("orderStatus").type(JsonFieldType.STRING)
                                        .description("주문 상태(PAYMENT_COMPLETED, ACCEPTED, CANCEL)"),
                                fieldWithPath("menus[]").type(JsonFieldType.ARRAY)
                                        .description("주문한 메뉴 정보"),
                                fieldWithPath("menus[].menuName").type(JsonFieldType.STRING)
                                        .description("메뉴명"),
                                fieldWithPath("menus[].quantity").type(JsonFieldType.NUMBER)
                                        .description("수량"),
                                fieldWithPath("menus[].totalPrice").type(JsonFieldType.NUMBER)
                                        .description("가격")
                        )));
    }

    @Test
    @DisplayName("존재하지 않는 주문을 가게에서 단건 조회하려하면 404 NotFound 를 응답한다.")
    void findDetailOrderForRestaurantNotFoundTest() throws Exception {
        Long wrongOrderId = -1L;
        mockMvc.perform(get("/api/v1/restaurants/orders/{orderId}", wrongOrderId)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("기간별 가게 주문 횟수와 매출 정보를 조회한다.")
    void findOrderStatisticsTest() throws Exception {
        OrderStatisticsRequest request = new OrderStatisticsRequest(
                restaurant.getId(), LocalDate.now().minusMonths(1),
                LocalDate.now());

        mockMvc.perform(get("/api/v1/restaurants/orders/statistics")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("find-order-statistics",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(
                                        MediaType.APPLICATION_JSON_VALUE),
                                headerWithName(HttpHeaders.ACCEPT).description(
                                        MediaType.APPLICATION_JSON_VALUE)
                        ),
                        requestFields(
                                fieldWithPath("restaurantId").type(JsonFieldType.NUMBER)
                                        .description("조회할 가게 ID"),
                                fieldWithPath("from").type(JsonFieldType.STRING)
                                        .description("조회 시작일(형식: yyyy-MM-dd)"),
                                fieldWithPath("end").type(JsonFieldType.STRING)
                                        .description("조회 마감일(형식: yyyy-MM-dd)")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(
                                        MediaType.APPLICATION_JSON_VALUE)
                        ),
                        responseFields(
                                fieldWithPath("orderCount").type(JsonFieldType.NUMBER)
                                        .description("주문 횟수"),
                                fieldWithPath("orderPrice").type(JsonFieldType.NUMBER)
                                        .description("주문 가격(할인 금액 제외)"),
                                fieldWithPath("discountPrice").type(JsonFieldType.NUMBER)
                                        .description("할인 금액")
                        )
                ));
    }

    @Test
    @DisplayName("존재하지 않는 가게의 매출 정보를 조회하려 하면 404 NotFound 를 응답한다.")
    void findOrderStatisticsNotFoundRestaurantTest() throws Exception {
        Long wrongRestaurantId = -1L;
        OrderStatisticsRequest request = new OrderStatisticsRequest(
                wrongRestaurantId, LocalDate.now().minusMonths(1),
                LocalDate.now());

        mockMvc.perform(get("/api/v1/restaurants/orders/statistics")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("주문을 취소한다.")
    void cancelOrderTest() throws Exception {
        mockMvc.perform(patch("/api/v1/orders/{orderId}/cancel", order.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("cancel-order",
                        pathParameters(
                                parameterWithName("orderId").description("취소할 주문 ID")
                        )
                ));
    }

    @Test
    @DisplayName("존재하지 않는 주문을 취소하려하면 404 NotFound 를 응답한다.")
    void cancelOrderNotFoundTest() throws Exception {
        long wrongOrderId = -1L;

        mockMvc.perform(patch("/api/v1/orders/{orderId}/cancel", wrongOrderId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("주문을 수락한다.")
    void acceptOrderTest() throws Exception {
        OrderAcceptRequest request = new OrderAcceptRequest(30);

        mockMvc.perform(patch("/api/v1/orders/{orderId}/accept", order.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("accept-order",
                        pathParameters(
                                parameterWithName("orderId").description("수락할 주문 ID")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(
                                        MediaType.APPLICATION_JSON_VALUE)
                        ),
                        requestFields(
                                fieldWithPath("cookingTime").type(JsonFieldType.NUMBER)
                                        .description("조리 예상 시간(분)")
                        )
                ));
    }

    @Test
    @DisplayName("존재하지 않는 주문을 수락하려하면 404 NotFound 를 응답한다.")
    void acceptOrderNotFoundTest() throws Exception {
        OrderAcceptRequest request = new OrderAcceptRequest(30);
        long wrongOrderId = -1L;

        mockMvc.perform(patch("/api/v1/orders/{orderId}/accept", wrongOrderId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}