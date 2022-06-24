package com.example.woowa.customer.customer.converter;

import static java.util.stream.Collectors.toList;

import com.example.woowa.customer.customer.dto.CreateCustomerDto;
import com.example.woowa.customer.customer.dto.CustomerAddressDto;
import com.example.woowa.customer.customer.dto.CustomerDto;
import com.example.woowa.customer.customer.dto.CustomerGradeDto;
import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.entity.CustomerGrade;
import com.example.woowa.order.review.converter.ReviewConverter;
import com.example.woowa.order.review.dto.ReviewDto;
import com.example.woowa.customer.voucher.converter.VoucherConverter;
import com.example.woowa.customer.voucher.dto.VoucherDto;

import java.util.List;

public class CustomerConverter {

    public static Customer toCustomer(CreateCustomerDto createCustomerDto, CustomerGrade customerGrade) {
        return new Customer(createCustomerDto.getLoginId(), createCustomerDto.getLoginPassword(),
                createCustomerDto.getBirthdate(), customerGrade);
    }

    public static CustomerDto toCustomerDto(Customer customer) {
        CustomerGradeDto customerGradeDto = CustomerGradeConverter.toCustomerGradeDto(customer.getCustomerGrade());
        List<CustomerAddressDto> customerAddressDtoList = customer.getCustomerAddresses().stream().map(CustomerAddressConverter::toCustomerAddressDto).collect(toList());
        List<ReviewDto> reviewDtoList = customer.getReviews().stream().map(ReviewConverter::toReviewDto).collect(toList());
        List<VoucherDto> voucherDtoList = customer.getVouchers().stream().map(VoucherConverter::toVoucherDto).collect(toList());
        return new CustomerDto(customer.getLoginId(), customer.getBirthdate().toString(),
                customer.getOrderPerMonth(), customer.getPoint(), customerGradeDto, reviewDtoList, customerAddressDtoList, voucherDtoList);
    }
}
