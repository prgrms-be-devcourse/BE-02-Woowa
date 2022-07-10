package com.example.woowa.customer.customer.service;

import com.example.woowa.customer.customer.converter.CustomerMapper;
import com.example.woowa.customer.customer.dto.CustomerAddressCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerAddressFindResponse;
import com.example.woowa.customer.customer.dto.CustomerAddressUpdateRequest;
import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.entity.CustomerAddress;
import com.example.woowa.customer.customer.repository.CustomerAddressRepository;
import com.example.woowa.delivery.entity.AreaCode;
import com.example.woowa.delivery.service.AreaCodeService;
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
    private final AreaCodeService areaCodeService;
    private final CustomerMapper customerMapper;

    @Transactional
    public CustomerAddressFindResponse createCustomerAddress(String loginId, CustomerAddressCreateRequest customerAddressCreateRequest) {
        Customer customer = customerService.findCustomerEntity(loginId);
        AreaCode areaCode = areaCodeService.findByAddress(customerAddressCreateRequest.getDefaultAddress());
        CustomerAddress customerAddress = customerMapper.toCustomerAddress(areaCode, customerAddressCreateRequest, customer);
        customerAddress = customerAddressRepository.save(customerAddress);
        customer.addCustomerAddress(customerAddress);
        return customerMapper.toCustomerAddressDto(customerAddress);
    }

    public List<CustomerAddressFindResponse> findCustomerAddresses(String loginId) {
        Customer customer = customerService.findCustomerEntity(loginId);;
        if (customer.getCustomerAddresses().isEmpty()) {
            return new ArrayList<>();
        }
        return customer.getCustomerAddresses().stream().map(customerMapper::toCustomerAddressDto).collect(
            Collectors.toList());
    }

    @Transactional
    public CustomerAddressFindResponse updateCustomerAddress(Long id, CustomerAddressUpdateRequest customerAddressUpdateRequest) {
        CustomerAddress customerAddress = findCustomerAddressEntity(id);
        if ((customerAddressUpdateRequest.getDefaultAddress() != null) && (customerAddressUpdateRequest.getDetailAddress() != null)) {
            AreaCode areaCode = areaCodeService.findByAddress(customerAddressUpdateRequest.getDefaultAddress());
            customerAddress.setAddress(areaCode, customerAddressUpdateRequest.getDetailAddress());
        }
        if (customerAddressUpdateRequest.getNickname() != null) {
            customerAddress.setNickname(customerAddressUpdateRequest.getNickname());
        }
        return customerMapper.toCustomerAddressDto(customerAddress);
    }

    @Transactional
    public void deleteCustomerAddress(String loginId, Long id) {
        Customer customer = customerService.findCustomerEntity(loginId);
        CustomerAddress customerAddress = findCustomerAddressEntity(id);
        customer.removeCustomerAddress(customerAddress);
        customerAddressRepository.delete(customerAddress);
    }

    private CustomerAddress findCustomerAddressEntity(Long id) {
        return customerAddressRepository.findById(id).orElseThrow(()-> new RuntimeException("customer address not existed"));
    }
}

