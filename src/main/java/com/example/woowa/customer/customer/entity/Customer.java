package com.example.woowa.customer.customer.entity;

import static lombok.AccessLevel.PROTECTED;

import com.example.woowa.common.base.BaseLoginEntity;
import com.example.woowa.customer.voucher.entity.Voucher;
import com.example.woowa.order.order.entity.Order;
import com.example.woowa.order.review.entity.Review;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Customer extends BaseLoginEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer orderPerMonth;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer orderPerLastMonth;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isIssued;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer point;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "customer_grade_id", nullable = false)
    private CustomerGrade customerGrade;

    @OneToMany(mappedBy = "customer", orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "customer", orphanRemoval = true)
    private List<CustomerAddress> customerAddresses = new ArrayList<>();

    @OneToMany(orphanRemoval = true)
    private List<Voucher> vouchers = new ArrayList<>();

    @OneToMany(mappedBy = "customer", orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    public Customer(String loginId, String loginPassword, LocalDate birthdate,
        CustomerGrade customerGrade) {
        super(loginId, loginPassword, "", "");
        this.orderPerMonth = 0;
        this.orderPerLastMonth = 0;
        this.isIssued = false;
        this.birthdate = birthdate;
        this.point = 0;
        this.customerGrade = customerGrade;
    }

    public void updateCustomerStatusOnFirstDay() {
        this.orderPerLastMonth = this.orderPerMonth;
        this.orderPerMonth = 0;
        this.isIssued = false;
    }

    public void updateCustomerStatusWhenOrder(int plusPoint) {
        addOrderPerMonth();
        addPoint(plusPoint);
    }

    public void updateCustomerStatusWhenOrderCancel(int minusPoint) {
        minusOrderPerMonth();
        usePoint(minusPoint);
    }

    public void addOrderPerMonth() {
        ++this.orderPerMonth;
    }

    public void minusOrderPerMonth() {
        --this.orderPerMonth;
    }

    public void useMonthlyCoupon() {
        this.isIssued = true;
    }

    public void usePoint(int point) {
        assert point <= this.point;
        this.point -= point;
    }

    public void addPoint(int point) {
        this.point += point;
    }

    public void setCustomerGrade(CustomerGrade customerGrade) {
        this.customerGrade = customerGrade;
    }

    public void setIsIssued(boolean value) {
        this.isIssued = value;
    }

    public void addReview(Review review) {
        this.reviews.add(review);
    }

    public void removeReview(Review review) {
        this.reviews.remove(review);
    }

    public List<CustomerAddress> getCustomerAddresses() {
        List<CustomerAddress> orderCustomerAddresses = this.customerAddresses.stream().filter((e)->e.getRecentOrderAt() != null).sorted(
            Comparator.comparing(CustomerAddress::getRecentOrderAt).reversed()).collect(
            Collectors.toList());
        List<CustomerAddress> notOrderCustomerAddresses = this.customerAddresses.stream().filter((e)->e.getRecentOrderAt() == null).collect(
            Collectors.toList());
        List<CustomerAddress> recentCustomerAddresses = new ArrayList<>();
        recentCustomerAddresses.addAll(orderCustomerAddresses);
        recentCustomerAddresses.addAll(notOrderCustomerAddresses);
        return recentCustomerAddresses;
    }

    public void addCustomerAddress(CustomerAddress customerAddress) {
        this.customerAddresses.add(customerAddress);
    }

    public void removeCustomerAddress(CustomerAddress customerAddress) {
        this.customerAddresses.remove(customerAddress);
    }

    public void addVoucher(Voucher voucher) {
        this.vouchers.add(voucher);
    }

    public void removeVoucher(Voucher voucher) {
        this.vouchers.remove(voucher);
    }

    public void addOrder(Order order) {
        this.customerAddresses.remove(order);
    }
}
