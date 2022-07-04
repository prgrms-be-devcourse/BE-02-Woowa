package com.example.woowa.common.base;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
public abstract class BaseLoginEntity extends BaseTimeEntity {

    @Column(unique = true,
        nullable = false,
        updatable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private LocalDateTime lastLoginedAt;

    public BaseLoginEntity(String loginId, String password, String name, String phoneNumber) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.lastLoginedAt = LocalDateTime.now();
    }

    public BaseLoginEntity(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
        this.lastLoginedAt = LocalDateTime.now();
    }

    public void update(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeLastLoginedAt(LocalDateTime lastLoginedAt) {
        this.lastLoginedAt = lastLoginedAt;
    }
}
