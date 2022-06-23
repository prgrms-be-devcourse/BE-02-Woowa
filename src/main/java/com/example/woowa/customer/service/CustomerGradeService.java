package com.example.woowa.customer.service;

import com.example.woowa.customer.dto.CreateCustomerGradeDto;
import com.example.woowa.customer.dto.CustomerGradeDto;
import com.example.woowa.customer.dto.UpdateCustomerGradeDto;

public interface CustomerGradeService {
  CustomerGradeDto createCustomerGrade(CreateCustomerGradeDto createCustomerGradeDto);
  CustomerGradeDto findCustomerGrade(Long id);
  CustomerGradeDto updateCustomerGrade(Long id, UpdateCustomerGradeDto updateCustomerGradeDto);
  void deleteCustomerGrade(Long id);
}
