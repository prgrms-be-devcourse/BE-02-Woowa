package com.example.woowa.order.entity;

import com.example.woowa.customer.entity.Customer;
import com.example.woowa.order.enums.OrderStatus;
import com.example.woowa.order.enums.PaymentType;
import com.example.woowa.restaurant.entity.Restaurant;
import com.example.woowa.voucher.entity.Voucher;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(nullable = false)
    private Integer beforeDiscountTotalPrice;

    @Column(nullable = false)
    private Integer afterDiscountTotalPrice;

    @Column(columnDefinition = "DEFAULT 0")
    private Integer discountPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType paymentType;

    @Column(columnDefinition = "DEFAULT 0")
    private Integer usedPoint;
    private Integer cookingTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    public Order(Voucher voucher, Customer customer, Restaurant restaurant,
            Integer beforeDiscountTotalPrice, Integer afterDiscountTotalPrice,
            Integer discountPrice,
            PaymentType paymentType, Integer usedPoint, OrderStatus orderStatus) {
        this.voucher = voucher;
        this.customer = customer;
        this.restaurant = restaurant;
        this.beforeDiscountTotalPrice = beforeDiscountTotalPrice;
        this.afterDiscountTotalPrice = afterDiscountTotalPrice;
        this.discountPrice = discountPrice;
        this.paymentType = paymentType;
        this.usedPoint = usedPoint;
        this.orderStatus = orderStatus;
    }

    public static Order createOrder(Customer customer, Restaurant restaurant, Voucher voucher,
            List<Cart> carts,
            Integer usedPoint, PaymentType paymentType) {
        customer.usePoint(usedPoint);

        int beforeDiscountTotalPrice = carts.stream()
                .mapToInt(cart -> cart.getMenu().getPrice() * cart.getQuantity())
                .sum();
        int discountPrice = voucher.getDiscountPrice(beforeDiscountTotalPrice);
        int afterDiscountTotalPrice = beforeDiscountTotalPrice - discountPrice;

        return new Order(voucher, customer, restaurant, beforeDiscountTotalPrice,
                afterDiscountTotalPrice,
                discountPrice, paymentType, usedPoint, OrderStatus.PAYMENT_COMPLETED);
    }

    public void acceptOrder(int cookingTime) {
        orderStatus = OrderStatus.ACCEPTED;
        this.cookingTime = cookingTime;
    }

    public void cancelOrder() {
        voucher.cancelUse();
        voucher = null;
        orderStatus = OrderStatus.CANCEL;
    }

}
