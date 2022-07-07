package com.example.woowa.customer.customer.entity;

import static lombok.AccessLevel.PROTECTED;

import com.example.woowa.common.base.BaseTimeEntity;
import com.example.woowa.delivery.entity.AreaCode;
import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "customer_address")
@Getter
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class CustomerAddress extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 45)
    private String detailAddress;

    @Column(nullable = false, length = 45)
    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Customer customer;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private AreaCode areaCode;

    @Column
    private LocalDateTime recentOrderAt;

    public CustomerAddress(AreaCode areaCode, String detailAddress, String nickname, Customer customer) {
        this.detailAddress = detailAddress;
        this.nickname = nickname;
        this.areaCode = areaCode;
        this.customer = customer;
    }

    public void setNickname(String nickname) {
        assert !nickname.isBlank();
        this.nickname = nickname;
    }

    public void setAddress(AreaCode areaCode, String detailAddress) {
        assert areaCode != null;
        assert !detailAddress.isBlank();
        this.areaCode = areaCode;
        this.detailAddress = detailAddress;
    }

    public String getAddress() {
        return areaCode.getDefaultAddress() + " " + detailAddress;
    }

    //고객의 최근 주문 주소를 정렬하기 위해 최근 업데이트 시간 속성을 참고하기로 했습니다.
    //해당 메소드는 주문을 실행할 때만 호출하는 용도로 사용된다고 예상됩니다.
    public AreaCode getAreaCode() {
        changeRecentOrderAt(LocalDateTime.now());
        return areaCode;
    }

    private void changeRecentOrderAt(LocalDateTime now) {
        this.recentOrderAt = now;
    }
}