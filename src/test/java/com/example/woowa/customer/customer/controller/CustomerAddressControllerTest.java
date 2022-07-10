package com.example.woowa.customer.customer.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.woowa.RestDocsConfiguration;
import com.example.woowa.customer.customer.dto.CustomerAddressCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerAddressFindResponse;
import com.example.woowa.customer.customer.dto.CustomerAddressUpdateRequest;
import com.example.woowa.customer.customer.service.CustomerAddressService;
import com.example.woowa.security.configuration.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs
@WebMvcTest(value = CustomerAddressController.class, excludeFilters = {
    @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = SecurityConfig.class
    ),
})
@Import(RestDocsConfiguration.class)
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser
class CustomerAddressControllerTest {
  @Autowired
  MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private CustomerAddressService customerAddressService;

  @Test
  void createCustomerAddress() throws Exception {
    CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest("서울특별시 서초구 서초동", "1000-1 101호", "집");
    CustomerAddressFindResponse customerAddressFindResponse = new CustomerAddressFindResponse(1l, "서울특별시 서초구 서초동 1000-1 101호", "집");

    given(customerAddressService.createCustomerAddress(anyString(), any())).willReturn(customerAddressFindResponse);

    mockMvc.perform(
            post("/api/v1/customers/addresses/{loginId}", "dev12")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerAddressCreateRequest))
                .with(csrf().asHeader())
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("customers-addresses-create",
            pathParameters(
                parameterWithName("loginId").description("조회할 고객 로그인 ID")
            ),
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
    CustomerAddressFindResponse customerAddressFindResponse = new CustomerAddressFindResponse(1l, "서울특별시 서초구 서초동 1000-1 101호", "집");
    List<CustomerAddressFindResponse> result = new ArrayList<>();
    result.add(customerAddressFindResponse);
    given(customerAddressService.findCustomerAddresses(anyString())).willReturn(result);

    mockMvc.perform(
            get("/api/v1/customers/addresses/{loginId}", "dev12")
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("customers-addresses-find",
            pathParameters(
                parameterWithName("loginId").description("조회할 고객 로그인 ID")
            ),
            responseFields(
                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("주소 아이디"),
                fieldWithPath("[].address").type(JsonFieldType.STRING).description("주소"),
                fieldWithPath("[].nickname").type(JsonFieldType.STRING).description("명칭")
            )
        ));
  }

  @Test
  void updateCustomerAddress() throws Exception {
    CustomerAddressFindResponse customerAddressFindResponse = new CustomerAddressFindResponse(1l, "서울특별시 서초구 서초동 아파트 101호", "집");
    given(customerAddressService.updateCustomerAddress(anyLong(), any())).willReturn(customerAddressFindResponse);

    CustomerAddressUpdateRequest customerAddressUpdateRequest = new CustomerAddressUpdateRequest("서울특별시 서초구 서초동", "아파트 101호","집");
    mockMvc.perform(
            put("/api/v1/customers/addresses/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerAddressUpdateRequest))
                .with(csrf().asHeader())
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("customers-addresses-update",
            pathParameters(
                parameterWithName("id").description("수정할 주소 ID")
            ),
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
    mockMvc.perform(
            delete("/api/v1/customers/addresses/{loginId}/{id}", " dev12", 1)
                .with(csrf().asHeader())
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("customers-addresses-delete",
            pathParameters(
                parameterWithName("loginId").description("삭제할 주소를 가진 고객 로그인 ID"),
                parameterWithName("id").description("삭제할 주소 ID")
            )
        ));
  }
}