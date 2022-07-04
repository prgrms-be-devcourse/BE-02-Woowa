package com.example.woowa.admin.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseBody;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.woowa.admin.dto.AdminCreateRequest;
import com.example.woowa.admin.dto.AdminUpdateRequest;
import com.example.woowa.admin.repository.AdminRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@TestInstance(Lifecycle.PER_CLASS)
class AdminControllerTest {
  @Autowired
  MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private AdminRepository adminRepository;

  @BeforeEach
  void setting() {
    adminRepository.deleteAll();
  }

  @AfterAll
  void settingafter() {
    adminRepository.deleteAll();
  }

  @Test
  void createAdmin() throws Exception {
    AdminCreateRequest  adminCreateRequest = new AdminCreateRequest("dev12", "Programmers12!");

    mockMvc.perform(
            post("/api/v1/admins")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adminCreateRequest))
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("admins-create",
            requestFields(
                fieldWithPath("loginId").type(JsonFieldType.STRING).description("아이디"),
                fieldWithPath("loginPassword").type(JsonFieldType.STRING).description("비밀번호")
            ),
            responseFields(
                fieldWithPath("loginId").type(JsonFieldType.STRING).description("생성된 아이디")
            )
        ));
  }

  @Test
  void findAdmin() throws Exception {
    AdminCreateRequest adminCreateRequest = new AdminCreateRequest("dev12", "Programmers12!");

    mockMvc.perform(
            post("/api/v1/admins")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adminCreateRequest))
        );

    mockMvc.perform(
            get("/api/v1/admins/{loginId}",adminCreateRequest.getLoginId())
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("admins-find",
            responseFields(
                fieldWithPath("loginId").type(JsonFieldType.STRING).description("생성된 아이디")
            )
        ));
  }

  @Test
  void updateAdmin() throws Exception {
    AdminCreateRequest adminCreateRequest = new AdminCreateRequest("dev12", "Programmers12!");

    mockMvc.perform(
        post("/api/v1/admins")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(adminCreateRequest))
    );

    AdminUpdateRequest adminUpdateRequest = new AdminUpdateRequest("Programmers123!");

    mockMvc.perform(
            put("/api/v1/admins/{loginId}", adminCreateRequest.getLoginId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adminUpdateRequest))
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("admins-update",
            requestFields(
                fieldWithPath("loginPassword").type(JsonFieldType.STRING).description("수정하려는 비밀 번호")
            ),
            responseFields(
                fieldWithPath("loginId").type(JsonFieldType.STRING).description("정보가 수정된 계정의 아이디")
            )
        ));
  }

  @Test
  void deleteAdmin() throws Exception {
    AdminCreateRequest adminCreateRequest = new AdminCreateRequest("dev12", "Programmers12!");

    mockMvc.perform(
        post("/api/v1/admins")
            .contentType(MediaType.TEXT_PLAIN)
            .content(objectMapper.writeValueAsString(adminCreateRequest))
    );

    mockMvc.perform(
            delete("/api/v1/admins/{loginId}",adminCreateRequest.getLoginId())
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("admins-delete",
            responseBody()
        ));
  }
}