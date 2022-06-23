package com.example.woowa.common;

import com.example.woowa.customer.entity.Customer;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "address")
@Getter
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private AreaCode areaCode;

    private String detailAddress;

    private String nickName;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Customer> customerList = new ArrayList<>();

}
