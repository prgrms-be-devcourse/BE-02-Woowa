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
    @DisplayName("레스토랑에 메뉴 그룹을 추가한다.")
    void addMenuGroupOkTest() throws Exception {
        MenuGroupSaveRequest menuGroupSaveRequest = new MenuGroupSaveRequest("볶음밥류", "맛있는 볶음밥");
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
                                parameterWithName("restaurantId").description("메뉴그룹을 추가할 가게의 ID")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(
                                        MediaType.APPLICATION_JSON_VALUE)
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("메뉴그룹명(필수, 최대 100자"),
                                fieldWithPath("description").type(JsonFieldType.STRING)
                                        .type(JsonFieldType.STRING)
                                        .description("메뉴그룹 설명(선택, 최대 500자)")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("생성된 메뉴 그룹의 위치")
                        )
                ));

        then(menuGroupService).should().addMenuGroup(restaurantId, menuGroupSaveRequest);
    }

    @Test
    @DisplayName("메뉴 그룹명을 누락하면 상태코드 400 응답이 발생한다.")
    void addMenuGroupEmptyTitleTest() throws Exception {
        long restaurantId = 1L;
        MenuGroupSaveRequest menuGroupSaveRequest = new MenuGroupSaveRequest("", "맛있는 볶음밥");

        mockMvc.perform(post("/api/v1/restaurant/{restaurantId}/menu-groups", restaurantId)
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuGroupSaveRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        then(menuGroupService).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("존재하지 않는 레스토랑에 메뉴 그룹을 추가하려 하면 상태코드 404 응답이 발생한다.")
    void addMenuGroupNotFoundRestaurantTest() throws Exception {
        MenuGroupSaveRequest request = new MenuGroupSaveRequest("볶음밥류", "맛있는 볶음밥");
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
    @DisplayName("메뉴 그룹을 단건 조회한다.")
    void findMenuGroupOkTest() throws Exception {
        long menuGroupId = 1L;
        MenuGroupResponse response = new MenuGroupResponse(menuGroupId, "김밥류", "맛있는 김밥류");
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
                                parameterWithName("menuGroupId").description("조회할 메뉴그룹 ID")
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
                                        .description("메뉴그룹 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("메뉴그룹명"),
                                fieldWithPath("description").type(JsonFieldType.STRING)
                                        .description("메뉴그룹 설명")
                        )
                ));

        then(menuGroupService).should().findMenuById(menuGroupId);
    }

    @Test
    @DisplayName("존재하지 않는 메뉴 그룹을 단건 조회하면 상태코드 404 응답이 발생한다.")
    void findMenuGroupNotFoundTest() throws Exception {
        long wrongMenuGroupId = -1L;
        given(menuGroupService.findMenuById(wrongMenuGroupId)).willThrow(NotFoundException.class);

        mockMvc.perform(get("/api/v1/menu-groups/{menuGroupId}", wrongMenuGroupId))
                .andDo(print())
                .andExpect(status().isNotFound());

        then(menuGroupService).should().findMenuById(wrongMenuGroupId);
    }

    @Test
    @DisplayName("레스토랑의 메뉴 그룹을 조회한다.")
    void findMenuGroupListOkTest() throws Exception {
        long restaurantId = 1L;
        List<MenuGroupResponse> menuGroupResponses = List.of(
                new MenuGroupResponse(1L, "김밥류", "맛있는 김밥"),
                new MenuGroupResponse(2L, "찌개류", "맛있는 찌개"),
                new MenuGroupResponse(2L, "면류", "맛있는 면")
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
                                parameterWithName("restaurantId").description("메뉴그룹을 조회할 가게 ID")
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
                                        .description("메뉴그룹 목록"),
                                fieldWithPath("menuGroups[].id").type(JsonFieldType.NUMBER)
                                        .description("메뉴그룹 ID"),
                                fieldWithPath("menuGroups[].title").type(JsonFieldType.STRING)
                                        .description("메뉴그룹명"),
                                fieldWithPath("menuGroups[].description").type(JsonFieldType.STRING)
                                        .description("메뉴그룹 설명")
                        )
                ));

        then(menuGroupService).should().findMenuGroupByRestaurant(restaurantId);
    }

    @Test
    @DisplayName("존재하지 않는 레스토랑의 메뉴 그룹을 조회하면 상태코드 404 응답이 발생한다.")
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
    @DisplayName("메뉴 그룹 정보를 업데이트 한다.")
    void updateMenuGroupOkTest() throws Exception {
        long menuGroupId = 1L;
        MenuGroupUpdateRequest request = new MenuGroupUpdateRequest("사이드류",
                "맛있는 사이드 메뉴");

        mockMvc.perform(patch("/api/v1/menu-groups/{menuGroupId}", menuGroupId)
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("update-menu-group",
                        pathParameters(
                                parameterWithName("menuGroupId").description("업데이트할 메뉴그룹 ID")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(
                                        MediaType.APPLICATION_JSON_VALUE)
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("메뉴그룹명(필수, 최대 100자)"),
                                fieldWithPath("description").type(JsonFieldType.STRING)
                                        .description("메뉴그룹 설명(선택, 최대 500자)")
                        )
                ));

        then(menuGroupService).should().updateMenuGroup(menuGroupId, request);
    }

    @Test
    @DisplayName("메뉴 그룹 정보를 업데이트하는데 메뉴명을 누락하면 상태코드 400 응답이 발생한다.")
    void updateMenuGroupEmptyTitleTest() throws Exception {
        long menuGroupId = 1L;
        MenuGroupUpdateRequest request = new MenuGroupUpdateRequest("",
                "맛있는 사이드 메뉴");

        mockMvc.perform(patch("/api/v1/menu-groups/{menuGroupId}", menuGroupId)
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        then(menuGroupService).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("존재하지 않는 메뉴 그룹 정보를 업데이트하려 하면 상태코드 404 응답이 발생한다.")
    void updateMenuGroupNotFoundTest() throws Exception {
        long wrongMenuGroupId = -1L;
        MenuGroupUpdateRequest request = new MenuGroupUpdateRequest("사이드류",
                "맛있는 사이드 메뉴");
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
    @DisplayName("메뉴 그룹을 삭제한다.")
    void deleteMenuGroupOkTest() throws Exception {
        long menuGroupId = 1L;

        mockMvc.perform(delete("/api/v1/menu-groups/{menuGroupId}", menuGroupId)
                        .with(csrf().asHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("delete-menu-group",
                        pathParameters(
                                parameterWithName("menuGroupId").description("삭제할 메뉴그룹 ID")
                        )
                ));

        then(menuGroupService).should().deleteMenuGroup(menuGroupId);
    }

    @Test
    @DisplayName("존재하지 않는 메뉴 그룹을 삭제하려 하면 상태코드 404 응답이 발생한다.")
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