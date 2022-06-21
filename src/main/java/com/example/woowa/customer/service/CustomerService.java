package com.example.woowa.customer.service;

import com.example.woowa.customer.dto.CreateCustomerDto;
import com.example.woowa.customer.dto.CustomerDto;
import com.example.woowa.customer.dto.UpdateCustomerDto;

public interface CustomerService {
  CustomerDto createCustomer(CreateCustomerDto createCustomerDto);
  CustomerDto readCustomer(Long id);
  CustomerDto updateCustomer(UpdateCustomerDto updateCustomerDto);
  void deleteCustomer(Long id);
  void updateCustomerGrade(Long id);
}
