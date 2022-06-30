package com.example.woowa.restaurant.menugroup.controller;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
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

        mockMvc.perform(post("/api/v1/restaurant/" + savedRestaurant.getId() + "/menu-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuGroupSaveRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION));
    }

    @Test
    @DisplayName("메뉴 그룹명을 누락하면 상태코드 400 응답이 발생한다.")
    void addMenuGroupEmptyTitleTest() throws Exception {
        MenuGroupSaveRequest menuGroupSaveRequest = new MenuGroupSaveRequest("", "맛있는 볶음밥");

        mockMvc.perform(post("/api/v1/restaurant/" + savedRestaurant.getId() + "/menu-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuGroupSaveRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("존재하지 않는 레스토랑에 메뉴 그룹을 추가하려 하면 상태코드 400 응답이 발생한다.")
    void addMenuGroupNotFoundRestaurantTest() throws Exception {
        MenuGroupSaveRequest menuGroupSaveRequest = new MenuGroupSaveRequest("볶음밥류", "맛있는 볶음밥");
        long wrongRestaurantId = -1L;

        mockMvc.perform(post("/api/v1/restaurant/" + wrongRestaurantId + "/menu-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuGroupSaveRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("메뉴 그룹을 단건 조회한다.")
    void findMenuGroupOkTest() throws Exception {
        MenuGroup menuGroup = savedMenuGroups.get(0);

        mockMvc.perform(get("/api/v1/menu-groups/" + menuGroup.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(menuGroup.getId()))
                .andExpect(jsonPath("title").value(menuGroup.getTitle()))
                .andExpect(jsonPath("description").value(menuGroup.getDescription()));
    }

    @Test
    @DisplayName("존재하지 않는 메뉴 그룹을 단건 조회하면 상태코드 400 응답이 발생한다.")
    void findMenuGroupNotFoundTest() throws Exception {
        long wrongMenuGroupId = -1L;

        mockMvc.perform(get("/api/v1/menu-groups/" + wrongMenuGroupId))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("레스토랑의 메뉴 그룹을 조회한다.")
    void findMenuGroupListOkTest() throws Exception {
        List<MenuGroupResponse> menuGroupResponses = savedMenuGroups.stream()
                .map(menuGroup -> new MenuGroupResponse(menuGroup.getId(), menuGroup.getTitle(),
                        menuGroup.getDescription()))
                .collect(Collectors.toList());

        mockMvc.perform(get("/api/v1/restaurant/" + savedRestaurant.getId() + "/menu-groups")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("menuGroups.length()").value(menuGroupResponses.size()));
    }

    @Test
    @DisplayName("존재하지 않는 레스토랑의 메뉴 그룹을 조회하면 상태코드 400 응답이 발생한다.")
    void findMenuGroupListNotFoundRestaurantTest() throws Exception {
        long wrongRestaurantId = -1L;
        mockMvc.perform(get("/api/v1/restaurant/" + wrongRestaurantId + "/menu-groups")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("메뉴 그룹 정보를 업데이트 한다.")
    void updateMenuGroupOkTest() throws Exception {
        MenuGroup menuGroup = savedMenuGroups.get(0);
        MenuGroupUpdateRequest menuGroupUpdateRequest = new MenuGroupUpdateRequest("사이드류",
                "맛있는 사이드 메뉴");

        mockMvc.perform(patch("/api/v1/menu-groups/" + menuGroup.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuGroupUpdateRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("메뉴 그룹 정보를 업데이트하는데 메뉴명을 누락하면 상태코드 400 응답이 발생한다.")
    void updateMenuGroupEmptyTitleTest() throws Exception {
        MenuGroup menuGroup = savedMenuGroups.get(0);
        MenuGroupUpdateRequest menuGroupUpdateRequest = new MenuGroupUpdateRequest("",
                "맛있는 사이드 메뉴");

        mockMvc.perform(patch("/api/v1/menu-groups/" + menuGroup.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuGroupUpdateRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("존재하지 않는 메뉴 그룹 정보를 업데이트하려 하면 상태코드 400 응답이 발생한다.")
    void updateMenuGroupNotFoundTest() throws Exception {
        long wrongMenuGroupId = -1L;
        MenuGroupUpdateRequest menuGroupUpdateRequest = new MenuGroupUpdateRequest("사이드류",
                "맛있는 사이드 메뉴");

        mockMvc.perform(patch("/api/v1/menu-groups/" + wrongMenuGroupId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuGroupUpdateRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("메뉴 그룹을 삭제한다.")
    void deleteMenuGroupOkTest() throws Exception {
        MenuGroup menuGroup = savedMenuGroups.get(0);

        mockMvc.perform(delete("/api/v1/menu-groups/" + menuGroup.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("존재하지 않는 메뉴 그룹을 삭제하려 하면 상태코드 400 응답이 발생한다.")
    void deleteMenuGroupNotFoundTest() throws Exception {
        long wrongMenuGroupId = -1L;

        mockMvc.perform(delete("/api/v1/menu-groups/" + wrongMenuGroupId))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}