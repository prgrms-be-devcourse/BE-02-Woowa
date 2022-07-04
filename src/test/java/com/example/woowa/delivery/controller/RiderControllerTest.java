package com.example.woowa.delivery.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.woowa.delivery.dto.RiderCreateRequest;
import com.example.woowa.delivery.mapper.RiderMapper;
import com.example.woowa.delivery.repository.RiderRepository;
import com.example.woowa.delivery.service.AreaCodeService;
import com.example.woowa.delivery.service.RiderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class RiderControllerTest {

    final String CONSTRAIN = "constraints";
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @InjectMocks
    RiderService riderService;
    @Mock
    RiderRepository riderRepository;
    @Mock
    AreaCodeService areaCodeService;
    @Mock
    RiderMapper riderMapper = Mappers.getMapper(RiderMapper.class);

    @Test
    void sign() throws Exception {
        RiderCreateRequest riderCreateRequest = new RiderCreateRequest("testId1234", "passwordA1!",
            "name",
            "010-1234-5678");
        ConstraintDescriptions riderCreateRequestConstraints = new ConstraintDescriptions(
            RiderCreateRequest.class);

        mockMvc.perform(post("/api/v1/rider")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(riderCreateRequest)))
            .andExpect(status().isCreated())
            .andDo(print())
            .andDo(document("sign-rider",
                    requestFields(
                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 ID")
                            .attributes(key(CONSTRAIN).value(
                                riderCreateRequestConstraints.descriptionsForProperty("loginId"))),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("패스워드")
                            .attributes(key(CONSTRAIN).value(
                                riderCreateRequestConstraints.descriptionsForProperty("password"))),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("사용자 이름")
                            .attributes(key(CONSTRAIN).value(
                                riderCreateRequestConstraints.descriptionsForProperty("name"))),
                        fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("휴대 전화")
                            .attributes(key(CONSTRAIN).value(
                                riderCreateRequestConstraints.descriptionsForProperty("phoneNumber")))
                    )
                )
            );
    }

    @Test
    void pagingRider() {
    }

    @Test
    void getRider() {
    }

    @Test
    void userUpdate() {
    }

    @Test
    void changeIsDelivery() {
    }

    @Test
    void addArea() {
    }
}