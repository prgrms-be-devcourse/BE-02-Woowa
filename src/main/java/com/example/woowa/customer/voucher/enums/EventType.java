package com.example.woowa.customer.voucher.enums;

import com.example.woowa.common.EnumFindable;
import java.util.Arrays;
import java.util.Optional;

public enum EventType implements EnumFindable {
  MONTH("month"),
  SPECIAL("special");

  private final String type;

  EventType(String type) {
    this.type = type;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public String toString() {
    return getType();
  }
}
