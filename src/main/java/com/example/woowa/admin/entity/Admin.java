package com.example.woowa.admin.entity;

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
@Table(name = "admin")
@Entity
public class Admin {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @Column(name = "login_id", unique = true, nullable = false, updatable = false, length = 45)
    private String loginId;

    @Column(name = "login_password", nullable = false, length = 45)
    private String loginPassword;

    public Admin(String loginId, String loginPassword) {
        this.loginId = loginId;
        this.loginPassword = loginPassword;
    }

    public void changePassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

}
