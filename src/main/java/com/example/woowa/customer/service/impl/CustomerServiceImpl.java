package com.example.woowa.customer.service.impl;

import com.example.woowa.customer.converter.CustomerConverter;
import com.example.woowa.customer.dto.CreateCustomerDto;
import com.example.woowa.customer.dto.CustomerDto;
import com.example.woowa.customer.dto.UpdateCustomerDto;
import com.example.woowa.customer.entity.Customer;
import com.example.woowa.customer.entity.CustomerGrade;
import com.example.woowa.customer.repository.CustomerGradeRepository;
import com.example.woowa.customer.repository.CustomerRepository;
import com.example.woowa.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
  private final CustomerRepository customerRepository;
  private final CustomerGradeRepository customerGradeRepository;

  @Override
  public CustomerDto createCustomer(CreateCustomerDto createCustomerDto) {
    Customer customer = CustomerConverter.toCustomer(createCustomerDto, findDefaultCustomerGrade());
    return CustomerConverter.toCustomerDto(customerRepository.save(customer));
  }

  @Override
  @Transactional(readOnly = true)
  public CustomerDto findCustomer(String loginId) {
    Customer customer = findCustomerEntity(loginId);
    return CustomerConverter.toCustomerDto(customer);
  }

  @Override
  public CustomerDto updateCustomer(String loginId, UpdateCustomerDto updateCustomerDto) {
    Customer customer = findCustomerEntity(loginId);
    if (updateCustomerDto.getLoginPassword() != null) {
      customer.setLoginPassword(updateCustomerDto.getLoginPassword());
    }
    return CustomerConverter.toCustomerDto(customer);
  }

  @Override
  public void deleteCustomer(String loginId) {
    Customer customer = findCustomerEntity(loginId);
    customerRepository.delete(customer);
  }

  @Override
  public void updateCustomerGrade(String loginId) {
    Customer customer = findCustomerEntity(loginId);
    customer.setCustomerGrade(findCustomerGrade(customer.getOrderPerMonth()));
  }

  @Override
  @Transactional(readOnly = true)
  public Customer findCustomerEntity(String loginId) {
    Customer customer = customerRepository.findByLoginId(loginId).orElseThrow(()-> new RuntimeException("login id not existed"));
    return customer;
  }

  @Override
  public CustomerGrade findDefaultCustomerGrade() {
    return customerGradeRepository.findFirstByOrderByOrderCount().orElseThrow(()-> new RuntimeException("no customer grade existed"));
  }

  @Override
  public CustomerGrade findCustomerGrade(int orderCount) {
    return customerGradeRepository.findFirstByOrderCountLessThanEqualOrderByOrderCountDesc(orderCount).orElse(findDefaultCustomerGrade());
  }
}