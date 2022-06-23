package com.example.woowa.customer.service;

import com.example.woowa.customer.converter.CustomerAddressConverter;
import com.example.woowa.customer.dto.CreateCustomerAddressDto;
import com.example.woowa.customer.dto.CustomerAddressDto;
import com.example.woowa.customer.dto.UpdateCustomerAddressDto;
import com.example.woowa.customer.entity.Customer;
import com.example.woowa.customer.entity.CustomerAddress;
import com.example.woowa.customer.repository.CustomerAddressRepository;
import com.example.woowa.customer.repository.CustomerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomerAddressService {
    private final CustomerAddressRepository customerAddressRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public CustomerAddressDto createCustomerAddress(String loginId, CreateCustomerAddressDto createCustomerAddressDto) {
        Customer customer = findCustomerEntity(loginId);
        CustomerAddress customerAddress = CustomerAddressConverter.toCustomerAddress(createCustomerAddressDto, customer);
        customerAddress = customerAddressRepository.save(customerAddress);
        customer.addCustomerAddress(customerAddress);
        return CustomerAddressConverter.toCustomerAddressDto(customerAddress);
    }

    public List<CustomerAddressDto> findCustomerAddress(String loginId) {
        Customer customer = findCustomerEntity(loginId);
        if (customer.getCustomerAddresses().isEmpty()) {
            return new ArrayList<>();
        }
        return customer.getCustomerAddresses().stream().map(CustomerAddressConverter::toCustomerAddressDto).collect(
                Collectors.toList());
    }

    @Transactional
    public CustomerAddressDto updateCustomerAddress(String loginId, Long addressId, UpdateCustomerAddressDto updateCustomerAddressDto) {
        Customer customer = findCustomerEntity(loginId);
        CustomerAddress customerAddress = customer.getCustomerAddresses().stream().filter((e) -> e.getId().equals(addressId)).findFirst().orElseThrow(() -> new RuntimeException("customer address not existed"));
        if (updateCustomerAddressDto.getAddress() != null) {
            customerAddress.setAddress(updateCustomerAddressDto.getAddress());
        }
        if (updateCustomerAddressDto.getNickname() != null) {
            customerAddress.setNickname(updateCustomerAddressDto.getNickname());
        }
        return CustomerAddressConverter.toCustomerAddressDto(customerAddress);
    }

    public void deleteCustomerAddress(String loginId, Long addressId) {
        Customer customer = findCustomerEntity(loginId);
        CustomerAddress customerAddress = customer.getCustomerAddresses().stream().filter((e) -> e.getId().equals(addressId)).findFirst().orElseThrow(() -> new RuntimeException("customer address not existed"));
        customer.removeCustomerAddress(customerAddress);
        customerAddressRepository.delete(customerAddress);
    }

    @Transactional(readOnly = true)
    public Customer findCustomerEntity(String loginId) {
        Customer customer = customerRepository.findByLoginId(loginId).orElseThrow(() -> new RuntimeException("login id not existed"));
        return customer;
    }
}

