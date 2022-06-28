package com.example.woowa.customer.voucher.enums;

import com.example.woowa.common.EnumFindable;
import java.util.Arrays;
import java.util.Optional;

public enum VoucherType implements EnumFindable {
    FiXED("fixed") {
        @Override
        public boolean isValidAmount(int amount) {
            return amount > 0;
        }

        @Override
        public int discount(int currentPrice, int amount) {
            return (currentPrice - amount);
        }

        @Override
        public boolean isOkayToDiscount(int currentPrice, int amount) {
            return currentPrice >= amount;
        }
    },

    PERCENT("percent") {
        @Override
        public boolean isValidAmount(int percent) {
            return (percent > 0) && (percent <= 100);
        }

        @Override
        public int discount(int currentPrice, int percent) {
            return currentPrice * (100 - percent) / 100;
        }
    };

    private final String type;

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return getType();
    }

    VoucherType(String type) {
        this.type = type;
    }

    public int discount(int currentPrice, int amount) {
        return 0;
    }

    public boolean isValidAmount(int amount) {
        return true;
    }

    public boolean isOkayToDiscount(int currentPrice, int amount) {
        return true;
    }
}