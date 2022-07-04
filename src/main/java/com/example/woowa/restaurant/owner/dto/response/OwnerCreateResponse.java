package com.example.woowa.restaurant.owner.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OwnerCreateResponse {

    private final Long id;

    private final String loginId;

    private final String password;

    private final String name;

    private final String phoneNumber;

    private final LocalDateTime createdAt;

}
