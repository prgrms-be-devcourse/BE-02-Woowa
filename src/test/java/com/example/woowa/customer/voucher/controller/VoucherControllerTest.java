package com.example.woowa.customer.voucher.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseBody;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.woowa.customer.customer.dto.CustomerAddressCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerFindResponse;
import com.example.woowa.customer.customer.dto.CustomerGradeCreateRequest;
import com.example.woowa.customer.customer.entity.CustomerGrade;
import com.example.woowa.customer.customer.repository.CustomerAddressRepository;
import com.example.woowa.customer.customer.repository.CustomerGradeRepository;
import com.example.woowa.customer.customer.repository.CustomerRepository;
import com.example.woowa.customer.customer.service.CustomerGradeService;
import com.example.woowa.customer.voucher.dto.VoucherCreateRequest;
import com.example.woowa.customer.voucher.dto.VoucherFindResponse;
import com.example.woowa.customer.voucher.enums.EventType;
import com.example.woowa.customer.voucher.enums.VoucherType;
import com.example.woowa.customer.voucher.repository.VoucherRepository;
import com.example.woowa.delivery.entity.AreaCode;
import com.example.woowa.delivery.service.AreaCodeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
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
class VoucherControllerTest {
  @Autowired
  MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private CustomerAddressRepository customerAddressRepository;

  @Autowired
  private CustomerGradeRepository customerGradeRepository;

  @Autowired
  private VoucherRepository voucherRepository;

  @MockBean
  private CustomerGradeService customerGradeService;

  @MockBean
  private AreaCodeService areaCodeService;

  @BeforeEach
  void settingBeforeTest() {
    customerRepository.deleteAll();
    customerAddressRepository.deleteAll();
    customerGradeRepository.deleteAll();
    voucherRepository.deleteAll();
    given(customerGradeService.findDefaultCustomerGrade()).willReturn(new CustomerGrade(1, "일반",3000, 2));
    given(areaCodeService.findByAddress(any())).willReturn(new AreaCode("1", "서울특별시 동작구", false));
  }

  @AfterEach
  void settingAfterTest() {
    customerRepository.deleteAll();
    customerAddressRepository.deleteAll();
    customerGradeRepository.deleteAll();
    voucherRepository.deleteAll();
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
  void registerMonthlyVoucher() throws Exception {
    String loginId = createCustomer();

    mockMvc.perform(
            get("/api/v1/vouchers/month/{loginId}", loginId)
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("vouchers-month",
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
    String loginId = createCustomer();
    VoucherCreateRequest voucherCreateRequest = new VoucherCreateRequest(VoucherType.FiXED.toString(),
        EventType.SPECIAL.toString(), 3000, "2022-12-01 12:00");
    String body = mockMvc.perform(
        post("/api/v1/vouchers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(voucherCreateRequest))
    ).andReturn().getResponse().getContentAsString();
    VoucherFindResponse voucherFindResponse = objectMapper.readValue(body, VoucherFindResponse.class);

    mockMvc.perform(
            get("/api/v1/vouchers/{loginId}/{id}", loginId, voucherFindResponse.getCode())
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("vouchers-register",
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
    VoucherCreateRequest voucherCreateRequest = new VoucherCreateRequest(VoucherType.FiXED.toString(),
        EventType.SPECIAL.toString(), 3000, "2022-12-01 12:00");

    mockMvc.perform(
            post("/api/v1/vouchers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(voucherCreateRequest))
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
    VoucherCreateRequest voucherCreateRequest = new VoucherCreateRequest(VoucherType.FiXED.toString(),
        EventType.SPECIAL.toString(), 3000, "2022-12-01 12:00");

    String body = mockMvc.perform(
            post("/api/v1/vouchers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(voucherCreateRequest))
        ).andReturn().getResponse().getContentAsString();
    VoucherFindResponse voucherFindResponse = objectMapper.readValue(body, VoucherFindResponse.class);

    mockMvc.perform(
            get("/api/v1/vouchers/{id}", voucherFindResponse.getId())
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("vouchers-find",
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
    String loginId = createCustomer();

    mockMvc.perform(
            get("/api/v1/vouchers/month/{loginId}", loginId)
        );
    mockMvc.perform(
            get("/api/v1/vouchers/user/{loginId}", loginId)
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("vouchers-find-users",
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
    String loginId = createCustomer();
    String body = mockMvc.perform(
            get("/api/v1/vouchers/month/{loginId}", loginId)
        ).andReturn().getResponse().getContentAsString();
    List<VoucherFindResponse> result = objectMapper.readValue(body, new TypeReference<List<VoucherFindResponse>>() {});
    mockMvc.perform(
            delete("/api/v1/vouchers/{loginId}/{id}", loginId,result.get(0).getId())
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("vouchers-delete",
            responseBody()
        ));
  }
}