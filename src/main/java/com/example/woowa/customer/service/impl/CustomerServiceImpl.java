package com.example.woowa.customer.service.impl;

import com.example.woowa.customer.converter.CustomerConverter;
import com.example.woowa.customer.dto.CreateCustomerDto;
import com.example.woowa.customer.dto.CustomerDto;
import com.example.woowa.customer.dto.UpdateCustomerDto;
import com.example.woowa.customer.entity.Customer;
import com.example.woowa.customer.entity.CustomerGrade;
import com.example.woowa.customer.repository.CustomerRepository;
import com.example.woowa.customer.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {
  private CustomerRepository customerRepository;

  @Override
  public CustomerDto createCustomer(CustomerGrade defaultGrade, CreateCustomerDto createCustomerDto) {
    Customer customer = CustomerConverter.toCustomer(createCustomerDto, defaultGrade);
    return CustomerConverter.toCustomerDto(customerRepository.save(customer));
  }

  @Override
  @Transactional(readOnly = true)
  public CustomerDto readCustomer(String loginId) {
    Customer customer = login(loginId);
    return CustomerConverter.toCustomerDto(customer);
  }

  @Override
  public CustomerDto updateCustomer(String loginId, UpdateCustomerDto updateCustomerDto) {
    Customer customer = login(loginId);
    if (updateCustomerDto.getLoginPassword() != null) {
      customer.setLoginPassword(updateCustomerDto.getLoginPassword());
    }
    return CustomerConverter.toCustomerDto(customer);
  }

  @Override
  public void deleteCustomer(String loginId) {
    Customer customer = login(loginId);
    customerRepository.delete(customer);
  }

  @Override
  public void updateCustomerGrade(CustomerGrade customerGrade, String loginId) {
    Customer customer = login(loginId);
    customer.setCustomerGrade(customerGrade);
  }

  @Override
  @Transactional(readOnly = true)
  public Customer login(String loginId) {
    Customer customer = customerRepository.findByLoginId(loginId).orElseThrow(()-> new RuntimeException("login id not existed"));
    return customer;
  }
}
