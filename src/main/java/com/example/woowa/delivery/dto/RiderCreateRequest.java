package com.example.woowa.delivery.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class RiderCreateRequest {

    @NotBlank(message = "loginId를 입력해 주세요.")
    @Pattern(regexp = "[a-zA-Z0-9]{10,20}", message = "id는 영어 대소문자와 숫자만 사용할 수 있습니다.(길이 : 10 ~ 20)")
    private final String loginId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$",
        message = "비밀번호는 8 ~ 16자에 영어 대소문자와 숫자 특수문자를 사용하여야 합니다.")
    private final String password;

    @NotBlank(message = "이름을 입력해주세요")
    @Size(max = 10, message = "이름은 최대 10자입니다.")
    private final String name;

    @NotBlank(message = "휴대폰번호를 입력해주세요.")
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$",
        message = "휴대폰번호 양식이 일치하지 않습니다.")
    private final String phoneNumber;
}
