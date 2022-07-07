package com.example.woowa.delivery.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.woowa.TestInitUtil;
import com.example.woowa.common.config.JpaAuditingConfiguration;
import com.example.woowa.delivery.dto.DeliveryResponse;
import com.example.woowa.delivery.entity.Delivery;
import com.example.woowa.delivery.mapper.DeliveryMapper;
import com.example.woowa.delivery.service.DeliveryService;
import com.example.woowa.security.configuration.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs
@WebMvcTest(value = DeliveryController.class, excludeFilters = {
    @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = SecurityConfig.class
    ),
})
@MockBean(JpaAuditingConfiguration.class)
@WithMockUser
class DeliveryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    DeliveryService deliveryService;

    DeliveryMapper deliveryMapper = Mappers.getMapper(DeliveryMapper.class);

    @Test
    void findByDelivery() throws Exception {
        Delivery delivery = TestInitUtil.initDelivery();
        DeliveryResponse deliveryResponse = deliveryMapper.toResponse(delivery);
        given(deliveryService.findResponseById(any())).willReturn(deliveryResponse);

        mockMvc.perform(get("/api/v1/delivery/{id}", 1L))
            .andExpect(status().is2xxSuccessful())
            .andDo(print())
            .andDo(document("findById-Delivery",
                    pathParameters(
                        parameterWithName("id").description("배달 아이디")
                    ),
                    responseFields(
                        fieldWithPath("id").description("배달 아이디"),
                        fieldWithPath("restaurantAddress").description("가게주소"),
                        fieldWithPath("customerAddress").description("고객주소"),
                        fieldWithPath("deliveryFee").description("배달비"),
                        fieldWithPath("deliveryStatus").description("배달상태"),
                        fieldWithPath("arrivalTime").description("도착시간")
                    )
                )
            );
    }

    @Test
    void findByWaitingDelivery() throws Exception {
        Delivery delivery = TestInitUtil.initDelivery();
        List<DeliveryResponse> deliveryResponseList = Collections.singletonList(
            deliveryMapper.toResponse(delivery));
        PageRequest pageRequest = PageRequest.of(0, 20);
        int start = (int) pageRequest.getOffset();
        int end = Math.min(start + pageRequest.getPageSize(), deliveryResponseList.size());
        PageImpl<DeliveryResponse> deliveryResponsePage = new PageImpl<>(
            deliveryResponseList.subList(start, end), pageRequest, deliveryResponseList.size());

        given(deliveryService.findWaitingDelivery(any())).willReturn(deliveryResponsePage);
        mockMvc.perform(get("/api/v1/delivery/rider")
                .param("page", String.valueOf(0))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().is2xxSuccessful())
            .andDo(print())
            .andDo(document("findByWaitingDelivery",
                    requestParameters(
                        parameterWithName("page").description("요청 페이지")
                    ),
                    responseFields(
                        fieldWithPath("content[].id").description("배달 아이디"),
                        fieldWithPath("content[].restaurantAddress").description("가게주소"),
                        fieldWithPath("content[].customerAddress").description("고객주소"),
                        fieldWithPath("content[].deliveryFee").description("배달비"),
                        fieldWithPath("content[].deliveryStatus").description("배달상태"),
                        fieldWithPath("content[].arrivalTime").description("도착시간"),
                        fieldWithPath("pageable.pageNumber").type(JsonFieldType.NUMBER)
                            .description("페이지 번호"),
                        fieldWithPath("pageable.pageSize").type(JsonFieldType.NUMBER)
                            .description("페이지 크기"),
                        fieldWithPath("pageable.offset").type(JsonFieldType.NUMBER)
                            .description("offset"),
                        fieldWithPath("pageable.paged").type(JsonFieldType.BOOLEAN)
                            .description("Pageable paging 여부"),
                        fieldWithPath("pageable.unpaged").type(JsonFieldType.BOOLEAN)
                            .description("Pageable not paging 여부"),
                        fieldWithPath("pageable.sort.sorted").type(JsonFieldType.BOOLEAN)
                            .description("페이지 정렬 여부"),
                        fieldWithPath("pageable.sort.unsorted").type(JsonFieldType.BOOLEAN)
                            .description("페이지 미정렬 여부"),
                        fieldWithPath("pageable.sort.empty").type(JsonFieldType.BOOLEAN)
                            .description("페이지 정렬 값의 여부"),
                        fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수"),
                        fieldWithPath("totalElements").type(JsonFieldType.NUMBER)
                            .description("전체 게시글 수"),
                        fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("현재 페이지가 마지막 여부"),
                        fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER)
                            .description("현재 페이지 내의 원소 개수"),
                        fieldWithPath("first").type(JsonFieldType.BOOLEAN)
                            .description("현재 페이지가 첫번째 페이지인지 여부"),
                        fieldWithPath("size").type(JsonFieldType.NUMBER).description("현재 페이지의 크기"),
                        fieldWithPath("number").type(JsonFieldType.NUMBER).description("현재 페이지의 index"),
                        fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("페이지 비어있는지 여부"),
                        fieldWithPath("sort").type(JsonFieldType.OBJECT).description("페이지 정보"),
                        fieldWithPath("sort.sorted").type(JsonFieldType.BOOLEAN)
                            .description("페이징 정렬 여부"),
                        fieldWithPath("sort.unsorted").type(JsonFieldType.BOOLEAN)
                            .description("페이징 미정렬 여부"),
                        fieldWithPath("sort.empty").type(JsonFieldType.BOOLEAN)
                            .description("페이징 속성 존재 여부")
                    )
                )
            );
    }

    @Test
    void acceptDelivery() throws Exception {
        mockMvc.perform(put("/api/v1/delivery/accept/{deliveryId}/{riderId}", 1L, 1L)
                .param("deliveryMinute", String.valueOf(0))
                .param("cookMinute", String.valueOf(0))
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().is2xxSuccessful())
            .andDo(print())
            .andDo(document("accept-delivery",
                    pathParameters(
                        parameterWithName("deliveryId").description("배달 아이디"),
                        parameterWithName("riderId").description("배달기사 아이디")
                    ),
                    requestParameters(
                        parameterWithName("deliveryMinute").description("배달 시간"),
                        parameterWithName("cookMinute").description("요리 시간")
                    )
                )
            );
    }

    @Test
    void delayDelivery() throws Exception {
        mockMvc.perform(put("/api/v1/delivery/delay/{deliveryId}/{riderId}", 1L, 1L)
                .param("delayMinute", String.valueOf(0))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf().asHeader())
            ).andExpect(status().is2xxSuccessful())
            .andDo(print())
            .andDo(document("accept-delivery",
                    pathParameters(
                        parameterWithName("deliveryId").description("배달 아이디"),
                        parameterWithName("riderId").description("배달기사 아이디")
                    ),
                    requestParameters(
                        parameterWithName("delayMinute").description("배달 시간")
                    )
                )
            );
    }

    @Test
    void pickUpDelivery() throws Exception {
        mockMvc.perform(put("/api/v1/delivery/pickup/{deliveryId}/{riderId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
            ).andExpect(status().is2xxSuccessful())
            .andDo(print())
            .andDo(document("accept-delivery",
                    pathParameters(
                        parameterWithName("deliveryId").description("배달 아이디"),
                        parameterWithName("riderId").description("배달기사 아이디")
                    )
                )
            );
    }

    @Test
    void finishDelivery() throws Exception {
        mockMvc.perform(put("/api/v1/delivery/pickup/{deliveryId}/{riderId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
            ).andExpect(status().is2xxSuccessful())
            .andDo(print())
            .andDo(document("accept-delivery",
                    pathParameters(
                        parameterWithName("deliveryId").description("배달 아이디"),
                        parameterWithName("riderId").description("배달기사 아이디")
                    )
                )
            );
    }
}