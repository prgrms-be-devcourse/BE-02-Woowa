package com.example.woowa.restaurant.owner.dto.request;

import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OwnerUpdateRequest {

    @Pattern(regexp = "[A-Za-z_][A-Za-z0-9_`~!@#$%^&*()-+=><.,?/]{10,}",
        message = "비밀번호의 첫문자는 영문 대/소문자여야 합니다. 또한 특수문자를 포함할 수 있으며 길이는 최소 10자이상이어야 합니다.")
    private final String password;

    @Pattern(regexp = "[가-힣]{1,10}", message = "이름은 최대 10자이며 공백을 포함할 수 없습니다.")
    private final String name;

    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "올바른 휴대 전화번호 형식이어야 합니다.")
    private final String phoneNumber;

}
