package com.example.woowa.customer.service;

import com.example.woowa.customer.dto.CreateCustomerAddressDto;
import com.example.woowa.customer.dto.CustomerAddressDto;
import com.example.woowa.customer.dto.CustomerLoginDto;
import com.example.woowa.customer.dto.UpdateCustomerAddressDto;
import java.util.List;

public interface CustomerAddressService {
  CustomerAddressDto createCustomerAddress(CustomerLoginDto customerLoginDto, CreateCustomerAddressDto createCustomerAddressDto);
  List<CustomerAddressDto> readCustomerAddress(CustomerLoginDto customerLoginDto);
  CustomerAddressDto updateCustomerAddress(CustomerLoginDto customerLoginDto, UpdateCustomerAddressDto updateCustomerAddressDto);
  void deleteCustomerAddress(CustomerLoginDto customerLoginDto,String nickname);
}
