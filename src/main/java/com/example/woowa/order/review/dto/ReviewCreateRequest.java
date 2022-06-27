package com.example.woowa.order.review.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
public class ReviewCreateRequest {
    @NotBlank
    @Length(min = 10, max = 500)
    private String content;
    @Min(value = 1)
    @Max(value = 5)
    private Integer scoreType;
}
