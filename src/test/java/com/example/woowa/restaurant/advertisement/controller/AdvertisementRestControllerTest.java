package com.example.woowa.restaurant.advertisement.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
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
import com.example.woowa.restaurant.advertisement.dto.request.AdvertisementCreateRequest;
import com.example.woowa.restaurant.advertisement.dto.request.AdvertisementUpdateRequest;
import com.example.woowa.restaurant.advertisement.dto.response.AdvertisementCreateResponse;
import com.example.woowa.restaurant.advertisement.dto.response.AdvertisementFindResponse;
import com.example.woowa.restaurant.advertisement.enums.RateType;
import com.example.woowa.restaurant.advertisement.enums.UnitType;
import com.example.woowa.restaurant.advertisement.service.AdvertisementService;
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
@WebMvcTest(controllers = AdvertisementRestController.class, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfig.class})
})
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser
class AdvertisementRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdvertisementService advertisementService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL_TEMPLATE = "/baemin/v1/";

    @Test
    @DisplayName("새로운 광고 생성을 요청하면 생성된 값을 반환한다.")
    void createAdvertisement() throws Exception {
        // Given
        AdvertisementCreateRequest request = new AdvertisementCreateRequest("울트라콜",
            UnitType.PER_ORDER.getType(),
            RateType.FLAT.getType(), 10, "테스트용 울트라콜", 10);
        AdvertisementCreateResponse mockedResponse = new AdvertisementCreateResponse(1L, "울트라콜",
            UnitType.PER_ORDER.getType(),
            RateType.FLAT.getType(), 10, "테스트용 울트라콜", 10, 0, LocalDateTime.now());
        when(advertisementService.createAdvertisement(
            any(AdvertisementCreateRequest.class))).thenReturn(mockedResponse);

        // When, Then
        MvcResult mvcResult = mockMvc.perform(
                post(BASE_URL_TEMPLATE.concat("advertisements"))
                    .content(objectMapper.writeValueAsString(request))
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(csrf().asHeader()))
            .andExpect(status().isCreated())
            .andDo(print())
            .andDo(document("advertisement-create",
                requestFields(
                    fieldWithPath("title").type(JsonFieldType.STRING).description("광고명"),
                    fieldWithPath("unitType").type(JsonFieldType.STRING).description("적용단위"),
                    fieldWithPath("rateType").type(JsonFieldType.STRING).description("청구유형"),
                    fieldWithPath("rate").type(JsonFieldType.NUMBER).description("광고료"),
                    fieldWithPath("description").type(JsonFieldType.STRING).description("설명"),
                    fieldWithPath("limitSize").type(JsonFieldType.NUMBER).description("최대 가게 수")
                ),
                responseFields(
                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("아이디"),
                    fieldWithPath("title").type(JsonFieldType.STRING).description("광고명"),
                    fieldWithPath("unitType").type(JsonFieldType.STRING).description("적용단위"),
                    fieldWithPath("rateType").type(JsonFieldType.STRING).description("청구유형"),
                    fieldWithPath("rate").type(JsonFieldType.NUMBER).description("광고료"),
                    fieldWithPath("description").type(JsonFieldType.STRING).description("설명"),
                    fieldWithPath("limitSize").type(JsonFieldType.NUMBER).description("최대 가게 수"),
                    fieldWithPath("currentSize").type(JsonFieldType.NUMBER).description("현재 가게 수"),
                    fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성일")
                )))
            .andReturn();

        ArgumentCaptor<AdvertisementCreateRequest> requestCaptor = ArgumentCaptor.forClass(
            AdvertisementCreateRequest.class);
        verify(advertisementService, times(1)).createAdvertisement(requestCaptor.capture());

        assertThat(requestCaptor.getValue().getTitle()).isEqualTo(request.getTitle());
        assertThat(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8))
            .isEqualTo(objectMapper.writeValueAsString(mockedResponse));
    }

    @Test
    @DisplayName("현재까지 저장된 모든 광고를 조회한다.")
    void findAllAdvertisements() throws Exception {
        // Given
        List<AdvertisementFindResponse> mockedResponse = makeAdvertisementFindResponses();
        when(advertisementService.findAdvertisements()).thenReturn(mockedResponse);

        // When, Then
        MvcResult mvcResult = mockMvc.perform(
                get(BASE_URL_TEMPLATE.concat("advertisements"))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("advertisement-findAll",
                responseFields(
                    fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("아이디"),
                    fieldWithPath("[].title").type(JsonFieldType.STRING).description("광고명"),
                    fieldWithPath("[].unitType").type(JsonFieldType.STRING).description("적용단위"),
                    fieldWithPath("[].rateType").type(JsonFieldType.STRING).description("청구유형"),
                    fieldWithPath("[].rate").type(JsonFieldType.NUMBER).description("광고료"),
                    fieldWithPath("[].description").type(JsonFieldType.STRING).description("설명"),
                    fieldWithPath("[].limitSize").type(JsonFieldType.NUMBER).description("최대 가게 수"),
                    fieldWithPath("[].currentSize").type(JsonFieldType.NUMBER).description("현재 가게 수"),
                    fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("생성일"),
                    fieldWithPath("[].updatedAt").type(JsonFieldType.STRING).description("변경일")
                )))
            .andReturn();

        verify(advertisementService, times(1)).findAdvertisements();
        assertThat(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8))
            .isEqualTo(objectMapper.writeValueAsString(mockedResponse));
    }

    @Test
    @DisplayName("Url path에 대응되는 아이디를 가진 광고 엔티티 정보를 반환한다.")
    void findAdvertisementById() throws Exception {
        // Given
        Long mockedId = 1L;
        AdvertisementFindResponse mockedResponse = new AdvertisementFindResponse(1L, "울트라콜",
            UnitType.PER_ORDER.getType(), RateType.FLAT.getType(), 10, "테스트용 울트라콜", 10, 0, LocalDateTime.now(), LocalDateTime.now());
        when(advertisementService.findAdvertisementById(anyLong())).thenReturn(mockedResponse);

        // When, Then
        MvcResult mvcResult = mockMvc.perform(
                get(BASE_URL_TEMPLATE.concat("advertisements/{advertisementId}"), mockedId)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("advertisement-findById",
                pathParameters(
                    parameterWithName("advertisementId").description("아이디")
                ),
                responseFields(
                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("아이디"),
                    fieldWithPath("title").type(JsonFieldType.STRING).description("광고명"),
                    fieldWithPath("unitType").type(JsonFieldType.STRING).description("적용단위"),
                    fieldWithPath("rateType").type(JsonFieldType.STRING).description("청구유형"),
                    fieldWithPath("rate").type(JsonFieldType.NUMBER).description("광고료"),
                    fieldWithPath("description").type(JsonFieldType.STRING).description("설명"),
                    fieldWithPath("limitSize").type(JsonFieldType.NUMBER).description("최대 가게 수"),
                    fieldWithPath("currentSize").type(JsonFieldType.NUMBER).description("현재 가게 수"),
                    fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성일"),
                    fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("변경일")
                )))
            .andReturn();

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(advertisementService, times(1)).findAdvertisementById(idCaptor.capture());

        assertThat(idCaptor.getValue()).isEqualTo(mockedId);
        assertThat(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8))
            .isEqualTo(objectMapper.writeValueAsString(mockedResponse));
    }

    @Test
    @DisplayName("업데이트 요청으로 저장된 엔티티의 정보를 변경한다.")
    void updateAdvertisementById() throws Exception {
        // Given
        Long mockedId = 1L;
        AdvertisementUpdateRequest request = new AdvertisementUpdateRequest("울트라콜",
            UnitType.PER_ORDER.getType(), RateType.FLAT.getType(), 10, "테스트용 울트라콜");

        // When, Then
        mockMvc.perform(
                put(BASE_URL_TEMPLATE.concat("advertisements/{advertisementId}"), mockedId)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(request))
                    .with(csrf().asHeader()))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(document("advertisement-update",
                pathParameters(
                    parameterWithName("advertisementId").description("아이디")
                ),
                requestFields(
                    fieldWithPath("title").type(JsonFieldType.STRING).description("광고명"),
                    fieldWithPath("unitType").type(JsonFieldType.STRING).description("적용단위"),
                    fieldWithPath("rateType").type(JsonFieldType.STRING).description("청구유형"),
                    fieldWithPath("rate").type(JsonFieldType.NUMBER).description("광고료"),
                    fieldWithPath("description").type(JsonFieldType.STRING).description("설명")
                )));

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<AdvertisementUpdateRequest> requestCaptor = ArgumentCaptor.forClass(AdvertisementUpdateRequest.class);
        verify(advertisementService, times(1)).updateAdvertisementById(idCaptor.capture(), requestCaptor.capture());

        assertThat(idCaptor.getValue()).isEqualTo(mockedId);
        assertThat(objectMapper.writeValueAsString(requestCaptor.getValue()))
            .isEqualTo(objectMapper.writeValueAsString(request));
    }

    @Test
    @DisplayName("Url path에 대응되는 아이디를 가진 광고 엔티티를 삭제한다.")
    void deleteAdvertisementById() throws Exception {
        // Given
        Long mockedId = 1L;

        // When, Then
        mockMvc.perform(
                delete(BASE_URL_TEMPLATE.concat("advertisements/{advertisementId}"), mockedId)
                    .with(csrf().asHeader()))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(document("advertisement-delete",
                pathParameters(
                    parameterWithName("advertisementId").description("아이디")
                )));

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(advertisementService, times(1)).deleteAdvertisementById(idCaptor.capture());

        assertThat(idCaptor.getValue()).isEqualTo(mockedId);
    }

    @Test
    @DisplayName("Url path를 통해 광고에 가게를 포함한다.")
    void includeRestaurantInAdvertisement() throws Exception {
        // Given
        Long mockedAdvertisementId = 10L;
        Long mockedRestaurantId = 20L;

        // When, Then
        mockMvc.perform(
                post(BASE_URL_TEMPLATE.concat("advertisements/{advertisementId}/restaurants/{restaurantId}"), mockedAdvertisementId, mockedRestaurantId)
                    .with(csrf().asHeader()))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(document("advertisement-include-restaurant",
                pathParameters(
                    parameterWithName("advertisementId").description("광고 아이디"),
                    parameterWithName("restaurantId").description("가게 아이디")
                )));

        ArgumentCaptor<Long> advertisementIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> restaurantIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(advertisementService, times(1)).includeRestaurantInAdvertisement(advertisementIdCaptor.capture(), restaurantIdCaptor.capture());

        assertThat(advertisementIdCaptor.getValue()).isEqualTo(mockedAdvertisementId);
        assertThat(restaurantIdCaptor.getValue()).isEqualTo(mockedRestaurantId);
    }

    @Test
    @DisplayName("Url path를 통해 광고에서 가게를 제외한다.")
    void excludeRestaurantOutOfAdvertisement() throws Exception {
        // Given
        Long mockedAdvertisementId = 10L;
        Long mockedRestaurantId = 20L;

        // When, Then
        mockMvc.perform(
                delete(BASE_URL_TEMPLATE.concat("advertisements/{advertisementId}/restaurants/{restaurantId}"), mockedAdvertisementId, mockedRestaurantId)
                    .with(csrf().asHeader()))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(document("advertisement-exclude-restaurant",
                pathParameters(
                    parameterWithName("advertisementId").description("광고 아이디"),
                    parameterWithName("restaurantId").description("가게 아이디")
                )));

        ArgumentCaptor<Long> advertisementIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> restaurantIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(advertisementService, times(1)).excludeRestaurantOutOfAdvertisement(advertisementIdCaptor.capture(), restaurantIdCaptor.capture());

        assertThat(advertisementIdCaptor.getValue()).isEqualTo(mockedAdvertisementId);
        assertThat(restaurantIdCaptor.getValue()).isEqualTo(mockedRestaurantId);
    }

    public List<AdvertisementFindResponse> makeAdvertisementFindResponses() {
        return List.of(
            new AdvertisementFindResponse(1L, "A", UnitType.PER_ORDER.getType(), RateType.PERCENT.getType(), 10, "a", 10, 1, LocalDateTime.now(), LocalDateTime.now()),
            new AdvertisementFindResponse(2L, "B", UnitType.PER_ORDER.getType(), RateType.PERCENT.getType(), 10, "b", 10, 2, LocalDateTime.now(), LocalDateTime.now()),
            new AdvertisementFindResponse(3L, "C", UnitType.PER_ORDER.getType(), RateType.PERCENT.getType(), 10, "c", 10, 3, LocalDateTime.now(), LocalDateTime.now())
        );
    }

}