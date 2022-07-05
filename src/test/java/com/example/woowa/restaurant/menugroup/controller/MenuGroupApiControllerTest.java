package com.example.woowa.restaurant.menugroup.controller;

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
import com.example.woowa.restaurant.menugroup.dto.MenuGroupResponse;
import com.example.woowa.restaurant.menugroup.dto.MenuGroupSaveRequest;
import com.example.woowa.restaurant.menugroup.dto.MenuGroupUpdateRequest;
import com.example.woowa.restaurant.menugroup.entity.MenuGroup;
import com.example.woowa.restaurant.menugroup.repository.MenuGroupRepository;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import com.example.woowa.restaurant.restaurant.repository.RestaurantRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
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
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
@Transactional
@Import(RestDocsConfiguration.class)
class MenuGroupApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    ObjectMapper objectMapper;

    Restaurant savedRestaurant;

    List<MenuGroup> savedMenuGroups;

    @BeforeEach
    void init() {
        savedRestaurant = restaurantRepository.save(
                Restaurant.createRestaurant("김밥나라", "000-00-00000",
                        LocalTime.of(9, 0, 0), LocalTime.of(23, 0, 0),
                        false, "00-000-0000",
                        "안녕하세요 저희 김밥나라는 정성을 다해 요리합니다.", "서울 특별시 강남구"));

        savedMenuGroups = List.of(
                menuGroupRepository.save(
                        MenuGroup.createMenuGroup(savedRestaurant, "김밥류", "맛잇는 김밥")),
                menuGroupRepository.save(
                        MenuGroup.createMenuGroup(savedRestaurant, "면류", "맛잇는 면")),
                menuGroupRepository.save(
                        MenuGroup.createMenuGroup(savedRestaurant, "찌개류", "맛잇는 찌개"))
        );
    }

    @Test
    @DisplayName("레스토랑에 메뉴 그룹을 추가한다.")
    void addMenuGroupOkTest() throws Exception {
        MenuGroupSaveRequest menuGroupSaveRequest = new MenuGroupSaveRequest("볶음밥류", "맛있는 볶음밥");

        mockMvc.perform(post("/api/v1/restaurant/{restaurantId}/menu-groups", savedRestaurant.getId())
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
    }

    @Test
    @DisplayName("메뉴 그룹명을 누락하면 상태코드 400 응답이 발생한다.")
    void addMenuGroupEmptyTitleTest() throws Exception {
        MenuGroupSaveRequest menuGroupSaveRequest = new MenuGroupSaveRequest("", "맛있는 볶음밥");

        mockMvc.perform(post("/api/v1/restaurant/{restaurantId}/menu-groups", savedRestaurant.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuGroupSaveRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("존재하지 않는 레스토랑에 메뉴 그룹을 추가하려 하면 상태코드 404 응답이 발생한다.")
    void addMenuGroupNotFoundRestaurantTest() throws Exception {
        MenuGroupSaveRequest menuGroupSaveRequest = new MenuGroupSaveRequest("볶음밥류", "맛있는 볶음밥");
        long wrongRestaurantId = -1L;

        mockMvc.perform(post("/api/v1/restaurant/{restaurantId}/menu-groups", wrongRestaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuGroupSaveRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("메뉴 그룹을 단건 조회한다.")
    void findMenuGroupOkTest() throws Exception {
        MenuGroup menuGroup = savedMenuGroups.get(0);

        mockMvc.perform(get("/api/v1/menu-groups/{menuGroupId}", menuGroup.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(menuGroup.getId()))
                .andExpect(jsonPath("title").value(menuGroup.getTitle()))
                .andExpect(jsonPath("description").value(menuGroup.getDescription()))
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
    }

    @Test
    @DisplayName("존재하지 않는 메뉴 그룹을 단건 조회하면 상태코드 404 응답이 발생한다.")
    void findMenuGroupNotFoundTest() throws Exception {
        long wrongMenuGroupId = -1L;

        mockMvc.perform(get("/api/v1/menu-groups/{menuGroupId}", wrongMenuGroupId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("레스토랑의 메뉴 그룹을 조회한다.")
    void findMenuGroupListOkTest() throws Exception {
        List<MenuGroupResponse> menuGroupResponses = savedMenuGroups.stream()
                .map(menuGroup -> new MenuGroupResponse(menuGroup.getId(), menuGroup.getTitle(),
                        menuGroup.getDescription()))
                .collect(Collectors.toList());

        mockMvc.perform(
                        get("/api/v1/restaurant/{restaurantId}/menu-groups", savedRestaurant.getId())
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
    }

    @Test
    @DisplayName("존재하지 않는 레스토랑의 메뉴 그룹을 조회하면 상태코드 404 응답이 발생한다.")
    void findMenuGroupListNotFoundRestaurantTest() throws Exception {
        long wrongRestaurantId = -1L;
        mockMvc.perform(get("/api/v1/restaurant/{restaurantId}/menu-groups", wrongRestaurantId)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("메뉴 그룹 정보를 업데이트 한다.")
    void updateMenuGroupOkTest() throws Exception {
        MenuGroup menuGroup = savedMenuGroups.get(0);
        MenuGroupUpdateRequest menuGroupUpdateRequest = new MenuGroupUpdateRequest("사이드류",
                "맛있는 사이드 메뉴");

        mockMvc.perform(patch("/api/v1/menu-groups/{menuGroupId}", menuGroup.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuGroupUpdateRequest)))
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
    }

    @Test
    @DisplayName("메뉴 그룹 정보를 업데이트하는데 메뉴명을 누락하면 상태코드 400 응답이 발생한다.")
    void updateMenuGroupEmptyTitleTest() throws Exception {
        MenuGroup menuGroup = savedMenuGroups.get(0);
        MenuGroupUpdateRequest menuGroupUpdateRequest = new MenuGroupUpdateRequest("",
                "맛있는 사이드 메뉴");

        mockMvc.perform(patch("/api/v1/menu-groups/{menuGroupId}",menuGroup.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuGroupUpdateRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("존재하지 않는 메뉴 그룹 정보를 업데이트하려 하면 상태코드 404 응답이 발생한다.")
    void updateMenuGroupNotFoundTest() throws Exception {
        long wrongMenuGroupId = -1L;
        MenuGroupUpdateRequest menuGroupUpdateRequest = new MenuGroupUpdateRequest("사이드류",
                "맛있는 사이드 메뉴");

        mockMvc.perform(patch("/api/v1/menu-groups/{menuGroupId}", wrongMenuGroupId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuGroupUpdateRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("메뉴 그룹을 삭제한다.")
    void deleteMenuGroupOkTest() throws Exception {
        MenuGroup menuGroup = savedMenuGroups.get(0);

        mockMvc.perform(delete("/api/v1/menu-groups/{menuGroupId}", menuGroup.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("delete-menu-group",
                        pathParameters(
                                parameterWithName("menuGroupId").description("삭제할 메뉴그룹 ID")
                        )
                ));
    }

    @Test
    @DisplayName("존재하지 않는 메뉴 그룹을 삭제하려 하면 상태코드 404 응답이 발생한다.")
    void deleteMenuGroupNotFoundTest() throws Exception {
        long wrongMenuGroupId = -1L;

        mockMvc.perform(delete("/api/v1/menu-groups/{menuGroupId}", wrongMenuGroupId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}