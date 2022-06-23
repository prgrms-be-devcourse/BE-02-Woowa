package com.example.woowa.customer.customer.entity;

import static lombok.AccessLevel.PROTECTED;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer_address")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class CustomerAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 45)
    private String address;

    @Column(nullable = false, unique = true, length = 45)
    private String nickname;

    @Column(nullable = false, length = 45)
    private String areaCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Customer customer;

    public CustomerAddress(String address, String nickname, Customer customer) {
        assert !address.isBlank();
        assert !nickname.isBlank();
        //assert ! areaCode.isBlank();
        assert customer != null;
        this.address = address;
        this.nickname = nickname;
        this.areaCode = "mock";
        this.customer = customer;
    }

    public void setAddress(String address) {
        assert !address.isBlank();
        this.address = address;
        setAreaCode("mock");
    }

    public void setNickname(String nickname) {
        assert !nickname.isBlank();
        this.nickname = nickname;
    }

    private void setAreaCode(String areaCode) {
        assert !areaCode.isBlank();
        this.areaCode = areaCode;
    }
}
