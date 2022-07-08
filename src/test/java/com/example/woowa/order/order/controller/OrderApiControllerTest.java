package com.example.woowa.order.order.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.woowa.RestDocsConfiguration;
import com.example.woowa.common.exception.NotFoundException;
import com.example.woowa.order.order.dto.cart.CartResponse;
import com.example.woowa.order.order.dto.cart.CartSaveRequest;
import com.example.woowa.order.order.dto.cart.CartSummeryResponse;
import com.example.woowa.order.order.dto.customer.OrderCustomerResponse;
import com.example.woowa.order.order.dto.customer.OrderListCustomerRequest;
import com.example.woowa.order.order.dto.customer.OrderListCustomerResponse;
import com.example.woowa.order.order.dto.customer.OrderSaveRequest;
import com.example.woowa.order.order.dto.customer.OrderSummeryResponse;
import com.example.woowa.order.order.dto.restaurant.OrderAcceptRequest;
import com.example.woowa.order.order.dto.restaurant.OrderListRestaurantRequest;
import com.example.woowa.order.order.dto.restaurant.OrderListRestaurantResponse;
import com.example.woowa.order.order.dto.restaurant.OrderRestaurantResponse;
import com.example.woowa.order.order.dto.statistics.OrderStatisticsRequest;
import com.example.woowa.order.order.dto.statistics.OrderStatisticsResponse;
import com.example.woowa.order.order.enums.PaymentType;
import com.example.woowa.order.order.service.OrderService;
import com.example.woowa.security.configuration.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs
@WebMvcTest(value = OrderApiController.class, excludeFilters = {
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = SecurityConfig.class
        )
})
@Import(RestDocsConfiguration.class)
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser
class OrderApiControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    OrderService orderService;

    @Test
    @DisplayName("주문을 생성한다.")
    void addOrderTest() throws Exception {
        long savedOrderId = 1L;
        OrderSaveRequest request = new OrderSaveRequest("dev12",
                1L,
                null, 0,
                PaymentType.CREDIT_CARD, "서울특별시 종로구",
                Collections.singletonList(new CartSaveRequest(1L, 2)));

        given(orderService.addOrder(request)).willReturn(savedOrderId);

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf().asHeader()))
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

        then(orderService).should().addOrder(request);
    }

    @Test
    @DisplayName("가게에서 주문을 조회한다.")
    void findOrderByRestaurantTest() throws Exception {
        long restaurantId = 1L;
        OrderListRestaurantRequest request = new OrderListRestaurantRequest(
                restaurantId, 0, 3, LocalDate.now().minusMonths(1),
                LocalDate.now());

        List<OrderRestaurantResponse> orderRestaurantResponses = List.of(
                new OrderRestaurantResponse(LocalDateTime.now(),
                        Collections.singletonList(
                                new CartResponse("참치 김밥", 1, 4500)), 4500, 3000,
                        7500, 0, "PAYMENT_COMPLETED"),
                new OrderRestaurantResponse(LocalDateTime.now(),
                        Collections.singletonList(new CartResponse("돈가스", 1, 7000)),
                        7000, 3000, 7500, 2500, "ACCEPTED")
        );

        OrderListRestaurantResponse response = new OrderListRestaurantResponse(
                false,
                2,
                orderRestaurantResponses
        );

        given(orderService.findOrderByRestaurant(request)).willReturn(
                response);

        mockMvc.perform(get("/api/v1/restaurants/orders")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
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

        then(orderService).should().findOrderByRestaurant(request);
    }

    @Test
    @DisplayName("존재하지 않는 가게의 주문을 조회하려 하면 404 NotFound 를 응답한다.")
    void findOrderByRestaurantNotFoundTest() throws Exception {
        Long wrongRestaurantId = -1L;
        OrderListRestaurantRequest request = new OrderListRestaurantRequest(
                wrongRestaurantId, 0, 3, LocalDate.now().minusMonths(1),
                LocalDate.now());

        given(orderService.findOrderByRestaurant(request)).willThrow(NotFoundException.class);

        mockMvc.perform(get("/api/v1/restaurants/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound());

        then(orderService).should().findOrderByRestaurant(request);
    }

    @Test
    @DisplayName("회원의 주문을 조회한다.")
    void findOrderByCustomerTest() throws Exception {
        OrderListCustomerRequest request = new OrderListCustomerRequest(
                "dev12", 0, 10, LocalDate.now().minusMonths(1),
                LocalDate.now());

        List<OrderSummeryResponse> orderSummeryResponses = List.of(
                new OrderSummeryResponse(1L, LocalDateTime.now(), "ACCEPTED", "김밥나라", 15000,
                        Collections.singletonList(new CartSummeryResponse("돈가스", 2))),
                new OrderSummeryResponse(1L, LocalDateTime.now(), "ACCEPTED", "맥도날드", 13000,
                        List.of(
                                new CartSummeryResponse("1955버거 세트", 1),
                                new CartSummeryResponse("빅맥 세트", 1)
                        ))
        );

        OrderListCustomerResponse response = new OrderListCustomerResponse(
                false,
                2,
                orderSummeryResponses
        );

        given(orderService.findOrderByCustomer(request)).willReturn(response);

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

        then(orderService).should().findOrderByCustomer(request);
    }

    @Test
    @DisplayName("존재하지 않는 회원의 주문을 조회하려하면 404 NotFound 를 응답한다.")
    void findOrderForCustomerTest() throws Exception {
        String wrongCustomerId = "";
        OrderListCustomerRequest request = new OrderListCustomerRequest(
                wrongCustomerId, 0, 1, LocalDate.now().minusMonths(1),
                LocalDate.now());

        given(orderService.findOrderByCustomer(request)).willThrow(NotFoundException.class);

        mockMvc.perform(get("/api/v1/customers/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound());

        then(orderService).should().findOrderByCustomer(request);
    }

    @Test
    @DisplayName("회원 주문을 단건 조회한다.")
    void findDetailOrderForCustomerTest() throws Exception {
        long orderId = 1L;

        List<CartResponse> cartResponses = List.of(
                new CartResponse("참치 김밥", 1, 4500),
                new CartResponse("치즈 라면", 1, 5000),
                new CartResponse("돈가스", 1, 7000)
        );

        OrderCustomerResponse request = new OrderCustomerResponse(
                LocalDateTime.now(),
                cartResponses,
                16500, 3000, 3000, 0, "ACCEPTED", "서울특별시 강남구"
        );

        given(orderService.findDetailOrderForCustomer(orderId)).willReturn(
                request);

        mockMvc.perform(get("/api/v1/customers/orders/{orderId}", orderId)
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

        then(orderService).should().findDetailOrderForCustomer(orderId);
    }

    @Test
    @DisplayName("존재하지 않는 주문을 회원이 단건 조회하려하면 404 NotFound 를 응답한다.")
    void findDetailOrderForCustomerNotFoundTest() throws Exception {
        Long wrongOrderId = -1L;
        given(orderService.findDetailOrderForCustomer(wrongOrderId)).willThrow(
                NotFoundException.class);

        mockMvc.perform(get("/api/v1/customers/orders/{orderId}", wrongOrderId)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound());

        then(orderService).should().findDetailOrderForCustomer(wrongOrderId);
    }

    @Test
    @DisplayName("가게의 주문을 단건 조회한다.")
    void findDetailOrderForRestaurantTest() throws Exception {
        long orderId = 1L;

        List<CartResponse> cartResponses = List.of(
                new CartResponse("참치 김밥", 1, 4500),
                new CartResponse("치즈 라면", 1, 5000),
                new CartResponse("돈가스", 1, 7000)
        );

        OrderRestaurantResponse request = new OrderRestaurantResponse(
                LocalDateTime.now(),
                cartResponses,
                16500, 3000, 13000, 6500, "PAYMENT_COMPLETED"
        );

        given(orderService.findDetailOrderByIdForRestaurant(orderId)).willReturn(
                request);

        mockMvc.perform(get("/api/v1/restaurants/orders/{orderId}", orderId)
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

        then(orderService).should().findDetailOrderByIdForRestaurant(orderId);
    }

    @Test
    @DisplayName("존재하지 않는 주문을 가게에서 단건 조회하려하면 404 NotFound 를 응답한다.")
    void findDetailOrderForRestaurantNotFoundTest() throws Exception {
        Long wrongOrderId = -1L;
        given(orderService.findDetailOrderByIdForRestaurant(wrongOrderId)).willThrow(
                NotFoundException.class);

        mockMvc.perform(get("/api/v1/restaurants/orders/{orderId}", wrongOrderId)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound());

        then(orderService).should().findDetailOrderByIdForRestaurant(wrongOrderId);
    }

    @Test
    @DisplayName("기간별 가게 주문 횟수와 매출 정보를 조회한다.")
    void findOrderStatisticsTest() throws Exception {
        OrderStatisticsRequest request = new OrderStatisticsRequest(
                1L, LocalDate.now().minusMonths(1),
                LocalDate.now());

        given(orderService.findOrderStatistics(request)).willReturn(
                new OrderStatisticsResponse(10L, 150000L, 35000L));

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

        then(orderService).should().findOrderStatistics(request);
    }

    @Test
    @DisplayName("존재하지 않는 가게의 매출 정보를 조회하려 하면 404 NotFound 를 응답한다.")
    void findOrderStatisticsNotFoundRestaurantTest() throws Exception {
        Long wrongRestaurantId = -1L;
        OrderStatisticsRequest request = new OrderStatisticsRequest(
                wrongRestaurantId, LocalDate.now().minusMonths(1),
                LocalDate.now());

        given(orderService.findOrderStatistics(request)).willThrow(NotFoundException.class);

        mockMvc.perform(get("/api/v1/restaurants/orders/statistics")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound());

        then(orderService).should().findOrderStatistics(request);
    }

    @Test
    @DisplayName("주문을 취소한다.")
    void cancelOrderTest() throws Exception {
        long orderId = 1L;

        mockMvc.perform(patch("/api/v1/orders/{orderId}/cancel", orderId)
                        .with(csrf().asHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("cancel-order",
                        pathParameters(
                                parameterWithName("orderId").description("취소할 주문 ID")
                        )
                ));

        then(orderService).should().cancelOrder(orderId);
    }

    @Test
    @DisplayName("존재하지 않는 주문을 취소하려하면 404 NotFound 를 응답한다.")
    void cancelOrderNotFoundTest() throws Exception {
        long wrongOrderId = -1L;
        doThrow(NotFoundException.class).when(orderService).cancelOrder(wrongOrderId);

        mockMvc.perform(patch("/api/v1/orders/{orderId}/cancel", wrongOrderId)
                        .with(csrf().asHeader()))
                .andDo(print())
                .andExpect(status().isNotFound());

        then(orderService).should().cancelOrder(wrongOrderId);
    }

    @Test
    @DisplayName("주문을 수락한다.")
    void acceptOrderTest() throws Exception {
        long orderId = 1L;
        OrderAcceptRequest request = new OrderAcceptRequest(30);

        mockMvc.perform(patch("/api/v1/orders/{orderId}/accept", orderId)
                        .with(csrf().asHeader())
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

        then(orderService).should().acceptOrder(orderId, request);
    }

    @Test
    @DisplayName("존재하지 않는 주문을 수락하려하면 404 NotFound 를 응답한다.")
    void acceptOrderNotFoundTest() throws Exception {
        OrderAcceptRequest request = new OrderAcceptRequest(30);
        long wrongOrderId = -1L;
        doThrow(NotFoundException.class).when(orderService).acceptOrder(wrongOrderId, request);

        mockMvc.perform(patch("/api/v1/orders/{orderId}/accept", wrongOrderId)
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound());

        then(orderService).should().acceptOrder(wrongOrderId, request);
    }
}