package com.example.woowa.customer.customer.service;

import com.example.woowa.customer.customer.converter.CustomerAddressConverter;
import com.example.woowa.customer.customer.dto.CustomerAddressCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerAddressFindResponse;
import com.example.woowa.customer.customer.dto.CustomerAddressUpdateRequest;
import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.entity.CustomerAddress;
import com.example.woowa.customer.customer.repository.CustomerAddressRepository;
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
    private final CustomerService customerService;

    @Transactional
    public CustomerAddressFindResponse createCustomerAddress(String loginId, CustomerAddressCreateRequest customerAddressCreateRequest) {
        Customer customer = customerService.findCustomerEntity(loginId);
        CustomerAddress customerAddress = CustomerAddressConverter.toCustomerAddress(
            customerAddressCreateRequest, customer);
        customerAddress = customerAddressRepository.save(customerAddress);
        customer.addCustomerAddress(customerAddress);
        return CustomerAddressConverter.toCustomerAddressDto(customerAddress);
    }

    public List<CustomerAddressFindResponse> findCustomerAddresses(String loginId) {
        Customer customer = customerService.findCustomerEntity(loginId);;
        if (customer.getCustomerAddresses().isEmpty()) {
            return new ArrayList<>();
        }
        return customer.getCustomerAddresses().stream().map(CustomerAddressConverter::toCustomerAddressDto).collect(
                Collectors.toList());
    }

    @Transactional
    public CustomerAddressFindResponse updateCustomerAddress(Long id, CustomerAddressUpdateRequest customerAddressUpdateRequest) {
        Customer customer = findCustomerAddressEntity(id).getCustomer();
        CustomerAddress customerAddress = customer.getCustomerAddresses().stream().filter((e) -> e.getId().equals(id)).findFirst().orElseThrow(() -> new RuntimeException("customer address not existed"));
        if ((customerAddressUpdateRequest.getDefaultAddress() != null) && (customerAddressUpdateRequest.getDetailAddress() != null)) {
            customerAddress.setAddress(customerAddressUpdateRequest.getDefaultAddress(),
                customerAddressUpdateRequest.getDetailAddress());
        }
        if (customerAddressUpdateRequest.getNickname() != null) {
            customerAddress.setNickname(customerAddressUpdateRequest.getNickname());
        }
        return CustomerAddressConverter.toCustomerAddressDto(customerAddress);
    }

    @Transactional
    public void deleteCustomerAddress(Long id) {
        Customer customer = findCustomerAddressEntity(id).getCustomer();
        CustomerAddress customerAddress = customer.getCustomerAddresses().stream().filter((e) -> e.getId().equals(id)).findFirst().orElseThrow(() -> new RuntimeException("customer address not existed"));
        customer.removeCustomerAddress(customerAddress);
        customerAddressRepository.delete(customerAddress);
    }

    private CustomerAddress findCustomerAddressEntity(Long id) {
        return customerAddressRepository.findById(id).orElseThrow(()-> new RuntimeException("customer address not existed"));
    }
}

