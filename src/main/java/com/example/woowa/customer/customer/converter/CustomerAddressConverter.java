package com.example.woowa.customer.customer.converter;

import com.example.woowa.customer.customer.dto.CustomerAddressCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerAddressFindResponse;
import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.entity.CustomerAddress;

public class CustomerAddressConverter {
    public static CustomerAddressFindResponse toCustomerAddressDto(CustomerAddress customerAddress) {
        return new CustomerAddressFindResponse(customerAddress.getId(), customerAddress.getAddress(), customerAddress.getNickname());
    }

    public static CustomerAddress toCustomerAddress(
        CustomerAddressCreateRequest customerAddressCreateRequest, Customer customer) {
        validateCustomerAddress(customerAddressCreateRequest.getDefaultAddress(), customerAddressCreateRequest.getDetailAddress(),
            customerAddressCreateRequest.getNickname(), customer);
        return new CustomerAddress(customerAddressCreateRequest.getDefaultAddress(), customerAddressCreateRequest.getDetailAddress(),
                customerAddressCreateRequest.getNickname(), customer);
    }

    private static void validateCustomerAddress(String defaultAddress, String detailAddress, String nickname,
        Customer customer) {
        assert !defaultAddress.isBlank();
        assert !detailAddress.isBlank();
        assert !nickname.isBlank();
        assert customer != null;
    }
}
