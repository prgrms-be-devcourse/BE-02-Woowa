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

import com.example.woowa.customer.customer.dto.CustomerAddressCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerAddressFindResponse;
import com.example.woowa.customer.customer.dto.CustomerAddressUpdateRequest;
import com.example.woowa.customer.customer.dto.CustomerCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerFindResponse;
import com.example.woowa.customer.customer.dto.CustomerGradeCreateRequest;
import com.example.woowa.customer.customer.repository.CustomerAddressRepository;
import com.example.woowa.customer.customer.repository.CustomerGradeRepository;
import com.example.woowa.customer.customer.repository.CustomerRepository;
import com.example.woowa.customer.customer.service.CustomerGradeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
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
class CustomerAddressControllerTest {
  @Autowired
  MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private CustomerGradeService customerGradeService;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private CustomerAddressRepository customerAddressRepository;

  @Autowired
  private CustomerGradeRepository customerGradeRepository;

  public void makeDefaultCustomerGrade() {
    CustomerGradeCreateRequest customerGradeCreateRequest = new CustomerGradeCreateRequest(1, "일반", 3000, 2);
    customerGradeService.createCustomerGrade(customerGradeCreateRequest);
  }

  @BeforeEach
  void settingBeforeTest() {
    makeDefaultCustomerGrade();
  }

  @AfterEach
  void settingAfterTest() {
    customerRepository.deleteAll();
    customerAddressRepository.deleteAll();
    customerGradeRepository.deleteAll();
  }

  String createCustomer() throws Exception {
    CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest("서울특별시 동작구 상도동","빌라 101호","집");
    CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest("dev12","Programmers123!", "2000-01-01", customerAddressCreateRequest);

    String body = mockMvc.perform(
        post("/api/v1/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customerCreateRequest))
    ).andReturn().getResponse().getContentAsString();

    CustomerFindResponse customerFindResponse = objectMapper.readValue(body, CustomerFindResponse.class);
    return customerFindResponse.getLoginId();
  }

  @Test
  void createCustomerAddress() throws Exception {
    String id = createCustomer();
    CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest("서울특별시 동작구 상도동", "아파트 101호","집");

    mockMvc.perform(
            post("/api/v1/customers/addresses/{loginId}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerAddressCreateRequest))
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("customers-addresses-create",
            requestFields(
                fieldWithPath("defaultAddress").type(JsonFieldType.STRING).description("기본 주소"),
                fieldWithPath("detailAddress").type(JsonFieldType.STRING).description("상세 주소"),
                fieldWithPath("nickname").type(JsonFieldType.STRING).description("집")
            ),
            responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("주소 아이디"),
                fieldWithPath("address").type(JsonFieldType.STRING).description("주소"),
                fieldWithPath("nickname").type(JsonFieldType.STRING).description("명칭")
            )
        ));
  }

  @Test
  void readCustomerAddresses() throws Exception {
    String id = createCustomer();
    mockMvc.perform(
            get("/api/v1/customers/addresses/{loginId}", id)
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("customers-addresses-find",
            responseFields(
                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("주소 아이디"),
                fieldWithPath("[].address").type(JsonFieldType.STRING).description("주소"),
                fieldWithPath("[].nickname").type(JsonFieldType.STRING).description("명칭")
            )
        ));
  }

  @Test
  void updateCustomerAddress() throws Exception {
    String id = createCustomer();
    String body = mockMvc.perform(
            get("/api/v1/customers/addresses/{loginId}", id)
        ).andReturn().getResponse().getContentAsString();
    List<CustomerAddressFindResponse> result = objectMapper.readValue(body, new TypeReference<List<CustomerAddressFindResponse>>() {});
    Long addressId = result.get(0).getId();
    CustomerAddressUpdateRequest customerAddressUpdateRequest = new CustomerAddressUpdateRequest("서울특별시 서초구 방배동", "빌라 201호","회사");

    mockMvc.perform(
            put("/api/v1/customers/addresses/{id}", addressId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerAddressUpdateRequest))
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("customers-addresses-update",
            requestFields(
                fieldWithPath("defaultAddress").type(JsonFieldType.STRING).description("기본 주소"),
                fieldWithPath("detailAddress").type(JsonFieldType.STRING).description("상세 주소"),
                fieldWithPath("nickname").type(JsonFieldType.STRING).description("집")
            ),
            responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("주소 아이디"),
                fieldWithPath("address").type(JsonFieldType.STRING).description("주소"),
                fieldWithPath("nickname").type(JsonFieldType.STRING).description("명칭")
            )
        ));
  }

  @Test
  void deleteCustomerAddress() throws Exception {
    String id = createCustomer();
    String body = mockMvc.perform(
        get("/api/v1/customers/addresses/{loginId}", id)
    ).andReturn().getResponse().getContentAsString();
    List<CustomerAddressFindResponse> result = objectMapper.readValue(body, new TypeReference<List<CustomerAddressFindResponse>>() {});
    Long addressId = result.get(0).getId();

    mockMvc.perform(
            delete("/api/v1/customers/addresses/{id}", addressId)
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("customers-addresses-delete",
            responseBody()
        ));
  }
}