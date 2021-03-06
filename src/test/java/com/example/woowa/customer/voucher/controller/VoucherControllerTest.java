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
    String code = UUID.randomUUID().toString();
    result.add(new VoucherFindResponse(1l, "fixed", "month", 1000, LocalDateTime.now(), code));
    result.add(new VoucherFindResponse(1l, "fixed", "month", 1000, LocalDateTime.now(), code));

    given(voucherService.registerMonthlyVoucher(anyString())).willReturn(result);

    mockMvc.perform(
            get("/api/v1/vouchers/month/{loginId}", "dev12")
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("vouchers-month",
            pathParameters(
                parameterWithName("loginId").description("?????? ????????? ????????? ?????? ????????? ID")
            ),
            responseFields(
                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                fieldWithPath("[].voucherType").type(JsonFieldType.STRING).description("?????? ??????"),
                fieldWithPath("[].eventType").type(JsonFieldType.STRING).description("????????? ??????"),
                fieldWithPath("[].discountValue").type(JsonFieldType.NUMBER).description("?????? ??????"),
                fieldWithPath("[].expirationDate").type(JsonFieldType.STRING).description("?????? ??????"),
                fieldWithPath("[].code").type(JsonFieldType.STRING).description("?????? ??????")
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
                parameterWithName("loginId").description("????????? ????????? ?????? ????????? ID"),
                parameterWithName("code").description("?????? code")
            ),
            responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                fieldWithPath("voucherType").type(JsonFieldType.STRING).description("?????? ??????"),
                fieldWithPath("eventType").type(JsonFieldType.STRING).description("????????? ??????"),
                fieldWithPath("discountValue").type(JsonFieldType.NUMBER).description("?????? ??????"),
                fieldWithPath("expirationDate").type(JsonFieldType.STRING).description("?????? ??????"),
                fieldWithPath("code").type(JsonFieldType.STRING).description("?????? ??????")
            )
        ));
  }

  @Test
  void createVoucher() throws Exception {
    VoucherFindResponse voucherFindResponse = new VoucherFindResponse(1l, "fixed", "special", 1000, LocalDateTime.of(2022, 1, 1, 12, 0), UUID.randomUUID().toString());
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
                fieldWithPath("voucherType").type(JsonFieldType.STRING).description("?????? ??????"),
                fieldWithPath("eventType").type(JsonFieldType.STRING).description("????????? ??????"),
                fieldWithPath("discountValue").type(JsonFieldType.NUMBER).description("?????? ??????"),
                fieldWithPath("expirationDate").type(JsonFieldType.STRING).description("?????? ??????")
                ),
            responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                fieldWithPath("voucherType").type(JsonFieldType.STRING).description("?????? ??????"),
                fieldWithPath("eventType").type(JsonFieldType.STRING).description("????????? ??????"),
                fieldWithPath("discountValue").type(JsonFieldType.NUMBER).description("?????? ??????"),
                fieldWithPath("expirationDate").type(JsonFieldType.STRING).description("?????? ??????"),
                fieldWithPath("code").type(JsonFieldType.STRING).description("?????? ??????")
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
                parameterWithName("id").description("????????? ?????? ID")
            ),
            responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                fieldWithPath("voucherType").type(JsonFieldType.STRING).description("?????? ??????"),
                fieldWithPath("eventType").type(JsonFieldType.STRING).description("????????? ??????"),
                fieldWithPath("discountValue").type(JsonFieldType.NUMBER).description("?????? ??????"),
                fieldWithPath("expirationDate").type(JsonFieldType.STRING).description("?????? ??????"),
                fieldWithPath("code").type(JsonFieldType.STRING).description("?????? ??????")
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
                parameterWithName("loginId").description("?????? ????????? ?????? ?????? ?????? ?????? ????????? ID")
            ),
            responseFields(
                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                fieldWithPath("[].voucherType").type(JsonFieldType.STRING).description("?????? ??????"),
                fieldWithPath("[].eventType").type(JsonFieldType.STRING).description("????????? ??????"),
                fieldWithPath("[].discountValue").type(JsonFieldType.NUMBER).description("?????? ??????"),
                fieldWithPath("[].expirationDate").type(JsonFieldType.STRING).description("?????? ??????"),
                fieldWithPath("[].code").type(JsonFieldType.STRING).description("?????? ??????")
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
                parameterWithName("loginId").description("????????? ????????? ????????? ?????? ????????? ID"),
                parameterWithName("id").description("????????? ?????? ID")
            )
        ));
  }
}