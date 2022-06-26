package com.example.woowa.customer.customer.entity;

import static lombok.AccessLevel.PROTECTED;

import com.example.woowa.delivery.entity.AreaCode;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
    private String defaultAddress;

    @Column(nullable = false, length = 45)
    private String detailAddress;

    @Column(nullable = false, length = 45)
    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Customer customer;

    @OneToOne(fetch = FetchType.LAZY)
    private AreaCode areaCode;

    public CustomerAddress(String defaultAddress, String detailAddress, String nickname, Customer customer) {
        this.defaultAddress = defaultAddress;
        this.detailAddress = detailAddress;
        this.nickname = nickname;
        this.areaCode = findAreaCode(defaultAddress);
        this.customer = customer;
    }

    //areaCode 서비스를 사용한 변환
    private AreaCode findAreaCode(String defaultAddress) {
        return null;
    }

    public void setAddress(String defaultAddress, String detailAddress) {
        assert !defaultAddress.isBlank();
        assert !detailAddress.isBlank();
        this.defaultAddress = defaultAddress;
        this.detailAddress = detailAddress;
        setAreaCode(findAreaCode(defaultAddress));
    }

    public String getAddress() {
        return defaultAddress + " " + detailAddress;
    }

    private void setAreaCode(AreaCode areaCode) {
        //assert areaCode != null;
        this.areaCode = areaCode;
    }

    public void setNickname(String nickname) {
        assert !nickname.isBlank();
        this.nickname = nickname;
    }
}