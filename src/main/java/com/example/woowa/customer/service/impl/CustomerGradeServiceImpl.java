package com.example.woowa.customer.service.impl;

import com.example.woowa.customer.converter.CustomerGradeConverter;
import com.example.woowa.customer.dto.CreateCustomerGradeDto;
import com.example.woowa.customer.dto.CustomerGradeDto;
import com.example.woowa.customer.dto.UpdateCustomerGradeDto;
import com.example.woowa.customer.entity.CustomerGrade;
import com.example.woowa.customer.repository.CustomerGradeRepository;
import com.example.woowa.customer.service.CustomerGradeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
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
  public CustomerGradeDto readCustomerGrade(Long id) {
    CustomerGrade customerGrade = customerGradeRepository.findById(id).orElseThrow(()-> new RuntimeException("customer grade not existed"));
    return CustomerGradeConverter.toCustomerGradeDto(customerGrade);
  }

  @Override
  @Transactional
  public CustomerGradeDto updateCustomerGrade(Long id, UpdateCustomerGradeDto updateCustomerGradeDto) {
    CustomerGrade customerGrade = customerGradeRepository.findById(id).orElseThrow(()-> new RuntimeException("customer grade not existed"));
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
  public void deleteCustomerGrade(Long id) {
    CustomerGrade customerGrade = customerGradeRepository.findById(id).orElseThrow(()-> new RuntimeException("customer grade not existed"));
    customerGradeRepository.delete(customerGrade);
  }

  @Override
  public CustomerGrade findDefaultCustomerGrade() {
    return customerGradeRepository.findFirstByOrderByOrderCount().orElseThrow(()-> new RuntimeException("no customer grade existed"));
  }

  @Override
  public CustomerGrade findCustomerGrade(int orderCount) {
    try {
      return customerGradeRepository.findFirstByOrderCountLessThanEqualOrderByOrderCountDesc(orderCount).orElseThrow(()-> new RuntimeException("no customer grade existed"));
    }
    catch (Exception e) {
     return findDefaultCustomerGrade();
    }
  }
}
