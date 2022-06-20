package com.example.woowa.owner.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "owner")
@Entity
public class Owner {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    @Column(name = "login_id", unique = true, nullable = false, updatable = false, length = 45)
    private String loginId;

    @Column(name = "login_password", nullable = false, length = 45)
    private String loginPassword;

    @Column(nullable = false, length = 45)
    private String name;

    @Column(name = "phone_number", nullable = false, length = 45)
    private String phoneNumber;

    public Owner(String loginId, String loginPassword, String name, String phoneNumber) {
        this.loginId = loginId;
        this.loginPassword = loginPassword;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public void changePassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public void changePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
