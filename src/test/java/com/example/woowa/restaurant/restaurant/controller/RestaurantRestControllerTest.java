package com.example.woowa.restaurant.restaurant.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.woowa.RestDocsConfiguration;
import com.example.woowa.restaurant.restaurant.dto.request.RestaurantCreateRequest;
import com.example.woowa.restaurant.restaurant.dto.request.RestaurantUpdateRequest;
import com.example.woowa.restaurant.restaurant.dto.response.RestaurantCreateResponse;
import com.example.woowa.restaurant.restaurant.dto.response.RestaurantFindResponse;
import com.example.woowa.restaurant.restaurant.service.RestaurantService;
import com.example.woowa.security.configuration.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
@WebMvcTest(controllers = RestaurantRestController.class, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfig.class})
})
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser
class RestaurantRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL_TEMPLATE = "/baemin/v1/";

    @Test
    @DisplayName("사장님 아이디와 함께 새로운 가게 생성을 요청하면 생성된 값을 반환한다.")
    void createRestaurantByOwnerId() throws Exception {
        // Given
        Long mockedOwnerId = 1L;
        Long mockedRestaurantId = 1L;
        RestaurantCreateRequest request =
            new RestaurantCreateRequest("테스트가게", "760-15-00993", LocalTime.now(), LocalTime.now(), false, "010-1234-6789", "테스트용 가게입니다.", "서울시 동작구", java.util.List.of(1L, 2L));
        RestaurantCreateResponse mockedResponse =
            new RestaurantCreateResponse(mockedOwnerId, mockedRestaurantId, "테스트가게", "760-15-00993", LocalTime.now(), LocalTime.now(), false, "010-1234-6789", "테스트용 가게입니다.", "서울시 동작구", java.util.List.of("한식", "중식"), LocalDateTime.now());
        when(restaurantService.createRestaurantByOwnerId(anyLong(), any(RestaurantCreateRequest.class))).thenReturn(mockedResponse);

        // When, Then
        MvcResult mvcResult = mockMvc.perform(
                post(BASE_URL_TEMPLATE.concat("owners/{ownerId}/restaurants"), mockedOwnerId)
                    .content(objectMapper.writeValueAsString(request))
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(csrf().asHeader()))
            .andExpect(status().isCreated())
            .andDo(print())
            .andDo(document("restaurant-create",
                pathParameters(
                    parameterWithName("ownerId").description("사장님 아이디")
                ),
                requestFields(
                    fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                    fieldWithPath("businessNumber").type(JsonFieldType.STRING).description("사업자등록번호"),
                    fieldWithPath("openingTime").type(JsonFieldType.STRING).description("문여는 시간"),
                    fieldWithPath("closingTime").type(JsonFieldType.STRING).description("문닫는 시간"),
                    fieldWithPath("isOpen").type(JsonFieldType.BOOLEAN).description("영업여부"),
                    fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("전화번호"),
                    fieldWithPath("description").type(JsonFieldType.STRING).description("소개"),
                    fieldWithPath("address").type(JsonFieldType.STRING).description("주소"),
                    fieldWithPath("categoryIds").type(JsonFieldType.ARRAY).description("속하는 카테고리명")
                ),
                responseFields(
                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("가게 아이디"),
                    fieldWithPath("ownerId").type(JsonFieldType.NUMBER).description("사장님 아이디"),
                    fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                    fieldWithPath("businessNumber").type(JsonFieldType.STRING).description("사업자등록번호"),
                    fieldWithPath("openingTime").type(JsonFieldType.STRING).description("문여는 시간"),
                    fieldWithPath("closingTime").type(JsonFieldType.STRING).description("문닫는 시간"),
                    fieldWithPath("isOpen").type(JsonFieldType.BOOLEAN).description("영업여부"),
                    fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("전화번호"),
                    fieldWithPath("description").type(JsonFieldType.STRING).description("소개"),
                    fieldWithPath("address").type(JsonFieldType.STRING).description("주소"),
                    fieldWithPath("categories.[]").type(JsonFieldType.ARRAY).description("속하는 카테고리명"),
                    fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성일")
                )))
            .andReturn();

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(
            Long.class);
        ArgumentCaptor<RestaurantCreateRequest> requestCaptor = ArgumentCaptor.forClass(
            RestaurantCreateRequest.class);
        verify(restaurantService, times(1)).createRestaurantByOwnerId(idCaptor.capture(), requestCaptor.capture());

        assertThat(idCaptor.getValue()).isEqualTo(mockedOwnerId);
        assertThat(requestCaptor.getValue().getBusinessNumber()).isEqualTo(request.getBusinessNumber());
        assertThat(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8))
            .isEqualTo(objectMapper.writeValueAsString(mockedResponse));
    }

    @Test
    @DisplayName("사장님 아이디를 통해 현재까지 저장된 모든 가게를 조회한다.")
    void findAllRestaurantsByOwnerId() throws Exception {
        // Given
        Long mockedOwnerId = 1L;
        List<RestaurantFindResponse> mockedResponse = makeRestaurantFindResponses();
        when(restaurantService.findRestaurantsByOwnerId(mockedOwnerId)).thenReturn(mockedResponse);

        // When, Then
        MvcResult mvcResult = mockMvc.perform(
                get(BASE_URL_TEMPLATE.concat("owners/{ownerId}/restaurants"), mockedOwnerId)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("restaurant-findAllByOwnerId",
                pathParameters(
                    parameterWithName("ownerId").description("사장님 아이디")
                ),
                responseFields(
                    fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("가게 아이디"),
                    fieldWithPath("[].ownerId").type(JsonFieldType.NUMBER).description("사장님 아이디"),
                    fieldWithPath("[].name").type(JsonFieldType.STRING).description("이름"),
                    fieldWithPath("[].businessNumber").type(JsonFieldType.STRING).description("사업자등록번호"),
                    fieldWithPath("[].openingTime").type(JsonFieldType.STRING).description("문여는 시간"),
                    fieldWithPath("[].closingTime").type(JsonFieldType.STRING).description("문닫는 시간"),
                    fieldWithPath("[].isOpen").type(JsonFieldType.BOOLEAN).description("영업여부"),
                    fieldWithPath("[].phoneNumber").type(JsonFieldType.STRING).description("전화번호"),
                    fieldWithPath("[].description").type(JsonFieldType.STRING).description("소개"),
                    fieldWithPath("[].averageReviewScore").type(JsonFieldType.NUMBER).description("평균 리뷰수"),
                    fieldWithPath("[].reviewCount").type(JsonFieldType.NUMBER).description("리뷰수"),
                    fieldWithPath("[].address").type(JsonFieldType.STRING).description("주소"),
                    fieldWithPath("[].categories.[]").type(JsonFieldType.ARRAY).description("속하는 카테고리명"),
                    fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("생성일"),
                    fieldWithPath("[].updatedAt").type(JsonFieldType.STRING).description("변경일")

                )))
            .andReturn();

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);

        verify(restaurantService, times(1)).findRestaurantsByOwnerId(idCaptor.capture());
        assertThat(idCaptor.getValue()).isEqualTo(mockedOwnerId);
        assertThat(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8))
            .isEqualTo(objectMapper.writeValueAsString(mockedResponse));
    }

    @Test
    @DisplayName("사장님, 가게 아이디를 통해 가게 정보를 변경한다.")
    void updateRestaurantByOwnerIdAndRestaurantId() throws Exception {
        // Given
        Long mockedOwnerId = 1L;
        Long mockedRestaurantId = 1L;
        RestaurantUpdateRequest request =
            new RestaurantUpdateRequest(LocalTime.now(), LocalTime.now(), "010-1234-6789", "서울시 동작구", "테스트용 가게입니다." );

        // When, Then
        mockMvc.perform(
                put(BASE_URL_TEMPLATE.concat("owners/{ownerId}/restaurants/{restaurantId}"), mockedOwnerId, mockedRestaurantId)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(request))
                    .with(csrf().asHeader()))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(document("restaurant-update",
                pathParameters(
                    parameterWithName("ownerId").description("사장님 아이디"),
                    parameterWithName("restaurantId").description("가게 아이디")
                ),
                requestFields(
                    fieldWithPath("openingTime").type(JsonFieldType.STRING).description("문여는 시간"),
                    fieldWithPath("closingTime").type(JsonFieldType.STRING).description("문닫는 시간"),
                    fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("전화번호"),
                    fieldWithPath("address").type(JsonFieldType.STRING).description("주소"),
                    fieldWithPath("description").type(JsonFieldType.STRING).description("소개")
                )));

        ArgumentCaptor<Long> ownerIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> restaurantIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<RestaurantUpdateRequest> requestCaptor = ArgumentCaptor.forClass(RestaurantUpdateRequest.class);
        verify(restaurantService, times(1)).updateRestaurantByOwnerIdAndRestaurantId(ownerIdCaptor.capture(), restaurantIdCaptor.capture(), requestCaptor.capture());

        assertThat(ownerIdCaptor.getValue()).isEqualTo(mockedOwnerId);
        assertThat(restaurantIdCaptor.getValue()).isEqualTo(mockedRestaurantId);
        assertThat(objectMapper.writeValueAsString(requestCaptor.getValue()))
            .isEqualTo(objectMapper.writeValueAsString(request));
    }

    @Test
    @DisplayName("사장님, 가게 아이디를 통해 가게 엔티티를 삭제한다.")
    void deleteRestaurantByOwnerIdAndRestaurantId() throws Exception {
        // Given
        Long mockedOwnerId = 1L;
        Long mockedRestaurantId = 1L;

        // When, Then
        mockMvc.perform(
                delete(BASE_URL_TEMPLATE.concat("owners/{ownerId}/restaurants/{restaurantId}"), mockedOwnerId, mockedRestaurantId)
                    .with(csrf().asHeader()))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(document("restaurant-delete",
                pathParameters(
                    parameterWithName("ownerId").description("사장님 아이디"),
                    parameterWithName("restaurantId").description("가게 아이디")
                )));

        ArgumentCaptor<Long> ownerIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> restaurantIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(restaurantService, times(1)).deleteRestaurantByOwnerIdAndRestaurantId(ownerIdCaptor.capture(), restaurantIdCaptor.capture());

        assertThat(ownerIdCaptor.getValue()).isEqualTo(mockedOwnerId);
        assertThat(restaurantIdCaptor.getValue()).isEqualTo(mockedRestaurantId);
    }

    @Test
    @DisplayName("광고 아이디를 통해 현재까지 저장된 모든 가게를 조회한다.")
    void findAllRestaurantsByAdvertisementId() throws Exception {
        // Given
        Long mockedAdvertisementId = 1L;
        List<RestaurantFindResponse> mockedResponse = makeRestaurantFindResponses();
        when(restaurantService.findRestaurantsByAdvertisementId(mockedAdvertisementId)).thenReturn(mockedResponse);

        // When, Then
        MvcResult mvcResult = mockMvc.perform(
                get(BASE_URL_TEMPLATE.concat("advertisements/{advertisementId}/restaurants"), mockedAdvertisementId)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("restaurant-findAllByAdvertisementId",
                pathParameters(
                    parameterWithName("advertisementId").description("광고 아이디")
                ),
                responseFields(
                    fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("가게 아이디"),
                    fieldWithPath("[].ownerId").type(JsonFieldType.NUMBER).description("사장님 아이디"),
                    fieldWithPath("[].name").type(JsonFieldType.STRING).description("이름"),
                    fieldWithPath("[].businessNumber").type(JsonFieldType.STRING).description("사업자등록번호"),
                    fieldWithPath("[].openingTime").type(JsonFieldType.STRING).description("문여는 시간"),
                    fieldWithPath("[].closingTime").type(JsonFieldType.STRING).description("문닫는 시간"),
                    fieldWithPath("[].isOpen").type(JsonFieldType.BOOLEAN).description("영업여부"),
                    fieldWithPath("[].phoneNumber").type(JsonFieldType.STRING).description("전화번호"),
                    fieldWithPath("[].description").type(JsonFieldType.STRING).description("소개"),
                    fieldWithPath("[].averageReviewScore").type(JsonFieldType.NUMBER).description("평균 리뷰수"),
                    fieldWithPath("[].reviewCount").type(JsonFieldType.NUMBER).description("리뷰수"),
                    fieldWithPath("[].address").type(JsonFieldType.STRING).description("주소"),
                    fieldWithPath("[].categories.[]").type(JsonFieldType.ARRAY).description("속하는 카테고리명"),
                    fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("생성일"),
                    fieldWithPath("[].updatedAt").type(JsonFieldType.STRING).description("변경일")

                )))
            .andReturn();

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(restaurantService, times(1)).findRestaurantsByAdvertisementId(idCaptor.capture());

        assertThat(idCaptor.getValue()).isEqualTo(mockedAdvertisementId);
        assertThat(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8))
            .isEqualTo(objectMapper.writeValueAsString(mockedResponse));
    }

    @Test
    @DisplayName("카테고리 아이디를 통해 현재까지 저장된 모든 가게를 조회한다.")
    void findAllRestaurantsByCategoryId() throws Exception {
        // Given
        Long mockedCategoryId = 1L;
        List<RestaurantFindResponse> mockedResponse = makeRestaurantFindResponses();
        when(restaurantService.findRestaurantsByCategoryId(mockedCategoryId)).thenReturn(mockedResponse);

        // When, Then
        MvcResult mvcResult = mockMvc.perform(
                get(BASE_URL_TEMPLATE.concat("categories/{categoryId}/restaurants"),
                    mockedCategoryId)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("restaurant-findAllByCategoryId",
                pathParameters(
                    parameterWithName("categoryId").description("카테고리 아이디")
                ),
                responseFields(
                    fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("가게 아이디"),
                    fieldWithPath("[].ownerId").type(JsonFieldType.NUMBER).description("사장님 아이디"),
                    fieldWithPath("[].name").type(JsonFieldType.STRING).description("이름"),
                    fieldWithPath("[].businessNumber").type(JsonFieldType.STRING).description("사업자등록번호"),
                    fieldWithPath("[].openingTime").type(JsonFieldType.STRING).description("문여는 시간"),
                    fieldWithPath("[].closingTime").type(JsonFieldType.STRING).description("문닫는 시간"),
                    fieldWithPath("[].isOpen").type(JsonFieldType.BOOLEAN).description("영업여부"),
                    fieldWithPath("[].phoneNumber").type(JsonFieldType.STRING).description("전화번호"),
                    fieldWithPath("[].description").type(JsonFieldType.STRING).description("소개"),
                    fieldWithPath("[].averageReviewScore").type(JsonFieldType.NUMBER).description("평균 리뷰수"),
                    fieldWithPath("[].reviewCount").type(JsonFieldType.NUMBER).description("리뷰수"),
                    fieldWithPath("[].address").type(JsonFieldType.STRING).description("주소"),
                    fieldWithPath("[].categories.[]").type(JsonFieldType.ARRAY).description("속하는 카테고리명"),
                    fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("생성일"),
                    fieldWithPath("[].updatedAt").type(JsonFieldType.STRING).description("변경일")

                )))
            .andReturn();

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(restaurantService, times(1)).findRestaurantsByCategoryId(idCaptor.capture());

        assertThat(idCaptor.getValue()).isEqualTo(mockedCategoryId);
        assertThat(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8))
            .isEqualTo(objectMapper.writeValueAsString(mockedResponse));
    }

    @Test
    @DisplayName("현재까지 저장된 모든 가게를 조회한다(지역 코드가 주어질 경우 해당 지역 코드를 가진 가게만을 조회한다).")
    void findAllRestaurants() throws Exception {
        // Given
        Long areaCodeIdParam = 1L;
        List<RestaurantFindResponse> mockedResponse = makeRestaurantFindResponses();
        when(restaurantService.findRestaurantByAreaCode(areaCodeIdParam)).thenReturn(mockedResponse);

        // When, Then
        MvcResult mvcResult = mockMvc.perform(
                get(BASE_URL_TEMPLATE.concat("restaurants")).param("areaCodeId", String.valueOf(
                        areaCodeIdParam))
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("restaurant-findAll",
                requestParameters(
                    parameterWithName("areaCodeId").optional().description("지역코드")
                ),
                responseFields(
                    fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("가게 아이디"),
                    fieldWithPath("[].ownerId").type(JsonFieldType.NUMBER).description("사장님 아이디"),
                    fieldWithPath("[].name").type(JsonFieldType.STRING).description("이름"),
                    fieldWithPath("[].businessNumber").type(JsonFieldType.STRING).description("사업자등록번호"),
                    fieldWithPath("[].openingTime").type(JsonFieldType.STRING).description("문여는 시간"),
                    fieldWithPath("[].closingTime").type(JsonFieldType.STRING).description("문닫는 시간"),
                    fieldWithPath("[].isOpen").type(JsonFieldType.BOOLEAN).description("영업여부"),
                    fieldWithPath("[].phoneNumber").type(JsonFieldType.STRING).description("전화번호"),
                    fieldWithPath("[].description").type(JsonFieldType.STRING).description("소개"),
                    fieldWithPath("[].averageReviewScore").type(JsonFieldType.NUMBER).description("평균 리뷰수"),
                    fieldWithPath("[].reviewCount").type(JsonFieldType.NUMBER).description("리뷰수"),
                    fieldWithPath("[].address").type(JsonFieldType.STRING).description("주소"),
                    fieldWithPath("[].categories.[]").type(JsonFieldType.ARRAY).description("속하는 카테고리명"),
                    fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("생성일"),
                    fieldWithPath("[].updatedAt").type(JsonFieldType.STRING).description("변경일")

                )))
            .andReturn();

        ArgumentCaptor<Long> areaCodeIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(restaurantService, times(1)).findRestaurantByAreaCode(areaCodeIdCaptor.capture());
        verify(restaurantService, never()).findRestaurants();

        assertThat(areaCodeIdCaptor.getValue()).isEqualTo(areaCodeIdParam);
        assertThat(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8))
            .isEqualTo(objectMapper.writeValueAsString(mockedResponse));
    }

    @Test
    @DisplayName("Url path에 대응되는 아이디를 가진 가게 엔티티 정보를 반환한다.")
    void findRestaurantById() throws Exception {
        // Given
        Long mockedRestaurantId = 1L;
        RestaurantFindResponse mockedResponse = new RestaurantFindResponse(1L, 1L, "테스트가게1", "760-15-00991", LocalTime.now(), LocalTime.now(), false, "010-1234-6789", "테스트용 가게입니다.",0.0, 0, "서울시 동작구", java.util.List.of("한식", "중식"), LocalDateTime.now(), LocalDateTime.now());
        when(restaurantService.findRestaurantById(mockedRestaurantId)).thenReturn(mockedResponse);

        // When, Then
        MvcResult mvcResult = mockMvc.perform(
                get(BASE_URL_TEMPLATE.concat("restaurants/{restaurantId}"),
                    mockedRestaurantId)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("restaurant-findById",
                pathParameters(
                    parameterWithName("restaurantId").description("가게 아이디")
                ),
                responseFields(
                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("가게 아이디"),
                    fieldWithPath("ownerId").type(JsonFieldType.NUMBER).description("사장님 아이디"),
                    fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                    fieldWithPath("businessNumber").type(JsonFieldType.STRING).description("사업자등록번호"),
                    fieldWithPath("openingTime").type(JsonFieldType.STRING).description("문여는 시간"),
                    fieldWithPath("closingTime").type(JsonFieldType.STRING).description("문닫는 시간"),
                    fieldWithPath("isOpen").type(JsonFieldType.BOOLEAN).description("영업여부"),
                    fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("전화번호"),
                    fieldWithPath("description").type(JsonFieldType.STRING).description("소개"),
                    fieldWithPath("averageReviewScore").type(JsonFieldType.NUMBER).description("평균 리뷰수"),
                    fieldWithPath("reviewCount").type(JsonFieldType.NUMBER).description("리뷰수"),
                    fieldWithPath("address").type(JsonFieldType.STRING).description("주소"),
                    fieldWithPath("categories.[]").type(JsonFieldType.ARRAY).description("속하는 카테고리명"),
                    fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성일"),
                    fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("변경일")

                )))
            .andReturn();

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(restaurantService, times(1)).findRestaurantById(idCaptor.capture());

        assertThat(idCaptor.getValue()).isEqualTo(mockedRestaurantId);
        assertThat(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8))
            .isEqualTo(objectMapper.writeValueAsString(mockedResponse));
    }

    @Test
    @DisplayName("주어지는 boolean 요청 파라미터에 따라 가게의 영업 상태를 변경한다.")
    void changeRestaurantState() throws Exception {
        // Given
        Long mockedOwnerId = 1L;
        Long mockedRestaurantId = 1L;
        Boolean isOpenParam = true;

        // When, Then
        mockMvc.perform(
                patch(BASE_URL_TEMPLATE.concat("owners/{ownerId}/restaurants/{restaurantId}"),
                    mockedOwnerId, mockedRestaurantId)
                    .param("isOpen", String.valueOf(isOpenParam))
                    .with(csrf().asHeader()))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(document("restaurant-open/close",
                pathParameters(
                    parameterWithName("ownerId").description("사장님 아이디"),
                    parameterWithName("restaurantId").description("가게 아이디")
                ),
                requestParameters(
                    parameterWithName("isOpen").description("영업여부")
                )));

        ArgumentCaptor<Long> ownerIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> restaurantIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(restaurantService, times(1)).openRestaurant(ownerIdCaptor.capture(), restaurantIdCaptor.capture());
        verify(restaurantService, never()).closeRestaurant(ownerIdCaptor.capture(), restaurantIdCaptor.capture());

        assertThat(ownerIdCaptor.getValue()).isEqualTo(mockedOwnerId);
        assertThat(restaurantIdCaptor.getValue()).isEqualTo(mockedRestaurantId);
    }

    @Test
    @DisplayName("주어지는 요청 파라미터에 따라 가게의 배달 가능 지역과 배달비를 등록한다.")
    void setDeliveryArea() throws Exception {
        // Given
        Long mockedOwnerId = 1L;
        Long mockedRestaurantId = 1L;
        Long areaCodeIdParam = 10L;
        Integer deliveryFeeParam = 1000;

        // When, Then
        mockMvc.perform(
                patch(BASE_URL_TEMPLATE.concat("owners/{ownerId}/restaurants/{restaurantId}/delivery/add"),
                    mockedOwnerId, mockedRestaurantId)
                    .param("areaCodeId", String.valueOf(areaCodeIdParam))
                    .param("deliveryFee", String.valueOf(deliveryFeeParam))
                    .with(csrf().asHeader()))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(document("restaurant-add-delivery",
                pathParameters(
                    parameterWithName("ownerId").description("사장님 아이디"),
                    parameterWithName("restaurantId").description("가게 아이디")
                ),
                requestParameters(
                    parameterWithName("areaCodeId").description("지역코드"),
                    parameterWithName("deliveryFee").description("배달비")
                )));

        ArgumentCaptor<Long> ownerIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> restaurantIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> areaCodeIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Integer> deliveryFeeCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(restaurantService, times(1))
            .setDeliveryArea(ownerIdCaptor.capture(), restaurantIdCaptor.capture(),
                areaCodeIdCaptor.capture(), deliveryFeeCaptor.capture());


        assertThat(ownerIdCaptor.getValue()).isEqualTo(mockedOwnerId);
        assertThat(restaurantIdCaptor.getValue()).isEqualTo(mockedRestaurantId);
        assertThat(areaCodeIdCaptor.getValue()).isEqualTo(areaCodeIdParam);
        assertThat(deliveryFeeCaptor.getValue()).isEqualTo(deliveryFeeParam);
    }

    @Test
    @DisplayName("주어지는 요청 파라미터에 따라 가게의 카테고리를 추가한다.")
    void addCategory() throws Exception {
        // Given
        Long mockedOwnerId = 1L;
        Long mockedRestaurantId = 1L;
        Long categoryIdParam = 10L;

        // When, Then
        mockMvc.perform(
                patch(BASE_URL_TEMPLATE.concat("owners/{ownerId}/restaurants/{restaurantId}/categories/add"),
                    mockedOwnerId, mockedRestaurantId)
                    .param("categoryId", String.valueOf(categoryIdParam))
                    .with(csrf().asHeader()))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(document("restaurant-add-category",
                pathParameters(
                    parameterWithName("ownerId").description("사장님 아이디"),
                    parameterWithName("restaurantId").description("가게 아이디")
                ),
                requestParameters(
                    parameterWithName("categoryId").description("카테고리 아이디")
                )));

        ArgumentCaptor<Long> ownerIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> restaurantIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> categoryIdCaptor = ArgumentCaptor.forClass(Long.class);

        verify(restaurantService, times(1))
            .addCategory(ownerIdCaptor.capture(), restaurantIdCaptor.capture(), categoryIdCaptor.capture());


        assertThat(ownerIdCaptor.getValue()).isEqualTo(mockedOwnerId);
        assertThat(restaurantIdCaptor.getValue()).isEqualTo(mockedRestaurantId);
        assertThat(categoryIdCaptor.getValue()).isEqualTo(categoryIdParam);
    }

    @Test
    @DisplayName("주어지는 요청 파라미터에 따라 가게의 카테고리를 제거한다.")
    void removeCategory() throws Exception {
        // Given
        Long mockedOwnerId = 1L;
        Long mockedRestaurantId = 1L;
        Long categoryIdParam = 10L;

        // When, Then
        mockMvc.perform(
                patch(BASE_URL_TEMPLATE.concat("owners/{ownerId}/restaurants/{restaurantId}/categories/remove"),
                    mockedOwnerId, mockedRestaurantId)
                    .param("categoryId", String.valueOf(categoryIdParam))
                    .with(csrf().asHeader()))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(document("restaurant-remove-category",
                pathParameters(
                    parameterWithName("ownerId").description("사장님 아이디"),
                    parameterWithName("restaurantId").description("가게 아이디")
                ),
                requestParameters(
                    parameterWithName("categoryId").description("카테고리 아이디")
                )));

        ArgumentCaptor<Long> ownerIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> restaurantIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> categoryIdCaptor = ArgumentCaptor.forClass(Long.class);

        verify(restaurantService, times(1))
            .removeCategory(ownerIdCaptor.capture(), restaurantIdCaptor.capture(), categoryIdCaptor.capture());

        assertThat(ownerIdCaptor.getValue()).isEqualTo(mockedOwnerId);
        assertThat(restaurantIdCaptor.getValue()).isEqualTo(mockedRestaurantId);
        assertThat(categoryIdCaptor.getValue()).isEqualTo(categoryIdParam);
    }

    public List<RestaurantFindResponse> makeRestaurantFindResponses() {
        return List.of(
            new RestaurantFindResponse(1L, 1L, "테스트가게1", "760-15-00991", LocalTime.now(), LocalTime.now(), false, "010-1234-6789", "테스트용 가게입니다.",0.0, 0, "서울시 동작구", java.util.List.of("한식", "중식"), LocalDateTime.now(), LocalDateTime.now()),
            new RestaurantFindResponse(2L, 1L, "테스트가게2", "760-15-00992", LocalTime.now(), LocalTime.now(), false, "010-1234-6789", "테스트용 가게입니다.",0.0, 0, "서울시 동작구", java.util.List.of("한식", "중식"), LocalDateTime.now(), LocalDateTime.now()),
            new RestaurantFindResponse(3L, 1L, "테스트가게3", "760-15-00993", LocalTime.now(), LocalTime.now(), false, "010-1234-6789", "테스트용 가게입니다.",0.0, 0, "서울시 동작구", java.util.List.of("한식", "중식"), LocalDateTime.now(), LocalDateTime.now())
        );
    }

}