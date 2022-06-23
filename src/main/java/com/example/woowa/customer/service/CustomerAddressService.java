package com.example.woowa.customer.service;

import com.example.woowa.customer.dto.CreateCustomerAddressDto;
import com.example.woowa.customer.dto.CustomerAddressDto;
import com.example.woowa.customer.dto.UpdateCustomerAddressDto;
import com.example.woowa.customer.entity.Customer;
import java.util.List;

public interface CustomerAddressService {
  CustomerAddressDto createCustomerAddress(Customer customer, CreateCustomerAddressDto createCustomerAddressDto);
  List<CustomerAddressDto> readCustomerAddress(Customer customer);
  CustomerAddressDto updateCustomerAddress(Customer customer, Long addressId, UpdateCustomerAddressDto updateCustomerAddressDto);
  void deleteCustomerAddress(Customer customer, Long addressId);
}
