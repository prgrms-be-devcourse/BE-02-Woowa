package com.example.woowa.voucher.enums;

import java.util.Arrays;
import java.util.Optional;

public enum VoucherType {
  FiXED("fixed") {
    @Override
    public boolean isValidAmount(int amount) {
      return amount > 0;
    }

    @Override
    public float discount(int currentPrice, int amount) {
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
      return (percent > 0)  && (percent <= 100);
    }

    @Override
    public float discount(int currentPrice, int percent) {
      return currentPrice * (100 - percent) / 100;
    }
  };

  private final String type;

  VoucherType(String type) {
    this.type = type;
  }

  public float discount(int currentPrice, int amount) {
    return 0;
  }

  public boolean isValidAmount(int amount) {return true;}

  public boolean isOkayToDiscount(int currentPrice, int amount) {
    return true;
  }

  @Override
  public String toString() {
    return type;
  }

  public static VoucherType fromString(String value) throws Exception {
    return Optional.ofNullable(find(value)).orElseThrow(() -> new RuntimeException("not valid voucher type"));
  }

  private static VoucherType find(String value) {
    return Arrays.stream(values()).filter(type -> type.toString().equals(value)).findFirst().orElse(null);
  }
}