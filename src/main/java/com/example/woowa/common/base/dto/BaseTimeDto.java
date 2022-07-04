package com.example.woowa.common.base.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BaseTimeDto {

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;
}
