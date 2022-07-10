package com.example.woowa.restaurant.owner.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
import com.example.woowa.restaurant.owner.dto.request.OwnerCreateRequest;
import com.example.woowa.restaurant.owner.dto.request.OwnerUpdateRequest;
import com.example.woowa.restaurant.owner.dto.response.OwnerCreateResponse;
import com.example.woowa.restaurant.owner.dto.response.OwnerFindResponse;
import com.example.woowa.restaurant.owner.service.OwnerService;
import com.example.woowa.security.configuration.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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
import org.springframework.test.web.servlet.MvcResult;

@Import(value = {RestDocsConfiguration.class})
@AutoConfigureRestDocs
@WebMvcTest(controllers = OwnerRestController.class, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfig.class})
})
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser
class OwnerRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OwnerService ownerService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL_TEMPLATE = "/baemin/v1/";

    @Test
    @DisplayName("새로운 사장님 생성을 요청하면 생성된 값을 반환한다.")
    void createOwner() throws Exception {
        // Given
        OwnerCreateRequest request =
            new OwnerCreateRequest("Aabcd123456", "tT@!123456789", "테스트", "010-1234-5678");
        OwnerCreateResponse mockedResponse =
            new OwnerCreateResponse(1L, "Aabcd123456", "tT@!123456789", "테스트", "010-1234-5678", LocalDateTime.now());
        when(ownerService.createOwner(any(OwnerCreateRequest.class))).thenReturn(mockedResponse);

        // When, Then
        MvcResult mvcResult = mockMvc.perform(
                post(BASE_URL_TEMPLATE.concat("owners"))
                    .content(objectMapper.writeValueAsString(request))
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(csrf().asHeader()))
            .andExpect(status().isCreated())
            .andDo(print())
            .andDo(document("owner-create",
                requestFields(
                    fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 아이디"),
                    fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                    fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                    fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("전화번호")
                ),
                responseFields(
                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("아이디"),
                    fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 아이디"),
                    fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                    fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                    fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("전화번호"),
                    fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성일")
                )))
            .andReturn();

        ArgumentCaptor<OwnerCreateRequest> requestCaptor = ArgumentCaptor.forClass(
            OwnerCreateRequest.class);
        verify(ownerService, times(1)).createOwner(requestCaptor.capture());

        assertThat(requestCaptor.getValue().getLoginId()).isEqualTo(request.getLoginId());
        assertThat(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8))
            .isEqualTo(objectMapper.writeValueAsString(mockedResponse));
    }

    @Test
    @DisplayName("현재까지 저장된 모든 사장님을 조회한다.")
    void findAllOwners() throws Exception {
        // Given
        List<OwnerFindResponse> mockedResponse = makeOwners();
        when(ownerService.findOwners()).thenReturn(mockedResponse);

        // When, Then
        MvcResult mvcResult = mockMvc.perform(
                get(BASE_URL_TEMPLATE.concat("owners"))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("owner-findAll",
                responseFields(
                    fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("아이디"),
                    fieldWithPath("[].loginId").type(JsonFieldType.STRING).description("로그인 아이디"),
                    fieldWithPath("[].password").type(JsonFieldType.STRING).description("비밀번호"),
                    fieldWithPath("[].name").type(JsonFieldType.STRING).description("이름"),
                    fieldWithPath("[].phoneNumber").type(JsonFieldType.STRING).description("전화번호"),
                    fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("생성일"),
                    fieldWithPath("[].updatedAt").type(JsonFieldType.STRING).description("변경일")
                )))
            .andReturn();

        verify(ownerService, times(1)).findOwners();
        assertThat(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8))
            .isEqualTo(objectMapper.writeValueAsString(mockedResponse));
    }

    @Test
    @DisplayName("Url path에 대응되는 아이디를 가진 사장님 엔티티 정보를 반환한다.")
    void findOwnerById() throws Exception {
        // Given
        Long mockedId = 1L;
        OwnerFindResponse mockedResponse = new OwnerFindResponse(1L, "Aabcd123456", "tT@!123456789", "테스트1", "010-1111-2222", LocalDateTime.now(), LocalDateTime.now());
        when(ownerService.findOwnerById(anyLong())).thenReturn(mockedResponse);

        // When, Then
        MvcResult mvcResult = mockMvc.perform(
                get(BASE_URL_TEMPLATE.concat("owners/{ownerId}"), mockedId)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("owner-findById",
                pathParameters(
                    parameterWithName("ownerId").description("아이디")
                ),
                responseFields(
                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("아이디"),
                    fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 아이디"),
                    fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                    fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                    fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("전화번호"),
                    fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성일"),
                    fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("변경일")
                )))
            .andReturn();

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(ownerService, times(1)).findOwnerById(idCaptor.capture());

        assertThat(idCaptor.getValue()).isEqualTo(mockedId);
        assertThat(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8))
            .isEqualTo(objectMapper.writeValueAsString(mockedResponse));
    }

    @Test
    @DisplayName("Url path에 대응되는 아이디를 가진 사장님 엔티티를 삭제한다.")
    void deleteOwnerById() throws Exception {
        // Given
        Long mockedId = 1L;

        // When, Then
        mockMvc.perform(
                delete(BASE_URL_TEMPLATE.concat("owners/{ownerId}"), mockedId)
                    .with(csrf().asHeader()))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(document("owner-delete",
                pathParameters(
                    parameterWithName("ownerId").description("아이디")
                )));

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(ownerService, times(1)).deleteOwnerById(idCaptor.capture());

        assertThat(idCaptor.getValue()).isEqualTo(mockedId);
    }

    @Test
    @DisplayName("업데이트 요청으로 저장된 엔티티의 정보를 변경한다.")
    void updateOwnerById() throws Exception {
        // Given
        Long mockedId = 1L;
        OwnerUpdateRequest request = new OwnerUpdateRequest("tT@!123456789", "테스트", "010-1111-2222");

        // When, Then
        mockMvc.perform(
                put(BASE_URL_TEMPLATE.concat("owners/{ownerId}"), mockedId)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(request))
                    .with(csrf().asHeader()))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(document("owner-update",
                pathParameters(
                    parameterWithName("ownerId").description("아이디")
                ),
                requestFields(
                    fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                    fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                    fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("전화번호")
                )));

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<OwnerUpdateRequest> requestCaptor = ArgumentCaptor.forClass(OwnerUpdateRequest.class);
        verify(ownerService, times(1)).updateOwnerById(idCaptor.capture(), requestCaptor.capture());

        assertThat(idCaptor.getValue()).isEqualTo(mockedId);
        assertThat(objectMapper.writeValueAsString(requestCaptor.getValue()))
            .isEqualTo(objectMapper.writeValueAsString(request));
    }

    public List<OwnerFindResponse> makeOwners() {
        return List.of(
            new OwnerFindResponse(1L, "Aabcd123456", "tT@!123456789", "테테테", "010-1111-2222", LocalDateTime.now(), LocalDateTime.now()),
            new OwnerFindResponse(2L, "Aabcd123457", "tT@!123456789", "스스스", "010-3333-4444", LocalDateTime.now(), LocalDateTime.now()),
            new OwnerFindResponse(3L, "Aabcd123458", "tT@!123456789", "트트트", "010-5555-6666", LocalDateTime.now(), LocalDateTime.now())
        );
    }

}