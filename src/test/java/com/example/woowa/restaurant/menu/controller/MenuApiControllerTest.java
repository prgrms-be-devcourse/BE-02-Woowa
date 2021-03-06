package com.example.woowa.restaurant.menu.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import com.example.woowa.restaurant.menu.dto.MainMenuStatusUpdateRequest;
import com.example.woowa.restaurant.menu.dto.MenuResponse;
import com.example.woowa.restaurant.menu.dto.MenuSaveRequest;
import com.example.woowa.restaurant.menu.dto.MenuUpdateRequest;
import com.example.woowa.restaurant.menu.service.MenuService;
import com.example.woowa.security.configuration.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
@WebMvcTest(value = MenuApiController.class, excludeFilters = {
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = SecurityConfig.class
        )
})
@Import(RestDocsConfiguration.class)
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser
class MenuApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MenuService menuService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("????????? ????????????.")
    void addMenuOkTest() throws Exception {
        long savedMenuId = 1L;
        long menuGroupId = 1L;
        MenuSaveRequest menuSaveRequest = new MenuSaveRequest(menuGroupId,
                "?????? ??????", "????????? ?????? ??????",
                4500);

        given(menuService.addMenu(menuSaveRequest)).willReturn(savedMenuId);

        mockMvc.perform(post("/api/v1/menus")
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(menuSaveRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andDo(document("add-menu",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(
                                        MediaType.APPLICATION_JSON_VALUE),
                                headerWithName(HttpHeaders.ACCEPT).description(
                                        MediaType.APPLICATION_JSON_VALUE)
                        ),
                        requestFields(
                                fieldWithPath("menuGroupId").type(JsonFieldType.NUMBER)
                                        .description("?????? ?????? ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("?????????(?????? 100???)"),
                                fieldWithPath("description").type(JsonFieldType.STRING)
                                        .description("?????? ??????(??????, ?????? 1000???)"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER)
                                        .description("?????? ??????")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("????????? ????????? ??????")
                        )
                ));

        then(menuService).should().addMenu(menuSaveRequest);
    }

    @Test
    @DisplayName("????????? ??????????????? ?????? ??????(title, price)??? ???????????? 400 ?????? ????????? ?????? ?????? ???????????? ????????????.")
    void addMenuMissingRequiredFieldTest() throws Exception {
        long menuGroupId = 1L;
        MenuSaveRequest menuSaveRequest = new MenuSaveRequest(menuGroupId,
                null, null,
                null);

        mockMvc.perform(post("/api/v1/menus")
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(menuSaveRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        then(menuService).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("???????????? ?????? ??????????????? ????????? ???????????? ?????? ???????????? 404 ????????? ????????????.")
    void addMenuBadRequestTest() throws Exception {
        Long wrongMenuGroupId = -1L;
        MenuSaveRequest menuSaveRequest = new MenuSaveRequest(wrongMenuGroupId,
                "?????? ??????", "????????? ?????? ??????",
                4500);

        given(menuService.addMenu(menuSaveRequest)).willThrow(
                NotFoundException.class);

        mockMvc.perform(post("/api/v1/menus")
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuSaveRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());

        then(menuService).should().addMenu(menuSaveRequest);
    }

    @Test
    @DisplayName("????????? ?????? ????????????.")
    void findDetailMenuOkTest() throws Exception {
        long menuId = 1L;
        MenuResponse response = new MenuResponse(menuId, "?????? ??????", "????????? ?????? ??????", 4500);
        given(menuService.findMenuById(menuId)).willReturn(response);

        mockMvc.perform(get("/api/v1/menus/{menuId}", menuId)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(menuId))
                .andExpect(jsonPath("title").value("?????? ??????"))
                .andExpect(jsonPath("description").value("????????? ?????? ??????"))
                .andExpect(jsonPath("price").value(4500))
                .andDo(document("find-detail-menu",
                        pathParameters(
                                parameterWithName("menuId").description("????????? ?????? ID")
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
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????? ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("?????????"),
                                fieldWithPath("description").type(JsonFieldType.STRING)
                                        .description("?????? ??????"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER)
                                        .description("?????? ??????")
                        )
                ));

        then(menuService).should().findMenuById(menuId);
    }

    @Test
    @DisplayName("???????????? ?????? ????????? ?????? ???????????? ???????????? 404 ????????? ????????????.")
    void findDetailMenuNotFoundTest() throws Exception {
        long wrongMenuId = -1L;
        given(menuService.findMenuById(wrongMenuId)).willThrow(NotFoundException.class);

        mockMvc.perform(get("/api/v1/menus/" + wrongMenuId)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound());

        then(menuService).should().findMenuById(wrongMenuId);
    }

    @Test
    @DisplayName("?????? ????????? ???????????? ??????.")
    void updateMenuOkTest() throws Exception {
        long menuId = 1L;
        MenuUpdateRequest request = new MenuUpdateRequest("?????? ??????2", "??? ????????? ?????? ??????", 5000);

        mockMvc.perform(patch("/api/v1/menus/" + menuId)
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("update-menu",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(
                                        MediaType.APPLICATION_JSON_VALUE)
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("?????????(??????, ?????? 100???)"),
                                fieldWithPath("description").type(JsonFieldType.STRING)
                                        .description("?????? ??????(??????, ?????? 1000???)"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER)
                                        .description("?????? ??????(??????)")
                        )
                ));

        then(menuService).should().updateMenu(menuId, request);
    }

    @Test
    @DisplayName("?????? ????????? ????????????????????? ?????? ??????(title, price)??? ???????????? 400 ??????????????? ?????? ?????? ???????????? ????????????.")
    void updateMenuMissingRequiredFieldTest() throws Exception {
        long menuId = 1L;
        MenuUpdateRequest menuUpdateRequest = new MenuUpdateRequest(null, null, null);

        mockMvc.perform(patch("/api/v1/menus/" + menuId)
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuUpdateRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        then(menuService).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("???????????? ?????? ?????? ????????? ?????????????????? ?????? ???????????? 404 ????????? ????????????.")
    void updateMenuNotFoundTest() throws Exception {
        long wrongMenuId = -1L;
        MenuUpdateRequest request = new MenuUpdateRequest("?????? ??????2", "??? ????????? ?????? ??????", 5000);
        doThrow(NotFoundException.class).when(menuService).updateMenu(wrongMenuId, request);

        mockMvc.perform(patch("/api/v1/menus/" + wrongMenuId)
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound());

        then(menuService).should().updateMenu(wrongMenuId, request);
    }

    @Test
    @DisplayName("????????? ?????? ?????? ?????? ????????? ????????????.")
    void changeMainMenuStatusOkTest() throws Exception {
        long menuId = 1L;
        MainMenuStatusUpdateRequest request = new MainMenuStatusUpdateRequest(
                true);

        mockMvc.perform(patch("/api/v1/menus/{menuId}/main-menu", menuId)
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("change-main-menu-status",
                        pathParameters(
                                parameterWithName("menuId").description("????????? ????????? ?????? ID")
                        ),
                        requestFields(
                                fieldWithPath("isMainMenu").type(JsonFieldType.BOOLEAN)
                                        .description("?????? ?????? ?????? ??????")
                        )
                ));

        then(menuService).should().changeMainMenuStatus(menuId, request);
    }

    @Test
    @DisplayName("???????????? ?????? ????????? ?????? ?????? ?????? ????????? ???????????? ?????? ???????????? 404 ????????? ????????????.")
    void changeMainMenuStatusNotFoundTest() throws Exception {
        long wrongMenuId = -1L;
        MainMenuStatusUpdateRequest request = new MainMenuStatusUpdateRequest(
                true);
        doThrow(NotFoundException.class).when(menuService)
                .changeMainMenuStatus(wrongMenuId, request);

        mockMvc.perform(patch("/api/v1/menus/{menuId}/main-menu", wrongMenuId)
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound());

        then(menuService).should().changeMainMenuStatus(wrongMenuId, request);
    }

    @Test
    @DisplayName("????????? ?????? ????????? ????????????.")
    void changeMenuStatusOkTest() throws Exception {
        long menuId = 1L;
        MenuStatusUpdateRequest menuStatusUpdateRequest = new MenuStatusUpdateRequest("sale");

        mockMvc.perform(patch("/api/v1/menus/{menuId}/menu-status", menuId)
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuStatusUpdateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("change-menu-status",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(
                                        MediaType.APPLICATION_JSON_VALUE)
                        ),
                        requestFields(
                                fieldWithPath("menuStatus").type(JsonFieldType.STRING)
                                        .description("????????? ?????? ??????(sale, sold_out, hidden ??? ??????)")
                        )
                ));

        then(menuService).should().changeMenuStatus(anyLong(), any());
    }

    @Test
    @DisplayName("???????????? ?????? ????????? ?????? ????????? ???????????? ?????? ???????????? 404 ????????? ????????????.")
    void changeMenuStatusNotFoundTest() throws Exception {
        MenuStatusUpdateRequest menuStatusUpdateRequest = new MenuStatusUpdateRequest("sale");
        long wrongMenuId = -1L;
        doThrow(NotFoundException.class).when(menuService).changeMenuStatus(anyLong(), any());

        mockMvc.perform(patch("/api/v1/menus/{menuId}/menu-status", wrongMenuId)
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuStatusUpdateRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());

        then(menuService).should().changeMenuStatus(anyLong(), any());
    }

    @Test
    @DisplayName("????????? ????????????.")
    void deleteMenuOkTest() throws Exception {
        long menuId = 1L;

        mockMvc.perform(delete("/api/v1/menus/{menuId}", menuId)
                        .with(csrf().asHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("delete-menu",
                        pathParameters(
                                parameterWithName("menuId").description("????????? ?????? ID")
                        )
                ));

        then(menuService).should().deleteMenu(menuId);
    }

    @Test
    @DisplayName("???????????? ?????? ????????? ???????????? ?????? ???????????? 404 ????????? ????????????.")
    void deleteMenuNotFoundTest() throws Exception {
        long wrongMenuId = -1L;
        doThrow(NotFoundException.class).when(menuService).deleteMenu(wrongMenuId);

        mockMvc.perform(delete("/api/v1/menus/" + wrongMenuId)
                        .with(csrf().asHeader()))
                .andDo(print())
                .andExpect(status().isNotFound());

        then(menuService).should().deleteMenu(wrongMenuId);
    }

    @AllArgsConstructor
    @Getter
    static class MenuStatusUpdateRequest {

        private final String menuStatus;
    }
}