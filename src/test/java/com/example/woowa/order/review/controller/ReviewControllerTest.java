package com.example.woowa.order.review.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
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
import com.example.woowa.order.review.dto.ReviewCreateRequest;
import com.example.woowa.order.review.dto.ReviewFindResponse;
import com.example.woowa.order.review.dto.ReviewUpdateRequest;
import com.example.woowa.order.review.service.ReviewService;
import com.example.woowa.security.configuration.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
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

@AutoConfigureRestDocs
@WebMvcTest(value = ReviewController.class, excludeFilters = {
    @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = SecurityConfig.class
    ),
})
@Import(RestDocsConfiguration.class)
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser
class ReviewControllerTest {
  @Autowired
  MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private ReviewService reviewService;

  @Test
  void createReview() throws Exception {
    ReviewFindResponse reviewFindResponse = new ReviewFindResponse(1l, "???????????? ???????????????.", 5);

    given(reviewService.createReview(anyString(), anyLong(), any())).willReturn(reviewFindResponse);
    ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest(reviewFindResponse.getContent(),
        reviewFindResponse.getScoreType());

    mockMvc.perform(
            post("/api/v1/reviews/{loginId}/{orderId}", "dev12", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewCreateRequest))
                .with(csrf().asHeader())
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("reviews-create",
            pathParameters(
                parameterWithName("loginId").description("?????? ????????? ID"),
                parameterWithName("orderId").description("????????? ?????? ID")
            ),
            requestFields(
                fieldWithPath("content").type(JsonFieldType.STRING).description("?????? ??????"),
                fieldWithPath("scoreType").type(JsonFieldType.NUMBER).description("??????")
            ),
            responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("????????? ?????? ?????????"),
                fieldWithPath("content").type(JsonFieldType.STRING).description("?????? ??????"),
                fieldWithPath("scoreType").type(JsonFieldType.NUMBER).description("??????")
            )
        ));
  }

  @Test
  void findReview() throws Exception {
    ReviewFindResponse reviewFindResponse = new ReviewFindResponse(1l, "???????????? ???????????????.", 5);

    given(reviewService.findReview(anyLong())).willReturn(reviewFindResponse);

    mockMvc.perform(
            get("/api/v1/reviews/{id}", reviewFindResponse.getId())
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("reviews-find",
            pathParameters(
                parameterWithName("id").description("????????? ?????? ID")
            ),
            responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("????????? ?????? ?????????"),
                fieldWithPath("content").type(JsonFieldType.STRING).description("?????? ??????"),
                fieldWithPath("scoreType").type(JsonFieldType.NUMBER).description("??????")
            )
        ));
  }

  @Test
  void findUserReview() throws Exception {
    List<ReviewFindResponse> result = new ArrayList<>();
    ReviewFindResponse reviewFindResponse1 = new ReviewFindResponse(1l, "???????????? ???????????????.", 5);
    ReviewFindResponse reviewFindResponse2 = new ReviewFindResponse(2l, "???????????? ???????????????.", 1);
    result.add(reviewFindResponse1);
    result.add(reviewFindResponse2);

    given(reviewService.findUserReview(anyString())).willReturn(result);

    mockMvc.perform(
            get("/api/v1/reviews/user/{loginId}", "dev12")
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("reviews-user-find",
            pathParameters(
                parameterWithName("loginId").description("?????? ????????? ID")
            ),
            responseFields(
                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("????????? ?????? ?????????"),
                fieldWithPath("[].content").type(JsonFieldType.STRING).description("?????? ??????"),
                fieldWithPath("[].scoreType").type(JsonFieldType.NUMBER).description("??????")
            )
        ));
  }

  @Test
  void updateReview() throws Exception {
    ReviewFindResponse reviewFindResponse = new ReviewFindResponse(1l, "???????????? ???????????????.", 5);

    given(reviewService.updateReview(anyLong(), any())).willReturn(reviewFindResponse);
    ReviewUpdateRequest reviewUpdateRequest = new ReviewUpdateRequest("???????????? ???????????????.", 1);

    mockMvc.perform(
            put("/api/v1/reviews/{id}", reviewFindResponse.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewUpdateRequest))
                .with(csrf().asHeader())
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("reviews-update",
            pathParameters(
                parameterWithName("id").description("????????? ?????? ID")
            ),
            requestFields(
                fieldWithPath("content").type(JsonFieldType.STRING).description("?????? ??????"),
                fieldWithPath("scoreType").type(JsonFieldType.NUMBER).description("??????")
            ),
            responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("????????? ?????? ?????????"),
                fieldWithPath("content").type(JsonFieldType.STRING).description("?????? ??????"),
                fieldWithPath("scoreType").type(JsonFieldType.NUMBER).description("??????")
            )
        ));
  }

  @Test
  void deleteReview() throws Exception {
    mockMvc.perform(
            delete("/api/v1/reviews/{loginId}/{id}","dev12", 1)
                .with(csrf().asHeader())
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("reviews-delete",
            pathParameters(
                parameterWithName("loginId").description("????????? ????????? ?????? ????????? ????????? ID"),
                parameterWithName("id").description("????????? ?????? ID")
            )
        ));
  }
}