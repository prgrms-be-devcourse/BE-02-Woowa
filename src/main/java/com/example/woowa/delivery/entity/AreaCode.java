package com.example.woowa.delivery.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AreaCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String defaultAddress;

    private boolean isAbolish;

    public AreaCode(String code, String defaultAddress, boolean isAbolish) {
        this.code = code;
        this.defaultAddress = defaultAddress;
        this.isAbolish = isAbolish;
    }
}
