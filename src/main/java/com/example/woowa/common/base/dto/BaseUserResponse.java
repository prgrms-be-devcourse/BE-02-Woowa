package com.example.woowa.common.base.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class BaseUserResponse extends BaseTimeDto {

    private final String loginId;
    private final String password;
    private final String name;
    private final String phoneNumber;
    private final LocalDateTime lastLoginedAt;

    public BaseUserResponse(LocalDateTime createdAt, LocalDateTime updatedAt, String loginId, String password, String name,
        String phoneNumber, LocalDateTime lastLoginedAt) {
        super(createdAt, updatedAt);
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.lastLoginedAt = lastLoginedAt;
    }
}