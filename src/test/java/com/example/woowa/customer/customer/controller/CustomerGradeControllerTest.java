package com.example.woowa.customer.customer.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseBody;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.woowa.customer.customer.dto.CustomerGradeCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerGradeFindResponse;
import com.example.woowa.customer.customer.dto.CustomerGradeUpdateRequest;
import com.example.woowa.customer.customer.repository.CustomerGradeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
class CustomerGradeControllerTest {
  @Autowired
  MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private CustomerGradeRepository customerGradeRepository;

  @BeforeEach
  void setting() {
    customerGradeRepository.deleteAll();
  }

  @AfterEach
  void setting1() {
    customerGradeRepository.deleteAll();
  }

  @Test
  void createCustomerGrade() throws Exception {
    CustomerGradeCreateRequest customerGradeCreateRequest = new CustomerGradeCreateRequest(5, "일반", 3000, 2);

    mockMvc.perform(
            post("/api/v1/customers/grades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerGradeCreateRequest))
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("customers-grades-create",
            requestFields(
                fieldWithPath("orderCount").type(JsonFieldType.NUMBER).description("최소 주문 횟수"),
                fieldWithPath("title").type(JsonFieldType.STRING).description("등급 이름"),
                fieldWithPath("discountPrice").type(JsonFieldType.NUMBER).description("할인 가격"),
                fieldWithPath("voucherCount").type(JsonFieldType.NUMBER).description("쿠폰 갯수")
            ),
            responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("생성된 고객 등급 아이디"),
                fieldWithPath("orderCount").type(JsonFieldType.NUMBER).description("최소 주문 횟수"),
                fieldWithPath("title").type(JsonFieldType.STRING).description("등급 이름"),
                fieldWithPath("discountPrice").type(JsonFieldType.NUMBER).description("할인 가격"),
                fieldWithPath("voucherCount").type(JsonFieldType.NUMBER).description("쿠폰 갯수")
            )
        ));
  }

  @Test
  void readCustomerGrade() throws Exception {
    CustomerGradeCreateRequest customerGradeCreateRequest = new CustomerGradeCreateRequest(5, "일반", 3000, 2);

    String body = mockMvc.perform(
            post("/api/v1/customers/grades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerGradeCreateRequest))
        ).andReturn().getResponse().getContentAsString();

    CustomerGradeFindResponse customerGradeFindResponse = objectMapper.readValue(body, CustomerGradeFindResponse.class);

    mockMvc.perform(
            get("/api/v1/customers/grades/{id}", customerGradeFindResponse.getId())
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("customers-grades-find",
            responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("생성된 고객 등급 아이디"),
                fieldWithPath("orderCount").type(JsonFieldType.NUMBER).description("최소 주문 횟수"),
                fieldWithPath("title").type(JsonFieldType.STRING).description("등급 이름"),
                fieldWithPath("discountPrice").type(JsonFieldType.NUMBER).description("할인 가격"),
                fieldWithPath("voucherCount").type(JsonFieldType.NUMBER).description("쿠폰 갯수")
            )
        ));
  }

  @Test
  void updateCustomerGrade() throws Exception {
    CustomerGradeCreateRequest customerGradeCreateRequest = new CustomerGradeCreateRequest(5, "일반", 3000, 2);
    String body = mockMvc.perform(
        post("/api/v1/customers/grades")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customerGradeCreateRequest))
    ).andReturn().getResponse().getContentAsString();
    CustomerGradeFindResponse customerGradeFindResponse = objectMapper.readValue(body, CustomerGradeFindResponse.class);
    CustomerGradeUpdateRequest updateCustomerGradeDto = new CustomerGradeUpdateRequest(10, "실버", 2000, 2);

    mockMvc.perform(
            put("/api/v1/customers/grades/{id}", customerGradeFindResponse.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCustomerGradeDto))
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("customers-grades-update",
            requestFields(
                fieldWithPath("orderCount").type(JsonFieldType.NUMBER).description("최소 주문 횟수"),
                fieldWithPath("title").type(JsonFieldType.STRING).description("등급 이름"),
                fieldWithPath("discountPrice").type(JsonFieldType.NUMBER).description("할인 가격"),
                fieldWithPath("voucherCount").type(JsonFieldType.NUMBER).description("쿠폰 갯수")
            ),
            responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("생성된 고객 등급 아이디"),
                fieldWithPath("orderCount").type(JsonFieldType.NUMBER).description("최소 주문 횟수"),
                fieldWithPath("title").type(JsonFieldType.STRING).description("등급 이름"),
                fieldWithPath("discountPrice").type(JsonFieldType.NUMBER).description("할인 가격"),
                fieldWithPath("voucherCount").type(JsonFieldType.NUMBER).description("쿠폰 갯수")
            )
        ));
  }

  @Test
  void deleteCustomerGrade() throws Exception {
    CustomerGradeCreateRequest customerGradeCreateRequest = new CustomerGradeCreateRequest(5, "일반", 3000, 2);

    String body = mockMvc.perform(
        post("/api/v1/customers/grades")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customerGradeCreateRequest))
    ).andReturn().getResponse().getContentAsString();

    CustomerGradeFindResponse customerGradeFindResponse = objectMapper.readValue(body, CustomerGradeFindResponse.class);

    mockMvc.perform(
            delete("/api/v1/customers/grades/{id}", customerGradeFindResponse.getId())
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("customers-grades-delete",
            responseBody()
        ));
  }
}