package com.example.woowa.order.order.entity;

import com.example.woowa.common.base.BaseTimeEntity;
import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.delivery.entity.Delivery;
import com.example.woowa.order.order.enums.OrderStatus;
import com.example.woowa.order.order.enums.PaymentType;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import com.example.woowa.customer.voucher.entity.Voucher;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Order extends BaseTimeEntity {

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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Cart> carts = new ArrayList<>();

    @Column(nullable = false)
    private Integer beforeDiscountTotalPrice;

    @Column(nullable = false)
    private Integer afterDiscountTotalPrice;

    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private Integer voucherDiscountPrice;

    @OneToOne(mappedBy = "order")
    private Delivery delivery;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType paymentType;

    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private Integer usedPoint;

    private Integer cookingTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    private Order(Voucher voucher, Customer customer, Restaurant restaurant,
            Integer beforeDiscountTotalPrice, Integer afterDiscountTotalPrice,
            Integer voucherDiscountPrice,
            PaymentType paymentType, Integer usedPoint, OrderStatus orderStatus) {
        this.voucher = voucher;
        this.customer = customer;
        this.restaurant = restaurant;
        this.beforeDiscountTotalPrice = beforeDiscountTotalPrice;
        this.afterDiscountTotalPrice = afterDiscountTotalPrice;
        this.voucherDiscountPrice = voucherDiscountPrice;
        this.paymentType = paymentType;
        this.usedPoint = usedPoint;
        this.orderStatus = orderStatus;
    }

    public static Order createOrder(Customer customer, Restaurant restaurant, Voucher voucher,
            Integer usedPoint, PaymentType paymentType, List<Cart> carts) {

        customer.usePoint(usedPoint);

        int beforeDiscountTotalPrice = carts.stream()
                .mapToInt(cart -> cart.getMenu().getPrice() * cart.getQuantity())
                .sum();
        int voucherDiscountPrice = getVoucherDiscountPrice(voucher, beforeDiscountTotalPrice);
        int afterDiscountTotalPrice = beforeDiscountTotalPrice - voucherDiscountPrice;

        Order order = new Order(voucher, customer, restaurant, beforeDiscountTotalPrice,
                afterDiscountTotalPrice,
                voucherDiscountPrice, paymentType, usedPoint, OrderStatus.PAYMENT_COMPLETED);

        carts.forEach(order::addCart);

        return order;
    }

    public void acceptOrder(int cookingTime) {
        orderStatus = OrderStatus.ACCEPTED;
        this.cookingTime = cookingTime;
    }

    public void cancelOrder() {
        if (Objects.nonNull(voucher)) {
            voucher.cancelUse();
            voucher = null;
        }

        orderStatus = OrderStatus.CANCEL;
    }

    public void addCart(Cart cart) {
        carts.add(cart);
        cart.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    private static int getVoucherDiscountPrice(Voucher voucher, int beforeDiscountTotalPrice) {
        return Objects.isNull(voucher) ? 0 : voucher.getDiscountPrice(beforeDiscountTotalPrice);
    }
}