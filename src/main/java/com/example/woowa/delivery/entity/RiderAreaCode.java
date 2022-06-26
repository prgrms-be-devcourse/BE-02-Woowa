package com.example.woowa.delivery.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "rider_area_code")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(RiderAreaCodeKey.class)
public class RiderAreaCode {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rider_id", nullable = false)
    private Rider rider;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_code_id", nullable = false)
    private AreaCode areaCode;

    public RiderAreaCode(Rider rider, AreaCode areaCode) {
        this.rider = rider;
        this.areaCode = areaCode;
    }
}
