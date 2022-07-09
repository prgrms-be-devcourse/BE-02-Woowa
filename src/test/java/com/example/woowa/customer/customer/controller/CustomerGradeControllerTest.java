package com.example.woowa.customer.customer.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import com.example.woowa.customer.customer.dto.CustomerGradeCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerGradeFindResponse;
import com.example.woowa.customer.customer.dto.CustomerGradeUpdateRequest;
import com.example.woowa.customer.customer.service.CustomerGradeService;
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
@WebMvcTest(value = CustomerGradeController.class, excludeFilters = {
    @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = SecurityConfig.class
    ),
})
@Import(RestDocsConfiguration.class)
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser
class CustomerGradeControllerTest {
  @Autowired
  MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private CustomerGradeService customerGradeService;

  @Test
  void createCustomerGrade() throws Exception {
    CustomerGradeFindResponse customerGradeFindResponse = new CustomerGradeFindResponse(1l, 5, "일반", 1000, 2);

    given(customerGradeService.createCustomerGrade(any())).willReturn(customerGradeFindResponse);
    CustomerGradeCreateRequest customerGradeCreateRequest = new CustomerGradeCreateRequest(5, "일반", 1000, 2);

    mockMvc.perform(
            post("/api/v1/customers/grades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerGradeCreateRequest))
                .with(csrf().asHeader())
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
    CustomerGradeFindResponse customerGradeFindResponse = new CustomerGradeFindResponse(1l, 5, "일반", 1000, 2);

    given(customerGradeService.findCustomerGrade(anyLong())).willReturn(customerGradeFindResponse);

    mockMvc.perform(
            get("/api/v1/customers/grades/{id}", customerGradeFindResponse.getId())
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("customers-grades-find",
            pathParameters(
                parameterWithName("id").description("조회할 고객 등급 ID")
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
  void updateCustomerGrade() throws Exception {
    CustomerGradeFindResponse customerGradeFindResponse = new CustomerGradeFindResponse(1l, 10, "실버", 2000, 2);

    given(customerGradeService.updateCustomerGrade(anyLong(), any())).willReturn(customerGradeFindResponse);
    CustomerGradeUpdateRequest updateCustomerGradeDto = new CustomerGradeUpdateRequest(10, "실버", 2000, 2);

    mockMvc.perform(
            put("/api/v1/customers/grades/{id}", customerGradeFindResponse.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCustomerGradeDto))
                .with(csrf().asHeader())
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("customers-grades-update",
            pathParameters(
                parameterWithName("id").description("수정할 고객 등급 ID")
            ),
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
    mockMvc.perform(
            delete("/api/v1/customers/grades/{id}", 1)
                .with(csrf().asHeader())
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("customers-grades-delete",
            pathParameters(
                parameterWithName("id").description("삭제할 고객 등급 ID")
            )
        ));
  }
}