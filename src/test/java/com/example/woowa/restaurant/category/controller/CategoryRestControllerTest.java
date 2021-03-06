package com.example.woowa.restaurant.category.controller;

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
import com.example.woowa.restaurant.category.dto.request.CategoryCreateRequest;
import com.example.woowa.restaurant.category.dto.request.CategoryUpdateRequest;
import com.example.woowa.restaurant.category.dto.response.CategoryCreateResponse;
import com.example.woowa.restaurant.category.dto.response.CategoryFindResponse;
import com.example.woowa.restaurant.category.service.CategoryService;
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
@WebMvcTest(controllers = CategoryRestController.class, excludeFilters =
    {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser
class CategoryRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL_TEMPLATE = "/baemin/v1/";

    @Test
    @DisplayName("????????? ??????????????? ????????? ???????????? ????????? ?????? ????????????.")
    void testCreateCategory() throws Exception {
        // Given
        CategoryCreateRequest request = new CategoryCreateRequest("??????");
        CategoryCreateResponse mockedResponse = new CategoryCreateResponse(1L, "??????", LocalDateTime.now());
        when(categoryService.createCategory(any(CategoryCreateRequest.class))).thenReturn(mockedResponse);

        // When, Then
        MvcResult mvcResult = mockMvc.perform(
                post(BASE_URL_TEMPLATE.concat("categories"))
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .with(csrf().asHeader()))
            .andExpect(status().isCreated())
            .andDo(print())
            .andDo(document("category-create",
                requestFields(
                    fieldWithPath("name").type(JsonFieldType.STRING).description("??????")
                ),
                responseFields(
                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????????"),
                    fieldWithPath("name").type(JsonFieldType.STRING).description("??????"),
                    fieldWithPath("createdAt").type(JsonFieldType.STRING).description("?????????")
                )))
            .andReturn();

        ArgumentCaptor<CategoryCreateRequest> requestCaptor = ArgumentCaptor.forClass(CategoryCreateRequest.class);
        verify(categoryService, times(1)).createCategory(requestCaptor.capture());

        assertThat(requestCaptor.getValue().getName()).isEqualTo(request.getName());
        assertThat(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8))
            .isEqualTo(objectMapper.writeValueAsString(mockedResponse));
    }

    @Test
    @DisplayName("???????????? ????????? ?????? ??????????????? ????????????.")
    void testFindAllCategories() throws Exception {
        // Given
        List<CategoryFindResponse> mockedResponses = makeCategoryFindResponses();
        when(categoryService.findCategories()).thenReturn(mockedResponses);

        // When, Then
        MvcResult mvcResult = mockMvc.perform(
                get(BASE_URL_TEMPLATE.concat("categories"))
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .characterEncoding(StandardCharsets.UTF_8))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("category-findAll",
                responseFields(
                    fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("?????????"),
                    fieldWithPath("[].name").type(JsonFieldType.STRING).description("??????"),
                    fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("?????????"),
                    fieldWithPath("[].updatedAt").type(JsonFieldType.STRING).description("?????????")
                )))
            .andReturn();

        verify(categoryService, times(1)).findCategories();
        assertThat(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8))
            .isEqualTo(objectMapper.writeValueAsString(mockedResponses));
    }

    @Test
    @DisplayName("Url path??? ???????????? ???????????? ?????? ???????????? ????????? ????????? ????????????.")
    void findCategoryById() throws Exception {
        // Given
        Long mockSavedEntityId = 1L;
        CategoryFindResponse mockedResponse = new CategoryFindResponse(1L, "??????", LocalDateTime.now(), LocalDateTime.now());
        when(categoryService.findCategoryById(anyLong())).thenReturn(mockedResponse);

        // When, Then
        MvcResult mvcResult = mockMvc.perform(
                get(BASE_URL_TEMPLATE.concat("categories/{categoryId}"), mockSavedEntityId)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .characterEncoding(StandardCharsets.UTF_8))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("category-findById",
                pathParameters(
                    parameterWithName("categoryId").description("????????? ???????????? ?????????")
                ),
                responseFields(
                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????????"),
                    fieldWithPath("name").type(JsonFieldType.STRING).description("??????"),
                    fieldWithPath("createdAt").type(JsonFieldType.STRING).description("?????????"),
                    fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("?????????")
                )))
            .andReturn();

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(categoryService, times(1)).findCategoryById(idCaptor.capture());

        assertThat(idCaptor.getValue()).isEqualTo(mockSavedEntityId);
        assertThat(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8))
            .isEqualTo(objectMapper.writeValueAsString(mockedResponse));
    }

    @Test
    @DisplayName("???????????? ???????????? ????????? ???????????? ????????? ????????????.")
    void updateCategoryById() throws Exception {
        // Given
        Long mockSavedEntityId = 1L;
        CategoryUpdateRequest updateRequest = new CategoryUpdateRequest("??????");

        // When, Then
        mockMvc.perform(
                put(BASE_URL_TEMPLATE.concat("categories/{categoryId}"), mockSavedEntityId)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest))
                    .with(csrf().asHeader()))
            .andExpect(status().isNoContent())
            .andDo(document("category-update",
                pathParameters(
                  parameterWithName("categoryId").description("????????? ???????????? ?????????")
                ),
                requestFields(
                    fieldWithPath("name").type(JsonFieldType.STRING).description("????????? ??????")
                )));

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<CategoryUpdateRequest> requestCaptor = ArgumentCaptor.forClass(CategoryUpdateRequest.class);
        verify(categoryService, times(1)).updateCategoryById(idCaptor.capture(), requestCaptor.capture());

        assertThat(idCaptor.getValue()).isEqualTo(mockSavedEntityId);
        assertThat(objectMapper.writeValueAsString(requestCaptor.getValue()))
            .isEqualTo(objectMapper.writeValueAsString(updateRequest));
    }

    @Test
    @DisplayName("Url path??? ???????????? ???????????? ?????? ???????????? ???????????? ????????????.")
    void deleteCategoryById() throws Exception {
        // Given
        Long mockSavedEntityId = 1L;

        // When, Then
        mockMvc.perform(
                delete(BASE_URL_TEMPLATE.concat("categories/{categoryId}"), mockSavedEntityId)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .with(csrf().asHeader()))
            .andExpect(status().isNoContent())
            .andDo(document("category-delete",
                pathParameters(
                    parameterWithName("categoryId").description("????????? ???????????? ?????????")
                )));

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(categoryService, times(1)).deleteCategory(idCaptor.capture());

        assertThat(idCaptor.getValue()).isEqualTo(mockSavedEntityId);
    }

    public static List<CategoryFindResponse> makeCategoryFindResponses() {
        return List.of(
            new CategoryFindResponse(1L, "??????", LocalDateTime.now(), LocalDateTime.now()),
            new CategoryFindResponse(2L, "??????", LocalDateTime.now(), LocalDateTime.now()),
            new CategoryFindResponse(3L, "??????", LocalDateTime.now(), LocalDateTime.now()),
            new CategoryFindResponse(4L, "??????", LocalDateTime.now(), LocalDateTime.now())
        );
    }

}