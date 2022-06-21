package com.example.woowa.customer.service.impl;

import com.example.woowa.customer.converter.CustomerConverter;
import com.example.woowa.customer.dto.CreateCustomerDto;
import com.example.woowa.customer.dto.CustomerDto;
import com.example.woowa.customer.dto.UpdateCustomerDto;
import com.example.woowa.customer.entity.Customer;
import com.example.woowa.customer.entity.CustomerGrade;
import com.example.woowa.customer.repository.CustomerRepository;
import com.example.woowa.customer.service.CustomerGradeService;
import com.example.woowa.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
  private CustomerRepository customerRepository;
  private CustomerGradeService customerGradeService;

  @Override
  @Transactional
  public CustomerDto createCustomer(CreateCustomerDto createCustomerDto) {
    CustomerGrade defaultGrade = customerGradeService.findDefaultCustomerGrade();
    Customer customer = CustomerConverter.toCustomer(createCustomerDto, defaultGrade);
    return CustomerConverter.toCustomerDto(customerRepository.save(customer));
  }

  @Override
  public CustomerDto readCustomer(Long id) {
    Customer customer = customerRepository.findById(id).orElseThrow(()-> new RuntimeException("customer not existed"));
    return CustomerConverter.toCustomerDto(customer);
  }

  @Override
  @Transactional
  public CustomerDto updateCustomer(UpdateCustomerDto updateCustomerDto) {
    Customer customer = customerRepository.findByLoginId(updateCustomerDto.getLoginId()).orElseThrow(()-> new RuntimeException("customer not existed"));
    if (updateCustomerDto.getLoginPassword() != null) {
      customer.setLoginPassword(updateCustomerDto.getLoginPassword());
    }
    return CustomerConverter.toCustomerDto(customer);
  }

  @Override
  public void deleteCustomer(Long id) {
    Customer customer = customerRepository.findById(id).orElseThrow(()-> new RuntimeException("customer not existed"));
    customerRepository.delete(customer);
  }

  @Override
  public void updateCustomerGrade(Long id) {
    Customer customer = customerRepository.findById(id).orElseThrow(()-> new RuntimeException("customer not existed"));
    CustomerGrade customerGrade = customerGradeService.findCustomerGrade(customer.getOrderPerMonth());
    customer.setCustomerGrade(customerGrade);
  }
}
