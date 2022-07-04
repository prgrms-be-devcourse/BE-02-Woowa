package com.example.woowa.delivery.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RiderUpdateRequest {

    @NotBlank(message = "이름을 입력해주세요")
    @Size(max = 10,
        message = "이름은 최대 10자입니다.")
    private final String name;

    @NotBlank(message = "휴대폰번호를 입력해주세요.")
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$",
        message = "휴대폰번호 양식이 일치하지 않습니다.")
    private final String phoneNumber;
}
