package com.example.woowa.customer.voucher.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.woowa.RestDocsConfiguration;
import com.example.woowa.customer.voucher.dto.VoucherCreateRequest;
import com.example.woowa.customer.voucher.dto.VoucherFindResponse;
import com.example.woowa.customer.voucher.enums.EventType;
import com.example.woowa.customer.voucher.enums.VoucherType;
import com.example.woowa.customer.voucher.service.VoucherService;
import com.example.woowa.security.configuration.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
@WebMvcTest(value = VoucherController.class, excludeFilters = {
    @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = SecurityConfig.class
    ),
})
@Import(RestDocsConfiguration.class)
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser
class VoucherControllerTest {
  @Autowired
  MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private VoucherService voucherService;

  @Test
  void registerMonthlyVoucher() throws Exception {
    List<VoucherFindResponse> result = new ArrayList<>();
    result.add(new VoucherFindResponse(1l, "fixed", "month", 1000, LocalDateTime.now(), UUID.randomUUID().toString()));
    result.add(new VoucherFindResponse(1l, "fixed", "month", 1000, LocalDateTime.now(), UUID.randomUUID().toString()));

    given(voucherService.registerMonthlyVoucher(anyString())).willReturn(result);

    mockMvc.perform(
            get("/api/v1/vouchers/month/{loginId}", "dev12")
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("vouchers-month",
            pathParameters(
                parameterWithName("loginId").description("정기 쿠폰을 발급할 고객 로그인 ID")
            ),
            responseFields(
                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("쿠폰 아이디"),
                fieldWithPath("[].voucherType").type(JsonFieldType.STRING).description("할인 타입"),
                fieldWithPath("[].eventType").type(JsonFieldType.STRING).description("이벤트 타입"),
                fieldWithPath("[].discountValue").type(JsonFieldType.NUMBER).description("할인 수치"),
                fieldWithPath("[].expirationDate").type(JsonFieldType.STRING).description("만료 시간"),
                fieldWithPath("[].code").type(JsonFieldType.STRING).description("등록 코드")
            )
        ));
  }

  @Test
  void registerVoucher() throws Exception {
    VoucherFindResponse voucherFindResponse = new VoucherFindResponse(1l, "fixed", "special", 1000, LocalDateTime.now(), UUID.randomUUID().toString());
    given(voucherService.registerVoucher(anyString(), anyString())).willReturn(voucherFindResponse);

    mockMvc.perform(
            get("/api/v1/vouchers/{loginId}/{code}", "dev12",  UUID.randomUUID().toString())
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("vouchers-register",
            pathParameters(
                parameterWithName("loginId").description("쿠폰을 등록할 고객 로그인 ID"),
                parameterWithName("code").description("쿠폰 code")
            ),
            responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("쿠폰 아이디"),
                fieldWithPath("voucherType").type(JsonFieldType.STRING).description("할인 타입"),
                fieldWithPath("eventType").type(JsonFieldType.STRING).description("이벤트 타입"),
                fieldWithPath("discountValue").type(JsonFieldType.NUMBER).description("할인 수치"),
                fieldWithPath("expirationDate").type(JsonFieldType.STRING).description("만료 시간"),
                fieldWithPath("code").type(JsonFieldType.STRING).description("등록 코드")
            )
        ));
  }

  @Test
  void createVoucher() throws Exception {
    VoucherFindResponse voucherFindResponse = new VoucherFindResponse(1l, "fixed", "special", 1000, LocalDateTime.now(), UUID.randomUUID().toString());
    VoucherCreateRequest voucherCreateRequest = new VoucherCreateRequest(VoucherType.FiXED.toString(),
        EventType.SPECIAL.toString(), 1000, "2022-01-01 12:00");

    given(voucherService.createVoucher(any())).willReturn(voucherFindResponse);

    mockMvc.perform(
            post("/api/v1/vouchers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(voucherCreateRequest))
                .with(csrf().asHeader())
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("vouchers-create",
            requestFields(
                fieldWithPath("voucherType").type(JsonFieldType.STRING).description("할인 타입"),
                fieldWithPath("eventType").type(JsonFieldType.STRING).description("이벤트 타입"),
                fieldWithPath("discountValue").type(JsonFieldType.NUMBER).description("할인 수치"),
                fieldWithPath("expirationDate").type(JsonFieldType.STRING).description("만료 시간")
                ),
            responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("쿠폰 아이디"),
                fieldWithPath("voucherType").type(JsonFieldType.STRING).description("할인 타입"),
                fieldWithPath("eventType").type(JsonFieldType.STRING).description("이벤트 타입"),
                fieldWithPath("discountValue").type(JsonFieldType.NUMBER).description("할인 수치"),
                fieldWithPath("expirationDate").type(JsonFieldType.STRING).description("만료 시간"),
                fieldWithPath("code").type(JsonFieldType.STRING).description("등록 코드")
                )
        ));
  }

  @Test
  void findVoucher() throws Exception {
    VoucherFindResponse voucherFindResponse = new VoucherFindResponse(1l, "fixed", "special", 1000, LocalDateTime.now(), UUID.randomUUID().toString());
    given(voucherService.findVoucher(any())).willReturn(voucherFindResponse);

    mockMvc.perform(
            get("/api/v1/vouchers/{id}", 1)
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("vouchers-find",
            pathParameters(
                parameterWithName("id").description("조회할 쿠폰 ID")
            ),
            responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("쿠폰 아이디"),
                fieldWithPath("voucherType").type(JsonFieldType.STRING).description("할인 타입"),
                fieldWithPath("eventType").type(JsonFieldType.STRING).description("이벤트 타입"),
                fieldWithPath("discountValue").type(JsonFieldType.NUMBER).description("할인 수치"),
                fieldWithPath("expirationDate").type(JsonFieldType.STRING).description("만료 시간"),
                fieldWithPath("code").type(JsonFieldType.STRING).description("등록 코드")
            )
        ));
  }

  @Test
  void findUserVoucher() throws Exception {
    List<VoucherFindResponse> result = new ArrayList<>();
    result.add(new VoucherFindResponse(1l, "fixed", "month", 1000, LocalDateTime.now(), UUID.randomUUID().toString()));
    result.add(new VoucherFindResponse(1l, "fixed", "month", 1000, LocalDateTime.now(), UUID.randomUUID().toString()));

    given(voucherService.findUserVoucher(anyString())).willReturn(result);

    mockMvc.perform(
            get("/api/v1/vouchers/user/{loginId}", "dev12")
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("vouchers-find-users",
            pathParameters(
                parameterWithName("loginId").description("보유 쿠폰을 조회 하고 싶은 고객 로그인 ID")
            ),
            responseFields(
                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("쿠폰 아이디"),
                fieldWithPath("[].voucherType").type(JsonFieldType.STRING).description("할인 타입"),
                fieldWithPath("[].eventType").type(JsonFieldType.STRING).description("이벤트 타입"),
                fieldWithPath("[].discountValue").type(JsonFieldType.NUMBER).description("할인 수치"),
                fieldWithPath("[].expirationDate").type(JsonFieldType.STRING).description("만료 시간"),
                fieldWithPath("[].code").type(JsonFieldType.STRING).description("등록 코드")
            )
        ));
  }

  @Test
  void deleteVoucher() throws Exception {
    mockMvc.perform(
            delete("/api/v1/vouchers/{loginId}/{id}", "dev12", 1)
                .with(csrf().asHeader())
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("vouchers-delete",
            pathParameters(
                parameterWithName("loginId").description("삭제한 쿠폰을 보유한 고객 로그인 ID"),
                parameterWithName("id").description("삭제할 쿠폰 ID")
            )
        ));
  }
}