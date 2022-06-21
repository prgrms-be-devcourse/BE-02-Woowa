package com.example.woowa.customer.dto;

import com.example.woowa.review.dto.ReviewDto;
import com.example.woowa.voucher.dto.VoucherDto;
import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomerDto {
  private String loginId;
  private String birthdate;
  private Integer orderPerMonth;
  private Integer point;
  private @Valid CustomerGradeDto customerGrade;
  private List<@Valid ReviewDto> reviews;
  private List<@Valid CustomerAddressDto> customerAddresses;
  private List<@Valid VoucherDto> vouchers;
}
