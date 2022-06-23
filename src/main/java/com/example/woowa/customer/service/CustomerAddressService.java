package com.example.woowa.customer.service;

import com.example.woowa.customer.dto.CreateCustomerAddressDto;
import com.example.woowa.customer.dto.CustomerAddressDto;
import com.example.woowa.customer.dto.UpdateCustomerAddressDto;
import com.example.woowa.customer.entity.Customer;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface CustomerAddressService {
  CustomerAddressDto createCustomerAddress(String loginId, CreateCustomerAddressDto createCustomerAddressDto);
  List<CustomerAddressDto> findCustomerAddress(String loginId);
  CustomerAddressDto updateCustomerAddress(String loginId, Long addressId, UpdateCustomerAddressDto updateCustomerAddressDto);
  void deleteCustomerAddress(String loginId, Long addressId);

  @Transactional(readOnly = true)
  Customer findCustomerEntity(String loginId);
}
