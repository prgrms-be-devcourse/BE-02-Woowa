package com.example.woowa.restaurant.menu.controller;

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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.woowa.RestDocsConfiguration;
import com.example.woowa.restaurant.menu.dto.MainMenuStatusUpdateRequest;
import com.example.woowa.restaurant.menu.dto.MenuSaveRequest;
import com.example.woowa.restaurant.menu.dto.MenuUpdateRequest;
import com.example.woowa.restaurant.menu.entity.Menu;
import com.example.woowa.restaurant.menu.enums.MenuStatus;
import com.example.woowa.restaurant.menu.repository.MenuRepository;
import com.example.woowa.restaurant.menugroup.entity.MenuGroup;
import com.example.woowa.restaurant.menugroup.repository.MenuGroupRepository;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import com.example.woowa.restaurant.restaurant.repository.RestaurantRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
@Import(RestDocsConfiguration.class)
class MenuApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    ObjectMapper objectMapper;

    MenuGroup savedMenuGroup;
    Restaurant savedRestaurant;
    Menu savedMenu;

    @BeforeEach
    void init() {
        savedRestaurant = restaurantRepository.save(
                Restaurant.createRestaurant("김밥나라", "000-00-00000",
                        LocalTime.of(9, 0, 0), LocalTime.of(23, 0, 0),
                        false, "00-000-0000",
                        "안녕하세요 저희 김밥나라는 정성을 다해 요리합니다.", "서울 특별시 강남구"));

        savedMenuGroup = menuGroupRepository.save(
                MenuGroup.createMenuGroup(savedRestaurant, "김밥류", "맛잇는 김밥"));

        savedMenu = menuRepository.save(
                Menu.createMenu(savedMenuGroup, "참치 김밥", 4500, "맛있는 참치 김밥", false,
                        MenuStatus.SALE));
    }

    @Test
    @DisplayName("메뉴를 추가한다.")
    void addMenuOkTest() throws Exception {
        MenuSaveRequest menuSaveRequest = new MenuSaveRequest(savedMenuGroup.getId(),
                savedMenu.getTitle(), savedMenu.getDescription(),
                savedMenu.getPrice());

        mockMvc.perform(post("/api/v1/menus")
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
                                        .description("메뉴 그룹 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("메뉴명(최대 100자)"),
                                fieldWithPath("description").type(JsonFieldType.STRING)
                                        .description("메뉴 설명(선택, 최대 1000자)"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER)
                                        .description("메뉴 가격")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("생성된 메뉴의 위치")
                        )
                ));

    }

    @Test
    @DisplayName("존재하지 않는 메뉴그룹에 메뉴를 추가하려 하면 상태코드 400 응답이 발생한다.")
    void addMenuBadRequestTest() throws Exception {
        Long wrongMenuGroupId = -1L;
        MenuSaveRequest menuSaveRequest = new MenuSaveRequest(wrongMenuGroupId,
                savedMenu.getTitle(), savedMenu.getDescription(),
                savedMenu.getPrice());

        mockMvc.perform(post("/api/v1/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuSaveRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("메뉴를 단건 조회한다.")
    void findDetailMenuOkTest() throws Exception {
        mockMvc.perform(get("/api/v1/menus/{menuId}", savedMenu.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(savedMenu.getId()))
                .andExpect(jsonPath("title").value(savedMenu.getTitle()))
                .andExpect(jsonPath("description").value(savedMenu.getDescription()))
                .andExpect(jsonPath("price").value(savedMenu.getPrice()))
                .andDo(document("find-detail-menu",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description(
                                        MediaType.APPLICATION_JSON_VALUE)
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(
                                        MediaType.APPLICATION_JSON_VALUE)
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("메뉴 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("메뉴명"),
                                fieldWithPath("description").type(JsonFieldType.STRING)
                                        .description("메뉴 설명"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER)
                                        .description("메뉴 가격")
                        )
                ));
    }

    @Test
    @DisplayName("존재하지 않는 메뉴를 단건 조회하면 상태코드 400 응답이 발생한다.")
    void findDetailMenuNotFoundTest() throws Exception {
        long wrongMenuId = -1L;
        mockMvc.perform(get("/api/v1/menus/" + wrongMenuId)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("메뉴 정보를 업데이트 한다.")
    void updateMenuOkTest() throws Exception {
        MenuUpdateRequest menuUpdateRequest = new MenuUpdateRequest("참치 김밥2", "더 맛있는 참치 김밥", 5000);

        mockMvc.perform(patch("/api/v1/menus/" + savedMenu.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuUpdateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("update-menu",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(
                                        MediaType.APPLICATION_JSON_VALUE)
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("메뉴명(필수, 최대 100자)"),
                                fieldWithPath("description").type(JsonFieldType.STRING)
                                        .description("메뉴 설명(선택, 최대 1000자)"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER)
                                        .description("메뉴 가격(필수)")
                        )
                ));
    }

    @Test
    @DisplayName("존재하지 않는 메뉴 정보를 업데이트하려 하면 상태코드 400 응답이 발생한다.")
    void updateMenuNotFoundTest() throws Exception {
        MenuUpdateRequest menuUpdateRequest = new MenuUpdateRequest("참치 김밥2", "더 맛있는 참치 김밥", 5000);
        long wrongMenuId = -1L;

        mockMvc.perform(patch("/api/v1/menus/" + wrongMenuId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuUpdateRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("메뉴의 대표 메뉴 설정 여부를 변경한다.")
    void changeMainMenuStatusOkTest() throws Exception {
        MainMenuStatusUpdateRequest mainMenuStatusUpdateRequest = new MainMenuStatusUpdateRequest(
                true);

        mockMvc.perform(patch("/api/v1/menus/" + savedMenu.getId() + "/main-menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mainMenuStatusUpdateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("change-main-menu-status",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(
                                        MediaType.APPLICATION_JSON_VALUE)
                        ),
                        requestFields(
                                fieldWithPath("isMainMenu").type(JsonFieldType.BOOLEAN)
                                        .description("대표 메뉴 설정 여부(boolean)")
                        )
                ));
    }

    @Test
    @DisplayName("존재하지 않는 메뉴의 대표 메뉴 설정 여부를 변경하려 하면 상태코드 400 응답이 발생한다.")
    void changeMainMenuStatusNotFoundTest() throws Exception {
        MainMenuStatusUpdateRequest mainMenuStatusUpdateRequest = new MainMenuStatusUpdateRequest(
                true);

        mockMvc.perform(patch("/api/v1/menus/" + savedMenu.getId() + "/main-menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mainMenuStatusUpdateRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("메뉴의 판매 상태를 변경한다.")
    void changeMenuStatusOkTest() throws Exception {
        MenuStatusUpdateRequest menuStatusUpdateRequest = new MenuStatusUpdateRequest("sale");

        mockMvc.perform(patch("/api/v1/menus/" + savedMenu.getId() + "/menu-status")
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
                                        .description("변경할 메뉴 상태(sale, sold_out, hidden 중 하나)")
                        )
                ));
    }

    @Test
    @DisplayName("존재하지 않는 메뉴의 판매 상태를 변경하려 하면 상태코드 400 응답이 발생한다.")
    void changeMenuStatusNotFoundTest() throws Exception {
        MenuStatusUpdateRequest menuStatusUpdateRequest = new MenuStatusUpdateRequest("sale");
        long wrongMenuId = -1L;

        mockMvc.perform(patch("/api/v1/menus/" + wrongMenuId + "/menu-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuStatusUpdateRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("메뉴를 삭제한다.")
    void deleteMenuOkTest() throws Exception {
        mockMvc.perform(delete("/api/v1/menus/{menuId}", savedMenu.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("delete-menu",
                        pathParameters(
                                parameterWithName("menuId").description("삭제할 메뉴 ID")
                        )
                ));
    }

    @Test
    @DisplayName("존재하지 않는 메뉴를 삭제하려 하면 상태코드 400 응답이 발생한다.")
    void deleteMenuNotFoundTest() throws Exception {
        long wrongMenuId = -1L;
        mockMvc.perform(delete("/api/v1/menus/" + wrongMenuId))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @AllArgsConstructor
    @Getter
    static class MenuStatusUpdateRequest {

        private final String menuStatus;
    }
}