package com.example.woowa.restaurant.owner.entity;

import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "owner")
@Entity
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Restaurant> restaurants = new ArrayList<>();

    @Column(unique = true, nullable = false, updatable = false, length = 45)
    private String loginId;

    @Column(nullable = false, length = 45)
    private String loginPassword;

    @Column(nullable = false, length = 45)
    private String name;

    @Column(nullable = false, length = 45)
    private String phoneNumber;

    public Owner(String loginId, String loginPassword, String name, String phoneNumber) {
        this.loginId = loginId;
        this.loginPassword = loginPassword;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

//    public void addRestaurant(Restaurant restaurant) {
//        if (restaurant.getOwner() != this) {
//            restaurant.setOwner(this);
//        }
//    }

    public void changeLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
