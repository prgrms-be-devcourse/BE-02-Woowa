package com.example.woowa.order.review.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.woowa.customer.customer.dto.CustomerAddressCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerFindResponse;
import com.example.woowa.customer.customer.dto.CustomerGradeCreateRequest;
import com.example.woowa.customer.customer.repository.CustomerAddressRepository;
import com.example.woowa.customer.customer.repository.CustomerGradeRepository;
import com.example.woowa.customer.customer.repository.CustomerRepository;
import com.example.woowa.customer.customer.service.CustomerGradeService;
import com.example.woowa.customer.customer.service.CustomerService;
import com.example.woowa.order.order.entity.Order;
import com.example.woowa.order.order.service.OrderService;
import com.example.woowa.order.review.converter.ReviewMapper;
import com.example.woowa.order.review.dto.ReviewCreateRequest;
import com.example.woowa.order.review.dto.ReviewFindResponse;
import com.example.woowa.order.review.dto.ReviewUpdateRequest;
import com.example.woowa.order.review.repository.ReviewRepository;
import com.example.woowa.order.review.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
class ReviewControllerTest {
  @Autowired
  MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private CustomerService customerService;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private CustomerGradeService customerGradeService;

  @Autowired
  private CustomerGradeRepository customerGradeRepository;

  @Autowired
  private CustomerAddressRepository customerAddressRepository;

  @Autowired
  private ReviewService reviewService;

  @Autowired
  private ReviewRepository reviewRepository;

  @Autowired
  private ReviewMapper reviewMapper;

  @Mock
  private OrderService orderService;

  public void makeDefaultCustomerGrade() {
    CustomerGradeCreateRequest customerGradeCreateRequest = new CustomerGradeCreateRequest(5, "일반", 3000, 2);
    customerGradeService.createCustomerGrade(customerGradeCreateRequest);
  }

  public String getCustomerLoginId() {
    CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest("서울특별시 동작구 상도동","빌라 101호","집");
    CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest("dev12","Programmers123!", "2000-01-01", customerAddressCreateRequest);
    CustomerFindResponse customerFindResponse = customerService.createCustomer(
        customerCreateRequest);
    return customerFindResponse.getLoginId();
  }

  @BeforeEach
  void settingBeforeTest() {
    reviewService = new ReviewService(reviewRepository, customerService, orderService, reviewMapper);
    makeDefaultCustomerGrade();
  }

  @AfterEach
  void settingAfterTest() {
    reviewRepository.deleteAll();
    customerAddressRepository.deleteAll();
    customerRepository.deleteAll();
    customerGradeRepository.deleteAll();
  }

  @Test
  void createReview() throws Exception {
    String customerId = getCustomerLoginId();
    ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest("정말정말 맛있습니다.", 5);
    given(orderService.findOrderById(any())).willReturn(null);

    mockMvc.perform(
            post("/api/v1/reviews/{loginId}/{orderId}", customerId, 10)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewCreateRequest))
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("reviews-create",
            requestFields(
                fieldWithPath("content").type(JsonFieldType.STRING).description("리뷰 내용"),
                fieldWithPath("scoreType").type(JsonFieldType.NUMBER).description("평점")
            ),
            responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("생성된 리뷰 아이디"),
                fieldWithPath("content").type(JsonFieldType.STRING).description("리뷰 내용"),
                fieldWithPath("scoreType").type(JsonFieldType.NUMBER).description("평점")
            )
        ));
  }

  @Test
  void findReview() throws Exception {
    String customerId = getCustomerLoginId();
    ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest("정말정말 맛있습니다.", 5);
    given(orderService.findOrderById(any())).willReturn(null);

    String body = mockMvc.perform(
            post("/api/v1/reviews/{loginId}/{orderId}", customerId, 10)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewCreateRequest))
        ).andReturn().getResponse().getContentAsString();

    ReviewFindResponse reviewFindResponse = objectMapper.readValue(body, ReviewFindResponse.class);

    mockMvc.perform(
            get("/api/v1/reviews/{id}", reviewFindResponse.getId())
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("reviews-find",
            responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("생성된 리뷰 아이디"),
                fieldWithPath("content").type(JsonFieldType.STRING).description("리뷰 내용"),
                fieldWithPath("scoreType").type(JsonFieldType.NUMBER).description("평점")
            )
        ));
  }

  @Test
  void findUserReview() throws Exception {
    String customerId = getCustomerLoginId();
    ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest("정말정말 맛있습니다.", 5);
    given(orderService.findOrderById(any())).willReturn(null);

    String body = mockMvc.perform(
        post("/api/v1/reviews/{loginId}/{orderId}", customerId, 10)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(reviewCreateRequest))
    ).andReturn().getResponse().getContentAsString();

    ReviewFindResponse reviewFindResponse = objectMapper.readValue(body, ReviewFindResponse.class);
    mockMvc.perform(
            get("/api/v1/reviews/user/{loginId}", customerId)
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("reviews-user-find",
            responseFields(
                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("생성된 리뷰 아이디"),
                fieldWithPath("[].content").type(JsonFieldType.STRING).description("리뷰 내용"),
                fieldWithPath("[].scoreType").type(JsonFieldType.NUMBER).description("평점")
            )
        ));
  }

  @Test
  void updateReview() throws Exception {
    String customerId = getCustomerLoginId();
    ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest("정말정말 맛있습니다.", 5);
    given(orderService.findOrderById(any())).willReturn(null);

    String body = mockMvc.perform(
        post("/api/v1/reviews/{loginId}/{orderId}", customerId, 10)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(reviewCreateRequest))
    ).andReturn().getResponse().getContentAsString();

    ReviewFindResponse reviewFindResponse = objectMapper.readValue(body, ReviewFindResponse.class);
    ReviewUpdateRequest reviewUpdateRequest = new ReviewUpdateRequest("정말정말 맛없습니다.", 1);

    mockMvc.perform(
            put("/api/v1/reviews/{id}", reviewFindResponse.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewUpdateRequest))
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("reviews-update",
            requestFields(
                fieldWithPath("content").type(JsonFieldType.STRING).description("리뷰 내용"),
                fieldWithPath("scoreType").type(JsonFieldType.NUMBER).description("평점")
            ),
            responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("수정된 리뷰 아이디"),
                fieldWithPath("content").type(JsonFieldType.STRING).description("리뷰 내용"),
                fieldWithPath("scoreType").type(JsonFieldType.NUMBER).description("평점")
            )
        ));
  }

  @Test
  void deleteReview() throws Exception {
    String customerId = getCustomerLoginId();
    ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest("정말정말 맛있습니다.", 5);
    given(orderService.findOrderById(any())).willReturn(null);

    String body = mockMvc.perform(
        post("/api/v1/reviews/{loginId}/{orderId}", customerId, 10)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(reviewCreateRequest))
    ).andReturn().getResponse().getContentAsString();

    ReviewFindResponse reviewFindResponse = objectMapper.readValue(body, ReviewFindResponse.class);

    mockMvc.perform(
            delete("/api/v1/reviews/{id}", reviewFindResponse.getId())
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("reviews-delete"
        ));
  }
}