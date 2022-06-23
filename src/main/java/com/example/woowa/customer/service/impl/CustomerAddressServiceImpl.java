package com.example.woowa.customer.service.impl;

import com.example.woowa.customer.converter.CustomerAddressConverter;
import com.example.woowa.customer.dto.CreateCustomerAddressDto;
import com.example.woowa.customer.dto.CustomerAddressDto;
import com.example.woowa.customer.dto.UpdateCustomerAddressDto;
import com.example.woowa.customer.entity.Customer;
import com.example.woowa.customer.entity.CustomerAddress;
import com.example.woowa.customer.repository.CustomerAddressRepository;
import com.example.woowa.customer.service.CustomerAddressService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class CustomerAddressServiceImpl implements CustomerAddressService {
  private CustomerAddressRepository customerAddressRepository;

  @Override
  @Transactional
  public CustomerAddressDto createCustomerAddress(Customer customer, CreateCustomerAddressDto createCustomerAddressDto) {
    CustomerAddress customerAddress = CustomerAddressConverter.toCustomerAddress(createCustomerAddressDto, customer);
    customerAddress = customerAddressRepository.save(customerAddress);
    customer.addCustomerAddress(customerAddress);
    return CustomerAddressConverter.toCustomerAddressDto(customerAddress);
  }

  @Override
  public List<CustomerAddressDto> readCustomerAddress(Customer customer) {
    if (customer.getCustomerAddresses().isEmpty()) {
      return new ArrayList<>();
    }
    return customer.getCustomerAddresses().stream().map(CustomerAddressConverter::toCustomerAddressDto).collect(
        Collectors.toList());
  }

  @Override
  @Transactional
  public CustomerAddressDto updateCustomerAddress(Customer customer, Long addressId, UpdateCustomerAddressDto updateCustomerAddressDto) {
    CustomerAddress customerAddress = customer.getCustomerAddresses().stream().filter((e) -> e.getId().equals(addressId)).findFirst().orElseThrow(()-> new RuntimeException("customer address not existed"));
    if (updateCustomerAddressDto.getAddress() != null) {
      customerAddress.setAddress(updateCustomerAddressDto.getAddress());
    }
    if (updateCustomerAddressDto.getNickname() != null) {
      customerAddress.setNickname(updateCustomerAddressDto.getNickname());
    }
    return CustomerAddressConverter.toCustomerAddressDto(customerAddress);
  }

  @Override
  public void deleteCustomerAddress(Customer customer, Long addressId) {
    CustomerAddress customerAddress = customer.getCustomerAddresses().stream().filter((e) -> e.getId().equals(addressId)).findFirst().orElseThrow(()-> new RuntimeException("customer address not existed"));
    customer.removeCustomerAddress(customerAddress);
    customerAddressRepository.delete(customerAddress);
  }
}

