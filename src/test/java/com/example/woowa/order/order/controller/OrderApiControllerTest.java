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
    @DisplayName("????????? ????????????.")
    void addOrderTest() throws Exception {
        long savedOrderId = 1L;
        OrderSaveRequest request = new OrderSaveRequest("dev12",
                1L,
                null, 0,
                PaymentType.CREDIT_CARD, "??????????????? ?????????",
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
                                        .description("????????? ????????? ?????????"),
                                fieldWithPath("restaurantId").type(JsonFieldType.NUMBER)
                                        .description("????????? ??????????????? ID"),
                                fieldWithPath("voucherId").description(
                                        "????????? ????????? ID(?????? ????????? ????????? ?????? ?????? ????????? null??? ??????)"),
                                fieldWithPath("usePoint").description("????????? ????????? ????????? ?????????"),
                                fieldWithPath("paymentType").description("?????? ??????"),
                                fieldWithPath("deliveryAddress").description("?????? ??????"),
                                fieldWithPath("carts[]").type(JsonFieldType.ARRAY)
                                        .description("???????????? ?????? ??????"),
                                fieldWithPath("carts[].menuId").type(JsonFieldType.NUMBER)
                                        .description("????????? ID"),
                                fieldWithPath("carts[].quantity").type(JsonFieldType.NUMBER)
                                        .description("?????? ??????")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("????????? ????????? ??????")
                        )
                ));

        then(orderService).should().addOrder(request);
    }

    @Test
    @DisplayName("???????????? ????????? ????????????.")
    void findOrderByRestaurantTest() throws Exception {
        long restaurantId = 1L;
        OrderListRestaurantRequest request = new OrderListRestaurantRequest(
                restaurantId, 0, 3, LocalDate.now().minusMonths(1),
                LocalDate.now());

        List<OrderRestaurantResponse> orderRestaurantResponses = List.of(
                new OrderRestaurantResponse(LocalDateTime.now(),
                        Collections.singletonList(
                                new CartResponse("?????? ??????", 1, 4500)), 4500, 3000,
                        7500, 0, "PAYMENT_COMPLETED"),
                new OrderRestaurantResponse(LocalDateTime.now(),
                        Collections.singletonList(new CartResponse("?????????", 1, 7000)),
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
                                        .description("?????? ID"),
                                fieldWithPath("pageNum").type(JsonFieldType.NUMBER)
                                        .description("????????? ??????(0?????? ??????)"),
                                fieldWithPath("size").type(JsonFieldType.NUMBER)
                                        .description("1???????????? ?????? ??????"),
                                fieldWithPath("from").type(JsonFieldType.STRING)
                                        .description("?????? ?????????(??????: yyyy-MM-dd)"),
                                fieldWithPath("end").type(JsonFieldType.STRING)
                                        .description("?????? ?????????(??????: yyyy-MM-dd)")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(
                                        MediaType.APPLICATION_JSON_VALUE)
                        ),
                        responseFields(
                                fieldWithPath("hasNextPage").type(JsonFieldType.BOOLEAN)
                                        .description("?????? ???????????? ????????? TRUE"),
                                fieldWithPath("size").type(JsonFieldType.NUMBER)
                                        .description("?????? ?????? ??????"),
                                fieldWithPath("orders[]").type(JsonFieldType.ARRAY)
                                        .description("?????? ??????"),
                                fieldWithPath("orders[].createdAt").type(JsonFieldType.STRING)
                                        .description("?????? ?????? ??????"),
                                fieldWithPath("orders[].orderPrice").type(JsonFieldType.NUMBER)
                                        .description("????????? ?????? ??????(????????? ??????)"),
                                fieldWithPath("orders[].deliveryFee").type(JsonFieldType.NUMBER)
                                        .description("?????????"),
                                fieldWithPath("orders[].afterDiscountTotalPrice").type(
                                        JsonFieldType.NUMBER).description("????????? ??????(????????? ??????"),
                                fieldWithPath("orders[].totalDiscountPrice").type(
                                        JsonFieldType.NUMBER).description("?????????(?????? + ????????? ??????)"),
                                fieldWithPath("orders[].orderStatus").type(JsonFieldType.STRING)
                                        .description("?????? ??????(PAYMENT_COMPLETED, ACCEPTED, CANCEL)"),
                                fieldWithPath("orders[].menus[]").type(JsonFieldType.ARRAY)
                                        .description("????????? ?????? ??????"),
                                fieldWithPath("orders[].menus[].menuName").type(
                                                JsonFieldType.STRING)
                                        .description("?????????"),
                                fieldWithPath("orders[].menus[].quantity").type(
                                                JsonFieldType.NUMBER)
                                        .description("??????"),
                                fieldWithPath("orders[].menus[].totalPrice").type(
                                                JsonFieldType.NUMBER)
                                        .description("??????")
                        )
                ));

        then(orderService).should().findOrderByRestaurant(request);
    }

    @Test
    @DisplayName("???????????? ?????? ????????? ????????? ???????????? ?????? 404 NotFound ??? ????????????.")
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
    @DisplayName("????????? ????????? ????????????.")
    void findOrderByCustomerTest() throws Exception {
        OrderListCustomerRequest request = new OrderListCustomerRequest(
                "dev12", 0, 10, LocalDate.now().minusMonths(1),
                LocalDate.now());

        List<OrderSummeryResponse> orderSummeryResponses = List.of(
                new OrderSummeryResponse(1L, LocalDateTime.now(), "ACCEPTED", "????????????", 15000,
                        Collections.singletonList(new CartSummeryResponse("?????????", 2))),
                new OrderSummeryResponse(1L, LocalDateTime.now(), "ACCEPTED", "????????????", 13000,
                        List.of(
                                new CartSummeryResponse("1955?????? ??????", 1),
                                new CartSummeryResponse("?????? ??????", 1)
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
                                        .description("?????? ????????? ID"),
                                fieldWithPath("pageNum").type(JsonFieldType.NUMBER)
                                        .description("????????? ??????"),
                                fieldWithPath("size").type(JsonFieldType.NUMBER)
                                        .description("???????????? ?????? ??????"),
                                fieldWithPath("from").type(JsonFieldType.STRING)
                                        .description("?????? ?????????(??????: yyyy-MM-dd)"),
                                fieldWithPath("end").type(JsonFieldType.STRING)
                                        .description("?????? ?????????(??????: yyyy-MM-dd)")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(
                                        MediaType.APPLICATION_JSON_VALUE)
                        ),
                        responseFields(
                                fieldWithPath("hasNextPage").type(JsonFieldType.BOOLEAN)
                                        .description("?????? ???????????? ????????? TRUE"),
                                fieldWithPath("size").type(JsonFieldType.NUMBER)
                                        .description("?????? ?????? ??????"),
                                fieldWithPath("orders[]").type(JsonFieldType.ARRAY)
                                        .description("?????? ??????"),
                                fieldWithPath("orders[].id").type(JsonFieldType.NUMBER)
                                        .description("?????? ID"),
                                fieldWithPath("orders[].createdAt").type(JsonFieldType.STRING)
                                        .description("?????? ??????(??????: yyyy-MM-dd HH:mm:ss)"),
                                fieldWithPath("orders[].orderStatus").type(JsonFieldType.STRING)
                                        .description("?????? ??????"),
                                fieldWithPath("orders[].restaurantName").type(JsonFieldType.STRING)
                                        .description("?????????"),
                                fieldWithPath("orders[].afterDiscountTotalPrice").type(
                                                JsonFieldType.NUMBER)
                                        .description("?????? ?????? ??????(?????? ??????, ????????? ??????)"),
                                fieldWithPath("orders[].carts[]").type(JsonFieldType.ARRAY)
                                        .description("????????? ?????? ??????"),
                                fieldWithPath("orders[].carts[].menuTitle").type(
                                        JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("orders[].carts[].quantity").type(
                                                JsonFieldType.NUMBER)
                                        .description("??????")
                        )
                ));

        then(orderService).should().findOrderByCustomer(request);
    }

    @Test
    @DisplayName("???????????? ?????? ????????? ????????? ?????????????????? 404 NotFound ??? ????????????.")
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
    @DisplayName("?????? ????????? ?????? ????????????.")
    void findDetailOrderForCustomerTest() throws Exception {
        long orderId = 1L;

        List<CartResponse> cartResponses = List.of(
                new CartResponse("?????? ??????", 1, 4500),
                new CartResponse("?????? ??????", 1, 5000),
                new CartResponse("?????????", 1, 7000)
        );

        OrderCustomerResponse request = new OrderCustomerResponse(
                LocalDateTime.now(),
                cartResponses,
                16500, 3000, 3000, 0, "ACCEPTED", "??????????????? ?????????"
        );

        given(orderService.findDetailOrderForCustomer(orderId)).willReturn(
                request);

        mockMvc.perform(get("/api/v1/customers/orders/{orderId}", orderId)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("find-detail-order-by-customer",
                        pathParameters(
                                parameterWithName("orderId").description("????????? ?????? ID")
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
                                        .description("?????? ?????? ??????(??????: yyyy-MM-dd HH:mm:ss)"),
                                fieldWithPath("orderPrice").type(JsonFieldType.NUMBER)
                                        .description("????????? ?????? ??????(????????? ??????)"),
                                fieldWithPath("deliveryFee").type(JsonFieldType.NUMBER)
                                        .description("?????????"),
                                fieldWithPath("voucherDiscountPrice").type(JsonFieldType.NUMBER)
                                        .description("?????? ?????? ??????"),
                                fieldWithPath("usedPoint").type(JsonFieldType.NUMBER)
                                        .description("????????? ????????? ?????????"),
                                fieldWithPath("orderStatus").type(JsonFieldType.STRING)
                                        .description("?????? ??????(PAYMENT_COMPLETED, ACCEPTED, CANCEL)"),
                                fieldWithPath("deliveryAddress").type(JsonFieldType.STRING)
                                        .description("?????? ??????"),
                                fieldWithPath("menus[]").type(JsonFieldType.ARRAY)
                                        .description("????????? ?????? ??????"),
                                fieldWithPath("menus[].menuName").type(JsonFieldType.STRING)
                                        .description("?????????"),
                                fieldWithPath("menus[].quantity").type(JsonFieldType.NUMBER)
                                        .description("??????"),
                                fieldWithPath("menus[].totalPrice").type(JsonFieldType.NUMBER)
                                        .description("??????")
                        )
                ));

        then(orderService).should().findDetailOrderForCustomer(orderId);
    }

    @Test
    @DisplayName("???????????? ?????? ????????? ????????? ?????? ?????????????????? 404 NotFound ??? ????????????.")
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
    @DisplayName("????????? ????????? ?????? ????????????.")
    void findDetailOrderForRestaurantTest() throws Exception {
        long orderId = 1L;

        List<CartResponse> cartResponses = List.of(
                new CartResponse("?????? ??????", 1, 4500),
                new CartResponse("?????? ??????", 1, 5000),
                new CartResponse("?????????", 1, 7000)
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
                                parameterWithName("orderId").description("????????? ?????? ID")
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
                                        .description("?????? ?????? ??????(??????: yyyy-MM-dd HH:mm:ss)"),
                                fieldWithPath("orderPrice").type(JsonFieldType.NUMBER)
                                        .description("????????? ?????? ??????(????????? ??????)"),
                                fieldWithPath("deliveryFee").type(JsonFieldType.NUMBER)
                                        .description("?????????"),
                                fieldWithPath("afterDiscountTotalPrice").type(JsonFieldType.NUMBER)
                                        .description("????????? ?????? ?????? ??????(????????? ??????)"),
                                fieldWithPath("totalDiscountPrice").type(JsonFieldType.NUMBER)
                                        .description("??? ?????? ??????(?????? + ?????????)"),
                                fieldWithPath("orderStatus").type(JsonFieldType.STRING)
                                        .description("?????? ??????(PAYMENT_COMPLETED, ACCEPTED, CANCEL)"),
                                fieldWithPath("menus[]").type(JsonFieldType.ARRAY)
                                        .description("????????? ?????? ??????"),
                                fieldWithPath("menus[].menuName").type(JsonFieldType.STRING)
                                        .description("?????????"),
                                fieldWithPath("menus[].quantity").type(JsonFieldType.NUMBER)
                                        .description("??????"),
                                fieldWithPath("menus[].totalPrice").type(JsonFieldType.NUMBER)
                                        .description("??????")
                        )));

        then(orderService).should().findDetailOrderByIdForRestaurant(orderId);
    }

    @Test
    @DisplayName("???????????? ?????? ????????? ???????????? ?????? ?????????????????? 404 NotFound ??? ????????????.")
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
    @DisplayName("????????? ?????? ?????? ????????? ?????? ????????? ????????????.")
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
                                        .description("????????? ?????? ID"),
                                fieldWithPath("from").type(JsonFieldType.STRING)
                                        .description("?????? ?????????(??????: yyyy-MM-dd)"),
                                fieldWithPath("end").type(JsonFieldType.STRING)
                                        .description("?????? ?????????(??????: yyyy-MM-dd)")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(
                                        MediaType.APPLICATION_JSON_VALUE)
                        ),
                        responseFields(
                                fieldWithPath("orderCount").type(JsonFieldType.NUMBER)
                                        .description("?????? ??????"),
                                fieldWithPath("orderPrice").type(JsonFieldType.NUMBER)
                                        .description("?????? ??????(?????? ?????? ??????)"),
                                fieldWithPath("discountPrice").type(JsonFieldType.NUMBER)
                                        .description("?????? ??????")
                        )
                ));

        then(orderService).should().findOrderStatistics(request);
    }

    @Test
    @DisplayName("???????????? ?????? ????????? ?????? ????????? ???????????? ?????? 404 NotFound ??? ????????????.")
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
    @DisplayName("????????? ????????????.")
    void cancelOrderTest() throws Exception {
        long orderId = 1L;

        mockMvc.perform(patch("/api/v1/orders/{orderId}/cancel", orderId)
                        .with(csrf().asHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("cancel-order",
                        pathParameters(
                                parameterWithName("orderId").description("????????? ?????? ID")
                        )
                ));

        then(orderService).should().cancelOrder(orderId);
    }

    @Test
    @DisplayName("???????????? ?????? ????????? ?????????????????? 404 NotFound ??? ????????????.")
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
    @DisplayName("????????? ????????????.")
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
                                parameterWithName("orderId").description("????????? ?????? ID")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(
                                        MediaType.APPLICATION_JSON_VALUE)
                        ),
                        requestFields(
                                fieldWithPath("cookingTime").type(JsonFieldType.NUMBER)
                                        .description("?????? ?????? ??????(???)")
                        )
                ));

        then(orderService).should().acceptOrder(orderId, request);
    }

    @Test
    @DisplayName("???????????? ?????? ????????? ?????????????????? 404 NotFound ??? ????????????.")
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