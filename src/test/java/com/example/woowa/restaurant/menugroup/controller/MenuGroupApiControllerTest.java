package com.example.woowa.restaurant.menugroup.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.woowa.RestDocsConfiguration;
import com.example.woowa.common.exception.NotFoundException;
import com.example.woowa.restaurant.menugroup.dto.MenuGroupListResponse;
import com.example.woowa.restaurant.menugroup.dto.MenuGroupResponse;
import com.example.woowa.restaurant.menugroup.dto.MenuGroupSaveRequest;
import com.example.woowa.restaurant.menugroup.dto.MenuGroupUpdateRequest;
import com.example.woowa.restaurant.menugroup.service.MenuGroupService;
import com.example.woowa.restaurant.restaurant.service.RestaurantService;
import com.example.woowa.security.configuration.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs
@WebMvcTest(value = MenuGroupApiController.class, excludeFilters = {
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = SecurityConfig.class
        )
})
@Import(RestDocsConfiguration.class)
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser
class MenuGroupApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MenuGroupService menuGroupService;

    @MockBean
    RestaurantService restaurantService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("??????????????? ?????? ????????? ????????????.")
    void addMenuGroupOkTest() throws Exception {
        MenuGroupSaveRequest menuGroupSaveRequest = new MenuGroupSaveRequest("????????????", "????????? ?????????");
        long restaurantId = 1L;
        long menuGroupId = 2L;
        given(menuGroupService.addMenuGroup(restaurantId, menuGroupSaveRequest)).willReturn(
                menuGroupId);

        mockMvc.perform(post("/api/v1/restaurant/{restaurantId}/menu-groups", restaurantId)
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuGroupSaveRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andDo(document("add-menu-group",
                        pathParameters(
                                parameterWithName("restaurantId").description("??????????????? ????????? ????????? ID")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(
                                        MediaType.APPLICATION_JSON_VALUE)
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("???????????????(??????, ?????? 100???"),
                                fieldWithPath("description").type(JsonFieldType.STRING)
                                        .type(JsonFieldType.STRING)
                                        .description("???????????? ??????(??????, ?????? 500???)")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("????????? ?????? ????????? ??????")
                        )
                ));

        then(menuGroupService).should().addMenuGroup(restaurantId, menuGroupSaveRequest);
    }

    @Test
    @DisplayName("?????? ???????????? ???????????? ???????????? 400 ????????? ????????????.")
    void addMenuGroupEmptyTitleTest() throws Exception {
        long restaurantId = 1L;
        MenuGroupSaveRequest menuGroupSaveRequest = new MenuGroupSaveRequest("", "????????? ?????????");

        mockMvc.perform(post("/api/v1/restaurant/{restaurantId}/menu-groups", restaurantId)
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuGroupSaveRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        then(menuGroupService).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("???????????? ?????? ??????????????? ?????? ????????? ???????????? ?????? ???????????? 404 ????????? ????????????.")
    void addMenuGroupNotFoundRestaurantTest() throws Exception {
        MenuGroupSaveRequest request = new MenuGroupSaveRequest("????????????", "????????? ?????????");
        long wrongRestaurantId = -1L;
        given(menuGroupService.addMenuGroup(wrongRestaurantId, request)).willThrow(
                NotFoundException.class);

        mockMvc.perform(post("/api/v1/restaurant/{restaurantId}/menu-groups", wrongRestaurantId)
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound());

        then(menuGroupService).should().addMenuGroup(wrongRestaurantId, request);
    }

    @Test
    @DisplayName("?????? ????????? ?????? ????????????.")
    void findMenuGroupOkTest() throws Exception {
        long menuGroupId = 1L;
        MenuGroupResponse response = new MenuGroupResponse(menuGroupId, "?????????", "????????? ?????????");
        given(menuGroupService.findMenuById(menuGroupId)).willReturn(response);

        mockMvc.perform(get("/api/v1/menu-groups/{menuGroupId}", menuGroupId)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(menuGroupId))
                .andExpect(jsonPath("title").value(response.getTitle()))
                .andExpect(jsonPath("description").value(response.getDescription()))
                .andDo(document("find-menu-group",
                        pathParameters(
                                parameterWithName("menuGroupId").description("????????? ???????????? ID")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description(
                                        MediaType.APPLICATION_JSON_VALUE)
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(
                                        MediaType.APPLICATION_JSON_VALUE)
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER)
                                        .description("???????????? ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("???????????????"),
                                fieldWithPath("description").type(JsonFieldType.STRING)
                                        .description("???????????? ??????")
                        )
                ));

        then(menuGroupService).should().findMenuById(menuGroupId);
    }

    @Test
    @DisplayName("???????????? ?????? ?????? ????????? ?????? ???????????? ???????????? 404 ????????? ????????????.")
    void findMenuGroupNotFoundTest() throws Exception {
        long wrongMenuGroupId = -1L;
        given(menuGroupService.findMenuById(wrongMenuGroupId)).willThrow(NotFoundException.class);

        mockMvc.perform(get("/api/v1/menu-groups/{menuGroupId}", wrongMenuGroupId))
                .andDo(print())
                .andExpect(status().isNotFound());

        then(menuGroupService).should().findMenuById(wrongMenuGroupId);
    }

    @Test
    @DisplayName("??????????????? ?????? ????????? ????????????.")
    void findMenuGroupListOkTest() throws Exception {
        long restaurantId = 1L;
        List<MenuGroupResponse> menuGroupResponses = List.of(
                new MenuGroupResponse(1L, "?????????", "????????? ??????"),
                new MenuGroupResponse(2L, "?????????", "????????? ??????"),
                new MenuGroupResponse(2L, "??????", "????????? ???")
        );

        given(menuGroupService.findMenuGroupByRestaurant(restaurantId)).willReturn(
                new MenuGroupListResponse(menuGroupResponses));

        mockMvc.perform(
                        get("/api/v1/restaurant/{restaurantId}/menu-groups", restaurantId)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("menuGroups.length()").value(menuGroupResponses.size()))
                .andDo(document("find-menu-group-list",
                        pathParameters(
                                parameterWithName("restaurantId").description("??????????????? ????????? ?????? ID")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description(
                                        MediaType.APPLICATION_JSON_VALUE)
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(
                                        MediaType.APPLICATION_JSON_VALUE)
                        ),
                        responseFields(
                                fieldWithPath("menuGroups[]").type(JsonFieldType.ARRAY)
                                        .description("???????????? ??????"),
                                fieldWithPath("menuGroups[].id").type(JsonFieldType.NUMBER)
                                        .description("???????????? ID"),
                                fieldWithPath("menuGroups[].title").type(JsonFieldType.STRING)
                                        .description("???????????????"),
                                fieldWithPath("menuGroups[].description").type(JsonFieldType.STRING)
                                        .description("???????????? ??????")
                        )
                ));

        then(menuGroupService).should().findMenuGroupByRestaurant(restaurantId);
    }

    @Test
    @DisplayName("???????????? ?????? ??????????????? ?????? ????????? ???????????? ???????????? 404 ????????? ????????????.")
    void findMenuGroupListNotFoundRestaurantTest() throws Exception {
        long wrongRestaurantId = -1L;
        given(menuGroupService.findMenuGroupByRestaurant(wrongRestaurantId)).willThrow(
                NotFoundException.class);

        mockMvc.perform(get("/api/v1/restaurant/{restaurantId}/menu-groups", wrongRestaurantId)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound());

        then(menuGroupService).should().findMenuGroupByRestaurant(wrongRestaurantId);
    }

    @Test
    @DisplayName("?????? ?????? ????????? ???????????? ??????.")
    void updateMenuGroupOkTest() throws Exception {
        long menuGroupId = 1L;
        MenuGroupUpdateRequest request = new MenuGroupUpdateRequest("????????????",
                "????????? ????????? ??????");

        mockMvc.perform(patch("/api/v1/menu-groups/{menuGroupId}", menuGroupId)
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("update-menu-group",
                        pathParameters(
                                parameterWithName("menuGroupId").description("??????????????? ???????????? ID")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(
                                        MediaType.APPLICATION_JSON_VALUE)
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("???????????????(??????, ?????? 100???)"),
                                fieldWithPath("description").type(JsonFieldType.STRING)
                                        .description("???????????? ??????(??????, ?????? 500???)")
                        )
                ));

        then(menuGroupService).should().updateMenuGroup(menuGroupId, request);
    }

    @Test
    @DisplayName("?????? ?????? ????????? ????????????????????? ???????????? ???????????? ???????????? 400 ????????? ????????????.")
    void updateMenuGroupEmptyTitleTest() throws Exception {
        long menuGroupId = 1L;
        MenuGroupUpdateRequest request = new MenuGroupUpdateRequest("",
                "????????? ????????? ??????");

        mockMvc.perform(patch("/api/v1/menu-groups/{menuGroupId}", menuGroupId)
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        then(menuGroupService).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("???????????? ?????? ?????? ?????? ????????? ?????????????????? ?????? ???????????? 404 ????????? ????????????.")
    void updateMenuGroupNotFoundTest() throws Exception {
        long wrongMenuGroupId = -1L;
        MenuGroupUpdateRequest request = new MenuGroupUpdateRequest("????????????",
                "????????? ????????? ??????");
        doThrow(NotFoundException.class).when(menuGroupService)
                .updateMenuGroup(wrongMenuGroupId, request);

        mockMvc.perform(patch("/api/v1/menu-groups/{menuGroupId}", wrongMenuGroupId)
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound());

        then(menuGroupService).should().updateMenuGroup(wrongMenuGroupId, request);
    }

    @Test
    @DisplayName("?????? ????????? ????????????.")
    void deleteMenuGroupOkTest() throws Exception {
        long menuGroupId = 1L;

        mockMvc.perform(delete("/api/v1/menu-groups/{menuGroupId}", menuGroupId)
                        .with(csrf().asHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("delete-menu-group",
                        pathParameters(
                                parameterWithName("menuGroupId").description("????????? ???????????? ID")
                        )
                ));

        then(menuGroupService).should().deleteMenuGroup(menuGroupId);
    }

    @Test
    @DisplayName("???????????? ?????? ?????? ????????? ???????????? ?????? ???????????? 404 ????????? ????????????.")
    void deleteMenuGroupNotFoundTest() throws Exception {
        long wrongMenuGroupId = -1L;
        doThrow(NotFoundException.class).when(menuGroupService).deleteMenuGroup(wrongMenuGroupId);

        mockMvc.perform(delete("/api/v1/menu-groups/{menuGroupId}", wrongMenuGroupId)
                        .with(csrf().asHeader()))
                .andDo(print())
                .andExpect(status().isNotFound());

        then(menuGroupService).should().deleteMenuGroup(wrongMenuGroupId);
    }
}