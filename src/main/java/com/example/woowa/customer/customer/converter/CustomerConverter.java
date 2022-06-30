package com.example.woowa.customer.customer.converter;

import static java.util.stream.Collectors.toList;

import com.example.woowa.customer.customer.dto.CustomerAddressFindResponse;
import com.example.woowa.customer.customer.dto.CustomerCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerFindResponse;
import com.example.woowa.customer.customer.dto.CustomerGradeFindResponse;
import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.entity.CustomerGrade;
import com.example.woowa.customer.voucher.converter.VoucherConverter;
import com.example.woowa.customer.voucher.dto.VoucherFindResponse;
import com.example.woowa.order.review.converter.ReviewConverter;
import com.example.woowa.order.review.dto.ReviewFindResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CustomerConverter {

    public static Customer toCustomer(CustomerCreateRequest customerCreateRequest, CustomerGrade customerGrade) {
        LocalDate localDate = LocalDate.parse(customerCreateRequest.getBirthdate(), DateTimeFormatter.ISO_DATE);
        Customer customer = new Customer(customerCreateRequest.getLoginId(), customerCreateRequest.getLoginPassword(),
            localDate, customerGrade);
        return customer;
    }
    public static CustomerFindResponse toCustomerDto(Customer customer) {
        CustomerGradeFindResponse customerGradeFindResponse = CustomerGradeConverter.toCustomerGradeDto(customer.getCustomerGrade());
        List<CustomerAddressFindResponse> customerAddressFindResponseList = customer.getCustomerAddresses().stream().map(CustomerAddressConverter::toCustomerAddressDto).collect(toList());
        List<ReviewFindResponse> reviewFindResponseList = customer.getReviews().stream().map(ReviewConverter::toReviewDto).collect(toList());
        List<VoucherFindResponse> voucherFindResponseList = customer.getVouchers().stream().map(VoucherConverter::toVoucherDto).collect(toList());
        return new CustomerFindResponse(customer.getLoginId(), customer.getBirthdate().toString(),
            customer.getOrderPerMonth(), customer.getPoint(), customer.getIsIssued(),
            customerGradeFindResponse);
    }
}
