package com.example.woowa.customer.customer.service;

import com.example.woowa.customer.customer.converter.CustomerAddressConverter;
import com.example.woowa.customer.customer.converter.CustomerConverter;
import com.example.woowa.customer.customer.dto.CustomerCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerFindResponse;
import com.example.woowa.customer.customer.dto.CustomerUpdateRequest;
import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.entity.CustomerAddress;
import com.example.woowa.customer.customer.repository.CustomerAddressRepository;
import com.example.woowa.customer.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerAddressRepository customerAddressRepository;
    private final CustomerRepository customerRepository;
    private final CustomerGradeService customerGradeService;

    public CustomerFindResponse createCustomer(CustomerCreateRequest customerCreateRequest) {
        Customer customer = CustomerConverter.toCustomer(customerCreateRequest, customerGradeService.findDefaultCustomerGrade());
        customerRepository.save(customer);
        CustomerAddress customerAddress = CustomerAddressConverter.toCustomerAddress(customerCreateRequest.getAddress(), customer);
        customerAddressRepository.save(customerAddress);
        customer.addCustomerAddress(customerAddress);
        return CustomerConverter.toCustomerDto(customer);
    }

    @Transactional(readOnly = true)
    public CustomerFindResponse findCustomer(String loginId) {
        Customer customer = findCustomerEntity(loginId);
        return CustomerConverter.toCustomerDto(customer);
    }

    public CustomerFindResponse updateCustomer(String loginId, CustomerUpdateRequest customerUpdateRequest) {
        Customer customer = findCustomerEntity(loginId);
        if (customerUpdateRequest.getLoginPassword() != null) {
            customer.setLoginPassword(customerUpdateRequest.getLoginPassword());
        }
        return CustomerConverter.toCustomerDto(customer);
    }

    public void deleteCustomer(String loginId) {
        Customer customer = findCustomerEntity(loginId);
        customerRepository.delete(customer);
    }

    @Transactional(readOnly = true)
    public Customer findCustomerEntity(String loginId) {
        Customer customer = customerRepository.findByLoginId(loginId).orElseThrow(() -> new RuntimeException("login id not existed"));
        return customer;
    }

    public void updateCustomerGrade(String loginId) {
        Customer customer = findCustomerEntity(loginId);
        customer.setCustomerGrade(customerGradeService.findCustomerGradeByOrderPerMonthCount(customer.getOrderPerMonth()));
    }

    public CustomerFindResponse updateCustomerStatusOnFirstDay(String loginId) {
        Customer customer = findCustomerEntity(loginId);
        customer.updateCustomerStatusOnFirstDay();
        return CustomerConverter.toCustomerDto(customer);
    }
}