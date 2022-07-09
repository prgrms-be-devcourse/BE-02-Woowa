package com.example.woowa.delivery.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.woowa.RestDocsConfiguration;
import com.example.woowa.TestInitUtil;
import com.example.woowa.delivery.dto.RiderCreateRequest;
import com.example.woowa.delivery.dto.RiderResponse;
import com.example.woowa.delivery.dto.RiderUpdateRequest;
import com.example.woowa.delivery.entity.Rider;
import com.example.woowa.delivery.mapper.RiderMapper;
import com.example.woowa.delivery.service.RiderService;
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
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs
@WebMvcTest(value = RiderController.class, excludeFilters = {
    @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = SecurityConfig.class
    )
})
@MockBean(JpaMetamodelMappingContext.class)
@Import(RestDocsConfiguration.class)
@WithMockUser
class RiderControllerTest {

    final String CONSTRAIN = "constraints";
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    RiderMapper riderMapper = Mappers.getMapper(RiderMapper.class);

    @MockBean
    RiderService riderService;


    @Test
    void sign() throws Exception {
        RiderCreateRequest riderCreateRequest = new RiderCreateRequest("testId1234", "passwordA1!",
            "name",
            "010-1234-5678");

        given(riderService.save(riderCreateRequest)).willReturn(1L);
        ConstraintDescriptions riderCreateRequestConstraints = new ConstraintDescriptions(
            RiderCreateRequest.class);

        mockMvc.perform(post("/api/v1/rider")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(riderCreateRequest))
                .with(csrf().asHeader())
            )
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/api/v1/rider/0"))
            .andDo(print())
            .andDo(document("sign-rider",
                    requestFields(
                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 ID")
                            .attributes(key(CONSTRAIN).value(
                                riderCreateRequestConstraints.descriptionsForProperty("loginId"))),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("패스워드")
                            .attributes(key(CONSTRAIN).value(
                                riderCreateRequestConstraints.descriptionsForProperty("password"))),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("사용자 이름")
                            .attributes(key(CONSTRAIN).value(
                                riderCreateRequestConstraints.descriptionsForProperty("name"))),
                        fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("휴대 전화")
                            .attributes(key(CONSTRAIN).value(
                                riderCreateRequestConstraints.descriptionsForProperty("phoneNumber")))
                    )
                )
            );
    }

    @Test
    void pagingRider() throws Exception {
        Rider rider = TestInitUtil.initRider();
        List<RiderResponse> riderResponseList = Collections.singletonList(
            riderMapper.toResponse(rider));
        PageRequest pageRequest = PageRequest.of(0, 20);
        int start = (int) pageRequest.getOffset();
        int end = Math.min(start + pageRequest.getPageSize(), riderResponseList.size());
        PageImpl<RiderResponse> riderResponsePage = new PageImpl<>(
            riderResponseList.subList(start, end), pageRequest, riderResponseList.size());

        given(riderService.findAll(any())).willReturn(riderResponsePage);

        mockMvc.perform(get("/api/v1/rider")
                .param("page", String.valueOf(0))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("paging-rdier",
                    requestParameters(
                        parameterWithName("page").description("요청 페이지")
                    ),
                    responseFields(
                        fieldWithPath("content[].id").description("배달기사 id"),
                        fieldWithPath("content[].createdAt").description("생성시간"),
                        fieldWithPath("content[].updatedAt").description("수정시간"),
                        fieldWithPath("content[].loginId").description("로그인Id"),
                        fieldWithPath("content[].password").description("비밀번호"),
                        fieldWithPath("content[].name").description("배달기사 이름"),
                        fieldWithPath("content[].phoneNumber").description("휴대폰 번호"),
                        fieldWithPath("content[].isDelivery").description("현재 배달 여부"),
                        fieldWithPath("content[].riderAreaList[]").description("배달가능 지역"),
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
    void getRider() throws Exception {
        Rider rider = TestInitUtil.initRider();
        RiderResponse riderResponse = riderMapper.toResponse(rider);
        given(riderService.findResponseById(any())).willReturn(riderResponse);

        mockMvc.perform(get("/api/v1/rider/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
            .andDo(print())
            .andDo(document("findById-rider",
                    pathParameters(
                        parameterWithName("id").description("라이더 id")),
                    responseFields(
                        fieldWithPath("id").description("배달기사 id"),
                        fieldWithPath("createdAt").description("생성시간"),
                        fieldWithPath("updatedAt").description("수정시간"),
                        fieldWithPath("loginId").description("로그인Id"),
                        fieldWithPath("password").description("비밀번호"),
                        fieldWithPath("name").description("배달기사 이름"),
                        fieldWithPath("phoneNumber").description("휴대폰 번호"),
                        fieldWithPath("isDelivery").description("현재 배달 여부"),
                        fieldWithPath("riderAreaList[]").description("배달가능 지역")
                    )
                )
            );
    }

    @Test
    void userUpdate() throws Exception {
        RiderUpdateRequest riderUpdateRequest = new RiderUpdateRequest("name", "010-1234-1234");

        mockMvc.perform(put("/api/v1/rider/{id}", 1L)
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(riderUpdateRequest))
            ).andExpect(status().is2xxSuccessful())
            .andDo(print())
            .andDo(document("update-rider",
                    pathParameters(
                        parameterWithName("id").description("라이더 id")),
                    requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("사용자 이름"),
                        fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("휴대 전화")
                    )
                )
            );
    }

    @Test
    void changeIsDelivery() throws Exception {
        mockMvc.perform(put("/api/v1/rider/{id}/status", 1L)
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .param("isDelivery", String.valueOf(true))
            ).andExpect(status().is2xxSuccessful())
            .andDo(print())
            .andDo(document("changeStatus-rider",
                    pathParameters(
                        parameterWithName("id").description("라이더 id")
                    ),
                    requestParameters(
                        parameterWithName("isDelivery").description("현재 배달 여부")
                    )
                )
            );
    }

    @Test
    @WithMockUser
    void addArea() throws Exception {
        mockMvc.perform(post("/api/v1/rider/{riderId}/{areaId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf().asHeader())
            ).andExpect(status().is2xxSuccessful())
            .andDo(print())
            .andDo(document("addArea-rider",
                    pathParameters(
                        parameterWithName("riderId").description("라이더 id"),
                        parameterWithName("areaId").description("지역 id")
                    )
                )
            );
    }
}