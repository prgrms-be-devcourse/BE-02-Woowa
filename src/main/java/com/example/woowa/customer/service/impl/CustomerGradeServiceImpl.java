package com.example.woowa.customer.service.impl;

import com.example.woowa.customer.converter.CustomerGradeConverter;
import com.example.woowa.customer.dto.CreateCustomerGradeDto;
import com.example.woowa.customer.dto.CustomerGradeDto;
import com.example.woowa.customer.dto.UpdateCustomerGradeDto;
import com.example.woowa.customer.entity.CustomerGrade;
import com.example.woowa.customer.repository.CustomerGradeRepository;
import com.example.woowa.customer.service.CustomerGradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomerGradeServiceImpl implements CustomerGradeService {
  private CustomerGradeRepository customerGradeRepository;

  @Override
  @Transactional
  public CustomerGradeDto createCustomerGrade(CreateCustomerGradeDto createCustomerGradeDto) {
    CustomerGrade customerGrade = CustomerGradeConverter.toCustomerGrade(createCustomerGradeDto);
    customerGrade = customerGradeRepository.save(customerGrade);
    return CustomerGradeConverter.toCustomerGradeDto(customerGrade);
  }

  @Override
  @Transactional
  public CustomerGradeDto updateCustomerGrade(UpdateCustomerGradeDto updateCustomerGradeDto) {
    CustomerGrade customerGrade = customerGradeRepository.findByGrade(
        updateCustomerGradeDto.getGrade()).orElseThrow(()-> new RuntimeException("customer grade not existed"));
    if (updateCustomerGradeDto.getGrade() != null) {
      customerGrade.setGrade(updateCustomerGradeDto.getGrade());
    }
    if (updateCustomerGradeDto.getOrderCount() != null) {
      customerGrade.setOrderCount(updateCustomerGradeDto.getOrderCount());
    }
    if (updateCustomerGradeDto.getDiscountPrice() != null) {
      customerGrade.setDiscountPrice(updateCustomerGradeDto.getDiscountPrice());
    }
    if (updateCustomerGradeDto.getVoucherCount() != null) {
      customerGrade.setVoucherCount(updateCustomerGradeDto.getVoucherCount());
    }
    return CustomerGradeConverter.toCustomerGradeDto(customerGrade);
  }

  @Override
  public void deleteCustomerGrade(String grade) {
    CustomerGrade customerGrade = customerGradeRepository.findByGrade(grade).orElseThrow(()-> new RuntimeException("customer grade not existed"));
    customerGradeRepository.delete(customerGrade);
  }

  @Override
  public CustomerGrade findDefaultCustomerGrade() {
    return customerGradeRepository.findFirstByOrderByOrderCount().orElseThrow(()-> new RuntimeException("no customer grade existed"));
  }

  @Override
  public CustomerGrade findCustomerGrade(int orderCount) {
    return customerGradeRepository.findFirstByOrderCountLessThanEqualOrderByOrderCountDesc(orderCount).orElseThrow(()-> new RuntimeException("no customer grade existed"));
  }
}
