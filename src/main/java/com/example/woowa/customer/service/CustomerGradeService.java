package com.example.woowa.customer.service;

import com.example.woowa.customer.dto.CreateCustomerGradeDto;
import com.example.woowa.customer.dto.CustomerGradeDto;
import com.example.woowa.customer.dto.UpdateCustomerGradeDto;
import com.example.woowa.customer.entity.CustomerGrade;

public interface CustomerGradeService {
  CustomerGradeDto createCustomerGrade(CreateCustomerGradeDto createCustomerGradeDto);
  CustomerGradeDto readCustomerGrade(Long id);
  CustomerGradeDto updateCustomerGrade(Long id, UpdateCustomerGradeDto updateCustomerGradeDto);
  void deleteCustomerGrade(Long id);
  CustomerGrade findDefaultCustomerGrade();
  CustomerGrade findCustomerGrade(int orderCount);
}
