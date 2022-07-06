package com.example.woowa.restaurant.owner.entity;

import com.example.woowa.common.base.BaseLoginEntity;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "owner")
@Entity
public class Owner extends BaseLoginEntity {

    private static final int LIMIT_OF_OWNERSHIP = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Restaurant> restaurants = new ArrayList<>();

    @Column(nullable = false)
    private Integer numberOfRestaurants;

    @Builder
    public Owner(String loginId, String password, String name, String phoneNumber) {
        super(loginId, password, name, phoneNumber);
        numberOfRestaurants = 0;
    }

    public void increaseNumberOfRestaurant() {
        if (ownTooManyRestaurants())
            throw new RuntimeException("소유한 가게의 수가 " + LIMIT_OF_OWNERSHIP + "개를 초과하였습니다.");
        numberOfRestaurants++;
    }

    public void decreaseNumberOfRestaurant() {
        numberOfRestaurants--;
    }

    public void addRestaurant(Restaurant restaurant) {
        if (ownTooManyRestaurants())
            throw new RuntimeException("가게를 " + LIMIT_OF_OWNERSHIP + "개이상 소유할 수 없습니다.");
        increaseNumberOfRestaurant();
        restaurant.setOwner(this);
    }

    public void removeRestaurant(Restaurant restaurant) {
        decreaseNumberOfRestaurant();
        getRestaurants().remove(restaurant);
    }

    public void changePassword(String loginPassword) {
        super.changePassword(loginPassword);
    }

    private boolean ownTooManyRestaurants() {
        return this.numberOfRestaurants >= LIMIT_OF_OWNERSHIP;
    }

}
