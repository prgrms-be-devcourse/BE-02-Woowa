package com.example.woowa.customer.service;

import com.example.woowa.customer.dto.CreateCustomerDto;
import com.example.woowa.customer.dto.CustomerDto;
import com.example.woowa.customer.dto.UpdateCustomerDto;
import com.example.woowa.customer.entity.Customer;
import com.example.woowa.customer.entity.CustomerGrade;

public interface CustomerService {
  CustomerDto createCustomer(CreateCustomerDto createCustomerDto);
  CustomerDto findCustomer(String loginId);
  CustomerDto updateCustomer(String loginId, UpdateCustomerDto updateCustomerDto);
  void deleteCustomer(String loginId);
  void updateCustomerGrade(String loginId);
  Customer findCustomerEntity(String loginId);
  CustomerGrade findDefaultCustomerGrade();
  CustomerGrade findCustomerGrade(int orderCount);
}
