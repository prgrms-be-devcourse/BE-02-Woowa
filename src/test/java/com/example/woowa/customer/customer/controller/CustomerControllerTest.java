package com.example.woowa.customer.customer.controller;

import static org.mockito.ArgumentMatchers.any;
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
import com.example.woowa.customer.customer.dto.CustomerCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerFindResponse;
import com.example.woowa.customer.customer.dto.CustomerGradeFindResponse;
import com.example.woowa.customer.customer.dto.CustomerUpdateRequest;
import com.example.woowa.customer.customer.service.CustomerService;
import com.example.woowa.security.configuration.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@WebMvcTest(value = CustomerController.class, excludeFilters = {
    @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = SecurityConfig.class
    ),
})
@Import(RestDocsConfiguration.class)
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser
class CustomerControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private CustomerService customerService;

  @Test
  void createCustomer() throws Exception {
    CustomerGradeFindResponse customerGradeFindResponse = new CustomerGradeFindResponse(1l, 5, "??????", 1000, 2);
    CustomerFindResponse customerFindResponse = new CustomerFindResponse("dev12", "2000-01-01", 0, 0, false, customerGradeFindResponse);

    given(customerService.createCustomer(any())).willReturn(customerFindResponse);
    CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest("??????????????? ????????? ?????????", "1000-1 101???", "???");
    CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest("dev12", "Programmers12!", "2000-01-01", customerAddressCreateRequest);

    mockMvc.perform(
            post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerCreateRequest))
                .with(csrf().asHeader())
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("customers-create",
            requestFields(
                fieldWithPath("loginId").type(JsonFieldType.STRING).description("?????????"),
                fieldWithPath("loginPassword").type(JsonFieldType.STRING).description("?????? ??????"),
                fieldWithPath("birthdate").type(JsonFieldType.STRING).description("????????????"),
                fieldWithPath("address.defaultAddress").type(JsonFieldType.STRING).description("?????? ??????"),
                fieldWithPath("address.detailAddress").type(JsonFieldType.STRING).description("?????? ??????"),
                fieldWithPath("address.nickname").type(JsonFieldType.STRING).description("???")
            ),
            responseFields(
                fieldWithPath("loginId").type(JsonFieldType.STRING).description("?????????"),
                fieldWithPath("birthdate").type(JsonFieldType.STRING).description("????????????"),
                fieldWithPath("point").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                fieldWithPath("isIssued").type(JsonFieldType.BOOLEAN).description("???????????? ?????? ??????"),
                fieldWithPath("orderPerMonth").type(JsonFieldType.NUMBER).description("??? ??? ??? ????????????"),
                fieldWithPath("customerGrade.id").type(JsonFieldType.NUMBER).description("?????? ?????? ?????????"),
                fieldWithPath("customerGrade.orderCount").type(JsonFieldType.NUMBER).description("?????? ?????? ??????"),
                fieldWithPath("customerGrade.title").type(JsonFieldType.STRING).description("?????? ?????????"),
                fieldWithPath("customerGrade.discountPrice").type(JsonFieldType.NUMBER).description("?????? ??????"),
                fieldWithPath("customerGrade.voucherCount").type(JsonFieldType.NUMBER).description("?????? ??????")
            )
        ));
  }

  @Test
  void readCustomer() throws Exception {
    CustomerGradeFindResponse customerGradeFindResponse = new CustomerGradeFindResponse(1l, 5, "??????", 1000, 2);
    CustomerFindResponse customerFindResponse = new CustomerFindResponse("dev12", "2000-01-01", 0, 0, false, customerGradeFindResponse);

    given(customerService.findCustomer(any())).willReturn(customerFindResponse);

    mockMvc.perform(
            get("/api/v1/customers/{loginId}", customerFindResponse.getLoginId())
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("customers-find",
            pathParameters(
                parameterWithName("loginId").description("????????? ?????? ????????? ID")
            ),
            responseFields(
                fieldWithPath("loginId").type(JsonFieldType.STRING).description("?????????"),
                fieldWithPath("birthdate").type(JsonFieldType.STRING).description("????????????"),
                fieldWithPath("point").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                fieldWithPath("orderPerMonth").type(JsonFieldType.NUMBER).description("??? ??? ??? ????????????"),
                fieldWithPath("isIssued").type(JsonFieldType.BOOLEAN).description("???????????? ?????? ??????"),
                fieldWithPath("customerGrade.id").type(JsonFieldType.NUMBER).description("?????? ?????? ?????????"),
                fieldWithPath("customerGrade.orderCount").type(JsonFieldType.NUMBER).description("?????? ?????? ??????"),
                fieldWithPath("customerGrade.title").type(JsonFieldType.STRING).description("?????? ?????????"),
                fieldWithPath("customerGrade.discountPrice").type(JsonFieldType.NUMBER).description("?????? ??????"),
                fieldWithPath("customerGrade.voucherCount").type(JsonFieldType.NUMBER).description("?????? ??????")
            )
        ));
  }

  @Test
  void updateCustomer() throws Exception {
    CustomerGradeFindResponse customerGradeFindResponse = new CustomerGradeFindResponse(1l, 5, "??????", 1000, 2);
    CustomerFindResponse customerFindResponse = new CustomerFindResponse("dev12", "2000-01-01", 0, 0, false, customerGradeFindResponse);

    given(customerService.updateCustomer(anyString(), any())).willReturn(customerFindResponse);
    CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest("Programmers12345!");

    mockMvc.perform(
            put("/api/v1/customers/{loginId}", customerFindResponse.getLoginId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerUpdateRequest))
                .with(csrf().asHeader())
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("customers-update",
            pathParameters(
                parameterWithName("loginId").description("????????? ?????? ????????? ID")
            ),
            requestFields(
                fieldWithPath("loginPassword").type(JsonFieldType.STRING).description("????????? ????????????")
            ),
            responseFields(
                fieldWithPath("loginId").type(JsonFieldType.STRING).description("?????????"),
                fieldWithPath("birthdate").type(JsonFieldType.STRING).description("????????????"),
                fieldWithPath("point").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                fieldWithPath("orderPerMonth").type(JsonFieldType.NUMBER).description("??? ??? ??? ????????????"),
                fieldWithPath("isIssued").type(JsonFieldType.BOOLEAN).description("???????????? ?????? ??????"),
                fieldWithPath("customerGrade.id").type(JsonFieldType.NUMBER).description("?????? ?????? ?????????"),
                fieldWithPath("customerGrade.orderCount").type(JsonFieldType.NUMBER).description("?????? ?????? ??????"),
                fieldWithPath("customerGrade.title").type(JsonFieldType.STRING).description("?????? ?????????"),
                fieldWithPath("customerGrade.discountPrice").type(JsonFieldType.NUMBER).description("?????? ??????"),
                fieldWithPath("customerGrade.voucherCount").type(JsonFieldType.NUMBER).description("?????? ??????")
            )
        ));
  }

  @Test
  void updateCustomerOnFirstDay() throws Exception {
    CustomerGradeFindResponse customerGradeFindResponse = new CustomerGradeFindResponse(1l, 5, "??????", 1000, 2);
    CustomerFindResponse customerFindResponse = new CustomerFindResponse("dev12", "2000-01-01", 0, 0, false, customerGradeFindResponse);

    given(customerService.updateCustomerStatusOnFirstDay(any())).willReturn(customerFindResponse);

    mockMvc.perform(
            put("/api/v1/customers/firstday/{loginId}", customerFindResponse.getLoginId())
                .with(csrf().asHeader())
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("customers-update-firstday",
            pathParameters(
                parameterWithName("loginId").description("?????????  ?????? ????????? ID")
            ),
            responseFields(
                fieldWithPath("loginId").type(JsonFieldType.STRING).description("?????????"),
                fieldWithPath("birthdate").type(JsonFieldType.STRING).description("????????????"),
                fieldWithPath("point").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                fieldWithPath("orderPerMonth").type(JsonFieldType.NUMBER).description("??? ??? ??? ????????????"),
                fieldWithPath("isIssued").type(JsonFieldType.BOOLEAN).description("???????????? ?????? ??????"),
                fieldWithPath("customerGrade.id").type(JsonFieldType.NUMBER).description("?????? ?????? ?????????"),
                fieldWithPath("customerGrade.orderCount").type(JsonFieldType.NUMBER).description("?????? ?????? ??????"),
                fieldWithPath("customerGrade.title").type(JsonFieldType.STRING).description("?????? ?????????"),
                fieldWithPath("customerGrade.discountPrice").type(JsonFieldType.NUMBER).description("?????? ??????"),
                fieldWithPath("customerGrade.voucherCount").type(JsonFieldType.NUMBER).description("?????? ??????")
            )
        ));
  }

  @Test
  void deleteCustomer() throws Exception {
    mockMvc.perform(
            delete("/api/v1/customers/{loginId}", "dev12")
                .with(csrf().asHeader())
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("customers-delete",
            pathParameters(
                parameterWithName("loginId").description("????????? ?????? ????????? ID")
            )
        ));
  }
}