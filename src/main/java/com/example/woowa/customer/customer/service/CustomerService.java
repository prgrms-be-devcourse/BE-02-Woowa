package com.example.woowa.customer.customer.service;

import com.example.woowa.common.exception.ErrorMessage;
import com.example.woowa.common.exception.NotFoundException;
import com.example.woowa.customer.customer.converter.CustomerMapper;
import com.example.woowa.customer.customer.dto.CustomerCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerFindResponse;
import com.example.woowa.customer.customer.dto.CustomerUpdateRequest;
import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.entity.CustomerAddress;
import com.example.woowa.customer.customer.repository.CustomerAddressRepository;
import com.example.woowa.customer.customer.repository.CustomerRepository;
import com.example.woowa.delivery.entity.AreaCode;
import com.example.woowa.delivery.service.AreaCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerAddressRepository customerAddressRepository;
    private final CustomerRepository customerRepository;
    private final CustomerGradeService customerGradeService;
    private final AreaCodeService areaCodeService;
    private final CustomerMapper customerMapper;

    @Transactional
    public CustomerFindResponse createCustomer(CustomerCreateRequest customerCreateRequest) {
        Customer customer = customerMapper.toCustomer(customerCreateRequest, customerGradeService.findDefaultCustomerGrade());
        customerRepository.save(customer);
        AreaCode areaCode = areaCodeService.findByAddress(customerCreateRequest.getAddress().getDefaultAddress());
        CustomerAddress customerAddress = customerMapper.toCustomerAddress(areaCode, customerCreateRequest.getAddress(), customer);
        customerAddressRepository.save(customerAddress);
        customer.addCustomerAddress(customerAddress);
        return customerMapper.toCustomerDto(customer);
    }

    public CustomerFindResponse findCustomer(String loginId) {
        Customer customer = findCustomerEntity(loginId);
        return customerMapper.toCustomerDto(customer);
    }

    @Transactional
    public CustomerFindResponse updateCustomer(String loginId, CustomerUpdateRequest customerUpdateRequest) {
        Customer customer = findCustomerEntity(loginId);
        if (customerUpdateRequest.getLoginPassword() != null) {
            customer.changePassword(customerUpdateRequest.getLoginPassword());
        }
        return customerMapper.toCustomerDto(customer);
    }

    @Transactional
    public void deleteCustomer(String loginId) {
        Customer customer = findCustomerEntity(loginId);
        customerRepository.delete(customer);
    }

    public Customer findCustomerEntity(String loginId) {
        Customer customer = customerRepository.findByLoginId(loginId).orElseThrow(() -> new NotFoundException(
                ErrorMessage.NOT_FOUND_CUSTOMER.getMessage()));
        return customer;
    }

    @Transactional
    public void updateCustomerGrade(String loginId) {
        Customer customer = findCustomerEntity(loginId);
        customer.setCustomerGrade(customerGradeService.findCustomerGradeByOrderPerMonthCount(customer.getOrderPerMonth()));
    }

    @Transactional
    public CustomerFindResponse updateCustomerStatusOnFirstDay(String loginId) {
        Customer customer = findCustomerEntity(loginId);
        customer.updateCustomerStatusOnFirstDay();
        return customerMapper.toCustomerDto(customer);
    }
}