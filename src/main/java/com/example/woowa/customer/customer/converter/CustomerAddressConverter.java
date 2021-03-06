package com.example.woowa.customer.customer.converter;

import com.example.woowa.customer.customer.dto.CustomerAddressCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerAddressFindResponse;
import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.entity.CustomerAddress;
import com.example.woowa.delivery.entity.AreaCode;

public class CustomerAddressConverter {
    public static CustomerAddressFindResponse toCustomerAddressDto(CustomerAddress customerAddress) {
        return new CustomerAddressFindResponse(customerAddress.getId(), customerAddress.getAddress(), customerAddress.getNickname());
    }

    public static CustomerAddress toCustomerAddress(
        AreaCode areaCode, CustomerAddressCreateRequest customerAddressCreateRequest, Customer customer) {
        return new CustomerAddress(areaCode, customerAddressCreateRequest.getDetailAddress(),
            customerAddressCreateRequest.getNickname(), customer);
    }
}
