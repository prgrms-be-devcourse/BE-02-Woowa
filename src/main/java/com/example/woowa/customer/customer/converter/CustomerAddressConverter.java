package com.example.woowa.customer.customer.converter;

import com.example.woowa.customer.customer.dto.CreateCustomerAddressDto;
import com.example.woowa.customer.customer.dto.CustomerAddressDto;
import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.entity.CustomerAddress;

public class CustomerAddressConverter {
    public static CustomerAddressDto toCustomerAddressDto(CustomerAddress customerAddress) {
        return new CustomerAddressDto(customerAddress.getId(), customerAddress.getAddress(), customerAddress.getNickname());
    }

    public static CustomerAddress toCustomerAddress(CreateCustomerAddressDto createCustomerAddressDto, Customer customer) {
        return new CustomerAddress(createCustomerAddressDto.getAddress(),
                createCustomerAddressDto.getNickname(), customer);
    }
}
