package com.example.woowa.customer.customer.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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

import com.example.woowa.customer.customer.dto.CustomerAddressCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerFindResponse;
import com.example.woowa.customer.customer.dto.CustomerUpdateRequest;
import com.example.woowa.customer.customer.entity.CustomerGrade;
import com.example.woowa.customer.customer.repository.CustomerAddressRepository;
import com.example.woowa.customer.customer.repository.CustomerRepository;
import com.example.woowa.customer.customer.service.CustomerGradeService;
import com.example.woowa.delivery.entity.AreaCode;
import com.example.woowa.delivery.service.AreaCodeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private CustomerAddressRepository customerAddressRepository;

  @MockBean
  private CustomerGradeService customerGradeService;

  @MockBean
  private AreaCodeService areaCodeService;

  @BeforeEach
  void settingBeforeTest() {
    customerRepository.deleteAll();
    customerAddressRepository.deleteAll();
    given(customerGradeService.findDefaultCustomerGrade()).willReturn(new CustomerGrade(1, "일반",3000, 2));
    given(areaCodeService.findByAddress(any())).willReturn(new AreaCode("1", "서울특별시 동작구", false));
  }

  @AfterEach
  void settingAfterTest() {
    customerRepository.deleteAll();
    customerAddressRepository.deleteAll();
  }

  @Test
  void createCustomer() throws Exception {
    CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest("서울특별시 동작구 상도동","빌라 101호","집");
    CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest("dev12","Programmers123!", "2000-01-01", customerAddressCreateRequest);

    mockMvc.perform(
            post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerCreateRequest))
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("customers-create",
            requestFields(
                fieldWithPath("loginId").type(JsonFieldType.STRING).description("아이디"),
                fieldWithPath("loginPassword").type(JsonFieldType.STRING).description("비밀 번호"),
                fieldWithPath("birthdate").type(JsonFieldType.STRING).description("생년월일"),
                fieldWithPath("address.defaultAddress").type(JsonFieldType.STRING).description("기본 주소"),
                fieldWithPath("address.detailAddress").type(JsonFieldType.STRING).description("상세 주소"),
                fieldWithPath("address.nickname").type(JsonFieldType.STRING).description("집")
            ),
            responseFields(
                fieldWithPath("loginId").type(JsonFieldType.STRING).description("아이디"),
                fieldWithPath("birthdate").type(JsonFieldType.STRING).description("생년월일"),
                fieldWithPath("point").type(JsonFieldType.NUMBER).description("적립 포인트"),
                fieldWithPath("isIssued").type(JsonFieldType.BOOLEAN).description("정기쿠폰 발행 여부"),
                fieldWithPath("orderPerMonth").type(JsonFieldType.NUMBER).description("한 달 간 구매횟수"),
                fieldWithPath("customerGrade.id").type(JsonFieldType.NUMBER).description("고객 등급 아이디"),
                fieldWithPath("customerGrade.orderCount").type(JsonFieldType.NUMBER).description("최소 주문 횟수"),
                fieldWithPath("customerGrade.title").type(JsonFieldType.STRING).description("고객 등급명"),
                fieldWithPath("customerGrade.discountPrice").type(JsonFieldType.NUMBER).description("할인 가격"),
                fieldWithPath("customerGrade.voucherCount").type(JsonFieldType.NUMBER).description("쿠폰 갯수")
            )
        ));
  }

  @Test
  void readCustomer() throws Exception {
    CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest("서울특별시 동작구 상도동","빌라 101호","집");
    CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest("dev12","Programmers123!", "2000-01-01", customerAddressCreateRequest);

    String body = mockMvc.perform(
        post("/api/v1/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customerCreateRequest))
    ).andReturn().getResponse().getContentAsString();

    CustomerFindResponse customerFindResponse = objectMapper.readValue(body, CustomerFindResponse.class);

    mockMvc.perform(
            get("/api/v1/customers/{loginId}", customerFindResponse.getLoginId())
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("customers-find",
            responseFields(
                fieldWithPath("loginId").type(JsonFieldType.STRING).description("아이디"),
                fieldWithPath("birthdate").type(JsonFieldType.STRING).description("생년월일"),
                fieldWithPath("point").type(JsonFieldType.NUMBER).description("적립 포인트"),
                fieldWithPath("orderPerMonth").type(JsonFieldType.NUMBER).description("한 달 간 구매횟수"),
                fieldWithPath("isIssued").type(JsonFieldType.BOOLEAN).description("정기쿠폰 발행 여부"),
                fieldWithPath("customerGrade.id").type(JsonFieldType.NUMBER).description("고객 등급 아이디"),
                fieldWithPath("customerGrade.orderCount").type(JsonFieldType.NUMBER).description("최소 주문 횟수"),
                fieldWithPath("customerGrade.title").type(JsonFieldType.STRING).description("고객 등급명"),
                fieldWithPath("customerGrade.discountPrice").type(JsonFieldType.NUMBER).description("할인 가격"),
                fieldWithPath("customerGrade.voucherCount").type(JsonFieldType.NUMBER).description("쿠폰 갯수")
            )
        ));
  }

  @Test
  void updateCustomer() throws Exception {
    CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest("서울특별시 동작구 상도동","빌라 101호","집");
    CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest("dev12","Programmers123!", "2000-01-01", customerAddressCreateRequest);

    String body = mockMvc.perform(
        post("/api/v1/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customerCreateRequest))
    ).andReturn().getResponse().getContentAsString();

    CustomerFindResponse customerFindResponse = objectMapper.readValue(body, CustomerFindResponse.class);
    CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest("Programmers234!");

    mockMvc.perform(
            put("/api/v1/customers/{loginId}", customerFindResponse.getLoginId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerUpdateRequest))
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("customers-update",
            requestFields(
                fieldWithPath("loginPassword").type(JsonFieldType.STRING).description("변경할 비밀번호")
            ),
            responseFields(
                fieldWithPath("loginId").type(JsonFieldType.STRING).description("아이디"),
                fieldWithPath("birthdate").type(JsonFieldType.STRING).description("생년월일"),
                fieldWithPath("point").type(JsonFieldType.NUMBER).description("적립 포인트"),
                fieldWithPath("orderPerMonth").type(JsonFieldType.NUMBER).description("한 달 간 구매횟수"),
                fieldWithPath("isIssued").type(JsonFieldType.BOOLEAN).description("정기쿠폰 발행 여부"),
                fieldWithPath("customerGrade.id").type(JsonFieldType.NUMBER).description("고객 등급 아이디"),
                fieldWithPath("customerGrade.orderCount").type(JsonFieldType.NUMBER).description("최소 주문 횟수"),
                fieldWithPath("customerGrade.title").type(JsonFieldType.STRING).description("고객 등급명"),
                fieldWithPath("customerGrade.discountPrice").type(JsonFieldType.NUMBER).description("할인 가격"),
                fieldWithPath("customerGrade.voucherCount").type(JsonFieldType.NUMBER).description("쿠폰 갯수")
            )
        ));
  }

  @Test
  void updateCustomerOnFirstDay() throws Exception {
    CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest("서울특별시 동작구 상도동","빌라 101호","집");
    CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest("dev12","Programmers123!", "2000-01-01", customerAddressCreateRequest);

    mockMvc.perform(
        post("/api/v1/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customerCreateRequest))
    );

    mockMvc.perform(
            put("/api/v1/customers/firstday/{loginId}", customerCreateRequest.getLoginId())
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("customers-update-firstday",
            responseFields(
                fieldWithPath("loginId").type(JsonFieldType.STRING).description("아이디"),
                fieldWithPath("birthdate").type(JsonFieldType.STRING).description("생년월일"),
                fieldWithPath("point").type(JsonFieldType.NUMBER).description("적립 포인트"),
                fieldWithPath("orderPerMonth").type(JsonFieldType.NUMBER).description("한 달 간 구매횟수"),
                fieldWithPath("isIssued").type(JsonFieldType.BOOLEAN).description("정기쿠폰 발행 여부"),
                fieldWithPath("customerGrade.id").type(JsonFieldType.NUMBER).description("고객 등급 아이디"),
                fieldWithPath("customerGrade.orderCount").type(JsonFieldType.NUMBER).description("최소 주문 횟수"),
                fieldWithPath("customerGrade.title").type(JsonFieldType.STRING).description("고객 등급명"),
                fieldWithPath("customerGrade.discountPrice").type(JsonFieldType.NUMBER).description("할인 가격"),
                fieldWithPath("customerGrade.voucherCount").type(JsonFieldType.NUMBER).description("쿠폰 갯수")
            )
        ));
  }

  @Test
  void deleteCustomer() throws Exception {
    CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest("서울특별시 동작구 상도동","빌라 101호","집");
    CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest("dev12","Programmers123!", "2000-01-01", customerAddressCreateRequest);

    mockMvc.perform(
        post("/api/v1/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customerCreateRequest))
    );

    mockMvc.perform(
            delete("/api/v1/customers/{loginId}", customerCreateRequest.getLoginId())
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("customers-delete",
            responseBody()
        ));
  }
}